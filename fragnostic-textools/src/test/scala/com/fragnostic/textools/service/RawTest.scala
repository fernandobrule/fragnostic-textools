package com.fragnostic.textools.service

import org.scalatest.{ FunSpec, Matchers }

class RawTest extends FunSpec with Matchers {

  describe("Raw Test") {

    it("Can Find All In With ISO") {

      val line = """body.serv.licen.sanit.emp=Licenciamiento Sanitario de Empresas (inicial, renovação, ampliação, redução de atividades)"""

      val regexp = """\u00E3""".r

      println(regexp.findAllIn(line).toList)

    }

    it("Can do Match With ISO") {

      val line = """body.serv.licen.sanit.emp=Licenciamiento Sanitario de Empresas (inicial, renovação, ampliação, redução de atividades)"""

      val lineIso = line.replaceAll("\u00E3", "\\\\u00E3")

      println(line)
      println(lineIso)

    }

    it("Can Find All In With Numbers") {

      val line = """1 2 3 4"""

      val regexp = """\d+""".r

      println(regexp.findAllIn(line).toList)

    }

    it("Can Replace Escape Characters") {

      val regexp = """\uE003"""

      val newregexp = regexp.replaceAll("""\\""", """\\\\""")

      println(newregexp)

    }

  }

}