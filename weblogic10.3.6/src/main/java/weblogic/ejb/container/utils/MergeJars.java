package weblogic.ejb.container.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import weblogic.descriptor.DescriptorBean;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.deployer.EJBDescriptorMBeanUtils;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.AssemblyDescriptorBean;
import weblogic.j2ee.descriptor.ContainerTransactionBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EjbRelationBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.MethodPermissionBean;
import weblogic.j2ee.descriptor.RelationshipsBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.PersistenceBean;
import weblogic.j2ee.descriptor.wl.PersistenceUseBean;
import weblogic.j2ee.descriptor.wl.SecurityRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.TransactionIsolationBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsRelationBean;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean;
import weblogic.utils.Debug;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public final class MergeJars {
   private static final boolean verbose = true;
   private static final boolean debug = true;
   private static final String EJB_JAR = "META-INF/ejb-jar.xml";
   private static final String WEBLOGIC_EJB_JAR = "META-INF/weblogic-ejb-jar.xml";
   private static final String CMP_JAR = "META-INF/weblogic-cmp-rdbms-jar.xml";
   private String m_targetJarFileName;
   private Collection m_jars = new LinkedList();
   private Collection m_ejbDescriptors = new LinkedList();

   public void mergeJars(String[] var1) {
      EjbDescriptorBean var2 = new EjbDescriptorBean();
      this.m_targetJarFileName = var1[0];

      EjbDescriptorBean var5;
      for(int var3 = 1; var3 < var1.length; ++var3) {
         try {
            System.out.println("Reading " + var1[var3]);
            VirtualJarFile var4 = VirtualJarFactory.createVirtualJar(new File(var1[var3]));
            this.m_jars.add(var4);
            var5 = EJBDescriptorMBeanUtils.createDescriptorFromJarFile(var4);
            this.m_ejbDescriptors.add(var5);
         } catch (IOException var9) {
            EJBLogger.logStackTrace(var9);
         } catch (XMLParsingException var10) {
            EJBLogger.logStackTrace(var10);
         } catch (XMLProcessingException var11) {
            EJBLogger.logStackTrace(var11);
         } catch (Exception var12) {
            EJBLogger.logStackTrace(var12);
         }
      }

      EjbJarBean var13 = var2.createEjbJarBean();
      var13.addDescription("Merged EJB Jar");
      var13.addDisplayName("Merged EJB Jar");
      var13.createEnterpriseBeans();
      WeblogicEjbJarBean var14 = var2.createWeblogicEjbJarBean();
      var14.setDescription("Merged Weblogic EJB Jar");
      var5 = null;
      WeblogicRdbmsJarBean var6 = null;
      Iterator var7 = this.m_ejbDescriptors.iterator();

      while(var7.hasNext()) {
         EjbDescriptorBean var8 = (EjbDescriptorBean)var7.next();
         addEJBJar20(var13, var8.getEjbJarBean());
         addWlEJBJar(var14, var8.getWeblogicEjbJarBean());
         if (null != var8.getWeblogicRdbms11JarBeans() && var8.getWeblogicRdbms11JarBeans().length > 0) {
            Debug.say("@@@ FOUND 1.1 CMP: " + var8.getWeblogicRdbms11JarBeans().length);
            weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean var15 = var2.createWeblogicRdbms11JarBean();
            addCmpJar(var15, var8.getWeblogicRdbms11JarBeans());
            adjustPersistenceTypeStorage(var14);
         }

         if (null != var8.getWeblogicRdbmsJarBeans() && var8.getWeblogicRdbmsJarBeans().length > 0) {
            Debug.say("@@@ FOUND 2.0 CMP: " + var8.getWeblogicRdbmsJarBeans().length);
            var6 = var2.createWeblogicRdbmsJarBean();
            addCmpJar(var6, var8.getWeblogicRdbmsJarBeans());
            adjustPersistenceTypeStorage(var14);
         }
      }

      createOutputJar(this.m_targetJarFileName, var2, this.m_jars);
   }

   private static void adjustPersistenceTypeStorage(WeblogicEjbJarBean var0) {
      WeblogicEnterpriseBeanBean[] var1 = var0.getWeblogicEnterpriseBeans();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (null != var1[var2].getEntityDescriptor()) {
            PersistenceBean var3 = var1[var2].getEntityDescriptor().getPersistence();
            if (null != var3 && null != var3.getPersistenceUse()) {
               PersistenceUseBean var4 = var3.getPersistenceUse();
               if (null != var4) {
                  var4.setTypeStorage("META-INF/weblogic-cmp-rdbms-jar.xml");
               } else {
                  System.out.println("Warning:  couldn't find a persistence use for EJB:" + var1[var2].getEjbName() + " " + var3.getPersistenceUse().getTypeIdentifier());
               }
            }
         }
      }

   }

   private static boolean mustSkip(JarEntry var0) {
      String var1 = var0.getName();
      if ("META-INF/MANIFEST.MF".equals(var1)) {
         return false;
      } else {
         return 0 == var1.indexOf("META-INF/") || var1.equals("_WL_GENERATED");
      }
   }

   private static void createOutputJar(String var0, EjbDescriptorBean var1, Collection var2) {
      System.out.println("Creating " + var0);
      HashMap var3 = new HashMap();
      JarOutputStream var4 = null;
      FileOutputStream var5 = null;

      try {
         var5 = new FileOutputStream(var0);
         var4 = new JarOutputStream(var5);
         HashMap var6 = new HashMap();
         Iterator var7 = var2.iterator();

         label183:
         while(var7.hasNext()) {
            VirtualJarFile var8 = (VirtualJarFile)var7.next();
            Iterator var9 = var8.entries();

            while(true) {
               while(true) {
                  JarEntry var10;
                  do {
                     if (!var9.hasNext()) {
                        continue label183;
                     }

                     var10 = (JarEntry)var9.next();
                  } while(mustSkip(var10));

                  Object var11 = var6.get(var10.getName());
                  JarEntry var12;
                  if (null == var11) {
                     var6.put(var10.getName(), var10);
                     var12 = new JarEntry(var10.getName());
                     var4.putNextEntry(var12);
                     InputStream var13 = var8.getInputStream(var10);
                     byte[] var14 = new byte[1024];

                     int var15;
                     while((var15 = var13.read(var14)) > 0) {
                        var4.write(var14, 0, var15);
                     }

                     var4.closeEntry();
                  } else {
                     var12 = (JarEntry)var11;
                     if (var12.getSize() != var10.getSize()) {
                        var3.put(var10.getName(), var10);
                     }
                  }
               }
            }
         }

         var4.close();
         var5.close();
         var1.usePersistenceDestination(var0);
         var1.persist();
      } catch (IOException var24) {
         EJBLogger.logStackTrace(var24);
      } finally {
         try {
            if (null != var4) {
               var4.close();
            }

            if (null != var5) {
               var5.close();
            }
         } catch (IOException var23) {
         }

      }

      System.out.println("... done");
      if (var3.size() > 0) {
         System.out.println("Warning:  the following files appear in several Jar files and have \ndifferent sizes.  The output JAR file may therefore fail to deploy.");
         Iterator var26 = var3.values().iterator();

         while(var26.hasNext()) {
            System.out.println("   " + ((JarEntry)var26.next()).getName());
         }
      }

   }

   private static void addCmpJar(weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean var0, weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean[] var1) {
      DescriptorBean var2 = (DescriptorBean)var0;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         WeblogicRdbmsBeanBean[] var4 = var1[var3].getWeblogicRdbmsBeans();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var2.createChildCopy("WeblogicRdbmsBean", (DescriptorBean)var4[var5]);
         }
      }

   }

   private static void addCmpJar(WeblogicRdbmsJarBean var0, WeblogicRdbmsJarBean[] var1) {
      DescriptorBean var2 = (DescriptorBean)var0;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBean[] var4 = var1[var3].getWeblogicRdbmsBeans();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var2.createChildCopy("WeblogicRdbmsBean", (DescriptorBean)var4[var5]);
         }

         WeblogicRdbmsRelationBean[] var7 = var1[var3].getWeblogicRdbmsRelations();

         for(int var6 = 0; var6 < var7.length; ++var6) {
            var2.createChildCopy("WeblogicRdbmsRelation", (DescriptorBean)var7[var6]);
         }
      }

   }

   private static void addWlEJBJar(WeblogicEjbJarBean var0, WeblogicEjbJarBean var1) {
      DescriptorBean var2 = (DescriptorBean)var0;
      WeblogicEnterpriseBeanBean[] var3 = var1.getWeblogicEnterpriseBeans();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2.createChildCopy("WeblogicEnterpriseBean", (DescriptorBean)var3[var4]);
      }

      SecurityRoleAssignmentBean[] var7 = var1.getSecurityRoleAssignments();

      for(int var5 = 0; var5 < var7.length; ++var5) {
         var2.createChildCopy("SecurityRoleAssignment", (DescriptorBean)var7[var5]);
      }

      TransactionIsolationBean[] var8 = var1.getTransactionIsolations();

      for(int var6 = 0; var6 < var8.length; ++var6) {
         var2.createChildCopy("TransactionIsolation", (DescriptorBean)var8[var6]);
      }

   }

   private static void addEJBJar20(EjbJarBean var0, EjbJarBean var1) {
      addBeans(var0, var1.getEnterpriseBeans());
      addRelations(var0, var1.getRelationships());
      addAssemblyDescriptor(var0, var1.getAssemblyDescriptor());
   }

   private static void addRelations(EjbJarBean var0, RelationshipsBean var1) {
      if (null != var1) {
         RelationshipsBean var2 = var0.getRelationships();
         if (null == var2) {
            var2 = var0.createRelationships();
         }

         DescriptorBean var3 = (DescriptorBean)var2;
         EjbRelationBean[] var4 = var1.getEjbRelations();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var3.createChildCopy("EjbRelation", (DescriptorBean)var4[var5]);
         }
      }

   }

   private static void addRelations(WeblogicRdbmsJarBean var0, WeblogicRdbmsJarBean[] var1) {
      DescriptorBean var2 = (DescriptorBean)var0;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         WeblogicRdbmsRelationBean[] var4 = var1[var3].getWeblogicRdbmsRelations();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var2.createChildCopy("WeblogicRdbmsRelation", (DescriptorBean)var4[var5]);
         }
      }

   }

   private static void addAssemblyDescriptor(EjbJarBean var0, AssemblyDescriptorBean var1) {
      if (null != var1) {
         AssemblyDescriptorBean var2 = var0.getAssemblyDescriptor();
         if (null == var2) {
            var2 = var0.createAssemblyDescriptor();
         }

         DescriptorBean var3 = (DescriptorBean)var2;
         SecurityRoleBean[] var4 = var1.getSecurityRoles();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var3.createChildCopy("SecurityRole", (DescriptorBean)var4[var5]);
         }

         MethodPermissionBean[] var8 = var1.getMethodPermissions();

         for(int var6 = 0; var6 < var8.length; ++var6) {
            var3.createChildCopy("MethodPermission", (DescriptorBean)var8[var6]);
         }

         ContainerTransactionBean[] var9 = var1.getContainerTransactions();

         for(int var7 = 0; var7 < var9.length; ++var7) {
            var3.createChildCopy("ContainerTransaction", (DescriptorBean)var9[var7]);
         }
      }

   }

   private static void addBeans(EjbJarBean var0, EnterpriseBeansBean var1) {
      EnterpriseBeansBean var2 = var0.getEnterpriseBeans();
      DescriptorBean var3 = (DescriptorBean)var2;
      EntityBeanBean[] var4 = var1.getEntities();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var3.createChildCopy("Entity", (DescriptorBean)var4[var5]);
      }

      SessionBeanBean[] var8 = var1.getSessions();

      for(int var6 = 0; var6 < var8.length; ++var6) {
         var3.createChildCopy("Session", (DescriptorBean)var8[var6]);
      }

      MessageDrivenBeanBean[] var9 = var1.getMessageDrivens();

      for(int var7 = 0; var7 < var9.length; ++var7) {
         var3.createChildCopy("MessageDriven", (DescriptorBean)var9[var7]);
      }

   }

   private static void ppp(String var0) {
      System.out.println("[MergeJars] " + var0);
   }

   public static void main(String[] var0) {
      if (var0.length < 2) {
         System.out.println("MergeJars v0.81:  Merge several jar files into one.");
         System.out.println("Usage:  MergeJars outputJar inputJar [inputJar inputJar...]");
      } else {
         (new MergeJars()).mergeJars(var0);
      }

   }
}
