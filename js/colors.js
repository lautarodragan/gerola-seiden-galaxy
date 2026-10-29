// Color de cada estrella (port de GalaxyPanel.paintComponent).
// Base azulada (0, 20, 50) más vida · 10 · densidad, donde densidad es cuántas
// estrellas van cayendo en la misma celda de la grilla 200×200. Las zonas
// densas se van hacia el blanco.

import { MATRIX_SIZE } from './galaxy.js';

const BASE = [0, 20, 50];
const HALF = MATRIX_SIZE / 2;
const cells = new Uint16Array(MATRIX_SIZE * MATRIX_SIZE);

/**
 * Escribe un color RGBA (bytes) por estrella en `out`.
 * @param {import('./galaxy.js').StarBuffer} stars
 * @param {Uint8Array} out con capacidad para `stars.count * 4` bytes
 */
export function computeColors(stars, out) {
  const { xy, life, count } = stars;
  cells.fill(0);

  for (let i = 0; i < count; i++) {
    const cx = Math.trunc(xy[i * 2]) + HALF;
    const cy = Math.trunc(xy[i * 2 + 1]) + HALF;
    let density = 1;
    if (cx >= 0 && cx < MATRIX_SIZE && cy >= 0 && cy < MATRIX_SIZE) {
      density = ++cells[cx * MATRIX_SIZE + cy];
    }

    const brightness = life[i] > 0 ? life[i] * 10 * density : -Infinity;
    out[i * 4] = Math.min(BASE[0] + brightness, 255);
    out[i * 4 + 1] = Math.min(BASE[1] + brightness, 255);
    out[i * 4 + 2] = Math.min(BASE[2] + brightness, 255);
    out[i * 4 + 3] = 255;
  }
}
