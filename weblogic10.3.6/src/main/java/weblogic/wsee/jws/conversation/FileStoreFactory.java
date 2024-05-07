package weblogic.wsee.jws.conversation;

public class FileStoreFactory implements StoreFactory {
   public static final String STORE_NAME = "storeName";
   public static final String TYPE = "file";

   public String getType() {
      return "file";
   }

   public Store createStore(StoreConfig var1) throws StoreException {
      return new FileStore(var1);
   }
}
