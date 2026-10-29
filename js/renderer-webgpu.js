// Renderer WebGPU: un cuadrado por estrella, dibujado con instancing.

const SHADER = /* wgsl */ `
struct Uniforms {
  resolution: vec2f, // tamaño del canvas en píxeles
  scale: f32,        // píxeles por unidad de galaxia
  size: f32,         // lado de cada estrella en píxeles
};

@group(0) @binding(0) var<uniform> u: Uniforms;

struct VertexOut {
  @builtin(position) position: vec4f,
  @location(0) color: vec4f,
};

const corners = array<vec2f, 6>(
  vec2f(0, 0), vec2f(1, 0), vec2f(0, 1),
  vec2f(0, 1), vec2f(1, 0), vec2f(1, 1),
);

@vertex
fn vs(
  @builtin(vertex_index) vertexIndex: u32,
  @location(0) starXY: vec2f,
  @location(1) starColor: vec4f,
) -> VertexOut {
  let origin = floor(starXY * u.scale + u.resolution * 0.5);
  let px = origin + corners[vertexIndex] * u.size;
  let clip = px / u.resolution * 2.0 - 1.0;
  var out: VertexOut;
  out.position = vec4f(clip.x, -clip.y, 0.0, 1.0);
  out.color = starColor;
  return out;
}

@fragment
fn fs(in: VertexOut) -> @location(0) vec4f {
  return in.color;
}`;

export async function createWebGPURenderer(canvas, { onLost } = {}) {
  if (!navigator.gpu) return null;
  const adapter = await navigator.gpu.requestAdapter();
  if (!adapter) return null;
  const device = await adapter.requestDevice();
  let ready = false; // avisamos de pérdidas solo una vez entregado el renderer
  device.lost.then((info) => {
    if (ready && info.reason !== 'destroyed') onLost?.(info);
  });

  // Pedimos el contexto recién al final: un canvas que ya tiene contexto
  // WebGPU no puede volver a usarse con WebGL2 si algo fallara después.
  const context = canvas.getContext('webgpu');
  if (!context) return null;
  const format = navigator.gpu.getPreferredCanvasFormat();
  context.configure({
    device,
    format,
    alphaMode: 'opaque',
    usage: GPUTextureUsage.RENDER_ATTACHMENT | GPUTextureUsage.COPY_SRC,
  });

  const module = device.createShaderModule({ code: SHADER });
  const pipeline = device.createRenderPipeline({
    layout: 'auto',
    vertex: {
      module,
      entryPoint: 'vs',
      buffers: [
        { arrayStride: 8, stepMode: 'instance', attributes: [{ shaderLocation: 0, offset: 0, format: 'float32x2' }] },
        { arrayStride: 4, stepMode: 'instance', attributes: [{ shaderLocation: 1, offset: 0, format: 'unorm8x4' }] },
      ],
    },
    fragment: { module, entryPoint: 'fs', targets: [{ format }] },
    primitive: { topology: 'triangle-list' },
  });

  const uniformData = new Float32Array(4);
  const uniformBuffer = device.createBuffer({
    size: uniformData.byteLength,
    usage: GPUBufferUsage.UNIFORM | GPUBufferUsage.COPY_DST,
  });
  const bindGroup = device.createBindGroup({
    layout: pipeline.getBindGroupLayout(0),
    entries: [{ binding: 0, resource: { buffer: uniformBuffer } }],
  });

  let capacity = 0;
  let positionBuffer = null;
  let colorBuffer = null;

  function ensureCapacity(count) {
    if (count <= capacity) return;
    capacity = Math.max(1024, capacity * 2, count);
    positionBuffer?.destroy();
    colorBuffer?.destroy();
    positionBuffer = device.createBuffer({
      size: capacity * 8,
      usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
    });
    colorBuffer = device.createBuffer({
      size: capacity * 4,
      usage: GPUBufferUsage.VERTEX | GPUBufferUsage.COPY_DST,
    });
  }

  const renderer = {
    name: 'WebGPU',
    canvas,
    device,

    render(xy, colors, count, { scale, size }) {
      uniformData.set([canvas.width, canvas.height, scale, size]);
      device.queue.writeBuffer(uniformBuffer, 0, uniformData);

      if (count > 0) {
        ensureCapacity(count);
        device.queue.writeBuffer(positionBuffer, 0, xy, 0, count * 2);
        device.queue.writeBuffer(colorBuffer, 0, colors, 0, count * 4);
      }

      const encoder = device.createCommandEncoder();
      const pass = encoder.beginRenderPass({
        colorAttachments: [{
          view: context.getCurrentTexture().createView(),
          clearValue: { r: 0, g: 0, b: 0, a: 1 },
          loadOp: 'clear',
          storeOp: 'store',
        }],
      });
      if (count > 0) {
        pass.setPipeline(pipeline);
        pass.setBindGroup(0, bindGroup);
        pass.setVertexBuffer(0, positionBuffer);
        pass.setVertexBuffer(1, colorBuffer);
        pass.draw(6, count);
      }
      pass.end();
      device.queue.submit([encoder.finish()]);
    },
  };

  // Cuadro de prueba: si la implementación falla al dibujar al canvas,
  // lo detectamos acá y el llamador puede caer a WebGL2.
  device.pushErrorScope('validation');
  renderer.render(new Float32Array(0), new Uint8Array(0), 0, { scale: 1, size: 1 });
  const error = await device.popErrorScope();
  if (error) throw new Error(`WebGPU falló al dibujar: ${error.message}`);

  ready = true;
  return renderer;
}
