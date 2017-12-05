package com.github.shafiquejamal.gisutil.location

import java.lang.Math._

import com.github.shafiquejamal.calculation.LocationCalculator
import com.github.shafiquejamal.point.{Area, PointOfInterest}

case class CartesianPoint(x: Double, y: Double)

case class BoundingEllipse(
    override val id: String,
    override val location: GPSCoordinate,
    kmSemiMajor: Double,
    kmSemiMinor: Double,
    degTiltFromWest: Double,
    pointsWithin: Seq[PointOfInterest[String]] = Seq())
  extends Area[String] {
  
  private val distanceToFocus = sqrt(abs(pow(kmSemiMajor, 2) - pow(kmSemiMinor, 2)))
  
  override def boundaryWraps(candidate: GPSCoordinate): Boolean = {
    val fA = LocationCalculator.locationFrom(degTiltFromWest, location, distanceToFocus)
    val fB = LocationCalculator.locationFrom(degTiltFromWest + 180, location, distanceToFocus)
    val distanceFromCandidateToFoci = (candidate kmDistanceTo fA) + (candidate kmDistanceTo fB)
    distanceFromCandidateToFoci < 2 * kmSemiMajor
  }
  
  protected def construct(allPointsWithin: Seq[PointOfInterest[String]]): Area[String] =
    this.copy(pointsWithin = allPointsWithin)
  
}
