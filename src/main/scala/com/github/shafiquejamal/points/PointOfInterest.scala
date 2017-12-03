package com.github.shafiquejamal.points

import com.github.shafiquejamal.gisutil.location.GPSCoordinate

trait PointOfInterest[T] {
  
  def id: T
  
  def location: GPSCoordinate
  
}
