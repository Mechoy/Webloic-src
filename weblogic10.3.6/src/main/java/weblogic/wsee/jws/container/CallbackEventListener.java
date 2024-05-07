package weblogic.wsee.jws.container;

import weblogic.wsee.jws.JwsContext;
import weblogic.wsee.message.WlMessageContext;

class CallbackEventListener implements ContainerListener {
   private JwsContext.Callback targetJWS = null;

   CallbackEventListener(JwsContext.Callback var1) {
      this.targetJWS = var1;
   }

   public void onCreate() throws Exception {
      this.targetJWS.onCreate();
   }

   public void onFinish(boolean var1) throws Exception {
      this.targetJWS.onFinish(var1);
   }

   public void onAgeTimeout(long var1) throws Exception {
      this.targetJWS.onAgeTimeout(var1);
   }

   public void onIdleTimeout(long var1) throws Exception {
      this.targetJWS.onIdleTimeout(var1);
   }

   public void onException(Exception var1, String var2, Object[] var3) throws Exception {
      this.targetJWS.onException(var1, var2, var3);
   }

   public void onAsyncFailure(String var1, Object[] var2) throws Exception {
      this.targetJWS.onAsyncFailure(var1, var2);
   }

   public void preInvoke(WlMessageContext var1) throws Exception {
   }

   public void postInvoke() throws Exception {
   }
}
