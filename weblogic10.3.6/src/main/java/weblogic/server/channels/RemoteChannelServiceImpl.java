package weblogic.server.channels;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.naming.ConfigurationException;
import javax.naming.NamingException;
import javax.naming.ServiceUnavailableException;
import weblogic.jndi.Environment;
import weblogic.kernel.KernelStatus;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ChannelList;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.URLManager;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rmi.extensions.ConnectEvent;
import weblogic.rmi.extensions.ConnectListener;
import weblogic.rmi.extensions.ConnectMonitor;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.ServerDisconnectEvent;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServerLogger;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

public final class RemoteChannelServiceImpl implements RemoteChannelService, ConnectMonitor {
   private static HashSet connectListeners = new HashSet();
   private static final boolean DEBUG = false;
   private static RemoteChannelService domainGateway;
   private static boolean shutdown = false;
   private static volatile int registeringNewServer = 0;
   private static HashMap connectedServers = new HashMap();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private RemoteChannelServiceImpl() {
   }

   public static synchronized RemoteChannelService getDomainGateway() {
      return domainGateway;
   }

   public static ConnectMonitor getInstance() {
      if (!KernelStatus.isServer()) {
         throw new UnsupportedOperationException("ConnectMonitor not supported in a client");
      } else {
         return RemoteChannelServiceImpl.ConnectSingleton.SINGLETON;
      }
   }

   public static synchronized void unregister() {
      shutdown = true;
   }

   private static synchronized boolean isShutdown() {
      return shutdown;
   }

   public static void registerForever(Environment var0) throws NamingException {
      ArrayList var1 = new ArrayList();
      var1.add(var0);
      registerForever((List)var1);
   }

   public static void registerForever(final List<Environment> var0) throws NamingException {
      registerInternal(var0, new DisconnectListener() {
         public void onDisconnect(DisconnectEvent var1) {
            if (var1 instanceof ServerDisconnectEvent) {
               ServerLogger.logServerDisconnect(((ServerDisconnectEvent)var1).getServerName());
            }

            TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(new TimerListenerImpl(var0, this), (long)(ManagementService.getRuntimeAccess(RemoteChannelServiceImpl.kernelId).getServer().getAdminReconnectIntervalSeconds() * 1000));
         }
      });
   }

   private static synchronized void registerInternal(List<Environment> var0, DisconnectListener var1) throws NamingException {
      Object var2 = null;
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         Environment var4 = (Environment)var3.next();

         try {
            RemoteChannelService var5 = (RemoteChannelService)PortableRemoteObject.narrow(var4.getInitialReference(RemoteChannelServiceImpl.class), RemoteChannelService.class);
            if (var5 == RemoteChannelServiceImpl.ConnectSingleton.SINGLETON) {
               throw new ConfigurationException("Cannot register for disconnect events on local server");
            }

            var5.getAdministrationURL();
            String var6 = var5.registerServer(LocalServerIdentity.getIdentity().getServerName(), ServerChannelManager.getLocalChannelsForExport());
            RemoteChannelServiceImpl.ConnectSingleton.SINGLETON.invokeListeners(var6, (ChannelList)null);
            ServerLogger.logServerConnect(var6);
            domainGateway = var5;

            try {
               DisconnectMonitorListImpl.getDisconnectMonitor().addDisconnectListener(var5, var1);
               return;
            } catch (Exception var8) {
               throw new AssertionError(var8);
            }
         } catch (ConfigurationException var9) {
            var2 = var9;
         } catch (ServiceUnavailableException var10) {
            var2 = var10;
         } catch (NamingException var11) {
         } catch (RemoteException var12) {
         }
      }

