package com.github.shafiquejamal.point

case class AreaPopulation[T, M <: Area[T, M]](area: Area[T, M], population: Long)
