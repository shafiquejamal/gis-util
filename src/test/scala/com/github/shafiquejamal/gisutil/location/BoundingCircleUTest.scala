package com.github.shafiquejamal.gisutil.location

import org.scalatest.{FlatSpecLike, Matchers}

class BoundingCircleUTest extends FlatSpecLike with Matchers {
  
  "The bounding circle" should "accurately determine whether it contains a candidate point" in {
    val circle = BoundingCircle("c1", GPSCoordinate(Lat(39.11405), Lng(-94.62746)), 2)
    val pointOutside = GPSCoordinate(Lat(39.120261171336146), Lng(-94.64946337892016))
    val pointInside = GPSCoordinate(Lat(39.120138217988696), Lng(-94.64902763079968))
  
    circle boundaryWraps pointOutside shouldBe false
    circle boundaryWraps pointInside shouldBe true
  }
  
}
