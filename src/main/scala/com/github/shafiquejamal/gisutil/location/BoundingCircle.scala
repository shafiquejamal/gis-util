package com.github.shafiquejamal.gisutil.location

import com.github.shafiquejamal.point.{Area, PointOfInterest}

case class  BoundingCircle(
    override val id: String,
    override val location: GPSCoordinate,
    kmRadius: Double,
    override val pointsWithin: Seq[PointOfInterest[String]] = Seq())
  extends Area[String] {
  
  override def boundaryWraps(candidate: GPSCoordinate): Boolean = (location kmDistanceTo candidate) < kmRadius
  
  protected def construct(allPointsWithin: Seq[PointOfInterest[String]]): Area[String] =
    this.copy(pointsWithin = allPointsWithin)
  
}
