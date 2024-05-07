package weblogic.cluster;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OptionalDataException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Date;
import weblogic.common.internal.WLObjectInputStream;
import weblogic.kernel.Kernel;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.spi.HostID;
import weblogic.security.HMAC;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.utils.ByteArrayDiffChecker;
import weblogic.utils.io.UnsyncByteArrayInputStream;

public final class MulticastMonitor implements MulticastSessionIDConstants {
   static final int MAX_FRAGMENT_SIZE = 65536;
   private final MulticastSocket sock;
   private byte[] fragmentBuffer;
   private final int bufSize = 131072;
   private final int localDomainNameHash;
   private final int localClusterNameHash;
   private byte[] salt = null;
   private byte[] key = null;
   private String domainDir;
   private EncryptionService es;
   private ByteArrayDiffChecker badc;
   private boolean esEnabled = false;

   public MulticastMonitor(String var1, int var2, String var3, String var4, String var5) throws IOException, UnknownHostException {
      this.sock = new MulticastSocket(var2);
      this.sock.setSoTimeout(30000);
      this.sock.setReceiveBufferSize(this.bufSize);
      this.localDomainNameHash = this.hashCode(var3);
      this.localClusterNameHash = this.hashCode(var4);
      this.sock.joinGroup(InetAddress.getByName(var1));
      this.domainDir = var5;
      this.badc = new ByteArrayDiffChecker();
      Kernel.ensureInitialized();
      this.initES();
   }

   private void initES() {
      if (this.domainDir != null) {
         System.setProperty("weblogic.RootDirectory", this.domainDir);
         this.es = SerializedSystemIni.getExistingEncryptionService();
         if (this.es != null) {
            this.salt = SerializedSystemIni.getSalt();
            this.key = SerializedSystemIni.getEncryptedSecretKey();
            this.esEnabled = true;
         }
      }
   }

   private boolean verify(byte[] var1, byte[] var2) {
      if (!this.esEnabled) {
         return false;
      } else {
         byte[] var3 = HMAC.digest(var1, this.key, this.salt);
         return this.badc.diffByteArrays(var2, var3) == null;
      }
   }

   public void run() throws Exception {
      while(true) {
         try {
            this.fragmentBuffer = new byte[65536];
            DatagramPacket var1 = new DatagramPacket(this.fragmentBuffer, 65536);
            this.sock.receive(var1);
            WLObjectInputStream var2 = getInputStream(var1.getData());
            int var3 = var2.readInt();
            int var4 = var2.readInt();
            HostID var5 = (HostID)var2.readObjectWL();
            Object var6 = null;
            Object var7 = null;
            byte[] var8 = null;
            String var9 = var2.readString();
            byte[] var25 = (byte[])((byte[])var2.readObject());

            byte[] var26;
            try {
               var26 = (byte[])((byte[])var2.readObject());
            } catch (IOException var21) {
               var26 = var25;
               var25 = null;
            }

            if (var25 != null) {
               if (!this.esEnabled) {
                  System.err.println("Cannot handle encrypted multicast traffic.  Make sure domaindir is specified and correct");
                  usage();
               }

               if (this.verify(var26, var25)) {
                  var8 = this.es.decryptBytes(var26);
               } else {
                  System.err.println("Message digest mismatch - ignoring packet");
                  System.exit(1);
               }
            } else {
               var8 = var26;
            }

            var2 = getInputStream(var8);
            int var10 = var2.readInt();
            long var11 = var2.readLong();
            int var13 = var2.readInt();
            int var14 = var2.readInt();
            int var15 = var2.readInt();
            boolean var16 = var2.readBoolean();
            boolean var17 = var2.readBoolean();
            boolean var18 = var2.readBoolean();
            byte[] var19 = var2.readBytes();
            if (!this.isFragmentFromForeignCluster(var3, var4)) {
               String var20;
               switch (var10) {
                  case 0:
                     var20 = " Received heartbeat message of size ";
                     break;
                  case 1:
                     var20 = " Received attribute message of size ";
                     break;
                  case 2:
                     var14 = var19.length;
                     var20 = " Received announcement message of size ";
                     break;
                  default:
                     var20 = " Received multicast message of size ";
               }

               System.out.println(var20 + var14 + " from " + ((ServerIdentity)var5).getServerName() + " @ " + new Date() + " messageVersion:" + var9 + " seqNum:" + var11 + " fragment # " + var13);
            } else {
               System.out.println("Received multicast message of size " + var14 + " from foreign cluster " + var5);
            }
         } catch (InterruptedIOException var22) {
         } catch (OptionalDataException var23) {
            System.err.println("Failed with OptionalDataException - EOF = " + var23.eof + " Length = " + var23.length);
            System.exit(1);
         } catch (IOException var24) {
            System.err.println("Failed with IOException " + var24);
            System.exit(1);
         }
      }
   }

   private boolean isFragmentFromForeignCluster(int var1, int var2) {
      return this.localDomainNameHash != var1 || this.localClusterNameHash != var2;
   }

   public int hashCode(String var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         var2 = 31 * var2 + var1.charAt(var3);
      }

      return var2;
   }

   private static WLObjectInputStream getInputStream(byte[] var0) throws IOException {
      UnsyncByteArrayInputStream var1 = new UnsyncByteArrayInputStream(var0);
      WLObjectInputStream var2 = new WLObjectInputStream(var1);
      var2.setReplacer(new MulticastReplacer(LocalServerIdentity.getIdentity()));
      return var2;
   }

   public static void main(String[] var0) {
      String var1 = null;

      try {
         if (var0.length < 4 || var0.length > 5) {
            usage();
         }

         if (var0.length == 5) {
            var1 = var0[4];
         }

         MulticastMonitor var2 = new MulticastMonitor(var0[0], Integer.parseInt(var0[1]), var0[2], var0[3], var1);
         var2.run();
      } catch (Exception var3) {
         usage();
      }

   }

   public static void usage() {
      System.out.println("java weblogic.cluster.MulticastMonitor <multicastaddress> <port> <domainname> <clustername> [<domaindir>] ");
      System.exit(1);
   }
}
