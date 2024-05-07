package weblogic.servlet.internal.dd.glassfish;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.j2ee.descriptor.wl.EjbReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;

public class EjbRefTagParser extends BaseGlassfishTagParser {
   void parse(XMLStreamReader var1, WeblogicWebAppBean var2) throws XMLStreamException {
      EjbReferenceDescriptionBean var3 = var2.createEjbReferenceDescription();

      int var4;
      do {
         var4 = var1.next();
         if (var4 == 1) {
            String var5 = var1.getLocalName();
            if ("ejb-ref-name".equals(var5)) {
               var3.setEjbRefName(this.parseTagData(var1));
               HTTPLogger.logGlassfishDescriptorParsed("ejb-ref-name");
            } else if ("jndi-name".equals(var5)) {
               var3.setJNDIName(this.parseTagData(var1));
               HTTPLogger.logGlassfishDescriptorParsed("jndi-name");
            }
         }
      } while(var1.hasNext() && !this.isEndTag(var4, var1, "ejb-ref"));

   }
}
