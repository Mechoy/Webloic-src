package weblogic.connector.deploy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import weblogic.application.ApplicationContextInternal;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.external.AdminObjInfo;
import weblogic.connector.external.ConfigPropInfo;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.external.RAInfo;
import weblogic.connector.external.impl.OutboundInfoImpl;
import weblogic.management.DeploymentException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.utils.classloaders.AugmentableClassLoaderManager;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public class DeployerUtil {
   private static String defaultNativeLibDir = "j2ca_native_lib";

   public static void createNativeLibDir(VirtualJarFile var0, RAInfo var1, ApplicationContextInternal var2) throws DeploymentException {
      String var3 = var1.getNativeLibDir();
      if (var3 == null || var3.length() == 0) {
         File var4 = new File(var2.getOutputPath());
         if (var4.isDirectory()) {
            var3 = var2.getOutputPath();
         } else {
            var3 = (new File(var2.getOutputPath())).getParent();
         }

         if (!var3.endsWith(File.separator)) {
            var3 = var3 + File.separator;
         }

         var3 = var3 + defaultNativeLibDir;
      }

      AuthenticatedSubject var25 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      SecurityManager.pushSubject(var25, var25);

      try {
         File var5 = new File(var3);
         Iterator var8 = var0.entries();

         while(var8.hasNext()) {
            ZipEntry var6 = (ZipEntry)var8.next();
            String var7 = var6.getName();
            if (var7.endsWith(".dll") || var7.endsWith(".so") || var7.endsWith(".sl")) {
               String var10;
               try {
                  String var9;
                  if (!var5.exists() && !var5.mkdirs() || !var5.isDirectory()) {
                     var9 = Debug.getExceptionErrorCreatingNativeLibDir(var5.getPath());
                     IOException var27 = new IOException(var9);
                     throw new DeploymentException(var9, var27);
                  }

                  var9 = var6.getName();
                  int var26 = var9.lastIndexOf("/");
                  int var11 = var9.lastIndexOf("\\");
                  int var12 = var26 > var11 ? var26 : var11;
                  if (var12 != -1) {
                     var9 = var9.substring(var12 + 1);
                  }

                  Debug.logExtractingNativeLib(var9, var3);
                  if (Debug.isDeploymentEnabled()) {
                     Debug.deployment("Extracting " + var9 + " to " + var3);
                  }

                  InputStream var13 = var0.getInputStream(var6);
                  FileOutputStream var14 = new FileOutputStream(var3 + File.separator + var9);
                  byte[] var15 = new byte[512];

                  int var16;
                  while((var16 = var13.read(var15, 0, var15.length)) != -1) {
                     var14.write(var15, 0, var16);
                  }

                  var13.close();
                  var14.close();
               } catch (FileNotFoundException var22) {
                  var10 = Debug.getExceptionFileNotFoundForNativeLibDir(var1.getDisplayName());
                  throw new DeploymentException(var10, var22);
               } catch (IOException var23) {
                  var10 = Debug.getExceptionExceptionCreatingNativeLibDir(var1.getDisplayName(), var23.toString());
                  throw new DeploymentException(var10, var23);
               }
            }
         }
      } finally {
         SecurityManager.popSubject(var25);
      }

   }

   public static void updateClassFinder(GenericClassLoader var0, RarArchive var1, boolean var2, List var3) {
      ArrayList var4 = new ArrayList();
      RAClassFinder var5 = new RAClassFinder(var1);
      var4.add(var5);
      populateRootDirectoryClassFinder(var1.getVirtualJarFile(), var4);
      addClassFinderToClassloader(var0, var2, var4, var3);
   }

   public static void updateClassFinder(GenericClassLoader var0, VirtualJarFile var1, boolean var2, List var3) {
      ArrayList var4 = new ArrayList();
      RAClassFinder var5 = new RAClassFinder(var1);
      var4.add(var5);
      populateRootDirectoryClassFinder(var1, var4);
      addClassFinderToClassloader(var0, var2, var4, var3);
   }

   public static void populateRootDirectoryClassFinder(VirtualJarFile var0, List<ClassFinder> var1) {
      String var2;
      if (var0.isDirectory()) {
         File[] var3 = var0.getRootFiles();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var2 = var3[var4].toString();
            ClasspathClassFinder2 var5 = new ClasspathClassFinder2(var2);
            var1.add(var5);
         }
      } else {
         var2 = var0.getName();
         ClasspathClassFinder2 var6 = new ClasspathClassFinder2(var2);
         var1.add(var6);
      }

   }

   public static void addClassFinderToClassloader(GenericClassLoader var0, boolean var1, List<ClassFinder> var2, List var3) {
      GenericClassLoader var4 = null;
      if (var1) {
         var4 = AugmentableClassLoaderManager.getAugmentableSystemClassLoader();
      } else {
         var4 = var0;
      }

      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         ClassFinder var6 = (ClassFinder)var5.next();
         var4.addClassFinder(var6);
         var3.add(var6);
      }

   }

   protected static ConnectorModuleChangePackage enumerateChanges(RAInstanceManager var0, RAInfo var1, RAInfo var2) {
      ConnectorModuleChangePackage var3 = new ConnectorModuleChangePackage();
      var3.addChanges(enumerateAdminObjectChanges(var0, var1, var2));
      var3.addChanges(enumerateConnectionPoolChanges(var0, var1, var2));
      var3.addChange(enumerateRAPropsChanges(var0, var1, var2));
      return var3;
   }

   private static Properties getPoolParamPropertyChanges(OutboundInfo var0, OutboundInfo var1) {
      Properties var2 = new Properties();
      int var3 = var0.getInitialCapacity();
      int var4 = var1.getInitialCapacity();
      int var5 = var0.getMaxCapacity();
      int var6 = var1.getMaxCapacity();
      int var7 = var0.getCapacityIncrement();
      int var8 = var1.getCapacityIncrement();
      int var9 = var0.getShrinkFrequencySeconds();
      int var10 = var1.getShrinkFrequencySeconds();
      int var11 = var0.getInactiveConnectionTimeoutSeconds();
      int var12 = var1.getInactiveConnectionTimeoutSeconds();
      int var13 = var0.getHighestNumWaiters();
      int var14 = var1.getHighestNumWaiters();
      int var15 = var0.getHighestNumUnavailable();
      int var16 = var1.getHighestNumUnavailable();
      int var17 = var0.getConnectionCreationRetryFrequencySeconds();
      int var18 = var1.getConnectionCreationRetryFrequencySeconds();
      int var19 = var0.getConnectionReserveTimeoutSeconds();
      int var20 = var1.getConnectionReserveTimeoutSeconds();
      int var21 = var0.getTestFrequencySeconds();
      int var22 = var1.getTestFrequencySeconds();
      int var23 = var0.getProfileHarvestFrequencySeconds();
      int var24 = var1.getProfileHarvestFrequencySeconds();
      boolean var25 = var0.isShrinkingEnabled();
      boolean var26 = var1.isShrinkingEnabled();
      boolean var27 = var0.isTestConnectionsOnCreate();
      boolean var28 = var1.isTestConnectionsOnCreate();
      boolean var29 = var0.isTestConnectionsOnRelease();
      boolean var30 = var1.isTestConnectionsOnRelease();
      boolean var31 = var0.isTestConnectionsOnReserve();
      boolean var32 = var1.isTestConnectionsOnReserve();
      if (var3 != var4) {
         var2.setProperty("initialCapacity", String.valueOf(var4));
      }

      if (var5 != var6) {
         var2.setProperty("maxCapacity", String.valueOf(var6));
      }

      if (var7 != var8) {
         var2.setProperty("capacityIncrement", String.valueOf(var8));
      }

      if (var9 != var10) {
         var2.setProperty("shrinkFrequencySeconds", String.valueOf(var10));
      }

      if (var11 != var12) {
         var2.setProperty("inactiveResTimeoutSeconds", String.valueOf(var12));
      }

      if (var13 != var14) {
         var2.setProperty("maxWaiters", String.valueOf(var14));
      }

      if (var15 != var16) {
         var2.setProperty("maxUnavl", String.valueOf(var16));
      }

      if (var17 != var18) {
         var2.setProperty("resCreationRetrySeconds", String.valueOf(var18));
      }

      if (var19 != var20) {
         var2.setProperty("resvTimeoutSeconds", String.valueOf(var20));
      }

      if (var21 != var22) {
         var2.setProperty("testFrequencySeconds", String.valueOf(var22));
      }

      if (var23 != var24) {
         var2.setProperty("harvestFreqSecsonds", String.valueOf(var24));
      }

      if (var25 != var26) {
         var2.setProperty("shrinkEnabled", String.valueOf(var26));
      }

      if (var27 != var28) {
         var2.setProperty("testOnCreate", String.valueOf(var28));
      }

      if (var29 != var30) {
         var2.setProperty("testOnRelease", String.valueOf(var30));
      }

      if (var31 != var32) {
         var2.setProperty("testOnReserve", String.valueOf(var32));
      }

      return var2.size() > 0 ? var2 : null;
   }

   private static Properties getLoggingPropertyChanges(OutboundInfo var0, OutboundInfo var1) {
      Properties var2 = new Properties();
      int var3 = var0.getFileCount();
      int var4 = var1.getFileCount();
      int var5 = var0.getFileSizeLimit();
      int var6 = var1.getFileSizeLimit();
      int var7 = var0.getFileTimeSpan();
      int var8 = var1.getFileTimeSpan();
      String var9 = var0.getLogFileRotationDir();
      String var10 = var1.getLogFileRotationDir();
      String var11 = var0.getLogFilename();
      String var12 = var1.getLogFilename();
      boolean var13 = var0.isLoggingEnabled();
      boolean var14 = var1.isLoggingEnabled();
      boolean var15 = var0.isNumberOfFilesLimited();
      boolean var16 = var1.isNumberOfFilesLimited();
      boolean var17 = var0.isRotateLogOnStartup();
      boolean var18 = var1.isRotateLogOnStartup();
      String var19 = var0.getRotationTime();
      String var20 = var1.getRotationTime();
      String var21 = var0.getRotationType();
      String var22 = var1.getRotationType();
      if (var3 != var4) {
         var2.setProperty("FileCount", String.valueOf(var4));
      }

      if (var5 != var6) {
         var2.setProperty("FileSizeLimit", String.valueOf(var6));
      }

      if (var7 != var8) {
         var2.setProperty("FileTimeSpan", String.valueOf(var8));
      }

      if (!stringsMatch(var9, var10)) {
         var2.setProperty("LogFileRotationDir", var10);
      }

      if (!stringsMatch(var11, var12)) {
         var2.setProperty("LogFilename", var12);
      }

      if (var13 != var14) {
         var2.setProperty("LoggingEnabled", String.valueOf(var14));
      }

      if (var15 != var16) {
         var2.setProperty("NumberOfFilesLimited", String.valueOf(var16));
      }

      if (var17 != var18) {
         var2.setProperty("RotateLogOnStartup", String.valueOf(var18));
      }

      if (!stringsMatch(var19, var20)) {
         var2.setProperty("RotationTime", String.valueOf(var20));
      }

      if (!stringsMatch(var21, var22)) {
         var2.setProperty("RotationType", String.valueOf(var22));
      }

      return var2.size() > 0 ? var2 : null;
   }

   private static boolean stringsMatch(String var0, String var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   private static List<ConnectionPoolChangePackage> enumerateConnectionPoolChanges(RAInstanceManager var0, RAInfo var1, RAInfo var2) {
      ArrayList var3 = new ArrayList();
      List var4 = var1.getOutboundInfos();
      Iterator var5 = var4.iterator();

      OutboundInfo var8;
      while(var5.hasNext()) {
         ConnectionPoolChangePackage var6 = null;
         OutboundInfoImpl var7 = (OutboundInfoImpl)var5.next();
         var8 = var2.getOutboundInfo(var7.getKey());
         if (var8 != null) {
            Properties var9 = getPoolParamPropertyChanges(var7, var8);
            Properties var10 = getLoggingPropertyChanges(var7, var8);
            HashMap var11 = new HashMap();
            var6 = new ConnectionPoolChangePackage(var0, var8, var9, var10, var11, ConnectorModuleChangePackage.ChangeType.UPDATE);
         } else {
            var6 = new ConnectionPoolChangePackage(var0, var7, (Properties)null, (Properties)null, (Map)null, ConnectorModuleChangePackage.ChangeType.REMOVE);
         }

         if (var6 != null) {
            var3.add(var6);
         }
      }

      List var13 = var2.getOutboundInfos();
      Iterator var14 = var13.iterator();

      while(var14.hasNext()) {
         var8 = null;
         OutboundInfoImpl var16 = (OutboundInfoImpl)var14.next();
         OutboundInfo var12 = var1.getOutboundInfo(var16.getKey());
         if (var12 == null) {
            ConnectionPoolChangePackage var15 = new ConnectionPoolChangePackage(var0, var16, (Properties)null, (Properties)null, (Map)null, ConnectorModuleChangePackage.ChangeType.NEW);
            var3.add(var15);
         }
      }

      return var3;
   }

   private static List<AdminObjectChangePackage> enumerateAdminObjectChanges(RAInstanceManager var0, RAInfo var1, RAInfo var2) {
      ArrayList var3 = new ArrayList();
      List var4 = var2.getAdminObjs();
      Iterator var5 = var4.iterator();

      AdminObjInfo var8;
      while(var5.hasNext()) {
         AdminObjectChangePackage var6 = null;
         AdminObjInfo var7 = (AdminObjInfo)var5.next();
         var8 = var1.getAdminObject(var7.getJndiName());
         if (var8 == null) {
            var6 = new AdminObjectChangePackage(var0, var7, ConnectorModuleChangePackage.ChangeType.NEW, (Map)null);
            var3.add(var6);
         }
      }

      List var9 = var1.getAdminObjs();
      Iterator var10 = var9.iterator();

      while(var10.hasNext()) {
         var8 = (AdminObjInfo)var10.next();
         if (var2.getAdminObject(var8.getJndiName()) == null) {
            var3.add(new AdminObjectChangePackage(var0, var8, ConnectorModuleChangePackage.ChangeType.REMOVE, (Map)null));
         }
      }

      return var3;
   }

   private static RAPropsChangePackage enumerateRAPropsChanges(RAInstanceManager var0, RAInfo var1, RAInfo var2) {
      RAPropsChangePackage var3 = null;
      if (!stringsMatch(var1.getJndiName(), var2.getJndiName())) {
         var3 = new RAPropsChangePackage(var0, (Map)null);
         var3.setNewJNDIName(var2.getJndiName());
      }

      return var3;
   }

   public static boolean enumeratePropsChanges(Map<String, ConfigPropInfo> var0, Map<String, ConfigPropInfo> var1, Map<String, ConfigPropInfo> var2) {
      Iterator var3 = var0.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         ConfigPropInfo var5 = (ConfigPropInfo)var4.getValue();
         ConfigPropInfo var6 = (ConfigPropInfo)var1.get(var5.getName());
         if (var6 != null && !hasEqualValue(var5, var6)) {
            var2.put(var6.getName().toLowerCase(), var6);
         }
      }

      return !var2.isEmpty();
   }

   private static boolean hasEqualValue(ConfigPropInfo var0, ConfigPropInfo var1) {
      if (var0.getValue() == null) {
         return var1.getValue() == null;
      } else {
         return var0.getValue().equals(var1.getValue());
      }
   }
}
