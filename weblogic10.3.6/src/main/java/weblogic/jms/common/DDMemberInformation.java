package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.cache.CacheEntry;
import weblogic.messaging.dispatcher.DispatcherId;

public class DDMemberInformation implements CacheEntry, Externalizable {
   static final long serialVersionUID = 7307718114460216150L;
   private static final int EXTVERSIONDIABLO = 1;
   private static final int EXTVERSIONDANTE = 2;
   private static final int EXTVERSIONCORDELL = 3;
   private static final int VERSION_MASK = 255;
   private static final int _HAS_DD_TYPE = 256;
   private static final int _HAS_DD_JNDI_NAME = 512;
   private static final int _HAS_DD_MEMBER_JNDI_NAME = 1024;
   private static final int _HAS_DD_MEMBER_LOCAL_JNDI_NAME = 2048;
   private static final int _HAS_DD_MEMBER_CLUSTER_NAME = 4096;
   private static final int _HAS_MIGRATABLE_TARGET_NAME = 8192;
   private static final int _HAS_APPLICATION_NAME = 16384;
   private static final int _HAS_MODULE_NAME = 32768;
   private static final int _HAS_DESTINATION_ID = 65536;
   private static final int _IS_INSERTION_PAUSED = 131072;
   private static final int _IS_PRODUCTION_PAUSED = 262144;
   private static final int _IS_CONSUMPTION_PAUSED = 524288;
   private static final int _HAS_DOMAIN_NAME = 1048576;
   private static final int _HAS_FORWARDING_POLICY = 2097152;
   private static final int _HAS_WLS_SERVER_NAME = 4194304;
   private static final int _HAS_PERSISTENT_STORE_NAME = 8388608;
   private static final int _IS_10_3_4_OR_LATER = 16777216;
   private String ddConfigName;
   private String ddType;
   private String ddJNDIName;
   private String ddMemberJndiName;
   private String ddMemberServerName;
   private String ddMemberLocalJndiName;
   private String ddMemberClusterName;
   private String ddMemberMigratableTargetName;
   private DestinationImpl dImpl;
   boolean isConsumptionPaused;
   boolean isInsertionPaused;
   boolean isProductionPaused;
   private String ddMemberDomainName;
   private boolean fromWire;
   private transient boolean isLocalDD;
   private int forwardingPolicy;
   private boolean isPre10_3_4;

