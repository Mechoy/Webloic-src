package weblogic.cluster.messaging.internal.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import weblogic.cluster.FragmentSocket;
import weblogic.cluster.messaging.internal.Environment;
import weblogic.cluster.messaging.internal.Message;
import weblogic.cluster.messaging.internal.MessageListener;
import weblogic.cluster.messaging.internal.MessageUtils;
import weblogic.cluster.messaging.internal.ServerConfigurationInformation;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ClusterRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class UnicastFragmentSocket implements FragmentSocket, MessageListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean DEBUG;
   private boolean blocked = true;
   private ArrayList blockedPackets = new ArrayList();
   private int fragmentsSentCount = 0;
   private int fragmentsReceivedCount = 0;
   private ArrayList receivedMessages = new ArrayList();
   private boolean shutdown;

   public UnicastFragmentSocket() {
      if (!Environment.isInitialized()) {
         Environment.setLogService(LogServiceImpl.getInstance());
         Environment.initialize(ConfiguredServersMonitorImpl.getInstance(), ConnectionManagerImpl.getInstance(), PropertyServiceImpl.getInstance());
         this.registerRuntime();
      }

      Environment.getGroupManager().setMessageListener(this);
      if (DEBUG) {
         this.debug("initialized UnicastFragmentSocket !");
      }

   }

   private void registerRuntime() {
      try {
         ClusterRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getClusterRuntime();
         new UnicastMessagingRuntimeMBeanImpl(var1);
      } catch (ManagementException var2) {
         if (DEBUG) {
            var2.printStackTrace();
         }
      }

   }

   private void debug(String var1) {
      Environment.getLogService().debug("[UnicastFragmentSocket] " + var1);
   }

   public synchronized void start() throws IOException {
      this.shutdown = false;
      this.blocked = false;
      Iterator var1 = this.blockedPackets.iterator();

      while(var1.hasNext()) {
         Packet var2 = (Packet)var1.next();
         this.send(var2.buffer, var2.length);
      }

      this.blockedPackets.clear();
   }

   public void send(byte[] var1, int var2) throws IOException {
      if (this.blocked) {
         if (DEBUG) {
            this.debug("blocked. adding to blocked packets");
         }

         this.blockedPackets.add(new Packet(var1, var2));
      } else {
         ServerConfigurationInformation var3 = Environment.getConfiguredServersMonitor().getLocalServerConfiguration();
         byte[] var4 = new byte[var2];
         System.arraycopy(var1, 0, var4, 0, var2);
         Message var5 = MessageUtils.createMessage(var4, var3.getServerName(), 1L);
         if (DEBUG) {
            this.debug("sending '" + var5 + "' to local group");
         }

         Environment.getGroupManager().getLocalGroup().send(var5);
         ++this.fragmentsSentCount;
      }
   }

   public int receive(byte[] var1) throws InterruptedIOException, IOException {
      Message var2 = null;
      synchronized(this.receivedMessages) {
         while(this.receivedMessages.size() == 0 && !this.shutdown) {
            try {
               if (DEBUG) {
                  this.debug("waiting for a message to arrive ...");
               }

               this.receivedMessages.wait();
            } catch (InterruptedException var6) {
            }
         }

         if (this.shutdown) {
            throw new IOException("unicast receiver is shutdown");
         }

         var2 = (Message)this.receivedMessages.remove(0);
      }

      int var3 = Math.min(var1.length, var2.getData().length);
      System.arraycopy(var2.getData(), 0, var1, 0, var3);
      ++this.fragmentsReceivedCount;
      if (DEBUG) {
         this.debug("message '" + var2 + "' received");
      }

      return var3;
   }

   public void shutdown() {
      if (DEBUG) {
         this.debug("shutdown");
      }

      this.blockedPackets.clear();
      this.shutdown = true;
      synchronized(this.receivedMessages) {
         this.receivedMessages.notify();
      }
   }

   public long getFragmentsSentCount() {
      return (long)this.fragmentsSentCount;
   }

   public long getFragmentsReceivedCount() {
      return (long)this.fragmentsReceivedCount;
   }

   public void setPacketDelay(long var1) {
   }

   public void shutdownPermanent() {
      this.shutdown();
   }

   public void onMessage(Message var1) {
      synchronized(this.receivedMessages) {
         this.receivedMessages.add(var1);
         this.receivedMessages.notify();
      }
   }

   static {
      DEBUG = Environment.DEBUG;
   }

   private static class Packet {
      private final byte[] buffer;
      private final int length;

      Packet(byte[] var1, int var2) {
         this.buffer = var1;
         this.length = var2;
      }
   }
}
