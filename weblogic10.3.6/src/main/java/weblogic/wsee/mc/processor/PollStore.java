package weblogic.wsee.mc.processor;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.persistence.LogicalStore;
import weblogic.wsee.persistence.StoreConnection;
import weblogic.wsee.persistence.StoreException;

public final class PollStore extends LogicalStore<String, McPoll> {
   private static final String CONNECTION_NAME = PollStore.class.getName();
   private static final Logger LOGGER;
   private static final PollStore _instance;

   public static PollStore getStore(String var0) throws StoreException {
      return (PollStore)_instance.getOrCreateLogicalStore(var0, CONNECTION_NAME);
   }

   private PollStore() {
   }

   private PollStore(String var1) throws StoreException {
      super(var1, CONNECTION_NAME);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(" == PollStore created for " + this.getName() + " and connection " + CONNECTION_NAME);
      }

      this.addAvailablePhysicalStores();
   }

   public PollStore createLogicalStore(String var1, String var2) throws StoreException {
      return new PollStore(var1);
   }

   public PollStoreConnection createStoreConnection(String var1, String var2) throws StoreException {
      return new PollStoreConnection(this.getName(), var1, var2);
   }

   public StoreConnection<String, McPoll> removePhysicalStore(String var1) throws StoreException {
      PollStoreConnection var2 = (PollStoreConnection)this.getStoreConnectionInternal(var1);
      Iterator var3 = var2.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();

         try {
            McPollManager.getInstance().stopPoll(var4, false);
         } catch (Exception var6) {
            LOGGER.log(Level.SEVERE, var6.toString(), var6);
         }
      }

      return super.removePhysicalStore(var1);
   }

   public boolean addPhysicalStore(String var1) throws StoreException {
      if (!super.addPhysicalStore(var1)) {
         return false;
      } else {
         PollStoreConnection var2 = (PollStoreConnection)this.getStoreConnectionInternal(var1);
         Iterator var3 = var2.values().iterator();

         while(var3.hasNext()) {
            McPoll var4 = (McPoll)var3.next();
            McPollManager.getInstance().startPoll(var4);
         }

         return true;
      }
   }

   void stopPollsInStore(boolean var1) {
      Iterator var2 = this.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();

         try {
            McPollManager.getInstance().stopPoll(var3, var1);
         } catch (Exception var5) {
            LOGGER.log(Level.SEVERE, var5.toString(), var5);
         }
      }

   }

   static {
      LOGGER = Logger.getLogger(CONNECTION_NAME);
      _instance = new PollStore();
      _instance.toString();
   }
}
