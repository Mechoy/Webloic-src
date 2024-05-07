package weblogic.wsee.jaxws.persistence;

import weblogic.wsee.persistence.LogicalStore;
import weblogic.wsee.persistence.StoreException;

public class PersistentContextStore extends LogicalStore<String, PersistentContext> {
   private static final String CONNECTION_NAME = PersistentContextStore.class.getName();
   private static final PersistentContextStore _instance = new PersistentContextStore();

   public static PersistentContextStore getStore(String var0) throws StoreException {
      return (PersistentContextStore)_instance.getOrCreateLogicalStore(var0, CONNECTION_NAME);
   }

   private PersistentContextStore() {
   }

   PersistentContextStore(String var1) throws StoreException {
      super(var1, CONNECTION_NAME);
   }

   public PersistentContextStore createLogicalStore(String var1, String var2) throws StoreException {
      return new PersistentContextStore(var1);
   }

   static {
      _instance.toString();
   }
}
