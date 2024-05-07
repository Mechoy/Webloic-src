package weblogic.wsee.jws.conversation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class StoreConfig extends HashMap {
   private static final long serialVersionUID = -3793629763830952861L;
   private String type = null;

   public StoreConfig(String var1) {
      this.type = var1;
   }

   public String getType() {
      return this.type;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof StoreConfig)) {
         return false;
      } else {
         return !this.type.equals(((StoreConfig)var1).type) ? false : super.equals(var1);
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeUTF("9.0");
      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      String var2 = var1.readUTF();
      if (!var2.equals("9.0")) {
         throw new IOException("Wrong version, expected: 9.0 actual: " + var2);
      } else {
         var1.defaultReadObject();
      }
   }
}
