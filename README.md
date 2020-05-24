# algorithmic clichés

*algorithmic clichés* are a collection of *algorithmic clichés* and examples.

suggestions for *algorithmic clichés* can be contributed via [issues tracking](https://github.com/dennisppaul/algorithmic-cliches/issues) and will be reviewed by the *Cliche Review Board* (CRB).

> "A cliché […] is an expression, idea, or element of an artistic work that has become overused to the point of losing its original meaning or effect, even to the point of being trite or irritating, especially when at some earlier time it was considered meaningful or novel.[1]" from [Wikipedia(EN) Cliché](https://en.wikipedia.org/wiki/Clich%C3%A9)

despite todays negative connotation of the term `cliché` this collection tries to advertise the intrinsic beauty of the collected examples, demistifying their technical complexity by making them accessible as open source code, and thereby encouraging the *intelligent, interesting, and relevant* use of these clichés, quite aware of the ambiguous endeavor.

yours faithfully   
CRB 

## installation

- download lastest release [`algorithmiccliches.zip`](https://github.com/dennisppaul/algorithmic-cliches/releases/latest/download/algorithmiccliches.zip)
- unpack `algorithmiccliches.zip` in processing library folder ( e.g `$HOME/Documents/Processing/libraries` on MacOS )
- install `qhull`
    - on macOS `qhull` is available via [Homebrew](http://brew.sh) install with `$ brew install qhull`
- run *examples*

## used libraries

- [Processing.org](http://www.processing.org/) ( updated to version 3.0++ ) including _PDF_, _DXF_, and _Minim_ libraries
- [Minim](http://code.compartmental.net/minim/)
- [Qhull](http://www.qhull.org/) for ( 2D + ) 3D voronoi diagrams 
- [QuickHull3D](http://www.cs.ubc.ca/~lloyd/java/quickhull3d.html) for 3D convex hulls in 3D voronoi
- [Gewebe](https://github.com/dennisppaul/gewebe/)
- [Teilchen](https://github.com/dennisppaul/teilchen)

## available algorithmic clichés

### cellular automaton

[![GameOfLife2](./images/sketches/CellularAutomataGameOfLife2.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchCellularAutomataGameOfLife2.java)

[![GameOfLife3](./images/sketches/CellularAutomataGameOfLife3.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchCellularAutomataGameOfLife3.java)

[Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) + [Cellular automaton](https://en.wikipedia.org/wiki/Cellular_automaton) @wikipedia

### delaunay triangulation

[![DelaunayTriangulation2](./images/sketches/DelaunayTriangulation2.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchDelaunayTriangulation2.java)

[Delaunay triangulation](https://en.wikipedia.org/wiki/Delaunay_triangulation) @wikipedia

### convex hull

[![ConvexHull3](./images/sketches/ConvexHull3.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchConvexHull3.java)

[Convex hull](http://en.wikipedia.org/wiki/Convex_hull) @wikipedia

### iso surfaces

[![IsoSurface2MetaCircle](./images/sketches/IsoSurface2MetaCircle.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchIsoSurface2MetaCircle.java)

[![IsoSurface2ExtractBlobs](./images/sketches/IsoSurface2ExtractBlobs.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchIsoSurface2ExtractBlobs.java)

[![IsoSurface3MetaBall](./images/sketches/IsoSurface3MetaBall.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchIsoSurface3MetaBall.java)

[Isosurface](https://en.wikipedia.org/wiki/Isosurface) + [Marching squares](https://en.wikipedia.org/wiki/Marching_squares) + [Marching cubes](https://en.wikipedia.org/wiki/Marching_cubes) @wikipedia

### voronoi diagram

[![Voronoi2](./images/sketches/Voronoi2.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchVoronoi2.java)

[![Voronoi2RoundRegions](./images/sketches/Voronoi2RoundRegions.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchVoronoi2RoundRegions.java)

[![Voronoi3](./images/sketches/Voronoi3.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchVoronoi3.java)

[Voronoi diagram](https://en.wikipedia.org/wiki/Voronoi_diagram) @wikipedia

### alpha shapes

[![AlphaShape3](./images/sketches/AlphaShape3.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchAlphaShape3.java)

[Alpha shape](https://en.wikipedia.org/wiki/Alpha_shape) @wikipedia

### diffusion-limited aggregation

[![DiffusionLimitedAggregation](./images/sketches/DiffusionLimitedAggregation.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchDiffusionLimitedAggregation.java)

[![DiffusionLimitedAggregationWithIsoSurfaces](./images/sketches/DiffusionLimitedAggregationWithIsoSurfaces.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchDiffusionLimitedAggregationWithIsoSurfaces.java)

[Diffusion-limited aggregation](https://en.wikipedia.org/wiki/Diffusion-limited_aggregation) @wikipedia

### fast fourier transform

[![FFTSimple](./images/sketches/FFTSimple.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchFFTSimple.java)

[![FFTLandscape](./images/sketches/FFTLandscape.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchFFTLandscape.java)

[Fast Fourier transform](https://en.wikipedia.org/wiki/Fast_Fourier_transform) @wikipedia

### agents

[![Step07_IntroducingTime](./images/sketches/Agents.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchAgents_Step07_IntroducingTime.java)

[Agent-based model](https://en.wikipedia.org/wiki/Agent-based_model) @wikipedia

### flocking

[![Flocking2](./images/sketches/Flocking2.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchFlocking2.java)

[![Flocking3](./images/sketches/Flocking3.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchFlocking3.java)

[Flocking (behavior)](http://en.wikipedia.org/wiki/Flocking_(behavior)) @wikipedia

### flow fields

[![FlowFields](./images/sketches/FlowFields.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchFlowFields.java)

[Vector field](https://en.wikipedia.org/wiki/Vector_field) @wikipedia

### perlin noise

[![PerlinNoise](./images/sketches/PerlinNoise.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchPerlinNoise.java)

[![PerlinNoiseWaterSurface](./images/sketches/PerlinNoiseWaterSurface.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchPerlinNoiseWaterSurface.java)

[Perlin noise](https://en.wikipedia.org/wiki/Perlin_noise) @wikipedia

### fluid dynamics

[![FluidDynamics2](./images/sketches/FluidDynamics2.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchFluidDynamics2.java)

[![FluidDynamicsWaterColumns](./images/sketches/FluidDynamicsWaterColumns.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchFluidDynamicsWaterColumns.java)

[Fluid dynamics](https://en.wikipedia.org/wiki/Fluid_dynamics) @wikipedia

### laser line

[![LaserLine](./images/sketches/LaserLine.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchLaserLine.java)

### lindenmayer system

[![LindenmayerSystemBasic](./images/sketches/LindenmayerSystemBasic.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchLindenmayerSystemBasic.java)

[Lindenmayer system](https://en.wikipedia.org/wiki/L-system) @wikipedia

### packing

[![PackingSpheresAroundCenter](./images/sketches/PackingSpheresAroundCenter.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchPackingSpheresAroundCenter.java)

[Circle packing theorem](https://en.wikipedia.org/wiki/Circle_packing_theorem) @wikipedia

## tools for algorithmic cliches

this is a collection of tools to facilitate some of the *algorithmic cliches*. these tools themselves are not considered *algorithmic cliches*.

### cubicle

[![Cubicle](./images/sketches/Cubicle.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchCubicle.java)

### octree

[![Octree3D](./images/sketches/Octree3D.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchOctree3D.java)

[Octree](https://en.wikipedia.org/wiki/Octree) @wikipedia

### state machine

[![StateMachineSimple](./images/sketches/StateMachineSimple.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchStateMachineSimple.java)

[![StateMachineWithObjects](./images/sketches/StateMachineWithObjects.png)](./src/de/hfkbremen/algorithmiccliches/examples/SketchStateMachineWithObjects.java)

[Finite-state machine](https://en.wikipedia.org/wiki/Finite-state_machine) @wikipedia
