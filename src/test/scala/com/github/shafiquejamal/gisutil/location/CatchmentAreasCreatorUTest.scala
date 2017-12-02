package com.github.shafiquejamal.gisutil.location

import org.scalatest.{FlatSpecLike, Matchers}

class CatchmentAreasCreatorUTest extends FlatSpecLike with Matchers {
  
  trait Fixture {
    val newEdgeLength = 0.250 / 3
    val expected = Seq(
      CatchmentArea("1", GPSCoordinates(Lat(39.11330139958722),Lng(-94.62842482586905)), newEdgeLength),
      CatchmentArea("2", GPSCoordinates(Lat(39.11330139958722),Lng(-94.62746000000003)), newEdgeLength),
      CatchmentArea("3", GPSCoordinates(Lat(39.11330139958722),Lng(-94.62649517413101)), newEdgeLength),
      CatchmentArea("4", GPSCoordinates(Lat(39.11405),Lng(-94.62842482586905)), newEdgeLength),
      CatchmentArea("5", GPSCoordinates(Lat(39.11405),Lng(-94.62746000000003)), newEdgeLength),
      CatchmentArea("6", GPSCoordinates(Lat(39.11405),Lng(-94.62649517413101)), newEdgeLength),
      CatchmentArea("7", GPSCoordinates(Lat(39.11479860041278),Lng(-94.62842482586905)), newEdgeLength),
      CatchmentArea("8", GPSCoordinates(Lat(39.11479860041278),Lng(-94.62746000000003)), newEdgeLength),
      CatchmentArea("9", GPSCoordinates(Lat(39.11479860041278),Lng(-94.62649517413101)), newEdgeLength))
  }
  
  "Calculating the center points using the bounding box" should "yield the center points corresponding to the " +
  "bounding box and number of slices" in new Fixture {
    val bb = BoundingBox(
      GPSCoordinates(Lat(39.112927099380826),Lng(-94.62890723880348)),
      GPSCoordinates(Lat(39.11517290061917),Lng(-94.62890723880348)),
      GPSCoordinates(Lat(39.11517290061917),Lng(-94.62601276119655)),
      GPSCoordinates(Lat(39.112927099380826),Lng(-94.62601276119655)), 0.250
    )
  
    CatchmentArea.from(bb, 3) should contain theSameElementsAs expected
  }
  
  "Calculating the center points using a given center point and total edge length" should "yield the center points " +
  "corresponding to the bounding box and number of slices" in new Fixture {
    val centerPoint = GPSCoordinates(Lat(39.11405), Lng(-94.62746))
    CatchmentArea.from(centerPoint, 0.250, 3) should contain theSameElementsAs expected
  }
  
}
