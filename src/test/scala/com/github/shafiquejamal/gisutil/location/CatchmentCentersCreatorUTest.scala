package com.github.shafiquejamal.gisutil.location

import org.scalatest.{FlatSpecLike, Matchers}

class CatchmentCentersCreatorUTest extends FlatSpecLike with Matchers {
  
  trait Fixture {
    val expected = Seq(
      GPSCoordinates(Lat(39.11330139958722),Lng(-94.62842482586905)),
      GPSCoordinates(Lat(39.11330139958722),Lng(-94.62746000000003)),
      GPSCoordinates(Lat(39.11330139958722),Lng(-94.62649517413101)),
      GPSCoordinates(Lat(39.11405),Lng(-94.62842482586905)),
      GPSCoordinates(Lat(39.11405),Lng(-94.62746000000003)),
      GPSCoordinates(Lat(39.11405),Lng(-94.62649517413101)),
      GPSCoordinates(Lat(39.11479860041278),Lng(-94.62842482586905)),
      GPSCoordinates(Lat(39.11479860041278),Lng(-94.62746000000003)),
      GPSCoordinates(Lat(39.11479860041278),Lng(-94.62649517413101)))
  }
  
  "Calculating an intermediate point between two points with a specified fraction" should "yield the corresponding " +
  "GPS coordinates" in {
    val pointA = GPSCoordinates(Lat(39.112927099380826),Lng(-94.62890723880348))
    val pointB = GPSCoordinates(Lat(39.11517290061917),Lng(-94.62890723880348))
    val pointC = GPSCoordinates(Lat(39.11517290061917),Lng(-94.62601276119655))
    
    CatchmentCentersCreator
      .intermediatePoint(pointA, pointB, 0.50) shouldEqual GPSCoordinates(Lat(39.11405), Lng(-94.62890723880349))
    CatchmentCentersCreator
      .intermediatePoint(pointB, pointC, 0.25) shouldEqual GPSCoordinates(Lat(39.1151729073293),Lng(-94.6281836194018))
  }
  
  "Calculating the center points using the bounding box" should "yield the center points corresponding to the " +
  "bounding box and number of slices" in new Fixture {
    val bb = BoundingBox(
      GPSCoordinates(Lat(39.112927099380826),Lng(-94.62890723880348)),
      GPSCoordinates(Lat(39.11517290061917),Lng(-94.62890723880348)),
      GPSCoordinates(Lat(39.11517290061917),Lng(-94.62601276119655)),
      GPSCoordinates(Lat(39.112927099380826),Lng(-94.62601276119655))
    )
    
    CatchmentCentersCreator.from(bb, 3) should contain theSameElementsAs expected
  }
  
  "Calculating the center points using a given center point and total edge length" should "yield the center points " +
  "corresponding to the bounding box and number of slices" in new Fixture {
    val centerPoint = GPSCoordinates(Lat(39.11405), Lng(-94.62746))
    CatchmentCentersCreator.from(centerPoint, 0.250, 3) should contain theSameElementsAs expected
  }
  
}
