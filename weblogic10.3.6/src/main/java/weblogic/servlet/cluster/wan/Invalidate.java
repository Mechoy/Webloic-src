package weblogic.servlet.cluster.wan;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Invalidate implements Externalizable {
   private String sessionID;
   private String contextPath;
   private int hashCode;

   public Invalidate() {
   }

   public Invalidate(String var1, String var2) {
      this.sessionID = var1;
      this.contextPath = var2;
      this.hashCode = var1.hashCode() ^ var2.hashCode();
   }

   final String getSessionID() {
      return this.sessionID;
   }

   final String getContextPath() {
      return this.contextPath;
   }

   public int hashCode() {
      return this.hashCode;
   }

   public boolean equals(Object var1) {
      try {
         Invalidate var2 = (Invalidate)var1;
         return this.sessionID.equals(var2.sessionID) && this.contextPath.equals(var2.contextPath);
      } catch (ClassCastException var3) {
         return false;
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF(this.sessionID);
      var1.writeUTF(this.contextPath);
      var1.writeInt(this.hashCode);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this.sessionID = var1.readUTF();
      this.contextPath = var1.readUTF();
      this.hashCode = var1.readInt();
   }
}
