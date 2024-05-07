package weblogic.cluster.replication;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.rmi.RemoteException;

public class AsyncBatchFailedException extends RemoteException implements Serializable {
   static final long serialVersionUID = -1L;
   private ROID[] ids;

   public AsyncBatchFailedException(ROID[] var1) {
      this.ids = var1;
   }

   public ROID[] getIDs() {
      return this.ids;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.ids);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.ids = (ROID[])((ROID[])var1.readObject());
   }
}
