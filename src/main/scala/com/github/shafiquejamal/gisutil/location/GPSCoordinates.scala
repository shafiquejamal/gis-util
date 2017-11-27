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
}
