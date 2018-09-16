# GIS Utilities

This project contains utilities for 

- creating a bounding box around a given GPS location of a given size (returns the GPS locations of the corners of the box)
- calculating the population within areas of a given size and center point

See below, or the test code, for how to use these utilities.

## Installation

```
libraryDependencies += "com.github.shafiquejamal" %% "gis-util" % "0.0.16"
```

## Use

### Bounding shapes

This library contains code for creating the following types of `Area`s:

- Bounding box (square)
- Circle
- Ellipse

Areas are able to determine whether a given point of interest (`PointOfInterest`) lies within them. A point of interest is a point specified by an id and a `GPSCoordinate`. They can also have a population as a state variable, which is a collection of `PointOfInterest` that they contain. 

#### Bounding box

##### Creating the bounding box

Given a center point and edge length, you can create a square bounding box that is axis aligned, and that can tell you whether a given point is inside it. You can create the bounding box using:
 
- four GPS coordinates that are corners of the box, in the following order: SW, NW, NE, SE, as well as the center point and optionally the edge length (just be sure to provide an accurate edge length.
). If you do not specify the edge length, then it is calculated from the SW and NW GPS coordinates provided. The following code creates a square bounding box with an edge length of 250m. Note that the edge length should be specified in km.


```scala
BoundingBox(
  GPSCoordinates(Lat(39.112927099380826),Lng(-94.62890723880348)),
  GPSCoordinates(Lat(39.11517290061917),Lng(-94.62890723880348)),
  GPSCoordinates(Lat(39.11517290061917),Lng(-94.62601276119655)),
  GPSCoordinates(Lat(39.112927099380826),Lng(-94.62601276119655)), 0.250
)

BoundingBox(
  GPSCoordinates(Lat(39.112927099380826),Lng(-94.62890723880348)),
  GPSCoordinates(Lat(39.11517290061917),Lng(-94.62890723880348)),
  GPSCoordinates(Lat(39.11517290061917),Lng(-94.62601276119655)),
  GPSCoordinates(Lat(39.112927099380826),Lng(-94.62601276119655))
)
```

- a center point and edge length

```scala
val centerPoint = GPSCoordinates(Lat(39.11405), Lng(-94.62746))

val bb250m = BoundingBox.from(centerPoint, 0.250)
```

To determine whether a given point lies within the bounding box, use the bounding box's `.contain` method:

```scala
val candidatePointInside250 = GPSCoordinates(Lat(39.11516), Lng(-94.62890))
val candidatePointOutside250 = GPSCoordinates(Lat(39.11518), Lng(-94.62890))

bb250m boundaryWraps candidatePointInside250 // true
bb250m boundaryWraps candidatePointOutside250 // false
```

##### Calculating the population within square bounding boxes

To determine the number of points of interest (e.g. number of people, stores, etc.) within the bounding box, you can send it all of the points of interest, and it will retain in its state only those that lie within its boundaries - you then call the `nWithin` method:

```scala
case class Person(override val id: String, override val location: GPSCoordinate) extends PointOfInterest[String]

val persons = Seq(
    Person("1_250_0_0", GPSCoordinate(Lat(39.11518), Lng(-94.62890))),
    Person("2_250_1_0", GPSCoordinate(Lat(39.11500), Lng(-94.62890))),
    Person("3_250_1_0", GPSCoordinate(Lat(39.11490), Lng(-94.62870))),
    Person("4_250_1_1", GPSCoordinate(Lat(39.11354), Lng(-94.62680))),
    Person("5_250_1_1", GPSCoordinate(Lat(39.11294), Lng(-94.62605))),
    Person("6_250_0_1", GPSCoordinate(Lat(39.11295), Lng(-94.62600))),
    Person("7_250_0_0", GPSCoordinate(Lat(39.11136), Lng(-94.62380))))
    
val centerPoint = GPSCoordinate(Lat(39.11405), Lng(-94.62746))
val bb250m = BoundingBox.from(centerPoint, 0.250)
val bb500m = BoundingBox.from(centerPoint, 0.500)

(bBox250m `with` persons).nWithin // 4
(bBox500m `with` persons).nWithin // 6
 
```

