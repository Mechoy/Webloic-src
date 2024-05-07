package weblogic.jms.server;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSUtilities;

public class SequenceData implements Externalizable, Cloneable {
   static final long serialVersionUID = 7571220896297616034L;
   private static final int EXTVERSION1 = 65536;
   private static final int VERSION_MASK = 983040;
   private static final int PROPERTY_UOO = 1;
   private static final int SAF_STICKY_ROUTING = 4096;
   String uooName;
   boolean safStickyRouting;

   public void setSAFStickyRouting(boolean var1) {
      this.safStickyRouting = var1;
   }

   public void setUnitOfOrder(String var1) {
      this.uooName = var1;
   }

   public boolean getSAFStickyRouting() {
      return this.safStickyRouting;
   }

   public String getUnitOfOrder() {
      return this.uooName;
   }

   public SequenceData copy() {
      try {
         return (SequenceData)this.clone();
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError(var2);
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof SequenceData)) {
         return false;
      } else {
         SequenceData var2 = (SequenceData)var1;
         if (this.safStickyRouting != var2.safStickyRouting) {
            return false;
         } else if (this.uooName == null) {
            return var2.uooName == null;
         } else {
            return this.uooName.equals(var2.uooName);
         }
      }
   }

   public int hashCode() {
      int var1 = 0;
      if (this.safStickyRouting) {
         var1 = 4096;
      }

      if (this.uooName != null) {
         var1 |= this.uooName.hashCode();
      }

      return var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      String var2 = this.uooName;
      int var3;
      if (var2 == null) {
         var3 = 65536;
      } else {
         var3 = 65537;
      }

      if (this.safStickyRouting) {
         var3 = 4096;
      }

      var1.writeInt(var3);
      if (var2 != null) {
         var1.writeUTF(var2);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 983040;
      if (var3 != 65536) {
         throw JMSUtilities.versionIOException(var3, 65536, 65536);
      } else {
         this.safStickyRouting = (var2 & 4096) != 0;
         if ((var2 & 1) != 0) {
            this.uooName = var1.readUTF();
         }

      }
   }
}
