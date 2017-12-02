package com.github.shafiquejamal.gisutil.location

import com.github.shafiquejamal.calculation.LocationCalculator.intermediatePoint
import com.github.shafiquejamal.points.PointOfInterest

case class CatchmentArea(
    override val id: String,
    override val location: GPSCoordinates,
    edgeLengthKm: Double)
  extends PointOfInterest[String]

object CatchmentArea {
  
  def from(centerPoint: GPSCoordinates, edgeLengthKm: Double, nSlicesOfEachSide: Int): Seq[CatchmentArea] = {
    val bb = BoundingBox.from(centerPoint, edgeLengthKm)
    from(bb, nSlicesOfEachSide)
  }
  
  def from(bb: BoundingBox, nSlicesOfEachSide: Int): Seq[CatchmentArea] = {
    val catchmentAreaEdgeLength = bb.edgeLengthKm / nSlicesOfEachSide
    val portionsOfDistance = 1.to(2 * nSlicesOfEachSide - 1, 2).map(_ / (2d * nSlicesOfEachSide)).toSeq
    val positionsAlongTopAndLeftEdges = portionsOfDistance.map(portion =>
      (intermediatePoint(bb.nW, bb.nE, portion), intermediatePoint(bb.sW, bb.nW, portion)))
    positionsAlongTopAndLeftEdges.flatMap { case (_, leftEdgeCoord) =>
      positionsAlongTopAndLeftEdges.map { case (topEdgeCoordinate, _) =>
        GPSCoordinates(leftEdgeCoord.lat, topEdgeCoordinate.lng)
      }
    }.zipWithIndex.map { case (centerPoint, index) =>
      CatchmentArea((index + 1).toString, centerPoint, catchmentAreaEdgeLength) }
  }
  
}
