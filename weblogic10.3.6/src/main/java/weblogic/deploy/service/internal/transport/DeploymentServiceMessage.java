package weblogic.deploy.service.internal.transport;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.deploy.service.internal.DomainVersion;
import weblogic.deploy.service.internal.adminserver.AdminRequestImpl;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.ArrayUtils;

public final class DeploymentServiceMessage implements Externalizable, Cloneable {
   private static final long serialVersionUID = -566288665135473593L;
   public static final byte HEARTBEAT = 0;
   public static final byte REQUEST_PREPARE = 1;
   public static final byte REQUEST_COMMIT = 2;
   public static final byte REQUEST_CANCEL = 3;
   public static final byte GET_DEPLOYMENTS = 4;
   public static final byte GET_DEPLOYMENTS_RESPONSE = 5;
   public static final byte PREPARE_ACK = 6;
   public static final byte PREPARE_NAK = 7;
   public static final byte COMMIT_SUCCEEDED = 8;
   public static final byte COMMIT_FAILED = 9;
   public static final byte CANCEL_SUCCEEDED = 10;
   public static final byte CANCEL_FAILED = 11;
   public static final byte REQUEST_STATUS = 12;
   public static final byte BLOCKING_GET_DEPLOYMENTS = 13;
   private byte deploymentServiceVersion;
   private final byte currentDeploymentServiceVersion;
   private byte messageType;
   private long deploymentId;
   private long timeoutValue = -1L;
   private boolean callConfigurationProviderLast;
   private String messageSrc;
   private transient Set targets = null;
   private ArrayList items = null;
   private DomainVersion fromVersion = null;
   private DomainVersion toVersion = null;
   private AuthenticatedSubject initiator = null;
   private String deploymentType = null;
   private boolean needsVersionUpdate = true;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public DeploymentServiceMessage() {
      DeploymentService.getDeploymentService();
      this.currentDeploymentServiceVersion = DeploymentService.getVersionByte();
   }

   public DeploymentServiceMessage(byte var1, byte var2, AdminRequestImpl var3, String var4) {
      DeploymentService.getDeploymentService();
      this.currentDeploymentServiceVersion = DeploymentService.getVersionByte();
      this.deploymentServiceVersion = var1;
      this.messageType = var2;
      this.messageSrc = ManagementService.getRuntimeAccess(kernelId).getServerName();
      if (var3 != null) {
         this.deploymentId = var3.getId();
         this.timeoutValue = var3.getTimeoutInterval();
         this.callConfigurationProviderLast = var3.isConfigurationProviderCalledLast();
         this.initiator = var3.getInitiator();
         this.targets = new HashSet();
         Iterator var5 = var3.getTargets();
         if (this.targets != null) {
            while(var5.hasNext()) {
               this.targets.add((String)var5.next());
            }
         }

         if (this.items == null) {
            this.items = new ArrayList();
         }

         Iterator var6 = var3.getDeployments();

         while(var6.hasNext()) {
            Deployment var7 = (Deployment)var6.next();
            String[] var8 = var7.getTargets();
            boolean var9 = false;
            if (!"Configuration".equals(var7.getCallbackHandlerId()) && !"Application".equals(var7.getCallbackHandlerId())) {
               if (var4 == null || var8 == null || var8.length == 0 || ArrayUtils.contains(var8, var4)) {
                  var9 = true;
               }
            } else {
               var9 = true;
            }

            if (var9) {
               this.items.add(var7);
            }
         }

         this.needsVersionUpdate = !var3.isControlRequest();
      }

   }

   public DeploymentServiceMessage(byte var1, byte var2, long var3, ArrayList var5) {
      DeploymentService.getDeploymentService();
      this.currentDeploymentServiceVersion = DeploymentService.getVersionByte();
      this.deploymentServiceVersion = var1;
      this.messageType = var2;
      this.deploymentId = var3;
      this.items = var5;
      if (ManagementService.getPropertyService(kernelId).serverNameIsSet()) {
         this.messageSrc = ManagementService.getPropertyService(kernelId).getServerName();
      } else {
         this.messageSrc = null;
      }

   }

   public final long getDeploymentId() {
      return this.deploymentId;
   }

   public final long getTimeoutInterval() {
      return this.timeoutValue;
   }

   public final boolean isConfigurationProviderCalledLast() {
      return this.callConfigurationProviderLast;
   }

   private void setTargets(Set var1) {
      this.targets = var1;
   }

   public final Set getTargets() {
      return this.targets;
   }

   public final byte getVersion() {
      return this.deploymentServiceVersion;
   }

   public final byte getMessageType() {
      return this.messageType;
   }

   public final ArrayList getItems() {
      return this.items;
   }

   public final String getMessageSrc() {
      return this.messageSrc;
   }

   public final AuthenticatedSubject getInitiator() {
      return this.initiator;
   }

