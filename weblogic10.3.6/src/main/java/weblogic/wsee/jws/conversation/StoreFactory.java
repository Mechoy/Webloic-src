package weblogic.wsee.jws.conversation;

public interface StoreFactory {
   Store createStore(StoreConfig var1) throws StoreException;

   String getType();
}
