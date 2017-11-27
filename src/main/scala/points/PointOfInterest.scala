package points

import com.github.shafiquejamal.gisutil.location.GPSCoordinates

trait PointOfInterest[T] {
  
  def id: T
  
  def gPSCoordinates: GPSCoordinates
  
}
