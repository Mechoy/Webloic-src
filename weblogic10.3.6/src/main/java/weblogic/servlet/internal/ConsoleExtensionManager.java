package weblogic.servlet.internal;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.Home;
import weblogic.management.DomainDir;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;

public class ConsoleExtensionManager {
   private static final ConsoleExtensionManager INSTANCE = new ConsoleExtensionManager();
   private static final XMLInputFactory XMLINFACTORY = XMLInputFactory.newInstance();
   private static final String extConfigFileName = "ConsoleExtensions.xml";
   static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static ConsoleExtensionManager getInstance() {
      return INSTANCE;
   }

   protected ConsoleExtensionManager() {
   }

   public ExtensionDef[] findExtensions() {
      HashMap var1 = new HashMap();
      this.includeDirectoryContents(this.getHomeExtensionDir(), var1, false);
      this.includeDirectoryContents(this.getHomeAutodeployExtensionDir(), var1, true);
      this.includeDirectoryContents(this.getDomainExtensionDir(), var1, true);
      File var2 = this.getHomeExtensionConfigFile();
      if (var2.exists()) {
         this.applyExtensionConfigFile(var2, var1);
      }

      var2 = this.getDomainExtensionConfigFile();
      if (var2.exists()) {
         this.applyExtensionConfigFile(var2, var1);
      }

      ExtensionDef[] var3 = new ExtensionDef[var1.size()];
      var1.values().toArray(var3);
      return var3;
   }

