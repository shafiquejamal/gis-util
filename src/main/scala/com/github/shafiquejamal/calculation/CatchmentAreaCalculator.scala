package com.github.shafiquejamal.calculation

import com.github.shafiquejamal.gisutil.location.BoundingBox
import com.github.shafiquejamal.points.{Area, AreaMeasures, PointOfInterest, AreaCharacteristics}

object CatchmentAreaCalculator {
  
  def calculateNPointsOfInterestWithinAreas[T](
      pointsOfInterest: Seq[PointOfInterest[T]],
      areas: Seq[Area[T]],
      edgeSizesKm: Seq[Double]):
  Seq[AreaCharacteristics[T]] = {
  
    areas.foldLeft(Seq[AreaCharacteristics[T]]()) { case (accAreasCharacteristics, area) =>
      val catchmentAreas = edgeSizesKm.map { edgeSizeKm =>
        val boundingBox = BoundingBox.from(area.center, edgeSizeKm)
        val nPointsOfInterestInCatchementArea =
          pointsOfInterest.count(pointOfInterest => boundingBox contains pointOfInterest.gPSCoordinates)
        AreaMeasures(edgeSizeKm, nPointsOfInterestInCatchementArea)
      }
      accAreasCharacteristics :+ AreaCharacteristics(area, catchmentAreas)
    }
    
  }
  
}
