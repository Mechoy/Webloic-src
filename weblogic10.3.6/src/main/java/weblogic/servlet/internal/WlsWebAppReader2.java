package weblogic.servlet.internal;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.servlet.internal.session.SessionConfigManager;

public class WlsWebAppReader2 extends VersionMunger {
   private static final Map nameChanges = new HashMap(2);
   private boolean inSessionParam = false;
   private boolean inJspParam = false;
   private boolean inContainerDescriptor = false;
   private String containerDescName = null;
   private final StringBuffer containerDescValue = new StringBuffer();
   private final List containerDescElements = new ArrayList();
   private Map servletDescriptors = new HashMap();
   private boolean inDeletedElement = false;
   private boolean isDeprecated = false;
   private boolean lookupFailed = false;
   boolean inParamName = false;
   String lastParamName = null;

   public WlsWebAppReader2(InputStream var1, AbstractDescriptorLoader2 var2) throws XMLStreamException {
      super(var1, var2, "weblogic.j2ee.descriptor.wl.WeblogicWebAppBeanImpl$SchemaHelper2", nameChanges, "http://xmlns.oracle.com/weblogic/weblogic-web-app");
   }

   public String getNamespaceURI() {
      String var1 = super.getNamespaceURI();
      return var1 != null && var1 != "" ? var1 : this.getDtdNamespaceURI();
   }

   protected VersionMunger.Continuation onStartElement(String var1) {
      if ("reference-descriptor".equals(var1)) {
         return this.SKIP;
      } else {
         if ("preprocessor".equals(var1) || "preprocessor-mapping".equals(var1)) {
            this.inDeletedElement = true;
         }

         if (this.inDeletedElement) {
            return this.SKIP;
         } else if ("session-param".equals(var1)) {
            this.inSessionParam = true;
            return this.SKIP;
         } else if ("jsp-param".equals(var1)) {
            this.inJspParam = true;
            return this.SKIP;
         } else if ("param-name".equals(var1)) {
            this.inParamName = true;
            return this.SKIP;
         } else if ("param-value".equals(var1)) {
            if (this.isDeprecated) {
               J2EELogger.logDeprecatedWeblogicParam(this.currentEvent.getParent().getElementName(), this.lastParamName);
               return this.SKIP;
            } else {
               if (this.lookupFailed) {
                  J2EELogger.logUnknownWeblogicParam(this.currentEvent.getParent().getElementName(), this.lastParamName);
                  if (this.inSessionParam) {
                     return this.SKIP;
                  }
               }

               this.currentEvent.setElementName(this.lastParamName);
               this.currentEvent.setDiscard(false);
               return CONTINUE;
            }
         } else {
            return CONTINUE;
         }
      }
   }

   protected VersionMunger.Continuation onCharacters(String var1) {
      if (this.inDeletedElement) {
         return this.SKIP;
      } else if (this.inParamName) {
         this.lastParamName = this.getSessionElementName2(var1);
         return this.SKIP;
      } else {
         return CONTINUE;
      }
   }

   String getSessionElementName2(String var1) {
      String var2 = null;
      if (var1 != null) {
         if (var1.trim().toLowerCase().length() == 0) {
            return var1.trim();
         }

         if (this.inSessionParam) {
            var2 = (String)SessionConfigManager.SESSION_ELEMENTS_MAP.get(var1.trim().toLowerCase());
            if (var2 == null) {
               this.lookupFailed = true;
            }
         }

         if (this.inJspParam) {
            var2 = (String)JSPManager.JSP_DESC_ELEMENTS_MAP.get(var1.trim().toLowerCase());
            if (var2 == "warning") {
               this.isDeprecated = true;
            }

            if (var2 == null) {
               this.lookupFailed = true;
            }
         }
      }

      return var2 == null ? var1.trim() : var2;
   }

   protected VersionMunger.Continuation onEndElement(String var1) {
      if ("reference-descriptor".equals(var1)) {
         return this.SKIP;
      } else if (this.inParamName && "param-name".equals(var1)) {
         this.inParamName = false;
         return this.SKIP;
      } else if ("param-value".equals(var1)) {
         this.currentEvent.setElementName(this.lastParamName);
         this.isDeprecated = false;
         this.lookupFailed = false;
         this.lastParamName = null;
         return CONTINUE;
      } else if (!this.inDeletedElement) {
         if ("session-param".equals(var1)) {
            this.inSessionParam = false;
            return this.SKIP;
         } else if ("jsp-param".equals(var1)) {
            this.inJspParam = false;
            return this.SKIP;
         } else {
            if ("session-descriptor".equals(var1)) {
               this.lastEvent.orderChildren();
            }

            if ("jsp-descriptor".equals(var1)) {
               this.lastEvent.orderChildren();
            }

            return CONTINUE;
         }
      } else {
         if ("preprocessor".equals(var1) || "preprocessor-mapping".equals(var1)) {
            this.inDeletedElement = false;
         }

         return this.SKIP;
      }
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
            return new WlsWebAppReader2(var1, this);
         }
      };
      System.out.println("stamp out version munger...");
      System.out.flush();
      DescriptorBean var11 = var10.loadDescriptorBean();
      Descriptor var12 = var11.getDescriptor();
      var12.toXML(System.out);
   }

   private static void usage() {
      System.out.print("java weblogic.servlet.internal.WlsWebAppReader2 <dd-filename> || <dd-filename> <plan-filename> <module-name>");
   }

   static {
      nameChanges.put("global-role", "externally-defined");
      nameChanges.put("resource-env-descriptor", "resource-env-description");
      nameChanges.put("res-env-ref-name", "resource-env-ref-name");
   }
}
