package weblogic.cacheprovider.coherence;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.ArrayList;
import weblogic.cacheprovider.coherence.management.CoherenceJMXBridge;
import weblogic.coherence.descriptor.wl.WeblogicCoherenceBean;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.configuration.CoherenceClusterSystemResourceMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class CoherenceClusterManager {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private CoherenceJMXBridge jmxBridge;

   public static CoherenceClusterManager getInstance() {
      return CoherenceClusterManager.SingletonMaker.singleton;
   }

   private CoherenceClusterManager() {
      this.jmxBridge = new CoherenceJMXBridge();
   }

   public boolean isCoherenceAvailable(ClassLoader var1) {
      try {
         this.getWLSCoherenceConfigurator(var1);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public void configureClusterService(ClassLoader var1, String var2) throws CoherenceException {
      try {
         WeblogicCoherenceBean var3 = null;
         boolean var4 = false;
         CoherenceClusterSystemResourceMBean var5 = this.lookupCoherenceClusterSystemResourceMBean(var2);
         if (var5 != null) {
            var3 = var5.getCoherenceClusterResource();
            var4 = var5.isUsingCustomClusterConfigurationFile();
         }

         Object var6 = this.getWLSCoherenceConfigurator(var1);
         if (var6 != null) {
            Class var7 = var6.getClass();
            Class[] var8 = new Class[]{Boolean.TYPE, WeblogicCoherenceBean.class, String.class, String.class};
            Method var9 = var7.getDeclaredMethod("configureClusterService", var8);
            Object[] var10 = new Object[]{var4, var3, DomainDir.getConfigDir(), this.getLoggingDestination()};
            var9.invoke(var6, var10);
         }
      } catch (Exception var11) {
         this.processException(var11);
      }

   }

   public void shutdownClusterService(ClassLoader var1) throws CoherenceException {
      try {
         Object var2 = this.getWLSCoherenceConfigurator(var1);
         if (var2 != null) {
            Class var3 = var2.getClass();
            Class[] var4 = new Class[]{ClassLoader.class};
            Method var5 = var3.getDeclaredMethod("shutdownClusterService", var4);
            Object[] var6 = new Object[]{var1};
            var5.invoke(var2, var6);
         }
      } catch (Exception var11) {
         this.processException(var11);
      } finally {
         this.jmxBridge.shutdown(var1);
      }

   }

   public void addCacheConfiguration(ClassLoader var1) throws CoherenceException {
      try {
         Object var2 = this.getWLSCacheFactoryBuilder(var1);
         if (var2 != null) {
            Class var3 = var2.getClass();
            Class[] var4 = new Class[]{ClassLoader.class};
            Method var5 = var3.getDeclaredMethod("addCacheConfiguration", var4);
            Object[] var6 = new Object[]{var1};
            var5.invoke(var2, var6);
         }
      } catch (Exception var7) {
         this.processException(var7);
      }

   }

   public void releaseCacheConfiguration(ClassLoader var1) throws CoherenceException {
      try {
         Object var2 = this.getWLSCacheFactoryBuilder(var1);
         if (var2 != null) {
            Class var3 = var2.getClass();
            Class[] var4 = new Class[]{ClassLoader.class};
            Method var5 = var3.getDeclaredMethod("releaseAll", var4);
            Object[] var6 = new Object[]{var1};
            var5.invoke(var2, var6);
         }
      } catch (Exception var7) {
         this.processException(var7);
      }

   }

   public Object ensureCache(String var1, ClassLoader var2) throws CoherenceException {
      try {
         Object var3 = this.getWLSCacheFactoryBuilder(var2);
         if (var3 != null) {
            Class var4 = var3.getClass();
            Class[] var5 = new Class[]{String.class, ClassLoader.class};
            Method var6 = var4.getMethod("ensureCache", var5);
            Object[] var7 = new Object[]{var1, var2};
            Object var8 = var6.invoke(var3, var7);
            if (DebugLogger.isDebugEnabled()) {
               DebugLogger.debug("CoherenceClusterManager: got cache " + var8 + "using classloader " + var2);
            }

            return var8;
         }
      } catch (Exception var9) {
         this.processException(var9);
      }

      return null;
   }

   public Object ensureService(String var1, ClassLoader var2) throws CoherenceException {
      try {
         Object var3 = this.getWLSCacheFactoryBuilder(var2);
         if (var3 != null) {
            Class var4 = var3.getClass();
            Class[] var5 = new Class[]{String.class};
            Method var6 = var4.getMethod("ensureService", var5);
            Object[] var7 = new Object[]{var1};
            return var6.invoke(var3, var7);
         }
      } catch (Exception var8) {
         this.processException(var8);
      }

      return null;
   }

   public void registerApplicationRuntimeMBean(ClassLoader var1, ApplicationRuntimeMBean var2) throws ManagementException {
      this.jmxBridge.registerApplicationRuntimeMBean(var1, var2);
   }

   public void unRegisterApplicationRuntimeMBean(ClassLoader var1, ApplicationRuntimeMBean var2) throws ManagementException {
      this.jmxBridge.unRegisterApplicationRuntimeMBean(var1, var2);
   }

   public void registerWebAppComponentRuntimeMBean(ClassLoader var1, WebAppComponentRuntimeMBean[] var2) throws ManagementException {
      this.jmxBridge.registerWebAppComponentRuntimeMBean(var1, var2);
   }

   public void unRegisterWebAppComponentRuntimeMBean(ClassLoader var1, WebAppComponentRuntimeMBean[] var2) throws ManagementException {
      this.jmxBridge.unRegisterWebAppComponentRuntimeMBean(var1, var2);
   }

   public void registerEJBComponentRuntimeMBean(ClassLoader var1, EJBComponentRuntimeMBean var2) throws ManagementException {
      this.jmxBridge.registerEJBComponentRuntimeMBean(var1, var2);
   }

   public void unRegisterEJBComponentRuntimeMBean(ClassLoader var1, EJBComponentRuntimeMBean var2) throws ManagementException {
      this.jmxBridge.unRegisterEJBComponentRuntimeMBean(var1, var2);
   }

   public CoherenceClusterSystemResourceMBean[] getAllLocalCoherenceClusterSystemResourceMBeans() {
      ArrayList var1 = new ArrayList();
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      String var3 = ManagementService.getRuntimeAccess(kernelId).getServerName();
      String var4 = null;
      if (ManagementService.getRuntimeAccess(kernelId).getServer().getCluster() != null) {
         var4 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster().getName();
      }

      CoherenceClusterSystemResourceMBean[] var5 = var2.getCoherenceClusterSystemResources();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         TargetMBean[] var7 = var5[var6].getTargets();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            if (var7[var8].getName().equals(var3) || var4 != null && var7[var8].getName().equals(var4)) {
               var1.add(var5[var6]);
            }
         }
      }

      return (CoherenceClusterSystemResourceMBean[])var1.toArray(new CoherenceClusterSystemResourceMBean[0]);
   }

   private Object getWLSCoherenceConfigurator(ClassLoader var1) throws CoherenceException {
      try {
         Class var2 = Class.forName("weblogic.coherence.service.internal.WLSCoherenceConfigurator", true, var1);
         Class[] var3 = new Class[]{Object.class};
         Method var4 = var2.getDeclaredMethod("getInstance", var3);
         Object[] var5 = new Object[]{this.jmxBridge};
         return var4.invoke((Object)null, var5);
      } catch (Exception var6) {
         this.processException(var6);
         return null;
      }
   }

   private Object getWLSCacheFactoryBuilder(ClassLoader var1) throws CoherenceException {
      try {
         Object var2 = this.getWLSCoherenceConfigurator(var1);
         Class var3 = var2.getClass();
         Method var4 = var3.getDeclaredMethod("getCacheFactoryBuilder", (Class[])null);
         return var4.invoke(var2, (Object[])null);
      } catch (Exception var5) {
         this.processException(var5);
         return null;
      }
   }

   private void processException(Throwable var1) throws CoherenceException {
      if (var1 instanceof CoherenceException) {
         throw (CoherenceException)var1;
      } else {
         if (var1 instanceof InvocationTargetException) {
            var1 = ((InvocationTargetException)var1).getCause();
         }

         CoherenceException var2 = new CoherenceException(var1.getMessage());
         var2.initCause(var1);
         throw var2;
      }
   }

   private CoherenceClusterSystemResourceMBean lookupCoherenceClusterSystemResourceMBean(String var1) {
      if (var1 == null) {
         return null;
      } else {
         DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         return var2.lookupCoherenceClusterSystemResource(var1);
      }
   }

   private String getCoherenceClusterConfigDir(CoherenceClusterSystemResourceMBean var1) throws IOException {
      String var2 = var1.getCustomClusterConfigurationFileName();
      String var3 = DomainDir.getConfigDir();
      String var4 = var1.getDescriptorFileName();
      File var5 = new File(var3 + File.separator + var4);
      File var6 = var5.getParentFile();
      return var6.getCanonicalPath();
   }

   private String getLoggingDestination() {
      ServerMBean var1 = getServerMBean();
      return var1.getLog().isLog4jLoggingEnabled() ? "log4j" : "jdk";
   }

   public static ServerMBean getServerMBean() {
      ServerMBean var0 = ManagementService.getRuntimeAccess(kernelId).getServer();
      return var0;
   }

   // $FF: synthetic method
   CoherenceClusterManager(Object var1) {
      this();
   }

   private static class SingletonMaker {
      private static final CoherenceClusterManager singleton = new CoherenceClusterManager();
   }
}
