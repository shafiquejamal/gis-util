package com.github.shafiquejamal.calculation

import com.github.shafiquejamal.gisutil.location.BoundingBox
import com.github.shafiquejamal.points.{Area, CatchmentArea, PointOfInterest, SurveyAreaCharacteristics}

object CatchmentAreaCalculator {
  
  def calculatePopulations[T](agents: Seq[PointOfInterest[T]], surveyAreas: Seq[Area[T]], edgeSizesKm: Seq[Double]):
  Seq[SurveyAreaCharacteristics[T]] = {
    
    surveyAreas.foldLeft(Seq[SurveyAreaCharacteristics[T]]()) { case (accSurveyAreasCharacteristics, surveyArea) =>
      val catchmentAreas = edgeSizesKm.map { edgeSizeKm =>
        val boundingBox = BoundingBox.from(surveyArea.center, edgeSizeKm)
        val nAgentsInCatchementArea = agents.count(agent => boundingBox contains agent.gPSCoordinates)
        CatchmentArea(edgeSizeKm, nAgentsInCatchementArea)
      }
      accSurveyAreasCharacteristics :+ SurveyAreaCharacteristics(surveyArea, catchmentAreas)
    }
    
  }
  
}
