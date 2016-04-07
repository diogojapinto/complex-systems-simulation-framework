package com.cssim.services

import com.cssim.SystemManager


abstract class ServicesModule {
  this: SystemManager =>

  val moduleServices: List[Service]

  services.append(moduleServices: _*)
}
