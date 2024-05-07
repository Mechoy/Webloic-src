package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.common.internal.InteropWriteReplaceable;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.store.common.PersistentStoreOutputStream;

public final class DistributedDestinationImpl extends DestinationImpl implements InteropWriteReplaceable, Externalizable {
   private static final int DDVERSION1 = 1;
   static final long serialVersionUID = 6099783323740404732L;
   private static final int BE_DESTINATION_NOT_TEMPORARY = 1;
   private int weight = 1;
   private int loadBalancePolicy;
   private int messageForwardingPolicy;
   private String ddJNDIName;
   private String distributedConfigMbeanName;
   private boolean hasStore;
   private boolean stale;
   private boolean serverAffinityEnabled = true;
   private boolean isQueueForward;
   private boolean isLocal = false;
   private boolean isConsumptionPaused;
   private boolean isInsertionPaused;
   private boolean isProductionPaused;
   private int nonSystemSubscriberConsumers;
   private int order;
   private static final int _DDVERSIONMASK = 3840;
   private static final int _DDVERSIONSHIFT = 8;
   private static final int _ISFORWARDING_POLICY_PARTITIONED = 1;
   private static final int _HASDDJNDINAME = 2;
   private static final int _HASSTORE = 4;
   private static final int _HASNONSYSSUBCNT = 8;
   private static final int _ISDURABLE = 16;
   private static final int _ISBOUNDBYINTERNALNAME = 32;
   private static final int _ISQUEUEFORWARD = 64;
   private static final int _ISSERVERAFFINITY = 128;
   private static final int _DONOTUSE_RESERVED = 4096;

   public DistributedDestinationImpl() {
   }

   public DistributedDestinationImpl(int var1, String var2, String var3, String var4, String var5, int var6, int var7, String var8, String var9, JMSServerId var10, JMSID var11, DispatcherId var12, boolean var13, String var14, String var15, boolean var16) {
      super(var1, var2, var8, var4, var5, var10, var11, var15, var14);
      this.ddJNDIName = var9;
      this.distributedConfigMbeanName = var3;
      this.loadBalancePolicy = var6;
      this.messageForwardingPolicy = var7;
      this.dispatcherId = var12;
      this.hasStore = var13;
      this.isLocal = var16;
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug(" ------- Distributed Destination ------------------------------\n name            = " + this.getMemberName() + "\n" + " serverName      = " + this.getServerName() + "\n" + " distributedConfigMbeanName = " + this.distributedConfigMbeanName + "\n" + " ddJNDIName      = " + this.ddJNDIName + "\n" + " --------------------------------------------------------------\n");
      }

   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public String toString() {
      return this.getName();
   }

   public int getLoadBalancingPolicy() {
      return this.loadBalancePolicy;
   }

   public int getMessageForwardingPolicy() {
      return this.messageForwardingPolicy;
   }

   public int getWeight() {
      return this.weight;
   }

   public void setWeight(int var1) {
      this.weight = var1;
   }

   public synchronized int getOrder() {
      return this.order;
   }

   public synchronized void setOrder(int var1) {
      this.order = var1;
   }

   public boolean isPersistent() {
      return this.hasStore;
   }

   public byte getDestinationInstanceType() {
      return 2;
   }

   public boolean isLocal() {
      return this.isLocal;
   }

   public void setStale(boolean var1) {
      this.stale = var1;
   }

   public boolean isStale() {
      return this.stale;
   }

   public void setQueueForward(boolean var1) {
      this.isQueueForward = var1;
   }

   public boolean isQueueForward() {
      return this.isQueueForward;
   }

   public void setNonSystemSubscriberConsumers(int var1) {
      this.nonSystemSubscriberConsumers = var1;
   }

   public int getNonSystemSubscriberConsumers() {
      return this.nonSystemSubscriberConsumers;
   }

   public boolean hasConsumer() {
      return this.nonSystemSubscriberConsumers > 0;
   }

   public String getInstanceName() {
      return this.getName();
   }

   public String getCreateDestinationArgument() {
      String var1 = this.distributedConfigMbeanName;
      if (var1.startsWith("/")) {
         var1 = var1.substring(2, var1.length() - 1).intern();
      }

      return var1;
   }

   public boolean equals(Object var1) {
      return var1 instanceof String ? var1.equals(this.getInstanceName()) : super.equals(var1);
   }

   public boolean same(String var1) {
      return var1.equals(this.getName());
   }

   public String getDDJNDIName() {
      return this.ddJNDIName;
   }

   public String getGlobalJNDIName() {
      return this.ddJNDIName;
   }

   public String getName() {
      return this.distributedConfigMbeanName;
   }

   public void setName(String var1) {
      this.distributedConfigMbeanName = var1;
   }

   String getDestinationName() {
      return this.distributedConfigMbeanName;
   }

   public boolean isConsumptionPaused() {
      return this.isConsumptionPaused;
   }

   public void setIsConsumptionPaused(boolean var1) {
      this.isConsumptionPaused = var1;
   }

   public boolean isInsertionPaused() {
      return this.isInsertionPaused;
   }

   public void setIsInsertionPaused(boolean var1) {
      this.isInsertionPaused = var1;
   }

   public boolean isProductionPaused() {
      return this.isProductionPaused;
   }

