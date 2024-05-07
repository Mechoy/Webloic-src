package weblogic.wsee.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AttachmentUtil {
   private static final HashMap CONTENT_TYPES = new HashMap();

   public static String getContentType(String var0) {
      String var1 = (String)CONTENT_TYPES.get(var0);
      if (var1 == null) {
         var1 = "text/plain";
      }

      return var1;
   }

   public static Class getJavaType(String var0) {
      Iterator var1 = CONTENT_TYPES.entrySet().iterator();

      Map.Entry var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = (Map.Entry)var1.next();
      } while(!var0.equals(var2.getValue()));

      try {
         return Class.forName((String)var2.getKey());
      } catch (ClassNotFoundException var4) {
         return null;
      }
   }

   static {
      CONTENT_TYPES.put("java.lang.String", "text/plain");
      CONTENT_TYPES.put("javax.xml.transform.Source", "text/xml");
      CONTENT_TYPES.put("javax.mail.internet.MimeMultipart", "multipart/*");
      CONTENT_TYPES.put("javax.activation.DataHandler", "*/*");
      CONTENT_TYPES.put("java.awt.Image", "image/gif");
   }
}
