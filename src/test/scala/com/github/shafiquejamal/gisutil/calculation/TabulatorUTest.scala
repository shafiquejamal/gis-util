package com.github.shafiquejamal.gisutil.calculation

import calculation.CatchmentAreaCalculator
import com.github.shafiquejamal.gisutil.location.{GPSCoordinates, Lat, Lng}
import org.scalatest.{FlatSpecLike, Matchers}
import points.{Area, CatchmentArea, PointOfInterest, SurveyAreaCharacteristics}

class TabulatorUTest extends FlatSpecLike with Matchers {
  
  trait Fixture {
    
    class SurveyAreaImpl(override val id: String, override val center: GPSCoordinates) extends Area[String]
    
    object SurveyAreaImpl {
      def apply(id: String, center: GPSCoordinates) = new SurveyAreaImpl(id, center)
    }
    
    val surveyArea1 = SurveyAreaImpl("Kansas_KC_1", GPSCoordinates(Lat(39.11405), Lng(-94.62746)))
    val surveyArea2 = SurveyAreaImpl("Kansas_KC_2", GPSCoordinates(Lat(39.11244), Lng(-94.62537)))
    
    val surveyAreas = Seq(surveyArea1, surveyArea2)
    
    class Person(override val id: String, override val gPSCoordinates: GPSCoordinates) extends PointOfInterest[String]
    
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
      Person("7_250_0_0", GPSCoordinates(Lat(39.11136), Lng(-94.6238))),
    )
  }
  
  "The catchment area calculator" should "calculate the number of people in each catchment area of the given size" in
  new Fixture {
    val expected = Seq(
      SurveyAreaCharacteristics(surveyArea1, Seq(CatchmentArea(0.25, 4), CatchmentArea(0.5, 6))),
      SurveyAreaCharacteristics(surveyArea2, Seq(CatchmentArea(0.25, 3), CatchmentArea(0.5, 4))),
    )
    val actual = CatchmentAreaCalculator.calculatePopulations(persons, surveyAreas, Seq(0.25, 0.5))
    
    actual should contain theSameElementsInOrderAs expected
  }
  
}
