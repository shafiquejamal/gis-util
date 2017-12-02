package com.github.shafiquejamal.calculation

import com.github.shafiquejamal.gisutil.location.BoundingBox
import com.github.shafiquejamal.points.{Area, AreaCharacteristics, AreaMeasures, PointOfInterest}

object CatchmentAreaCalculator {
  
  def calculateNPointsOfInterestWithinAreas[T](
      pointsOfInterest: Seq[PointOfInterest[T]],
      areas: Seq[PointOfInterest[T]],
      edgeSizesKm: Seq[Double]):
  Seq[AreaCharacteristics[T]] = {
  
    areas.foldLeft(Seq[AreaCharacteristics[T]]()) { case (accAreasCharacteristics, area) =>
      val catchmentAreas = edgeSizesKm.map { edgeSizeKm =>
        val boundingBox = BoundingBox.from(area.location, edgeSizeKm)
        val nPointsOfInterestInCatchementArea =
          pointsOfInterest.count(pointOfInterest => boundingBox contains pointOfInterest.location)
        AreaMeasures(edgeSizeKm, nPointsOfInterestInCatchementArea)
      }
      accAreasCharacteristics :+ AreaCharacteristics(area, catchmentAreas)
    }
    
  }
  
}
