package weblogic.messaging.path.helper;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import weblogic.messaging.path.Key;
import weblogic.messaging.saf.utils.SAFClientUtil;

public class KeyString implements Key, Externalizable {
   static final long serialVersionUID = -6246813907189434496L;
   private String id;
   private String assembly;
   private byte subsystem;
   private static int EXTVERSION = 1;
   private static int VERSION_MASK = 255;
   private static int FLAG_CLUSTER = 256;

   public KeyString(byte var1, String var2, String var3) {
      assert var1 > -1 && var1 < Key.RESERVED_SUBSYSTEMS.length;

      this.subsystem = var1;
      this.assembly = var2.intern();
      this.id = var3.intern();
   }

   public KeyString() {
   }

   public byte getSubsystem() {
      return this.subsystem;
   }

   public String getAssemblyId() {
      return this.assembly;
   }

   public Serializable getKeyId() {
      return this.id;
   }

   public String getStringId() {
      return this.id;
   }

   public int hashCode() {
      return this.assembly.hashCode() ^ this.id.hashCode() ^ this.subsystem;
   }

   public String toString() {
      return this.id + "^" + this.subsystem + "^" + this.assembly;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Key)) {
         return false;
      } else {
         Key var2 = (Key)var1;
         return var2 == this || this.subsystem == var2.getSubsystem() && (this.assembly == var2.getAssemblyId() || this.assembly.equals(var2.getAssemblyId())) && (this.id == var2.getKeyId() || this.id.equals(var2.getKeyId()));
      }
   }

   public int compareTo(Object var1) {
      if (var1 == this) {
         return 0;
      } else {
         Key var2 = (Key)var1;
         int var3 = this.id.compareTo(var2.getKeyId());
         if (var3 != 0) {
            return var3;
         } else {
            var3 = this.assembly.compareTo(var2.getAssemblyId());
            return var3 != 0 ? var3 : this.subsystem - var2.getSubsystem();
         }
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = EXTVERSION;
      if (this.assembly == "CLUSTER") {
         var2 |= FLAG_CLUSTER;
      }

      var1.writeInt(var2);
      var1.writeUTF(this.id);
      if (this.assembly != "CLUSTER") {
         var1.writeUTF(this.assembly);
      }

      var1.writeByte(this.subsystem);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & VERSION_MASK;
      if (var3 != EXTVERSION) {
         throw SAFClientUtil.versionIOException(var3, EXTVERSION, EXTVERSION);
      } else {
         this.id = var1.readUTF();
         if ((var2 & FLAG_CLUSTER) == 0) {
            this.assembly = var1.readUTF().intern();
         } else {
            this.assembly = "CLUSTER";
         }

         this.subsystem = var1.readByte();
      }
   }
}
