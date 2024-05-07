package weblogic.ejb.container.deployer;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.zip.ZipEntry;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.Deployer;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.persistence.InstalledPersistence;
import weblogic.ejb.container.persistence.PersistenceType;
import weblogic.ejb.container.persistence.spi.CMPDeployer;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.EjbDescriptorFactory;
import weblogic.j2ee.descriptor.EnterpriseBeanBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.EntityDescriptorBean;
import weblogic.j2ee.descriptor.wl.PersistenceBean;
import weblogic.j2ee.descriptor.wl.PersistenceUseBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean;
import weblogic.management.descriptors.XMLElementMBean;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.process.ProcessorFactory;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public final class EJBDescriptorMBeanUtils {
   private static EJBComplianceTextFormatter fmt = new EJBComplianceTextFormatter();
   static final String WLPersistence = "WebLogic_CMP_RDBMS";
   private static ProcessorFactory procFac = new ProcessorFactory();

   public static EjbDescriptorBean createDescriptorFromJarFile(VirtualJarFile var0) throws Exception {
      return createDescriptorFromJarFile(var0, false);
   }

   public static EjbDescriptorBean createDescriptorFromJarFile(VirtualJarFile var0, boolean var1) throws Exception {
      EjbDescriptorBean var2 = new EjbDescriptorBean();
      var2.setJarFileName(var0.getName());
      ProcessorFactory var3 = new ProcessorFactory();
      var3.setValidating(var1);
      Object var4 = null;

      try {
         var2 = EjbDescriptorFactory.createDescriptorFromJarFile(var0);
         loadWeblogicRDBMSJarMBeans(var2, var0, var3, var1);
      } catch (FileNotFoundException var7) {
         if (var1) {
            throw var7;
         }
      } catch (XMLParsingException var8) {
         var2.setParsingErrorMessage(var8.toString());
         if (var1) {
            throw var8;
         }
      } catch (XMLProcessingException var9) {
         var2.setParsingErrorMessage(var9.toString());
         if (var1) {
            throw var9;
         }
      } catch (Exception var10) {
         String var6 = StackTraceUtils.throwable2StackTrace(var10);
         var2.setParsingErrorMessage(var6);
         if (var1) {
            throw var10;
         }
      }

      return var2;
   }

   public static void loadWeblogicRDBMSJarMBeans(EjbDescriptorBean var0, VirtualJarFile var1, ProcessorFactory var2, boolean var3) throws Exception {
      HashSet var4 = new HashSet();
      Set var5 = getEJBNames(var0);
      Iterator var6 = var5.iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         PersistenceBean var8 = getPersistenceMBean(var7, var0);
         if (var8 != null && var8.getPersistenceUse() != null) {
            String var9 = var8.getPersistenceUse().getTypeIdentifier();
            if ("WebLogic_CMP_RDBMS".equals(var9)) {
               String var10 = var8.getPersistenceUse().getTypeVersion();
               String var11 = getRDBMSDescriptorFileName(var8);
               if (var11 != null && !var4.contains(var11)) {
                  loadRDBMSDescriptor(var0, var1, var7, var11, var10, var2, var3);
                  var4.add(var11);
               }
            }
         }
      }

   }

   public static Set getCMPEJBNames(EjbDescriptorBean var0) {
      HashSet var1 = new HashSet();
      if (var0.getEjbJarBean() != null) {
         EnterpriseBeansBean var2 = var0.getEjbJarBean().getEnterpriseBeans();
         if (var2 != null) {
            EntityBeanBean[] var3 = var2.getEntities();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               if ("Container".equals(var3[var4].getPersistenceType()) && var3[var4].getEjbName() != null) {
                  var1.add(var3[var4].getEjbName());
               }
            }
         }
      }

      return var1;
   }

   public static Set getEJBNames(EjbDescriptorBean var0) {
      HashSet var1 = new HashSet();
      if (var0.getEjbJarBean() != null) {
         EnterpriseBeansBean var2 = var0.getEjbJarBean().getEnterpriseBeans();
         if (var2 != null) {
            EntityBeanBean[] var3 = var2.getEntities();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               var1.add(var3[var4].getEjbName());
            }

            SessionBeanBean[] var7 = var2.getSessions();

            for(int var5 = 0; var5 < var7.length; ++var5) {
               var1.add(var7[var5].getEjbName());
            }

            MessageDrivenBeanBean[] var8 = var2.getMessageDrivens();

            for(int var6 = 0; var6 < var8.length; ++var6) {
               var1.add(var8[var6].getEjbName());
            }
         }
      }

      return var1;
   }

   public static WeblogicEnterpriseBeanBean getWeblogicEnterpriseMBean(String var0, EjbDescriptorBean var1) {
      if (var1.getWeblogicEjbJarBean() != null) {
         WeblogicEnterpriseBeanBean[] var2 = var1.getWeblogicEjbJarBean().getWeblogicEnterpriseBeans();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (var0.equals(var2[var3].getEjbName())) {
                  return var2[var3];
               }
            }
         }
      }

      return null;
   }

   public static EnterpriseBeanBean getEnterpriseMBean(String var0, EjbDescriptorBean var1) {
      if (var1.getEjbJarBean() != null) {
         EnterpriseBeansBean var2 = var1.getEjbJarBean().getEnterpriseBeans();
         if (var2 != null) {
            SessionBeanBean[] var3 = var2.getSessions();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var0.equals(var3[var4].getEjbName())) {
                  return var3[var4];
               }
            }

            EntityBeanBean[] var7 = var2.getEntities();

            for(int var5 = 0; var5 < var7.length; ++var5) {
               if (var0.equals(var7[var5].getEjbName())) {
                  return var7[var5];
               }
            }

            MessageDrivenBeanBean[] var8 = var2.getMessageDrivens();

            for(int var6 = 0; var6 < var8.length; ++var6) {
               if (var0.equals(var8[var6].getEjbName())) {
                  return var8[var6];
               }
            }
         }
      }

      return null;
   }

   static void loadRDBMSDescriptor(EjbDescriptorBean var0, VirtualJarFile var1, String var2, String var3, String var4, ProcessorFactory var5, boolean var6) throws Exception {
      ZipEntry var7 = var1.getEntry(var3);
      if (var7 == null) {
         EJBLogger.logCouldNotFindSpecifiedRDBMSDescriptorInJarFile(var2, var3, var1.getName());
         if (var6) {
            throw new FileNotFoundException(fmt.cmpFileNotFound(var2, var3, var1.getName()));
         }
      } else {
         InstalledPersistence var8 = new InstalledPersistence();
         PersistenceType var9 = var8.getInstalledType("WebLogic_CMP_RDBMS", var4);
         if (var9 != null) {
            CMPDeployer var10 = var9.getCmpDeployer();
            if (var10 instanceof Deployer) {
               Deployer var11 = (Deployer)var10;
               loadRDBMS20Descriptor(var0, var11, var1, var3, var5);
            } else if (var10 instanceof weblogic.ejb.container.cmp11.rdbms.Deployer) {
               weblogic.ejb.container.cmp11.rdbms.Deployer var12 = (weblogic.ejb.container.cmp11.rdbms.Deployer)var10;
               loadRDBMS11Descriptor(var0, var12, var1, var3, var2, var5);
            }
         }
      }

   }

   static void loadRDBMS20Descriptor(EjbDescriptorBean var0, Deployer var1, VirtualJarFile var2, String var3, ProcessorFactory var4) throws Exception {
      var1.parseXMLFile(var2, var3, var0);
   }

   static void loadRDBMS11Descriptor(EjbDescriptorBean var0, weblogic.ejb.container.cmp11.rdbms.Deployer var1, VirtualJarFile var2, String var3, String var4, ProcessorFactory var5) throws Exception {
      var1.parseXMLFile(var2, var3, var4, var5, var0);
   }

   public static PersistenceBean getPersistenceMBean(String var0, EjbDescriptorBean var1) {
      WeblogicEnterpriseBeanBean var2 = getWeblogicEnterpriseMBean(var0, var1);
      if (var2 != null && var2.getEntityDescriptor() != null) {
         EntityDescriptorBean var3 = var2.getEntityDescriptor();
         if (var3.getPersistence() != null) {
            PersistenceBean var4 = var3.getPersistence();
            if (var4 != null) {
               return var4;
            }
         }
      }

      return null;
   }

   public static String getRDBMSDescriptorFileName(PersistenceBean var0) {
      PersistenceUseBean var1 = var0.getPersistenceUse();
      return var1 != null ? var1.getTypeStorage() : null;
   }

   public static String getRDBMSDescriptorFileName(WeblogicRdbmsJarBean var0, EjbDescriptorBean var1) {
      String var2 = "weblogic-cmp-rdbms-jar.xml";
      WeblogicRdbmsBeanBean[] var3 = var0.getWeblogicRdbmsBeans();
      if (var3.length >= 1) {
         String var4 = var3[0].getEjbName();
         if (var4 != null) {
            PersistenceBean var5 = getPersistenceMBean(var4, var1);
            if (var5 != null) {
               return getRDBMSDescriptorFileName(var5);
            }
         }
      }

      return var2;
   }

   public static String getRDBMSDescriptorFileName(weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean var0, EjbDescriptorBean var1) {
      String var2 = "weblogic-cmp-rdbms-jar.xml";
      weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBean[] var3 = var0.getWeblogicRdbmsBeans();
      if (var3.length >= 1) {
         String var4 = var3[0].getEjbName();
         if (var4 != null) {
            PersistenceBean var5 = getPersistenceMBean(var4, var1);
            if (var5 != null) {
               return getRDBMSDescriptorFileName(var5);
            }
         }
      }

      return var2;
   }

   public static boolean areIdenticalMBeans(XMLElementMBean var0, XMLElementMBean var1) {
      boolean var2 = true;
      if ((var0 != null || var1 == null) && (var0 == null || var1 != null)) {
         Method[] var3 = var0.getClass().getDeclaredMethods();
         LinkedList var4 = new LinkedList();

         Method var6;
         for(int var5 = 0; var5 < var3.length; ++var5) {
            var6 = var3[var5];
            if (var6.getName().startsWith("get") || var6.getName().startsWith("is")) {
               var4.add(var6);
            }
         }

         Iterator var11 = var4.iterator();

         while(var11.hasNext()) {
            var6 = (Method)var11.next();

            try {
               Object var7 = var6.invoke(var0);
               Object var8 = var6.invoke(var1);
               if (var6.getReturnType().isAssignableFrom(XMLElementMBean.class)) {
                  var2 = var2 && areIdenticalMBeans((XMLElementMBean)var7, (XMLElementMBean)var8);
               } else if (var7 == null && var8 == null) {
                  var2 = true;
               } else if (var7 == null && var8 != null || var7 != null && var8 == null) {
                  var2 = false;
               } else {
                  var2 = var2 && var7.equals(var8);
               }
            } catch (IllegalAccessException var9) {
               EJBLogger.logStackTrace(var9);
               var2 = false;
            } catch (InvocationTargetException var10) {
               EJBLogger.logStackTrace(var10);
               var2 = false;
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   private static void ppp(String var0) {
      System.out.println("[EJBDescriptorMBeanUtils] " + var0);
   }
}
