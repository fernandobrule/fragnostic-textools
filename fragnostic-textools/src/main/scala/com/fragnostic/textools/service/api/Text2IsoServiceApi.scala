package com.fragnostic.textools.service.api

trait Text2IsoServiceApi {

  def text2IsoService: Text2IsoServiceApi

  trait Text2IsoServiceApi {

    def text2Iso(pathSrc: String, pathTgt: String): Either[String, String]

  }

}
