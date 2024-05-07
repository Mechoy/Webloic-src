package weblogic.servlet.internal.dd.glassfish;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.j2ee.descriptor.wl.ResourceEnvDescriptionBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;

public class ResourceEnvRefTagParser extends BaseGlassfishTagParser {
   void parse(XMLStreamReader var1, WeblogicWebAppBean var2) throws XMLStreamException {
      ResourceEnvDescriptionBean var3 = var2.createResourceEnvDescription();

      int var4;
      do {
         var4 = var1.next();
         if (var4 == 1) {
            String var5 = var1.getLocalName();
            if ("resource-env-ref-name".equals(var5)) {
               var3.setResourceEnvRefName(this.parseTagData(var1));
               HTTPLogger.logGlassfishDescriptorParsed("resource-env-ref-name");
            } else if ("jndi-name".equals(var5)) {
               var3.setJNDIName(this.parseTagData(var1));
               HTTPLogger.logGlassfishDescriptorParsed("jndi-name");
            }
         }
      } while(var1.hasNext() && !this.isEndTag(var4, var1, "resource-env-ref"));

   }
}
