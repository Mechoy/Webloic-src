package weblogic.html;

/** @deprecated */
public final class EntityEscape {
   public static String escapeString(String var0) {
      if (var0 == null) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         int var2 = var0.length();
         char[] var3 = new char[var2];
         var0.getChars(0, var2, var3, 0);
         escapeChars(var3, var1);
         return var1.toString();
      }
   }

   public static void escapeString(String var0, StringBuffer var1) {
      if (var0 != null) {
         int var2 = var0.length();
         char[] var3 = new char[var2];
         var0.getChars(0, var2, var3, 0);
         escapeChars(var3, var1);
      }
   }

   public static void escapeChars(char[] var0, StringBuffer var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         String var3;
         switch (var0[var2]) {
            case '"':
               var3 = "&quot;";
               break;
            case '&':
               var3 = "&amp;";
               break;
            case '<':
               var3 = "&lt;";
               break;
            case '>':
               var3 = "&gt;";
               break;
            default:
               switch (var0[var2]) {
                  case '\'':
                     var3 = "&#39;";
                     break;
                  case '(':
                  case ')':
                  case '*':
                  case '+':
                  case ',':
                  case '-':
                  case '.':
                  case '/':
                  case '0':
                  case '1':
                  case '2':
                  case '3':
                  case '4':
                  case '5':
                  case '6':
                  case '7':
                  case '8':
                  case '9':
                  case ':':
                  case ';':
                  case '<':
                  case '=':
                  case '>':
                  case '?':
                  case '@':
                  case 'A':
                  case 'B':
                  case 'C':
                  case 'D':
                  case 'E':
                  case 'F':
                  case 'G':
                  case 'H':
                  case 'I':
                  case 'J':
                  case 'K':
                  case 'L':
                  case 'M':
                  case 'N':
                  case 'O':
                  case 'P':
                  case 'Q':
                  case 'R':
                  case 'S':
                  case 'T':
                  case 'U':
                  case 'V':
                  case 'W':
                  case 'X':
                  case 'Y':
                  case 'Z':
                  case '[':
                  case '\\':
                  case ']':
                  case '^':
                  case '_':
                  case '`':
                  case 'a':
                  case 'b':
                  case 'c':
                  case 'd':
                  case 'e':
                  case 'f':
                  case 'g':
                  case 'h':
                  case 'i':
                  case 'j':
                  case 'k':
                  case 'l':
                  case 'm':
                  case 'n':
                  case 'o':
                  case 'p':
                  case 'q':
                  case 'r':
                  case 's':
                  case 't':
                  case 'u':
                  case 'v':
                  case 'w':
                  case 'x':
                  case 'y':
                  case 'z':
                  case '{':
                  case '|':
                  case '}':
                  case '~':
                  case '\u007f':
                  case '\u0080':
                  case '\u0081':
                  case '\u0082':
                  case '\u0083':
                  case '\u0084':
                  case '\u0085':
                  case '\u0086':
                  case '\u0087':
                  case '\u0088':
                  case '\u0089':
                  case '\u008a':
                  case '\u008b':
                  case '\u008c':
                  case '\u008d':
                  case '\u008e':
                  case '\u008f':
                  case '\u0090':
                  case '\u0091':
                  case '\u0092':
                  case '\u0093':
                  case '\u0094':
                  case '\u0095':
                  case '\u0096':
                  case '\u0097':
                  case '\u0098':
                  case '\u0099':
                  case '\u009a':
                  case '\u009b':
                  case '\u009c':
                  case '\u009d':
                  case '\u009e':
                  case '\u009f':
                  default:
                     var3 = String.valueOf(var0[var2]);
                     break;
                  case ' ':
                     var3 = "&nbsp;";
                     break;
                  case '¡':
                     var3 = "&iexcl;";
                     break;
                  case '¢':
                     var3 = "&cent;";
                     break;
                  case '£':
                     var3 = "&pound;";
                     break;
                  case '¤':
                     var3 = "&curren;";
                     break;
                  case '¥':
                     var3 = "&yen;";
                     break;
                  case '¦':
                     var3 = "&brvbar;";
                     break;
                  case '§':
                     var3 = "&sect;";
                     break;
                  case '¨':
                     var3 = "&uml;";
                     break;
                  case '©':
                     var3 = "&copy;";
                     break;
                  case 'ª':
                     var3 = "&ordf;";
                     break;
                  case '«':
                     var3 = "&laquo;";
                     break;
                  case '¬':
                     var3 = "&not;";
                     break;
                  case '\u00ad':
                     var3 = "&shy;";
                     break;
                  case '®':
                     var3 = "&reg;";
                     break;
                  case '¯':
                     var3 = "&macr;";
                     break;
                  case '°':
                     var3 = "&deg;";
                     break;
                  case '±':
                     var3 = "&plusmn;";
                     break;
                  case '²':
                     var3 = "&sup2;";
                     break;
                  case '³':
                     var3 = "&sup3;";
                     break;
                  case '´':
                     var3 = "&acute;";
                     break;
                  case 'µ':
                     var3 = "&micro;";
                     break;
                  case '¶':
                     var3 = "&para;";
                     break;
                  case '·':
                     var3 = "&middot;";
                     break;
                  case '¸':
                     var3 = "&cedil;";
                     break;
                  case '¹':
                     var3 = "&sup1;";
                     break;
                  case 'º':
                     var3 = "&ordm;";
                     break;
                  case '»':
                     var3 = "&raquo;";
                     break;
                  case '¼':
                     var3 = "&frac14;";
                     break;
                  case '½':
                     var3 = "&frac12;";
                     break;
                  case '¾':
                     var3 = "&frac34;";
                     break;
                  case '¿':
                     var3 = "&iquest;";
                     break;
                  case 'À':
                     var3 = "&Agrave;";
                     break;
                  case 'Á':
                     var3 = "&Aacute;";
                     break;
                  case 'Â':
                     var3 = "&Acirc;";
                     break;
                  case 'Ã':
                     var3 = "&Atilde;";
                     break;
                  case 'Ä':
                     var3 = "&Auml;";
                     break;
                  case 'Å':
                     var3 = "&Aring;";
                     break;
                  case 'Æ':
                     var3 = "&AElig;";
                     break;
                  case 'Ç':
                     var3 = "&Ccedil;";
                     break;
                  case 'È':
                     var3 = "&Egrave;";
                     break;
                  case 'É':
                     var3 = "&Eacute;";
                     break;
                  case 'Ê':
                     var3 = "&Ecirc;";
                     break;
                  case 'Ë':
                     var3 = "&Euml;";
                     break;
                  case 'Ì':
                     var3 = "&Igrave;";
                     break;
                  case 'Í':
                     var3 = "&Iacute;";
                     break;
                  case 'Î':
                     var3 = "&Icirc;";
                     break;
                  case 'Ï':
                     var3 = "&Iuml;";
                     break;
                  case 'Ð':
                     var3 = "&ETH;";
                     break;
                  case 'Ñ':
                     var3 = "&Ntilde;";
                     break;
                  case 'Ò':
                     var3 = "&Ograve;";
                     break;
                  case 'Ó':
                     var3 = "&Oacute;";
                     break;
                  case 'Ô':
                     var3 = "&Ocirc;";
                     break;
                  case 'Õ':
                     var3 = "&Otilde;";
                     break;
                  case 'Ö':
                     var3 = "&Ouml;";
                     break;
                  case '×':
                     var3 = "&times;";
                     break;
                  case 'Ø':
                     var3 = "&Oslash;";
                     break;
                  case 'Ù':
                     var3 = "&Ugrave;";
                     break;
                  case 'Ú':
                     var3 = "&Uacute;";
                     break;
                  case 'Û':
                     var3 = "&Ucirc;";
                     break;
                  case 'Ü':
                     var3 = "&Uuml;";
                     break;
                  case 'Ý':
                     var3 = "&Yacute;";
                     break;
                  case 'Þ':
                     var3 = "&THORN;";
                     break;
                  case 'ß':
                     var3 = "&szlig;";
                     break;
                  case 'à':
                     var3 = "&agrave;";
                     break;
                  case 'á':
                     var3 = "&aacute;";
                     break;
                  case 'â':
                     var3 = "&acirc;";
                     break;
                  case 'ã':
                     var3 = "&atilde;";
                     break;
                  case 'ä':
                     var3 = "&auml;";
                     break;
                  case 'å':
                     var3 = "&aring;";
                     break;
                  case 'æ':
                     var3 = "&aelig;";
                     break;
                  case 'ç':
                     var3 = "&ccedil;";
                     break;
                  case 'è':
                     var3 = "&egrave;";
                     break;
                  case 'é':
                     var3 = "&eacute;";
                     break;
                  case 'ê':
                     var3 = "&ecirc;";
                     break;
                  case 'ë':
                     var3 = "&euml;";
                     break;
                  case 'ì':
                     var3 = "&igrave;";
                     break;
                  case 'í':
                     var3 = "&iacute;";
                     break;
                  case 'î':
                     var3 = "&icirc;";
                     break;
                  case 'ï':
                     var3 = "&iuml;";
                     break;
                  case 'ð':
                     var3 = "&eth;";
                     break;
                  case 'ñ':
                     var3 = "&ntilde;";
                     break;
                  case 'ò':
                     var3 = "&ograve;";
                     break;
                  case 'ó':
                     var3 = "&oacute;";
                     break;
                  case 'ô':
                     var3 = "&ocirc;";
                     break;
                  case 'õ':
                     var3 = "&otilde;";
                     break;
                  case 'ö':
                     var3 = "&ouml;";
                     break;
                  case '÷':
                     var3 = "&divide;";
                     break;
                  case 'ø':
                     var3 = "&oslash;";
                     break;
                  case 'ù':
                     var3 = "&ugrave;";
                     break;
                  case 'ú':
                     var3 = "&uacute;";
                     break;
                  case 'û':
                     var3 = "&ucirc;";
                     break;
                  case 'ü':
                     var3 = "&uuml;";
                     break;
                  case 'ý':
                     var3 = "&yacute;";
                     break;
                  case 'þ':
                     var3 = "&thorn;";
                     break;
                  case 'ÿ':
                     var3 = "&yuml;";
               }
         }

         var1.append(var3);
      }

   }
}
