package com.github.shafiquejamal.gisutil.location

import org.scalatest.{FlatSpecLike, Matchers}

class GPSCoordinatesUTest extends FlatSpecLike with Matchers {
  
  "GPS coordinates" should "be able to calculate distances between themselves" in {
    val pointA = GPSCoordinates(Lat(39.112927099380826), Lng(-94.62890723880348))
    val pointB = GPSCoordinates(Lat(39.11517290061917), Lng(-94.62890723880348))
    val expected = 250
    
    round(pointA metersDistanceTo pointB, 8) shouldBe expected
    round(pointA kmDistanceTo pointB, 8) shouldBe expected / 1000d
  }
  
  private def round(valueToRound: Double, nDecimalPlaces: Int) =
    BigDecimal(valueToRound).setScale(nDecimalPlaces, BigDecimal.RoundingMode.HALF_UP).toDouble
  
}
