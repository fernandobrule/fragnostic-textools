package com.fragnostic.textools.service.impl

import com.fragnostic.textools.service.api.NormalizeServiceApi

trait NormalizeServiceImpl extends NormalizeServiceApi {

  def normalizeService = new DefaultNormalizeService

  class DefaultNormalizeService extends NormalizeServiceApi {

    override def normaliza(path: String): Either[String, String] = {
      Left("Not Yet")
    }

  }

}
