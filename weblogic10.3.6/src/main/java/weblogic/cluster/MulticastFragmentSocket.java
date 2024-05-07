package weblogic.cluster;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import weblogic.utils.Debug;

class MulticastFragmentSocket implements FragmentSocket {
   private static final boolean DEBUG = true;
   private String multicastAddr;
   private InetAddress group;
   private InetAddress interfaceAddr;
   private int port;
   private MulticastSocket sock;
   private boolean blocked;
   private ArrayList blockedPackets;
   private long packetDelay;
   private long lastDelay;
   private long lastSendTime;
   private long fragmentsSentCount;
   private long fragmentsReceivedCount;
   private static final String osName = initOSNameProp();
   private static final boolean isLinux;
   private static final int SO_TIMEOUT = 30000;
   private byte ttl;
   private int bufSize;
   private boolean shutdownPermanent;
   private Object SOCK_INIT_LOCK = new Object();

   MulticastFragmentSocket(String var1, String var2, int var3, byte var4, long var5, int var7) throws IOException, UnknownHostException {
      this.multicastAddr = var1;
      this.group = InetAddress.getByName(var1);
      this.port = var3;
      if (var2 != null) {
         if (ClusterDebugLogger.isDebugEnabled()) {
            ClusterDebugLogger.debug("Setting the interface address to : " + var2);
         }

         this.interfaceAddr = InetAddress.getByName(var2);
      }

      this.ttl = var4;
      this.bufSize = var7;
      this.blocked = true;
      this.blockedPackets = new ArrayList();
      this.packetDelay = var5;
      this.lastDelay = 0L;
      this.lastSendTime = 0L;
      this.fragmentsSentCount = 0L;
      this.fragmentsReceivedCount = 0L;
   }

   private void initializeMulticastSocket() throws IOException {
      if (this.sock == null) {
         synchronized(this.SOCK_INIT_LOCK) {
            if (this.sock == null) {
               MulticastSocket var2;
               if (isLinux) {
                  try {
                     var2 = new MulticastSocket(new InetSocketAddress(this.multicastAddr, this.port));
                  } catch (BindException var5) {
                     if (ClusterDebugLogger.isDebugEnabled()) {
                        ClusterDebugLogger.debug("Failed to bind to : " + this.multicastAddr, var5);
                        ClusterDebugLogger.debug("Will try binding to IP_ANY...");
                     }

                     var2 = new MulticastSocket(this.port);
                  }
               } else {
                  var2 = new MulticastSocket(this.port);
               }

               if (this.interfaceAddr != null) {
                  var2.setInterface(this.interfaceAddr);
               }

               var2.setTimeToLive(this.ttl);
               var2.setSoTimeout(30000);
               if (var2.getReceiveBufferSize() < this.bufSize) {
                  var2.setReceiveBufferSize(this.bufSize);
               }

               if (var2.getSendBufferSize() < this.bufSize) {
                  var2.setSendBufferSize(this.bufSize);
               }

               var2.joinGroup(this.group);
               this.sock = var2;
            }
         }
      }
   }

   private static String initOSNameProp() {
      String var0 = "UNKNOWN";

      try {
         var0 = System.getProperty("os.name", "UNKNOWN").toLowerCase(Locale.ENGLISH);
      } catch (SecurityException var2) {
      }

      return var0;
   }

   public synchronized void send(byte[] var1, int var2) throws IOException {
      if (this.shutdownPermanent) {
         throw new IOException("multicast socket shutdown to enable unicast mode");
      } else {
         DatagramPacket var3 = new DatagramPacket(var1, var2, this.group, this.port);
         if (this.blocked) {
            this.blockedPackets.add(var3);
         } else {
            this.sendThrottled(var3);
         }

      }
   }

   public synchronized void start() throws IOException {
      this.initializeMulticastSocket();
      this.blocked = false;
      Iterator var1 = this.blockedPackets.iterator();

      while(var1.hasNext()) {
         DatagramPacket var2 = (DatagramPacket)var1.next();
         this.sendThrottled(var2);
      }

      this.blockedPackets.clear();
   }

   public void shutdown() {
      try {
         if (this.sock != null) {
            this.sock.leaveGroup(this.group);
            this.sock.close();
         }
      } catch (IOException var2) {
      }

      this.sock = null;
      this.blockedPackets.clear();
   }

   private void sendThrottled(DatagramPacket var1) throws IOException {
      long var2 = System.currentTimeMillis();
      long var4 = this.packetDelay - (var2 - this.lastSendTime);
      if (var4 <= 0L) {
         this.lastSendTime = var2;
      } else {
         try {
            Thread.sleep(var4);
         } catch (InterruptedException var7) {
         }

         this.lastSendTime = var2 + var4;
      }

      try {
         if (this.sock == null) {
            this.initializeMulticastSocket();
         }

         this.sock.send(var1);
         if (MulticastManager.theOne().getSendStartTimestamp() == 0L) {
            MulticastManager.theOne().flagStartedSending();
         } else if (!MulticastManager.theOne().getCanReceiveOwnMessages()) {
            MulticastManager.theOne().handleMissedOwnMessages();
         }
      } catch (IOException var8) {
         if (this.sock != null) {
            this.shutdown();
         }

         throw var8;
      }

      ++this.fragmentsSentCount;
   }

   public int receive(byte[] var1) throws InterruptedIOException, IOException {
      DatagramPacket var2 = new DatagramPacket(var1, var1.length);

      try {
         if (this.sock == null) {
            if (this.shutdownPermanent) {
               throw new IOException("multicast socket shutdown to enable unicast mode");
            }

            this.initializeMulticastSocket();
         }

         this.sock.receive(var2);
      } catch (IOException var4) {
         if (this.sock != null) {
            this.shutdown();
         }

         throw var4;
      }

      ++this.fragmentsReceivedCount;
      return var2.getLength();
   }

   public long getFragmentsSentCount() {
      return this.fragmentsSentCount;
   }

   public long getFragmentsReceivedCount() {
      return this.fragmentsReceivedCount;
   }

   public void setPacketDelay(long var1) {
      Debug.say("Setting packet delay to " + var1);
      this.packetDelay = var1;
   }

   public synchronized void shutdownPermanent() {
      this.shutdownPermanent = true;
      this.shutdown();
   }

   static {
      isLinux = "linux".equals(osName);
   }
}
