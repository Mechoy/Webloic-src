package weblogic.deploy.service.internal.transport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.management.deploy.internal.DeploymentManagerLogger;
import weblogic.protocol.ConnectMonitorFactory;
import weblogic.rmi.extensions.ConnectEvent;
import weblogic.rmi.extensions.ConnectListener;
import weblogic.rmi.extensions.ConnectMonitor;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.ServerDisconnectEvent;
import weblogic.work.WorkManagerFactory;

public class ServerDisconnectManager {
   private static final long DISCONNECT_TIMEOUT = (long)Integer.getInteger("weblogic.deployment.serverDisconnectTimeout", 0) * 1000L;
   private ServerConnectDisconnectListenerImpl connectDisconnectListener;

   public static ServerDisconnectManager getInstance() {
      return ServerDisconnectManager.Maker.SINGLETON;
   }

   private ServerDisconnectManager() {
      this.connectDisconnectListener = null;
   }

   public void initialize() {
      this.connectDisconnectListener = new ServerConnectDisconnectListenerImpl();
      ConnectMonitor var1 = ConnectMonitorFactory.getConnectMonitor();
      var1.addConnectDisconnectListener(this.connectDisconnectListener, this.connectDisconnectListener);
   }

   public ServerDisconnectListener findOrCreateDisconnectListener(String var1) {
      ServerDisconnectListener var2 = this.findDisconnectListener(var1);
      if (var2 == null) {
         synchronized(this) {
            var2 = this.findDisconnectListener(var1);
            if (var2 == null) {
               var2 = this.createListener(var1);
               this.connectDisconnectListener.registerListener(var1, var2);
               return var2;
            }
         }
      }

      return var2;
   }

   public ServerDisconnectListener findDisconnectListener(String var1) {
      return this.connectDisconnectListener.getRegisteredListener(var1);
   }

   public void removeDisconnectListener(String var1) {
      this.connectDisconnectListener.unregisterListener(var1);
   }

   public void removeAll() {
      this.connectDisconnectListener.unregisterAll();
   }

   private ServerDisconnectListener createListener(String var1) {
      return new ServerDisconnectListenerImpl(var1);
   }

   // $FF: synthetic method
   ServerDisconnectManager(Object var1) {
      this();
   }

   private class ServerConnectDisconnectListenerImpl implements DisconnectListener, ConnectListener {
      private Map disconnectListeners;

      private ServerConnectDisconnectListenerImpl() {
         this.disconnectListeners = new HashMap();
      }

      public void onDisconnect(DisconnectEvent var1) {
         if (var1 instanceof ServerDisconnectEvent) {
            String var2 = ((ServerDisconnectEvent)var1).getServerName();
            this.handleOnDisconnectEvent(var2, var1);
         }
      }

      public void onConnect(ConnectEvent var1) {
         String var2 = var1.getServerName();
         this.handleOnConnectEvent(var2, var1);
      }

      private void handleOnConnectEvent(final String var1, ConnectEvent var2) {
         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               ServerDisconnectListener var1x = ServerConnectDisconnectListenerImpl.this.getRegisteredListener(var1);
               if (var1x != null) {
                  ((ServerDisconnectListenerImpl)var1x).setConnected(true);
               } else {
                  var1x = ServerDisconnectManager.this.createListener(var1);
                  ServerConnectDisconnectListenerImpl.this.registerListener(var1, var1x);
               }

            }
         });
      }

      private void handleOnDisconnectEvent(final String var1, final DisconnectEvent var2) {
         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               ServerDisconnectListener var1x = ServerConnectDisconnectListenerImpl.this.getRegisteredListener(var1);
               if (var1x != null) {
                  ((ServerDisconnectListenerImpl)var1x).setConnected(false);
                  var1x.onDisconnect(var2);
               }

            }
         });
      }

      private void unregisterAll() {
         synchronized(this) {
            this.disconnectListeners.clear();
         }
      }

      private void registerListener(String var1, ServerDisconnectListener var2) {
         synchronized(this) {
            this.disconnectListeners.put(var1, var2);
         }
      }

      private ServerDisconnectListener getRegisteredListener(String var1) {
         synchronized(this) {
            return (ServerDisconnectListener)this.disconnectListeners.get(var1);
         }
      }

      private void unregisterListener(String var1) {
         synchronized(this) {
            this.disconnectListeners.remove(var1);
         }
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString()).append("(");
         var1.append("disconnectListeners=").append(this.disconnectListeners);
         var1.append(")");
         return var1.toString();
      }

      // $FF: synthetic method
      ServerConnectDisconnectListenerImpl(Object var2) {
         this();
      }
   }

   private class ServerDisconnectListenerImpl implements ServerDisconnectListener {
      private String serverName;
      private boolean connected;
      private ArrayList allListeners;

      private ServerDisconnectListenerImpl(String var2) {
         this.serverName = null;
         this.connected = true;
         this.allListeners = new ArrayList();
         this.serverName = var2;
      }

      public void registerListener(DisconnectListener var1) {
         synchronized(this.allListeners) {
            this.allListeners.add(var1);
         }
      }

      public void unregisterListener(DisconnectListener var1) {
         synchronized(this.allListeners) {
            this.allListeners.remove(var1);
         }
      }

      public void onDisconnect(DisconnectEvent var1) {
         if (!this.allListeners.isEmpty()) {
            if (!this.isReconnected()) {
               Iterator var2 = ((ArrayList)this.allListeners.clone()).iterator();

               while(var2.hasNext()) {
                  DisconnectListener var3 = (DisconnectListener)var2.next();

                  try {
                     var3.onDisconnect(var1);
                  } catch (Throwable var5) {
                     DeploymentManagerLogger.logDisconnectListenerError(var3.toString(), var5);
                  }
               }

               this.allListeners.clear();
            }
         }
      }

      protected synchronized void setConnected(boolean var1) {
         this.connected = var1;
         this.notify();
      }

      protected synchronized boolean isConnected() {
         return this.connected;
      }

      protected synchronized boolean isReconnected() {
         long var1 = ServerDisconnectManager.DISCONNECT_TIMEOUT;
         long var5 = System.currentTimeMillis() + var1;

         while(true) {
            long var7 = var5 - System.currentTimeMillis();
            if (var1 == 0L || this.connected || var7 <= 0L) {
               return this.connected;
            }

            long var9 = var7 <= 5000L ? var7 : 5000L;

            try {
               this.wait(var9);
            } catch (InterruptedException var12) {
            }
         }
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(super.toString()).append("(");
         var1.append("Server = ").append(this.serverName).append(", ");
         var1.append("listeners = ").append(this.allListeners).append(")");
         return var1.toString();
      }

      // $FF: synthetic method
      ServerDisconnectListenerImpl(String var2, Object var3) {
         this(var2);
      }
   }

   static class Maker {
      private static final ServerDisconnectManager SINGLETON = new ServerDisconnectManager();
   }
}
