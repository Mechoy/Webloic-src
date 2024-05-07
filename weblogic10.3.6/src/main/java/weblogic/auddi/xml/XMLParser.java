package weblogic.auddi.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.MissingResourceException;
import org.w3c.dom.Document;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public abstract class XMLParser {
   public Document parseFile(String var1) throws SchemaException {
      try {
         String var2 = Util.getFileContent(var1);
         return this.parseRequest(var2);
      } catch (IOException var3) {
         throw new SchemaException(UDDIMessages.get("error.schema.parser.info", var3.getMessage()));
      }
   }

   public abstract Document parseRequest(String var1) throws SchemaException;

   public Document parseAsStream(String var1) throws SchemaException {
      try {
         InputStream var2 = XMLParser.class.getResourceAsStream(var1);
         if (var2 == null) {
            throw new MissingResourceException("resource not found.", "", "");
         } else {
            BufferedReader var3 = new BufferedReader(new InputStreamReader(var2));
            StringBuffer var4 = new StringBuffer();

            String var5;
            while((var5 = var3.readLine()) != null) {
               var4.append(var5.trim());
            }

            return this.parseRequest(var4.toString());
         }
      } catch (IOException var6) {
         throw new SchemaException(UDDIMessages.get("error.schema.parser.info", var6.getMessage()));
      }
   }
}
