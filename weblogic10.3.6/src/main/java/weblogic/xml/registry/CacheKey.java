package weblogic.xml.registry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

class CacheKey implements Serializable {
   static final long serialVersionUID = 1L;
   String registry = null;
   String publicKey = null;
   String systemKey = null;
   transient int hash = 0;

   public CacheKey(String var1, String var2, String var3) {
      this.registry = var1.intern();
      if (var2 != null) {
         this.publicKey = var2.intern();
         this.hash = var2.hashCode();
      } else if (var3 != null) {
         this.systemKey = var3.intern();
         this.hash = var3.hashCode();
      }

   }

   public String toString() {
      if (this.publicKey != null) {
         return this.registry + ":PID=" + this.publicKey;
      } else {
         return this.systemKey != null ? this.registry + ":SID=" + this.systemKey : "UNKNOWN";
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof CacheKey)) {
         return false;
      } else {
         CacheKey var2 = (CacheKey)var1;
         if (this.registry != var2.registry) {
            return false;
         } else if (this.publicKey != null && this.publicKey == var2.publicKey) {
            return true;
         } else {
            return this.systemKey != null && this.systemKey == var2.systemKey;
         }
      }
   }

   public int hashCode() {
      return this.hash;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.registry = this.registry.intern();
      if (this.publicKey != null) {
         this.publicKey = this.publicKey.intern();
         this.hash = this.publicKey.hashCode();
      } else if (this.systemKey != null) {
         this.systemKey = this.systemKey.intern();
         this.hash = this.systemKey.hashCode();
      }

   }
}
