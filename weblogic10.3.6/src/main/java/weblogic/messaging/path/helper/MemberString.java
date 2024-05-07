package weblogic.messaging.path.helper;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import weblogic.messaging.path.Member;
import weblogic.messaging.saf.utils.SAFClientUtil;

public class MemberString implements Member, Externalizable {
   static final long serialVersionUID = -5685689480293580262L;
   protected String id;
   protected String serverName;
   protected long timestamp;
   protected int generation;
   private static int lastGeneration;
   private static int EXTVERSION = 1;
   private static int VERSION_MASK = 255;
   private static int FLAG_ID_SERVER = 256;

   public MemberString(String var1, String var2) {
      assert var1 != null && var2 != null;

      synchronized(this.getClass()) {
         this.timestamp = System.currentTimeMillis();
         this.generation = lastGeneration++;
      }

      this.id = var1.intern();
      if (var2 != this.id && !var2.equals(this.id)) {
         this.serverName = var2.intern();
      } else {
         this.serverName = this.id;
      }

   }

   public MemberString() {
   }

   public Serializable getMemberId() {
      return this.id;
   }

   public String getStringId() {
      return this.id;
   }

   public String getWLServerName() {
      return this.serverName;
   }

   public long getTimeStamp() {
      return this.timestamp;
   }

   public int getGeneration() {
      return this.generation;
   }

   public int hashCode() {
      return this.id.hashCode() ^ this.serverName.hashCode();
   }

   public String toString() {
      return this.id + "^" + this.serverName + "^" + this.generation + "^" + this.timestamp;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Member)) {
         return false;
      } else {
         Member var2 = (Member)var1;
         return var2 == this || this.timestamp == var2.getTimeStamp() && this.generation == var2.getGeneration() && this.id.equals(var2.getMemberId()) && this.serverName.equals(var2.getWLServerName());
      }
   }

   public int compareTo(Object var1) {
      Member var2 = (Member)var1;
      int var3 = this.id.compareTo((String)var2.getMemberId());
      if (var3 != 0) {
         return var3;
      } else {
         var3 = this.serverName.compareTo(var2.getWLServerName());
         if (var3 != 0) {
            return var3;
         } else {
            long var4 = this.timestamp - var2.getTimeStamp();
            if (var4 < 0L) {
               return -1;
            } else {
               return var4 > 0L ? 1 : this.generation - var2.getGeneration();
            }
         }
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = EXTVERSION;
      if (this.id == this.serverName || this.id.equals(this.serverName)) {
         var2 |= FLAG_ID_SERVER;
      }

      var1.writeInt(var2);
      var1.writeInt(this.generation);
      var1.writeLong(this.timestamp);
      var1.writeUTF(this.id);
      if ((var2 & FLAG_ID_SERVER) == 0) {
         var1.writeUTF(this.serverName);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & VERSION_MASK;
      if (var3 != EXTVERSION) {
         throw SAFClientUtil.versionIOException(var3, EXTVERSION, EXTVERSION);
      } else {
         this.generation = var1.readInt();
         this.timestamp = var1.readLong();
         this.id = var1.readUTF().intern();
         if ((var2 & FLAG_ID_SERVER) == 0) {
            this.serverName = var1.readUTF().intern();
         } else {
            this.serverName = this.id;
         }

      }
   }
}
