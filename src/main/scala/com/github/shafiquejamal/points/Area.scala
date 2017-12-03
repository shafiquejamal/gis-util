package com.github.shafiquejamal.points

import com.github.shafiquejamal.gisutil.location.GPSCoordinate

trait Area[T] extends PointOfInterest[T] {
  
  def contains(candidate: GPSCoordinate): Boolean
  
}
