package com.github.shafiquejamal.gisutil.location

import java.lang.Math._

import com.github.shafiquejamal.gisutil.location.Constants.Rkm

object CatchmentCentersCreator {
  
  def from(centerPoint: GPSCoordinates, edgeLengthKm: Double, nSlicesOfEachSide: Int): Seq[GPSCoordinates] = {
    val bb = BoundingBox.from(centerPoint, edgeLengthKm)
    from(bb, nSlicesOfEachSide)
  }
  
  def from(bb: BoundingBox, nSlicesOfEachSide: Int): Seq[GPSCoordinates] = {
    val portionsOfDistance = 1.to(2 * nSlicesOfEachSide - 1, 2).map(_ / (2d * nSlicesOfEachSide)).toSeq
    val positionsAlongTopAndLeftEdges = portionsOfDistance.map(portion =>
      (intermediatePoint(bb.nW, bb.nE, portion), intermediatePoint(bb.sW, bb.nW, portion)))
    positionsAlongTopAndLeftEdges.flatMap { case (_, leftEdgeCoord) =>
      positionsAlongTopAndLeftEdges.map { case (topEdgeCoordinate, _) =>
        GPSCoordinates(leftEdgeCoord.lat, topEdgeCoordinate.lng)
      }
    }
  }

  // From https://www.movable-type.co.uk/scripts/latlong.html
  def intermediatePoint(pointA: GPSCoordinates, pointB: GPSCoordinates, f: Double): GPSCoordinates = {
    val dkm = pointA kmDistanceTo pointB
    val delta = dkm / Rkm
    val a = sin((1 - f) * delta) / sin(delta)
    val b = sin(f * delta) / sin(delta)
    val phi1 = pointA.lat.value.toRadians
    val phi2 = pointB.lat.value.toRadians
    val lambda1 = pointA.lng.value.toRadians
    val lambda2 = pointB.lng.value.toRadians
    val x = a * cos(phi1) * cos(lambda1) + b * cos(phi2) * cos(lambda2)
    val y = a * cos(phi1) * sin(lambda1) + b * cos(phi2) * sin(lambda2)
    val z = a * sin(phi1) + b * sin(phi2)
    val phiI = atan2(z, sqrt(pow(x, 2) + pow(y, 2)))
    val lambdaI = atan2(y, x)
    GPSCoordinates(Lat(phiI.toDegrees), Lng(lambdaI.toDegrees))
  }
  
  
}
