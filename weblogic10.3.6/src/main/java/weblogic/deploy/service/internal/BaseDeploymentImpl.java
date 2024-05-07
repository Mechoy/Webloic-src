package weblogic.deploy.service.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import weblogic.deploy.service.ChangeDescriptor;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.Version;

public abstract class BaseDeploymentImpl implements Deployment {
   private String identity;
   private String callbackHandlerId;
   private Version proposedVersion;
   private ArrayList targets;
   private ArrayList serversToBeStarted;
   private String dataTransferHandlerType;
   private ArrayList changeDescriptors;

   protected BaseDeploymentImpl() {
   }

   protected BaseDeploymentImpl(String var1, String var2, Version var3, List var4, List var5, String var6, List var7) {
      this.setIdentity(var1);
      this.setCallbackHandlerId(var2);
      this.setProposedVersion(var3);
      this.setTargets(var4);
      this.setServersToBeRestarted(var5);
      this.setDataTransferHandlerType(var6);
      this.setChangeDescriptors(var7);
   }

   public final String getIdentity() {
      return this.identity;
   }

   public final void setIdentity(String var1) {
      this.identity = var1;
   }

   public final String getCallbackHandlerId() {
      return this.callbackHandlerId;
   }

   public final void setCallbackHandlerId(String var1) {
      this.callbackHandlerId = var1;
   }

   public final Version getProposedVersion() {
      return this.proposedVersion;
   }

   public final void setProposedVersion(Version var1) {
      this.proposedVersion = var1;
   }

   public final String[] getTargets() {
      if (this.targets == null) {
         this.targets = new ArrayList();
      }

      String[] var1 = new String[this.targets.size()];
      var1 = (String[])((String[])this.targets.toArray(var1));
      return var1;
   }

   public final void setTargets(List var1) {
      this.targets = new ArrayList();
      if (var1 != null) {
         this.targets.addAll(var1);
      }

   }

   public final void addTarget(String var1) {
      if (this.targets == null) {
         this.targets = new ArrayList();
      }

      this.targets.add(var1);
   }

   public final void removeTarget(String var1) {
      if (this.targets != null) {
         this.targets.remove(var1);
      }

   }

   public final String[] getServersToBeRestarted() {
      if (this.serversToBeStarted == null) {
         this.serversToBeStarted = new ArrayList();
      }

      String[] var1 = new String[this.serversToBeStarted.size()];
      var1 = (String[])((String[])this.serversToBeStarted.toArray(var1));
      return var1;
   }

   public final void setServersToBeRestarted(List var1) {
      this.serversToBeStarted = new ArrayList();
      if (var1 != null) {
         this.serversToBeStarted.addAll(var1);
      }

   }

   public final void addServerToBeRestarted(String var1) {
      if (this.serversToBeStarted == null) {
         this.serversToBeStarted = new ArrayList();
      }

      this.serversToBeStarted.add(var1);
   }

   public final void removeServerToBeRestarted(String var1) {
      if (this.serversToBeStarted != null) {
         this.serversToBeStarted.remove(var1);
      }

   }

   public final String getDataTransferHandlerType() {
      return this.dataTransferHandlerType;
   }

   public final void setDataTransferHandlerType(String var1) {
      this.dataTransferHandlerType = var1;
   }

   public final List getChangeDescriptors() {
      if (this.changeDescriptors == null) {
         this.changeDescriptors = new ArrayList();
      }

      return this.changeDescriptors;
   }

   public final void setChangeDescriptors(List var1) {
      this.changeDescriptors = new ArrayList();
      if (var1 != null) {
         this.changeDescriptors.addAll(var1);
      }

   }

   public final void addChangeDescriptor(ChangeDescriptor var1) {
      if (this.changeDescriptors == null) {
         this.changeDescriptors = new ArrayList();
      }

      this.changeDescriptors.add(var1);
   }

   public final void removeChangeDescriptor(ChangeDescriptor var1) {
      if (this.changeDescriptors != null) {
         this.changeDescriptors.remove(var1);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.identity);
      var1.writeObject(this.callbackHandlerId);
      var1.writeObject(this.proposedVersion);
      ArrayList var2 = this.targets != null && !this.targets.isEmpty() ? this.targets : null;
      var1.writeObject(var2);
      ArrayList var3 = this.serversToBeStarted != null && !this.serversToBeStarted.isEmpty() ? this.serversToBeStarted : null;
      var1.writeObject(var3);
      var1.writeObject(this.dataTransferHandlerType);
      ArrayList var4 = this.changeDescriptors != null && !this.changeDescriptors.isEmpty() ? this.changeDescriptors : null;
      var1.writeObject(var4);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.setIdentity((String)var1.readObject());
      this.setCallbackHandlerId((String)var1.readObject());
      this.setProposedVersion((Version)var1.readObject());
      this.setTargets((List)var1.readObject());
      this.setServersToBeRestarted((List)var1.readObject());
      this.setDataTransferHandlerType((String)var1.readObject());
      this.setChangeDescriptors((List)var1.readObject());
   }

   public final String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString()).append("[");
      var1.append(this.toPrettyString()).append("]");
      return var1.toString();
   }

   protected String toPrettyString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("id: ");
      var1.append(this.getIdentity());
      var1.append(", callback id: ");
      var1.append(this.getCallbackHandlerId());
      String[] var2 = this.getTargets();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.append(", target[");
            var1.append(var3);
            var1.append("] = ");
            var1.append(var2[var3]);
         }
      }

      if (this.getProposedVersion() != null) {
         var1.append(", proposedVersion: ");
         var1.append(this.getProposedVersion().toString());
      }

      return var1.toString();
   }
}
