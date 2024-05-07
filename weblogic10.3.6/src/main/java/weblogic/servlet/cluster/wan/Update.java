package weblogic.servlet.cluster.wan;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;

public class Update implements Externalizable {
   static final long serialVersionUID = 6694065036191470528L;
   private String sessionID;
   private String contextPath;
   private long creationTime;
   private int maxInactiveTime;
   private long lastAccessTime;
   private SessionDiff change;
   private boolean serialized = false;
   private int hashCode;

   public Update(String var1, String var2, long var3, int var5, long var6, SessionDiff var8) {
      this.sessionID = var1;
      this.contextPath = var2;
      this.hashCode = var1.hashCode() ^ var2.hashCode();
      this.creationTime = var3;
      this.maxInactiveTime = var5;
      this.lastAccessTime = var6;
      this.change = var8;
   }

   public Update() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      if (var1 instanceof WLObjectOutput) {
         WLObjectOutput var2 = (WLObjectOutput)var1;
         var2.writeImmutable(this.sessionID);
         var2.writeImmutable(this.contextPath);
      } else {
         var1.writeUTF(this.sessionID);
         var1.writeUTF(this.contextPath);
      }

      var1.writeLong(this.creationTime);
      var1.writeInt(this.maxInactiveTime);
      var1.writeLong(this.lastAccessTime);
      var1.writeObject(this.change.cloneAndClear());
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      if (var1 instanceof WLObjectInput) {
         WLObjectInput var2 = (WLObjectInput)var1;
         this.sessionID = (String)var2.readImmutable();
         this.contextPath = (String)var2.readImmutable();
      } else {
         this.sessionID = var1.readUTF();
         this.contextPath = var1.readUTF();
      }

      this.creationTime = var1.readLong();
      this.maxInactiveTime = var1.readInt();
      this.lastAccessTime = var1.readLong();
      this.change = (SessionDiff)var1.readObject();
      this.hashCode = this.sessionID.hashCode() ^ this.contextPath.hashCode();
      this.serialized = true;
   }

   public int hashCode() {
      return this.hashCode;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         try {
            Update var2 = (Update)var1;
            return this.hashCode == var2.hashCode && this.contextPath.equals(var2.contextPath) && this.sessionID.equals(var2.sessionID);
         } catch (ClassCastException var3) {
            return false;
         }
      }
   }

   public String getSessionID() {
      return this.sessionID;
   }

   public String getContextPath() {
      return this.contextPath;
   }

   public long getCreationTime() {
      return this.creationTime;
   }

   public int getMaxInactiveTime() {
      return this.maxInactiveTime;
   }

   public long getLastAccessTime() {
      return this.lastAccessTime;
   }

   public SessionDiff getChange() {
      return !this.serialized ? this.change.cloneAndClear() : this.change;
   }
}
