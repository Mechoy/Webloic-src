package weblogic.wsee.jws.container;

import java.lang.reflect.Method;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Verbose;

class ContextEventListener implements ContainerListener {
   private static final long serialVersionUID = 2088692834536863957L;
   private static final boolean verbose = Verbose.isVerbose(ContextEventListener.class);
   private String contextFieldName = null;
   private Object targetJWS = null;

   ContextEventListener(String var1, Object var2) {
      this.contextFieldName = var1;
      this.targetJWS = var2;
   }

   public void onCreate() {
      this.executeMethod("onCreate", new Class[0], new Object[0], false);
   }

   public void onFinish(boolean var1) {
      this.executeMethod("onFinish", new Class[]{Boolean.TYPE}, new Object[]{var1}, true);
   }

   public void onException(Exception var1, String var2, Object[] var3) {
      this.executeMethod("onException", new Class[]{Exception.class, String.class, Object[].class}, new Object[]{var1, var2, var3}, false);
   }

   public void preInvoke(WlMessageContext var1) throws Exception {
   }

   public void postInvoke() throws Exception {
   }

   public void onAgeTimeout(long var1) throws Exception {
      this.executeMethod("onAgeTimeout", new Class[]{Long.TYPE}, new Object[]{var1}, false);
   }

   public void onIdleTimeout(long var1) throws Exception {
      this.executeMethod("onIdleTimeout", new Class[]{Long.TYPE}, new Object[]{var1}, false);
   }

   public void onAsyncFailure(String var1, Object[] var2) throws Exception {
      this.executeMethod("onAsyncFailure", new Class[]{String.class, Object[].class}, new Object[]{var1, var2}, false);
   }

   private void executeMethod(String var1, Class[] var2, Object[] var3, boolean var4) throws InvokeException {
      String var5 = this.contextFieldName + "_" + var1;

      try {
         Method var6 = this.targetJWS.getClass().getMethod(var5, var2);
         var6.invoke(this.targetJWS, var3);
      } catch (NoSuchMethodException var7) {
         if (verbose) {
            Verbose.log((Object)(" not found on " + this.targetJWS.getClass().getName()));
         }
      } catch (Exception var8) {
         if (var4) {
            throw new InvokeException("Unable to Fire " + var1 + " event", var8);
         }

         if (verbose) {
            Verbose.log("Exception executing " + var5 + this.targetJWS.getClass().getName(), var8);
         }
      }

   }
}
