package weblogic.ejb.spi;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.ReaderEvent2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.j2ee.OldDescriptorCompatibility;

public final class EjbJarReader extends VersionMunger {
   private boolean doneVersion = false;
   private boolean inIcon = false;
   private boolean isEJB11 = false;
   private boolean isEJB20 = false;
   private boolean inEnvEntryValue = false;
   private boolean inEjbName = false;
   private boolean inEjbLink = false;
   private boolean inActivationConfig = false;
   private boolean inSecurity = false;
   private boolean inQueryMethod = false;
   private boolean inMethodIntf = false;
   private static final Map jmsNameConversion = new HashMap(4);

   public EjbJarReader(InputStream var1, AbstractDescriptorLoader2 var2) throws XMLStreamException {
      super(var1, var2, "weblogic.j2ee.descriptor.EjbJarBeanImpl$SchemaHelper2");
   }

   private boolean isIcon(String var1) {
      return "small-icon".equals(var1) || "large-icon".equals(var1);
   }

   private boolean isSecurity(String var1) {
      return "security-role-ref".equals(var1) || "security-identity".equals(var1);
   }

   public String getDtdNamespaceURI() {
      return "http://java.sun.com/xml/ns/javaee";
   }

   public void initDtdText(String var1) {
      if (var1.lastIndexOf("DTD Enterprise JavaBeans 1.1") > 0) {
         this.isEJB11 = true;
      } else if (var1.lastIndexOf("DTD Enterprise JavaBeans 2.0") > 0) {
         this.isEJB20 = true;
      }

   }

   private boolean needsCMPVersion(String var1) {
      if (this.isEJB11 && !this.doneVersion && "cmp-field".equals(var1)) {
         this.doneVersion = true;
         return true;
      } else {
         return false;
      }
   }

   private VersionMunger.Continuation startIcon(String var1) {
      this.currentEvent.discard();
      this.inIcon = true;
      this.forceSkipParent = true;
      this.pushStartElement("icon");
      this.pushStartElementWithStackAsParent(var1);
      return CONTINUE;
   }

   private VersionMunger.Continuation continueIcon(String var1) {
      this.currentEvent.discard();
      this.pushStartElementWithStackAsParent(var1);
      return CONTINUE;
   }

   private VersionMunger.Continuation endIcon(String var1) {
      this.inIcon = false;
      this.pushEndElement("icon");
      this.forceSkipParent = false;
      return this.USE_BUFFER;
   }

   private VersionMunger.Continuation cmp11Version(String var1) {
      this.pushStartElement("cmp-version");
      this.pushCharacters("1.x".toCharArray());
      this.pushEndElement("cmp-version");
      return this.USE_BUFFER;
   }

   private boolean isJMSProperty(String var1) {
      return jmsNameConversion.containsKey(var1);
   }

   private String convertJMSName(String var1) {
      String var2 = (String)jmsNameConversion.get(var1);
      return var2 == null ? var1 : var2;
   }

   private VersionMunger.Continuation startJMSProperty(String var1) {
      if (!this.inActivationConfig) {
         this.forceSkipParent = true;
         this.pushStartElement("activation-config");
         this.inActivationConfig = true;
      }

      this.pushStartElementWithStackAsParent("activation-config-property");
      this.pushStartElementWithStackAsParent("activation-config-property-name");
      this.pushCharacters(this.convertJMSName(var1).toCharArray());
      this.pushEndElement("activation-config-property-name");
      this.pushStartElementWithStackAsParent("activation-config-property-value");
      this.currentEvent.discard();
      return this.USE_BUFFER;
   }

   private VersionMunger.Continuation endActivationConfig() {
      return this.endActivationConfig((String)null);
   }

   private VersionMunger.Continuation endActivationConfig(String var1) {
      this.inActivationConfig = false;
      this.forceSkipParent = false;
      this.pushEndElement("activation-config");
      return this.USE_BUFFER;
   }

   public VersionMunger.Continuation onStartElement(String var1) {
      if ("query-method".equals(var1)) {
         this.inQueryMethod = true;
      }

      if (this.inQueryMethod && "method-intf".equals(var1)) {
         this.inMethodIntf = true;
         return this.SKIP;
      } else {
         if ("ejb-name".equals(var1)) {
            this.doneVersion = false;
         }

         if (this.isIcon(var1)) {
            return !this.inIcon ? this.startIcon(var1) : this.continueIcon(var1);
         } else if (this.inIcon) {
            return this.endIcon(var1);
         } else if (this.needsCMPVersion(var1)) {
            return this.cmp11Version(var1);
         } else if ("message-driven-destination".equals(var1)) {
            return this.SKIP;
         } else if (this.isJMSProperty(var1)) {
            return this.startJMSProperty(var1);
         } else if (this.inActivationConfig) {
            return this.endActivationConfig(var1);
         } else {
            if ("env-entry-value".equals(var1)) {
               this.inEnvEntryValue = true;
            }

            if ("ejb-name".equals(var1)) {
               this.inEjbName = true;
            }

            if ("ejb-link".equals(var1)) {
               this.inEjbLink = true;
            }

            return CONTINUE;
         }
      }
   }

