package com.github.shafiquejamal.points

import com.github.shafiquejamal.gisutil.location.GPSCoordinates

trait Area[T] extends PointOfInterest[T] {
  
  def contains(candidate: GPSCoordinates): Boolean
  
}
