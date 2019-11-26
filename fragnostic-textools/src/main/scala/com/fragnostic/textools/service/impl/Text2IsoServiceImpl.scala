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

    def convert(itr: Iterator[String], out: PrintWriter): Unit = {
      if (itr.hasNext) {
        out.println(convert(itr.next(), text2isoMap.keysIterator, text2isoMap))
        convert(itr, out)
      }
      ()
    }

    private def close(cls: Closeable): Unit = {
      try {
        cls.close()
      } catch {
        case e: IOException => logger.error(s"close() - $e")
        case e: Throwable => logger.error(s"close() - $e")
      }
    }

    override def text2Iso(pathSrc: String, pathTgt: String): Either[String, String] = {
      val bs: BufferedSource = Source.fromFile(pathSrc, cp1252)
      val out: PrintWriter = new PrintWriter(new BufferedWriter(new FileWriter(pathTgt)));
      try {
        bs.getLines().foreach(
          line => {
            val c = convert(line, text2isoMap.keysIterator, text2isoMap)
            println(s"$line =>\n\t$c")
            out.write(c)
            out.write(NEW_LINE)
          })
        Right("text.2.iso.success")
      } catch {
        case e: Throwable => {
          logger.error(s"close() - $e")
          Left("text.2.iso.fail")
        }
      } finally {
        close(bs)
        close(out)
      }
    }

  }

}
