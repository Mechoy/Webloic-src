package weblogic.application.internal;

import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;

public class ApplicationReader extends VersionMunger {
   private boolean inWeb;
   private boolean hasSetContextRoot;
   private int crCount = 0;

   public ApplicationReader(InputStream var1, AbstractDescriptorLoader2 var2) throws XMLStreamException {
      super(var1, var2, "weblogic.j2ee.descriptor.ApplicationBeanImpl$SchemaHelper2");
   }

   public String getDtdNamespaceURI() {
      return "http://java.sun.com/xml/ns/javaee";
   }

   protected VersionMunger.Continuation onStartElement(String var1) {
      if ("web".equals(var1)) {
         this.inWeb = true;
         this.hasSetContextRoot = false;
      } else if ("context-root".equals(var1)) {
         this.hasSetContextRoot = true;
      }

      return CONTINUE;
   }

   protected VersionMunger.Continuation onEndElement(String var1) {
      if ("web".equals(var1)) {
         assert this.inWeb;

         this.inWeb = false;
         if (!this.hasSetContextRoot) {
            this.pushStartElementLastEvent("context-root");
            this.pushCharacters("__BEA_WLS_INTERNAL_UNSET_CONTEXT_ROOT_" + this.crCount++);
            this.pushEndElement("context-root");
            this.pushEndElement("web");
            return this.USE_BUFFER;
         }
      }

      return CONTINUE;
   }

   protected boolean isOldSchema() {
      String var1 = this.getNamespaceURI();
      return var1 != null && var1.indexOf("j2ee") != -1;
   }

   protected void transformOldSchema() {
      if (this.currentEvent.getElementName().equals("application")) {
         int var1 = this.currentEvent.getReaderEventInfo().getAttributeCount();

         for(int var2 = 0; var2 < var1; ++var2) {
            this.currentEvent.getReaderEventInfo().getAttributeLocalName(var2);
            String var3 = this.currentEvent.getReaderEventInfo().getAttributeValue(var2);
            if (var3.equals("1.4")) {
               this.versionInfo = var3;
               this.currentEvent.getReaderEventInfo().setAttributeValue("5", var2);
            }
         }

         this.transformNamespace("http://java.sun.com/xml/ns/javaee", this.currentEvent, "http://java.sun.com/xml/ns/j2ee");
      }

      this.tranformedNamespace = "http://java.sun.com/xml/ns/javaee";
   }
}
