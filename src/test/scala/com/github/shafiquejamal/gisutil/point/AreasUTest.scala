package com.github.shafiquejamal.gisutil.point

import com.github.shafiquejamal.gisutil.location._
import com.github.shafiquejamal.point.Areas
import org.scalatest.{FlatSpecLike, Matchers}

class AreasUTest extends FlatSpecLike with Matchers {
  
  "Areas" should "contain only unique ids" in {
  
    val centerPoint = GPSCoordinate(Lat(39.11405), Lng(-94.62746))
    val bBox250m = BoundingBox(
      GPSCoordinate(Lat(39.112927099380826), Lng(-94.62890723880348)),
      GPSCoordinate(Lat(39.11517290061917), Lng(-94.62890723880348)),
      GPSCoordinate(Lat(39.11517290061917), Lng(-94.62601276119655)),
      GPSCoordinate(Lat(39.112927099380826), Lng(-94.62601276119655)), 0.250, centerPoint).copy(id = "1")
    
    val pointInBoth = GPSCoordinate(Lat(39.11405), Lng(-94.62746))
    val pointInBoxOnly = GPSCoordinate(Lat(39.11330139958722), Lng(-94.62842482586905))
    val pointInEllipseOnly = GPSCoordinate(Lat(39.11330139958722), Lng(-94.62549517413101))
    val pointInNeither = GPSCoordinate(Lat(39.11330139958722), Lng(-94.62449517413101))
    val ellipse1 = BoundingEllipse("1", pointInBoth, 2, 1, 20)
    val ellipse2a = BoundingEllipse("2a", GPSCoordinate(Lat(39.11405), Lng(-94.62746)), 0.2, 0.1, 20)
  
    a[RuntimeException] shouldBe thrownBy(Areas(Seq(ellipse1, bBox250m)))
    
    Areas(Seq(bBox250m, ellipse2a)) contains pointInBoth shouldBe true
    Areas(Seq(bBox250m, ellipse2a)) contains pointInBoxOnly shouldBe true
    Areas(Seq(bBox250m, ellipse2a)) contains pointInEllipseOnly shouldBe true
    Areas(Seq(bBox250m, ellipse2a)) contains pointInNeither shouldBe false
    
  }
  
}
