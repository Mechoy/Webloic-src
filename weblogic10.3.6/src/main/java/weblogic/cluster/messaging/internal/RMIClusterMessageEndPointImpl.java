package weblogic.cluster.messaging.internal;

import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public class RMIClusterMessageEndPointImpl implements ClusterMessageEndPoint {
   private static final DebugCategory debugMessageEndPoint = Debug.getCategory("weblogic.cluster.leasing.MessageEndPoint");
   private static final boolean DEBUG = debugEnabled();
   private PingMessageListener listener;

   public ClusterResponse process(ClusterMessage var1) throws ClusterMessageProcessingException {
      if (var1.getMessageType() == 9) {
         if (DEBUG) {
            debug("received PING from " + var1.getSenderInformation());
         }

         this.invokeListener(var1.getSenderInformation());
         return null;
      } else {
         ClusterMessageReceiver var2 = ClusterMessageFactory.getInstance().getMessageReceiver(var1);
         if (DEBUG) {
            debug("dispatching " + var1 + " to " + var2);
         }

         if (var2 == null) {
            throw new ClusterMessageProcessingException("leasing is not ready!");
         } else {
            return var2.process(var1);
         }
      }
   }

   public void processOneWay(ClusterMessage var1) throws ClusterMessageProcessingException {
      if (var1.getMessageType() == 9) {
         if (DEBUG) {
            debug("received PING ONE_WAY from " + var1.getSenderInformation());
         }

         this.invokeListener(var1.getSenderInformation());
      } else {
         ClusterMessageReceiver var2 = ClusterMessageFactory.getInstance().getMessageReceiver(var1);
         if (DEBUG) {
            debug("dispatching ONE_WAY " + var1 + " to " + var2);
         }

         if (var2 == null) {
            throw new ClusterMessageProcessingException("leasing is not ready!");
         } else {
            var2.process(var1);
         }
      }
   }

   private static void debug(String var0) {
      DebugLogger.debug("[RMIClusterMessageEndPointImpl] " + var0);
   }

   public static RMIClusterMessageEndPointImpl getInstance() {
      return RMIClusterMessageEndPointImpl.Singleton.SINGLETON;
   }

   private static boolean debugEnabled() {
      return true;
   }

   public void registerPingMessageListener(PingMessageListener var1) {
      this.listener = var1;
   }

   private void invokeListener(final ServerInformation var1) {
      debug("invoking listener: " + this.listener + " for sender: " + var1.getServerName());
      if (this.listener != null) {
         final PingMessageListener var2 = this.listener;
         WorkAdapter var3 = new WorkAdapter() {
            public void run() {
               RMIClusterMessageEndPointImpl.debug(this.toString());
               var2.pingReceived(var1);
            }

            public String toString() {
               return var2 + ": Work Adapter ping Received from " + var1.getServerName();
            }
         };
         WorkManagerFactory.getInstance().getSystem().schedule(var3);
      }

   }

   private static class Singleton {
      private static final RMIClusterMessageEndPointImpl SINGLETON = new RMIClusterMessageEndPointImpl();
   }
}
