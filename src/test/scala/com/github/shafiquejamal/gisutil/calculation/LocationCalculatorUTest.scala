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
  
  
}
