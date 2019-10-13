package com.fragnostic.textools.service.agnostic

import com.fragnostic.textools.service.impl.{ Iso2TextServiceImpl, NormalizeServiceImpl, Text2IsoServiceImpl, Text2PlainServiceImpl }

object CakeService {

  lazy val normalizeServicePiece = new NormalizeServiceImpl {}

  lazy val iso2TextServicePiece = new Iso2TextServiceImpl {}

  lazy val text2IsoServicePiece = new Text2IsoServiceImpl {}

  lazy val text2PlainServicePiece = new Text2PlainServiceImpl {}

  val text2PlainService = text2PlainServicePiece.text2PlainService

  val text2IsoService = text2IsoServicePiece.text2IsoService

  val iso2TextService = iso2TextServicePiece.iso2TextService

  val normalizeService = normalizeServicePiece.normalizeService

}
