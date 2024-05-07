package weblogic.jms.dd;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Hashtable;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSService;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSUtilities;
import weblogic.jndi.Aggregatable;
import weblogic.jndi.internal.NamingNode;
import weblogic.management.provider.ManagementService;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class DDMemberStatusSharer implements MemberStatusListener, Aggregatable, Externalizable {
   static final long serialVersionUID = 3562221018441394798L;
   private DDMember member;
   private static final int VERSION_SHIFT = 3;
   private static final int VERSION_MASK = 7;
   private static final int EXTVERSION = 1;
   private static final int EXTVERSION_DANTE = 2;
   private static final int EXTVERSION_CORDELL = 3;
   private static final int IS_PRODUCTION_PAUSED = 8;
   private static final int IS_CONSUMPTION_PAUSED = 16;
   private static final int IS_INSERTION_PAUSED = 32;
   private static final int IS_UP = 64;
   private static final int HAS_CONSUMERS = 128;
   private static final int IS_PERSISTENT = 256;
   private static final int HAS_GLOBAL_JNDI_NAME = 512;
   private static final int HAS_LOCAL_JNDI_NAME = 1024;
   private static final int HAS_MIGRATABLE_TARGET_NAME = 2048;
   private static final int HAS_DOMAIN_NAME = 4096;
   private static final int SECURITY_MASK = 24576;
   private static final int WANTS_UNSIGNED = 8192;
   private static final int WANTS_SIGNED = 16384;
   private static final int WANTS_SIGNEDFULL = 24576;
   private static final int WANTS_KERNELID = 0;
   private static final int HAS_MORE_FLAGS = 32768;
   private static final int HAS_STORE_NAME = 1;
   private boolean init = false;
   private String internalDistributedJNDIName;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public DDMemberStatusSharer(DDMember var1) {
      this.member = var1;
      var1.addStatusListener(this);
   }

   public DDMemberStatusSharer() {
   }

   private void init() {
      String var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getName();
      this.internalDistributedJNDIName = "weblogic.jms.internal.ddmember." + this.member.getName() + "." + var1;
      this.init = true;
   }

   public void doBind() {
      if (this.member.isLocal()) {
         if (!this.init) {
            this.init();
         }

         try {
            PrivilegedActionUtilities.rebindAsSU(JMSService.getContext(true), this.internalDistributedJNDIName, this, kernelId);
         } catch (NamingException var2) {
         }

      }
   }

   public void doUnbind() {
      if (!this.init) {
         this.init();
      }

      try {
         PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(true), this.internalDistributedJNDIName, this, kernelId);
      } catch (NamingException var2) {
      }

   }

   public void memberStatusChange(DDMember var1, int var2) {
      if (!var1.isDestinationUp()) {
         if ((var2 & 16) != 0) {
            var1.removeStatusListener(this);
            this.doUnbind();
         }
      } else {
         this.doBind();
      }

   }

   public void onBind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      if (var3 == null) {
         var3 = this;
      }

      DDManager.memberUpdate(((DDMemberStatusSharer)var3).member);
   }

   public void onRebind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      this.onBind(var1, var2, var3);
   }

   private boolean checkForReplacement(NamingNode var1, String var2) {
      try {
         NamingEnumeration var3 = var1.listBindings("", (Hashtable)null);

         while(var3.hasMoreElements()) {
            NameClassPair var4 = (NameClassPair)var3.nextElement();
            if (!var4.getName().equals(var2)) {
               DDManager.memberUpdate(((DDMemberStatusSharer)var1.lookupLink(var4.getName(), (Hashtable)null)).member);
            }
         }
      } catch (NamingException var5) {
         var5.printStackTrace();
      } catch (RemoteException var6) {
         var6.printStackTrace();
      }

      return false;
   }

   public boolean onUnbind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      if (this.checkForReplacement(var1, var2)) {
         return true;
      } else {
         DDHandler var4 = DDManager.findDDHandlerByMemberName(this.member.getName());
         if (var4 != null) {
            DDMember var5 = var4.findMemberByName(this.member.getName());
            if (var5 != null) {
               synchronized(var5) {
                  var5.setIsUp(false);
                  var5.setHasConsumers(false);
               }
            }
         }

         return true;
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = this.getVersion(var1);
      short var3 = (short)var2;
      if (this.member.isProductionPaused()) {
         var3 = (short)(var3 | 8);
      }

      if (this.member.isConsumptionPaused()) {
         var3 = (short)(var3 | 16);
      }

      if (this.member.isInsertionPaused()) {
         var3 = (short)(var3 | 32);
      }

      if (this.member.isUp()) {
         var3 = (short)(var3 | 64);
      }

      if (this.member.hasConsumers()) {
         var3 = (short)(var3 | 128);
      }

      if (this.member.isPersistent()) {
         var3 = (short)(var3 | 256);
      }

      if (this.member.getMigratableTargetName() != null) {
         var3 = (short)(var3 | 2048);
      }

      if (var2 > 1 && this.member.getDomainName() != null) {
         var3 = (short)(var3 | 4096);
      }

      if (this.member.getGlobalJNDIName() != null) {
         var3 = (short)(var3 | 512);
      }

      if (this.member.getLocalJNDIName() != null) {
         var3 = (short)(var3 | 1024);
      }

      switch (this.member.getRemoteSecurityMode()) {
         case 11:
            var3 = (short)(var3 | 16384);
            break;
         case 12:
            var3 = (short)(var3 | 8192);
            break;
         case 13:
            var3 = (short)(var3 | 24576);
            break;
         case 14:
            var3 = (short)(var3 | 0);
            break;
         default:
            throw new AssertionError();
      }

      short var4;
      if (var2 >= 3) {
         var4 = 0;
         if (this.member.getPersistentStoreName() != null && this.member.getPersistentStoreName().length() != 0) {
            var4 = (short)(var4 | 1);
         }

         if (var4 != 0) {
            var3 = (short)(var3 | '耀');
         }
      } else {
         var4 = 0;
      }

      var1.writeShort(var3);
      var1.writeInt(this.member.getWeight());
      var1.writeUTF(this.member.getName());
      var1.writeUTF(this.member.getWLSServerName());
      var1.writeUTF(this.member.getJMSServerName());
      if ((var3 & 2048) != 0) {
         var1.writeUTF(this.member.getMigratableTargetName());
      }

      if ((var3 & 4096) != 0) {
         var1.writeUTF(this.member.getDomainName());
      }

      if ((var3 & 512) != 0) {
         var1.writeUTF(this.member.getGlobalJNDIName());
      }

      if ((var3 & 1024) != 0) {
         var1.writeUTF(this.member.getLocalJNDIName());
      }

      this.member.getBackEndId().writeExternal(var1);
      this.member.getDestinationId().writeExternal(var1);
      this.member.getDispatcherId().writeExternal(var1);
      if (var2 >= 3) {
         if ((var3 & '耀') != 0) {
            var1.writeShort(var4);
         }

         if ((var4 & 1) != 0) {
            var1.writeUTF(this.member.getPersistentStoreName());
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readShort();
      int var3 = var2 & 7;
      if (var3 != 1 && var3 != 2 && var3 != 3) {
         throw JMSUtilities.versionIOException(var3, 1, 3);
      } else {
         this.member = new DDMember();
         this.member.setIsProductionPaused((var2 & 8) != 0);
         this.member.setIsConsumptionPaused((var2 & 16) != 0);
         this.member.setIsInsertionPaused((var2 & 32) != 0);
         this.member.setHasConsumers((var2 & 128) != 0);
         this.member.setIsPersistent((var2 & 256) != 0);
         boolean var4 = false;
         byte var9;
         switch (var2 & 24576) {
            case 0:
               var9 = 14;
               break;
            case 8192:
               var9 = 12;
               break;
            case 16384:
               var9 = 11;
               break;
            case 24576:
               var9 = 13;
               break;
            default:
               throw new AssertionError();
         }

         this.member.setRemoteSecurityMode(var9);
         this.member.setWeight(var1.readInt());
         this.member.setName(var1.readUTF());
         this.member.setWLSServerName(var1.readUTF());
         this.member.setJMSServerName(var1.readUTF());
         if ((var2 & 2048) != 0) {
            this.member.setMigratableTargetName(var1.readUTF());
         }

         if ((var2 & 4096) != 0) {
            this.member.setDomainName(var1.readUTF());
         }

         if ((var2 & 512) != 0) {
            this.member.setGlobalJNDIName(var1.readUTF());
         }

         if ((var2 & 1024) != 0) {
            this.member.setLocalJNDIName(var1.readUTF());
         }

         JMSServerId var5 = new JMSServerId();
         var5.readExternal(var1);
         this.member.setBackEndId(var5);
         JMSID var6 = new JMSID();
         var6.readExternal(var1);
         this.member.setDestinationId(var6);
         DispatcherId var7 = new DispatcherId();
         var7.readExternal(var1);
         this.member.setDispatcherId(var7);
         short var8 = 0;
         if ((var2 & '耀') != 0) {
            var8 = var1.readShort();
         }

         if ((var8 & 1) != 0) {
            this.member.setPersistentStoreName(var1.readUTF());
         }

         this.member.setIsUp((var2 & 64) != 0);
      }
   }

   public boolean equals(Object var1) {
      return var1 instanceof DDMemberStatusSharer && ((DDMemberStatusSharer)var1).member.getName().equals(this.member.getName());
   }

   public int hashCode() {
      return this.member.getName().hashCode();
   }

   public String toString() {
      return "DDMemberStatusSharer: " + this.member.getName() + ", hash: " + this.hashCode();
   }

   private int getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         if (var2.compareTo(PeerInfo.VERSION_DIABLO) < 0) {
            throw JMSUtilities.versionIOException(0, 1, 3);
         }

         if (var2.compareTo(PeerInfo.VERSION_920) <= 0) {
            return 1;
         }

         if (var2.compareTo(PeerInfo.VERSION_1030) <= 0) {
            return 2;
         }
      }

      return 3;
   }
}
