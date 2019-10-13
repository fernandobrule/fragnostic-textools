package com.fragnostic.textools.service

import com.fragnostic.textools.service.agnostic.CakeService
import org.scalatest.{ FunSpec, Matchers }

class Text2IsoTest extends FunSpec with Matchers {

  val base = "/Users/fernandobrule/Clones/fragnostic/fragnostic-code/fragnostic-textools/fragnostic-textools"
  val pathSrc: String = s"$base/src/test/resources/portugues-original.txt"
  val pathTgt: String = s"$base/target/portugues-iso.txt"

  describe("Parse Text 2 ISO Test") {

    it("Can Parse Text 2 ISO") {

      val answer = CakeService.text2IsoService.text2Iso(pathSrc, pathTgt) fold (
        error => error,
        success => success)

      answer should be("text.2.iso.success")

    }

  }

}