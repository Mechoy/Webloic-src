package weblogic.wsee.persistence;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import weblogic.wsee.Version;

public abstract class AbstractStorable implements Storable {
   private static final long serialVersionUID = 1L;
   private Serializable _id;
   private long _creationTimestamp;
   private long _lastUpdateTimestamp;
   protected transient String _serialFormVersion = "10.3.6";
   private transient String _physicalStoreName;

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject(this._serialFormVersion);
      var1.writeObject(this._id);
      var1.writeLong(this._creationTimestamp);
      var1.writeLong(this._lastUpdateTimestamp);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this._serialFormVersion = (String)var1.readObject();
      this._id = (Serializable)var1.readObject();
      if (Version.isLaterThanOrEqualTo(this._serialFormVersion, "10.3.6")) {
         this._creationTimestamp = var1.readLong();
         this._lastUpdateTimestamp = var1.readLong();
      }

   }

   public AbstractStorable(Serializable var1) {
      this._id = var1;
      this._creationTimestamp = System.currentTimeMillis();
   }

   public Serializable getObjectId() {
      return this._id;
   }

   public boolean hasExplicitExpiration() {
      return false;
   }

   public boolean isExpired() {
      return false;
   }

   public String getPhysicalStoreName() {
      return this._physicalStoreName;
   }

   public void setPhysicalStoreName(String var1) {
      this._physicalStoreName = var1;
   }

   public Long getCreationTime() {
      return this._creationTimestamp;
   }

   public Long getLastUpdatedTime() {
      return this._lastUpdateTimestamp;
   }

   protected void setCreationTime(long var1) {
      this._creationTimestamp = var1;
   }

   public void touch() {
      this._lastUpdateTimestamp = System.currentTimeMillis();
   }

   protected void setLastUpdatedTime(long var1) {
      this._lastUpdateTimestamp = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this._id);
      return var1.toString();
   }
}
