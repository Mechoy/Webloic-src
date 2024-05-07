package weblogic.servlet.internal.dd.glassfish;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.j2ee.descriptor.wl.ResourceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;

public class ResourceRefTagParser extends BaseGlassfishTagParser {
   void parse(XMLStreamReader var1, WeblogicWebAppBean var2) throws XMLStreamException {
      ResourceDescriptionBean var3 = var2.createResourceDescription();

      int var4;
      do {
         var4 = var1.next();
         if (var4 == 1) {
            String var5 = var1.getLocalName();
            if ("res-ref-name".equals(var5)) {
               var3.setResRefName(this.parseTagData(var1));
               HTTPLogger.logGlassfishDescriptorParsed("res-ref-name");
            } else if ("jndi-name".equals(var5)) {
               var3.setJNDIName(this.parseTagData(var1));
               HTTPLogger.logGlassfishDescriptorParsed("jndi-name");
            }
         }
      } while(var1.hasNext() && !this.isEndTag(var4, var1, "resource-ref"));

   }
}
