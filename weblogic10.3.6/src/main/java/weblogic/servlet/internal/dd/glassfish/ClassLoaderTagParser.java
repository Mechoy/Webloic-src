package weblogic.servlet.internal.dd.glassfish;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.j2ee.descriptor.wl.ContainerDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;

public class ClassLoaderTagParser extends BaseGlassfishTagParser {
   void parse(XMLStreamReader var1, WeblogicWebAppBean var2) throws XMLStreamException {
      ContainerDescriptorBean var3 = var2.createContainerDescriptor();
      String var4 = var1.getAttributeValue((String)null, "delegate");
      var3.setPreferWebInfClasses(this.convertToBoolean(var4));
      HTTPLogger.logGlassfishDescriptorParsed("delegate");

      int var5;
      do {
         var5 = var1.next();
      } while(var1.hasNext() && !this.isEndTag(var5, var1, "class-loader"));

   }
}
