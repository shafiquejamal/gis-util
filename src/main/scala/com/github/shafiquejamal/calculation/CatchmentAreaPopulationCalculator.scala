package com.github.shafiquejamal.calculation

import com.github.shafiquejamal.gisutil.location.BoundingBox
import com.github.shafiquejamal.point._

class CatchmentAreaPopulationCalculator[T](pointsOfInterest: Seq[PointOfInterest[String]] = Seq()) {
  
  def in(areas: Seq[Area[String]]): Seq[AreaPopulation[String]] =
    areas map { area => AreaPopulation(area, (area `with` pointsOfInterest).nWithin) }
  
  def in(areas: Seq[PointOfInterest[T]], edgeSizesKm: Seq[Double]): Seq[SquareAreaCharacteristics[T]] = {
  
    areas.foldLeft(Seq[SquareAreaCharacteristics[T]]()) { case (accAreasCharacteristics, area) =>
      val catchmentAreas = edgeSizesKm.map { edgeSizeKm =>
        SquareAreaMeasures(edgeSizeKm, (BoundingBox.from(area.location, edgeSizeKm) `with` pointsOfInterest).nWithin)
      }
      accAreasCharacteristics :+ SquareAreaCharacteristics(area, catchmentAreas)
    }
  }
  
}

object CatchmentAreaPopulationCalculator {
  
  def apply() = new CatchmentAreaPopulationCalculator(Seq())
  
  def numberOf[T](pointOfInterest: Seq[PointOfInterest[String]]) = new CatchmentAreaPopulationCalculator[String](pointOfInterest)
  
}