      if (var2 != null) {
         throw var2;
      } else {
         var1.onDisconnect((DisconnectEvent)null);
      }
   }

   public static void retrieveRemoteChannels(ServerIdentity var0) throws RemoteException {
      if (getDomainGateway() != null && var0.getDomainName().equals(LocalServerIdentity.getIdentity().getDomainName())) {
         getDomainGateway().getChannelList(var0);
      }

   }

   public ServerChannel getServerChannel(String var1) {
      return ChannelService.findLocalServerChannel(var1);
   }

   public String getDefaultURL() {
      return ChannelHelper.getDefaultURL();
   }

   public String getAdministrationURL() {
      return ChannelHelper.getLocalAdministrationURL();
   }

   public String getURL(String var1) {
      Protocol var2 = ProtocolManager.getProtocolByName(var1);
      return ChannelHelper.getURL(var2);
   }

   public ServerIdentity getServerIdentity() {
      return LocalServerIdentity.getIdentity();
   }

   public synchronized String registerServer(String var1, ChannelList var2) {
      ++registeringNewServer;
      ServerLogger.logServerConnect(var1);
      this.invokeListeners(var1, var2);
      connectedServers.put(var1, var2);
      return this.getServerIdentity().getServerName();
   }

   public void updateServer(String var1, ChannelList var2) {
   }

   private void invokeListeners(final String var1, final ChannelList var2) {
      WorkManagerFactory.getInstance().getSystem().schedule(new WorkAdapter() {
         private final long time = System.currentTimeMillis();
         private final String serverName = var2 != null ? var2.getIdentity().getServerName() : var1;

         public void run() {
            try {
               HashSet var1x = null;
               synchronized(RemoteChannelServiceImpl.connectListeners) {
                  var1x = (HashSet)RemoteChannelServiceImpl.connectListeners.clone();
               }

               if (var1x != null) {
                  Iterator var2x = var1x.iterator();

                  while(var2x.hasNext()) {
                     ConnectListener var3 = (ConnectListener)var2x.next();
                     var3.onConnect(new ConnectEvent() {
                        public String getServerName() {
                           return serverName;
                        }

                        public long getTime() {
                           return time;
                        }
                     });
                     if (var2 != null && var3 instanceof DisconnectListener) {
                        try {
                           DisconnectMonitorListImpl.getDisconnectMonitor().addDisconnectListener(((ChannelListImpl)var2).getChannelService(), (DisconnectListener)var3);
                        } catch (Exception var12) {
                           ServerLogger.logDisconnectRegistrationFailed(var12);
                        }
                     }
                  }

                  if (var2 != null) {
                     try {
                        DisconnectMonitorListImpl.getDisconnectMonitor().addDisconnectListener(((ChannelListImpl)var2).getChannelService(), new DisconnectListener() {
                           public void onDisconnect(DisconnectEvent var1x) {
                              if (var1x instanceof ServerDisconnectEvent) {
                                 ServerLogger.logServerDisconnect(((ServerDisconnectEvent)var1x).getServerName());
                              } else {
                                 ServerLogger.logServerDisconnect("<unknown>");
                              }

                           }
                        });
                     } catch (Exception var11) {
                        ServerLogger.logDisconnectRegistrationFailed(var11);
                     }
                  }
               }
            } finally {
               RemoteChannelServiceImpl.registeringNewServer--;
            }

         }
      });
   }

   public ChannelList getChannelList(ServerIdentity var1) {
      return new ChannelListImpl(var1);
   }

   public void removeChannelList(ServerIdentity var1) {
      ChannelService.removeChannelEntries(var1);
   }

   public String[] getConnectedServers() {
      return URLManager.getConnectedServers();
   }

   public void addConnectListener(ConnectListener var1) {
      synchronized(connectListeners) {
         connectListeners.add(var1);
      }
   }

   public void removeConnectListener(ConnectListener var1) {
      synchronized(connectListeners) {
         connectListeners.remove(var1);
      }
   }

   public synchronized void addConnectDisconnectListener(ConnectListener var1, DisconnectListener var2) {
      while(registeringNewServer != 0) {
         try {
            this.wait(1000L);
         } catch (InterruptedException var8) {
         }
      }

      this.addConnectListener(new ConnectDisconnectListener(var1, var2));
      Iterator var3 = connectedServers.keySet().iterator();
      Iterator var4 = connectedServers.values().iterator();

      while(var3.hasNext()) {
         String var5 = (String)var3.next();
         ChannelListImpl var6 = (ChannelListImpl)var4.next();
         if (URLManager.isConnected(var5)) {
            try {
               DisconnectMonitorListImpl.getDisconnectMonitor().addDisconnectListener(var6.getChannelService(), var2);
            } catch (Exception var9) {
               ServerLogger.logDisconnectRegistrationFailed(var9);
            }
         }
      }

   }

   private static final void p(String var0) {
      System.out.println("<RemoteChannelServiceImpl>: " + var0);
   }

   // $FF: synthetic method
   RemoteChannelServiceImpl(Object var1) {
      this();
   }

   private static class ConnectDisconnectListener implements ConnectListener, DisconnectListener {
      private ConnectListener conlisten;
      private DisconnectListener dislisten;

      private ConnectDisconnectListener(ConnectListener var1, DisconnectListener var2) {
         this.conlisten = var1;
         this.dislisten = var2;
      }

      public int hashCode() {
         return this.conlisten.hashCode();
      }

      public boolean equals(Object var1) {
         try {
            return ((ConnectDisconnectListener)var1).conlisten == this.conlisten && ((ConnectDisconnectListener)var1).dislisten == this.dislisten;
         } catch (ClassCastException var3) {
            return false;
         }
      }

      public void onConnect(ConnectEvent var1) {
         String var2 = var1.getServerName();
         this.conlisten.onConnect(var1);
      }

      public void onDisconnect(DisconnectEvent var1) {
         RemoteChannelServiceImpl.getInstance().addConnectListener(this);
         this.dislisten.onDisconnect(var1);
      }

      // $FF: synthetic method
      ConnectDisconnectListener(ConnectListener var1, DisconnectListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class TimerListenerImpl implements TimerListener {
      private DisconnectListener listener;
      private List<Environment> envList;

      private TimerListenerImpl(List<Environment> var1, DisconnectListener var2) {
         this.listener = var2;
         this.envList = var1;
      }

      public void timerExpired(Timer var1) {
         if (!RemoteChannelServiceImpl.isShutdown()) {
            try {
               RemoteChannelServiceImpl.registerInternal(this.envList, this.listener);
            } catch (NamingException var3) {
               throw new AssertionError(var3);
            }
         }
      }

      // $FF: synthetic method
      TimerListenerImpl(List var1, DisconnectListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class ConnectSingleton {
      private static final RemoteChannelServiceImpl SINGLETON = new RemoteChannelServiceImpl();
   }
}
