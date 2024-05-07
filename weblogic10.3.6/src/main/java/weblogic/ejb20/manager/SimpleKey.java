package weblogic.ejb20.manager;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.utils.io.Immutable;

public class SimpleKey implements Externalizable, Immutable {
   private static final long serialVersionUID = 2652002217566939126L;
   private long val;
   private transient int hashValue;

   public SimpleKey(long var1) {
      this.val = var1;
      this.hashValue = (int)(var1 ^ var1 >> 32);
   }

   public SimpleKey() {
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof SimpleKey) {
         SimpleKey var2 = (SimpleKey)var1;
         return this.val == var2.val;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hashValue;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeLong(this.val);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this.val = var1.readLong();
      this.hashValue = (int)(this.val ^ this.val >> 32);
   }

   public String toString() {
      return Long.toString(this.val);
   }
}
