// Elige el renderer: WebGPU si está disponible, si no WebGL2.
// Se puede forzar uno con `?renderer=webgl2` o `?renderer=webgpu` en la URL.

import { createWebGL2Renderer } from './renderer-webgl2.js';
import { createWebGPURenderer } from './renderer-webgpu.js';

/**
 * Un canvas que ya entregó un contexto WebGPU no puede dar uno WebGL2,
 * así que para caer de uno al otro lo reemplazamos por un clon limpio.
 */
export function replaceCanvas(canvas) {
  const fresh = canvas.cloneNode(false);
  canvas.replaceWith(fresh);
  return fresh;
}

/**
 * @param {HTMLCanvasElement} canvas
 * @param {'auto' | 'webgpu' | 'webgl2'} preferred
 * @param {{ onLost?: (info: GPUDeviceLostInfo) => void }} options
 * @returns {Promise<{ name: string, canvas: HTMLCanvasElement, render: Function }>}
 */
export async function createRenderer(canvas, preferred = 'auto', options = {}) {
  if (preferred !== 'webgl2') {
    try {
      const renderer = await createWebGPURenderer(canvas, options);
      if (renderer) return renderer;
    } catch (error) {
      console.warn('WebGPU no disponible, probando WebGL2:', error);
      if (canvas.getContext('webgpu')) canvas = replaceCanvas(canvas);
    }
    if (preferred === 'webgpu') throw new Error('WebGPU no está disponible en este navegador.');
  }

  const renderer = createWebGL2Renderer(canvas);
  if (renderer) return renderer;
  throw new Error('Este navegador no soporta ni WebGPU ni WebGL2.');
}
