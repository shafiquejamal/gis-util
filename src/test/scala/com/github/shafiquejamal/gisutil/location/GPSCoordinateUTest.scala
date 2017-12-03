package com.github.shafiquejamal.gisutil.location

import org.scalatest.{FlatSpecLike, Matchers}

class GPSCoordinateUTest extends FlatSpecLike with Matchers {
  
  "GPS coordinates" should "be able to calculate distances between themselves" in {
    val pointA = GPSCoordinate(Lat(39.112927099380826), Lng(-94.62890723880348))
    val pointB = GPSCoordinate(Lat(39.11517290061917), Lng(-94.62890723880348))
    val expected = 250d
    val tolerance = 0.000000001
    
    pointA metersDistanceTo pointB shouldBe expected +- tolerance
    pointA kmDistanceTo pointB shouldBe (expected / 1000d) +- tolerance
  }
  
}
