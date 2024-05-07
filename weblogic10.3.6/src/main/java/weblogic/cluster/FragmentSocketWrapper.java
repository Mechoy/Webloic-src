package weblogic.cluster;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.security.AccessController;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

final class FragmentSocketWrapper implements FragmentSocket, BeanUpdateListener {
   private static final boolean DEBUG = true;
   private static final int MAX_RECEIVE_RETRIES = 5;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static FragmentSocketWrapper THE_ONE;
   private final String mcastaddr;
   private final String interfaceAddress;
   private final int port;
   private final byte ttl;
   private final long packetDelay;
   private final int bufSize;
   private FragmentSocket sock;
   private String currentMessagingMode;

   static synchronized FragmentSocketWrapper getInstance(String var0, String var1, int var2, byte var3, long var4, int var6) throws IOException {
      if (THE_ONE != null) {
         return THE_ONE;
      } else {
         THE_ONE = new FragmentSocketWrapper(var0, var1, var2, var3, var4, var6);
         return THE_ONE;
      }
   }

   private FragmentSocketWrapper(String var1, String var2, int var3, byte var4, long var5, int var7) throws IOException {
      this.mcastaddr = var1;
      this.interfaceAddress = var2;
      this.port = var3;
      this.ttl = var4;
      this.packetDelay = var5;
      this.bufSize = var7;
      ClusterMBean var8 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      this.currentMessagingMode = var8.getClusterMessagingMode();
      if ("unicast".equals(var8.getClusterMessagingMode())) {
         this.sock = this.getUnicastFragmentSocket();
      } else {
         this.sock = new MulticastFragmentSocket(var1, var2, var3, var4, var5, var7);
      }

      var8.addBeanUpdateListener(this);
   }

   private FragmentSocket getUnicastFragmentSocket() {
      try {
         Class var1 = Class.forName("weblogic.cluster.messaging.internal.server.UnicastFragmentSocket");
         return (FragmentSocket)var1.newInstance();
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      } catch (IllegalAccessException var3) {
         throw new AssertionError(var3);
      } catch (InstantiationException var4) {
         throw new AssertionError(var4);
      }
   }

   public void start() throws IOException {
      this.sock.start();
   }

   public void send(byte[] var1, int var2) throws IOException {
      this.sock.send(var1, var2);
   }

   public int receive(byte[] var1) throws InterruptedIOException, IOException {
      int var2 = 0;

      while(var2 < 5) {
         FragmentSocket var3 = this.sock;

         try {
            return var3.receive(var1);
         } catch (IOException var5) {
            if (var3 == this.sock) {
               throw var5;
            }

            ++var2;
         }
      }

      throw new IOException("unable to receive cluster messages after switching messaging mode");
   }

   public void shutdown() {
      this.sock.shutdown();
   }

   public long getFragmentsSentCount() {
      return this.sock.getFragmentsSentCount();
   }

   public long getFragmentsReceivedCount() {
      return this.sock.getFragmentsReceivedCount();
   }

   public void setPacketDelay(long var1) {
      this.sock.setPacketDelay(var1);
   }

   public void shutdownPermanent() {
      throw new AssertionError("shutdownPermanent should not be called on FragmentSocketWrapper!");
   }

   public void prepareUpdate(BeanUpdateEvent var1) {
   }

   public void activateUpdate(BeanUpdateEvent var1) {
      DescriptorBean var2 = var1.getProposedBean();
      if (var2 instanceof ClusterMBean) {
         String var3 = ((ClusterMBean)var2).getClusterMessagingMode();
         if (!var3.equals(this.currentMessagingMode)) {
            this.switchMessagingMode(var3);
         }
      }

   }

   private void switchMessagingMode(String var1) {
      this.debug("switching from " + this.currentMessagingMode + " to " + var1);
      FragmentSocket var2 = this.sock;

      try {
         if ("unicast".equals(var1)) {
            this.sock = this.getUnicastFragmentSocket();
            this.sock.start();
            this.debug("unicast mode started");
         } else {
            this.sock = new MulticastFragmentSocket(this.mcastaddr, this.interfaceAddress, this.port, this.ttl, this.packetDelay, this.bufSize);
            this.sock.start();
            this.debug("multicast mode started");
         }
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      this.currentMessagingMode = var1;
      var2.shutdownPermanent();
   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }

   private void debug(String var1) {
      System.out.println("[FragmentSocketWrapper] " + var1);
   }
}
