package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.ApplicationFileManager;
import weblogic.application.Deployment;
import weblogic.application.io.Ear;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.j2ee.descriptor.WebBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.utils.Debug;
import weblogic.utils.FileUtils;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.application.WarDetector;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFile;

public final class EarUtils {
   public static final Deployment.AdminModeCallback noopAdminModeCallback = new Deployment.AdminModeCallback() {
      public void completed() {
      }

      public void waitForCompletion(long var1) {
      }
   };
   static final String EXPLODED_EAR_SUFFIX = ".ear";
   private static final DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugAppContainer");

   private EarUtils() {
   }

   public static boolean isDebugOn() {
      return debugLogger.isDebugEnabled();
   }

   public static void debug(String var0) {
      debugLogger.debug(addClassName(var0));
   }

   static String addClassName(String var0) {
      StackTraceElement[] var1 = (new Exception()).getStackTrace();
      String var2 = var1[2].getClassName();
      int var3 = var2.lastIndexOf(".");
      if (var3 != -1) {
         var2 = var2.substring(var3 + 1);
      }

      return "[" + var2 + "] " + var0;
   }

   public static void handleUnsetContextRoot(String var0, String var1, ApplicationBean var2, WeblogicWebAppBean var3) {
      if (var2 != null) {
         ModuleBean[] var4 = var2.getModules();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            WebBean var6 = var4[var5].getWeb();
            if (var6 != null && var6.getWebUri().equals(var0)) {
               if (var6.getContextRoot() != null && var6.getContextRoot().startsWith("__BEA_WLS_INTERNAL_UNSET_CONTEXT_ROOT")) {
                  String var7 = null;
                  if (var1 != null) {
                     var7 = var1;
                  } else if (var3 != null && var3.getContextRoots().length > 0) {
                     var7 = var3.getContextRoots()[0];
                  } else if (var0 != null) {
                     var7 = WarDetector.instance.stem(var0);
                  }

                  if (var7 != null) {
                     var6.setContextRoot(var7);
                  }
               }

               return;
            }
         }

      }
   }

   public static String fixAppContextRoot(String var0) {
      return var0 != null && var0.startsWith("__BEA_WLS_INTERNAL_UNSET_CONTEXT_ROOT") ? null : var0;
   }

   public static boolean isEar(File var0) {
      if (!var0.isDirectory()) {
         return var0.getName().endsWith(".ear");
      } else {
         return (new File(var0, "META-INF/application.xml")).exists() || isSplitDir(var0) || var0.getName().endsWith(".ear");
      }
   }

   private static ApplicationBean createApplicationBean() {
      return (ApplicationBean)(new DescriptorManager()).createDescriptorRoot(ApplicationBean.class).getRootBean();
   }

   private static File extractAndGetTempRoot(VirtualJarFile var0) throws IOException {
      String var1 = var0.getName();
      int var2 = var1.lastIndexOf(System.getProperty("file.separator"));
      if (var2 > -1) {
         var1.substring(var2);
      }

      File var3 = FileUtils.createTempDir("_earscanner_tmp", (File)null);
      if (var3.exists()) {
         FileUtils.remove(var3);
      }

      var3.mkdirs();
      JarFileUtils.extract(var0, var3);
      return var3;
   }

   public static void linkURI(Ear var0, ApplicationFileManager var1, String var2, File var3) throws IOException {
      var0.registerLink(var2, var3);
      var1.registerLink(var2, var3.getAbsolutePath());
   }

   public static String reallyGetModuleURI(ModuleBean var0) {
      return var0.getWeb() != null ? var0.getWeb().getWebUri() : getModuleURI(var0);
   }

   public static String getModuleURI(ModuleBean var0) {
      if (var0.getConnector() != null) {
         return var0.getConnector();
      } else if (var0.getEjb() != null) {
         return var0.getEjb();
      } else if (var0.getJava() != null) {
         return var0.getJava();
      } else if (var0.getWeb() != null) {
         return getContextRootName(var0);
      } else if (var0.getAltDd() != null) {
         return var0.getAltDd();
      } else {
         throw new AssertionError("ModuleBean contains no module URI");
      }
   }

   public static void informDescriptor(DescriptorBean var0) {
      try {
         (new EditableDescriptorManager()).writeDescriptorAsXML(var0.getDescriptor(), System.out);
      } catch (Exception var2) {
         Debug.say("Failed to write descriptor to stdout because of " + var2);
      }

   }

   public static void handleParsingError(Throwable var0, String var1) throws ToolFailureException {
      throw new ToolFailureException(J2EELogger.logAppcErrorParsingEARDescriptorsLoggable(var1, StackTraceUtils.throwable2StackTrace(var0)).getMessage(), var0);
   }

   public static File getSplitDirProperties(File var0) {
      return new File(var0, ".beabuild.txt");
   }

   public static boolean isSplitDir(File var0) {
      return getSplitDirProperties(var0).exists();
   }

   public static String getContextRootName(ModuleBean var0) {
      WebBean var1 = var0.getWeb();
      if (var1 == null) {
         return null;
      } else {
         String var2 = fixAppContextRoot(var1.getContextRoot());
         return var2 != null && !"".equals(var2) ? var2 : var1.getWebUri();
      }
   }

   public static File getConfigDir(ApplicationContextInternal var0) {
      return var0.getAppDeploymentMBean().getPlanDir() == null ? null : new File(var0.getAppDeploymentMBean().getLocalPlanDir());
   }

   public static void informRoleToPrincipalsMapping(Map var0) {
      if (var0 != null) {
         Debug.say("**********************************");
         Iterator var1 = var0.entrySet().iterator();

         while(true) {
            Map.Entry var2;
            String[] var3;
            do {
               if (!var1.hasNext()) {
                  return;
               }

               var2 = (Map.Entry)var1.next();
               var3 = (String[])((String[])var2.getValue());
            } while(var3.length <= 0);

            Debug.say("Role: " + var2.getKey() + " -> principals: ");

            for(int var4 = 0; var4 < var3.length; ++var4) {
               Debug.say(var3[var4]);
            }
         }
      }
   }

   public static File[] getAppFiles(String[] var0, AppDeploymentMBean var1) throws IOException {
      String var2 = var1.getApplicationName();
      File var3 = new File(var1.getAbsoluteSourcePath());
      if (!var3.isDirectory()) {
         String var19 = DeployerRuntimeLogger.logPartialRedeployOfArchiveLoggable(var2).getMessage();
         throw new IOException(var19);
      } else if (!isEar(var3)) {
         return new File[]{var3};
      } else {
         ArrayList var4 = new ArrayList();
         HashSet var5 = new HashSet(Arrays.asList(var0));
         VirtualJarFile var6 = null;

         try {
            ApplicationFileManager var7 = ApplicationFileManager.newInstance(var3);
            var6 = var7.getVirtualJarFile();
            String var20 = var1.getAbsolutePlanDir();
            File var9 = var20 == null ? null : new File(var20);
            Map var10 = addAppDescriptors(var1, var4, var6);
            File var11 = (File)var10.get("META-INF/application.xml");
            File var12 = (File)var10.get("META-INF/weblogic-application.xml");
            File var13 = (File)var10.get("META-INF/weblogic-extension.xml");
            ApplicationDescriptor var14 = new ApplicationDescriptor(var11, var12, var13, var9, getDeploymentPlanDescriptor(var1.getAbsolutePlanPath()), var3.getName());
            ApplicationBean var15 = var14.getApplicationDescriptor();
            if (var15 == null && var6 != null) {
               var15 = ModuleDiscovery.discoverModules(var6);
            }

            addAppURIs(var15, var5, var4, var6);
            if (!var5.isEmpty()) {
               WeblogicApplicationBean var16 = var14.getWeblogicApplicationDescriptor();
               addWlAppURIs(var16, var5, var4, var6);
            }

            if (!var5.isEmpty()) {
               StringBuffer var21 = new StringBuffer();
               Iterator var17 = var5.iterator();

               while(var17.hasNext()) {
                  var21.append(var17.next());
                  if (var17.hasNext()) {
                     var21.append(", ");
                  }
               }

               throw new IOException(J2EELogger.logUrisDidntMatchModulesLoggable(var21.toString()).getMessage());
            }
         } catch (XMLStreamException var18) {
            IOException var8 = new IOException(var18.getMessage());
            var8.initCause(var18);
            throw var8;
         }

         return (File[])((File[])var4.toArray(new File[var4.size()]));
      }
   }

   private static DeploymentPlanBean getDeploymentPlanDescriptor(String var0) {
      if (var0 == null) {
         return null;
      } else {
         File var1 = new File(var0);
         DeploymentPlanBean var2 = null;

         try {
            DeploymentPlanDescriptorLoader var3 = new DeploymentPlanDescriptorLoader(var1);
            var2 = var3.getDeploymentPlanBean();
            return var2;
         } catch (XMLStreamException var4) {
            throw new IllegalArgumentException(var4.getMessage(), var4);
         } catch (IOException var5) {
            throw new IllegalArgumentException(var5.getMessage(), var5);
         } catch (ClassCastException var6) {
            throw new IllegalArgumentException(var6.getMessage(), var6);
         }
      }
   }

   private static Map addAppDescriptors(AppDeploymentMBean var0, List var1, VirtualJarFile var2) {
      String[] var3 = new String[]{"META-INF/application.xml", "META-INF/weblogic-application.xml", "META-INF/weblogic-extension.xml"};
      HashMap var4 = new HashMap(var3.length);

      for(int var5 = 0; var5 < var3.length; ++var5) {
         File var6 = getFile(var3[var5], var2);
         if (var6 != null) {
            var4.put(var3[var5], var6);
         }
      }

      File var10 = var0.getAbsolutePlanDir() == null ? null : new File(var0.getAbsolutePlanDir());

      for(int var11 = 0; var11 < var3.length; ++var11) {
         File var7 = getExternalPlanDescriptorFile(getDeploymentPlanDescriptor(var0.getAbsolutePlanPath()), var10, var0.getApplicationName(), var3[var11]);
         if (var7 != null && var7.exists()) {
            var4.put(var3[var11], var7);
         }
      }

      String var12 = var0.getAltDescriptorPath();
      if (var12 != null && var12.trim().length() > 0) {
         var4.put("META-INF/application.xml", new File(var12));
      }

      String var13 = var0.getAltWLSDescriptorPath();
      if (var13 != null && var13.trim().length() > 0) {
         var4.put("META-INF/weblogic-application.xml", new File(var13));
      }

      Iterator var8 = var4.values().iterator();

      while(var8.hasNext()) {
         File var9 = (File)var8.next();
         if (var9.exists()) {
            var1.add(var9);
         }
      }

      return var4;
   }

   private static File getFile(String var0, VirtualJarFile var1) {
      File[] var2 = var1.getRootFiles();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         File var4 = new File(var2[var3], var0);
         if (var4.exists()) {
            return var4;
         }
      }

      return null;
   }

   private static void addAppURIs(ApplicationBean var0, Set var1, List var2, VirtualJarFile var3) {
      if (var0 != null) {
         ModuleBean[] var4 = var0.getModules();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = null;
            String var7 = null;
            File var8;
            if (var4[var5].getWeb() != null) {
               var6 = var4[var5].getWeb().getWebUri();
               var7 = fixAppContextRoot(var4[var5].getWeb().getContextRoot());
               if (var7 == null || "".equals(var7) || "/".equals(var7)) {
                  var7 = var6;
               }

               if (var1.contains(var7)) {
                  var8 = getFile(var6, var3);
                  if (var8 != null) {
                     var2.add(var8);
                  }

                  var1.remove(var7);
               }
            } else {
               if (var4[var5].getEjb() != null) {
                  var6 = var4[var5].getEjb();
               } else if (var4[var5].getConnector() != null) {
                  var6 = var4[var5].getConnector();
               } else if (var4[var5].getJava() != null) {
                  var6 = var4[var5].getJava();
               }

               if (var6 != null && var1.contains(var6)) {
                  var8 = getFile(var6, var3);
                  if (var8 != null) {
                     var2.add(var8);
                  }

                  var1.remove(var6);
               }
            }

            if (var1.isEmpty()) {
               break;
            }
         }

      }
   }

   private static void addWlAppURIs(WeblogicApplicationBean var0, Set var1, List var2, VirtualJarFile var3) {
      if (var0 != null) {
         WeblogicModuleBean[] var4 = var0.getModules();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            File var6;
            if ("JDBC".equals(var4[var5].getType())) {
               if (var1.contains(var4[var5].getName())) {
                  var1.remove(var4[var5].getName());
                  var6 = getFile(var4[var5].getPath(), var3);
                  if (var6 != null) {
                     var2.add(var6);
                  }
               }
            } else if (var1.contains(var4[var5].getPath())) {
               var1.remove(var4[var5].getPath());
               var6 = getFile(var4[var5].getPath(), var3);
               if (var6 != null) {
                  var2.add(var6);
               }
            }
         }

      }
   }

   private static File getExternalPlanDescriptorFile(DeploymentPlanBean var0, File var1, String var2, String var3) {
      if (var0 != null && var1 != null) {
         ModuleDescriptorBean var4 = var0.findModuleDescriptor(var2, var3);
         if (var4 != null && var4.isExternal()) {
            File var5;
            if (var0.rootModule(var2)) {
               var5 = var1;
            } else {
               var5 = new File(var1, var2);
            }

            return new File(var5, var4.getUri());
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static String toModuleId(ApplicationContextInternal var0, String var1) {
      if (var1 == null) {
         return var1;
      } else {
         ApplicationBean var2 = var0.getApplicationDD();
         if (var2 == null) {
            return var1;
         } else {
            ModuleBean[] var3 = var2.getModules();
            if (var3 == null) {
               return var1;
            } else {
               Map var4 = var0.getModuleURItoIdMap();
               String var5 = var4 != null ? (String)var4.get(var1) : null;
               return var5 != null ? var5 : var1;
            }
         }
      }
   }

   public static String[] toModuleIds(ApplicationContextInternal var0, String[] var1) {
      if (var1 != null && var1.length != 0) {
         ApplicationBean var2 = var0.getApplicationDD();
         if (var2 == null) {
            return var1;
         } else {
            ModuleBean[] var3 = var2.getModules();
            if (var3 == null) {
               return var1;
            } else {
               int var4 = var1.length;
               String[] var5 = new String[var4];

               for(int var6 = 0; var6 < var4; ++var6) {
                  var5[var6] = toModuleId(var0, var1[var6]);
               }

               return var5;
            }
         }
      } else {
         return var1;
      }
   }
}
