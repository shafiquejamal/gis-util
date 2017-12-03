package com.github.shafiquejamal.gisutil.calculation

import com.github.shafiquejamal.calculation.CatchmentAreaPopulationCalculator
import com.github.shafiquejamal.gisutil.location.{BoundingBox, GPSCoordinates, Lat, Lng}
import com.github.shafiquejamal.points._
import org.scalatest.{FlatSpecLike, Matchers}

class CatchmentAreaPopulationCalculatorUTest extends FlatSpecLike with Matchers {
  
  trait Fixture {
    
    class SurveyAreaImpl(override val id: String, override val location: GPSCoordinates) extends PointOfInterest[String]
    
    object SurveyAreaImpl {
      def apply(id: String, center: GPSCoordinates) = new SurveyAreaImpl(id, center)
    }
    
    val surveyArea1 = SurveyAreaImpl("Kansas_KC_1", GPSCoordinates(Lat(39.11405), Lng(-94.62746)))
    val surveyArea2 = SurveyAreaImpl("Kansas_KC_2", GPSCoordinates(Lat(39.11244), Lng(-94.62537)))
    
    val surveyAreas = Seq(surveyArea1, surveyArea2)
    
    class Person(override val id: String, override val location: GPSCoordinates) extends PointOfInterest[String]
    
    object Person {
      def apply(id: String, gPSCoordinates: GPSCoordinates) = new Person(id, gPSCoordinates)
    }
    
    val persons = Seq(
      Person("1_250_0_0", GPSCoordinates(Lat(39.11518), Lng(-94.62890))),
      Person("2_250_1_0", GPSCoordinates(Lat(39.11500), Lng(-94.62890))),
      Person("3_250_1_0", GPSCoordinates(Lat(39.11490), Lng(-94.62870))),
      Person("4_250_1_1", GPSCoordinates(Lat(39.11354), Lng(-94.62680))),
      Person("5_250_1_1", GPSCoordinates(Lat(39.11294), Lng(-94.62605))),
      Person("6_250_0_1", GPSCoordinates(Lat(39.11295), Lng(-94.62600))),
      Person("7_250_0_0", GPSCoordinates(Lat(39.11136), Lng(-94.62380))))
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
    val centerPoint = GPSCoordinates(Lat(39.11405), Lng(-94.62746))
    val bBox250m = BoundingBox(
      GPSCoordinates(Lat(39.112927099380826),Lng(-94.62890723880348)),
      GPSCoordinates(Lat(39.11517290061917),Lng(-94.62890723880348)),
      GPSCoordinates(Lat(39.11517290061917),Lng(-94.62601276119655)),
      GPSCoordinates(Lat(39.112927099380826),Lng(-94.62601276119655)), 0.250, centerPoint)
    val bBox500m = BoundingBox(
      GPSCoordinates(Lat(39.11180419876166),Lng(-94.63035447760728)),
      GPSCoordinates(Lat(39.116295801238344),Lng(-94.63035447760728)),
      GPSCoordinates(Lat(39.116295801238344),Lng(-94.62456552239274)),
      GPSCoordinates(Lat(39.11180419876166),Lng(-94.62456552239274)), 0.500, centerPoint)
    val boundingBoxes = Seq(bBox250m, bBox500m)

    val expected = Seq(AreaPopulation(bBox250m, 4), AreaPopulation(bBox500m, 6))
    
    CatchmentAreaPopulationCalculator numberOf persons in boundingBoxes should contain theSameElementsInOrderAs expected
  }
  
}
