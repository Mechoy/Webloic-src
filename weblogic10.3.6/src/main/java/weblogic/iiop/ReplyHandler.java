package weblogic.iiop;

import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class ReplyHandler extends WorkAdapter {
   private static final boolean DEBUG = false;
   private final EndPoint endpoint;
   private final Message m;

   static void p(String var0) {
      System.err.println("<ReplyHandler>: " + var0);
   }

   public ReplyHandler(EndPoint var1, Message var2) {
      this.endpoint = var1;
      this.m = var2;
      WorkManagerFactory.getInstance().getSystem().schedule(this);
   }

   public void run() {
      try {
         IIOPOutputStream var1 = this.m.getOutputStream();
         this.endpoint.send(var1);
         var1.close();
      } catch (Throwable var2) {
         this.endpoint.handleProtocolException(this.m, var2);
      }

   }
}
