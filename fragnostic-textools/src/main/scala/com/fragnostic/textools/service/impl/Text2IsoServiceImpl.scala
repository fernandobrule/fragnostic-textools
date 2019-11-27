package com.fragnostic.textools.service.impl

import java.io.{ BufferedWriter, Closeable, FileWriter, IOException, PrintWriter }

import com.fragnostic.support.FilesSupport
import com.fragnostic.textools.service.api.Text2IsoServiceApi
import com.fragnostic.textools.service.support.TextoolSupport
import org.slf4j.{ Logger, LoggerFactory }

import scala.io.{ BufferedSource, Source }

trait Text2IsoServiceImpl extends Text2IsoServiceApi {

  def text2IsoService = new DefaultText2IsoService

  class DefaultText2IsoService extends Text2IsoServiceApi with FilesSupport with TextoolSupport {

    private[this] val logger: Logger = LoggerFactory.getLogger(getClass.getName)

    private val text2isoMap: Map[String, String] = Map(

      //
      // simbolos
      "\\{" -> "\\\\u007b", // left curly bracket
      "\\}" -> "\\\\u007d", // right curly bracket
      "\\u00a1" -> "\\\\u00a1", // exclamation mark izquierda
      "\\u0021" -> "\\\\u0021", // exclamation mark derecha
      "\\u0022" -> "\\\\u0022", // aspas
      "\\u0023" -> "\\\\u0023", // csharp
      "\\u0024" -> "\\\\u0024", // dollar
      "\\u0025" -> "\\\\u0025", // porcentaje
      "\\u0026" -> "\\\\u0026", // ampersand
      "\\u0027" -> "\\\\u0027", // apostrophe
      "\\(" -> "\\\\u0028", // parentesis izquierdo
      "\\)" -> "\\\\u0029", // parentesis derecho
      "\\u002f" -> "\\\\u002f", // forward slash
      "\\u003c" -> "\\\\u003c", // signo menor
      "\\u003e" -> "\\\\u003e", // signo mayor
      "\\u003f" -> "\\\\u003f", // signo interrogacion derecho
      "\\u00bf" -> "\\\\u00bf", // signo interrogacion izquierdo
      "\\u005b" -> "\\\\u005b", // left bracket
      "\\u005d" -> "\\\\u005d", // right bracket
      "\\u007c" -> "\\\\u007c", // pipe
      "\\u007e" -> "\\\\u007e", // tilde
      "\u00b0" -> "\\\\u00b0", // grados
      "\u00ba" -> "\\\\u00ba", // grados subrayado

      //
      // letras especiales
      "\u00e7" -> "\\\\u00e7", // cedilha min
      "\u00f1" -> "\\\\u00f1", // enhe

      //
      // a minuscula
      "\u00e0" -> "\\\\u00e0", // a minuscula con acento crase
      "\u00e1" -> "\\\\u00e1", // a minuscula con acento agudo
      "\u00e2" -> "\\\\u00e2", // a minuscula con acento circunflejo
      "\u00e3" -> "\\\\u00e3", // a minuscula con tilde
      // e minuscula
      "\u00e8" -> "\\\\u00e8", // e minuscula con acento grave
      "\u00e9" -> "\\\\u00e9", // e minuscula con acento agudo
      "\u00ea" -> "\\\\u00ea", // e minuscula con acento circunflejo
      // i minuscula
      "\u00ec" -> "\\\\u00ec", // i minuscula con acento grave
      "\u00ed" -> "\\\\u00ed", // i minuscula con acento agudo
      "\u00ee" -> "\\\\u00ee", // i minuscula con acento circunflejo
      // o minuscula
      "\u00f2" -> "\\\\u00f2", // o minuscula con acento grave
      "\u00f3" -> "\\\\u00f3", // o minuscula con acento agudo
      "\u00f4" -> "\\\\u00f4", // o minuscula con acento circunflejo
      "\u00f5" -> "\\\\u00f5", // o minuscula con acento tilde
      // u minuscula
      "\u00f9" -> "\\\\u00f9", // u minuscula con acento grave
      "\u00fa" -> "\\\\u00fa", // u minuscula con acento agudo
      "\u00fb" -> "\\\\u00fb", // u minuscula con acento circunflejo

      //
      // A mayuscula
      "\u00c0" -> "\\\\u00c0", // A mayuscula con acento grave
      "\u00c1" -> "\\\\u00c1", // A mayuscula con acento agudo
      "\u00c2" -> "\\\\u00c2", // A mayuscula con acento circunfleajo
      "\u00c3" -> "\\\\u00c3", // A mayuscula con acento tilde
      "\u00c7" -> "\\\\u00c7", // cedilha may
      // E mayuscula
      "\u00c8" -> "\\\\u00c8", // E mayuscula con acento grave
      "\u00c9" -> "\\\\u00c9", // E mayuscula con acento agudo
      "\u00ca" -> "\\\\u00ca", // E mayuscula con acento circunflejo
      // I mayuscula
      "\u00cc" -> "\\\\u00cc", // I mayuscula con acento grave
      "\u00cd" -> "\\\\u00cd", // I mayuscula con acento agudo
      "\u00ce" -> "\\\\u00ce", // I mayuscula con acento circunflejo
      // O mayuscula
      "\u00d2" -> "\\\\u00d2", // O mayuscula con acento grave
      "\u00d3" -> "\\\\u00d3", // O mayuscula con acento agudo
      "\u00d4" -> "\\\\u00d4", // O mayuscula con acento circunflejo
      "\u00d5" -> "\\\\u00d5", // O mayuscula con acento tilde
      // U mayuscula
      "\u00d9" -> "\\\\u00d9", // U mayuscula con acento grave
      "\u00da" -> "\\\\u00da", // U mayuscula con acento agudo
      "\u00db" -> "\\\\u00db" // U mayuscula con acento circunflejo

    )

    private def close(cls: Closeable): Unit = {
      try {
        cls.close()
      } catch {
        case e: IOException => logger.error(s"close() - $e")
        case e: Throwable => logger.error(s"close() - $e")
      }
    }

    private def convert(line: String, out: PrintWriter): Unit = {
      try {
        println(s"about to convert:\n\t$line")
        val c = convert(line, text2isoMap.keysIterator, text2isoMap)
        println(s"\t=> $c")
        out.write(c)
        out.write(NEW_LINE)
      } catch {
        case e: Throwable =>
          logger.error(s"convert() - $e")
      }
    }

    override def text2Iso(pathSrc: String, pathTgt: String, encoding: String): Either[String, String] = {
      val bs: BufferedSource = Source.fromFile(pathSrc, encoding)
      val out: PrintWriter = new PrintWriter(new BufferedWriter(new FileWriter(pathTgt)));
      try {
        bs.getLines().foreach(
          line => {
            convert(line, out)
          })
        Right("text.2.iso.success")
      } catch {
        case e: Throwable =>
          logger.error(s"text2Iso() - $e")
          Left("text.2.iso.fail")
      } finally {
        close(bs)
        close(out)
      }
    }

  }

}
