package weblogic.servlet.cluster.wan;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public final class BatchedSessionState implements Externalizable {
   static final long serialVersionUID = 9157854138389175601L;
   private Update[] updates;

   public BatchedSessionState(Update[] var1) {
      this.updates = var1;
   }

   public Update[] getUpdates() {
      return this.updates;
   }

   public BatchedSessionState() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.updates);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.updates = (Update[])((Update[])var1.readObject());
   }
}
