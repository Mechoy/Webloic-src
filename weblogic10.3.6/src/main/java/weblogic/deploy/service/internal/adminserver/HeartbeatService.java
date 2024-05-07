package weblogic.deploy.service.internal.adminserver;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.internal.transport.CommonMessageReceiver;
import weblogic.deploy.service.internal.transport.CommonMessageSender;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ConnectMonitorFactory;
import weblogic.rmi.extensions.ConnectEvent;
import weblogic.rmi.extensions.ConnectListener;
import weblogic.rmi.extensions.ConnectMonitor;
import weblogic.rmi.extensions.DisconnectEvent;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.ServerDisconnectEvent;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManagerFactory;

public final class HeartbeatService extends AbstractServerService implements TimerListener {
   private static final long HEARTBEAT_PERIOD_MILLIS = (long)(Integer.getInteger("weblogic.deployment.HeartbeatPeriodSeconds", 60) * 1000);
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private TimerManager timerManager;
   private CommonMessageSender messageSender = null;
   private CommonMessageReceiver messageReceiver = null;
   private final List connectedServers = new ArrayList();

   private CommonMessageSender getMessageSender() {
      if (this.messageSender == null) {
         this.messageSender = CommonMessageSender.getInstance();
      }

      return this.messageSender;
   }

   private CommonMessageReceiver getMessageReceiver() {
      if (this.messageReceiver == null) {
         this.messageReceiver = CommonMessageReceiver.getInstance();
      }

      return this.messageReceiver;
   }

   public void start() throws ServiceFailureException {
      if (ManagementService.getPropertyService(kernelId).isAdminServer()) {
         this.monitorServerConnections();
         if (this.timerManager == null) {
            if (isDebugEnabled()) {
               debug("Starting DeploymentService hearbeats");
            }

            this.timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.deploy.service.internal.adminserver.HeartbeatService", WorkManagerFactory.getInstance().getSystem());
            this.timerManager.scheduleAtFixedRate(this, 0L, HEARTBEAT_PERIOD_MILLIS);
         }
      }

      this.getMessageReceiver().setHeartbeatServiceInitialized();
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   public synchronized void halt() throws ServiceFailureException {
      if (this.timerManager != null) {
         if (isDebugEnabled()) {
            debug("Halting DeploymentService hearbeats");
         }

         this.timerManager.suspend();
      }

   }

   public void timerExpired(Timer var1) {
      this.createAndSendHeartbeatMessage();
   }

   private void createAndSendHeartbeatMessage() {
      this.createAndSendHeartbeatMessage(this.getConnectedServers());
   }

   private void createAndSendHeartbeatMessage(List var1) {
      if (isDebugEnabled()) {
         debug("Creating and sending out heartbeat to : " + var1);
      }

      this.getMessageSender().sendHeartbeatMsg(var1);
   }

   private List getConnectedServers() {
      synchronized(this.connectedServers) {
         return new ArrayList(this.connectedServers);
      }
   }

   private void monitorServerConnections() {
      ConnectMonitor var1 = ConnectMonitorFactory.getConnectMonitor();
      var1.addConnectDisconnectListener(this.createConnectListener(), this.createDisconnectListener());
   }

   private DisconnectListener createDisconnectListener() {
      return new DisconnectListener() {
         public void onDisconnect(DisconnectEvent var1) {
            String var2 = ((ServerDisconnectEvent)var1).getServerName();
            synchronized(HeartbeatService.this.connectedServers) {
               HeartbeatService.this.connectedServers.remove(var2);
            }

            if (HeartbeatService.isDebugEnabled()) {
               HeartbeatService.debug("[HeartbeatService] disconnected to server '" + var2 + "'");
            }

         }
      };
   }

   private ConnectListener createConnectListener() {
      return new ConnectListener() {
         public void onConnect(ConnectEvent var1) {
            String var2 = var1.getServerName();
            ArrayList var3 = new ArrayList();
            var3.add(var2);
            HeartbeatService.this.createAndSendHeartbeatMessage(var3);
            synchronized(HeartbeatService.this.connectedServers) {
               HeartbeatService.this.connectedServers.add(var2);
            }

            if (HeartbeatService.isDebugEnabled()) {
               HeartbeatService.debug("[HeartbeatService] connected to server '" + var2 + "'");
            }

         }
      };
   }

   private static final void debug(String var0) {
      Debug.serviceTransportDebug(var0);
   }

   private static final boolean isDebugEnabled() {
      return Debug.isServiceTransportDebugEnabled();
   }
}
