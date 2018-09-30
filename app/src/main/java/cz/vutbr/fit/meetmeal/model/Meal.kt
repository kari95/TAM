package cz.vutbr.fit.meetmeal.model

import org.joda.time.*

abstract class Meal (
  val name: String
)

class NowMeal(
  name: String,
  val time: DateTime
): Meal(name)

class PlannedMeal(
  name: String,
  val time: DateTime
): Meal(name)