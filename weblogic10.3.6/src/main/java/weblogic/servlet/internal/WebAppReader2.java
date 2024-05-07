package weblogic.servlet.internal;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.ReaderEvent2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.OldDescriptorCompatibility;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;

public class WebAppReader2 extends VersionMunger {
   private static boolean enableReordering = Boolean.getBoolean("weblogic.servlet.descriptor.enableReordering");
   private boolean requiresLeadingForwardSlash = false;
   private boolean inEjbRef = false;
   private boolean inEjbLink = false;
   private ArrayList taglibs = new ArrayList();
   private ReaderEvent2 jspConfig = null;

   public WebAppReader2(InputStream var1, AbstractDescriptorLoader2 var2) throws XMLStreamException {
      super(var1, var2, "weblogic.j2ee.descriptor.WebAppBeanImpl$SchemaHelper2", !enableReordering);
   }

   public String getDtdNamespaceURI() {
      return "http://java.sun.com/xml/ns/javaee";
   }

   public String getNamespaceURI() {
      String var1 = super.getNamespaceURI();
      return var1 != null && var1 != "" ? var1 : this.getDtdNamespaceURI();
   }

   protected VersionMunger.Continuation onStartElement(String var1) {
      this.requiresLeadingForwardSlash = this.requiresLeadingForwardSlash(this.getLocalName());
      if ("taglib".equals(var1)) {
         this.getTaglibs().add(this.currentEvent);
      }

      if ("ejb-ref".equals(var1)) {
         this.inEjbRef = true;
      }

      if ("ejb-link".equals(var1)) {
         this.inEjbLink = true;
      }

      return this.inEjbRef && "run-as".equals(var1) ? this.SKIP : CONTINUE;
   }

   protected VersionMunger.Continuation onCharacters(String var1) {
      if (this.inEjbLink) {
         this.replaceSlashWithPeriod(this.inEjbLink);
      }

      return CONTINUE;
   }

   protected VersionMunger.Continuation onEndElement(String var1) {
      this.requiresLeadingForwardSlash = false;
      if ("ejb-ref".equals(var1)) {
         this.inEjbRef = false;
      }

      if ("ejb-link".equals(var1)) {
         this.inEjbLink = false;
      }

      if (this.getTaglibs().size() > 0 && "web-app".equals(var1)) {
         if (this.debug) {
            System.out.println("taglibs = " + this.getTaglibs());
         }

         System.out.flush();
         ReaderEvent2 var2 = this.lastEvent;
         SchemaHelper var3 = var2.getSchemaHelper();
         if (this.debug) {
            System.out.println("lastEvent = " + this.lastEvent + ", parentSchemaHelper = " + var3);
         }

         this.jspConfig = new ReaderEvent2(1, "jsp-config", var2, var2.getLocation());
         int var4 = var3.getPropertyIndex(this.jspConfig.getElementName());
         SchemaHelper var5 = var3.getSchemaHelper(var4);
         this.jspConfig.setSchemaHelper(var5);

         for(int var6 = 0; var6 < this.getTaglibs().size(); ++var6) {
            ReaderEvent2 var7 = (ReaderEvent2)this.getTaglibs().get(var6);
            var7.getParent().getChildren().remove(var7);
            this.jspConfig.adopt(var7, var5);
         }

         var2.adopt(this.jspConfig, var3);
         this.getTaglibs().clear();
      }

      return CONTINUE;
   }

   protected VersionMunger.Continuation onEndDocument() {
      this.orderChildren();
      return CONTINUE;
   }

   private boolean requiresLeadingForwardSlash(String var1) {
      return "location".equals(var1);
   }

   public String getText() {
      if (this.debug) {
         System.out.println("** WebAppReader.getText()");
      }

      String var1 = super.getText();
      if (!this.hasDTD()) {
         return var1;
      } else {
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

         if (var1.charAt(0) != '/' && this.requiresLeadingForwardSlash) {
            var1 = "/" + var1;
            if (this.debug) {
               System.out.println("txt, replaced = " + var1 + " for " + this.currentEvent.getElementName());
            }

            this.currentEvent.getReaderEventInfo().setCharacters(var1.toCharArray());
         }

         return var1;
      }
   }