   public boolean shouldIncludeExtension(String var1, ExtensionDef var2) {
      if (var2 == null) {
         return false;
      } else {
         if (var1.endsWith(".war") || var1.endsWith(".war")) {
            var1 = var1.substring(0, var1.length() - 4);
         }

         if (!var1.equals(var2.getModuleName())) {
            return false;
         } else if (!var2.isEnabled()) {
            return false;
         } else {
            String[] var3 = var2.getRequiredServices();
            if (var3 != null) {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  if (!this.isServiceEnabled(var3[var4])) {
                     return false;
                  }
               }
            }

            File var5 = new File(var2.getPath());
            return var5.exists();
         }
      }
   }

   public void release() {
   }

   protected boolean isServiceEnabled(String var1) {
      ServerRuntimeMBean var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getServerRuntime();
      return var2.isServiceAvailable(var1);
   }

   private void applyExtensionConfigFile(File var1, Map<String, ExtensionDef> var2) {
      XMLStreamReader var3 = null;

      try {
         var3 = XMLINFACTORY.createXMLStreamReader(new FileReader(var1.getPath()));

         while(var3.hasNext()) {
            switch (var3.next()) {
               case 1:
                  if ("extensions".equals(var3.getLocalName())) {
                     this.parseExtensions(var3, var2);
                  }
            }
         }
      } catch (Exception var6) {
      }

      try {
         var3.close();
      } catch (XMLStreamException var5) {
      }

   }

   private void parseExtensions(XMLStreamReader var1, Map<String, ExtensionDef> var2) throws XMLStreamException {
      while(var1.hasNext()) {
         switch (var1.next()) {
            case 1:
               if (!"extension-def".equals(var1.getLocalName())) {
                  break;
               }

               String var3 = var1.getAttributeValue((String)null, "module");
               String var4 = var1.getAttributeValue((String)null, "path");
               String var5 = var1.getAttributeValue((String)null, "enabled");
               String var6 = var1.getAttributeValue((String)null, "requiredServices");
               String var7 = var1.getAttributeValue((String)null, "prerequisites");
               if (isEmptyString(var3)) {
                  var3 = "console";
               }

               boolean var8 = true;
               if (!isEmptyString(var5)) {
                  var8 = "true".equals(var5);
               }

               if (isEmptyString(var4)) {
                  break;
               }

               String var9 = this.computeExtensionDefName(var3, var4);
               ExtensionDef var10 = (ExtensionDef)var2.get(var9);
               if (var10 != null) {
                  var10.setEnabled(var8);
                  var10.setPrerequisites(var7);
                  var10.setRequiredServices(var6);
               } else {
                  var10 = new ExtensionDef(var3, var4, var8, var6, var7);
               }

               var2.put(var10.getName(), var10);
               break;
            case 2:
               if ("extensions".equals(var1.getLocalName())) {
                  return;
               }
         }
      }

   }

   protected File getDomainExtensionDir() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain();
      String var2 = var1.getConsoleExtensionDirectory();
      if (var2 != null) {
         File var3 = new File(var1.getRootDirectory(), var2);
         if (var3.isDirectory()) {
            return var3;
         }
      }

      return null;
   }

   protected File getHomeExtensionDir() {
      File var1 = new File(Home.getPath());
      return var1.getParentFile() != null ? new File(var1.getParentFile(), "server" + File.separator + "lib" + File.separator + "console-ext") : null;
   }

   protected File getHomeAutodeployExtensionDir() {
      File var1 = new File(Home.getPath());
      return var1.getParentFile() != null ? new File(var1.getParentFile(), "server" + File.separator + "lib" + File.separator + "console-ext" + File.separator + "autodeploy") : null;
   }

   protected File getDomainExtensionConfigFile() {
      return new File(DomainDir.getDataDirForServer(ManagementService.getPropertyService(KERNEL_ID).getServerName()) + File.separatorChar + "console" + File.separatorChar + "ConsoleExtensions.xml");
   }

   protected File getHomeExtensionConfigFile() {
      File var1 = new File(Home.getPath());
      return var1.getParentFile() != null ? new File(var1.getParentFile(), "server" + File.separator + "lib" + File.separator + "console-ext" + File.separator + "ConsoleExtensions.xml") : null;
   }

   private void includeDirectoryContents(File var1, Map<String, ExtensionDef> var2, boolean var3) {
      FileFilter var4 = FileUtils.makeExtensionFilter(new String[]{".jar", ".war"});
      if (var1 != null && var1.exists() && var1.isDirectory()) {
         File[] var5 = var1.listFiles(var4);

         for(int var6 = 0; var5 != null && var6 < var5.length; ++var6) {
            String var7 = this.computeExtensionName(var5[var6].getAbsolutePath());
            String var8 = "console";
            if (var7.toLowerCase().indexOf("help") > 0) {
               var8 = "consolehelp";
            }

            ExtensionDef var9 = new ExtensionDef(var8, var5[var6].getAbsolutePath(), var3, (String)null, (String)null);
            var9.setDiscovered(true);
            var2.put(var9.getName(), var9);
         }
      }

   }

   protected String computeExtensionDefName(String var1, String var2) {
      return var1 + "." + this.computeExtensionName(var2);
   }

   protected String computeExtensionName(String var1) {
      int var2 = var1.lastIndexOf(File.separator);
      if (var2 > 0) {
         var1 = var1.substring(var2 + 1);
      }

      var2 = var1.lastIndexOf(".");
      if (var2 > 0) {
         var1 = var1.substring(0, var2);
      }

      return var1;
   }

   private static boolean isEmptyString(String var0) {
      return var0 == null || var0.length() == 0 || isWhitespace(var0);
   }

   private static boolean isWhitespace(String var0) {
      for(int var1 = 0; var1 < var0.length(); ++var1) {
         if (!Character.isWhitespace(var0.charAt(var1))) {
            return false;
         }
      }

      return true;
   }

   public class ExtensionDef {
      private String name = null;
      private String extensionName = null;
      private String moduleName;
      private String path;
      private boolean enabled;
      private boolean discovered;
      private String[] prerequisites;
      private String[] requiredServices;

      ExtensionDef(String var2, String var3, boolean var4, String var5, String var6) {
         this.extensionName = ConsoleExtensionManager.this.computeExtensionName(var3);
         this.moduleName = var2;
         this.name = this.moduleName + "." + this.extensionName;
         this.path = var3;
         this.setEnabled(var4);
         this.setRequiredServices(var5);
         this.setPrerequisites(var6);
         this.setDiscovered(false);
      }

      public boolean isEnabled() {
         return this.enabled;
      }

      public boolean isDiscovered() {
         return this.discovered;
      }

      public String getName() {
         return this.name;
      }

      public String getExtensionName() {
         return this.extensionName;
      }

      public String getModuleName() {
         return this.moduleName;
      }

      public String getPath() {
         return this.path;
      }

      public File getFile() {
         return new File(this.getPath());
      }

      public void setEnabled(boolean var1) {
         this.enabled = var1;
      }

      public void setDiscovered(boolean var1) {
         this.discovered = var1;
      }

      public void setRequiredServices(String var1) {
         if (var1 != null) {
            this.requiredServices = var1.split(",");
         } else {
            this.requiredServices = null;
         }

      }

      public String[] getRequiredServices() {
         return this.requiredServices == null ? new String[0] : this.requiredServices;
      }

      public String getRequiredServiceList() {
         return this.requiredServices != null ? this.getStringArrayString(this.requiredServices) : null;
      }

      public void setPrerequisites(String var1) {
         if (var1 != null) {
            this.prerequisites = var1.split(",");
         } else {
            this.prerequisites = null;
         }

      }

      public String[] getPrerequisites() {
         return this.prerequisites == null ? new String[0] : this.prerequisites;
      }

      public String getPrerequisiteList() {
         return this.prerequisites != null ? this.getStringArrayString(this.prerequisites) : null;
      }

      private String getStringArrayString(String[] var1) {
         StringBuffer var2 = new StringBuffer();
         if (var1 != null) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               var2.append(var1[var3]);
               if (var3 + 1 != var1.length) {
                  var2.append(", ");
               }
            }
         }

         return var2.toString();
      }

      public String toString() {
         return super.toString() + ",\n        name=" + this.name + ",\n        extensionName=" + this.extensionName + ",\n        moduleName=" + this.moduleName + ",\n        path=" + this.path + ",\n        enabled=" + this.enabled + ",\n        discovered=" + this.discovered + ",\n        prerequisites=" + this.getStringArrayString(this.prerequisites) + ",\n        requiredServices=" + this.getStringArrayString(this.requiredServices);
      }
   }
}
