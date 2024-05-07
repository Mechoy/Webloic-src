package weblogic.wsee.mc.processor;

import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.persistence.StoreConnection;
import weblogic.wsee.persistence.StoreException;

public final class PendingStoreConnection extends StoreConnection<String, McPending> {
   private static final Logger LOGGER = Logger.getLogger(PendingStoreConnection.class.getName());
   private String _logicalStoreName;

   public PendingStoreConnection(String var1, String var2, String var3) throws StoreException {
      super(var2, var3);
      this._logicalStoreName = var1;
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(" == PendingStoreConnection Created for " + var1 + var2 + " and connection " + var3);
      }

   }

   protected void recoverValue(McPending var1) {
      var1.setLogicalStoreName(this._logicalStoreName);
   }
}
