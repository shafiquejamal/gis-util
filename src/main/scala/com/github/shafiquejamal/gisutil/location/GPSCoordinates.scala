package com.github.shafiquejamal.gisutil.location

case class Lat(value: Double) extends Ordered[Lat] {
  override def compare(that: Lat): Int = value compare that.value
}

case class Lng(value: Double) extends Ordered[Lng] {
  override def compare(that: Lng): Int = value compare that.value
}

case class GPSCoordinates(lat: Lat, lng: Lng) extends Ordered[GPSCoordinates] {
  override def compare(that: GPSCoordinates): Int = {
    val latitudeComparison = lat.compare(that.lat)
    if (latitudeComparison != 0) {
      latitudeComparison
    } else {
      lng compare that.lng
    }
  }
  
  def makeId: String = s"${lat.value.toString}_${lng.value.toString}"
  
  // https://www.movable-type.co.uk/scripts/latlong.html
  def metersDistanceTo(that: GPSCoordinates): Double = {
    import Constants.Rkm
    val lat1 = lat.value
    val lat2 = that.lat.value
    val phi1 = lat1.toRadians
    val phi2 = lat2.toRadians
    val deltaPhi = (lat2 - lat1).toRadians
    val lng1 = lng.value
    val lng2 = that.lng.value
    val deltaLambda = (lng2 - lng1).toRadians
    val a = Math.pow(Math.sin(deltaPhi / 2d), 2) +
      Math.cos(phi1) * Math.cos(phi2) * Math.pow(Math.sin(deltaLambda / 2d), 2)
    val c = 2d * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    Rkm * 1000 * c
  }
  
  def kmDistanceTo(that: GPSCoordinates): Double = metersDistanceTo(that) / 1000d
  
}


object Constants {
  val MIN_LAT: Double = (-90).toRadians
  val MAX_LAT: Double = 90.toRadians
  val MIN_LON: Double = (-180).toRadians
  val MAX_LON: Double = 180.toRadians
  
  val Rkm = 6378.1
}