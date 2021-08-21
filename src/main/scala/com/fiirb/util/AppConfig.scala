package com.fiirb.util

import com.typesafe.config.ConfigFactory

object AppConfig {
  private val conf = ConfigFactory.load()

  val basePort: Int = conf.getInt("app.port")

  val csCardsBaseUrl: String = conf.getString("cs_cards.base_url")

  val scoredCardsBaseUrl: String = conf.getString("scored_cards.base_url")

}
