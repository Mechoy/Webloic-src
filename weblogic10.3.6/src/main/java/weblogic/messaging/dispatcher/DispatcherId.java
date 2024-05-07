package weblogic.messaging.dispatcher;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.InetAddress;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.messaging.common.MessagingUtilities;

public class DispatcherId implements Externalizable, Comparable {
   static final long serialVersionUID = 2503587581403689795L;
   private String name;
   private String id;
   private int hashCode;
   private transient int counter;
   private static final byte EXTVERSION0 = 0;
   private static final byte EXTVERSION1 = 1;
   private static final byte EXTVERSION2 = 2;
   private static final byte EXTVERSION = 2;
   private static final int VERSION_MASK = 15;
   private static final int ID_FLAG = 16;

   public DispatcherId(String var1, String var2) {
      this.name = var1;
      this.id = var2;
      String var3 = var1 + var2;
      this.hashCode = var3.hashCode();
   }

   public DispatcherId(DispatcherId var1, int var2) {
      this.name = var1.name;
      this.id = var1.id;
      this.hashCode = var1.hashCode();
      this.counter = var2;
   }

   public final int compareTo(Object var1) {
      DispatcherId var2 = (DispatcherId)var1;
      int var3 = var2.hashCode;
      if (this.hashCode < var3) {
         return -1;
      } else if (this.hashCode > var3) {
         return 1;
      } else if (this.name.length() < var2.name.length()) {
         return -1;
      } else if (this.name.length() > var2.name.length()) {
         return 1;
      } else {
         int var4 = this.name.compareTo(var2.name);
         if (var4 != 0) {
            return var4;
         } else {
            if (this.id != null && var2.id != null) {
               if (this.id.length() < var2.id.length()) {
                  return -1;
               }

               if (this.id.length() > var2.id.length()) {
                  return 1;
               }

               var4 = this.id.compareTo(var2.id);
               if (var4 != 0) {
                  return var4;
               }
            }

            if (this.counter == var2.counter) {
               return 0;
            } else {
               return this.counter < var2.counter ? -1 : 1;
            }
         }
      }
   }

   public final String getName() {
      return this.name;
   }

   public final String getId() {
      return this.id;
   }

   public final int hashCode() {
      return this.hashCode;
   }

   public final boolean equals(Object var1) {
      return !(var1 instanceof DispatcherId) ? false : this.internalEquals((DispatcherId)var1, true);
   }

   public final boolean isSameServer(Object var1) {
      return !(var1 instanceof DispatcherId) ? false : this.internalEquals((DispatcherId)var1, false);
   }

   private boolean internalEquals(DispatcherId var1, boolean var2) {
      if (this == var1) {
         return true;
      } else if (this.hashCode != var1.hashCode) {
         return false;
      } else if (var2 && this.counter != var1.counter) {
         return false;
      } else {
         return this.name.equals(var1.name) && (this.id == null || var1.id == null || this.id != null && var1.id != null && this.id.equals(var1.id));
      }
   }

   public final String toString() {
      return this.name;
   }

   public DispatcherId() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = this.getVersion(var1);
      if (var2 == 2) {
         if (this.id != null) {
            var1.writeByte(18);
         } else {
            var1.writeByte(2);
         }

         var1.writeUTF(this.name);
         var1.writeInt(this.hashCode);
         if (this.id != null) {
            var1.writeUTF(this.id);
         }
      } else {
         var1.writeByte(1);
         var1.writeUTF(this.name);
         var1.writeInt(this.hashCode);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      byte var3 = (byte)(var2 & 15);
      if (var3 != 1 && var3 != 2) {
         throw MessagingUtilities.versionIOException(var3, 1, 2);
      } else {
         this.name = var1.readUTF();
         this.hashCode = var1.readInt();
         if ((var2 & 16) != 0) {
            this.id = var1.readUTF();
         }

      }
   }

   protected int getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         int var3 = var2.getMajor();
         return var3 <= 8 ? 1 : 2;
      } else {
         return 1;
      }
   }

   public final String getHostAddress() {
      try {
         int var1 = this.name.indexOf(":");
         if (var1 != -1) {
            String var2 = this.name.substring(var1 + 1, this.name.length());
            var2 = var2.substring(0, var2.indexOf(":"));
            return InetAddress.getByName(var2).getHostAddress();
         }
      } catch (Exception var3) {
      }

      return "0.0.0.0";
   }

   public String getDetail() {
      return "id = " + this.id + ", name = " + this.name + ", counter=" + this.counter + ", hashcode = " + this.hashCode;
   }
}
