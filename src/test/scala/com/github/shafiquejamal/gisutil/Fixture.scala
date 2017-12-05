package com.github.shafiquejamal.gisutil

import com.github.shafiquejamal.gisutil.location.{BoundingBox, GPSCoordinate, Lat, Lng}
import com.github.shafiquejamal.point.PointOfInterest

object Fixture {
  
  trait TestClassFixture {
    case class Person(override val id: String, override val location: GPSCoordinate) extends PointOfInterest[String]
  }
  
  trait PersonsOnDividedFrameFixture extends TestClassFixture {
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
  
  trait BoundingBoxFixture {
    
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
  }
  
  trait PersonsFixture extends TestClassFixture {
     val persons = Seq(
      Person("1_250_0_0", GPSCoordinate(Lat(39.11518), Lng(-94.62890))),
      Person("2_250_1_0", GPSCoordinate(Lat(39.11500), Lng(-94.62890))),
      Person("3_250_1_0", GPSCoordinate(Lat(39.11490), Lng(-94.62870))),
      Person("4_250_1_1", GPSCoordinate(Lat(39.11354), Lng(-94.62680))),
      Person("5_250_1_1", GPSCoordinate(Lat(39.11294), Lng(-94.62605))),
      Person("6_250_0_1", GPSCoordinate(Lat(39.11295), Lng(-94.62600))),
      Person("7_250_0_0", GPSCoordinate(Lat(39.11136), Lng(-94.62380))))
  }
  
}
