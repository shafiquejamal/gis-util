package com.github.shafiquejamal.point

import com.github.shafiquejamal.gisutil.location.GPSCoordinate

trait Area[T, K <: Area[T, K]] extends PointOfInterest[T] {
  
  require(pointsWithin.forall{pointWithin => boundaryWraps(pointWithin.location)})
  
  def boundaryWraps(candidate: GPSCoordinate): Boolean
  
  def pointsWithin: Seq[PointOfInterest[T]]
  
  def nWithin: Long = pointsWithin.length
  
  def hasAsMember(pointOfInterest: PointOfInterest[T]): Boolean = pointsWithin contains pointOfInterest
  
  def `with`(pointsOfInterest: Seq[PointOfInterest[T]]): K = {
    val pointsToReplaceWith = pointsOfInterest.filter(pointOfInterest => this boundaryWraps pointOfInterest.location)
    construct(pointsToReplaceWith)
  }
  
  def withAdded(pointsOfInterest: Seq[PointOfInterest[T]]): K = {
    val pointsToAdd = pointsOfInterest.filter(pointOfInterest => this boundaryWraps pointOfInterest.location)
    construct(pointsToAdd ++ pointsWithin)
  }
  
  protected def construct(allPointsWithin: Seq[PointOfInterest[T]]): K
}

case class Areas[T](areas: Seq[Area[T, _]]) {
  
  require(areas.map(_.id).distinct.length == areas.length)
  
  def contains(candidate: GPSCoordinate): Boolean = areas.exists(_.boundaryWraps(candidate))
  
}

object Areas {
  
  def apply[T, M <: Area[T, M]](area: M): Areas[T] = Areas(Seq(area))
  
}