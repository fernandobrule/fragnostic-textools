package com.fragnostic.textools.service

import com.fragnostic.textools.service.agnostic.CakeService
import org.scalatest.{ FunSpec, Matchers }

class Text2IsoTest extends FunSpec with Matchers {

  val pathSrc: String = "/home/fernandobrule/Clones/branch_v3_23_16_20191106_TZZHXNMS-674/v3---rebase-with-master/core/src/br/com/padtec/v3/util/strings_pt_BR.properties"
  val pathTgt: String = "/home/fernandobrule/Covenant/fragnostic/fragnostic-textools/target/portugues-iso.txt"

  describe("Text 2 ISO Test") {

    it("Can Convert Text 2 ISO") {

      val answer = CakeService.text2IsoService.text2Iso(pathSrc, pathTgt) fold (
        error => error,
        success => success)

      answer should be("text.2.iso.success")

    }

  }

}