package weblogic.diagnostics.harvester.internal;

import java.beans.BeanInfo;
import java.lang.reflect.Method;
import weblogic.diagnostics.harvester.HarvesterRuntimeException;
import weblogic.utils.AssertionError;

public final class TreeBeanHarvestableDataProviderHelper {
   static Object supportService = null;
   private static final String SUPPORT_SERVICE_CLASS = "weblogic.management.mbeanservers.runtime.internal.DiagnosticSupportService";
   private static final Class SupportServiceClass = getClass("weblogic.management.mbeanservers.runtime.internal.DiagnosticSupportService");
   private static Method getBeanInfo = getDeclaredMethod("getBeanInfo", new Class[]{String.class});
   private static Method getObjectIdentifier = getDeclaredMethod("getObjectIdentifier", new Class[]{Object.class});
   private static Method getRegisteredObjectIdentifier = getDeclaredMethod("getRegisteredObjectIdentifier", new Class[]{Object.class});
   private static Method unregisterInstance = getDeclaredMethod("unregisterInstance", new Class[]{Object.class});
   private static Method typeNameForInstance = getDeclaredMethod("getInterfaceClassForObjectIdentifier", new Class[]{String.class});
   private static Method instanceForObjectIdentifier = getDeclaredMethod("getInstanceForObjectIdentifier", new Class[]{String.class});

   public static void setSupportService(Object var0) {
      if (supportService == null) {
         supportService = var0;
      }
   }

   public static BeanInfo getBeanInfo(String var0) {
      try {
         return (BeanInfo)getBeanInfo.invoke(supportService, var0);
      } catch (Exception var2) {
         throw new HarvesterRuntimeException(var2);
      }
   }

   public static String getObjectNameForBean(Object var0) {
      try {
         return (String)getObjectIdentifier.invoke(supportService, var0);
      } catch (Exception var2) {
         throw new HarvesterRuntimeException(var2);
      }
   }

   public static String getRegisteredObjectNameForBean(Object var0) {
      try {
         return (String)getRegisteredObjectIdentifier.invoke(supportService, var0);
      } catch (Exception var2) {
         throw new HarvesterRuntimeException(var2);
      }
   }

   static void unregisterInstance(Object var0) {
      try {
         unregisterInstance.invoke(supportService, var0);
      } catch (Exception var2) {
         throw new HarvesterRuntimeException(var2);
      }
   }

   public static String getTypeNameForInstance(String var0) {
      try {
         return (String)typeNameForInstance.invoke(supportService, var0);
      } catch (Exception var2) {
         throw new HarvesterRuntimeException(var2);
      }
   }

   public static Object getInstanceForObjectIdentifier(String var0) {
      try {
         return instanceForObjectIdentifier.invoke(supportService, var0);
      } catch (Exception var2) {
         throw new HarvesterRuntimeException(var2);
      }
   }

   private static Method getDeclaredMethod(String var0, Class[] var1) {
      try {
         return SupportServiceClass.getDeclaredMethod(var0, var1);
      } catch (NoSuchMethodException var3) {
         throw new AssertionError(var3);
      }
   }

   private static Class getClass(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }
}
