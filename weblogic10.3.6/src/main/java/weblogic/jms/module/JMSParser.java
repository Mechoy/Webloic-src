package weblogic.jms.module;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.descriptor.VersionMunger;
import weblogic.deploy.internal.DeploymentPlanDescriptorLoader;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.jms.common.JMSDebug;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class JMSParser {
   public static JMSBean createJMSDescriptor(String var0, String var1) throws ModuleException {
      if (var0 == null) {
         throw new ModuleException("Null URI specified");
      } else if (var1 == null) {
         try {
            return createJMSDescriptor((ApplicationContextInternal)null, (BasicDeploymentMBean)null, var0);
         } catch (Exception var7) {
            throw new ModuleException("Could not create the JMS descriptor", var7);
         }
      } else {
         File var2 = new File(var0);
         File var3 = new File(var1);
         DeploymentPlanDescriptorLoader var4 = new DeploymentPlanDescriptorLoader(var3);

         DeploymentPlanBean var5;
         try {
            var5 = var4.getDeploymentPlanBean();
         } catch (IOException var10) {
            throw new ModuleException(var10.getMessage(), var10);
         } catch (XMLStreamException var11) {
            throw new ModuleException(var11.getMessage(), var11);
         }

         try {
            return createJMSDescriptor(var2, var2.getParentFile(), var5, var0, var0);
         } catch (IOException var8) {
            throw new ModuleException(var8.getMessage(), var8);
         } catch (XMLStreamException var9) {
            throw new ModuleException(var9.getMessage(), var9);
         }
      }
   }

   public static JMSBean createJMSDescriptor(String var0) throws ModuleException {
      return createJMSDescriptor(var0, (String)null);
   }

   static JMSBean createJMSDescriptor(ApplicationContextInternal var0, BasicDeploymentMBean var1, String var2) throws ModuleException {
      DeploymentPlanBean var3 = null;
      String var4 = null;
      AbstractDescriptorLoader2 var5 = null;
      File var6 = null;
      if (var2 == null) {
         throw new ModuleException("Null URI specified");
      } else {
         if (var1 != null) {
            if (var1 instanceof AppDeploymentMBean) {
               if (var0.isEar()) {
                  File[] var7 = var0.getEar().getModuleRoots(var2);
                  if (var7.length <= 0) {
                     throw new ModuleException("Could not find the JMS module file \"" + var2 + "\" in application \"" + var0.getApplicationId() + "\"");
                  }
               }

               AppDeploymentMBean var11 = (AppDeploymentMBean)var1;
               var3 = var11.getDeploymentPlanDescriptor();
               if (var11.getPlanDir() != null) {
                  var6 = new File(var11.getLocalPlanDir());
               }

               var4 = var11.getName();
               if (var5 == null) {
                  if (JMSDebug.JMSModule.isDebugEnabled()) {
                     JMSDebug.JMSModule.debug("Creating jms descriptor from deployment at " + getCanonicalPath(var0, var2));
                  }

                  String var8 = getModuleName(var11, var2);
                  String var9 = getModuleUri(var11, var2);
                  var5 = createDescriptorLoader(new File(getCanonicalPath(var0, var2)), var6, var3, var8, var9);
               }
            } else {
               SystemResourceMBean var12 = (SystemResourceMBean)var1;
               if (var12 == null) {
                  throw new ModuleException("Application was neither a system resource nor a deployment");
               }

               var4 = var12.getName();
               if (var5 == null) {
                  if (JMSDebug.JMSModule.isDebugEnabled()) {
                     JMSDebug.JMSModule.debug("Creating jms descriptor from system resource at " + getCanonicalPath(var0, var2));
                  }

                  var5 = createDescriptorLoader((File)(new File(getCanonicalPath(var0, var2))), (File)null, (DeploymentPlanBean)null, var4, var2);
               }
            }
         } else if (var5 == null) {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("Creating jms descriptor offline for " + var2);
            }

            var5 = createDescriptorLoader((File)(new File(var2)), (File)null, (DeploymentPlanBean)null, (String)null, var2);
         }

         try {
            return getJMSBean(var5);
         } catch (Exception var10) {
            throw new ModuleException("Could not create the JMS descriptor", var10);
         }
      }
   }

   public AbstractDescriptorLoader2 getJmsDescriptorLoader(File var1, String var2) throws IOException, XMLStreamException {
      return createDescriptorLoader((File)var1, (File)null, (DeploymentPlanBean)null, (String)null, var2);
   }

   public AbstractDescriptorLoader2 getJmsDescriptorLoader(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) throws IOException, XMLStreamException {
      return createDescriptorLoader(var1, var2, var3, var4, var5);
   }

   private static String getModuleUri(AppDeploymentMBean var0, String var1) {
      return var0.getSourcePath() != null && var0.getSourcePath().endsWith(".xml") ? "." : var1;
   }

   private static String getModuleName(AppDeploymentMBean var0, String var1) {
      return var0.getSourcePath() != null ? (new File(var0.getSourcePath())).getName() : var1;
   }

   private static JMSBean createJMSDescriptor(VirtualJarFile var0, String var1, String var2, String var3) throws ModuleException {
      File var4 = null;
      DeploymentPlanBean var5 = null;
      ApplicationContextInternal var6 = null;
      AbstractDescriptorLoader2 var7 = null;
      if (var1 != null) {
         var6 = ApplicationAccess.getApplicationAccess().getApplicationContext(var1);
      }

      if (var6 != null) {
         AppDeploymentMBean var8 = var6.getAppDeploymentMBean();
         var5 = var8.getDeploymentPlanDescriptor();
         if (var8.getPlanDir() != null) {
            var4 = new File(var8.getLocalPlanDir());
         }
      }

      var7 = createDescriptorLoader(var0, var4, var5, var2, var3);

      try {
         return getJMSBean(var7);
      } catch (Exception var9) {
         throw new ModuleException("Could not create the JMS descriptor", var9);
      }
   }

   public static JMSBean createJMSDescriptor(InputStream var0, DescriptorManager var1, List var2, boolean var3) throws ModuleException {
      AbstractDescriptorLoader2 var4 = createDescriptorLoader(var0, var1, var2, var3);

      try {
         return getJMSBean(var4);
      } catch (Exception var6) {
         throw new ModuleException("Could not create the JMS descriptor", var6);
      }
   }

   public static JMSBean createJMSDescriptor(DescriptorManager var0, GenericClassLoader var1, File var2, DeploymentPlanBean var3, String var4, String var5) throws IOException, XMLStreamException {
      AbstractDescriptorLoader2 var6 = createDescriptorLoader(var0, var1, var2, var3, var4, var5);
      return getJMSBean(var6);
   }

   private static JMSBean createJMSDescriptor(File var0, File var1, DeploymentPlanBean var2, String var3, String var4) throws IOException, XMLStreamException {
      AbstractDescriptorLoader2 var5 = createDescriptorLoader(var0, var1, var2, var3, var4);
      return getJMSBean(var5);
   }

   private static String getCanonicalPath(ApplicationContextInternal var0, String var1) {
      if (var0.isEar()) {
         File var2 = var0.getEar().getModuleRoots(var1)[0];
         return var2.getAbsolutePath().replace(File.separatorChar, '/');
      } else {
         return var0.getStagingPath();
      }
   }

   private static JMSBean getJMSBean(AbstractDescriptorLoader2 var0) throws IOException, XMLStreamException {
      return (JMSBean)var0.loadDescriptorBean();
   }

   private static AbstractDescriptorLoader2 createDescriptorLoader(DescriptorManager var0, GenericClassLoader var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      return new AbstractDescriptorLoader2(var0, var1, var2, var3, var4, var5) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            return JMSParser.createVersionMunger(var1, this);
         }
      };
   }

   private static AbstractDescriptorLoader2 createDescriptorLoader(File var0, File var1, DeploymentPlanBean var2, String var3, String var4) {
      return new AbstractDescriptorLoader2(var0, var1, var2, var3, var4) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            return JMSParser.createVersionMunger(var1, this);
         }
      };
   }

   private static AbstractDescriptorLoader2 createDescriptorLoader(VirtualJarFile var0, File var1, DeploymentPlanBean var2, String var3, String var4) {
      return new AbstractDescriptorLoader2(var0, var1, var2, var3, var4) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            return JMSParser.createVersionMunger(var1, this);
         }
      };
   }

   private static AbstractDescriptorLoader2 createDescriptorLoader(InputStream var0, DescriptorManager var1, List var2, boolean var3) {
      return new AbstractDescriptorLoader2(var0, var1, var2, var3) {
         protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
            return JMSParser.createVersionMunger(var1, this);
         }
      };
   }

   private static VersionMunger createVersionMunger(InputStream var0, AbstractDescriptorLoader2 var1) throws XMLStreamException {
      String var2 = "weblogic.j2ee.descriptor.wl.JMSBeanImpl$SchemaHelper2";
      return new VersionMunger(var0, var1, var2, "http://xmlns.oracle.com/weblogic/weblogic-jms");
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 1) {
         usage();
      }

      String var1 = var0[0];
      String var2 = null;
      if (var0.length == 2) {
         var2 = var0[1];
      }

      Object var3 = null;
      Object var4 = null;
      File var5 = null;
      JMSBean var6 = null;

      try {
         var5 = new File(var1);
         if (var5.getName().endsWith(".ear")) {
            JarFile var7 = new JarFile(var1);
            VirtualJarFile var8 = VirtualJarFactory.createVirtualJar(var7);
            if (var2 == null) {
               Iterator var9 = var8.entries();

               while(var9.hasNext()) {
                  ZipEntry var10 = (ZipEntry)var9.next();
                  if (var10.getName().endsWith("-jms.xml")) {
                     var2 = var10.getName();
                     System.out.println("\n\n... getting JMSBean from EAR for uri " + var2 + ":\n\n");

                     try {
                        var6 = createJMSDescriptor(var8, (String)var3, (String)var4, var2);
                        if (var6 != null) {
                           DescriptorUtils.writeAsXML((DescriptorBean)var6);
                        }
                     } catch (Throwable var15) {
                        logValidationError(var15);
                     }
                  }
               }
            } else {
               System.out.println("\n\n... getting JMSBean from EAR for uri " + var2 + ":\n\n");

               try {
                  var6 = createJMSDescriptor(var8, (String)var3, (String)var4, var2);
                  if (var6 != null) {
                     DescriptorUtils.writeAsXML((DescriptorBean)var6);
                  }
               } catch (Throwable var14) {
                  logValidationError(var14);
               }
            }
         } else if (var5.getPath().endsWith("-jms.xml")) {
            if (var0.length == 1) {
               System.out.println("\n\n... getting JMSBean from JMSMD:\n\n");

               try {
                  var6 = createJMSDescriptor(var0[0]);
                  if (var6 != null) {
                     DescriptorUtils.writeAsXML((DescriptorBean)var6);
                  }
               } catch (Throwable var13) {
                  logValidationError(var13);
               }
            } else if (var0[1].endsWith("plan.xml")) {
               System.out.println("\n\n... plan:");

               try {
                  var6 = createJMSDescriptor(var0[0], var0[1]);
                  if (var6 != null) {
                     DescriptorUtils.writeAsXML((DescriptorBean)var6);
                  }
               } catch (Throwable var12) {
                  logValidationError(var12);
               }
            }
         } else {
            usage();
         }
      } catch (Throwable var16) {
         logValidationError(var16);
      }

   }

   private static void logValidationError(Throwable var0) {
      System.out.println(var0.toString());
      var0.printStackTrace();
   }

   private static void usage() {
      System.err.println("usage: java weblogic.jms.module.JMSParser <JMS Module descriptor | J2EE Application Archive>");
      System.err.println("\n\n example:\n java weblogic.jms.module.JMSParser my-jms.xml");
      System.err.println("\n\n example:\n java weblogic.jms.module.JMSParser myapp.ear <Any JMS module URI inside this EAR>");
      System.exit(0);
   }
}
