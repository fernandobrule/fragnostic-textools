package com.fragnostic.textools.service.api

trait Iso2TextServiceApi {

  def iso2TextService: Iso2TextServiceApi

  trait Iso2TextServiceApi {

    def iso2Text(pathSrc: String, pathTgt: String, encoding: String): Either[String, String]

  }

}
