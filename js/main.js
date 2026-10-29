// Interfaz: conecta los controles con la simulación y el renderer.
// Port de ControlPanel.java / MainPanel.java.

import { Galaxy, MATRIX_SIZE, defaultProperties } from './galaxy.js';
import { computeColors } from './colors.js';
import { createRenderer, replaceCanvas } from './renderer.js';

const MAX_STEPS_PER_FRAME = 10;

const $ = (id) => document.getElementById(id);

const ui = {
  starCount: $('starCount'),
  distribution: $('distribution'),
  initialize: $('initialize'),
  rotate: $('rotate'),
  rotationCount: $('rotationCount'),
  rotationLag: $('rotationLag'),
  grow: $('grow'),
  growthRate: $('growthRate'),
  advanceOne: $('advanceOne'),
  advanceMany: $('advanceMany'),
  advanceManyCount: $('advanceManyCount'),
  start: $('start'),
  stop: $('stop'),
  interval: $('interval'),
  reset: $('reset'),
  zoom: $('zoom'),
  constantSize: $('constantSize'),
  infoStarCount: $('infoStarCount'),
  infoGeneration: $('infoGeneration'),
  infoRenderer: $('infoRenderer'),
  canvas: $('galaxy'),
  error: $('error'),
};

const galaxy = new Galaxy();

const view = {
  zoom: 1, // 1 = la grilla de 200 unidades ocupa el lado menor del canvas
  constantSize: false,
  interval: 20, // ms entre generaciones al avanzar indefinidamente
  advanceMany: 5,
};

let renderer = null;
let colors = new Uint8Array(0);

// --- Dibujo -----------------------------------------------------------------

function draw() {
  if (!renderer) return;
  const { stars } = galaxy;
  if (colors.length < stars.count * 4) colors = new Uint8Array(stars.capacity * 4);
  computeColors(stars, colors);

  const { width, height } = ui.canvas;
  const dpr = window.devicePixelRatio || 1;
  const scale = (Math.min(width, height) / MATRIX_SIZE) * view.zoom;
  const size = view.constantSize ? Math.max(1, Math.round(dpr)) : Math.max(1, Math.round(scale));

  renderer.render(stars.xy, colors, stars.count, { scale, size });

  ui.infoStarCount.textContent = stars.count.toLocaleString('es');
  ui.infoGeneration.textContent = galaxy.generation.toLocaleString('es');
}

function resizeCanvas() {
  const dpr = window.devicePixelRatio || 1;
  const width = Math.max(1, Math.round(ui.canvas.clientWidth * dpr));
  const height = Math.max(1, Math.round(ui.canvas.clientHeight * dpr));
  if (ui.canvas.width !== width || ui.canvas.height !== height) {
    ui.canvas.width = width;
    ui.canvas.height = height;
  }
  draw();
}

// --- Bucle "avanzar indefinidamente" ------------------------------------------

let running = false;
let lastTime = 0;
let carry = 0;

function loop(now) {
  if (!running) return;
  carry += now - lastTime;
  lastTime = now;

  const steps = Math.min(Math.floor(carry / view.interval), MAX_STEPS_PER_FRAME);
  carry = steps === MAX_STEPS_PER_FRAME ? 0 : carry - steps * view.interval;
  for (let i = 0; i < steps; i++) galaxy.step();

  draw();
  requestAnimationFrame(loop);
}

function start() {
  if (running) return;
  running = true;
  lastTime = performance.now();
  carry = view.interval; // la primera generación sale en el primer cuadro
  ui.start.setAttribute('aria-pressed', 'true');
  requestAnimationFrame(loop);
}

function stop() {
  running = false;
  ui.start.setAttribute('aria-pressed', 'false');
}

// --- Controles ----------------------------------------------------------------

function clampInput(input) {
  const min = Number(input.min);
  const max = Number(input.max);
  let value = Number(input.value);
  if (!Number.isFinite(value)) value = min;
  value = Math.min(Math.max(value, min), max);
  input.value = value;
  return value;
}

