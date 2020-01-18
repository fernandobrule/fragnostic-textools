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

    //
    // https://en.wikipedia.org/wiki/ISO/IEC_8859-5
    //
    private val text2iso8859g5Map: Map[String, String] = Map(
      "\\u0401" -> "\\\\u0401", // Ё   -> - Yo (Cyrillic)
      "\\u0402" -> "\\\\u0402", // Ђ   -> - Dje
      "\\u0403" -> "\\\\u0403", // Ѓ   -> - Gje
      "\\u0404" -> "\\\\u0404", // Є   -> - Ukrainian Ye
      "\\u0405" -> "\\\\u0405", // Ѕ   -> - Dze
      "\\u0406" -> "\\\\u0406", // І   -> - Ukrainian I
      "\\u0407" -> "\\\\u0407", // Ї   -> - Yi (Cyrillic)
      "\\u0408" -> "\\\\u0408", // Ј   -> - Je (Cyrillic)
      "\\u0409" -> "\\\\u0409", // Љ   -> - Lje
      "\\u040A" -> "\\\\u040A", // Њ   -> - Nje
      "\\u040B" -> "\\\\u040B", // Ћ   -> - Tshe
      "\\u040C" -> "\\\\u040C", // Ќ   -> - Kje
      "\\u040E" -> "\\\\u040E", // Ў   -> - Short U (Cyrillic)
      "\\u040F" -> "\\\\u040F", // Џ   -> - Dzhe
      "\\u0410" -> "\\\\u0410", // А   -> - A (Cyrillic)
      "\\u0411" -> "\\\\u0411", // Б   -> - Be (Cyrillic)
      "\\u0412" -> "\\\\u0412", // В   -> - Ve (Cyrillic)
      "\\u0413" -> "\\\\u0413", // Г   -> - Ge (Cyrillic)
      "\\u0414" -> "\\\\u0414", // Д   -> - De (Cyrillic)
      "\\u0415" -> "\\\\u0415", // Е   -> - Ye (Cyrillic)
      "\\u0416" -> "\\\\u0416", // Ж   -> - Zhe (Cyrillic)
      "\\u0417" -> "\\\\u0417", // З   -> - Ze (Cyrillic)
      "\\u0418" -> "\\\\u0418", // И   -> - I (Cyrillic)
      "\\u0419" -> "\\\\u0419", // Й   -> - Short I
      "\\u041A" -> "\\\\u041A", // К   -> - Ka (Cyrillic)
      "\\u041B" -> "\\\\u041B", // Л   -> - El (Cyrillic)
      "\\u041C" -> "\\\\u041C", // М   -> - Em (Cyrillic)
      "\\u041D" -> "\\\\u041D", // Н   -> - En (Cyrillic)
      "\\u041E" -> "\\\\u041E", // О   -> - O (Cyrillic)
      "\\u041F" -> "\\\\u041F", // П   -> - Pe (Cyrillic)
      "\\u0420" -> "\\\\u0420", // Р   -> - Er (Cyrillic)
      "\\u0421" -> "\\\\u0421", // С   -> - Es (Cyrillic)
      "\\u0422" -> "\\\\u0422", // Т   -> - Te (Cyrillic)
      "\\u0423" -> "\\\\u0423", // У   -> - U (Cyrillic)
      "\\u0424" -> "\\\\u0424", // Ф   -> - Ef (Cyrillic)
      "\\u0425" -> "\\\\u0425", // Х   -> - Ha (Cyrillic)
      "\\u0426" -> "\\\\u0426", // Ц   -> - Tse (Cyrillic)
      "\\u0427" -> "\\\\u0427", // Ч   -> - Che (Cyrillic)
      "\\u0428" -> "\\\\u0428", // Ш   -> - Sha (Cyrillic)
      "\\u0429" -> "\\\\u0429", // Щ   -> - Shcha
      "\\u042A" -> "\\\\u042A", // Ъ   -> - Hard sign
      "\\u042B" -> "\\\\u042B", // Ы   -> - Yery
      "\\u042C" -> "\\\\u042C", // Ь   -> - Soft sign
      "\\u042D" -> "\\\\u042D", // Э   -> - E (Cyrillic)
      "\\u042E" -> "\\\\u042E", // Ю   -> - Yu (Cyrillic)
      "\\u042F" -> "\\\\u042F", // Я   -> - Ya (Cyrillic)
      "\\u0430" -> "\\\\u0430", // а   -> - A (Cyrillic)
      "\\u0431" -> "\\\\u0431", // б   -> - Be (Cyrillic)
      "\\u0432" -> "\\\\u0432", // в   -> - Ve (Cyrillic)
      "\\u0433" -> "\\\\u0433", // г   -> - Ge (Cyrillic)
      "\\u0434" -> "\\\\u0434", // д   -> - De (Cyrillic)
      "\\u0435" -> "\\\\u0435", // е   -> - Ye (Cyrillic)
      "\\u0436" -> "\\\\u0436", // ж   -> - Zhe (Cyrillic)
      "\\u0437" -> "\\\\u0437", // з   -> - Ze (Cyrillic)
      "\\u0438" -> "\\\\u0438", // и   -> - I (Cyrillic)
      "\\u0439" -> "\\\\u0439", // й   -> - Short I
      "\\u043A" -> "\\\\u043A", // к   -> - Ka (Cyrillic)
      "\\u043B" -> "\\\\u043B", // л   -> - El (Cyrillic)
      "\\u043C" -> "\\\\u043C", // м   -> - Em (Cyrillic)
      "\\u043D" -> "\\\\u043D", // н   -> - En (Cyrillic)
      "\\u043E" -> "\\\\u043E", // о   -> - O (Cyrillic)
      "\\u043F" -> "\\\\u043F", // п   -> - Pe (Cyrillic)
      "\\u0440" -> "\\\\u0440", // р   -> - Er (Cyrillic)
      "\\u0441" -> "\\\\u0441", // с   -> - Es (Cyrillic)
      "\\u0442" -> "\\\\u0442", // т   -> - Te (Cyrillic)
      "\\u0443" -> "\\\\u0443", // у   -> - U (Cyrillic)
      "\\u0444" -> "\\\\u0444", // ф   -> - Ef (Cyrillic)
      "\\u0445" -> "\\\\u0445", // х   -> - Ha (Cyrillic)
      "\\u0446" -> "\\\\u0446", // ц   -> - Tse (Cyrillic)
      "\\u0447" -> "\\\\u0447", // ч   -> - Che (Cyrillic)
      "\\u0448" -> "\\\\u0448", // ш   -> - Sha (Cyrillic)
      "\\u0449" -> "\\\\u0449", // щ   -> - Shcha
      "\\u044A" -> "\\\\u044A", // ъ   -> - Hard sign
      "\\u044B" -> "\\\\u044B", // ы   -> - Yery
      "\\u044C" -> "\\\\u044C", // ь   -> - Soft sign
      "\\u044D" -> "\\\\u044D", // э   -> - E (Cyrillic)
      "\\u044E" -> "\\\\u044E", // ю   -> - Yu (Cyrillic)
      "\\u044F" -> "\\\\u044F", // я   -> - Ya (letter)
      "\\u2116" -> "\\\\u2116", // №   -> - Numero sign
      "\\u0451" -> "\\\\u0451", // ё   -> - Yo (Cyrillic)
      "\\u0452" -> "\\\\u0452", // ђ   -> - Dje
      "\\u0453" -> "\\\\u0453", // ѓ   -> - Gje
      "\\u0454" -> "\\\\u0454", // є   -> - Ukrainian Ye
      "\\u0455" -> "\\\\u0455", // ѕ   -> - Dze
      "\\u0456" -> "\\\\u0456", // і   -> - Ukrainian I
      "\\u0457" -> "\\\\u0457", // ї   -> - Yi (Cyrillic)
      "\\u0458" -> "\\\\u0458", // ј   -> - Je (Cyrillic)
      "\\u0459" -> "\\\\u0459", // љ   -> - Lje
      "\\u045A" -> "\\\\u045A", // њ   -> - Nje
      "\\u045B" -> "\\\\u045B", // ћ   -> - Tshe
      "\\u045C" -> "\\\\u045C", // ќ   -> - Kje
      "\\u00A7" -> "\\\\u00A7", // §   -> - Section sign
      "\\u045E" -> "\\\\u045E", // ў   -> - Short U (Cyrillic)
      "\\u045F" -> "\\\\u045F", // џ   -> - Dzhe
      "\\u0401" -> "\\\\u0401", // Ё   -> - Yo (Cyrillic)
      "\\u04C7" -> "\\\\u04C7", // Ӈ   -> - Ӈ
      "\\u04D2" -> "\\\\u04D2", // Ӓ   -> - Ӓ
      "\\u04EC" -> "\\\\u04EC", // Ӭ   -> - Ӭ
      "\\u048C" -> "\\\\u048C", // Ҍ   -> - Ҍ
      "\\u0406" -> "\\\\u0406", // І   -> - Ukrainian I
      "\\u04E6" -> "\\\\u04E6", // Ӧ   -> - Ӧ
      "\\u048A" -> "\\\\u048A", // Ҋ   -> - Ҋ
      "\\u04C5" -> "\\\\u04C5", // Ӆ   -> - Ӆ
      "\\u04C9" -> "\\\\u04C9", // Ӊ   -> - Ӊ
      "\\u00AB" -> "\\\\u00AB", // «   -> - Guillemet
      "\\u04CD" -> "\\\\u04CD", // Ӎ   -> - Ӎ
      "\\u049E" -> "\\\\u049E", // Ҏ   -> - Ҏ
      "\\u02BC" -> "\\\\u02BC", // ʼ   -> - ʼ
      "\\u2116" -> "\\\\u2116", // №   -> - Numero sign
      "\\u0451" -> "\\\\u0451", // ё   -> - Yo (Cyrillic)
      "\\u04C8" -> "\\\\u04C8", // ӈ   -> - Ӈ
      "\\u04D3" -> "\\\\u04D3", // ӓ   -> - Ӓ
      "\\u04ED" -> "\\\\u04ED", // ӭ   -> - Ӭ
      "\\u048D" -> "\\\\u048D", // ҍ   -> - Ҍ
      "\\u0456" -> "\\\\u0456", // і   -> - Ukrainian I
      "\\u04E7" -> "\\\\u04E7", // ӧ   -> - Ӧ
      "\\u048B" -> "\\\\u048B", // ҋ   -> - Ҋ
      "\\u04C6" -> "\\\\u04C6", // ӆ   -> - Ӆ
      "\\u04CA" -> "\\\\u04CA", // ӊ   -> - Ӊ
      "\\u00BB" -> "\\\\u00BB", // »   -> - Guillemet
      "\\u04CE" -> "\\\\u04CE", // ӎ   -> - Ӎ
      "\\u00A7" -> "\\\\u00A7", // §   -> - Section sign
      "\\u049F" -> "\\\\u049F", // ҏ   -> - Ҏ
      "\\u02EE" -> "\\\\u02EE", // ˮ   -> - ˮ
      "\\u0401" -> "\\\\u0401", // Ё   -> - Yo (Cyrillic)
      "\\u04D0" -> "\\\\u04D0", // Ӑ   -> - Ӑ
      "\\u04D2" -> "\\\\u04D2", // Ӓ   -> - Ӓ
      "\\u04D6" -> "\\\\u04D6", // Ӗ   -> - Ӗ
      "\\u04AA" -> "\\\\u04AA", // Ҫ   -> - Ҫ
      "\\u0406" -> "\\\\u0406", // І   -> - Ukrainian I
      "\\u04E6" -> "\\\\u04E6", // Ӧ   -> - Ӧ
      "\\u04E4" -> "\\\\u04E4", // Ӥ   -> - Ӥ
      "\\u04DC" -> "\\\\u04DC", // Ӝ   -> - Ӝ
      "\\u04A4" -> "\\\\u04A4", // Ҥ   -> - Ҥ
      "\\u04F8" -> "\\\\u04F8", // Ӹ   -> - Ӹ
      "\\u04DE" -> "\\\\u04DE", // Ӟ   -> - Ӟ
      "\\u04F0" -> "\\\\u04F0", // Ӱ   -> - Ӱ
      "\\u04F4" -> "\\\\u04F4", // Ӵ   -> - Ӵ
      "\\u2116" -> "\\\\u2116", // №   -> - Numero sign
      "\\u0451" -> "\\\\u0451", // ё   -> - Yo (Cyrillic)
      "\\u04D1" -> "\\\\u04D1", // ӑ   -> - Ӑ
      "\\u04D3" -> "\\\\u04D3", // ӓ   -> - Ӓ
      "\\u04D7" -> "\\\\u04D7", // ӗ   -> - Ӗ
      "\\u04AB" -> "\\\\u04AB", // ҫ   -> - Ҫ
      "\\u0456" -> "\\\\u0456", // і   -> - Ukrainian I
      "\\u04E7" -> "\\\\u04E7", // ӧ   -> - Ӧ
      "\\u04E5" -> "\\\\u04E5", // ӥ   -> - Ӥ
      "\\u04DD" -> "\\\\u04DD", // ӝ   -> - Ӝ
      "\\u04A5" -> "\\\\u04A5", // ҥ   -> - Ҥ
      "\\u04F9" -> "\\\\u04F9", // ӹ   -> - Ӹ
      "\\u04DF" -> "\\\\u04DF", // ӟ   -> - Ӟ
      "\\u00A7" -> "\\\\u00A7", // §   -> - Section sign
      "\\u04F1" -> "\\\\u04F1", // ӱ   -> - Ӱ
      "\\u04F5" -> "\\\\u04F5" // ӵ   -> - Ӵ
    )

    //
    // https://en.wikipedia.org/wiki/ISO/IEC_8859-1
    //
    private val text2iso8859g1Map: Map[String, String] = Map(
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
        logger.info(s"about to convert:\n\t$line")
        val lineIso = convert(line, text2iso8859g5Map.keysIterator, text2iso8859g5Map)
        out.write(lineIso)
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
        bs.getLines().foreach(line => convert(line, out))
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
