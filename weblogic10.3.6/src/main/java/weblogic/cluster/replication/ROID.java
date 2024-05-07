package weblogic.cluster.replication;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import weblogic.rjvm.LocalRJVM;
import weblogic.utils.io.DataIO;

public final class ROID implements Externalizable {
   private static final long serialVersionUID = -7737873844013713694L;
   private static final long differentiator;
   private static long numObjects = 0L;
   private static final byte[] pk = LocalRJVM.getLocalRJVM().getPublicKey();
   private static final Object NUM_OBJ_INC_LOCK = new Object();
   private long value;
   private int hashValue;

   static ROID create() {
      int var0;
      synchronized(NUM_OBJ_INC_LOCK) {
         var0 = numObjects++;
      }

      return new ROID(differentiator + var0);
   }

   private ROID(long var1) {
      this.value = var1;
      this.computeHash();
   }

   public ROID() {
   }

   private void computeHash() {
      this.hashValue = (int)(this.value ^ this.value >> 32);
   }

   public int hashCode() {
      return this.hashValue;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof ROID) {
         ROID var2 = (ROID)var1;
         return this.value == var2.value;
      } else {
         return false;
      }
   }

   public String toString() {
      return "" + this.value;
   }

   public int getValueAsInt() {
      return (int)this.value;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      DataIO.writeLong((OutputStream)var1, this.value);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.value = DataIO.readLong((InputStream)var1);
      this.computeHash();
   }

   static {
      long var0 = 0L;

      for(int var2 = 0; var2 < pk.length; ++var2) {
         var0 = var0 << 8 ^ var0 ^ (long)pk[var2];
      }

      differentiator = var0;
   }
}
