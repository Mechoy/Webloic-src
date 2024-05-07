package weblogic.ejb.container.internal;

import weblogic.ejb.container.interfaces.Invokable;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;

public final class WSOMethodInvoker {
   private WSOMethodInvoker() {
   }

   public static Object invoke(BaseWSLocalObject var0, MethodDescriptor var1, Object[] var2, int var3) throws Throwable {
      Object var4 = null;
      boolean var5 = false;

      do {
         var0.__WL_business(var1);
         WLEnterpriseBean var6 = (WLEnterpriseBean)var0.__WL_getWrap().getBean();
         int var7 = var6.__WL_getMethodState();

         try {
            var6.__WL_setMethodState(131072);
            var4 = ((Invokable)var0).__WL_invoke(var6, var2, var3);
            var0.__WL_business_success();
         } catch (Throwable var15) {
            var0.__WL_business_fail(var15);
            var0.__WL_setException(var15);
            if (var4 == null) {
               var4 = getDefault(var1.getMethod().getReturnType());
            }
         } finally {
            var6.__WL_setMethodState(var7);
         }

         try {
            var5 = var0.__WL_postInvokeTxRetry();
         } catch (Throwable var14) {
            var0.__WL_setException(var14);
            throw var0.__WL_getException();
         }
      } while(var5);

      return var4;
   }

   private static Object getDefault(Class<?> var0) {
      if (var0.isPrimitive() && var0 != Void.TYPE) {
         if (var0 == Boolean.TYPE) {
            return Boolean.FALSE;
         } else if (var0 == Byte.TYPE) {
            return 0;
         } else if (var0 == Character.TYPE) {
            return '\u0000';
         } else if (var0 == Double.TYPE) {
            return 0.0;
         } else if (var0 == Float.TYPE) {
            return 0.0F;
         } else if (var0 == Integer.TYPE) {
            return 0;
         } else if (var0 == Long.TYPE) {
            return 0L;
         } else if (var0 == Short.TYPE) {
            return Short.valueOf((short)0);
         } else {
            throw new AssertionError("Unknown primitive type : " + var0);
         }
      } else {
         return null;
      }
   }
}
