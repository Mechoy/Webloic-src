package weblogic.servlet.internal.dd.glassfish;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;

public class DefaultTagParser extends BaseGlassfishTagParser {
   void parse(XMLStreamReader var1, WeblogicWebAppBean var2) throws XMLStreamException {
      HTTPLogger.logGlassfishDescriptorIgnored(var1.getLocalName());
   }
}
