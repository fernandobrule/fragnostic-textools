package com.fragnostic.textools.service.impl

import com.fragnostic.support.FilesSupport
import com.fragnostic.textools.service.api.Text2IsoServiceApi
import com.fragnostic.textools.service.support.TextoolSupport

trait Text2IsoServiceImpl extends Text2IsoServiceApi {

  def text2IsoService = new DefaultText2IsoService

  class DefaultText2IsoService extends Text2IsoServiceApi with FilesSupport with TextoolSupport {

    private val text2isoMap: Map[String, String] = Map(
      "\\(" -> "\\\\u0028", // parentesis izquierdo
      "\\)" -> "\\\\u0029", // parentesis derecho
      "\\u0022" -> "\\\\u0022", // quotation mark
      "\u00C1" -> "\\\\u00C1", // a may con acento agudo
      "\u00C9" -> "\\\\u00C9", // e may con acento tilde
      "\u00E0" -> "\\\\u00E0", // a min con acento crase
      "\u00E1" -> "\\\\u00E1", // a min con acento agudo
      "\u00E2" -> "\\\\u00E2", // a min con acento circunflejo
      "\u00E3" -> "\\\\u00E3", // a min con tilde
      "\u00E7" -> "\\\\u00E7", // cedilha
      "\u00E9" -> "\\\\u00E9", // e min con acento agudo
      "\u00E9" -> "\\\\u00E9", // e min con tilde
      "\u00EA" -> "\\\\u00EA", // e min con acento circunflejo
      "\u00ED" -> "\\\\u00ED", // i min con acento agudo
      "\u00F3" -> "\\\\u00F3", // o min con acento agudo
      "\u00F5" -> "\\\\u00F5", // o min con acento tilde
      "\u00FA" -> "\\\\u00FA" // u min con acento tilde
    )

    override def text2Iso(pathSrc: String, pathTgt: String): Either[String, String] =
      fileToList(pathSrc, charsetName) fold (
        error => Left("text.2.iso.error"),
        list =>
          writeLinesToFile(list map (
            line =>
              convert(line, text2isoMap.keysIterator, text2isoMap)), pathTgt) fold (
            error => Left("text.2.iso.error"),
            success => Right("text.2.iso.success")))

  }

}
