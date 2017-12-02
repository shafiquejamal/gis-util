package com.github.shafiquejamal.points

import com.github.shafiquejamal.gisutil.location.GPSCoordinates

trait PointOfInterest[T] {
  
  def id: T
  
  def location: GPSCoordinates
  
}
