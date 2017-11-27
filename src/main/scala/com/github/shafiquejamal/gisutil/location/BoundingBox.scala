package com.github.shafiquejamal.gisutil.location

case class BoundingBox(
    gPSCoordinates1: GPSCoordinates,
    gPSCoordinates2: GPSCoordinates,
    gPSCoordinates3: GPSCoordinates,
    gPSCoordinates4: GPSCoordinates)
  extends Ordered[BoundingBox]{
  
  override def compare(that: BoundingBox): Int =
    (gPSCoordinates1, gPSCoordinates2, gPSCoordinates3, gPSCoordinates4)
      .compare(that.gPSCoordinates1, that.gPSCoordinates2, that.gPSCoordinates3, that.gPSCoordinates4)
  
  // Taken from: https://stackoverflow.com/questions/18295825/determine-if-point-is-within-bounding-box
  def contains(candidate: GPSCoordinates): Boolean = {
    val gPSCoordinates = Seq(gPSCoordinates1, gPSCoordinates2, gPSCoordinates3, gPSCoordinates4).sorted
    
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

object DegreeRadianConverter {
  
  implicit class DegreeRadianConverter(value: Double) {
    def toDegrees: Double = value * Math.PI / 180d
    
    def toRadians: Double = value * 180d / Math.PI
  }
  
  object Constants {
    val MIN_LAT: Double = (-90).toRadians
    val MAX_LAT: Double = 90.toRadians
    val MIN_LON: Double = (-180).toRadians
    val MAX_LON: Double = 180.toRadians
  
    val R = 6378.1
  }
}


object BoundingBox {
  
  import DegreeRadianConverter.Constants._
  
  // Adapted from https://stackoverflow.com/questions/238260/how-to-calculate-the-bounding-box-for-a-given-lat-lng-location
  def from(center: GPSCoordinates, edgeLengthKm: Double): BoundingBox = {
    val radDist = (edgeLengthKm / 2) / R
   
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
    val corner1 = GPSCoordinates(Lat(minLat), Lng(minLng))
    val corner2 = GPSCoordinates(Lat(maxLat), Lng(minLng))
    val corner3 = GPSCoordinates(Lat(maxLat), Lng(maxLng))
    val corner4 = GPSCoordinates(Lat(minLat), Lng(maxLng))
    
    BoundingBox(corner1, corner2, corner3, corner4)
  }
  
}