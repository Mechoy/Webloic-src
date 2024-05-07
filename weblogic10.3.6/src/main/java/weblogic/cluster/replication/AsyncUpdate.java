package weblogic.cluster.replication;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import weblogic.rmi.spi.HostID;

public class AsyncUpdate implements Externalizable {
   static final long serialVersionUID = -9097910107368943550L;
   private boolean update;
   private ROID id;
   private int version;
   private Serializable change = null;
   private Object key;
   private HostID primaryHost = null;
   private Replicatable ro = null;
   private transient AsyncReplicatable aro = null;

   public AsyncUpdate(ROID var1, int var2, AsyncReplicatable var3, Object var4) {
      this.id = var1;
      this.version = var2;
      this.aro = var3;
      this.change = var3.getBatchedChanges();
      this.key = var4;
      this.update = true;
   }

   public AsyncUpdate(HostID var1, ROID var2, int var3, Object var4, AsyncReplicatable var5) {
      this.primaryHost = var1;
      this.id = var2;
      this.version = var3;
      this.key = var4;
      this.aro = var5;
      this.ro = (Replicatable)var5;
      this.update = false;
   }

   public void recreate(HostID var1) {
      if (this.update) {
         this.ro = (Replicatable)this.aro;
         this.update = false;
         this.change = null;
         this.primaryHost = var1;
      }

   }

   public void commit() {
      this.aro.commit();
   }

   public AsyncUpdate() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeBoolean(this.update);
      if (this.update) {
         this.writeForUpdate(var1);
      } else {
         this.writeForCreate(var1);
      }

   }

   public void writeForUpdate(ObjectOutput var1) throws IOException {
      var1.writeObject(this.id);
      var1.writeInt(this.version);
      synchronized(this.change) {
         var1.writeObject(this.change);
         this.aro.commit();
      }

      var1.writeObject(this.key);
   }

   public void writeForCreate(ObjectOutput var1) throws IOException {
      var1.writeObject(this.id);
      var1.writeInt(this.version);
      var1.writeObject(this.key);
      var1.writeObject(this.primaryHost);
      synchronized(this.aro.getBatchedChanges()) {
         var1.writeObject(this.ro);
         this.aro.commit();
      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.update = var1.readBoolean();
      if (this.update) {
         this.readForUpdate(var1);
      } else {
         this.readForCreate(var1);
      }

   }

   public void readForUpdate(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.id = (ROID)var1.readObject();
      this.version = var1.readInt();
      this.change = (Serializable)var1.readObject();
      this.key = var1.readObject();
   }

   public void readForCreate(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.id = (ROID)var1.readObject();
      this.version = var1.readInt();
      this.key = var1.readObject();
      this.primaryHost = (HostID)var1.readObject();
      this.ro = (Replicatable)var1.readObject();
   }

   public int hashCode() {
      return this.id.hashCode() ^ this.version;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         try {
            AsyncUpdate var2 = (AsyncUpdate)var1;
            return this.hashCode() == var2.hashCode() && this.version == var2.getVersion() && this.getId().equals(var2.getId());
         } catch (ClassCastException var3) {
            return false;
         }
      }
   }

   public Serializable getChange() {
      return this.change;
   }

   public ROID getId() {
      return this.id;
   }

   public Object getKey() {
      return this.key;
   }

   public int getVersion() {
      return this.version;
   }

   public HostID getPrimaryHost() {
      return this.primaryHost;
   }

   public boolean isUpdate() {
      return this.update;
   }

   public Replicatable getRO() {
      return this.ro;
   }
}
