# GIS Utilities

This project contains utilities for 

- creating a bounding box around a given GPS location of a given size (returns the GPS locations of the corners of the box)
- calculating the population within areas of a given size and center point

See the test files for how to use these utilities.

## Installation

```
libraryDependencies += "com.github.shafiquejamal" %% "gis-util" % "0.0.1"
```
## Calculating the population within areas

The following image illustrates the concept:

![Screenshot areas with markers](https://user-images.githubusercontent.com/2116061/33293205-02f1dad4-d39a-11e7-83f4-a1d33be88797.png)

The upper left square, KC1, has an edge length of 250m and contains 4 markers. The lower left square, KC2, also has an edge length of 250m and contains 3 markers. When the edge lengths are increased to 500m, the areas contain 6 and 4 markers respectively. 
