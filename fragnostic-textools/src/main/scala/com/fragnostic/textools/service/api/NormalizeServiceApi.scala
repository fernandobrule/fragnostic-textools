package com.fragnostic.textools.service.api

trait NormalizeServiceApi {

  def normalizeService: NormalizeServiceApi

  trait NormalizeServiceApi {

    def normaliza(path: String): Either[String, String]

  }

}
