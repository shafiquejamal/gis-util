package com.github.shafiquejamal.gisutil.calculation

import com.github.shafiquejamal.calculation.CatchmentAreaPopulationCalculator
import com.github.shafiquejamal.gisutil.location._
import com.github.shafiquejamal.points._
import org.scalatest.{FlatSpecLike, Matchers}

class CatchmentAreaPopulationCalculatorUTest extends FlatSpecLike with Matchers {
  

  trait TestClassFixture {
  
    case class Person(override val id: String, override val location: GPSCoordinate) extends PointOfInterest[String]
    
  }
  
  trait CircleAndEllipseFixture extends TestClassFixture {
    val persons = Seq(
      Person("", GPSCoordinate(Lat(39.11330139958722), Lng(-94.62842482586905))),
      Person("", GPSCoordinate(Lat(39.11330139958722), Lng(-94.62746000000003))),
      Person("", GPSCoordinate(Lat(39.11330139958722), Lng(-94.62649517413101))),
      Person("", GPSCoordinate(Lat(39.11405), Lng(-94.62842482586905))),
      Person("", GPSCoordinate(Lat(39.11405), Lng(-94.62746000000003))),
      Person("", GPSCoordinate(Lat(39.11405), Lng(-94.62649517413101))),
      Person("", GPSCoordinate(Lat(39.11479860041278), Lng(-94.62842482586905))),
      Person("", GPSCoordinate(Lat(39.11479860041278), Lng(-94.62746000000003))),
      Person("", GPSCoordinate(Lat(39.11479860041278), Lng(-94.62649517413101))))
  }
  
  trait Fixture extends TestClassFixture {
    
    class SurveyAreaImpl(override val id: String, override val location: GPSCoordinate) extends PointOfInterest[String]
    
    object SurveyAreaImpl {
      def apply(id: String, center: GPSCoordinate) = new SurveyAreaImpl(id, center)
    }
    
    val surveyArea1 = SurveyAreaImpl("Kansas_KC_1", GPSCoordinate(Lat(39.11405), Lng(-94.62746)))
    val surveyArea2 = SurveyAreaImpl("Kansas_KC_2", GPSCoordinate(Lat(39.11244), Lng(-94.62537)))
    
    val surveyAreas = Seq(surveyArea1, surveyArea2)
    
    val persons = Seq(
      Person("1_250_0_0", GPSCoordinate(Lat(39.11518), Lng(-94.62890))),
      Person("2_250_1_0", GPSCoordinate(Lat(39.11500), Lng(-94.62890))),
      Person("3_250_1_0", GPSCoordinate(Lat(39.11490), Lng(-94.62870))),
      Person("4_250_1_1", GPSCoordinate(Lat(39.11354), Lng(-94.62680))),
      Person("5_250_1_1", GPSCoordinate(Lat(39.11294), Lng(-94.62605))),
      Person("6_250_0_1", GPSCoordinate(Lat(39.11295), Lng(-94.62600))),
      Person("7_250_0_0", GPSCoordinate(Lat(39.11136), Lng(-94.62380))))
  }
  
  "The catchment area calculator" should "calculate the number of people in each square catchment area of the given " +
  "size when given a point of interest and edge lengths" in
  new Fixture {
    val expected = Seq(
      SquareAreaCharacteristics(surveyArea1, Seq(SquareAreaMeasures(0.25, 4), SquareAreaMeasures(0.5, 6))),
      SquareAreaCharacteristics(surveyArea2, Seq(SquareAreaMeasures(0.25, 3), SquareAreaMeasures(0.5, 4)))
    )
    val actual =
      (CatchmentAreaPopulationCalculator numberOf persons).in(surveyAreas, Seq(0.25, 0.5))
    
    actual should contain theSameElementsInOrderAs expected
  }
  
  it should "calculate the number of people in the given bounding boxes" in new Fixture {
    val centerPoint = GPSCoordinate(Lat(39.11405), Lng(-94.62746))
    val bBox250m = BoundingBox(
      GPSCoordinate(Lat(39.112927099380826), Lng(-94.62890723880348)),
      GPSCoordinate(Lat(39.11517290061917), Lng(-94.62890723880348)),
      GPSCoordinate(Lat(39.11517290061917), Lng(-94.62601276119655)),
      GPSCoordinate(Lat(39.112927099380826), Lng(-94.62601276119655)), 0.250, centerPoint)
    val bBox500m = BoundingBox(
      GPSCoordinate(Lat(39.11180419876166), Lng(-94.63035447760728)),
      GPSCoordinate(Lat(39.116295801238344), Lng(-94.63035447760728)),
      GPSCoordinate(Lat(39.116295801238344), Lng(-94.62456552239274)),
      GPSCoordinate(Lat(39.11180419876166), Lng(-94.62456552239274)), 0.500, centerPoint)
    val boundingBoxes = Seq(bBox250m, bBox500m)

    val expected = Seq(AreaPopulation(bBox250m, 4), AreaPopulation(bBox500m, 6))
    
    CatchmentAreaPopulationCalculator numberOf persons in boundingBoxes should contain theSameElementsInOrderAs expected
  }
  
  it should "calculate the number of people in the given bounding circles" in new CircleAndEllipseFixture {
    val circle100m = BoundingCircle("200m", GPSCoordinate(Lat(39.11350139958722), Lng(-94.62650517413101)), 0.1)
    val circle105m = BoundingCircle("200m", GPSCoordinate(Lat(39.11350139958722), Lng(-94.62650517413101)), 0.105)
    val circles = Seq(circle100m, circle105m)
    
    val expected = Seq(AreaPopulation(circle100m, 3), AreaPopulation(circle105m, 4))
    
    CatchmentAreaPopulationCalculator numberOf persons in circles should contain theSameElementsInOrderAs expected
  }
  
  it should "calculate the number of people in the given bounding ellipses" in new CircleAndEllipseFixture {
    val ellipseBig = BoundingEllipse("eb", GPSCoordinate(Lat(39.11405), Lng(-94.62746)), 0.2, 0.1, 20)
    val ellipseSmall = BoundingEllipse("es", GPSCoordinate(Lat(39.11405), Lng(-94.62746)), 0.1, 0.05, 20)
    val ellipses = Seq(ellipseBig, ellipseSmall)
    val expected = Seq(AreaPopulation(ellipseBig, 7), AreaPopulation(ellipseSmall, 3))
    
    CatchmentAreaPopulationCalculator numberOf persons in ellipses should contain theSameElementsInOrderAs expected
  }
}
