package weblogic.wsee.reliability2.store;

import weblogic.wsee.persistence.StoreConnection;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.reliability2.sequence.Sequence;

public class SequenceStoreConnection<S extends Sequence> extends StoreConnection<String, S> {
   private String _logicalStoreName;

   public SequenceStoreConnection(String var1, String var2, String var3) throws StoreException {
      super(var2, var3);
      this._logicalStoreName = var1;
   }

   protected void recoverValue(S var1) {
      var1.setLogicalStoreName(this._logicalStoreName);
   }
}
