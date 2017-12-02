package com.github.shafiquejamal.gisutil.location

import com.github.shafiquejamal.calculation.LocationCalculator.intermediatePoint
import com.github.shafiquejamal.points.Area

case class BoundingBox(
    sW: GPSCoordinates,
    nW: GPSCoordinates,
    nE: GPSCoordinates,
    sE: GPSCoordinates,
    edgeLengthKm: Double,
    location: GPSCoordinates,
    id: String)
  extends Ordered[BoundingBox] with Area[String] {
  
  override def compare(that: BoundingBox): Int =
    (sW, nW, nE, sE).compare(that.sW, that.nW, that.nE, that.sE)
  
  // Taken from: https://stackoverflow.com/questions/18295825/determine-if-point-is-within-bounding-box
  override def contains(candidate: GPSCoordinates): Boolean = {
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
  def from(center: GPSCoordinates, edgeLengthKm: Double): BoundingBox = {
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
    val sW = GPSCoordinates(Lat(minLat), Lng(minLng))
    val nW = GPSCoordinates(Lat(maxLat), Lng(minLng))
    val nE = GPSCoordinates(Lat(maxLat), Lng(maxLng))
    val sE = GPSCoordinates(Lat(minLat), Lng(maxLng))
    
    val id = s"${center.lat.value.toString}_${center.lng.value.toString}"
    
    BoundingBox(sW, nW, nE, sE, edgeLengthKm, center, id)
  }
  
  def apply(sW: GPSCoordinates, nW: GPSCoordinates, nE: GPSCoordinates, sE: GPSCoordinates): BoundingBox =
    BoundingBox(sW, nW, nE, sE, sW kmDistanceTo nW)
  
  def apply(sW: GPSCoordinates, nW: GPSCoordinates, nE: GPSCoordinates, sE: GPSCoordinates,
      edgeLengthKm: Double): BoundingBox = {
    val center: GPSCoordinates = intermediatePoint(sW, nE, 0.5)
    BoundingBox(sW, nW, nE, sE, edgeLengthKm, center)
  }
  
  def apply(sW: GPSCoordinates, nW: GPSCoordinates, nE: GPSCoordinates, sE: GPSCoordinates, edgeLengthKm: Double,
      center: GPSCoordinates): BoundingBox = {
    val id = s"${center.lat.value.toString}_${center.lng.value.toString}"
    BoundingBox(sW, nW, nE, sE, edgeLengthKm, center, id)
  }
  
}