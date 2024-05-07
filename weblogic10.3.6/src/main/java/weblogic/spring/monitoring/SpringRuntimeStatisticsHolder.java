package weblogic.spring.monitoring;

import java.util.concurrent.ConcurrentHashMap;

public class SpringRuntimeStatisticsHolder {
   private static ConcurrentHashMap<Object, SpringApplicationContextRuntimeMBeanImpl> globalSpringApplicationContextRuntimeMBeans = new ConcurrentHashMap();
   private static ConcurrentHashMap<Object, SpringTransactionManagerRuntimeMBeanImpl> globalSpringTransactionManagerRuntimeMBeans = new ConcurrentHashMap();

   public static SpringApplicationContextRuntimeMBeanImpl getGlobalSpringApplicationContextRuntimeMBeanImpl(Object var0) {
      return var0 == null ? null : (SpringApplicationContextRuntimeMBeanImpl)globalSpringApplicationContextRuntimeMBeans.get(var0);
   }

   public static SpringTransactionManagerRuntimeMBeanImpl getGlobalSpringTransactionManagerRuntimeMBeanImpl(Object var0) {
      return var0 == null ? null : (SpringTransactionManagerRuntimeMBeanImpl)globalSpringTransactionManagerRuntimeMBeans.get(var0);
   }

   public static void putGlobalSpringApplicationContextRuntimeMBeanImpl(Object var0, SpringApplicationContextRuntimeMBeanImpl var1) {
      globalSpringApplicationContextRuntimeMBeans.put(var0, var1);
   }

   public static void putGlobalSpringTransactionManagerRuntimeMBeanImpl(Object var0, SpringTransactionManagerRuntimeMBeanImpl var1) {
      globalSpringTransactionManagerRuntimeMBeans.put(var0, var1);
   }

   public static SpringApplicationContextRuntimeMBeanImpl removeGlobalSpringApplicationContextRuntimeMBeanImpl(Object var0) {
      return var0 == null ? null : (SpringApplicationContextRuntimeMBeanImpl)globalSpringApplicationContextRuntimeMBeans.remove(var0);
   }

   public static SpringTransactionManagerRuntimeMBeanImpl removeGlobalSpringTransactionManagerRuntimeMBeanImpl(Object var0) {
      return var0 == null ? null : (SpringTransactionManagerRuntimeMBeanImpl)globalSpringTransactionManagerRuntimeMBeans.remove(var0);
   }

   public static ConcurrentHashMap getGlobalSpringTransactionManagerRuntimeMBeanMap() {
      return globalSpringTransactionManagerRuntimeMBeans;
   }
}