function bindNumber(input, onChange) {
  input.addEventListener('change', () => onChange(clampInput(input)));
  input.addEventListener('input', () => {
    const value = Number(input.value);
    if (Number.isFinite(value) && value >= Number(input.min) && value <= Number(input.max)) onChange(value);
  });
}

function bindCheckbox(input, onChange) {
  input.addEventListener('change', () => onChange(input.checked));
}

function applyDefaults() {
  Object.assign(galaxy.properties, defaultProperties());
  view.zoom = 1;
  view.constantSize = false;
  view.interval = 20;
  view.advanceMany = 5;

  const p = galaxy.properties;
  ui.starCount.value = p.initialStarCount;
  ui.distribution.value = p.distribution;
  ui.rotate.checked = p.rotate;
  ui.rotationCount.value = p.rotationCount;
  ui.rotationLag.value = p.rotationLag;
  ui.grow.checked = p.grow;
  ui.growthRate.value = p.growthRate;
  ui.advanceManyCount.value = view.advanceMany;
  ui.interval.value = view.interval;
  ui.zoom.value = Math.round(view.zoom * 100);
  ui.constantSize.checked = view.constantSize;
}

function bindControls() {
  const p = galaxy.properties;

  bindNumber(ui.starCount, (v) => { p.initialStarCount = v; });
  ui.distribution.addEventListener('change', () => { p.distribution = ui.distribution.value; });
  ui.initialize.addEventListener('click', () => { galaxy.initialize(); draw(); });

  bindCheckbox(ui.rotate, (v) => { p.rotate = v; });
  bindNumber(ui.rotationCount, (v) => { p.rotationCount = v; });
  bindNumber(ui.rotationLag, (v) => { p.rotationLag = v; });

  bindCheckbox(ui.grow, (v) => { p.grow = v; });
  bindNumber(ui.growthRate, (v) => { p.growthRate = v; });

  ui.advanceOne.addEventListener('click', () => { galaxy.step(); draw(); });
  bindNumber(ui.advanceManyCount, (v) => { view.advanceMany = v; });
  ui.advanceMany.addEventListener('click', () => {
    for (let i = 0; i < view.advanceMany; i++) galaxy.step();
    draw();
  });
  ui.start.addEventListener('click', start);
  ui.stop.addEventListener('click', stop);
  bindNumber(ui.interval, (v) => { view.interval = v; });
  ui.reset.addEventListener('click', () => { applyDefaults(); draw(); });

  bindNumber(ui.zoom, (v) => { view.zoom = v / 100; draw(); });
  bindCheckbox(ui.constantSize, (v) => { view.constantSize = v; draw(); });
}

// --- Arranque -----------------------------------------------------------------

const resizeObserver = new ResizeObserver(resizeCanvas);

async function setUpRenderer(preferred) {
  try {
    renderer = await createRenderer(ui.canvas, preferred, {
      // Si el navegador pierde el dispositivo WebGPU, seguimos con WebGL2.
      onLost: (info) => {
        console.warn('Dispositivo WebGPU perdido, pasando a WebGL2:', info.message);
        renderer = null;
        resizeObserver.disconnect();
        ui.canvas = replaceCanvas(ui.canvas);
        setUpRenderer('webgl2');
      },
    });
  } catch (error) {
    ui.infoRenderer.textContent = 'no disponible';
    ui.error.textContent = error.message;
    ui.error.hidden = false;
    console.error(error);
    return;
  }
  ui.canvas = renderer.canvas;
  ui.infoRenderer.textContent = renderer.name;
  resizeObserver.observe(ui.canvas);
  resizeCanvas();
}

async function main() {
  applyDefaults();
  bindControls();
  galaxy.initialize();
  await setUpRenderer(new URLSearchParams(location.search).get('renderer') ?? 'auto');
}

main();
