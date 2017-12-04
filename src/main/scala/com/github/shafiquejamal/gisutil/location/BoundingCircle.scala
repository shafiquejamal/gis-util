package com.github.shafiquejamal.gisutil.location

import com.github.shafiquejamal.point.Area

case class BoundingCircle(
    override val id: String,
    override val location: GPSCoordinate,
    kmRadius: Double)
  extends Area[String] {
  
  override def contains(candidate: GPSCoordinate): Boolean = (location kmDistanceTo candidate) < kmRadius
  
}
