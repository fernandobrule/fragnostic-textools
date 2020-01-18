package com.fragnostic.textools.service

import com.fragnostic.textools.service.agnostic.CakeService
import org.scalatest.{ FunSpec, Matchers }

class Text2PlainTest extends FunSpec with Matchers with TestSupport {

  val base = "/Users/fernandobrule/Clones/fragnostic/fragnostic-code/fragnostic-textools/fragnostic-textools"
  val pathSrc: String = s"$base/src/test/resources/portugues-original.txt"
  val pathTgt: String = s"$base/target/portugues-plain.txt"

  describe("Text 2 Plain Test") {

    it("Can Convert Text 2 ISO") {

      val answer = CakeService.text2PlainService.text2Plain(pathSrc, pathTgt, utf8) fold (
        error => error,
        success => success)

      answer should be("text.2.plain.success")

    }

  }

}
