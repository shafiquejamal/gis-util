package com.github.shafiquejamal.point

case class SquareAreaCharacteristics[T](
    pointOfInterest: PointOfInterest[T],
    catchmentAreasMeasures: Seq[SquareAreaMeasures])

