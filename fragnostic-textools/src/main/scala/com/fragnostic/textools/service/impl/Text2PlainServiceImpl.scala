package com.fragnostic.textools.service.impl

import com.fragnostic.support.FilesSupport
import com.fragnostic.textools.service.api.Text2PlainServiceApi
import com.fragnostic.textools.service.support.TextoolSupport

trait Text2PlainServiceImpl extends Text2PlainServiceApi {

  def text2PlainService = new DefaultText2PlainService

  class DefaultText2PlainService extends Text2PlainServiceApi with FilesSupport with TextoolSupport {

    private val text2plainMap: Map[String, String] = Map(
      "\\(" -> "\\\\u0028", // parentesis izquierdo
      "\\)" -> "\\\\u0029", // parentesis derecho
      "\\u0022" -> "\"", // quotation mark
      "\u00C1" -> "A", // a may con acento agudo
      "\u00C9" -> "E", // e may con acento tilde
      "\u00E0" -> "a", // a min con acento crase
      "\u00E1" -> "a", // a min con acento agudo
      "\u00E2" -> "a", // a min con acento circunflejo
      "\u00E3" -> "a", // a min con tilde
      "\u00E7" -> "c", // cedilha
      "\u00E9" -> "e", // e min con acento agudo
      "\u00E9" -> "e", // e min con tilde
      "\u00EA" -> "e", // e min con acento circunflejo
      "\u00ED" -> "i", // i min con acento agudo
      "\u00F3" -> "o", // o min con acento agudo
      "\u00F5" -> "o", // o min con acento tilde
      "\u00FA" -> "o" // u min con acento tilde
    )

    override def text2Plain(pathSrc: String, pathTgt: String, encoding: String): Either[String, String] =
      fileToList(pathSrc, encoding) fold (
        error => Left("text.2.plain.error"),
        list =>
          writeLinesToFile(list map (
            line => convert(line, text2plainMap.keysIterator, text2plainMap)), pathTgt) fold (
            error => Left("text.2.plain.error"),
            success => Right("text.2.plain.success")))

  }

}
