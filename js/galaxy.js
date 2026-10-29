// Simulación: autómata de Gerola-Seiden para galaxias espirales.
// Port del paquete `galaxia` (Galaxy.java, Star.java, GalaxyProperties.java).

export const MATRIX_SIZE = 200;

export const Distribution = Object.freeze({
  Uniform: 'uniform',
  Angular: 'angular',
  Zone: 'zone',
});

export function defaultProperties() {
  return {
    initialStarCount: 5000,
    distribution: Distribution.Uniform,
    rotate: true,
    rotationCount: 5, // movimiento angular: (valor / 100) · 2π por generación
    rotationLag: 3, // desfase radial: (valor / 10) / radio
    grow: true,
    growthRate: 125, // probabilidad de nacimiento por intento, en milésimas
  };
}

const TAU = Math.PI * 2;
const randomLife = () => Math.floor(Math.random() * 8) + 2;

/**
 * Lista de estrellas en arrays tipados (structure of arrays).
 * `xy` está intercalado [x0, y0, x1, y1, ...] para subirlo a la GPU tal cual.
 */
export class StarBuffer {
  constructor(capacity = 1024) {
    this.count = 0;
    this.xy = new Float32Array(capacity * 2);
    this.life = new Float32Array(capacity);
  }

  get capacity() {
    return this.life.length;
  }

  clear() {
    this.count = 0;
  }

  push(x, y, life) {
    if (this.count === this.capacity) this.#grow();
    this.xy[this.count * 2] = x;
    this.xy[this.count * 2 + 1] = y;
    this.life[this.count] = life;
    this.count++;
  }

  #grow() {
    const capacity = Math.max(1024, this.capacity * 2);
    const xy = new Float32Array(capacity * 2);
    const life = new Float32Array(capacity);
    xy.set(this.xy);
    life.set(this.life);
    this.xy = xy;
    this.life = life;
  }
}

function addDiskStars(stars, count, angleFrom, angleSpan) {
  for (let i = 0; i < count; i++) {
    const angle = angleFrom + Math.random() * angleSpan;
    const radius = 3 + Math.random() * 85;
    stars.push(Math.cos(angle) * radius, Math.sin(angle) * radius, randomLife());
  }
}

function addZoneStars(stars, count) {
  const groupCount = Math.round(10 + Math.random() * 5);
  const groupRadius = 20;
  const starsPerGroup = Math.round(count / groupCount);
  const maxCenterRadius = MATRIX_SIZE / 2 - groupRadius - 10;

  for (let g = 0; g < groupCount; g++) {
    let cx, cy;
    do {
      cx = Math.random() * MATRIX_SIZE - MATRIX_SIZE / 2;
      cy = Math.random() * MATRIX_SIZE - MATRIX_SIZE / 2;
    } while (Math.hypot(cx, cy) > maxCenterRadius);

    for (let i = 0; i < starsPerGroup; i++) {
      const angle = Math.random() * TAU;
      const radius = Math.random() * groupRadius;
      stars.push(cx + Math.cos(angle) * radius, cy + Math.sin(angle) * radius, randomLife());
    }
  }
}

export class Galaxy {
  constructor(properties = defaultProperties()) {
    this.properties = properties;
    this.generation = 0;
    this.stars = new StarBuffer();
    this.#spare = new StarBuffer();
  }

  #spare;

  initialize() {
    const { initialStarCount: n, distribution } = this.properties;
    const stars = this.stars;
    stars.clear();
    this.generation = 0;

    switch (distribution) {
      case Distribution.Angular:
        addDiskStars(stars, Math.round(n * 0.4), 0, TAU);
        addDiskStars(stars, Math.round(n * 0.3), 0, Math.PI / 6);
        addDiskStars(stars, Math.round(n * 0.3), Math.PI, Math.PI / 6);
        break;
      case Distribution.Zone:
        addDiskStars(stars, Math.round(n * 0.4), 0, TAU);
        addZoneStars(stars, Math.round(n * 0.6));
        break;
      default:
        addDiskStars(stars, n, 0, TAU);
    }
  }

  /** Rotación diferencial: las estrellas interiores giran más rápido que las exteriores. */
  rotate() {
    const { rotationCount, rotationLag } = this.properties;
    const baseStep = (TAU * rotationCount) / 100;
    const { xy, count } = this.stars;

    for (let i = 0; i < count; i++) {
      const x = xy[i * 2];
      const y = xy[i * 2 + 1];
      const radius = Math.hypot(x, y);
      if (radius === 0) continue;
      const angle = Math.atan2(y, x) + baseStep + rotationLag / 10 / radius;
      xy[i * 2] = Math.cos(angle) * radius;
      xy[i * 2 + 1] = Math.sin(angle) * radius;
    }
  }

  /**
   * Envejecimiento: cada estrella pierde media unidad de vida por generación.
   * Al morir, explota y puede encender hasta 8 estrellas nuevas a su alrededor.
   */
  grow() {
    const birthOdds = this.properties.growthRate / 1000;
    const current = this.stars;
    const next = this.#spare;
    next.clear();

    for (let i = 0; i < current.count; i++) {
      const x = current.xy[i * 2];
      const y = current.xy[i * 2 + 1];
      const life = current.life[i] - 0.5;

      if (life > 0) {
        next.push(x, y, life);
        continue;
      }

      for (let attempt = 0; attempt < 8; attempt++) {
        if (Math.random() < birthOdds) {
          next.push(x + Math.random() * 2 - 1, y + Math.random() * 2 - 1, 5 + Math.floor(Math.random() * 5));
        }
      }
    }

    this.stars = next;
    this.#spare = current;
  }

  step() {
    if (this.properties.rotate) this.rotate();
    if (this.properties.grow) this.grow();
    this.generation++;
  }
}
