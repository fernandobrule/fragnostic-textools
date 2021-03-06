package com.fragnostic.textools.service.support

trait TextoolSupport {

  val charsetName = "UTF-8"
  val csharp = "#"
  val cp1252 = "cp1252"
  val NEW_LINE = "\n"

  protected def convert(line: String, isoItr: Iterator[String], map: Map[String, String]): String =
    if (isoItr.hasNext) {
      val code = isoItr.next()
      if (line.startsWith(csharp)) {
        convert(line, isoItr, map)
      } else {
        convert(line.replaceAll(code, map(code)), isoItr, map)
      }
    } else {
      line
    }

}
