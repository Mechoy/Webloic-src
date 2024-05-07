package weblogic.cluster.replication;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class AsyncBatch implements Externalizable {
   static final long serialVersionUID = -7477097798536735352L;
   private AsyncUpdate[] updates;

   public AsyncBatch(AsyncUpdate[] var1) {
      this.updates = var1;
   }

   public AsyncBatch() {
   }

   public AsyncUpdate[] getUpdates() {
      return this.updates;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.updates);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.updates = (AsyncUpdate[])((AsyncUpdate[])var1.readObject());
   }
}
