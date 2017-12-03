package com.github.shafiquejamal.gisutil.location

import com.github.shafiquejamal.calculation.LocationCalculator.intermediatePoint
import com.github.shafiquejamal.points.PointOfInterest

case class CatchmentCenter(
    override val id: String,
    override val location: GPSCoordinates,
    edgeLengthKm: Double)
  extends PointOfInterest[String] {
  
  def toSeq: Seq[String] = Seq(id, location.lat.value.toString, location.lng.value.toString, edgeLengthKm.toString)
  
}

object CatchmentCenter {
  
  def cSVHeader: Seq[String] = Seq("id", "latitude", "longitude", "edgeLength (km)")
  
  def from(centerPoint: GPSCoordinates, edgeLengthKm: Double, nSlicesOfEachSide: Int): Seq[CatchmentCenter] = {
    val bb = BoundingBox.from(centerPoint, edgeLengthKm)
    from(bb, nSlicesOfEachSide)
  }
  
  def from(bb: BoundingBox, nSlicesOfEachSide: Int): Seq[CatchmentCenter] = {
    val catchmentAreaEdgeLength = bb.edgeLengthKm / nSlicesOfEachSide
    val portionsOfDistance = 1.to(2 * nSlicesOfEachSide - 1, 2).map(_ / (2d * nSlicesOfEachSide)).toSeq
    val positionsAlongTopAndLeftEdges = portionsOfDistance.map(portion =>
      (intermediatePoint(bb.nW, bb.nE, portion), intermediatePoint(bb.sW, bb.nW, portion)))
    positionsAlongTopAndLeftEdges.flatMap { case (_, leftEdgeCoord) =>
      positionsAlongTopAndLeftEdges.map { case (topEdgeCoordinate, _) =>
        GPSCoordinates(leftEdgeCoord.lat, topEdgeCoordinate.lng)
      }
    }.map { centerPoint =>
      CatchmentCenter(centerPoint.makeId, centerPoint, catchmentAreaEdgeLength) }
  }
  
}