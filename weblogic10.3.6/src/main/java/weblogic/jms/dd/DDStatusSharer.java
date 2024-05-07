package weblogic.jms.dd;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.AccessController;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.naming.NamingException;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSService;
import weblogic.jms.common.JMSUtilities;
import weblogic.jndi.Aggregatable;
import weblogic.jndi.internal.NamingNode;
import weblogic.management.provider.ManagementService;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class DDStatusSharer implements DDStatusListener, Aggregatable, Externalizable {
   static final long serialVersionUID = -3705735684883464847L;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private DDHandler ddHandler;
   private static int VERSION_FARALON_PS1 = 1;
   private static int EXTVERSION = 2;
   private static final int VERSION_SHIFT = 3;
   private static final int VERSION_MASK = 7;
   private static final int NO_APPLICATION_NAME = 8;
   private static final int NO_MODULE_NAME = 16;
   private static final int IS_ROUTING_PATHSERVICE = 32;
   private static final int NO_SAF_EXPORT_POLICY = 64;
   private static final int IS_ROUTING_HASH = 128;
   private static final int NO_JNDI_NAME = 256;
   private static final int IS_QUEUE = 512;
   private static final int IS_UOW_DESTINATION = 1024;
   private static final int HAS_REFERENCE_NAME = 2048;
   private static final int RESET_DELIVERY_COUNT = 4096;
   private List remoteMemberList;
   private boolean everHadMembers = false;
   private String serverName;

   public DDStatusSharer() {
   }

   public DDStatusSharer(DDHandler var1) {
      this.ddHandler = var1;
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.serverName = ManagementService.getRuntimeAccess(var2).getServer().getName();
      if (var1.getNumberOfMembers() != 0) {
         this.everHadMembers = true;
      }

      var1.addStatusListener(this, 29);
   }

   public void statusChangeNotification(DDHandler var1, int var2) {
      if (var1.isLocal()) {
         if (!this.everHadMembers) {
            if (var1.getNumberOfMembers() == 0) {
               return;
            }

            this.everHadMembers = true;
         }

         String var3 = "weblogic.jms.internal.dd." + this.serverName + "." + var1.getName();

         try {
            if (!var1.isActive()) {
               PrivilegedActionUtilities.unbindAsSU(JMSService.getContext(true), var3, kernelId);
            } else {
               PrivilegedActionUtilities.rebindAsSU(JMSService.getContext(true), var3, this, kernelId);
            }
         } catch (NamingException var5) {
         }

      }
   }

   public void onBind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      DDStatusSharer var4;
      if (var3 == null) {
         var4 = this;
      } else {
         var4 = (DDStatusSharer)var3;
      }

      DDManager.remoteUpdate(var4.ddHandler, var4.remoteMemberList);
   }

   public void onRebind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      this.onBind(var1, var2, var3);
   }

   public boolean onUnbind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      DDManager.remoteDeactivate(this.ddHandler.getName());
      return true;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.ddHandler = new DDHandler();
      int var2 = var1.readInt();
      int var3 = var2 & 7;
      boolean var4;
      if (var3 == EXTVERSION) {
         var4 = true;
      } else {
         if (var3 != VERSION_FARALON_PS1) {
            throw JMSUtilities.versionIOException(var3, EXTVERSION, VERSION_FARALON_PS1);
         }

         var4 = false;
      }

      this.ddHandler.setIsQueue((var2 & 512) != 0);
      this.ddHandler.setIsUOWDestination((var2 & 1024) != 0);
      this.ddHandler.setName(var1.readUTF());
      if ((var2 & 8) == 0) {
         this.ddHandler.setApplicationName(var1.readUTF());
      }

      if ((var2 & 16) == 0) {
         this.ddHandler.setEARModuleName(var1.readUTF());
      }

      if ((var2 & 64) == 0) {
         this.ddHandler.setSAFExportPolicy(var1.readUTF());
      }

      if ((var2 & 128) != 0) {
         this.ddHandler.setUnitOfOrderRouting("Hash".intern());
      } else if ((var2 & 32) != 0) {
         this.ddHandler.setUnitOfOrderRouting("PathService".intern());
      }

      if ((var2 & 256) == 0) {
         this.ddHandler.setJNDIName(var1.readUTF());
      }

      this.ddHandler.setLoadBalancingPolicyAsInt(var1.readInt());
      this.ddHandler.setForwardDelay(var1.readInt());
      int var5 = var1.readInt();
      this.remoteMemberList = new LinkedList();

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var1.readUTF();
         this.remoteMemberList.add(var7);
      }

      if ((var2 & 2048) != 0) {
         this.ddHandler.setReferenceName(var1.readUTF());
      }

      this.ddHandler.setResetDeliveryCountOnForward((var2 & 4096) != 0);
      if (var4) {
         this.ddHandler.setForwardingPolicy(var1.readInt());
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2;
      boolean var3;
      if (var1 instanceof PeerInfoable && ((PeerInfoable)var1).getPeerInfo().compareTo(PeerInfo.VERSION_1033) < 0) {
         var2 = VERSION_FARALON_PS1;
         var3 = false;
      } else {
         var2 = EXTVERSION;
         var3 = true;
      }

      String var4 = this.ddHandler.getUnitOfOrderRouting();
      if (var4 != null) {
         if (var4.equals("Hash")) {
            var2 |= 128;
         } else if (var4.equals("PathService")) {
            var2 |= 32;
         }
      }

      if (this.ddHandler.getApplicationName() == null) {
         var2 |= 8;
      }

      if (this.ddHandler.getEARModuleName() == null) {
         var2 |= 16;
      }

      if (this.ddHandler.getSAFExportPolicy() == null) {
         var2 |= 64;
      }

      if (this.ddHandler.getJNDIName() == null) {
         var2 |= 256;
      }

      if (this.ddHandler.isQueue()) {
         var2 |= 512;
      }

      if (this.ddHandler.isUOWDestination()) {
         var2 |= 1024;
      }

      if (this.ddHandler.getReferenceName() != null) {
         var2 |= 2048;
      }

      if (this.ddHandler.getResetDeliveryCountOnForward()) {
         var2 |= 4096;
      }

      var1.writeInt(var2);
      var1.writeUTF(this.ddHandler.getName());
      if ((var2 & 8) == 0) {
         var1.writeUTF(this.ddHandler.getApplicationName());
      }

      if ((var2 & 16) == 0) {
         var1.writeUTF(this.ddHandler.getEARModuleName());
      }

      if ((var2 & 64) == 0) {
         var1.writeUTF(this.ddHandler.getSAFExportPolicy());
      }

      if ((var2 & 256) == 0) {
         var1.writeUTF(this.ddHandler.getJNDIName());
      }

      var1.writeInt(this.ddHandler.getLoadBalancingPolicyAsInt());
      var1.writeInt(this.ddHandler.getForwardDelay());
      if (this.ddHandler.isActive()) {
         Iterator var5 = this.ddHandler.memberCloneIterator();
         this.remoteMemberList = new LinkedList();

         while(var5.hasNext()) {
            DDMember var6 = (DDMember)var5.next();
            this.remoteMemberList.add(var6.getName());
         }
      }

      var1.writeInt(this.remoteMemberList.size());
      ListIterator var7 = this.remoteMemberList.listIterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         var1.writeUTF(var8);
      }

      if (this.ddHandler.isActive()) {
         this.remoteMemberList = null;
      }

      if ((var2 & 2048) != 0) {
         var1.writeUTF(this.ddHandler.getReferenceName());
      }

      if (var3) {
         var1.writeInt(this.ddHandler.getForwardingPolicy());
      }

   }

   public boolean equals(Object var1) {
      return var1 instanceof DDStatusSharer ? ((DDStatusSharer)var1).ddHandler.getName().equals(this.ddHandler.getName()) : false;
   }

   public int hashCode() {
      return this.ddHandler.getName().hashCode();
   }

   public String toString() {
      return "DDStatusSharer: " + this.ddHandler.getName() + ", hash: " + this.hashCode();
   }
}
