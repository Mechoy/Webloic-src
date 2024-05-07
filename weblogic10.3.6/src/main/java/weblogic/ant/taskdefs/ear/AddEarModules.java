package weblogic.ant.taskdefs.ear;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import weblogic.application.ApplicationDescriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorException;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.WebBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;

public class AddEarModules extends Task {
   private static final int TYPE_None = 0;
   private static final int TYPE_Ejb = 1;
   private static final int TYPE_WebApp = 2;
   private static final String EXT_War = ".war";
   private static final String EXT_Jar = ".jar";
   private static final String DIR_AppLib = "app-inf/lib";
   private static final String DIR_WebLib = "web-inf/lib";
   private static final String WEBAPP = "web-inf/web.xml";
   private static final String EJB = "meta-inf/ejb-jar.xml";
   private File _earDir;
   private File _earFile;
   private EarFile _appXml;
   private boolean _update;
   private Vector _modules = new Vector();

   public void setEar(String var1) {
      this._earDir = new File(var1);
      if (!this._earDir.isDirectory()) {
         this._earFile = this._earDir;
         this._earDir = null;
      }

   }

   public void setUpdate(boolean var1) {
      this._update = var1;
   }

   public void addEjb(Ejb var1) {
      this._modules.add(var1);
   }

   public void addWeb(Web var1) {
      this._modules.add(var1);
   }

   public void execute() throws BuildException {
      if (this._earDir == null && this._earFile != null) {
         if (!this._earFile.exists()) {
            throw new BuildException("'" + this._earFile + "' is not a file or directory");
         }

         this.removeTmpDir();
         this._earDir = this.getTmpDir();
         this.expand();
      }

      if (this._earDir.exists() && this._earDir.isDirectory()) {
         this._appXml = new EarFile(this._earDir);
         Enumeration var1 = this._modules.elements();

         while(var1.hasMoreElements()) {
            Ejb var2 = (Ejb)var1.nextElement();
            File var3 = new File(this._earDir, var2.getUri());
            if (var2 instanceof Web) {
               Web var4 = (Web)var2;
               this.copyModule(var4);
               if (this.getModuleType(var3) != 2) {
                  this.log("Module at '" + var3 + "' is not a web-app", 1);
               }

               this.addWebModule(var3, var4.getContext());
            } else {
               this.copyModule(var2);
               if (this.getModuleType(var3) != 1) {
                  this.log("Module at '" + var3 + "' is not an ejb", 1);
               }

               this.addEjbModule(var3);
            }
         }

         if (this._update) {
            this.checkDirectory();
         }

         try {
            this._appXml.write();
         } catch (IOException var5) {
            throw new BuildException("Error updating application.xml in ear");
         }

         if (this._earFile != null) {
            this.zip();
         }

      } else {
         throw new BuildException("'" + this._earDir + "' is not a directory");
      }
   }

   private void removeTmpDir() throws BuildException {
      File var1 = this.getTmpDir();
      if (var1.exists()) {
         this.log("Deleting: " + var1.getAbsolutePath());
         this.removeDir(var1);
         var1.delete();
      }
   }

   private void removeDir(File var1) throws BuildException {
      String[] var2 = var1.list();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         String var4 = var2[var3];
         File var5 = new File(var1, var4);
         if (var5.isDirectory()) {
            this.removeDir(var5);
         } else if (!var5.delete()) {
            throw new BuildException("Unable to delete file " + var5.getAbsolutePath());
         }
      }

