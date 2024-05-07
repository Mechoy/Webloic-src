package weblogic.servlet.internal.dd.glassfish;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;

public class ContextRootTagParser extends BaseGlassfishTagParser {
   void parse(XMLStreamReader var1, WeblogicWebAppBean var2) throws XMLStreamException {
      String var3 = this.parseTagData(var1);
      var2.setContextRoots(new String[]{var3});
      HTTPLogger.logGlassfishDescriptorParsed("context-root");
   }
}
