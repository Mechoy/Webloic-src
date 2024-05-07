package weblogic.j2ee.dd;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import weblogic.application.ApplicationFileManager;
import weblogic.j2ee.J2EELogger;
import weblogic.management.descriptors.application.WebModuleMBean;
import weblogic.utils.io.XMLWriter;
import weblogic.utils.jars.VirtualJarFile;

public final class WebModuleDescriptor extends ModuleDescriptor implements WebModuleMBean {
   private static final long serialVersionUID = -4976877915175897268L;
   private static final boolean debug = false;
   private String context;

   public WebModuleDescriptor() {
   }

   public WebModuleDescriptor(String var1, String var2) {
      super(var1);
      this.context = var2;
   }

   public void setContext(String var1) {
      String var2 = this.context;
      this.context = var1;
      this.checkChange("context", var2, var1);
   }

   public String getContext() {
      return this.context;
   }

   public void setWebURI(String var1) {
      this.setURI(var1);
   }

   public void setModuleURI(String var1) {
      this.setURI(var1);
   }

   public String getModuleURI() {
      return this.getURI();
   }

   public void setContextRoot(String var1) {
      this.setContext(var1);
   }

   public String getContextRoot() {
      return this.getContext();
   }

   public String getModuleKey() {
      return this.getModuleURI() + "_" + this.getContextRoot();
   }

   public void toXML(XMLWriter var1) {
      var1.println("<module>");
      var1.incrIndent();
      var1.println("<web>");
      var1.incrIndent();
      var1.println("<web-uri>" + this.getURI() + "</web-uri>");
      if (this.getContext() != null) {
         var1.println("<context-root>" + this.getContext() + "</context-root>");
      }

      var1.decrIndent();
      var1.println("</web>");
      if (this.getAltDDURI() != null) {
         var1.println("<alt-dd>" + this.getAltDDURI() + "</alt-dd>");
      }

      var1.decrIndent();
      var1.println("</module>");
   }

   public String getAdminMBeanType(ApplicationFileManager var1) {
      return this.isWebService(var1) ? "WebServiceComponent" : "WebAppComponent";
   }

   private boolean isWebService(ApplicationFileManager var1) {
      VirtualJarFile var2 = null;

      boolean var4;
      try {
         var2 = var1.getVirtualJarFile();
         if (!var2.isDirectory()) {
            if (var2.getEntry(this.getModuleURI() + "/" + "WEB-INF/web-services.xml") != null) {
               boolean var50 = true;
               return var50;
            }

            ZipEntry var49 = var2.getEntry(this.getModuleURI());
            if (var49 != null) {
               InputStream var51 = var2.getJarFile().getInputStream(var49);
               if (var51 == null) {
                  boolean var52 = false;
                  return var52;
               }

               ZipInputStream var5 = new ZipInputStream(var51);

               try {
                  for(ZipEntry var6 = var5.getNextEntry(); var6 != null; var6 = var5.getNextEntry()) {
                     if (var6.getName().equals("WEB-INF/web-services.xml")) {
                        boolean var7 = true;
                        return var7;
                     }
                  }
               } finally {
                  var51.close();
                  var5.close();
               }
            }

            var4 = false;
            return var4;
         }

         VirtualJarFile var3 = null;

         try {
            var3 = var1.getVirtualJarFile(this.getURI());
            var4 = var3.getEntry("WEB-INF/web-services.xml") != null;
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Exception var44) {
               }
            }

         }
      } catch (IOException var47) {
         J2EELogger.logErrorCheckingWebService("can't read WEB-INF/web-services.xml", var47);
         var4 = false;
         return var4;
      } finally {
         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (IOException var43) {
         }

      }

      return var4;
   }
}
