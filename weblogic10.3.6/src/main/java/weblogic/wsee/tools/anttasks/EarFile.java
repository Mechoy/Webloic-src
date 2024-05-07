package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationDescriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.WebBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.wsee.util.DescriptorBeanUtil;
import weblogic.wsee.util.IOUtil;

class EarFile {
   private File earDir = null;
   private File applicationXml = null;
   private File weblogicApplicationXml = null;
   private boolean appBeanDirty = false;
   private boolean weblogicAppBeanDirty = false;
   private ApplicationBean appBean = null;
   private WeblogicApplicationBean weblogicAppBean = null;
   private String enc = null;
   private Set<String> addedModules = new HashSet();

   EarFile(File var1, String var2) {
      this.earDir = var1;
      this.enc = var2;
      this.setApplicationXml((File)null);
   }

   void setApplicationXml(File var1) {
      if (var1 == null) {
         this.applicationXml = new File(this.earDir, "META-INF/application.xml");
      } else {
         this.applicationXml = var1;
      }

      this.weblogicApplicationXml = new File(this.applicationXml.getParentFile(), "weblogic-application.xml");
   }

   File getEarDir() {
      return this.earDir;
   }

   File getAppInfDir() {
      return new File(this.earDir, "APP-INF");
   }

   void addWebModule(String var1, String var2) throws IOException {
      ApplicationBean var3 = this.getApplicationBean();
      ModuleBean[] var4 = var3.getModules();
      boolean var5 = false;

      WebBean var7;
      for(int var6 = 0; var6 < var4.length && !var5; ++var6) {
         var7 = var4[var6].getWeb();
         if (var7 != null) {
            if (var7.getWebUri().equals(var1)) {
               var5 = true;
               if (!var7.getContextRoot().equals(var2)) {
                  var7.setContextRoot(var2);
                  this.appBeanDirty = true;
               }
            } else if (var7.getContextRoot().equals(var2)) {
               throw new IOException("Context path " + var2 + " for web application " + var1 + " is already in use by this application.");
            }
         }
      }

      if (!var5) {
         ModuleBean var8 = var3.createModule();
         var7 = var8.createWeb();
         var7.setWebUri(var1);
         var7.setContextRoot(var2);
         this.appBeanDirty = true;
      }

      this.addedModules.add(var1);
   }

   void addEjbModule(String var1) throws IOException {
      ApplicationBean var2 = this.getApplicationBean();
      ModuleBean[] var3 = var2.getModules();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var1.equals(var3[var4].getEjb())) {
            return;
         }
      }

      ModuleBean var5 = var2.createModule();
      var5.setEjb(var1);
      this.appBeanDirty = true;
      this.addedModules.add(var1);
   }

   boolean isAdded(String var1) {
      return this.addedModules.contains(var1);
   }

   void addLibrary(String var1) throws IOException {
      WeblogicApplicationBean var2 = this.getWeblogicApplicationBean();
      LibraryRefBean[] var3 = var2.getLibraryRefs();
      LibraryRefBean[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         LibraryRefBean var7 = var4[var6];
         if (var7.getLibraryName().equals(var1)) {
            return;
         }
      }

      LibraryRefBean var8 = var2.createLibraryRef();
      var8.setLibraryName(var1);
      this.weblogicAppBeanDirty = true;
   }

   private synchronized ApplicationBean getApplicationBean() throws IOException {
      if (this.appBean == null) {
         System.out.println("[EarFile] Application File : " + this.applicationXml.getAbsolutePath());
         ApplicationDescriptor var1 = new ApplicationDescriptor(this.applicationXml, (File)null, (File)null, (File)null, (DeploymentPlanBean)null, (String)null);
         var1.setValidateSchema(false);

         try {
            this.appBean = var1.getApplicationDescriptor();
         } catch (IOException var4) {
         } catch (XMLStreamException var5) {
         }

         if (this.appBean == null) {
            this.appBean = (ApplicationBean)(new EditableDescriptorManager()).createDescriptorRoot(ApplicationBean.class).getRootBean();
            this.appBean.setVersion("5");
            int var2 = this.earDir.getAbsolutePath().lastIndexOf("/");
            if (var2 == -1) {
               var2 = this.earDir.getAbsolutePath().lastIndexOf("\\");
            }

            String var3 = this.earDir.getAbsolutePath().substring(var2 + 1);
            this.appBean.setDisplayNames(new String[]{var3});
         }
      }

      return this.appBean;
   }

   private synchronized WeblogicApplicationBean getWeblogicApplicationBean() throws IOException {
      if (this.weblogicAppBean == null) {
         this.weblogicAppBean = DescriptorBeanUtil.loadWeblogicAppBean(this.weblogicApplicationXml);
         if (this.weblogicAppBean == null) {
            this.weblogicAppBean = (WeblogicApplicationBean)(new EditableDescriptorManager()).createDescriptorRoot(WeblogicApplicationBean.class).getRootBean();
         }
      }

      return this.weblogicAppBean;
   }

   void write() throws IOException {
      this.writeBean(this.applicationXml, this.getApplicationBean());
      this.writeBean(this.weblogicApplicationXml, this.getWeblogicApplicationBean());
      this.addedModules.clear();
   }

   private void writeBean(File var1, Object var2) throws IOException {
      var1.getParentFile().mkdirs();
      OutputStream var3 = IOUtil.createEncodedFileOutputStream(var1, this.enc);

      try {
         (new EditableDescriptorManager()).writeDescriptorAsXML(((DescriptorBean)var2).getDescriptor(), var3, this.enc);
         var3.flush();
      } finally {
         var3.close();
      }

   }
}