   private static String getMessageTypeString(byte var0) {
      switch (var0) {
         case 0:
            return "HEARTBEAT";
         case 1:
            return "REQUEST_PREPARE";
         case 2:
            return "REQUEST_COMMIT";
         case 3:
            return "REQUEST_CANCEL";
         case 4:
            return "GET_DEPLOYMENTS";
         case 5:
            return "GET_DEPLOYMENTS_RESPONSE";
         case 6:
            return "PREPARE_ACK";
         case 7:
            return "PREPARE_NAK";
         case 8:
            return "COMMIT_SUCCEEDED";
         case 9:
            return "COMMIT_FAILED";
         case 10:
            return "CANCEL_SUCCEEDED";
         case 11:
            return "CANCEL_FAILED";
         case 12:
            return "REQUEST_STATUS";
         case 13:
            return "BLOCKING_GET_DEPLOYMENTS";
         default:
            return "ILLEGAL";
      }
   }

   public final Object clone() {
      DeploymentServiceMessage var1 = new DeploymentServiceMessage(this.deploymentServiceVersion, this.messageType, this.deploymentId, (ArrayList)this.items.clone());
      var1.setTargets(this.targets);
      return var1;
   }

   public final void setFromVersion(DomainVersion var1) {
      this.fromVersion = var1;
   }

   public final DomainVersion getFromVersion() {
      return this.fromVersion;
   }

   public final void setToVersion(DomainVersion var1) {
      this.toVersion = var1;
   }

   public final DomainVersion getToVersion() {
      return this.toVersion;
   }

   public final boolean needsVersionUpdate() {
      return this.needsVersionUpdate;
   }

   public final void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(this.deploymentServiceVersion);
      this.writeMessage(this.deploymentServiceVersion, var1);
   }

   private void writeMessage(byte var1, ObjectOutput var2) throws IOException {
      if (var1 == this.currentDeploymentServiceVersion) {
         var2.writeByte(this.messageType);
         var2.writeLong(this.deploymentId);
         var2.writeLong(this.timeoutValue);
         var2.writeBoolean(this.callConfigurationProviderLast);
         ((WLObjectOutput)var2).writeString(this.messageSrc);
         boolean var3 = this.items != null;
         var2.writeBoolean(var3);
         if (var3) {
            ((WLObjectOutput)var2).writeArrayList(this.items);
         }

         boolean var4 = this.fromVersion != null;
         var2.writeBoolean(var4);
         if (var4) {
            var2.writeObject(this.fromVersion);
         }

         boolean var5 = this.toVersion != null;
         var2.writeBoolean(var5);
         if (var5) {
            var2.writeObject(this.toVersion);
         }

         boolean var6 = this.initiator != null;
         var2.writeBoolean(var6);
         if (var6) {
            var2.writeObject(SecurityServiceManager.sendASToWire(this.initiator));
         }

         boolean var7 = this.deploymentType != null;
         var2.writeBoolean(var7);
         if (var7) {
            var2.writeObject(this.deploymentType);
         }

         var2.writeBoolean(this.needsVersionUpdate);
      }

   }

   public final void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.deploymentServiceVersion = var1.readByte();
      this.readMessage(this.deploymentServiceVersion, var1);
   }

   private void readMessage(byte var1, ObjectInput var2) throws IOException, ClassNotFoundException {
      if (var1 == this.currentDeploymentServiceVersion) {
         this.messageType = var2.readByte();
         this.deploymentId = var2.readLong();
         this.timeoutValue = var2.readLong();
         this.callConfigurationProviderLast = var2.readBoolean();
         this.messageSrc = ((WLObjectInput)var2).readString();
         boolean var3 = var2.readBoolean();
         if (var3) {
            this.items = ((WLObjectInput)var2).readArrayList();
         }

         boolean var4 = var2.readBoolean();
         if (var4) {
            this.fromVersion = (DomainVersion)var2.readObject();
         }

         boolean var5 = var2.readBoolean();
         if (var5) {
            this.toVersion = (DomainVersion)var2.readObject();
         }

         boolean var6 = var2.readBoolean();
         if (var6) {
            this.initiator = SecurityServiceManager.getASFromWire((AuthenticatedSubject)var2.readObject());
         }

         boolean var7 = var2.readBoolean();
         if (var7) {
            this.setDeploymentType((String)var2.readObject());
         }

         this.needsVersionUpdate = var2.readBoolean();
      }

   }

   public final String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("DeploymentService message ");
      var1.append(getMessageTypeString(this.messageType));
      var1.append(" and deployment id: ");
      var1.append(this.deploymentId);
      var1.append(" from : ");
      var1.append(this.messageSrc);
      var1.append(" with : ");
      if (this.items != null && !this.items.isEmpty()) {
         Iterator var2 = this.items.iterator();
         var1.append(this.items.size());
         var1.append(" item(s) : ");

         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 != null) {
               var1.append("[");
               var1.append(var3.toString());
               var1.append("] ");
            }
         }
      } else {
         var1.append(0);
         var1.append(" items ");
      }

      if (this.fromVersion != null) {
         var1.append(" fromVersion: ");
         var1.append(this.fromVersion.toString());
      }

      if (this.toVersion != null) {
         var1.append(" toVersion: ");
         var1.append(this.toVersion.toString());
      }

      return var1.toString();
   }

   public final String getDeploymentType() {
      return this.deploymentType;
   }

   public final void setDeploymentType(String var1) {
      if (this.messageType != 13) {
         throw new IllegalArgumentException("setDeploymentType is invalid for this message type");
      } else {
         this.deploymentType = var1;
      }
   }
}
