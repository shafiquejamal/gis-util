package com.github.shafiquejamal.point

import com.github.shafiquejamal.gisutil.location.GPSCoordinate

trait Area[T] extends PointOfInterest[T] {
  
  def boundaryWraps(candidate: GPSCoordinate): Boolean
  
  def pointsWithin: Seq[PointOfInterest[T]]
  
  def nWithin: Long = pointsWithin.length
  
  def hasAsMember(pointOfInterest: PointOfInterest[T]): Boolean = pointsWithin contains pointOfInterest
  
  def `with`(pointsOfInterest: Seq[PointOfInterest[T]]): Area[T] = {
    val pointsToAdd = pointsOfInterest.filter(pointOfInterest => this boundaryWraps pointOfInterest.location)
    construct(pointsToAdd)
  }
  
  protected def construct(allPointsWithin: Seq[PointOfInterest[T]]): Area[T]
}

case class Areas[T](areas: Seq[Area[T]]) {
  
  require(areas.map(_.id).distinct.length == areas.length)
  
  def contains(candidate: GPSCoordinate): Boolean = areas.exists(_.boundaryWraps(candidate))
  
}

object Areas {
  
  def apply[T](area: Area[T]): Areas[T] = Areas(Seq(area))
  
}