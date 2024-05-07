package weblogic.wsee.jws.conversation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import weblogic.store.CustomObjectInput;
import weblogic.store.ObjectHandler;

public class PersistentStoreObjectHandler implements CustomObjectInput, ObjectHandler {
   public ObjectInput getObjectInput(ByteBuffer[] var1) throws IOException {
      return new PersistentStoreObjectInputStream(var1);
   }

   public void writeObject(ObjectOutput var1, Object var2) throws IOException {
      var1.writeObject(var2);
   }

   public Object readObject(ObjectInput var1) throws ClassNotFoundException, IOException {
      return var1.readObject();
   }
}
