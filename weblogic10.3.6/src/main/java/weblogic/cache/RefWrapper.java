package weblogic.cache;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.ref.SoftReference;

public class RefWrapper implements Externalizable {
   private static final boolean nosoftrefs = Boolean.getBoolean("weblogicx.cache.nosoftreferences");
   private SoftReference softref;
   private Object hardref;

   public Object get() {
      if (nosoftrefs) {
         return this.hardref;
      } else {
         return this.softref == null ? null : this.softref.get();
      }
   }

   public RefWrapper() {
   }

   public RefWrapper(Object var1) {
      if (nosoftrefs) {
         this.hardref = var1;
      } else {
         this.softref = new SoftReference(var1);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.get());
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      Object var2 = var1.readObject();
      if (var2 != null) {
         if (nosoftrefs) {
            this.hardref = var2;
         } else {
            this.softref = new SoftReference(var2);
         }

      }
   }
}
