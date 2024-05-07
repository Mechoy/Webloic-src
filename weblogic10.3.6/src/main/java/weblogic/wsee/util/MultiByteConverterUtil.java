package weblogic.wsee.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringTokenizer;
import weblogic.utils.http.HttpRequestParser;

public abstract class MultiByteConverterUtil {
   private static final String DO_NOT_ENCODE_THESE = "/:!&?=";
   private static final String DEFAULT_ENCODING = HttpRequestParser.getURIDecodeEncoding();
   private static final String LEGAL_URL_CHAR_PATTERNS = "[a-zA-Z0-9/$-_.+!*'(),%?;:@=&]+";

   public static String encodeMultiByteURL(String var0, String var1) {
      String var2 = var1 == null ? DEFAULT_ENCODING : var1;

      try {
         StringBuilder var3 = new StringBuilder();
         StringTokenizer var4 = new StringTokenizer(var0, "/:!&?=", true);

         while(true) {
            while(var4.hasMoreTokens()) {
               String var5 = var4.nextToken();
               if (var5.length() == 1 && "/:!&?=".indexOf(var5) >= 0) {
                  var3.append(var5);
               } else {
                  var3.append(URLEncoder.encode(var5, var2));
               }
            }

            return var3.toString();
         }
      } catch (UnsupportedEncodingException var6) {
         throw new WLJAXRPCException("Unable to encode WSDL URL", var6);
      }
   }

   public static String encodeMultiByteURL(String var0) {
      return encodeMultiByteURL(var0, (String)null);
   }

   public static boolean isEncodedUrl(String var0) {
      return var0 != null && !"".equals(var0) ? var0.matches("[a-zA-Z0-9/$-_.+!*'(),%?;:@=&]+") : false;
   }

   public static boolean hasNonAsciiOrSpace(String var0) {
      if (var0 == null) {
         return false;
      } else {
         for(int var1 = 0; var1 < var0.length(); ++var1) {
            char var2 = var0.charAt(var1);
            if (var2 > 127 || var2 == ' ') {
               return true;
            }
         }

         return false;
      }
   }
}
