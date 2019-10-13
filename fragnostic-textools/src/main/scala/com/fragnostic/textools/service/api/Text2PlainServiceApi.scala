package com.fragnostic.textools.service.api

trait Text2PlainServiceApi {

  def text2PlainService: Text2PlainServiceApi

  trait Text2PlainServiceApi {

    def text2Plain(pathSrc: String, pathTgt: String): Either[String, String]

  }

}