      if (!var1.delete()) {
         throw new BuildException("Unable to delete directory " + var1.getAbsolutePath());
      }
   }

   private void checkDirectory() {
      this.check(this._earDir);
   }

   private void check(File var1) {
      File[] var2 = var1.listFiles();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         File var4 = var2[var3];
         if (var4.isDirectory()) {
            this.check(var4);
         } else {
            String var5 = var4.getAbsolutePath();
            var5 = var5.replace('\\', '/');
            if (var5.toLowerCase().indexOf("app-inf/lib") < 0 && var5.toLowerCase().indexOf("web-inf/lib") < 0) {
               switch (this.checkArchive(var4)) {
                  case 1:
                     this.addEjbModule(var4);
                     break;
                  case 2:
                     this.addWebModule(var4, (String)null);
               }
            }

            if (var5.toLowerCase().endsWith("meta-inf/ejb-jar.xml")) {
               var5 = var4.getAbsolutePath();
               var5 = var5.substring(0, var5.length() - "meta-inf/ejb-jar.xml".length() - 1);
               this.addEjbModule(new File(var5));
            }

            if (var5.toLowerCase().endsWith("web-inf/web.xml")) {
               var5 = var4.getAbsolutePath();
               var5 = var5.substring(0, var5.length() - "web-inf/web.xml".length() - 1);
               this.addWebModule(new File(var5), (String)null);
            }
         }
      }

   }

   private int getModuleType(File var1) {
      int var2 = this.checkArchive(var1);
      if (var2 != 0) {
         return var2;
      } else {
         File var3 = new File(var1, "web-inf/web.xml");
         if (var3.exists()) {
            return 2;
         } else {
            var3 = new File(var1, "meta-inf/ejb-jar.xml");
            return var3.exists() ? 1 : 0;
         }
      }
   }

   public void copyModule(Ejb var1) {
      Enumeration var2 = var1.getFileSets();
      String var3 = var1.getUri();
      String var4 = var1.getSrc();
      File var5 = new File(this._earDir, var3);
      if (var4 != null) {
         File var6 = new File(var4, var3);
         FileSet var7 = new FileSet();
         if (var6.isDirectory()) {
            var7.setDir(var6);
         } else {
            var5 = var5.getParentFile();
            var7.setFile(var6);
         }

         this.copyFileset(var7, var5);
      }

      while(var2.hasMoreElements()) {
         FileSet var8 = (FileSet)var2.nextElement();
         this.copyFileset(var8, var5);
      }

   }

   private void copyFileset(FileSet var1, File var2) throws BuildException {
      Hashtable var3 = new Hashtable();

      try {
         String[] var4 = var1.getDirectoryScanner(this.getProject()).getIncludedFiles();
         this.scanDir(var3, var1.getDir(this.getProject()), var2, var4);
         if (var3.size() == 0) {
            throw new BuildException("No files in fileset dir=" + var1.getDir(this.getProject()));
         }

         if (var3.size() > 0) {
            this.log("Copying " + var3.size() + " file" + (var3.size() == 1 ? "" : "s") + " to " + var2.getAbsolutePath());
            Enumeration var5 = var3.keys();

            while(var5.hasMoreElements()) {
               String var6 = (String)var5.nextElement();
               String var7 = (String)var3.get(var6);

               try {
                  this.getProject().copyFile(var6, var7, false, true);
               } catch (IOException var13) {
                  String var9 = "Failed to copy " + var6 + " to " + var7 + " due to " + var13.getMessage();
                  throw new BuildException(var9, var13, this.getLocation());
               }
            }
         }
      } finally {
         var3.clear();
      }

   }

   private void scanDir(Hashtable var1, File var2, File var3, String[] var4) {
      for(int var5 = 0; var5 < var4.length; ++var5) {
         String var6 = var4[var5];
         File var7 = new File(var2, var6);
         File var8 = new File(var3, var6);
         var1.put(var7.getAbsolutePath(), var8.getAbsolutePath());
      }

   }

   private File getTmpDir() {
      return new File(System.getProperty("java.io.tmpdir"), this._earFile.getName());
   }

   private int checkArchive(File var1) throws BuildException {
      String var2 = var1.getAbsolutePath();
      if (!var2.toLowerCase().endsWith(".jar") && !var2.toLowerCase().endsWith(".war")) {
         return 0;
      } else {
         try {
            ZipFile var3 = new ZipFile(var1);
            Enumeration var4 = var3.getEntries();

            String var6;
            do {
               if (!var4.hasMoreElements()) {
                  var3.close();
                  return 0;
               }

               ZipEntry var5 = (ZipEntry)var4.nextElement();
               var6 = var5.getName();
               var6 = var6.replace('\\', '/');
               if (var6.toLowerCase().equals("web-inf/web.xml")) {
                  return 2;
               }
            } while(!var6.toLowerCase().equals("meta-inf/ejb-jar.xml"));

            return 1;
         } catch (IOException var7) {
            throw new BuildException("Error processing " + var1);
         }
      }
   }

   protected void addWebModule(File var1, String var2) throws BuildException {
      try {
         String var3 = var1.getAbsolutePath();
         boolean var4 = this._appXml.isWebModule(var1);
         if (var2 == null) {
            var2 = var3.substring(this._earDir.getAbsolutePath().length() + 1);
            if (!var1.isDirectory() && (var2.endsWith(".jar") || var2.endsWith(".war"))) {
               var2 = var2.substring(0, var2.length() - ".jar".length());
            }
         }

         this.log((var4 ? "Updating" : "Adding") + " web module '" + this.getRelativeFile(var1) + "' with context '" + var2 + "'");
         this._appXml.addWebModule(var1, var2);
      } catch (IOException var5) {
         throw new BuildException("Error updating application.xml in ear");
      }
   }

   protected void addEjbModule(File var1) throws BuildException {
      try {
         if (!this._appXml.isEjbModule(var1)) {
            this.log("Adding ebj module '" + this.getRelativeFile(var1) + "'");
            this._appXml.addEjbModule(var1);
         }

      } catch (IOException var3) {
         throw new BuildException("Error updating application.xml in ear");
      }
   }

   private void expand() throws BuildException {
      Expand var1 = new Expand();
      var1.setSrc(this._earFile);
      var1.setDest(this._earDir);
      var1.setProject(this.getProject());
      var1.setTaskName(this.getTaskName());
      var1.execute();
   }

   private void zip() throws BuildException {
      Zip var1 = new Zip();
      var1.setFile(this._earFile);
      var1.setBasedir(this._earDir);
      var1.setProject(this.getProject());
      var1.setTaskName(this.getTaskName());
      var1.execute();
   }

   private String getRelativeFile(File var1) throws IOException {
      String var2 = var1.getCanonicalFile().getAbsolutePath().substring(this._earDir.getCanonicalFile().getAbsolutePath().length() + 1).replace('\\', '/');
      return var2;
   }

   private static class EarFile {
      private File earDir = null;
      private boolean appBeanDirty = false;
      private ApplicationBean appBean = null;

      public EarFile(File var1) {
         this.earDir = var1;
      }

      public boolean isWebModule(File var1) throws IOException {
         ApplicationBean var2 = this.getApplicationBean(true);
         String var3 = this.getRelativeFilePath(var1);
         ModuleBean[] var4 = var2.getModules();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            WebBean var6 = var4[var5].getWeb();
            if (var6 != null && var6.getWebUri().equals(var3)) {
               return true;
            }
         }

         return false;
      }

      public void addWebModule(File var1, String var2) throws IOException {
         ApplicationBean var3 = this.getApplicationBean(true);
         String var4 = this.getRelativeFilePath(var1);
         ModuleBean[] var5 = var3.getModules();
         boolean var6 = false;

         WebBean var8;
         for(int var7 = 0; var7 < var5.length && !var6; ++var7) {
            var8 = var5[var7].getWeb();
            if (var8 != null && var8.getWebUri().equals(var4)) {
               var6 = true;
               if (!var8.getContextRoot().equals(var2)) {
                  var8.setContextRoot(var2);
                  this.appBeanDirty = true;
               }
            }
         }

         if (!var6) {
            ModuleBean var9 = var3.createModule();
            var8 = var9.createWeb();
            var8.setWebUri(var4);
            var8.setContextRoot(var2);
            this.appBeanDirty = true;
         }

      }

      public boolean isEjbModule(File var1) throws IOException {
         ApplicationBean var2 = this.getApplicationBean(true);
         String var3 = this.getRelativeFilePath(var1);
         ModuleBean[] var4 = var2.getModules();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5].getEjb();
            if (var6 != null && var3.equals(var4[var5].getEjb())) {
               return true;
            }
         }

         return false;
      }

      public void addEjbModule(File var1) throws IOException {
         ApplicationBean var2 = this.getApplicationBean(true);
         String var3 = this.getRelativeFilePath(var1);
         ModuleBean[] var4 = var2.getModules();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var3.equals(var4[var5].getEjb())) {
               return;
            }
         }

         ModuleBean var6 = var2.createModule();
         var6.setEjb(var3);
         this.appBeanDirty = true;
      }

      private String getRelativeFilePath(File var1) throws IOException {
         String var2 = var1.getCanonicalFile().getAbsolutePath().substring(this.earDir.getCanonicalFile().getAbsolutePath().length() + 1).replace('\\', '/');
         return var2;
      }

      private synchronized ApplicationBean getApplicationBean(boolean var1) throws IOException {
         if (this.appBean == null) {
            File var2 = new File(this.earDir, "META-INF/application.xml");
            if (var2.exists()) {
               try {
                  ApplicationDescriptor var3 = new ApplicationDescriptor(var2, (File)null, (File)null, (File)null, (DeploymentPlanBean)null, (String)null);
                  var3.setValidateSchema(false);
                  this.appBean = var3.getApplicationDescriptor();
               } catch (DescriptorException var4) {
                  if (!var1) {
                     throw new IOException("The existing application.xml at " + var2.getAbsolutePath() + " is corrupted.\n" + var4.getMessage());
                  }

                  System.out.println("[AddEarModules] The existing application.xml at " + var2.getAbsolutePath() + " is corrupted. " + var4.getMessage());
                  System.out.println("[AddEarModules] Recreating a new application.xml");
                  this.createApplicationBean();
               } catch (Exception var5) {
                  throw new IOException("The existing application.xml at " + var2.getAbsolutePath() + " is corrupted.\n" + var5.getMessage());
               }
            }

            if (!var2.exists() && var1) {
               this.createApplicationBean();
            }
         }

         return this.appBean;
      }

      private void createApplicationBean() {
         this.appBean = (ApplicationBean)(new EditableDescriptorManager()).createDescriptorRoot(ApplicationBean.class).getRootBean();
         int var1 = this.earDir.getAbsolutePath().lastIndexOf("/");
         if (var1 == -1) {
            var1 = this.earDir.getAbsolutePath().lastIndexOf("\\");
         }

         String var2 = this.earDir.getAbsolutePath().substring(var1 + 1);
         this.appBean.setDisplayNames(new String[]{var2});
      }

      public void write() throws IOException {
         if (this.appBeanDirty) {
            FileOutputStream var1 = null;

            try {
               File var2 = new File(this.earDir, "META-INF");
               var2.mkdirs();
               File var3 = new File(var2, "application.xml");
               var1 = new FileOutputStream(var3);
               (new EditableDescriptorManager()).writeDescriptorAsXML(((DescriptorBean)this.appBean).getDescriptor(), var1);
            } finally {
               if (var1 != null) {
                  try {
                     var1.close();
                  } catch (IOException var9) {
                  }
               }

            }
         }

      }
   }

   public static class Web extends Ejb {
      private String _context;

      public void setContext(String var1) {
         this._context = var1;
      }

      public String getContext() {
         return this._context;
      }
   }

   public static class Ejb {
      private String _src;
      private String _uri;
      private Vector _srcs = new Vector();

      public void addFileSet(FileSet var1) {
         this._srcs.add(var1);
      }

      public void setUri(String var1) {
         this._uri = var1;
      }

      public void setSrc(String var1) {
         this._src = var1;
      }

      public String getUri() {
         return this._uri;
      }

      public String getSrc() {
         return this._src;
      }

      public Enumeration getFileSets() {
         return this._srcs.elements();
      }
   }
}
