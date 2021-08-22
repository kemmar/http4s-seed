package com.fiirb.util

import com.typesafe.config.ConfigFactory

object AppConfig {
  private lazy val conf = ConfigFactory.load()

  lazy val basePort: Int = conf.getInt("app.port")

  lazy val csCardsBaseUrl: String = conf.getString("cs_cards.base_url")

  lazy val scoredCardsBaseUrl: String = conf.getString("scored_cards.base_url")

}
