package weblogic.wsee.jws.conversation;

import javax.xml.rpc.JAXRPCException;
import weblogic.store.PersistentMap;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.store.xa.PersistentStoreXA;

class FileStore implements Store {
   private static final String DEFAULT_MAP_NAME = "WSConversations";
   private PersistentMap map;
   private StoreConfig storeConfig = null;

   FileStore(StoreConfig var1) {
      this.storeConfig = var1;
      String var2 = (String)var1.get("storeName");
      this.init(var2, (String)null);
   }

   public StoreConfig getStoreConfig() {
      return this.storeConfig;
   }

   public void insert(ConversationState var1) throws StoreException {
      try {
         boolean var2 = this.map.put(var1.getId(), var1);
         if (var2) {
            throw new StoreException("Conversation already exists: " + var1.getId());
         }
      } catch (PersistentStoreException var3) {
         throw new StoreException("Could not update persistent state for conversation " + var1.getId(), var3);
      }
   }

   public void update(ConversationState var1) throws StoreException {
      try {
         boolean var2 = this.map.put(var1.getId(), var1);
         if (!var2) {
            throw new StoreException("Attempt to update non-existant conversation: " + var1.getId());
         }
      } catch (PersistentStoreException var3) {
         throw new StoreException("Could not update persistent state for conversation " + var1.getId(), var3);
      }
   }

   public ConversationState read(String var1) throws StoreException {
      try {
         return (ConversationState)this.map.get(var1);
      } catch (PersistentStoreException var3) {
         throw new StoreException("Conversation read failed: id=" + var1, var3);
      }
   }

   public ConversationState readForUpdate(String var1) throws StoreException {
      try {
         return (ConversationState)this.map.get(var1);
      } catch (PersistentStoreException var3) {
         throw new StoreException("Conversation read failed: id=" + var1, var3);
      }
   }

   public void delete(String var1) throws StoreException {
      try {
         this.map.remove(var1);
      } catch (PersistentStoreException var3) {
         throw new StoreException("Conversation delete failed: id=" + var1, var3);
      }
   }

   private void init(String var1, String var2) {
      if (var2 == null) {
         var2 = "WSConversations";
      }

      PersistentStoreXA var3 = this.getPersistentStore(var1);

      try {
         this.map = var3.createPersistentMapXA(var2, new PersistentStoreObjectHandler());
      } catch (PersistentStoreException var5) {
         throw new JAXRPCException("Could not create conversation map", var5);
      }
   }

   private PersistentStoreXA getPersistentStore(String var1) {
      PersistentStoreXA var2 = null;
      if (var1 == null) {
         var2 = (PersistentStoreXA)PersistentStoreManager.getManager().getDefaultStore();
      } else if (PersistentStoreManager.getManager().storeExistsByLogicalName(var1)) {
         var2 = (PersistentStoreXA)PersistentStoreManager.getManager().getStoreByLogicalName(var1);
      } else {
         var2 = (PersistentStoreXA)PersistentStoreManager.getManager().getStore(var1);
      }

      if (var2 == null) {
         throw new JAXRPCException("persistent store not found: " + var1 == null ? "default" : var1);
      } else {
         return var2;
      }
   }
}
