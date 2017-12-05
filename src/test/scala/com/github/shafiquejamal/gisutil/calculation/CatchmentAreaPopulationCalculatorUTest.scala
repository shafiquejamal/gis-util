package com.github.shafiquejamal.gisutil.calculation

import com.github.shafiquejamal.calculation.CatchmentAreaPopulationCalculator
import com.github.shafiquejamal.gisutil.Fixture.{BoundingBoxFixture, PersonsFixture, PersonsOnDividedFraymFixture, TestClassFixture}
import com.github.shafiquejamal.gisutil.location._
import com.github.shafiquejamal.point._
import org.scalatest.{FlatSpecLike, Matchers}

class CatchmentAreaPopulationCalculatorUTest extends FlatSpecLike with Matchers {
  
  trait Fixture extends PersonsFixture {
    
    class SurveyAreaImpl(override val id: String, override val location: GPSCoordinate) extends PointOfInterest[String]
    
    object SurveyAreaImpl {
      def apply(id: String, center: GPSCoordinate) = new SurveyAreaImpl(id, center)
    }
    
    val surveyArea1 = SurveyAreaImpl("Kansas_KC_1", GPSCoordinate(Lat(39.11405), Lng(-94.62746)))
    val surveyArea2 = SurveyAreaImpl("Kansas_KC_2", GPSCoordinate(Lat(39.11244), Lng(-94.62537)))
    
    val surveyAreas = Seq(surveyArea1, surveyArea2)
  }
  
  "The catchment area calculator" should "calculate the number of people in each square catchment area of the given " +
  "size when given a point of interest and edge lengths" in new Fixture {
    val expected = Seq(
      SquareAreaCharacteristics(surveyArea1, Seq(SquareAreaMeasures(0.25, 4), SquareAreaMeasures(0.5, 6))),
      SquareAreaCharacteristics(surveyArea2, Seq(SquareAreaMeasures(0.25, 3), SquareAreaMeasures(0.5, 4)))
    )
    val actual =
      (CatchmentAreaPopulationCalculator numberOf persons).in(surveyAreas, Seq(0.25, 0.5))
    
    actual should contain theSameElementsInOrderAs expected
  }
  
  it should "calculate the number of people in the given bounding boxes" in new Fixture with BoundingBoxFixture {
  
    val boundingBoxes = Seq(bBox250m, bBox500m)
    val expected = Seq(AreaPopulation(bBox250m, 4), AreaPopulation(bBox500m, 6))
    
    CatchmentAreaPopulationCalculator numberOf persons in boundingBoxes should contain theSameElementsInOrderAs expected
  }
  
  it should "calculate the number of people in the given bounding circles" in new PersonsOnDividedFraymFixture {
    val circle100m = BoundingCircle("200m", GPSCoordinate(Lat(39.11350139958722), Lng(-94.62650517413101)), 0.1)
    val circle105m = BoundingCircle("200m", GPSCoordinate(Lat(39.11350139958722), Lng(-94.62650517413101)), 0.105)
    val circles = Seq(circle100m, circle105m)
    
    val expected = Seq(AreaPopulation(circle100m, 3), AreaPopulation(circle105m, 4))
    
    CatchmentAreaPopulationCalculator numberOf persons in circles should contain theSameElementsInOrderAs expected
  }
  
  it should "calculate the number of people in the given bounding ellipses" in new PersonsOnDividedFraymFixture {
    val ellipseBig = BoundingEllipse("eb", GPSCoordinate(Lat(39.11405), Lng(-94.62746)), 0.2, 0.1, 20)
    val ellipseSmall = BoundingEllipse("es", GPSCoordinate(Lat(39.11405), Lng(-94.62746)), 0.1, 0.05, 20)
    val ellipses = Seq(ellipseBig, ellipseSmall)
    val expected = Seq(AreaPopulation(ellipseBig, 7), AreaPopulation(ellipseSmall, 3))
    
    CatchmentAreaPopulationCalculator numberOf persons in ellipses should contain theSameElementsInOrderAs expected
  }
}
