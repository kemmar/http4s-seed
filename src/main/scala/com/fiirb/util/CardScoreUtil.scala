package com.fiirb.util

import scala.math.BigDecimal.RoundingMode

trait CardScoreUtil {

  def apr: Double

  def createCardScore(eligibility: Double): BigDecimal = BigDecimal(eligibility * {
    val value = (1 / apr)
    value * value
  }).setScale(3, RoundingMode.DOWN)
}
