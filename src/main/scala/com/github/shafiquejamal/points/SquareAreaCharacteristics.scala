package com.github.shafiquejamal.points

case class SquareAreaCharacteristics[T](
    pointOfInterest: PointOfInterest[T],
    catchmentAreasMeasures: Seq[SquareAreaMeasures])

