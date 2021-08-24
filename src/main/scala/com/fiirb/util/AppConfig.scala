package com.fiirb.util

import com.typesafe.config.ConfigFactory

object AppConfig {
  private lazy val conf = ConfigFactory.load()

  lazy val basePort: Int = conf.getInt("app.port")

  lazy val novelServiceBaseUrl: String = conf.getString("novel_service.base_url")

}
