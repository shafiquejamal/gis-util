package com.github.shafiquejamal.gisutil.location

import com.github.shafiquejamal.points.Area

case class BoundingCircle(
    override val id: String,
    override val location: GPSCoordinates,
    kmRadius: Double) extends Area[String] {
  
  override def contains(candidate: GPSCoordinates): Boolean = (location kmDistanceTo candidate) < kmRadius
  
}
