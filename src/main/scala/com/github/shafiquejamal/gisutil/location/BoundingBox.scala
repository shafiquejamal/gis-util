package com.github.shafiquejamal.gisutil.location

import com.github.shafiquejamal.calculation.LocationCalculator.intermediatePoint
import com.github.shafiquejamal.points.Area

case class BoundingBox(
    sW: GPSCoordinate,
    nW: GPSCoordinate,
    nE: GPSCoordinate,
    sE: GPSCoordinate,
    edgeLengthKm: Double,
    location: GPSCoordinate,
    id: String)
  extends Ordered[BoundingBox] with Area[String] {
  
  override def compare(that: BoundingBox): Int =
    (sW, nW, nE, sE).compare(that.sW, that.nW, that.nE, that.sE)
  
  // Taken from: https://stackoverflow.com/questions/18295825/determine-if-point-is-within-bounding-box
  override def contains(candidate: GPSCoordinate): Boolean = {
    val gPSCoordinates = Seq(sW, nW, nE, sE).sorted
    
    val bottomLeft = gPSCoordinates.head
    val topRight = gPSCoordinates.reverse.head
    
    val isLongitudeIsInRange = if (topRight.lng < bottomLeft.lng) {
      candidate.lng >= bottomLeft.lng || candidate.lng <= topRight.lng
    } else {
      candidate.lng >= bottomLeft.lng && candidate.lng <= topRight.lng
    }
    
    candidate.lat >= bottomLeft.lat && candidate.lat <= topRight.lat && isLongitudeIsInRange
  }
  
}

object BoundingBox {
  
  import Constants._
  
  // Adapted from https://stackoverflow.com/questions/238260/how-to-calculate-the-bounding-box-for-a-given-lat-lng-location
  def from(center: GPSCoordinate, edgeLengthKm: Double): BoundingBox = {
    val radDist = (edgeLengthKm / 2) / Rkm
   
    val degLat = center.lat.value
    val degLon = center.lng.value
  
    val radLat = degLat.toRadians
    val radLon = degLon.toRadians

    val minLatTemp = radLat - radDist
    val maxLatTemp = radLat + radDist
    
    //  // define deltaLon to help determine min and max longitudes
    val deltaLon = Math.asin(Math.sin(radDist) / Math.cos(radLat))
    val (minLat, minLng, maxLat, maxLng) = if (minLatTemp > MIN_LAT && maxLatTemp < MAX_LAT) {
      val minLonTemp = radLon - deltaLon
      val minLon = if (minLonTemp < MIN_LON) minLonTemp + 2 * Math.PI else minLonTemp
      val maxLonTemp = radLon + deltaLon
      val maxLon = if (maxLonTemp > MAX_LON) maxLonTemp - 2 * Math.PI else maxLonTemp
      (minLatTemp.toDegrees, minLon.toDegrees, maxLatTemp.toDegrees, maxLon.toDegrees)
    } else {
      (Math.max(minLatTemp, MIN_LAT).toDegrees, MIN_LON.toDegrees, Math.min(maxLatTemp, MAX_LAT).toDegrees,
        MAX_LON.toDegrees)
    }
    val sW = GPSCoordinate(Lat(minLat), Lng(minLng))
    val nW = GPSCoordinate(Lat(maxLat), Lng(minLng))
    val nE = GPSCoordinate(Lat(maxLat), Lng(maxLng))
    val sE = GPSCoordinate(Lat(minLat), Lng(maxLng))
    
    val id = s"${center.lat.value.toString}_${center.lng.value.toString}"
    
    BoundingBox(sW, nW, nE, sE, edgeLengthKm, center, id)
  }
  
  def apply(sW: GPSCoordinate, nW: GPSCoordinate, nE: GPSCoordinate, sE: GPSCoordinate): BoundingBox =
    BoundingBox(sW, nW, nE, sE, sW kmDistanceTo nW)
  
  def apply(sW: GPSCoordinate, nW: GPSCoordinate, nE: GPSCoordinate, sE: GPSCoordinate,
      edgeLengthKm: Double): BoundingBox = {
    val center: GPSCoordinate = intermediatePoint(sW, nE, 0.5)
    BoundingBox(sW, nW, nE, sE, edgeLengthKm, center)
  }
  
  def apply(sW: GPSCoordinate, nW: GPSCoordinate, nE: GPSCoordinate, sE: GPSCoordinate, edgeLengthKm: Double,
      center: GPSCoordinate): BoundingBox = {
    BoundingBox(sW, nW, nE, sE, edgeLengthKm, center, center.makeId)
  }
  
}