   protected VersionMunger.Continuation onCharacters(String var1) {
      if (this.inMethodIntf) {
         return this.SKIP;
      } else {
         if (this.currentEvent.isDiscarded() && this.stack.size() > 0) {
            ReaderEvent2 var2 = (ReaderEvent2)this.stack.peek();
            var2.getReaderEventInfo().setCharacters(var1.toCharArray());
         }

         if (this.inEnvEntryValue && var1 != null && var1.length() > 0 && (this.hasDTD() || this.isOldSchema())) {
            this.currentEvent.getReaderEventInfo().setCharacters(var1.trim().toCharArray());
         }

         if (this.inEjbName || this.inEjbLink) {
            this.replaceSlashWithPeriod(this.inEjbLink);
         }

         return CONTINUE;
      }
   }

   public VersionMunger.Continuation onEndElement(String var1) {
      if (this.inQueryMethod) {
         if ("query-method".equals(var1)) {
            this.inQueryMethod = false;
         } else if ("method-intf".equals(var1)) {
            this.inMethodIntf = false;
            return this.SKIP;
         }
      }

      if ("message-driven-destination".equals(var1)) {
         return this.SKIP;
      } else if (this.isJMSProperty(var1)) {
         this.pushEndElement("activation-config-property-value");
         this.pushEndElement("activation-config-property");
         return this.USE_BUFFER;
      } else if (this.inActivationConfig) {
         this.inActivationConfig = false;
         this.forceSkipParent = false;
         this.pushEndElement("activation-config");
         this.pushEndElement(var1);
         return this.USE_BUFFER;
      } else {
         if (this.isIcon(var1)) {
            this.pushEndElement(var1);
         }

         if ("env-entry-value".equals(var1)) {
            this.inEnvEntryValue = false;
         }

         if ("ejb-name".equals(var1)) {
            this.inEjbName = false;
         }

         if ("ejb-link".equals(var1)) {
            this.inEjbLink = false;
         }

         return CONTINUE;
      }
   }

   public VersionMunger.Continuation onEndDocument() {
      if (this.inActivationConfig) {
         this.pushEndElement("activation-config");
         return this.USE_BUFFER;
      } else {
         this.orderChildren();
         return CONTINUE;
      }
   }

   protected String getLatestSchemaVersion() {
      return "3.0";
   }

   protected boolean isOldSchema() {
      String var1 = this.getNamespaceURI();
      return var1 != null && var1.indexOf("j2ee") != -1;
   }

   protected void transformOldSchema() {
      if (this.currentEvent.getElementName().equals("ejb-jar")) {
         int var1 = this.currentEvent.getReaderEventInfo().getAttributeCount();

         for(int var2 = 0; var2 < var1; ++var2) {
            String var3 = this.currentEvent.getReaderEventInfo().getAttributeLocalName(var2);
            String var4 = this.currentEvent.getReaderEventInfo().getAttributeValue(var2);
            if (var4.equals("2.1")) {
               this.versionInfo = var4;
               this.currentEvent.getReaderEventInfo().setAttributeValue("3.0", var2);
            }
         }

         this.transformNamespace("http://java.sun.com/xml/ns/javaee", this.currentEvent, "http://java.sun.com/xml/ns/j2ee");
      }

      this.tranformedNamespace = "http://java.sun.com/xml/ns/javaee";
   }

   public String getText() {
      if (this.debug) {
         System.out.println("** EjbJarReader.getText() " + this.currentEvent.getElementName());
      }

      String var1 = super.getText().trim();
      if (var1 != null) {
         String var2 = OldDescriptorCompatibility.canonicalize(this.currentEvent.getElementName(), var1);
         if (var2 != null) {
            if (this.debug) {
               System.out.println("txt = " + var1 + ", replaced = " + var2);
            }

            this.currentEvent.getReaderEventInfo().setCharacters(var2.toCharArray());
            return var2;
         }
      }

      return var1;
   }

   public char[] getTextCharacters() {
      if (this.debug) {
         System.out.println("** EjbJarReader.getTextCharacters()" + this.currentEvent.getElementName());
      }

      char[] var1 = super.getTextCharacters();
      if (var1 != null) {
         String var2 = OldDescriptorCompatibility.canonicalize(this.currentEvent.getElementName(), new String(var1));
         if (var2 != null) {
            System.arraycopy(var2.toCharArray(), 0, var1, 0, var2.length());
            if (this.debug) {
               System.out.println("chars = " + new String(var1) + ", replaced = " + var2);
            }

            this.currentEvent.getReaderEventInfo().setCharacters(var1);
         }
      }

      return var1;
   }

   public String getElementText() throws XMLStreamException {
      if (this.debug) {
         System.out.println("** EjbJarReader.getElementText()" + this.currentEvent.getElementName());
      }

      String var1 = super.getElementText();
      if (var1 != null) {
         String var2 = OldDescriptorCompatibility.canonicalize(this.currentEvent.getElementName(), var1);
         if (var2 != null) {
            if (this.debug) {
               System.out.println("txt = " + var1 + ", replaced = " + var2);
            }

            return var2;
         }
      }

      return var1;
   }

   public boolean supportsValidation() {
      return true;
   }

   static {
      jmsNameConversion.put("acknowledge-mode", "acknowledgeMode");
      jmsNameConversion.put("message-selector", "messageSelector");
      jmsNameConversion.put("destination-type", "destinationType");
      jmsNameConversion.put("subscription-durability", "subscriptionDurability");
   }
}
