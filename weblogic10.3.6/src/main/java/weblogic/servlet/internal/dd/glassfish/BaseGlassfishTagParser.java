package weblogic.servlet.internal.dd.glassfish;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.lang.StringUtils;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;

public abstract class BaseGlassfishTagParser {
   abstract void parse(XMLStreamReader var1, WeblogicWebAppBean var2) throws XMLStreamException;

   protected String parseTagData(XMLStreamReader var1) throws XMLStreamException {
      String var2 = null;

      for(int var3 = var1.next(); var3 != 4; var3 = var1.next()) {
      }

      if (var1.getText() != null) {
         var2 = var1.getText().trim();
      }

      return var2;
   }

   protected boolean isEndTag(int var1, XMLStreamReader var2, String var3) {
      return var1 == 2 && StringUtils.isNotEmpty(var3) && var3.equals(var2.getLocalName());
   }

   protected boolean convertToBoolean(String var1) {
      String[] var2 = DescriptorConstants.BOOLEAN_FALSE;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (var5.equals(var1)) {
            return false;
         }
      }

      return true;
   }

   protected Property getProperty(XMLStreamReader var1) {
      return new Property(var1.getAttributeValue((String)null, "name"), var1.getAttributeValue((String)null, "value"));
   }

   class Property {
      String name;
      String value;

      public Property(String var2, String var3) {
         this.name = var2;
         this.value = var3;
      }

      public String getName() {
         return this.name;
      }

      public String getValue() {
         return this.value;
      }
   }
}
