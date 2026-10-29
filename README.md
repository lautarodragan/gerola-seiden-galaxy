# gerola-seiden-galaxy

Simulación de galaxias espirales con el autómata de Gerola-Seiden.

Fue un trabajo práctico para la secundaria, hecho en 2006 en Java (applet + Swing).
En 2026 lo porté a HTML, CSS y JavaScript moderno, sin dependencias ni proceso de
compilación, dibujando con WebGPU (o WebGL2 si el navegador no tiene WebGPU).

Versión original: http://msthompson.edu.ar/mst/data/tpsimula/galaxia/index.php

## Cómo funciona

Cada estrella tiene una posición y una "vida". En cada generación:

- **Rotar**: cada estrella gira alrededor del centro un ángulo fijo
  (`movimiento angular / 100 · 2π`) más un término que depende del radio
  (`desfase radial / 10 / radio`). Como las estrellas interiores giran más rápido,
  las estructuras se estiran en brazos espirales.
- **Crecer**: cada estrella pierde media unidad de vida. Al llegar a cero muere y
  explota: hace 8 intentos de encender una estrella nueva a su alrededor, cada uno
  con probabilidad `factor de crecimiento / 1000`.

El color de cada estrella depende de su vida y de cuántas estrellas comparten su
celda de la grilla de 200×200: las zonas densas tienden al blanco.

## Estructura

```
index.html            página y controles
style.css             estilos
js/main.js            conecta los controles con la simulación y el renderer
js/galaxy.js          la simulación (port de Galaxy.java)
js/colors.js          color de cada estrella (port de GalaxyPanel.java)
js/renderer.js        elige WebGPU o WebGL2
js/renderer-webgpu.js renderer WebGPU
js/renderer-webgl2.js renderer WebGL2
src/                  el código Java original de 2006, sin tocar
```

Se puede forzar un renderer con `?renderer=webgpu` o `?renderer=webgl2` en la URL.

## Correrlo localmente

Los módulos ES no cargan desde `file://`, así que hace falta servir la carpeta por
HTTP. Con Python, desde la raíz del repo:

```nu
python3 -m http.server 8000
start http://localhost:8000
```

## Publicarlo en GitHub Pages

El sitio son archivos estáticos en la raíz del repo, así que no hace falta
ningún workflow: en GitHub, **Settings → Pages → Build and deployment**, elegir
**Deploy from a branch**, la rama que corresponda y la carpeta `/ (root)`.

El archivo `.nojekyll` en la raíz le dice a GitHub Pages que no pase los
archivos por Jekyll y los sirva tal cual.