   public void setIsProductionPaused(boolean var1) {
      this.isProductionPaused = var1;
   }

   public String debugString() {
      return !JMSDebug.JMSCommon.isDebugEnabled() ? "Distributed Destination Impl" : new String(this.getInstanceName() + " " + this.getMemberName() + " | pers-" + this.isPersistent() + " | cons-" + this.hasConsumer() + " | weit-" + this.getWeight() + " | locl-" + this.isLocal());
   }

   public Object interopWriteReplace(PeerInfo var1) throws IOException {
      if (var1.compareTo(PeerInfo.VERSION_60) < 0) {
         throw new IOException(JMSClientExceptionLogger.logInvalidPeerLoggable(1).getMessage());
      } else if (var1.compareTo(PeerInfo.VERSION_70) < 0) {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("\n   *** Peer > 6 & < 7 WriteReplace DDImpl with DImpl ***\n      serverName = " + this.getServerName() + "      qname      = " + this.distributedConfigMbeanName);
         }

         return new DestinationImpl(this.type, this.getServerName(), this.distributedConfigMbeanName, this.getApplicationName(), this.getModuleName(), this.backEndId, this.destinationId);
      } else {
         if (JMSDebug.JMSCommon.isDebugEnabled()) {
            JMSDebug.JMSCommon.debug("\n   *** Peer > 7 WriteReplace return this ***\n");
         }

         return this;
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      if (var1 instanceof PersistentStoreOutputStream) {
         this.writeDestinationImpl(var1, this.distributedConfigMbeanName);
      } else {
         PeerInfo var2 = var1 instanceof PeerInfoable ? ((PeerInfoable)var1).getPeerInfo() : null;
         if (var2 != null) {
            Object var3 = this.interopWriteReplace(var2);
            if (!(var3 instanceof DistributedDestinationImpl)) {
               this.writeDestinationImpl(var1);
               return;
            }
         }

         this.writeDistributedDestinationImpl(var1, var2);
      }
   }

   private void writeDistributedDestinationImpl(ObjectOutput var1, PeerInfo var2) throws IOException {
      int var3 = 0;
      byte var4 = 1;
      var3 |= var4 << 8;
      var3 |= 32;
      var3 |= 16;
      if (this.ddJNDIName != null) {
         var3 |= 2;
      }

      if (this.hasStore) {
         var3 |= 4;
      }

      if (this.serverAffinityEnabled) {
         var3 |= 128;
      }

      int var5 = this.nonSystemSubscriberConsumers;
      if (var5 != 0) {
         var3 |= 8;
      }

      if (this.isQueueForward) {
         var3 |= 64;
      }

      var3 |= 4096;
      if (this.messageForwardingPolicy == 0) {
         var3 |= 1;
      }

      var1.writeShort(var3);
      this.writeDestinationImpl(var1);
      if (var1 instanceof WLObjectOutput) {
         ((WLObjectOutput)var1).writeAbbrevString(this.distributedConfigMbeanName);
         if ((var3 & 2) != 0) {
            ((WLObjectOutput)var1).writeAbbrevString(this.ddJNDIName);
         }
      } else {
         var1.writeUTF(this.distributedConfigMbeanName);
         if ((var3 & 2) != 0) {
            var1.writeUTF(this.ddJNDIName);
         }
      }

      var1.writeInt(this.loadBalancePolicy);
      var1.writeInt(this.weight);
      if (var5 != 0) {
         var1.writeLong((long)var5);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      boolean var2 = false;
      short var3 = var1.readShort();
      short var5;
      if ((var3 & 4096) == 0) {
         var5 = var3;
      } else {
         byte var4 = (byte)((var3 & 3840) >>> 8 & 255);
         if (var4 != 1) {
            throw JMSUtilities.versionIOException(var4, 1, 1);
         }

         var5 = var1.readShort();
      }

      this.readDestinationImpl(var1, var5);
      JMSDispatcher var6 = JMSDispatcherManager.getLocalDispatcher();
      this.isLocal = var6.getId().equals(this.dispatcherId);
      if ((var3 & 4096) == 0) {
         this.distributedConfigMbeanName = super.getDestinationName();
      } else {
         if (var1 instanceof WLObjectInput) {
            this.distributedConfigMbeanName = ((WLObjectInput)var1).readAbbrevString();
            if ((var3 & 2) != 0) {
               this.ddJNDIName = ((WLObjectInput)var1).readAbbrevString();
            }
         } else {
            this.distributedConfigMbeanName = var1.readUTF();
            if ((var3 & 2) != 0) {
               this.ddJNDIName = var1.readUTF();
            }
         }

         this.loadBalancePolicy = var1.readInt();
         if ((var3 & 1) != 0) {
            this.messageForwardingPolicy = 0;
         } else {
            this.messageForwardingPolicy = 1;
         }

         this.weight = var1.readInt();
         if ((var3 & 4) != 0) {
            this.hasStore = true;
         }

         if ((var3 & 128) != 0) {
            this.serverAffinityEnabled = true;
         } else {
            this.serverAffinityEnabled = false;
         }

         if ((var3 & 72) != 0) {
            if ((var3 & 8) != 0) {
               this.nonSystemSubscriberConsumers = (int)var1.readLong();
            }

            if ((var3 & 64) != 0) {
               this.isQueueForward = true;
            }
         }

      }
   }
}
