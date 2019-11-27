package com.fragnostic.textools.service

import com.fragnostic.textools.service.agnostic.CakeService
import org.scalatest.{ FunSpec, Matchers }

class Text2IsoTest extends FunSpec with Matchers {

  val basePath: String = "/home/fernandobrule/Covenant/rebase/enUS"
  /*
  // strings_en_US.properties
  val pathSrc: String = s"$basePath/strings_en_US.properties"
  val pathTgt: String = s"$basePath/strings_en_US.properties.text2iso"

  // strings_en_US_LOCAL_14453.properties
  val pathSrc: String = s"$basePath/strings_en_US_LOCAL_14453.properties"
  val pathTgt: String = s"$basePath/strings_en_US_LOCAL_14453.properties.text2iso"
  */
  // strings_en_US_REMOTE_14453.properties
  val pathSrc: String = s"$basePath/strings_en_US_REMOTE_14453.properties"
  val pathTgt: String = s"$basePath/strings_en_US_REMOTE_14453.properties.text2iso"

  val cp1252 = "cp1252"

  describe("Text 2 ISO Test") {

    it("Can Convert Text 2 ISO") {

      val answer = CakeService.text2IsoService.text2Iso(pathSrc, pathTgt, cp1252) fold (
        error => error,
        success => success)

      answer should be("text.2.iso.success")

    }

  }

}