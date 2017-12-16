package com.github.shafiquejamal.gisutil.location

import org.scalatest.{FlatSpecLike, Matchers}

class BoundingEllipseUTest extends FlatSpecLike with Matchers {

  "The bounding ellipse" should "accurately determine whether it contains a candidate point" in {
    val ellipse = BoundingEllipse("e1", GPSCoordinate(Lat(39.11405), Lng(-94.62746)), 2, 1, 20)
    val pointOutside = GPSCoordinate(Lat(39.120261171336146), Lng(-94.64946337892016))
    val pointInside = GPSCoordinate(Lat(39.120138217988696), Lng(-94.64902763079968))
    
    ellipse boundaryWraps pointOutside shouldBe false
    ellipse boundaryWraps pointInside shouldBe true
  }

}

