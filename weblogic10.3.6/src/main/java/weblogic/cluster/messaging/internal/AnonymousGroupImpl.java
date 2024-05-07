package weblogic.cluster.messaging.internal;

import java.io.IOException;

final class AnonymousGroupImpl extends GroupImpl {
   public void start() {
   }

   public void send(Message var1) {
      GroupMember[] var2 = this.getMembers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         GroupMember var4 = var2[var3];
         if (!var1.getServerName().equals(var4.getConfiguration().getServerName())) {
            try {
               var4.send(var1);
               if (DEBUG) {
                  this.debug(var1 + " send ok to " + var4);
               }
            } catch (IOException var6) {
               if (DEBUG) {
                  var6.printStackTrace();
               }

               this.removeFromRunningSet(var4);
               if (DEBUG) {
                  this.debug(var1 + " send failed to " + var4);
               }
            }
         }
      }

   }

   protected void performLeaderActions(Message var1) {
      throw new AssertionError("local server can never perform leader actions on anonymous group !");
   }

   public void forward(Message var1, Connection var2) {
      throw new AssertionError("forward cannot be called on anonymous groups !");
   }

   protected synchronized void startDiscoveryIfNeeded() {
   }

   protected void debug(String var1) {
      Environment.getLogService().debug("[AnonymousGroup] [" + this.toString() + "] " + var1);
   }
}
