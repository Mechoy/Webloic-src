package weblogic.servlet.internal.dd.glassfish;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.j2ee.descriptor.wl.JspDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;

public class JspConfigTagParser extends BaseGlassfishTagParser {
   void parse(XMLStreamReader var1, WeblogicWebAppBean var2) throws XMLStreamException {
      JspDescriptorBean var3 = var2.createJspDescriptor();

      int var4;
      do {
         var4 = var1.next();
         if (var4 == 1) {
            String var5 = var1.getLocalName();
            if ("property".equals(var5)) {
               BaseGlassfishTagParser.Property var6 = this.getProperty(var1);
               if ("checkInterval".equals(var6.getName())) {
                  var3.setPageCheckSeconds(Integer.parseInt(var6.getValue()));
                  HTTPLogger.logGlassfishDescriptorParsed("checkInterval");
               } else if ("keepgenerated".equals(var6.getName())) {
                  var3.setKeepgenerated(this.convertToBoolean(var6.getValue()));
                  HTTPLogger.logGlassfishDescriptorParsed("keepgenerated");
               } else if ("scratchdir".equals(var6.getName())) {
                  var3.setWorkingDir(var6.getValue());
                  HTTPLogger.logGlassfishDescriptorParsed("scratchdir");
               }
            }
         }
      } while(var1.hasNext() && !this.isEndTag(var4, var1, "jsp-config"));

   }
}
