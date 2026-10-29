// Renderer WebGL2: un cuadrado por estrella, dibujado con instancing.

const VERTEX_SHADER = `#version 300 es
layout(location = 0) in vec2 corner;     // esquina del cuadrado unitario (0..1)
layout(location = 1) in vec2 starXY;     // posición en unidades de la galaxia
layout(location = 2) in vec4 starColor;  // RGBA normalizado

uniform vec2 uResolution; // tamaño del canvas en píxeles
uniform float uScale;     // píxeles por unidad de galaxia
uniform float uSize;      // lado de cada estrella en píxeles

out vec4 vColor;

void main() {
  vec2 origin = floor(starXY * uScale + uResolution * 0.5);
  vec2 px = origin + corner * uSize;
  vec2 clip = px / uResolution * 2.0 - 1.0;
  gl_Position = vec4(clip.x, -clip.y, 0.0, 1.0);
  vColor = starColor;
}`;

const FRAGMENT_SHADER = `#version 300 es
precision mediump float;
in vec4 vColor;
out vec4 outColor;
void main() {
  outColor = vColor;
}`;

const QUAD = new Float32Array([0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1]);

function compile(gl, type, source) {
  const shader = gl.createShader(type);
  gl.shaderSource(shader, source);
  gl.compileShader(shader);
  if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
    throw new Error(`Error compilando shader: ${gl.getShaderInfoLog(shader)}`);
  }
  return shader;
}

export function createWebGL2Renderer(canvas) {
  const gl = canvas.getContext('webgl2', { antialias: false, alpha: false });
  if (!gl) return null;

  const program = gl.createProgram();
  gl.attachShader(program, compile(gl, gl.VERTEX_SHADER, VERTEX_SHADER));
  gl.attachShader(program, compile(gl, gl.FRAGMENT_SHADER, FRAGMENT_SHADER));
  gl.linkProgram(program);
  if (!gl.getProgramParameter(program, gl.LINK_STATUS)) {
    throw new Error(`Error enlazando programa: ${gl.getProgramInfoLog(program)}`);
  }

  const uniforms = {
    resolution: gl.getUniformLocation(program, 'uResolution'),
    scale: gl.getUniformLocation(program, 'uScale'),
    size: gl.getUniformLocation(program, 'uSize'),
  };

  const vao = gl.createVertexArray();
  gl.bindVertexArray(vao);

  const quadBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, quadBuffer);
  gl.bufferData(gl.ARRAY_BUFFER, QUAD, gl.STATIC_DRAW);
  gl.enableVertexAttribArray(0);
  gl.vertexAttribPointer(0, 2, gl.FLOAT, false, 0, 0);

  const positionBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer);
  gl.enableVertexAttribArray(1);
  gl.vertexAttribPointer(1, 2, gl.FLOAT, false, 0, 0);
  gl.vertexAttribDivisor(1, 1);

  const colorBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer);
  gl.enableVertexAttribArray(2);
  gl.vertexAttribPointer(2, 4, gl.UNSIGNED_BYTE, true, 0, 0);
  gl.vertexAttribDivisor(2, 1);

  gl.bindVertexArray(null);
  gl.clearColor(0, 0, 0, 1);

  return {
    name: 'WebGL2',
    canvas,

    /**
     * @param {Float32Array} xy posiciones intercaladas
     * @param {Uint8Array} colors RGBA por estrella
     * @param {number} count cantidad de estrellas
     * @param {{ scale: number, size: number }} view
     */
    render(xy, colors, count, { scale, size }) {
      gl.viewport(0, 0, canvas.width, canvas.height);
      gl.clear(gl.COLOR_BUFFER_BIT);
      if (count === 0) return;

      gl.useProgram(program);
      gl.uniform2f(uniforms.resolution, canvas.width, canvas.height);
      gl.uniform1f(uniforms.scale, scale);
      gl.uniform1f(uniforms.size, size);

      gl.bindVertexArray(vao);
      gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer);
      gl.bufferData(gl.ARRAY_BUFFER, xy.subarray(0, count * 2), gl.DYNAMIC_DRAW);
      gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer);
      gl.bufferData(gl.ARRAY_BUFFER, colors.subarray(0, count * 4), gl.DYNAMIC_DRAW);

      gl.drawArraysInstanced(gl.TRIANGLES, 0, 6, count);
      gl.bindVertexArray(null);
    },
  };
}
