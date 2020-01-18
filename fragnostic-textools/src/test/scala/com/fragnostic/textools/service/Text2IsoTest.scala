package com.fragnostic.textools.service

import com.fragnostic.textools.service.agnostic.CakeService
import org.scalatest.{ FunSpec, Matchers }

class Text2IsoTest extends FunSpec with Matchers with TestSupport {

  val basePath = "/Users/fernandobrule/Clones/regulatorio/regulatorio-code/regulatorio-i18n/cl/atacamasoft/regulatorio/web/i18n/"
  val pathSrc: String = s"$basePath/mensajes-web-javascript_ru_RU.properties"
  val pathTgt: String = s"$basePath/mensajes-web-javascript_ru_RU.properties---iso"

  describe("Text 2 ISO Test") {

    it("Can Convert Text 2 ISO") {

      val answer = CakeService.text2IsoService.text2Iso(pathSrc, pathTgt, utf8) fold (
        error => error,
        success => success)

      answer should be("text.2.iso.success")

    }

  }

}