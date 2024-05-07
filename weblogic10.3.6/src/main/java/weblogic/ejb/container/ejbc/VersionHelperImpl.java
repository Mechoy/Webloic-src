package weblogic.ejb.container.ejbc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import weblogic.version;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.ejbc.codegen.MethodSignature;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.VersionHelper;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.InterceptorsBean;
import weblogic.j2ee.descriptor.RelationshipsBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;
import weblogic.utils.FileUtils;
import weblogic.utils.Getopt2;
import weblogic.utils.jars.VirtualJarFile;

public final class VersionHelperImpl implements VersionHelper {
   private static final DebugLogger debugLogger;
   private static final String VERSION_TAG = "WLS_RELEASE_BUILD_VERSION_37";
   private static final String[] SAVED_OPTIONS;
   private final DeploymentInfo di;
   private final Map<String, List<BeanInfo>> classesToBeans;
   private final Properties currentJarHash;
   private final Getopt2 opts;

   public VersionHelperImpl(DeploymentInfo var1, VirtualJarFile var2, Getopt2 var3) throws ClassNotFoundException {
      this.di = var1;
      this.opts = var3;
      this.classesToBeans = new HashMap();
      this.currentJarHash = this.makeFileHash(var2);
   }

   public boolean needsRecompile(String var1, ClassLoader var2) throws ClassNotFoundException {
      if (debugLogger.isDebugEnabled()) {
         debug("calculating hash for: " + var1);
      }

      String var3 = this.currentJarHash.getProperty(var1);
      if (var3 == null) {
         if (debugLogger.isDebugEnabled()) {
            debug("Hmm.  The old hash for class " + var1 + " was null");
         }

         return true;
      } else {
         boolean var4 = ((BeanInfo)((List)this.classesToBeans.get(var1)).get(0)).isEntityBean();
         long var5 = this.computeCRC(var1, var2, var4);
         if (debugLogger.isDebugEnabled()) {
            debug("new hash: " + var5 + " oldHash: " + var3);
         }

         return !String.valueOf(var5).equals(var3);
      }
   }

   public Collection needsRecompile(VirtualJarFile var1) {
      Properties var2 = this.getPreviousFileHash(var1);
      return this.needsRecompile(var2);
   }

   public Collection needsRecompile(Properties var1) {
      assert this.currentJarHash != null;

      if (var1 == null) {
         if (debugLogger.isDebugEnabled()) {
            debug("Recompiling because no previous hashes found");
         }

         return this.di.getBeanInfos();
      } else if (this.currentJarHash.size() != var1.size()) {
         if (debugLogger.isDebugEnabled()) {
            debug("Recompiling because number of hashes different");
            debug("current size: " + this.currentJarHash.size() + " old size: " + var1.size());
            debug("Dumping hashes:");
            debug("CurrentHashes:");
            this.dumpHashes(this.currentJarHash);
            debug("OldHashes:");
            this.dumpHashes(var1);
         }

         return this.di.getBeanInfos();
      } else {
         Enumeration var2 = this.currentJarHash.propertyNames();
         HashSet var3 = new HashSet();

         while(true) {
            String var4;
            String var5;
            String var6;
            do {
               if (!var2.hasMoreElements()) {
                  if (debugLogger.isDebugEnabled() && var3.isEmpty()) {
                     debug("Need not recompile EJB component " + this.di.getEJBComponentName());
                  }

                  return var3;
               }

               var4 = (String)var2.nextElement();
               var5 = this.currentJarHash.getProperty(var4);
               var6 = var1.getProperty(var4);
            } while(var5.equals(var6));

            if (var4.endsWith(".xml") || "WLS_RELEASE_BUILD_VERSION_37".equals(var4)) {
               if (debugLogger.isDebugEnabled()) {
                  debug("Recompiling all due to different hash for " + var4);
               }

               return this.di.getBeanInfos();
            }

            if (!this.classesToBeans.containsKey(var4)) {
               return this.di.getBeanInfos();
            }

            List var7 = (List)this.classesToBeans.get(var4);
            if (debugLogger.isDebugEnabled()) {
               Iterator var8 = var7.iterator();

               String var9;
               for(var9 = ""; var8.hasNext(); var9 = var9.concat(((BeanInfo)var8.next()).getEJBName() + " ")) {
               }

               debug("Recompiling the following due to different hash for " + var4 + ": " + var9);
            }

            var3.addAll(var7);
         }
      }
   }

   public Properties getCurrentJarHash() {
      return this.currentJarHash;
   }

   public DeploymentInfo getDeploymentInfo() {
      return this.di;
   }

