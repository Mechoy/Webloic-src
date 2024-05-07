package weblogic.wsee.mc.processor;

import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.persistence.LogicalStore;
import weblogic.wsee.persistence.StoreException;

public final class PendingStore extends LogicalStore<String, McPending> {
   private static final String CONNECTION_NAME = PendingStore.class.getName();
   private static final Logger LOGGER;
   private static final PendingStore _instance;

   public static PendingStore getStore(String var0) throws StoreException {
      return (PendingStore)_instance.getOrCreateLogicalStore(var0, CONNECTION_NAME);
   }

   private PendingStore() {
   }

   private PendingStore(String var1) throws StoreException {
      super(var1, CONNECTION_NAME);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(" == PendingStore created for " + this.getName() + " and connection " + CONNECTION_NAME);
      }

   }

   public PendingStore createLogicalStore(String var1, String var2) throws StoreException {
      return new PendingStore(var1);
   }

   public PendingStoreConnection createStoreConnection(String var1, String var2) throws StoreException {
      return new PendingStoreConnection(this.getName(), var1, var2);
   }

   static {
      LOGGER = Logger.getLogger(CONNECTION_NAME);
      _instance = new PendingStore();
      _instance.toString();
   }
}
