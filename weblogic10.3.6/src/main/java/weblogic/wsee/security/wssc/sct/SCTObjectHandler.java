package weblogic.wsee.security.wssc.sct;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.store.ObjectHandler;

public class SCTObjectHandler implements ObjectHandler {
   public void writeObject(ObjectOutput var1, Object var2) throws IOException {
      if (var2 instanceof String) {
         var1.writeByte(0);
         var1.writeUTF((String)var2);
      } else if (var2 instanceof SCCredential) {
         var1.writeByte(1);
         ((SCCredential)var2).writeExternal(var1);
      } else {
         throw new IOException("Unable to handle writing object: " + var2);
      }
   }

   public Object readObject(ObjectInput var1) throws ClassNotFoundException, IOException {
      if (var1.readByte() == 0) {
         return var1.readUTF();
      } else {
         SCCredential var2 = new SCCredential();
         var2.readExternal(var1);
         return var2;
      }
   }
}
