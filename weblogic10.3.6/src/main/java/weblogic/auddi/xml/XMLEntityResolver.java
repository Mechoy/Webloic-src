package weblogic.auddi.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.PropertyManager;

class XMLEntityResolver implements EntityResolver {
   private static byte[] s_soapschema;

   public XMLEntityResolver() {
      Logger.trace("+XMLEntityResolver.CTOR()");
      InputStream var1 = null;
      if (s_soapschema == null) {
         Logger.trace("XMLEntityResolver.init");
         String var2 = PropertyManager.getRuntimeProperty("soap.schema.resource");
         if (var2 != null) {
            var1 = this.getClass().getResourceAsStream(var2);
         }

         try {
            s_soapschema = new byte[var1.available()];
            int var3 = 0;

            while(true) {
               int var4 = var1.read();
               if (var4 == -1) {
                  break;
               }

               s_soapschema[var3++] = (byte)var4;
            }
         } catch (IOException var5) {
            Logger.debug((Throwable)var5);
         }
      }

      Logger.trace("-XMLEntityResolver.CTOR()");
   }

   public InputSource resolveEntity(String var1, String var2) {
      Logger.trace("+XMLEntityResolver.resolveEntity() - systemId: " + var2);
      if (var2 == null) {
         Logger.trace("-XMLEntityResolver.resolveEntity() - systemId was NULL");
         return null;
      } else {
         BufferedInputStream var3 = null;
         String var4 = PropertyManager.getRuntimeProperty("soap.schema.resource");
         if (var4 != null) {
            var3 = new BufferedInputStream(new ByteArrayInputStream(s_soapschema));
         }

         Logger.trace("-XMLEntityResolver.resolveEntity() returning : " + (var3 == null ? "Null" : "Not Null"));
         return var3 == null ? null : new InputSource(var3);
      }
   }
}
