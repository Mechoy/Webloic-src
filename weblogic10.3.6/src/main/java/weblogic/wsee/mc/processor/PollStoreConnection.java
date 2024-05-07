package weblogic.wsee.mc.processor;

import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.persistence.StoreConnection;
import weblogic.wsee.persistence.StoreException;

public final class PollStoreConnection extends StoreConnection<String, McPoll> {
   private static final Logger LOGGER = Logger.getLogger(PollStoreConnection.class.getName());
   private String _logicalStoreName;

   public PollStoreConnection(String var1, String var2, String var3) throws StoreException {
      super(var2, var3);
      this._logicalStoreName = var1;
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("PollStoreConnection Created for logical store " + var1 + ", physical store " + var2 + " and connection name " + var3);
      }

   }

   protected void recoverValue(McPoll var1) {
      var1.setLogicalStoreName(this._logicalStoreName);
   }
}
