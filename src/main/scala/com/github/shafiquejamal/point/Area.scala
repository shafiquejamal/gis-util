package com.github.shafiquejamal.point

import com.github.shafiquejamal.gisutil.location.GPSCoordinate

trait Area[T] extends PointOfInterest[T] {
  
  def contains(candidate: GPSCoordinate): Boolean
  
}

case class Areas[T](areas: Seq[Area[T]]) {
  
  require(areas.map(_.id).distinct.length == areas.length)
  
  def contains(candidate: GPSCoordinate): Boolean = areas.exists(_.contains(candidate))
  
}

object Areas {
  
  def apply[T](area: Area[T]): Areas[T] = Areas(Seq(area))
  
}