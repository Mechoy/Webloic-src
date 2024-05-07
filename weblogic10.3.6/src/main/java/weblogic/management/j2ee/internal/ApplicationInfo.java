package weblogic.management.j2ee.internal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.utils.Debug;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

class ApplicationInfo {
   public static final int J2EE_APP = 1;
   public static final int J2EE_WEB_MODULE = 2;
   public static final int J2EE_EJB_MODULE = 3;
   public static final int J2EE_CONNECTOR_MODULE = 4;
   public static final int J2EE_APP_CLIENT_MODULE = 5;
   private static final String APP_DD_URI = "META-INF/application.xml";
   private static final String WEB_DD_URI = "WEB-INF/web.xml";
   private static final String EJB_DD_URI = "META-INF/ejb-jar.xml";
   private static final String RAR_DD_URI = "META-INF/ra.xml";
   private static final String WLS_APP_DD_URI = "META-INF/weblogic-application.xml";
   private static final String WLS_WEB_DD_URI = "WEB-INF/weblogic.xml";
   private static final String WLS_EJB_DD_URI = "META-INF/weblogic-ejb-jar.xml";
   private static final String WLS_RAR_DD_URI = "META-INF/weblogic-ra.xml";
   private final ObjectName name;
   private final int type;
   private final MBeanServerConnection domainConnection;
   private final MBeanServerConnection editConnection;
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJ2EEManagement");
   private String appSource = null;
   private String split_build_dir = null;

   public ApplicationInfo(ObjectName var1, int var2) {
      this.name = var1;
      this.type = var2;
      this.domainConnection = MBeanServerConnectionProvider.getDomainMBeanServerConnection();
      this.editConnection = MBeanServerConnectionProvider.getEditMBeanServerConnection();
   }

   public String getDescriptor() {
      switch (this.type) {
         case 1:
            return this.getApplicationDD();
         case 2:
            return this.getWebModuleDD();
         case 3:
            return this.getEJBModuleDD();
         case 4:
            return this.getConnectorModuleDD();
         default:
            throw new AssertionError("In valid request");
      }
   }

   public String getWebLogicDescriptor() {
      switch (this.type) {
         case 1:
            return this.getWebLogicApplicationDD();
         case 2:
            return this.getWebLogicWebModuleDD();
         case 3:
            return this.getWebLogicEJBModuleDD();
         case 4:
            return this.getWebLogicConnectorModuleDD();
         default:
            throw new AssertionError("In valid request");
      }
   }

   public boolean isEar() {
      return this.isEar(this.getApplicationSource());
   }

   public boolean isParentEar() {
      ObjectName var1 = this.getParentObjectName();
      return this.isEar(this.getApplicationSource(var1));
   }

   private String getApplicationLevelDD(String var1) {
      InputStream var2 = null;
      VirtualJarFile var3 = null;

      String var6;
      try {
         String var4 = this.getApplicationSource();
         var3 = VirtualJarFactory.createVirtualJar(new File(var4));
         ZipEntry var5 = var3.getEntry(var1);
         if (var5 != null) {
            var2 = var3.getInputStream(var5);
            var6 = this.makeDescriptor(var2);
            return var6;
         }

         var6 = null;
      } catch (IOException var16) {
         throw new AssertionError(" Unable to read Application Deployment Descriptor: " + var1);
      } finally {
         try {
            if (var2 != null) {
               var2.close();
            }

            if (var3 != null) {
               var3.close();
            }
         } catch (IOException var15) {
         }

      }

      return var6;
   }

   private String getApplicationDD() {
      return this.getApplicationLevelDD("META-INF/application.xml");
   }

   private String getWebLogicApplicationDD() {
      return this.getApplicationLevelDD("META-INF/weblogic-application.xml");
   }

   private String getWebModuleDD() {
      try {
         ObjectName var1 = this.getParentObjectName();
         String var2 = this.getApplicationSource(var1);
         String var3 = (String)this.domainConnection.getAttribute(this.name, "ModuleURI");
         return this.getModuleDescriptor(var2, var3, "WEB-INF/web.xml");
      } catch (Throwable var4) {
         throw new AssertionError("Unable to get information about Web Module");
      }
   }

