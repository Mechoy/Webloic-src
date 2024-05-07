package weblogic.deploy.service.internal.adminserver;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import weblogic.deploy.service.ChangeDescriptor;

class ChangeDescriptorImpl implements ChangeDescriptor {
   private static final long serialVersionUID = 4628863147014401090L;
   private String changeOperation;
   private String changeTarget;
   private String changeSource;
   private Serializable data;
   private Serializable version;
   private String identity;

   ChangeDescriptorImpl(String var1, String var2, String var3, Serializable var4, String var5) {
      this.changeOperation = var1;
      this.changeTarget = var2.replace('\\', '/');
      this.changeSource = var3.replace('\\', '/');
      this.version = var4;
      this.identity = var5;
   }

   ChangeDescriptorImpl(Serializable var1, Serializable var2) {
      this.data = var1;
      this.version = var2;
   }

   public ChangeDescriptorImpl() {
   }

   public String getChangeOperation() {
      return this.changeOperation;
   }

   public String getChangeTarget() {
      return this.changeTarget;
   }

   public String getChangeSource() {
      return this.changeSource;
   }

   public Serializable getVersion() {
      return this.version;
   }

   public Serializable getData() {
      return this.data;
   }

   public String getIdentity() {
      return this.identity;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.data != null) {
         var1.append("Serializable data and version");
      } else {
         var1.append("Change operation: ");
         var1.append(this.changeOperation);
         var1.append(", target: ");
         var1.append(this.changeTarget);
         var1.append(", source: ");
         var1.append(this.changeSource);
         var1.append(", version: ");
         var1.append(this.version);
         var1.append(", id: ");
         var1.append(this.identity);
      }

      return var1.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.getChangeOperation());
      var1.writeObject(this.getChangeTarget());
      var1.writeObject(this.getChangeSource());
      var1.writeObject(this.getData());
      var1.writeObject(this.getVersion());
      var1.writeObject(this.getIdentity());
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.changeOperation = (String)var1.readObject();
      this.changeTarget = (String)var1.readObject();
      this.changeSource = (String)var1.readObject();
      this.data = (Serializable)var1.readObject();
      this.version = (Serializable)var1.readObject();
      this.identity = (String)var1.readObject();
   }
}
