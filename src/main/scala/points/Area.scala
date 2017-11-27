package points

import com.github.shafiquejamal.gisutil.location.GPSCoordinates

trait Area[T] {
  
  def id: T
  
  def center: GPSCoordinates
  
}
