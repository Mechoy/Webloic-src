package weblogic.wsee.sender.DefaultProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.persistence.LogicalStore;
import weblogic.wsee.persistence.StoreConnection;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.sender.api.SendRequest;

public final class RequestStore extends LogicalStore<String, SendRequest> {
   private static final String CONNECTION_NAME = RequestStore.class.getName();
   private static final Logger LOGGER;
   private static final RequestStore _instance;

   public static RequestStore getStore(String var0) throws StoreException {
      return (RequestStore)_instance.getOrCreateLogicalStore(var0, CONNECTION_NAME);
   }

   private RequestStore() {
   }

   private RequestStore(String var1) throws StoreException {
      super(var1, CONNECTION_NAME);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(" == RequestStore created for " + this.getName() + " and connection " + CONNECTION_NAME);
      }

   }

   public RequestStore createLogicalStore(String var1, String var2) throws StoreException {
      return new RequestStore(var1);
   }

   protected StoreConnection<String, SendRequest> createStoreConnection(String var1, String var2) throws StoreException {
      return new RequestStoreConnection(var1, var2);
   }

   public List<SendRequest> getPendingRequests(String var1) {
      List var2 = this.getStoreConnections();
      ArrayList var3 = new ArrayList();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         StoreConnection var5 = (StoreConnection)var4.next();
         List var6 = ((RequestStoreConnection)var5).getPendingRequests(var1);
         var3.addAll(var3.size(), var6);
      }

      return var3;
   }

   public boolean removeAllPendingRequests(String var1) throws StoreException {
      List var2 = this.getStoreConnections();
      boolean var3 = false;

      StoreConnection var5;
      for(Iterator var4 = var2.iterator(); var4.hasNext(); var3 |= ((RequestStoreConnection)var5).removeAllPendingRequests(var1)) {
         var5 = (StoreConnection)var4.next();
      }

      return var3;
   }

   public SendRequest get(String var1, long var2) {
      List var4 = this.getStoreConnections();
      SendRequest var5 = null;
      Iterator var6 = var4.iterator();

      while(var6.hasNext()) {
         StoreConnection var7 = (StoreConnection)var6.next();
         var5 = ((RequestStoreConnection)var7).get(var1, var2);
         if (var5 != null) {
            break;
         }
      }

      return var5;
   }

   static {
      LOGGER = Logger.getLogger(CONNECTION_NAME);
      _instance = new RequestStore();
      _instance.toString();
   }
}