   public void removeCompilerOptions(Properties var1) {
      for(int var2 = 0; var2 < SAVED_OPTIONS.length; ++var2) {
         var1.remove(SAVED_OPTIONS[var2]);
      }

   }

   private Properties makeFileHash(VirtualJarFile var1) throws ClassNotFoundException {
      Properties var2 = new Properties();
      var2.setProperty("WLS_RELEASE_BUILD_VERSION_37", version.getReleaseBuildVersion());
      Collection var3 = this.di.getBeanInfos();
      Iterator var4 = var3.iterator();

      while(true) {
         while(true) {
            BeanInfo var5;
            do {
               if (!var4.hasNext()) {
                  Iterator var11 = this.classesToBeans.entrySet().iterator();

                  while(var11.hasNext()) {
                     Map.Entry var13 = (Map.Entry)var11.next();
                     String var15 = (String)var13.getKey();
                     BeanInfo var17 = (BeanInfo)((List)var13.getValue()).get(0);
                     long var18 = this.computeCRC(var15, var17.getClassLoader(), var17.isEntityBean());
                     var2.setProperty(var15, String.valueOf(var18));
                  }

                  this.addHashForDDs(var2);

                  for(int var12 = 0; var12 < SAVED_OPTIONS.length; ++var12) {
                     this.saveOptionIfPresent(SAVED_OPTIONS[var12], var2);
                  }

                  return var2;
               }

               var5 = (BeanInfo)var4.next();
               this.addBeanDependencyToClass(var5, var5.getBeanClassName());
            } while(!(var5 instanceof ClientDrivenBeanInfo));

            ClientDrivenBeanInfo var6 = (ClientDrivenBeanInfo)var5;
            if (var6.hasDeclaredRemoteHome()) {
               this.addBeanDependencyToClass(var5, var6.getRemoteInterfaceName());
               this.addBeanDependencyToClass(var5, var6.getHomeInterfaceName());
            }

            if (var6.hasDeclaredLocalHome()) {
               this.addBeanDependencyToClass(var5, var6.getLocalInterfaceName());
               this.addBeanDependencyToClass(var5, var6.getLocalHomeInterfaceName());
            }

            if (var6.hasWebserviceClientView() && var6.getServiceEndpointName() != null) {
               this.addBeanDependencyToClass(var5, var6.getServiceEndpointName());
            }

            if (var6 instanceof Ejb3SessionBeanInfo) {
               Ejb3SessionBeanInfo var14 = (Ejb3SessionBeanInfo)var6;
               Iterator var16 = var14.getBusinessRemotes().iterator();

               Class var9;
               while(var16.hasNext()) {
                  var9 = (Class)var16.next();
                  this.addBeanDependencyToClass(var5, var9.getName());
               }

               var16 = var14.getBusinessLocals().iterator();

               while(var16.hasNext()) {
                  var9 = (Class)var16.next();
                  this.addBeanDependencyToClass(var5, var9.getName());
               }
            } else if (var6 instanceof EntityBeanInfo) {
               EntityBeanInfo var7 = (EntityBeanInfo)var6;
               CMPInfo var8 = var7.getCMPInfo();
               if (var8 != null && !var7.isUnknownPrimaryKey() && var8.getCMPrimaryKeyFieldName() == null) {
                  this.addBeanDependencyToClass(var5, var7.getPrimaryKeyClassName());
               }
            }
         }
      }
   }

   private void addHashForDDs(Properties var1) throws ClassNotFoundException {
      EjbDescriptorBean var2 = this.di.getEjbDescriptorBean();
      String var3 = var2.getEjbJarBean().getVersion();
      if (var3 != null) {
         var1.setProperty("version", var3);
      }

      String var4 = var2.getEjbJarBean().getEjbClientJar();
      if (var4 != null) {
         var1.setProperty("ejb-client-jar", var4);
      }

      EnterpriseBeansBean var5 = var2.getEjbJarBean().getEnterpriseBeans();
      if (var5 != null) {
         var1.setProperty("enterprise-beans", ((AbstractDescriptorBean)var5).getHashValue());
      }

      RelationshipsBean var6 = var2.getEjbJarBean().getRelationships();
      if (var6 != null) {
         var1.setProperty("relationships", ((AbstractDescriptorBean)var6).getHashValue());
      }

      InterceptorsBean var7 = var2.getEjbJarBean().getInterceptors();
      if (var7 != null && var7.getInterceptors() != null) {
         var1.setProperty("interceptors(non-serializable)", this.nonSerIcptrs(var7));
      }

      var1.setProperty("weblogic-ejb-jar.xml", ((AbstractDescriptorBean)var2.getWeblogicEjbJarBean()).getHashValue());
      WeblogicRdbmsJarBean[] var8 = var2.getWeblogicRdbmsJarBeans();
      String[] var9 = new String[var8.length];

      int var10;
      for(var10 = 0; var10 < var8.length; ++var10) {
         var9[var10] = ((AbstractDescriptorBean)var8[var10]).getHashValue();
      }

      Arrays.sort(var9);

      for(var10 = 0; var10 < var8.length; ++var10) {
         var1.setProperty("weblogic-cmp-rdbms-jar.xml" + var10, var9[var10]);
      }

   }

