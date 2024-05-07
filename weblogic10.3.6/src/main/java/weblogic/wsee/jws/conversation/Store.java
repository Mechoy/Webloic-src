package weblogic.wsee.jws.conversation;

public interface Store {
   void insert(ConversationState var1) throws StoreException;

   void update(ConversationState var1) throws StoreException;

   ConversationState read(String var1) throws StoreException;

   ConversationState readForUpdate(String var1) throws StoreException;

   void delete(String var1) throws StoreException;
}
