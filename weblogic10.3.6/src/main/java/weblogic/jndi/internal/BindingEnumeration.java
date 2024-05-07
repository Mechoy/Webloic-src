package weblogic.jndi.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.naming.Binding;
import javax.naming.NameClassPair;

public final class BindingEnumeration extends NameClassPairEnumeration implements Externalizable {
   static final long serialVersionUID = -1874230391047383407L;

   public BindingEnumeration(Binding[] var1) {
      super(var1);
   }

   public BindingEnumeration() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(this.list.length);

      for(int var2 = 0; var2 < this.list.length; ++var2) {
         Binding var3 = (Binding)this.list[var2];
         var1.writeObject(var3.getName());
         var1.writeObject(var3.getClassName());

         try {
            var1.writeObject(var3.getObject());
         } catch (IOException var5) {
            var1.writeObject("non-serializable");
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.list = new Binding[var1.readInt()];
      int var2 = 0;

      for(int var3 = 0; var3 < this.list.length; ++var3) {
         String var4 = (String)var1.readObject();
         String var5 = (String)var1.readObject();

         try {
            Object var6 = var1.readObject();
            this.list[var2++] = new Binding(var4, var5, var6);
         } catch (ClassNotFoundException var7) {
            System.out.println("Skipping over incompatible object at name=" + var4 + ", className=" + var5);
         }
      }

      if (var2 < this.list.length) {
         NameClassPair[] var8 = this.list;
         this.list = new Binding[var2];
         System.arraycopy(var8, 0, this.list, 0, var2);
      }

   }
}
