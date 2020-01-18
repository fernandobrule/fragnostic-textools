package com.fragnostic.textools.service

import com.fragnostic.textools.service.agnostic.CakeService
import org.scalatest.{ FunSpec, Matchers }

class Iso2TextTest extends FunSpec with Matchers with TestSupport {

  val base = "/Users/fernandobrule/Clones/fragnostic/fragnostic-code/fragnostic-textools/fragnostic-textools"
  val pathSrc: String = s"$base/target/portugues-iso.txt"
  val pathTgt: String = s"$base/target/portugues-text.txt"

  describe("ISO 2 Text Test") {

    it("Can Convert ISO 2 Text") {

      val answer = CakeService.iso2TextService.iso2Text(pathSrc, pathTgt, utf8) fold (
        error => error,
        success => success)

      answer should be("iso.2.text.success")

    }

  }

}