   public char[] getTextCharacters() {
      if (this.debug) {
         System.out.println("** WebAppReader2.getTextCharacters()");
      }

      char[] var1 = super.getTextCharacters();
      if (!this.hasDTD()) {
         return var1;
      } else {
         if (var1 != null) {
            String var2 = OldDescriptorCompatibility.canonicalize(this.currentEvent.getElementName(), new String(var1));
            if (var2 != null) {
               System.arraycopy(var2.toCharArray(), 0, var1, 0, var2.length());
               if (this.debug) {
                  System.out.println("chars = " + new String(var1) + ", replaced = " + var2);
               }

               this.currentEvent.getReaderEventInfo().setCharacters(var1);
            }

            if (var1[0] != '/' && this.requiresLeadingForwardSlash) {
               char[] var3 = new char[var1.length + 1];
               System.arraycopy(var1, 0, var3, 1, var1.length);
               var3[0] = '/';
               var1 = var3;
               if (this.debug) {
                  System.out.println("txt, replaced = " + new String(var3) + " for " + this.currentEvent.getElementName());
               }

               this.currentEvent.getReaderEventInfo().setCharacters(var3);
            }
         }

         return var1;
      }
   }

   public String getElementText() throws XMLStreamException {
      if (this.debug) {
         System.out.println("** WebAppReader2.getElementText()");
      }

      String var1 = super.getElementText();
      if (!this.hasDTD()) {
         return var1;
      } else {
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
   }

   private ArrayList getTaglibs() {
      if (this.taglibs == null) {
         this.taglibs = new ArrayList();
      }

      return this.taglibs;
   }

   protected String getLatestSchemaVersion() {
      return "2.5";
   }

   protected boolean isOldSchema() {
      String var1 = this.getNamespaceURI();
      if (var1 != null && var1.indexOf("j2ee") != -1) {
         this.isOldSchema = true;
         this.versionInfo = "2.4";
         return this.isOldSchema;
      } else {
         return false;
      }
   }

   protected void transformOldSchema() {
      if (this.currentEvent.getElementName().equals("web-app")) {
         int var1 = this.currentEvent.getReaderEventInfo().getAttributeCount();

         for(int var2 = 0; var2 < var1; ++var2) {
            String var3 = this.currentEvent.getReaderEventInfo().getAttributeLocalName(var2);
            String var4 = this.currentEvent.getReaderEventInfo().getAttributeValue(var2);
            if (var4.equals("2.4")) {
               this.versionInfo = var4;
               this.currentEvent.getReaderEventInfo().setAttributeValue("2.5", var2);
            }
         }

         this.transformNamespace("http://java.sun.com/xml/ns/javaee", this.currentEvent, "http://java.sun.com/xml/ns/j2ee");
      }

      this.tranformedNamespace = "http://java.sun.com/xml/ns/javaee";
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         usage();
         System.exit(-1);
      }

      String var1 = var0[0];
      String var2 = var0.length > 1 && var0[1].endsWith("plan.xml") ? var0[1] : null;
      File var3 = new File(var1);
      Object var4 = null;
      File var5 = new File(".");
      DeploymentPlanBean var6 = null;
      String var7 = var0.length > 2 ? var0[2] : null;
      AbstractDescriptorLoader2 var10;
      if (var2 != null) {
         if (var7 == null) {
            usage();
            System.exit(-1);
         }

         var10 = new AbstractDescriptorLoader2(new File(var2), var2) {
         };
         var6 = (DeploymentPlanBean)var10.loadDescriptorBean();
      }

      var10 = new AbstractDescriptorLoader2(var3, var5, var6, var7, var1) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            System.out.println("call it...");
            return new WebAppReader2(var1, this);
         }
      };
      System.out.println("stamp out version munger for: " + var1);
      System.out.flush();
      DescriptorBean var11 = var10.loadDescriptorBean();
      Descriptor var12 = var11.getDescriptor();
      var12.toXML(System.out);
   }

   private static void usage() {
      System.out.print("java weblogic.servlet.internal.WebAppReader2 <dd-filename> || <dd-filename> <plan-filename> <module-name>");
   }
}
