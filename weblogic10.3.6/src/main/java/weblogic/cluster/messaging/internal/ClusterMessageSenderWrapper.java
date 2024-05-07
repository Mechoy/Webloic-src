package weblogic.cluster.messaging.internal;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.work.WorkManagerFactory;

public final class ClusterMessageSenderWrapper implements ClusterMessageSender {
   private static final DebugCategory debugClusterMessaging = Debug.getCategory("weblogic.cluster.leasing.ClusterMessaging");
   private static final boolean DEBUG = debugEnabled();
   private ClusterMessageSender delegate;
   private ArrayList listeners = new ArrayList();

   ClusterMessageSenderWrapper(ClusterMessageSender var1) {
      this.delegate = var1;
   }

   synchronized void addMessageDeliveryFailureListener(MessageDeliveryFailureListener var1) {
      this.listeners.add(var1);
   }

   synchronized void removeMessageDeliveryFailureListener(MessageDeliveryFailureListener var1) {
      this.listeners.remove(var1);
   }

   public ClusterResponse[] send(ClusterMessage var1, ServerInformation[] var2) throws ClusterMessageProcessingException {
      try {
         return this.delegate.send(var1, var2);
      } catch (ClusterMessageProcessingException var4) {
         this.invokeListeners(var4);
         throw var4;
      }
   }

   public ClusterResponse send(ClusterMessage var1, ServerInformation var2) throws RemoteException {
      try {
         return this.delegate.send(var1, var2);
      } catch (RemoteException var4) {
         this.invokeListeners(var2.getServerName(), var4);
         throw var4;
      }
   }

   public ClusterResponse send(ClusterMessage var1, String var2) throws RemoteException {
      try {
         return this.delegate.send(var1, var2);
      } catch (RemoteException var4) {
         this.invokeListeners(var2, var4);
         throw var4;
      }
   }

   private synchronized void invokeListeners(ClusterMessageProcessingException var1) {
      if (this.listeners != null && this.listeners.size() != 0) {
         HashMap var2 = var1.getFailedServers();
         if (var2 != null && var2.size() != 0) {
            Iterator var3 = var2.keySet().iterator();

            while(var3.hasNext()) {
               ServerInformation var4 = (ServerInformation)var3.next();
               this.invokeListeners(var4.getServerName(), (RemoteException)var2.get(var4));
            }

         }
      }
   }

   private synchronized void invokeListeners(String var1, RemoteException var2) {
      if (this.listeners != null && this.listeners.size() != 0) {
         MessageDeliveryFailureListener[] var3 = new MessageDeliveryFailureListener[this.listeners.size()];
         this.listeners.toArray(var3);
         WorkManagerFactory.getInstance().getSystem().schedule(new ListenerInvocationRunnable(var3, var1, var2));
      }
   }

   private static boolean debugEnabled() {
      return debugClusterMessaging.isEnabled() || DebugLogger.isDebugEnabled();
   }

   private static void debug(String var0) {
      DebugLogger.debug("[MessageSenderWrapper] " + var0);
   }

   private static class ListenerInvocationRunnable implements Runnable {
      private final MessageDeliveryFailureListener[] listeners;
      private final String serverName;
      private final RemoteException re;

      ListenerInvocationRunnable(MessageDeliveryFailureListener[] var1, String var2, RemoteException var3) {
         this.listeners = var1;
         this.serverName = var2;
         this.re = var3;
      }

      public void run() {
         for(int var1 = 0; var1 < this.listeners.length; ++var1) {
            MessageDeliveryFailureListener var2 = this.listeners[var1];
            if (ClusterMessageSenderWrapper.DEBUG) {
               ClusterMessageSenderWrapper.debug("invoking onMessageDeliveryFailure on " + this.serverName);
            }

            var2.onMessageDeliveryFailure(this.serverName, this.re);
         }

      }
   }
}
