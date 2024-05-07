package weblogic.cluster;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.util.Arrays;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.WLObjectOutputStream;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.LocalServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.io.UnsyncByteArrayOutputStream;

public final class MulticastSender implements MulticastSession {
   private static final int DEFAULT_CACHE_SIZE = 3;
   private static final int RESEND_BLOCKING_INTERVAL_MILLIS = 2000;
   private static final int PAYLOAD_FUDGE_FACTOR = 27;
   private static final short DATA_HEADERS_SIZE = 29;
   private static final int ENCRYPTION_OVERHEAD_SIZE = 34;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private int senderNum;
   private FragmentSocket sock;
   private RecoverListener rl;
   private boolean retryEnabled;
   private int cacheSize;
   private OutgoingMessage[] cache;
   private int numMessages;
   private long oldestSeqNum;
   private long lastResendTime;
   private long lastSeqNumResent;
   private int lastFragNumResent;
   private int localDomainNameHash;
   private int localClusterNameHash;
   private final boolean useHTTPForSD;
   private final boolean adminSender;
   private boolean suspended;

   MulticastSender(int var1, FragmentSocket var2, RecoverListener var3, int var4, boolean var5) {
      this(var1, var2, var3, var4, var5, false);
   }

   MulticastSender(int var1, FragmentSocket var2, RecoverListener var3, int var4, boolean var5, boolean var6) {
      this.senderNum = var1;
      this.sock = var2;
      this.rl = var3;
      this.adminSender = var6;
      this.suspended = false;
      this.retryEnabled = var3 != null;
      if (!this.retryEnabled) {
         this.cacheSize = 1;
      } else if (var4 > 0) {
         this.cacheSize = var4;
      } else {
         this.cacheSize = 3;
      }

      this.cache = new OutgoingMessage[this.cacheSize];

      for(int var7 = 0; var7 < this.cacheSize; ++var7) {
         this.cache[var7] = new OutgoingMessage();
      }

      this.numMessages = 0;
      this.oldestSeqNum = 0L;
      this.lastResendTime = 0L;
      ClusterMBean var8 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      this.localDomainNameHash = this.hashCode(ManagementService.getRuntimeAccess(kernelId).getDomain().getName());
      this.localClusterNameHash = this.hashCode(var8.getName());
      this.useHTTPForSD = var5;
   }

   public synchronized void send(GroupMessage var1) throws IOException {
      if (!this.isAdminSender() && this.suspended) {
         if (ClusterDebugLogger.isDebugEnabled()) {
            ClusterDebugLogger.debug("Not sending message " + var1 + " as the " + "multicast session is supended");
         }

      } else {
         OutgoingMessage var2 = this.prepare(var1, false);
         if (ClusterDebugLogger.isDebugEnabled()) {
            ClusterDebugLogger.debug("Sending senderNum:" + this.senderNum + " seqNum:" + var2.seqNum + " message:" + var1 + " outgoing message:" + var2);
         }

         this.fragmentAndSend(var2, 0);
         if (this.retryEnabled) {
            this.lastResendTime = System.currentTimeMillis();
            this.lastSeqNumResent = var2.seqNum;
            this.lastFragNumResent = 0;
            MulticastManager.theOne().replaceItem(new LastSeqNumHBI(this.senderNum, var2.seqNum, this.useHTTPForSD));
         }

      }
   }

