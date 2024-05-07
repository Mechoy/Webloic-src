package weblogic.cluster;

import java.io.IOException;
import java.util.BitSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.common.internal.WLObjectInputStream;
import weblogic.rmi.spi.HostID;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class MulticastReceiver {
   private static final boolean DEBUG = false;
   private static final int CACHE_SIZE = 3;
   private HostID memberID;
   private int senderNum;
   private IncomingMessage[] cache;
   protected long currentSeqNum;
   private boolean recoveryInProgress;
   private boolean outOfSync;
   private boolean retryEnabled;
   private WorkManager workManager;
   private ConcurrentHashMap<String, GroupMessageStat> groupMessageStatistics;

   MulticastReceiver(HostID var1, int var2) {
      this(var1, var2, WorkManagerFactory.getInstance().getDefault());
   }

   MulticastReceiver(HostID var1, int var2, WorkManager var3) {
      this.groupMessageStatistics = new ConcurrentHashMap();
      this.memberID = var1;
      this.senderNum = var2;
      this.workManager = var3;
      this.clear();
   }

   synchronized void dispatch(long var1, int var3, int var4, int var5, boolean var6, boolean var7, byte[] var8) {
      this.retryEnabled = var7;
      this.processFragment(var1, var3, var4, var5, var6, var8);

      for(final GroupMessage var9 = this.getNextMessage(); var9 != null; var9 = this.getNextMessage()) {
         final HostID var10 = this.memberID;
         String var12 = var9.getClass().getName();
         final GroupMessageStat var13 = (GroupMessageStat)this.groupMessageStatistics.get(var9.getClass().getName());
         if (var13 == null) {
            var13 = new GroupMessageStat(var12);
            this.groupMessageStatistics.put(var12, var13);
         }

         var13.start();
         WorkAdapter var15 = new WorkAdapter() {
            private HostID id = var10;
            private GroupMessage msg = var9;

            public void run() {
               long var1 = System.nanoTime();
               this.msg.execute(this.id);
               long var3 = System.nanoTime() - var1;
               var13.finish(var3);
            }

            public String toString() {
               return "Dispatch Multicast Msg Fragment";
            }
         };
         this.workManager.schedule(var15);
      }

   }

   private void processFragment(long var1, int var3, int var4, int var5, boolean var6, byte[] var7) {
      if (ClusterFragmentsDebugLogger.isDebugEnabled()) {
         ClusterFragmentsDebugLogger.debug("Received fragment memberID:" + this.memberID + " senderNum:" + this.senderNum + " seqNum:" + var1 + "fragNum:" + var3 + " containing " + var7.length + " out of " + var4 + " bytes");
         ClusterFragmentsDebugLogger.debug("currentSeqNum: " + this.currentSeqNum);
      }

      if (var1 >= this.currentSeqNum) {
         if (var1 == this.currentSeqNum) {
            if (var6 && !this.recoveryInProgress) {
               ++this.currentSeqNum;
               return;
            }
         } else if (var1 > this.currentSeqNum) {
            if (var6) {
               this.reportLostMessages(var1 - this.currentSeqNum);
               this.currentSeqNum = var1;
               this.recoveryInProgress = true;
               this.setInSync();
            } else if (this.retryEnabled) {
               this.setOutOfSync();
               if (var1 >= this.currentSeqNum + 3L) {
                  return;
               }
            } else if (var1 >= this.currentSeqNum + 3L) {
               long var8 = var1 - 3L + 1L;
               this.reportLostMessages(var8 - this.currentSeqNum);
               this.currentSeqNum = var8;
            }
         }

         IncomingMessage var10 = this.cache[(int)(var1 % 3L)];
         var10.processFragment(var1, var3, var4, var5, var7);
      }
   }

   private GroupMessage getNextMessage() {
      byte[] var1 = null;
      if (this.retryEnabled) {
         var1 = this.cache[(int)(this.currentSeqNum % 3L)].getMessage(this.currentSeqNum);
         if (var1 != null) {
            this.recoveryInProgress = false;
            this.setInSync();
            ++this.currentSeqNum;
         } else {
            this.resetOutOfSync();
         }
      } else {
         for(long var2 = this.currentSeqNum; var2 < this.currentSeqNum + 3L; ++var2) {
            var1 = this.cache[(int)(var2 % 3L)].getMessage(var2);
            if (var1 != null) {
               if (var2 > this.currentSeqNum) {
                  this.reportLostMessages(var2 - this.currentSeqNum);
               }

               this.currentSeqNum = var2 + 1L;
               break;
            }
         }
      }

      if (var1 != null) {
         try {
            WLObjectInputStream var6 = MulticastManager.getInputStream(var1);
            GroupMessage var3 = (GroupMessage)var6.readObjectWL();
            if (ClusterDebugLogger.isDebugEnabled()) {
               ClusterDebugLogger.debug("Received memberID:" + this.memberID + " senderNum:" + this.senderNum + " seqNum:" + (this.currentSeqNum - 1L) + " message:" + var3);
            }

            return var3;
         } catch (IOException var4) {
            if (MulticastManager.theOne().isUnicastMessagingMode()) {
               ClusterExtensionLogger.logUnicastReceiveError(var4);
            } else {
               ClusterLogger.logMulticastReceiveError(var4);
            }

            return null;
         } catch (ClassNotFoundException var5) {
            if (MulticastManager.theOne().isUnicastMessagingMode()) {
               ClusterExtensionLogger.logUnicastReceiveError(var5);
            } else {
               ClusterLogger.logMulticastReceiveError(var5);
            }

            return null;
         }
      } else {
         return null;
      }
   }

   synchronized void processLastSeqNum(long var1) {
      if (var1 >= this.currentSeqNum) {
         this.setOutOfSync();
      }

   }

   long getCurrentSeqNum() {
      return this.currentSeqNum;
   }

   protected void setOutOfSync() {
      if (!this.outOfSync) {
         MulticastManager.theOne().incrementResendRequestsCount();
         this.outOfSync = true;
         IncomingMessage var1 = this.cache[(int)(this.currentSeqNum % 3L)];
         int var2 = var1.nextFragNum(this.currentSeqNum);
         MulticastManager.theOne().addItem(new NAKHBI(this.memberID, this.senderNum, this.currentSeqNum, var2));
      }

   }

   private void resetOutOfSync() {
      if (this.outOfSync) {
         IncomingMessage var1 = this.cache[(int)(this.currentSeqNum % 3L)];
         int var2 = var1.nextFragNum(this.currentSeqNum);
         MulticastManager.theOne().replaceItem(new NAKHBI(this.memberID, this.senderNum, this.currentSeqNum, var2));
      }

   }

   private void setInSync() {
      if (this.outOfSync) {
         this.outOfSync = false;
         MulticastManager.theOne().removeItem(new NAKHBI(this.memberID, this.senderNum, 0L, 0));
      }

   }

   void setInSync(long var1) {
      this.currentSeqNum = var1;
      this.setInSync();
   }

   synchronized void shutdown() {
      if (this.outOfSync) {
         MulticastManager.theOne().removeItem(new NAKHBI(this.memberID, this.senderNum, 0L, 0));
      }

   }

   private void reportLostMessages(long var1) {
      if (this.currentSeqNum > 0L) {
         MulticastManager.theOne().incrementMulticastMessagesLostCount(var1);
         if (ClusterService.getClusterService().isUnicastMessagingModeEnabled()) {
            ClusterExtensionLogger.logLostUnicastMessages(var1);
         } else {
            ClusterLogger.logLostMulticastMessages(var1);
         }
      }

   }

   synchronized void clear() {
      this.cache = new IncomingMessage[3];

      for(int var1 = 0; var1 < 3; ++var1) {
         this.cache[var1] = new IncomingMessage();
      }

      this.currentSeqNum = 0L;
      this.recoveryInProgress = false;
      this.outOfSync = false;
      this.groupMessageStatistics.clear();
   }

   void dumpDiagnosticImageData(XMLStreamWriter var1) throws XMLStreamException, IOException {
      var1.writeStartElement("MulticastReceiver");
      var1.writeAttribute("MemberID", this.memberID.toString());
      var1.writeAttribute("SenderNum", String.valueOf(this.senderNum));
      Iterator var2 = this.groupMessageStatistics.values().iterator();

      while(var2.hasNext()) {
         GroupMessageStat var3 = (GroupMessageStat)var2.next();
         var3.dumpDiagnosticImageData(var1);
      }

      var1.writeEndElement();
   }

   private static class GroupMessageStat {
      private String name;
      private AtomicLong totalScheduled;
      private AtomicLong totalExecutionTimeNanos;
      private AtomicLong totalExecuted;
      private long minExecutionTimeNanos = Long.MAX_VALUE;
      private long maxExecutionTimeNanos = -1L;
      private long lastExecutionTimeNanos = -1L;

      public GroupMessageStat(String var1) {
         this.name = var1;
         this.totalScheduled = new AtomicLong();
         this.totalExecutionTimeNanos = new AtomicLong();
         this.totalExecuted = new AtomicLong();
      }

      public String getName() {
         return this.name;
      }

      public long getTotalScheduled() {
         return this.totalScheduled.get();
      }

      public long getTotalExecutionTimeNanos() {
         return this.totalExecutionTimeNanos.get();
      }

      public long getTotalExecuted() {
         return this.totalExecuted.get();
      }

      public double getAverageExecutionTimeNanos() {
         long var1 = this.getTotalExecuted();
         long var3 = this.getTotalExecutionTimeNanos();
         return var1 != 0L ? (double)var3 / (double)var1 : 0.0;
      }

      public long getPendingCount() {
         return this.getTotalScheduled() - this.getTotalExecuted();
      }

      public void start() {
         this.totalScheduled.addAndGet(1L);
      }

      public void finish(long var1) {
         this.totalExecutionTimeNanos.addAndGet(var1);
         this.totalExecuted.addAndGet(1L);
         if (var1 < this.minExecutionTimeNanos) {
            this.minExecutionTimeNanos = var1;
         }

         if (var1 > this.maxExecutionTimeNanos) {
            this.maxExecutionTimeNanos = var1;
         }

         this.lastExecutionTimeNanos = var1;
      }

      void dumpDiagnosticImageData(XMLStreamWriter var1) throws XMLStreamException, IOException {
         var1.writeStartElement("GroupMessageStat");
         var1.writeAttribute("Name", this.name);
         var1.writeAttribute("TotalScheduled", String.valueOf(this.totalScheduled));
         var1.writeAttribute("TotalExecuted", String.valueOf(this.totalExecuted));
         var1.writeAttribute("AverageExecutionTimeNanos", String.valueOf(this.getAverageExecutionTimeNanos()));
         var1.writeAttribute("MinExecutionTimeNanos", String.valueOf(this.minExecutionTimeNanos));
         var1.writeAttribute("MaxExecutionTimeNanos", String.valueOf(this.maxExecutionTimeNanos));
         var1.writeAttribute("LastExecutionTimeNanos", String.valueOf(this.lastExecutionTimeNanos));
         var1.writeEndElement();
      }
   }

   private class IncomingMessage {
      private static final int INVALID_SEQNUM = -1;
      private long currentSeqNum = -1L;
      private int numFragments;
      private long numFragmentsReceived;
      private byte[] serializedMessage;
      private BitSet fragmentsReceived;

      IncomingMessage() {
      }

      void processFragment(long var1, int var3, int var4, int var5, byte[] var6) {
         if (this.currentSeqNum != var1) {
            this.currentSeqNum = var1;
            this.numFragments = -1;
            this.numFragmentsReceived = 0L;
            this.serializedMessage = new byte[var4];
            this.fragmentsReceived = new BitSet();
         }

         if (!this.fragmentsReceived.get(var3)) {
            System.arraycopy(var6, 0, this.serializedMessage, var5, var6.length);
            this.fragmentsReceived.set(var3);
            ++this.numFragmentsReceived;
            if (var5 + var6.length >= var4) {
               this.numFragments = var3 + 1;
            }
         }

      }

      byte[] getMessage(long var1) {
         return var1 == this.currentSeqNum && this.numFragmentsReceived == (long)this.numFragments ? this.serializedMessage : null;
      }

      int nextFragNum(long var1) {
         if (var1 != this.currentSeqNum) {
            return 0;
         } else {
            int var3;
            for(var3 = 0; var3 < this.fragmentsReceived.length() && this.fragmentsReceived.get(var3); ++var3) {
            }

            return var3;
         }
      }
   }
}
