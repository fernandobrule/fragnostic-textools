package com.fragnostic.textools.service.impl

import com.fragnostic.support.FilesSupport
import com.fragnostic.textools.service.api.Iso2TextServiceApi
import com.fragnostic.textools.service.support.TextoolSupport

import scala.annotation.tailrec
import scala.collection.Iterator

trait Iso2TextServiceImpl extends Iso2TextServiceApi with FilesSupport with TextoolSupport {

  def iso2TextService = new DefaultIso2TextService

  class DefaultIso2TextService extends Iso2TextServiceApi {

    private val iso2textMap: Map[String, String] = Map(
      "\\\\u0022" -> "\"", // quotation mark
      "\\\\u0028" -> "(", // parentesis izquierdo
      "\\\\u0029" -> ")", // parentesis derecho
      "\\\\u00C1" -> "\u00C1", // a may con acento agudo
      "\\\\u00C9" -> "\u00C9", // e may con acento tilde
      "\\\\u00E0" -> "\u00E0", // a min con acento crase
      "\\\\u00E1" -> "\u00E1", // a min con acento agudo
      "\\\\u00E2" -> "\u00E2", // a min con acento circunflejo
      "\\\\u00E3" -> "\u00E3", // a min con acento tilde
      "\\\\u00E3" -> "\u00E3", // a min con tilde
      "\\\\u00E7" -> "\u00E7", // cedilha
      "\\\\u00E9" -> "\u00E9", // e min con acento agudo
      "\\\\u00E9" -> "\u00E9", // e min con tilde
      "\\\\u00EA" -> "\u00EA", // e min con acento circunflejo
      "\\\\u00ED" -> "\u00ED", // i min con acento agudo
      "\\\\u00F3" -> "\u00F3", // o min con acento agudo
      "\\\\u00F5" -> "\u00F5", // o min con acento tilde
      "\\\\u00FA" -> "\u00FA" // u min con acento tilde
    )

    @tailrec
    private def line2(line: String, isoItr: Iterator[String], map: Map[String, String]): String =
      if (isoItr.hasNext) {
        val code = isoItr.next()
        if (line.startsWith(csharp)) {
          line2(line, isoItr, map)
        } else {
          line2(line.replaceAll(code, map(code)), isoItr, map)
        }
      } else {
        line
      }

    private def line2text(line: String, isoItr: Iterator[String], map: Map[String, String]): String =
      line2(line, isoItr, map)

    override def iso2Text(pathSrc: String, pathTgt: String): Either[String, String] =
      fileToList(pathSrc, charsetName) fold (
        error => Left("iso.2.text.error"),
        list => {
          writeLinesToFile(list map (
            (line: String) => line2text(line, iso2textMap.keysIterator, iso2textMap)), pathTgt) fold (
            error => Left("iso.2.text.error"),
            success => Right("iso.2.text.success"))
        })

  }

}
