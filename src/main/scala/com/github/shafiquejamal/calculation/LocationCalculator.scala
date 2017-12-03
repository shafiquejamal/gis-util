package com.github.shafiquejamal.calculation

import java.lang.Math._

import com.github.shafiquejamal.gisutil.location.Constants.Rkm
import com.github.shafiquejamal.gisutil.location.{GPSCoordinate, Lat, Lng}

object LocationCalculator {
  
  // From https://www.movable-type.co.uk/scripts/latlong.html
  def intermediatePoint(pointA: GPSCoordinate, pointB: GPSCoordinate, f: Double): GPSCoordinate = {
    val dkm = pointA kmDistanceTo pointB
    val delta = dkm / Rkm
    val a = sin((1 - f) * delta) / sin(delta)
    val b = sin(f * delta) / sin(delta)
    val phi1 = pointA.lat.value.toRadians
    val phi2 = pointB.lat.value.toRadians
    val lambda1 = pointA.lng.value.toRadians
    val lambda2 = pointB.lng.value.toRadians
    val x = a * cos(phi1) * cos(lambda1) + b * cos(phi2) * cos(lambda2)
    val y = a * cos(phi1) * sin(lambda1) + b * cos(phi2) * sin(lambda2)
    val z = a * sin(phi1) + b * sin(phi2)
    val phiI = atan2(z, sqrt(pow(x, 2) + pow(y, 2)))
    val lambdaI = atan2(y, x)
    GPSCoordinate(Lat(phiI.toDegrees), Lng(lambdaI.toDegrees))
  }
  
  def locationFrom(degTiltFromWest: Double, startingPoint: GPSCoordinate, kmDistance: Double): GPSCoordinate = {
    val lat1Rad = startingPoint.lat.value.toRadians
    val lng1Rad = startingPoint.lng.value.toRadians
    val bearingRad = (degTiltFromWest - 90).toRadians
    val deltaRad = kmDistance / Rkm
    
    val lat2Rad = Math.asin(
      (Math.sin(lat1Rad) * Math.cos(deltaRad)) +
      (Math.cos(lat1Rad) * Math.sin(deltaRad) * Math.cos(bearingRad)))
    val lng2Rad = lng1Rad + Math.atan2(Math.sin(bearingRad) * Math.sin(deltaRad) * Math.cos(lat1Rad),
      Math.cos(deltaRad) - (Math.sin(lat1Rad) * Math.sin(lat2Rad)))
  
    GPSCoordinate(Lat(lat2Rad.toDegrees), Lng(lng2Rad.toDegrees))
  }
  
  
}