   private String getWebLogicWebModuleDD() {
      return this.getModuleDD("WEB-INF/weblogic.xml");
   }

   private String getEJBModuleLevelDD(String var1) {
      try {
         ObjectName var2 = this.getParentObjectName();
         String var3 = this.getApplicationSource(var2);
         String var4 = (String)this.domainConnection.getAttribute(this.name, "ModuleId");
         String var5 = this.getModuleDescriptor(var3, var4, var1);
         if (var5 == null) {
            var5 = this.getModuleDescriptor(this.appSource, var4, var1);
         }

         if (var5 == null) {
            var5 = "";
         }

         return var5;
      } catch (Throwable var6) {
         throw new AssertionError("Unabled get information about the Module: " + var1);
      }
   }

   private String getEJBModuleDD() {
      return this.getEJBModuleLevelDD("META-INF/ejb-jar.xml");
   }

   private String getWebLogicEJBModuleDD() {
      return this.getEJBModuleLevelDD("META-INF/weblogic-ejb-jar.xml");
   }

   private String getModuleDD(String var1) {
      try {
         ObjectName var2 = this.getParentObjectName();
         String var3 = this.getApplicationSource(var2);
         String var4 = (String)this.domainConnection.getAttribute(this.name, "ModuleId");
         return this.getModuleDescriptor(var3, var4, var1);
      } catch (Throwable var5) {
         throw new AssertionError("Unabled get information about the Module: " + var1);
      }
   }

   private String getConnectorModuleDD() {
      return this.getModuleDD("META-INF/ra.xml");
   }

   private String getWebLogicConnectorModuleDD() {
      return this.getModuleDD("META-INF/weblogic-ra.xml");
   }

   private String getApplicationSource(ObjectName var1) {
      if (this.type == 3 && this.split_build_dir != null) {
         return this.split_build_dir;
      } else if (this.appSource != null) {
         return this.appSource;
      } else {
         String var2 = null;

         try {
            var2 = (String)this.domainConnection.getAttribute(var1, "ApplicationName");
            String var3 = var1.getDomain() + ":Name=" + var2 + ",Type=AppDeployment";

            try {
               this.appSource = (String)this.editConnection.getAttribute(new ObjectName(var3), "SourcePath");
            } catch (InstanceNotFoundException var5) {
               this.appSource = InternalAppDataCacheService.getSourceLocation(var2);
            }

            this.appSource = this.getCorrectPathIfSplitDir(this.appSource);
            return this.appSource;
         } catch (Throwable var6) {
            throw new AssertionError("Failed to get information about the application" + var6);
         }
      }
   }

   private String getCorrectPathIfSplitDir(String var1) {
      FileInputStream var2 = null;
      File var3 = new File(var1, ".beabuild.txt");
      if (!var3.exists()) {
         return var1;
      } else {
         try {
            String var5;
            try {
               var2 = new FileInputStream(var3);
               Properties var4 = new Properties();
               var4.load(var2);
               var5 = var4.getProperty("bea.srcdir");
               if (var5 != null) {
                  this.split_build_dir = var1;
                  String var6 = var5;
                  return var6;
               }
            } catch (Throwable var17) {
               var5 = var1;
               return var5;
            }
         } finally {
            try {
               if (var2 != null) {
                  var2.close();
               }
            } catch (IOException var16) {
            }

         }

         return var1;
      }
   }

   private String getApplicationSource() {
      return this.getApplicationSource(this.name);
   }

   private boolean isEar(String var1) {
      if (var1 == null) {
         return false;
      } else if (var1.endsWith("ear")) {
         return true;
      } else {
         VirtualJarFile var2 = null;

         boolean var4;
         try {
            var2 = VirtualJarFactory.createVirtualJar(new File(var1));
            if (var2.getEntry("META-INF/application.xml") == null) {
               return false;
            }

            boolean var3 = true;
            return var3;
         } catch (IOException var15) {
            var4 = false;
         } finally {
            try {
               if (var2 != null) {
                  var2.close();
               }
            } catch (IOException var14) {
            }

         }

         return var4;
      }
   }

