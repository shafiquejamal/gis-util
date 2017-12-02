package com.github.shafiquejamal.gisutil.location

import org.scalatest.{FlatSpecLike, Matchers}

class CatchmentCenterUTest extends FlatSpecLike with Matchers {
  
  trait Fixture {
    val newEdgeLength = 0.250 / 3
    val expected = Seq(
      CatchmentCenter("39.11330139958722_-94.62842482586905", GPSCoordinates(Lat(39.11330139958722), Lng(-94.62842482586905)), newEdgeLength),
      CatchmentCenter("39.11330139958722_-94.62746000000003", GPSCoordinates(Lat(39.11330139958722), Lng(-94.62746000000003)), newEdgeLength),
      CatchmentCenter("39.11330139958722_-94.62649517413101", GPSCoordinates(Lat(39.11330139958722), Lng(-94.62649517413101)), newEdgeLength),
      CatchmentCenter("39.11405_-94.62842482586905", GPSCoordinates(Lat(39.11405), Lng(-94.62842482586905)), newEdgeLength),
      CatchmentCenter("39.11405_-94.62746000000003", GPSCoordinates(Lat(39.11405), Lng(-94.62746000000003)), newEdgeLength),
      CatchmentCenter("39.11405_-94.62649517413101", GPSCoordinates(Lat(39.11405), Lng(-94.62649517413101)), newEdgeLength),
      CatchmentCenter("39.11479860041278_-94.62842482586905", GPSCoordinates(Lat(39.11479860041278), Lng(-94.62842482586905)), newEdgeLength),
      CatchmentCenter("39.11479860041278_-94.62746000000003", GPSCoordinates(Lat(39.11479860041278), Lng(-94.62746000000003)), newEdgeLength),
      CatchmentCenter("39.11479860041278_-94.62649517413101", GPSCoordinates(Lat(39.11479860041278), Lng(-94.62649517413101)), newEdgeLength))
  }
  
  "Calculating the center points using the bounding box" should "yield the center points corresponding to the " +
  "bounding box and number of slices" in new Fixture {
    val bb = BoundingBox(
      GPSCoordinates(Lat(39.112927099380826),Lng(-94.62890723880348)),
      GPSCoordinates(Lat(39.11517290061917),Lng(-94.62890723880348)),
      GPSCoordinates(Lat(39.11517290061917),Lng(-94.62601276119655)),
      GPSCoordinates(Lat(39.112927099380826),Lng(-94.62601276119655)), 0.250
    )
  
    CatchmentCenter.from(bb, 3) should contain theSameElementsAs expected
  }
  
  "Calculating the center points using a given center point and total edge length" should "yield the center points " +
  "corresponding to the bounding box and number of slices" in new Fixture {
    val centerPoint = GPSCoordinates(Lat(39.11405), Lng(-94.62746))
    CatchmentCenter.from(centerPoint, 0.250, 3) should contain theSameElementsAs expected
  }
  
}
