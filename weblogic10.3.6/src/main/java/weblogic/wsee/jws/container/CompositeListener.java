package weblogic.wsee.jws.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.wsee.message.WlMessageContext;

class CompositeListener implements ContainerListener {
   private static final long serialVersionUID = 6347626382878766457L;
   private List<ContainerListener> listeners = new ArrayList();

   void addListener(ContainerListener var1) {
      this.listeners.add(var1);
   }

   public void onAgeTimeout(long var1) throws Exception {
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ContainerListener var4 = (ContainerListener)var3.next();
         var4.onAgeTimeout(var1);
      }

   }

   public void onIdleTimeout(long var1) throws Exception {
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ContainerListener var4 = (ContainerListener)var3.next();
         var4.onIdleTimeout(var1);
      }

   }

   public void onAsyncFailure(String var1, Object[] var2) throws Exception {
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ContainerListener var4 = (ContainerListener)var3.next();
         var4.onAsyncFailure(var1, var2);
      }

   }

   public void preInvoke(WlMessageContext var1) throws Exception {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ContainerListener var3 = (ContainerListener)var2.next();
         var3.preInvoke(var1);
      }

   }

   public void postInvoke() throws Exception {
      Iterator var1 = this.listeners.iterator();

      while(var1.hasNext()) {
         ContainerListener var2 = (ContainerListener)var1.next();
         var2.postInvoke();
      }

   }

   public void onCreate() throws Exception {
      Iterator var1 = this.listeners.iterator();

      while(var1.hasNext()) {
         ContainerListener var2 = (ContainerListener)var1.next();
         var2.onCreate();
      }

   }

   public void onFinish(boolean var1) throws Exception {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ContainerListener var3 = (ContainerListener)var2.next();
         var3.onFinish(var1);
      }

   }

   public void onException(Exception var1, String var2, Object[] var3) throws Exception {
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         ContainerListener var5 = (ContainerListener)var4.next();
         var5.onException(var1, var2, var3);
      }

   }
}