The following image illustrates the concept:

![Screenshot areas with markers](https://user-images.githubusercontent.com/2116061/33293205-02f1dad4-d39a-11e7-83f4-a1d33be88797.png)

The upper left square, KC1, has an edge length of 250m and contains 4 markers. The lower left square, KC2, also has an edge length of 250m and contains 3 markers. When the edge lengths are increased to 500m, the areas contain 6 and 4 markers respectively. The code above is for the box that is the upper left square.


### Circle, Ellipse

Circle extends the same Area trait as does square, and so has the `nWithin`, `boundaryWraps` and other methods. 

A Circle is created by specifying a location and a radius:

```scala
val circle = BoundingCircle("c1", GPSCoordinate(Lat(39.11405), Lng(-94.62746)), 2)
val pointOutside = GPSCoordinate(Lat(39.120261171336146), Lng(-94.64946337892016))
val pointInside = GPSCoordinate(Lat(39.120138217988696), Lng(-94.64902763079968))

circle boundaryWraps pointOutside // false
circle boundaryWraps pointInside // true
```

An Ellipse is created by specifying a location, semi-major axis, semi-minor axis, and tilt from West.:

```scala
val ellipse = BoundingEllipse("e1", GPSCoordinate(Lat(39.11405), Lng(-94.62746)), 2, 1, 20)
    
ellipse boundaryWraps pointOutside // false
ellipse boundaryWraps pointInside // true
```

The picture below illustrates the concept. The circle and the ellipse are able to determine whether each point (marked by the green markers at the upper left) are inside or outside of its boundary. 

![Ellipse with point inside and point outside](https://user-images.githubusercontent.com/2116061/34072157-2595ad44-e250-11e7-950d-3f52616398d8.png) 

#### Slice a given bounding box into more bounding boxes

Bounding box KC1 above of 250m edge length can be divided into 9 non-overlapping, identically-sized bounding boxes that completely cover the area of the original bounding box.

All that is needed is the center point of the original bounding box, the edge length of the original bounding box, and the number of slices into which to cut each edge. From this, new center points and the edge length of each (the latter being identical for all) can then be calculated.

The following image shows the new center points that the code below calculates.

![Screenshot areas with center points](https://user-images.githubusercontent.com/2116061/33327655-0d06063a-d426-11e7-9559-744e2ec02e19.png)
   
```scala
val centerPoint = GPSCoordinates(Lat(39.11405), Lng(-94.62746))

CatchmentAreasCreator.from(centerPoint, 0.250, 3)
```

This should yield the following points:
```scala
GPSCoordinates(Lat(39.11330139958722),Lng(-94.62842482586905)),
GPSCoordinates(Lat(39.11330139958722),Lng(-94.62746000000003)),
GPSCoordinates(Lat(39.11330139958722),Lng(-94.62649517413101)),
GPSCoordinates(Lat(39.11405),Lng(-94.62842482586905)),
GPSCoordinates(Lat(39.11405),Lng(-94.62746000000003)),
GPSCoordinates(Lat(39.11405),Lng(-94.62649517413101)),
GPSCoordinates(Lat(39.11479860041278),Lng(-94.62842482586905)),
GPSCoordinates(Lat(39.11479860041278),Lng(-94.62746000000003)),
GPSCoordinates(Lat(39.11479860041278),Lng(-94.62649517413101)))
```

## References

https://www.movable-type.co.uk/scripts/latlong.html

https://stackoverflow.com/questions/18295825/determine-if-point-is-within-bounding-box

https://stackoverflow.com/questions/238260/how-to-calculate-the-bounding-box-for-a-given-lat-lng-location
