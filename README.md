# GIS Utilities

This project contains utilities for 

- creating a bounding box around a given GPS location of a given size (returns the GPS locations of the corners of the box)
- calculating the population within areas of a given size and center point

See below, or the test code, for how to use these utilities.

## Installation

```
libraryDependencies += "com.github.shafiquejamal" %% "gis-util" % "0.0.3"
```

## Use

### Creating a bounding box

Given a center point and edge length, you can create a square bounding box that is axis aligned, and that can tell you whether a given point is inside it. The bounding box is just four GPS coordinates that are corners of the box, in the following order: SW, NW, NE, SE.

The following code creates a square bounding box with an edge length of 250m. Note that the edge length should be specified in km.

```scala
val centerPoint = GPSCoordinates(Lat(39.11405), Lng(-94.62746))

val bb250m = BoundingBox.from(centerPoint, 0.250)
```

To determine whether a given point lies within the bounding box, use the bounding box's `.contain` method:

```scala
val candidatePointInside250 = GPSCoordinates(Lat(39.11516), Lng(-94.62890))
val candidatePointOutside250 = GPSCoordinates(Lat(39.11518), Lng(-94.62890))

bb250m contains candidatePointInside250 // true
bb250m contains candidatePointOutside250 // false
```

### Calculating the population within square bounding boxes

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

https://stackoverflow.com/questions/18295825/determine-if-point-is-within-bounding-box

https://stackoverflow.com/questions/238260/how-to-calculate-the-bounding-box-for-a-given-lat-lng-location