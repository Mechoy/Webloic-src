package weblogic.auddi.uddi.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.PropertyManager;

class UDDIEntityResolver implements EntityResolver {
   private static final String UDDI_SCHEMA_REF = "/uddi_v2.xsd";
   private static final String UDDI_SCHEMA_SOURCE = "uddi.schema.resource";
   private static final String XML_SCHEMA_REF = "http://www.w3.org/2001/xml.xsd";
   private static final String XML_SCHEMA_SOURCE = "xml.schema.resource";
   private static byte[] s_uddischema;
   private static byte[] s_xmlschema;

   public UDDIEntityResolver() {
      Logger.trace("+UDDIEntityResolver.CTOR()");
      InputStream var1 = null;
      if (s_uddischema == null) {
         String var2 = PropertyManager.getRuntimeProperty("uddi.schema.resource");
         if (var2 == null) {
            throw new RuntimeException(UDDIMessages.get("error.resource.notfound", "uddi.schema.resource"));
         }

         var1 = this.getClass().getResourceAsStream(var2);

         int var3;
         int var4;
         try {
            s_uddischema = new byte[var1.available()];
            var3 = 0;

            while(true) {
               var4 = var1.read();
               if (var4 == -1) {
                  break;
               }

               s_uddischema[var3++] = (byte)var4;
            }
         } catch (IOException var6) {
            Logger.debug((Throwable)var6);
         }

         var2 = PropertyManager.getRuntimeProperty("xml.schema.resource");
         if (var2 == null) {
            throw new RuntimeException(UDDIMessages.get("error.resource.notfound", "xml.schema.resource"));
         }

         var1 = this.getClass().getResourceAsStream(var2);

         try {
            s_xmlschema = new byte[var1.available()];
            var3 = 0;

            while(true) {
               var4 = var1.read();
               if (var4 == -1) {
                  break;
               }

               s_xmlschema[var3++] = (byte)var4;
            }
         } catch (IOException var5) {
            Logger.debug((Throwable)var5);
         }
      }

      Logger.trace("-UDDIEntityResolver.CTOR()");
   }

   public InputSource resolveEntity(String var1, String var2) {
      Logger.trace("+UDDIEntityResolver.resolveEntity() - systemId: " + var2);
      if (var2 == null) {
         Logger.trace("-UDDIEntityResolver.resolveEntity() - systemId was NULL");
         return null;
      } else {
         if (var2.startsWith("file://")) {
            int var3 = var2.lastIndexOf(47);
            var2 = var2.substring(var3);
         }

         Object var5 = null;
         BufferedInputStream var4 = null;
         if (var2.equals("/uddi_v2.xsd")) {
            var4 = new BufferedInputStream(new ByteArrayInputStream(s_uddischema));
         } else if (var2.equals("http://www.w3.org/2001/xml.xsd")) {
            var4 = new BufferedInputStream(new ByteArrayInputStream(s_xmlschema));
         }

         Logger.trace("-UDDIEntityResolver.resolveEntity() returning :" + (var4 == null ? "Null" : "Not Null"));
         return var4 == null ? null : new InputSource(var4);
      }
   }
}