   synchronized void processNAK(long var1, int var3, String var4) {
      try {
         long var5 = System.currentTimeMillis();
         if (var1 < this.lastSeqNumResent || var1 == this.lastSeqNumResent && var3 < this.lastFragNumResent || var5 - this.lastResendTime > 2000L) {
            this.lastResendTime = var5;
            long var7;
            int var9;
            if (var1 >= this.oldestSeqNum) {
               if (ClusterDebugLogger.isDebugEnabled()) {
                  ClusterDebugLogger.debug("Resending senderNum:" + this.senderNum + " seqNum:" + var1 + " fragNum:" + var3);
               }

               var7 = var1;
               var9 = var3;
               this.lastSeqNumResent = var1;
               this.lastFragNumResent = var3;
            } else {
               if (this.cache[(int)(this.oldestSeqNum % (long)this.cacheSize)].isRecover) {
                  if (ClusterDebugLogger.isDebugEnabled()) {
                     ClusterDebugLogger.debug("Resending recover senderNum:" + this.senderNum + " seqNum:" + this.oldestSeqNum);
                  }
               } else {
                  GroupMessage var10 = this.rl.createRecoverMessage();
                  if (ClusterDebugLogger.isDebugEnabled()) {
                     ClusterDebugLogger.debug("Sending recover senderNum:" + this.senderNum + " seqNum:" + this.oldestSeqNum + " message:" + var10);
                  }

                  OutgoingMessage var11 = this.prepare(var10, true);
                  MulticastManager.theOne().replaceItem(new LastSeqNumHBI(this.senderNum, var11.seqNum, this.useHTTPForSD));
               }

               var7 = this.oldestSeqNum;
               var9 = 0;
               this.lastSeqNumResent = 0L;
               this.lastFragNumResent = 0;
            }

            for(long var14 = var7; var14 <= this.oldestSeqNum + (long)this.numMessages - 1L; ++var14) {
               OutgoingMessage var12 = this.cache[(int)(var14 % (long)this.cacheSize)];
               var12 = var12.replace(var4);
               this.fragmentAndSend(var12, var9);
               var9 = 0;
            }
         }
      } catch (IOException var13) {
         if (ClusterDebugLogger.isDebugEnabled()) {
            ClusterLogger.logMulticastSendError(var13);
         } else {
            ClusterLogger.logMulticastSendErrorMsg(var13.getLocalizedMessage());
         }
      }

   }

   public int getSessionID() {
      return this.senderNum;
   }

   GroupMessage createRecoverMessage() {
      return this.rl == null ? null : this.rl.createRecoverMessage();
   }

   long getCurrentSeqNum() {
      return this.oldestSeqNum + (long)this.numMessages;
   }

   boolean isAdminSender() {
      return this.adminSender;
   }

   boolean isRetryEnabled() {
      return this.retryEnabled;
   }

   synchronized void suspend() {
      this.suspended = true;
   }

   synchronized void resume() {
      this.suspended = false;
   }

   private OutgoingMessage prepare(GroupMessage var1, boolean var2) throws IOException {
      UnsyncByteArrayOutputStream var3 = new UnsyncByteArrayOutputStream();
      WLObjectOutputStream var4 = MulticastManager.getOutputStream(var3);
      var4.writeObjectWL(var1);
      String var5 = ((UpgradeUtils.PeerInfoableObjectOutput)var4).getClusterVersion();
      var4.flush();
      long var6 = this.oldestSeqNum + (long)this.numMessages;
      OutgoingMessage var8 = this.cache[(int)(var6 % (long)this.cacheSize)];
      if (isHeartbeatMessage(var1)) {
         var8.set(var3.toRawBytes(), var3.size(), var6, var2, this.retryEnabled, "0,0,0", (GroupMessage)null);
      } else {
         var8.set(var3.toRawBytes(), var3.size(), var6, var2, this.retryEnabled, var5, var1);
      }

      if (var2) {
         this.oldestSeqNum = var6;
         this.numMessages = 1;
      } else if (this.numMessages == this.cacheSize) {
         ++this.oldestSeqNum;
      } else {
         ++this.numMessages;
      }

      return var8;
   }

   private static boolean isHeartbeatMessage(GroupMessage var0) {
      return var0 instanceof HeartbeatMessage;
   }

   private void fragmentAndSend(OutgoingMessage var1, int var2) throws IOException {
      int var3 = 0;

      for(int var4 = 0; var3 < var1.size; ++var4) {
         UnsyncByteArrayOutputStream var5 = new UnsyncByteArrayOutputStream(MulticastManager.MAX_FRAGMENT_SIZE);
         WLObjectOutputStream var6 = MulticastManager.getOutputStream(var5);
         var6.writeInt(this.localDomainNameHash);
         var6.writeInt(this.localClusterNameHash);
         var6.writeObjectWL(LocalServerIdentity.getIdentity());
         var6.writeString(var1.messageVersion);
         var6.flush();
         int var7 = var5.size() + 27;
         if (ClusterService.getClusterService().multicastDataEncryptionEnabled()) {
            var7 += 34;
         }

         var7 += 56;
         int var8 = Math.min(MulticastManager.MAX_FRAGMENT_SIZE - var7, var1.size - var3);
         byte[] var9 = this.serializePayload(var1, var4, var3, var2, var8);
         byte[] var10 = var9;
         if (ClusterService.getClusterService().multicastDataEncryptionEnabled()) {
            var10 = EncryptionHelper.encrypt(var9);
         }

         if (var10 != null) {
            if (ClusterService.getClusterService().multicastDataEncryptionEnabled()) {
               var6.writeObject(EncryptionHelper.sign(var10));
            }

            var6.writeObject(var10);
            var6.flush();
            if (ClusterFragmentsDebugLogger.isDebugEnabled()) {
               ClusterFragmentsDebugLogger.debug("Sending fragment senderNum:" + this.senderNum + " seqNum:" + var1.seqNum + "fragNum:" + var4 + " containing " + var8 + " bytes out of " + var1.size);
            }

            this.sock.send(var5.toRawBytes(), var5.size());
         }

         var3 += var8;
      }

   }

