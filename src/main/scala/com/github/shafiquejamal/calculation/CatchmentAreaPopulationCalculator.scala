package com.github.shafiquejamal.calculation

import com.github.shafiquejamal.gisutil.location.BoundingBox
import com.github.shafiquejamal.point._

class CatchmentAreaPopulationCalculator[T](pointsOfInterest: Seq[PointOfInterest[T]] = Seq()) {
  
  def in(areas: Seq[Area[T]]): Seq[AreaPopulation[T]] =
    areas map { area =>
      AreaPopulation(area, pointsOfInterest.count(pointOfInterest => area.contains(pointOfInterest.location))) }
  
  def in(areas: Seq[PointOfInterest[T]], edgeSizesKm: Seq[Double]): Seq[SquareAreaCharacteristics[T]] = {
  
    areas.foldLeft(Seq[SquareAreaCharacteristics[T]]()) { case (accAreasCharacteristics, area) =>
      val catchmentAreas = edgeSizesKm.map { edgeSizeKm =>
        val boundingBox = BoundingBox.from(area.location, edgeSizeKm)
        val nPointsOfInterestInCatchementArea =
          pointsOfInterest.count(pointOfInterest => boundingBox contains pointOfInterest.location)
        SquareAreaMeasures(edgeSizeKm, nPointsOfInterestInCatchementArea)
      }
      accAreasCharacteristics :+ SquareAreaCharacteristics(area, catchmentAreas)
    }
  }
  
}

object CatchmentAreaPopulationCalculator {
  
  def apply() = new CatchmentAreaPopulationCalculator(Seq())
  
  def numberOf[T](pointOfInterest: Seq[PointOfInterest[T]]) = new CatchmentAreaPopulationCalculator[T](pointOfInterest)
  
}