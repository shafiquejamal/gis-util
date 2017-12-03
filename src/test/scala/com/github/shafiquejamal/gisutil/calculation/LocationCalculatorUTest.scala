package com.github.shafiquejamal.gisutil.calculation

import com.github.shafiquejamal.calculation.LocationCalculator
import com.github.shafiquejamal.gisutil.location.{GPSCoordinates, Lat, Lng}
import org.scalatest.{FlatSpecLike, Matchers}

class LocationCalculatorUTest extends FlatSpecLike with Matchers {
  
  "Calculating an intermediate point between two points with a specified fraction" should "yield the corresponding " +
  "GPS coordinates" in {
    val pointA = GPSCoordinates(Lat(39.112927099380826),Lng(-94.62890723880348))
    val pointB = GPSCoordinates(Lat(39.11517290061917),Lng(-94.62890723880348))
    val pointC = GPSCoordinates(Lat(39.11517290061917),Lng(-94.62601276119655))
  
    LocationCalculator
      .intermediatePoint(pointA, pointB, 0.50) shouldEqual GPSCoordinates(Lat(39.11405), Lng(-94.62890723880349))
    LocationCalculator
      .intermediatePoint(pointB, pointC, 0.25) shouldEqual GPSCoordinates(Lat(39.1151729073293),Lng(-94.6281836194018))
  }
  
  "The location calculator" should "calculate the destination point given distance, initial bearing, and starting " +
  "point" in {
    
    val expected = GPSCoordinates(Lat(39.12634533421416), Lng(-94.67103481171077))
    val actual = LocationCalculator.locationFrom(20, GPSCoordinates(Lat(39.11405), Lng(-94.62746000000003)), 4)
    actual kmDistanceTo expected shouldBe 0d +- 0.0045d
  }
  
}