   private byte[] serializePayload(OutgoingMessage var1, int var2, int var3, int var4, int var5) throws IOException {
      ByteArrayOutputStream var6 = new ByteArrayOutputStream(MulticastManager.MAX_FRAGMENT_SIZE);
      WLObjectOutputStream var7 = new WLObjectOutputStream(var6);
      var7.writeInt(this.senderNum);
      var7.writeLong(var1.seqNum);
      var7.writeInt(var2);
      var7.writeInt(var1.size);
      var7.writeInt(var3);
      var7.writeBoolean(var1.isRecover);
      var7.writeBoolean(var1.retryEnabled);
      var7.writeBoolean(this.useHTTPForSD);
      if (var2 >= var4) {
         var7.writeBytes(var1.message, var3, var5);
         var7.flush();
         return var6.toByteArray();
      } else {
         return null;
      }
   }

   public int hashCode(String var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         var2 = 31 * var2 + var1.charAt(var3);
      }

      return var2;
   }

   public String toString() {
      return "MulticastSender-" + this.senderNum;
   }

   class OutgoingMessage {
      public byte[] message;
      public int size;
      public long seqNum;
      public boolean isRecover;
      public boolean retryEnabled;
      public String messageVersion;
      public GroupMessage groupMessage;

      void set(byte[] var1, int var2, long var3, boolean var5, boolean var6, String var7, GroupMessage var8) {
         this.message = var1;
         this.size = var2;
         this.seqNum = var3;
         this.isRecover = var5;
         this.retryEnabled = var6;
         this.messageVersion = var7;
         this.groupMessage = var8;
      }

      void clear() {
         this.message = null;
      }

      public synchronized OutgoingMessage replace(String var1) {
         WLObjectOutputStream var2 = null;

         OutgoingMessage var3;
         try {
            if (var1 != null && this.messageVersion != null) {
               if (var1.equals(this.messageVersion)) {
                  var3 = this;
                  return var3;
               }

               PeerInfo var21 = PeerInfo.getPeerInfo(this.messageVersion);
               PeerInfo var22 = PeerInfo.getPeerInfo(var1);
               if (ClusterDebugLogger.isDebugEnabled()) {
                  ClusterDebugLogger.debug("[UPGRADE] outgoing message needs replacement?, messageVersion:" + this.messageVersion + ", remoteVersion:" + var1);
               }

               if (var21.compareTo(var22) <= 0) {
                  OutgoingMessage var23 = this;
                  return var23;
               }

               UnsyncByteArrayOutputStream var5 = new UnsyncByteArrayOutputStream();
               var2 = MulticastManager.getOutputStream(var5, var22);
               var2.writeObjectWL(this.groupMessage);
               this.messageVersion = ((UpgradeUtils.PeerInfoableObjectOutput)var2).getClusterVersion();
               var2.writeString(this.messageVersion);
               var2.flush();
               this.message = var5.toRawBytes();
               this.size = var5.size();
               if (ClusterDebugLogger.isDebugEnabled()) {
                  ClusterDebugLogger.debug("[UPGRADE] outgoing message is replaced and new messageVersion is " + this.messageVersion);
               }

               OutgoingMessage var6 = this;
               return var6;
            }

            var3 = this;
         } catch (IOException var19) {
            var19.printStackTrace();
            OutgoingMessage var4 = this;
            return var4;
         } finally {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (IOException var18) {
               }
            }

         }

         return var3;
      }

      public String toString() {
         return "message:" + Arrays.toString(this.message) + ", size:" + this.size + ", seqNum:" + this.seqNum + ", messageVersion:" + this.messageVersion;
      }
   }
}
