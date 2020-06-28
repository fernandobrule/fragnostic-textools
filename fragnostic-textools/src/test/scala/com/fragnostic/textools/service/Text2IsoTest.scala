package com.fragnostic.textools.service

import com.fragnostic.textools.service.agnostic.CakeService
import org.scalatest.{ FunSpec, Matchers }

class Text2IsoTest extends FunSpec with Matchers with TestSupport {

  val basePath = "/Users/fernandobrule/Clones/futurepyme/futurepyme-office/futurepyme-office-web-i18n-resources/cl/atacamasoft/fp/web/i18n"
  val pathSrc: String = s"$basePath/futurepyme-web-mensajes_en_US.properties"
  val pathTgt: String = s"$basePath/futurepyme-web-mensajes_en_US.properties---iso"

  describe("Text 2 ISO Test") {

    it("Can Convert Text 2 ISO") {

      val answer = CakeService.text2IsoService.text2Iso(pathSrc, pathTgt, utf8) fold (
        error => error,
        success => success)

      answer should be("text.2.iso.success")

    }

  }

}