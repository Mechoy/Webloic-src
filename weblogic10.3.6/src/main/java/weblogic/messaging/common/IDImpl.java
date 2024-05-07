package weblogic.messaging.common;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.messaging.ID;

public class IDImpl implements ID {
   static final long serialVersionUID = 1531949956170006912L;
   private static final byte WL6_VERSION = 1;
   private static final byte EXTVERSION = 2;
   private static final int SEED_SHIFT = 45;
   private static final long SEED_MASK = 262143L;
   private static final long TIMESTAMP_MASK = 35184372088831L;
   protected long unique;
   protected int counter;

   public IDImpl(IDFactory var1) {
      var1.initId(this);
   }

   public IDImpl(long var1, int var3, int var4) {
      this.init(var1, var3, var4);
   }

   void init(long var1, int var3, int var4) {
      this.unique = ((long)var3 & 262143L) << 45 | var1;
      this.counter = var4;
   }

   public long getTimestamp() {
      return this.unique & 35184372088831L;
   }

   public int getSeed() {
      return (int)(this.unique >> 45);
   }

   public int getCounter() {
      return this.counter;
   }

   public IDImpl() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(2);
      var1.writeLong(this.unique);
      var1.writeInt(this.counter);
   }

   public void readExternal(ObjectInput var1) throws ClassNotFoundException, IOException {
      byte var2 = var1.readByte();
      if (var2 == 1) {
         this.unique = var1.readLong();
         int var3 = var1.readInt();
         this.unique |= ((long)var3 & 262143L) << 45;
         var1.readInt();
         var1.readInt();
         this.counter = var1.readInt();
      } else {
         if (var2 != 2) {
            throw MessagingUtilities.versionIOException(var2, 1, 2);
         }

         this.unique = var1.readLong();
         this.counter = var1.readInt();
      }

   }

   public String toString() {
      return "<" + this.unique + "." + this.counter + ">";
   }

   public boolean equals(Object var1) {
      IDImpl var2 = (IDImpl)var1;
      return this.counter == var2.counter && this.unique == var2.unique;
   }

   public int compareTo(Object var1) {
      IDImpl var2 = (IDImpl)var1;
      if (this.unique < var2.unique) {
         return -1;
      } else if (this.unique > var2.unique) {
         return 1;
      } else if (this.counter < var2.counter) {
         return -1;
      } else {
         return this.counter > var2.counter ? 1 : 0;
      }
   }

   public int hashCode() {
      return (int)(this.unique ^ (long)this.counter);
   }
}
