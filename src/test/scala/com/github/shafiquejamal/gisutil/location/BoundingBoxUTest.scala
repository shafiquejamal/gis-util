package com.github.shafiquejamal.gisutil.location

import org.scalatest.{FlatSpecLike, Matchers}

class BoundingBoxUTest extends FlatSpecLike with Matchers {
  
  trait Fixture {
    val centerPoint = GPSCoordinates(Lat(39.11405), Lng(-94.62746))
    val bb250m = BoundingBox.from(centerPoint, 0.250)
    val bb500m = BoundingBox.from(centerPoint, 0.500)
  }
  
  "Creating a bounding box from a center point and edge length" should "return a bouding box with coordinates that " +
  "form a square with an edge length of the specified edge length" in new Fixture {
    val expectedBox250m = BoundingBox(
      GPSCoordinates(Lat(39.112927099380826),Lng(-94.62890723880348)),
      GPSCoordinates(Lat(39.11517290061917),Lng(-94.62890723880348)),
      GPSCoordinates(Lat(39.11517290061917),Lng(-94.62601276119655)),
      GPSCoordinates(Lat(39.112927099380826),Lng(-94.62601276119655))
    )
    val expectedBox500m = BoundingBox(
      GPSCoordinates(Lat(39.11180419876166),Lng(-94.63035447760728)),
      GPSCoordinates(Lat(39.116295801238344),Lng(-94.63035447760728)),
      GPSCoordinates(Lat(39.116295801238344),Lng(-94.62456552239274)),
      GPSCoordinates(Lat(39.11180419876166),Lng(-94.62456552239274))
    )
  
    bb250m shouldEqual expectedBox250m
    bb500m shouldEqual expectedBox500m
  }
  
  "The bounding box" should "be able to determine whether a given point is contained within it" in new Fixture {
    val candidatePointInside250 = GPSCoordinates(Lat(39.11516), Lng(-94.62890))
    val candidatePointOutside250 = GPSCoordinates(Lat(39.11518), Lng(-94.62890))
    
    bb250m contains candidatePointInside250 shouldBe true
    bb250m contains candidatePointOutside250 shouldBe false
    BoundingBox.from(centerPoint, 0.2479) contains candidatePointInside250 shouldBe false
  }
  
}
