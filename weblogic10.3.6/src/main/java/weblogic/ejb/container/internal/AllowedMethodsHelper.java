package weblogic.ejb.container.internal;

import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.kernel.ThreadLocalStack;

public final class AllowedMethodsHelper {
   private static final ThreadLocalStack threadStorage = new ThreadLocalStack(true);
   private static final ThreadLocalStack threadStorageForMethodInvocationState = new ThreadLocalStack(true);

   public static void pushBean(WLEnterpriseBean var0) {
      threadStorage.push(var0);
   }

   public static void popBean() {
      threadStorage.pop();
   }

   public static WLEnterpriseBean getBean() {
      return (WLEnterpriseBean)threadStorage.peek();
   }

   public static void pushMethodInvocationState(Object var0) {
      threadStorageForMethodInvocationState.push(var0);
   }

   public static void popMethodInvocationState() {
      threadStorageForMethodInvocationState.pop();
   }

   public static Object getMethodInvocationState() {
      return threadStorageForMethodInvocationState.peek();
   }
}
