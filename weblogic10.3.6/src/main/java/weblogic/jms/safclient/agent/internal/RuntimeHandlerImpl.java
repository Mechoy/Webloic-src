package weblogic.jms.safclient.agent.internal;

import weblogic.jms.forwarder.RuntimeHandler;

public final class RuntimeHandlerImpl implements RuntimeHandler {
   private String agentName;
   private String destinationName;
   private String remoteContextName;

   public RuntimeHandlerImpl(String var1, String var2, String var3) {
      this.agentName = var1;
      this.destinationName = var2;
      this.remoteContextName = var3;
   }

   public void disconnected(Exception var1) {
      System.out.println("Agent \"" + this.agentName + "\" lost the connection to " + this.remoteContextName + " while processing messages for " + this.destinationName);
      Object var2 = var1;

      for(int var3 = 0; var2 != null; var2 = ((Throwable)var2).getCause()) {
         System.out.println("Stack level " + var3++);
         ((Throwable)var2).printStackTrace();
      }

      System.out.println("disconnect stack trace finished");
   }

   public void connected() {
      System.out.println("Agent \"" + this.agentName + "\" got connected to " + this.remoteContextName + " while processing messages for " + this.destinationName);
   }
}
