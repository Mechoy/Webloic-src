package weblogic.servlet.internal.dd.glassfish;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.j2ee.descriptor.wl.SessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;

public class SessionConfigTagParser extends BaseGlassfishTagParser {
   void parse(XMLStreamReader var1, WeblogicWebAppBean var2) throws XMLStreamException {
      SessionDescriptorBean var3 = var2.createSessionDescriptor();

      int var4;
      do {
         var4 = var1.next();
         if (var4 == 1) {
            String var5 = var1.getLocalName();
            if ("session-manager".equals(var5)) {
               this.parseSessionManager(var1, var3);
            } else if ("session-properties".equals(var5)) {
               this.parseSessionProperties(var1, var3);
            }
         }
      } while(var1.hasNext() && !this.isEndTag(var4, var1, "session-config"));

   }

   void parseSessionManager(XMLStreamReader var1, SessionDescriptorBean var2) throws XMLStreamException {
      int var3;
      do {
         var3 = var1.next();
         if (var3 == 1) {
            String var4 = var1.getLocalName();
            if ("manager-properties".equals(var4)) {
               this.parseManagerProperties(var1, var2);
            } else if ("store-properties".equals(var4)) {
               this.parseStoreProperties(var1, var2);
            }
         }
      } while(var1.hasNext() && !this.isEndTag(var3, var1, "session-manager"));

   }

   private void parseSessionProperties(XMLStreamReader var1, SessionDescriptorBean var2) throws XMLStreamException {
      int var3;
      do {
         var3 = var1.next();
         if (var3 == 1) {
            String var4 = var1.getLocalName();
            if ("property".equals(var4)) {
               BaseGlassfishTagParser.Property var5 = this.getProperty(var1);
               if ("timeoutSeconds".equals(var5.getName())) {
                  var2.setTimeoutSecs(Integer.parseInt(var5.getValue()));
                  HTTPLogger.logGlassfishDescriptorParsed("timeoutSeconds");
               }
            }
         }
      } while(var1.hasNext() && !this.isEndTag(var3, var1, "session-properties"));

   }

   private void parseStoreProperties(XMLStreamReader var1, SessionDescriptorBean var2) throws XMLStreamException {
      int var3;
      do {
         var3 = var1.next();
         if (var3 == 1) {
            String var4 = var1.getLocalName();
            if ("property".equals(var4)) {
               BaseGlassfishTagParser.Property var5 = this.getProperty(var1);
               if ("directory".equals(var5.getName())) {
                  var2.setPersistentStoreDir(var5.getValue());
                  HTTPLogger.logGlassfishDescriptorParsed("directory");
               }
            }
         }
      } while(var1.hasNext() && !this.isEndTag(var3, var1, "store-properties"));

   }

   private void parseManagerProperties(XMLStreamReader var1, SessionDescriptorBean var2) throws XMLStreamException {
      int var3;
      do {
         var3 = var1.next();
         if (var3 == 1) {
            String var4 = var1.getLocalName();
            if ("property".equals(var4)) {
               BaseGlassfishTagParser.Property var5 = this.getProperty(var1);
               if ("reapIntervalSeconds".equals(var5.getName())) {
                  var2.setInvalidationIntervalSecs(Integer.parseInt(var5.getValue()));
                  HTTPLogger.logGlassfishDescriptorParsed("reapIntervalSeconds");
               } else if ("maxSessions".equals(var5.getName())) {
                  var2.setMaxInMemorySessions(Integer.parseInt(var5.getValue()));
                  HTTPLogger.logGlassfishDescriptorParsed("maxSessions");
               }
            }
         }
      } while(var1.hasNext() && !this.isEndTag(var3, var1, "manager-properties"));

   }
}
