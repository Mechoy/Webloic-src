package weblogic.wsee.jws.conversation.database;

import weblogic.wsee.jws.conversation.Store;
import weblogic.wsee.jws.conversation.StoreConfig;
import weblogic.wsee.jws.conversation.StoreException;
import weblogic.wsee.jws.conversation.StoreFactory;

public class DBStoreFactory implements StoreFactory {
   public static final String TYPE = "database";
   private static final String DEFAULT_TABLE_NAME = "WLW_JWS_CONVERSATION_DATA";

   public String getType() {
      return "database";
   }

   public Store createStore(StoreConfig var1) throws StoreException {
      try {
         TableAccess var2 = DbPersistence.initTableAccess("WLW_JWS_CONVERSATION_DATA");
         return new DbStore(var1, var2);
      } catch (Exception var3) {
         throw new StoreException(var3);
      }
   }
}
