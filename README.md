# GIS Utilities

This project contains utilities for 

- creating a bounding box around a given GPS location of a given size (returns the GPS locations of the corners of the box)
- calculating the population within areas of a given size and center point

See below, or the test code, for how to use these utilities.

## Installation

```
libraryDependencies += "com.github.shafiquejamal" %% "gis-util" % "0.0.3"
```
## Calculating the population within areas

The following image illustrates the concept:

![Screenshot areas with markers](https://user-images.githubusercontent.com/2116061/33293205-02f1dad4-d39a-11e7-83f4-a1d33be88797.png)

The upper left square, KC1, has an edge length of 250m and contains 4 markers. The lower left square, KC2, also has an edge length of 250m and contains 3 markers. When the edge lengths are increased to 500m, the areas contain 6 and 4 markers respectively. 

Create the survey areas:
```scala
class SurveyAreaImpl(override val id: String, override val center: GPSCoordinates) extends Area[String]

object SurveyAreaImpl {
  def apply(id: String, center: GPSCoordinates) = new SurveyAreaImpl(id, center)
}

val surveyArea1 = SurveyAreaImpl("Kansas_KC_1", GPSCoordinates(Lat(39.11405), Lng(-94.62746)))
val surveyArea2 = SurveyAreaImpl("Kansas_KC_2", GPSCoordinates(Lat(39.11244), Lng(-94.62537)))
```

Create the points of interest (people, houses, stores, etc.) that you want to count for each survey area:
```scala
class Person(override val id: String, override val gPSCoordinates: GPSCoordinates) extends PointOfInterest[String]

object Person {
  def apply(id: String, gPSCoordinates: GPSCoordinates) = new Person(id, gPSCoordinates)
}

val persons = Seq(
  Person("1_250_0_0", GPSCoordinates(Lat(39.11518), Lng(-94.62890))),
  Person("2_250_1_0", GPSCoordinates(Lat(39.11500), Lng(-94.62890))),
  Person("3_250_1_0", GPSCoordinates(Lat(39.11490), Lng(-94.62870))),
  Person("4_250_1_1", GPSCoordinates(Lat(39.11354), Lng(-94.62680))),
  Person("5_250_1_1", GPSCoordinates(Lat(39.11294), Lng(-94.62605))),
  Person("6_250_0_1", GPSCoordinates(Lat(39.11295), Lng(-94.62600))),
  Person("7_250_0_0", GPSCoordinates(Lat(39.11136), Lng(-94.62380)))
)
```
Choose the edge lengths of the catchment area squares, in km. This example calculates number of points within catchment area squares of 250m and of 500m.
```scala
val squareEdgeLengths = Seq(0.250, 0.500)
```

Call the method to do the calculation:
```scala
CatchmentAreaCalculator.calculateNPointsOfInterestWithinAreas(persons, surveyAreas, squareEdgeLengths)
```

This should yield the following objects:
```scala
AreaCharacteristics(surveyArea1, Seq(AreaMeasures(0.25, 4), AreaMeasures(0.5, 6))),
AreaCharacteristics(surveyArea2, Seq(AreaMeasures(0.25, 3), AreaMeasures(0.5, 4)))
```

## References

https://www.movable-type.co.uk/scripts/latlong.html