   public DDMemberInformation(String var1, String var2, String var3, DestinationImpl var4, String var5, String var6, String var7, String var8, String var9) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, false);
   }

   public DDMemberInformation(String var1, String var2, String var3, DestinationImpl var4, String var5, String var6, String var7, String var8, String var9, boolean var10) {
      this(var1, var2, var3, 1, var4, var5, var6, var7, var8, var9, false, false, false, var10);
   }

   public DDMemberInformation(String var1, String var2, String var3, int var4, DestinationImpl var5, String var6, String var7, String var8, String var9, String var10, boolean var11, boolean var12, boolean var13, boolean var14) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, (String)null, var11, var12, var13, var14);
   }

   public DDMemberInformation(String var1, String var2, String var3, int var4, DestinationImpl var5, String var6, String var7, String var8, String var9, String var10, String var11, boolean var12, boolean var13, boolean var14, boolean var15) {
      this.ddConfigName = null;
      this.ddType = null;
      this.ddJNDIName = null;
      this.ddMemberJndiName = null;
      this.ddMemberLocalJndiName = null;
      this.ddMemberClusterName = null;
      this.ddMemberMigratableTargetName = null;
      this.isConsumptionPaused = false;
      this.isInsertionPaused = false;
      this.isProductionPaused = false;
      this.ddMemberDomainName = null;
      this.fromWire = false;
      this.isLocalDD = false;
      this.forwardingPolicy = 1;
      this.isPre10_3_4 = true;
      this.ddConfigName = var1;
      this.ddType = var2;
      this.ddJNDIName = var3;
      this.forwardingPolicy = var4;
      this.dImpl = var5;
      this.ddMemberServerName = var6;
      this.ddMemberJndiName = var7;
      this.ddMemberLocalJndiName = var8;
      this.ddMemberClusterName = var9;
      this.ddMemberMigratableTargetName = var10;
      this.ddMemberDomainName = var11;
      this.isConsumptionPaused = var12;
      this.isInsertionPaused = var13;
      this.isProductionPaused = var14;
      this.isLocalDD = var15;
      if (var5 != null) {
         this.isPre10_3_4 = var5.isPre10_3_4();
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof DDMemberInformation)) {
         return false;
      } else {
         DDMemberInformation var2;
         label44: {
            var2 = (DDMemberInformation)var1;
            if (this.dImpl != null) {
               if (this.dImpl.equals(var2.dImpl)) {
                  break label44;
               }
            } else if (var2.dImpl == null) {
               break label44;
            }

            return false;
         }

         if (this.ddConfigName != null) {
            if (!this.ddConfigName.equals(var2.ddConfigName)) {
               return false;
            }
         } else if (var2.ddConfigName != null) {
            return false;
         }

         if (this.ddType != null) {
            if (!this.ddType.equals(var2.ddType)) {
               return false;
            }
         } else if (var2.ddType != null) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int var1 = this.ddConfigName != null ? this.ddConfigName.hashCode() : 0;
      var1 = 29 * var1 + (this.ddType != null ? this.ddType.hashCode() : 0);
      var1 = 29 * var1 + (this.dImpl != null ? this.dImpl.hashCode() : 0);
      return var1;
   }

   public boolean isConsumptionPaused() {
      return this.isConsumptionPaused;
   }

   public boolean isInsertionPaused() {
      return this.isInsertionPaused;
   }

   public boolean isProductionPaused() {
      return this.isProductionPaused;
   }

   public boolean isDD() {
      if (this.fromWire) {
         return true;
      } else {
         return this.isLocalDD;
      }
   }

   public String getName() {
      return this.getDDJNDIName();
   }

   public String getMemberName() {
      return this.dImpl == null ? null : this.dImpl.getName();
   }

   public String getDDType() {
      return this.ddType;
   }

   public String getDDConfigName() {
      return this.ddConfigName;
   }

   public String getDDJNDIName() {
      return this.ddJNDIName;
   }

   public String getDDMemberJndiName() {
      return this.ddMemberJndiName;
   }

   public Destination getDestination() {
      return this.dImpl;
   }

   public String getDDMemberLocalJndiName() {
      return this.ddMemberLocalJndiName;
   }

   public String getDDMemberServerName() {
      return this.ddMemberServerName;
   }

   public String getDDMemberClusterName() {
      return this.ddMemberClusterName;
   }

   public String getDDMemberMigratableTargetName() {
      return this.ddMemberMigratableTargetName;
   }

   public String getDDMemberDomainName() {
      return this.ddMemberDomainName;
   }

   public int getForwardingPolicy() {
      return this.forwardingPolicy;
   }

   public boolean isAdvancedTopicSupported() {
      return !this.isPre10_3_4;
   }

   public DDMemberInformation() {
      this.ddConfigName = null;
      this.ddType = null;
      this.ddJNDIName = null;
      this.ddMemberJndiName = null;
      this.ddMemberLocalJndiName = null;
      this.ddMemberClusterName = null;
      this.ddMemberMigratableTargetName = null;
      this.isConsumptionPaused = false;
      this.isInsertionPaused = false;
      this.isProductionPaused = false;
      this.ddMemberDomainName = null;
      this.fromWire = false;
      this.isLocalDD = false;
      this.forwardingPolicy = 1;
      this.isPre10_3_4 = true;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = this.getVersion(var1);
      int var3 = var2;
      if (this.ddType != null) {
         var3 = var2 | 256;
      }

      if (this.ddJNDIName != null) {
         var3 |= 512;
      }

      if (this.ddMemberJndiName != null) {
         var3 |= 1024;
      }

      if (this.ddMemberLocalJndiName != null) {
         var3 |= 2048;
      }

      if (this.ddMemberClusterName != null) {
         var3 |= 4096;
      }

      if (this.ddMemberMigratableTargetName != null) {
         var3 |= 8192;
      }

      if (this.dImpl.getId() != null) {
         var3 |= 65536;
      }

      if (this.isConsumptionPaused) {
         var3 |= 524288;
      }

      if (this.isInsertionPaused) {
         var3 |= 131072;
      }

      if (this.isProductionPaused) {
         var3 |= 262144;
      }

      if (var2 > 1 && this.ddMemberDomainName != null) {
         var3 |= 1048576;
      }

      if (var2 >= 3) {
         var3 |= 2097152;
         if (this.ddMemberServerName != null) {
            var3 |= 4194304;
         }

         if (this.dImpl.getApplicationName() != null) {
            var3 |= 16384;
         }

         if (this.dImpl.getModuleName() != null) {
            var3 |= 32768;
         }

         if (this.dImpl.getApplicationName() != null) {
            var3 |= 16384;
         }

         if (this.dImpl.getPersistentStoreName() != null) {
            var3 |= 8388608;
         }
      }

      if (!this.isPre10_3_4) {
         var3 |= 16777216;
      }

      var1.writeInt(var3);
      var1.writeUTF(this.ddConfigName);
      if ((var3 & 256) != 0) {
         var1.writeUTF(this.ddType);
      }

      if ((var3 & 512) != 0) {
         var1.writeUTF(this.ddJNDIName);
      }

      if ((var3 & 1024) != 0) {
         var1.writeUTF(this.ddMemberJndiName);
      }

      if ((var3 & 2048) != 0) {
         var1.writeUTF(this.ddMemberLocalJndiName);
      }

      if ((var3 & 4096) != 0) {
         var1.writeUTF(this.ddMemberClusterName);
      }

      if ((var3 & 8192) != 0) {
         var1.writeUTF(this.ddMemberMigratableTargetName);
      }

      if ((var3 & 1048576) != 0) {
         var1.writeUTF(this.ddMemberDomainName);
      }

      if ((var3 & 2097152) != 0) {
         var1.writeInt(this.forwardingPolicy);
      }

      var1.writeInt(this.dImpl.getType());
      var1.writeUTF(this.dImpl.getServerName());
      var1.writeUTF(this.getMemberName());
      if ((var3 & 16384) != 0) {
         var1.writeUTF(this.dImpl.getApplicationName());
      }

      if ((var3 & '耀') != 0) {
         var1.writeUTF(this.dImpl.getModuleName());
      }

      this.dImpl.getBackEndId().writeExternal(var1);
      if ((var3 & 65536) != 0) {
         this.dImpl.getId().writeExternal(var1);
      }

      this.dImpl.getDispatcherId().writeExternal(var1);
      if ((var3 & 4194304) != 0) {
         var1.writeUTF(this.ddMemberServerName);
      }

      if ((var3 & 8388608) != 0) {
         var1.writeUTF(this.dImpl.getPersistentStoreName());
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1 && var3 != 2 && var3 != 3) {
         throw JMSUtilities.versionIOException(var3, 1, 3);
      } else {
         if ((var2 & 524288) != 0) {
            this.isConsumptionPaused = true;
         }

         if ((var2 & 131072) != 0) {
            this.isInsertionPaused = true;
         }

         if ((var2 & 262144) != 0) {
            this.isProductionPaused = true;
         }

         this.ddConfigName = var1.readUTF();
         if ((var2 & 256) != 0) {
            this.ddType = var1.readUTF();
         }

         if ((var2 & 512) != 0) {
            this.ddJNDIName = var1.readUTF();
         }

         if ((var2 & 1024) != 0) {
            this.ddMemberJndiName = var1.readUTF();
         }

         if ((var2 & 2048) != 0) {
            this.ddMemberLocalJndiName = var1.readUTF();
         }

         if ((var2 & 4096) != 0) {
            this.ddMemberClusterName = var1.readUTF();
         }

         if ((var2 & 8192) != 0) {
            this.ddMemberMigratableTargetName = var1.readUTF();
         }

         if ((var2 & 1048576) != 0) {
            this.ddMemberDomainName = var1.readUTF();
         }

         if ((var2 & 2097152) != 0) {
            this.forwardingPolicy = var1.readInt();
         }

         int var4 = var1.readInt();
         String var5 = var1.readUTF();
         String var6 = var1.readUTF();
         String var7 = null;
         if ((var2 & 16384) != 0) {
            var7 = var1.readUTF();
         }

         String var8 = null;
         if ((var2 & '耀') != 0) {
            var8 = var1.readUTF();
         }

         JMSServerId var9 = new JMSServerId();
         var9.readExternal(var1);
         JMSID var10 = null;
         if ((var2 & 65536) != 0) {
            var10 = new JMSID();
            var10.readExternal(var1);
         }

         DispatcherId var11 = new DispatcherId();
         var11.readExternal(var1);
         if ((var2 & 4194304) != 0) {
            this.ddMemberServerName = var1.readUTF();
         }

         String var12 = null;
         if ((var2 & 8388608) != 0) {
            var12 = var1.readUTF();
         }

         if ((var2 & 16777216) != 0) {
            this.isPre10_3_4 = false;
         }

         this.dImpl = new DestinationImpl(var4, var5, var12, var6, var7, var8, var9, var10, var11);
         this.fromWire = true;
      }
   }

   public String toString() {
      return new String("\nDDMemberInformation : \n  DD Type                          = " + this.ddType + "\n" + (this.ddType.equals("javax.jms.Topic") ? "  DD ForwardingPolicy              = " + this.getForwardingPolicy() : "") + "\n" + "  DD Config Name                   = " + this.getDDConfigName() + "\n" + "  DD JNDI Name                     = " + this.ddJNDIName + "\n" + "  DD Member JNDI Name              = " + this.ddMemberJndiName + "\n" + "  DD Member Name                   = " + this.getMemberName() + "\n" + "  DD Member Consumption Paused     = " + this.isConsumptionPaused + "\n" + "  DD Member Insertion Paused       = " + this.isInsertionPaused + "\n" + "  DD Member Production Paused      = " + this.isProductionPaused + "\n" + "  DD Member Local JNDI Name        = " + this.ddMemberLocalJndiName + "\n" + "  DD Member Server Name            = " + this.ddMemberServerName + "\n" + "  DD Member Cluster Name           = " + this.ddMemberClusterName + "\n" + "  DD Member Migratable Target Name = " + this.ddMemberMigratableTargetName + "\n" + "  DD Member Domain Name            = " + this.ddMemberDomainName + "\n" + "  DD Member AdvancedTopicSupported = " + this.isAdvancedTopicSupported() + "\n");
   }

   private int getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         if (var2.compareTo(PeerInfo.VERSION_DIABLO) < 0) {
            throw JMSUtilities.versionIOException(0, 1, 3);
         }

         if (var2.compareTo(PeerInfo.VERSION_1000) < 0) {
            return 1;
         }

         if (var2.compareTo(PeerInfo.VERSION_1033) < 0) {
            return 2;
         }
      }

      return 3;
   }
}
