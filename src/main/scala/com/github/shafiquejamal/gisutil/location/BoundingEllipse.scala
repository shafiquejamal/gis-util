package com.github.shafiquejamal.gisutil.location

import java.lang.Math._

import com.github.shafiquejamal.calculation.LocationCalculator
import com.github.shafiquejamal.points.Area

case class CartesianPoint(x: Double, y: Double)

case class BoundingEllipse(
    override val id: String,
    override val location: GPSCoordinate,
    kmSemiMajor: Double,
    kmSemiMinor: Double,
    degTiltFromWest: Double)
  extends Area[String] {
  
  private val distanceToFocus = sqrt(abs(pow(kmSemiMajor, 2) - pow(kmSemiMinor, 2)))
  
  override def contains(candidate: GPSCoordinate): Boolean = {
    val fA = LocationCalculator.locationFrom(degTiltFromWest, location, distanceToFocus)
    val fB = LocationCalculator.locationFrom(degTiltFromWest + 180, location, distanceToFocus)
    val distanceFromCandidateToFoci = (candidate kmDistanceTo fA) + (candidate kmDistanceTo fB)
    distanceFromCandidateToFoci < 2 * kmSemiMajor
  }
  
}