   private String nonSerIcptrs(InterceptorsBean var1) throws ClassNotFoundException {
      StringBuilder var2 = new StringBuilder();
      TreeSet var3 = new TreeSet();
      ClassLoader var4 = this.di.getModuleClassLoader();
      InterceptorBean[] var5 = var1.getInterceptors();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         InterceptorBean var8 = var5[var7];
         String var9 = var8.getInterceptorClass();
         Class var10 = var4.loadClass(var9);
         if (!Serializable.class.isAssignableFrom(var10)) {
            var3.add(var9);
         }
      }

      Iterator var11 = var3.iterator();

      while(var11.hasNext()) {
         String var12 = (String)var11.next();
         var2.append(var12 + " ");
      }

      return var2.toString();
   }

   private void addBeanDependencyToClass(BeanInfo var1, String var2) {
      List var3 = (List)this.classesToBeans.get(var2);
      if (var3 == null) {
         LinkedList var4 = new LinkedList();
         var4.add(var1);
         this.classesToBeans.put(var2, var4);
      } else {
         var3.add(var1);
      }

   }

   private Properties getPreviousFileHash(VirtualJarFile var1) {
      ZipEntry var2 = var1.getEntry("_WL_GENERATED");
      if (var2 == null) {
         return null;
      } else {
         InputStream var3 = null;

         Properties var5;
         try {
            Properties var4 = new Properties();
            var3 = var1.getInputStream(var2);
            var4.load(var3);
            var5 = var4;
            return var5;
         } catch (IOException var15) {
            EJBLogger.logExceptionLoadingTimestamp(var15);
            var5 = null;
         } finally {
            try {
               if (var3 != null) {
                  var3.close();
               }
            } catch (IOException var14) {
            }

         }

         return var5;
      }
   }

   private long computeCRC(File var1) throws IOException {
      return FileUtils.computeCRC(var1);
   }

   private long computeCRC(String var1, ClassLoader var2, boolean var3) throws ClassNotFoundException {
      CRC32 var4 = new CRC32();
      Class var5 = var2.loadClass(var1);
      ArrayList var6 = new ArrayList();
      Method[] var7 = var5.getMethods();
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Method var10 = var7[var9];
         if (var10.getDeclaringClass() != Object.class) {
            String var11 = null;
            if (var3) {
               var11 = (new MethodSignature(var10, var5)).toString(false);
            } else {
               var11 = var10.toString();
            }

            var6.add(var11);
            if (debugLogger.isDebugEnabled()) {
               debug("Signature" + var11);
            }
         }
      }

      Collections.sort(var6);
      Iterator var12 = var6.iterator();

      while(var12.hasNext()) {
         String var14 = (String)var12.next();
         var4.update(var14.getBytes());
      }

      Field[] var13 = var5.getFields();
      var6.clear();

      for(var8 = 0; var8 < var13.length; ++var8) {
         if (var13[var8].getDeclaringClass() != Object.class) {
            var6.add(var13[var8].toString());
         }
      }

      Collections.sort(var6);
      Iterator var15 = var6.iterator();

      while(var15.hasNext()) {
         String var16 = (String)var15.next();
         var4.update(var16.getBytes());
      }

      return var4.getValue();
   }

   private void saveOptionIfPresent(String var1, Properties var2) {
      if (this.isOptionPresent(var1)) {
         var2.setProperty(var1, "true");
      }

   }

   private boolean isOptionPresent(String var1) {
      return this.opts != null && this.opts.hasOption(var1);
   }

   private void dumpHashes(Properties var1) {
      Enumeration var2 = var1.propertyNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         debug("Property name: " + var3);
      }

   }

   private static void debug(String var0) {
      debugLogger.debug("[VersionHelper] " + var0);
   }

   static {
      debugLogger = EJBDebugService.compilationLogger;
      SAVED_OPTIONS = new String[]{"keepgenerated", "g"};
   }
}