   private ObjectName getParentObjectName() {
      try {
         return (ObjectName)this.domainConnection.getAttribute(this.name, "Parent");
      } catch (InstanceNotFoundException var2) {
         throw new Error("Failed to access information about parent ObjectName", var2);
      } catch (ReflectionException var3) {
         throw new Error("Failed to access information about parent ObjectName", var3);
      } catch (IOException var4) {
         throw new Error("Failed to access information about parent ObjectName", var4);
      } catch (MBeanException var5) {
         throw new Error("Failed to access information about parent ObjectName", var5);
      } catch (AttributeNotFoundException var6) {
         throw new Error("Failed to access information about parent ObjectName", var6);
      }
   }

   private String getModuleDescriptor(String var1, String var2, String var3) {
      if (debug.isDebugEnabled()) {
         Debug.say("Application source: " + var1 + "  With uri: " + var2 + "  and deployment descriptor location: " + var3);
      }

      ZipEntry var4 = null;
      InputStream var5 = null;
      VirtualJarFile var6 = null;

      String var9;
      try {
         ZipEntry var8;
         try {
            var6 = VirtualJarFactory.createVirtualJar(new File(var1));
            Iterator var7;
            if (debug.isDebugEnabled()) {
               for(var7 = var6.entries(); var7.hasNext(); var8 = (ZipEntry)var7.next()) {
               }
            }

            var4 = var6.getEntry(var3);
            String var24;
            if (var4 != null) {
               var5 = var6.getInputStream(var4);
               var24 = this.makeDescriptor(var5);
               return var24;
            }

            var4 = var6.getEntry(var2);
            if (var4 == null) {
               var7 = null;
               return var7;
            }

            var24 = var2 + "/" + var3;
            var8 = var6.getEntry(var24);
            if (var8 == null) {
               var5 = var6.getInputStream(var4);
               var9 = this.makeDescriptor(var5, var3);
               return var9;
            }

            var5 = var6.getInputStream(var8);
            var9 = this.makeDescriptor(var5);
         } catch (IOException var22) {
            var8 = null;
            return var8;
         }
      } finally {
         try {
            if (var5 != null) {
               var5.close();
            }

            if (var6 != null) {
               var6.close();
            }
         } catch (IOException var21) {
         }

      }

      return var9;
   }

   private String makeDescriptor(InputStream var1) {
      BufferedReader var2 = new BufferedReader(new InputStreamReader(var1));

      try {
         String var3;
         String var4;
         for(var3 = new String(); (var4 = var2.readLine()) != null; var3 = var3 + var4 + System.getProperty("line.separator")) {
         }

         var2.close();
         String var5 = var3;
         return var5;
      } catch (IOException var14) {
         throw new AssertionError(" Unable to read Application Deployment Descriptor");
      } finally {
         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (IOException var13) {
         }

      }
   }

   private String makeDescriptor(InputStream var1, String var2) {
      ByteArrayOutputStream var3 = null;
      ZipInputStream var4 = null;
      String var5 = new String();

      String var7;
      try {
         var4 = new ZipInputStream(var1);

         for(ZipEntry var6 = var4.getNextEntry(); var6 != null; var6 = var4.getNextEntry()) {
            if (var6.getName().equals(var2)) {
               var3 = new ByteArrayOutputStream();
               byte[] var19 = new byte[1024];

               int var8;
               while((var8 = var4.read(var19)) > 0) {
                  var3.write(var19, 0, var8);
               }

               var3.flush();
               var5 = var3.toString();
               return var5;
            }
         }

         return var5;
      } catch (IOException var17) {
         var7 = var5;
      } finally {
         try {
            if (var3 != null) {
               var3.close();
            }

            if (var4 != null) {
               var4.close();
            }
         } catch (IOException var16) {
         }

      }

      return var7;
   }
}
