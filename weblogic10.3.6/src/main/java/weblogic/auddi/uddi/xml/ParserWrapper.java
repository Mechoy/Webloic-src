package weblogic.auddi.uddi.xml;

import java.io.IOException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import weblogic.auddi.util.Logger;
import weblogic.auddi.xml.ParserFactory;
import weblogic.auddi.xml.SchemaException;

public class ParserWrapper {
   public static Document parseRequest(String var0) throws SchemaException {
      return parseRequest(var0, true);
   }

   public static Document parseRequest(String var0, boolean var1) throws SchemaException {
      Logger.trace("+ParserWrapper.parseRequest()");
      Document var2;
      if (var1) {
         UDDIParserFactory var3 = null;
         UDDIParser var4 = null;

         try {
            var3 = new UDDIParserFactory();
            var4 = var3.getUDDIParser();
            var2 = var4.parseRequest(var0);
         } finally {
            if (var4 != null) {
               var3.returnUDDIParser(var4);
            }

         }
      } else {
         try {
            var2 = (new ParserFactory()).createDOMParser().parse(var0);
         } catch (SAXException var9) {
            throw new SchemaException(var9);
         } catch (IOException var10) {
            throw new SchemaException(var10);
         }
      }

      Logger.trace("-ParserWrapper.parseRequest()");
      return var2;
   }
}
