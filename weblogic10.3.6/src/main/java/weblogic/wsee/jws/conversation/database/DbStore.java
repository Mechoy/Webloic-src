package weblogic.wsee.jws.conversation.database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import weblogic.wsee.jws.conversation.ConversationState;
import weblogic.wsee.jws.conversation.Store;
import weblogic.wsee.jws.conversation.StoreConfig;
import weblogic.wsee.jws.conversation.StoreException;

class DbStore implements Store {
   private TableAccess _tableAccess;
   private StoreConfig _storeConfig = null;

   DbStore(StoreConfig var1, TableAccess var2) {
      this._tableAccess = var2;
      this._storeConfig = var1;
   }

   public StoreConfig getStoreConfig() {
      return this._storeConfig;
   }

   public void insert(ConversationState var1) throws StoreException {
      Connection var2 = null;

      try {
         var2 = DbPersistence.getConnection();
         byte[] var3 = this.toByteArray(var1);
         this._tableAccess.doStoreByInsert(var2, var1.getId(), var3, var1.getTimeStamp());
      } catch (Exception var8) {
         throw new StoreException(var8);
      } finally {
         DbPersistence.cleanup(var2, (PreparedStatement)null);
      }

   }

   public void update(ConversationState var1) throws StoreException {
      if (this._tableAccess == null) {
         throw new StoreException("Unable to update conversation, Table does not exist");
      } else {
         Connection var2 = null;

         try {
            var2 = DbPersistence.getConnection();
            byte[] var3 = this.toByteArray(var1);
            this._tableAccess.doStoreByUpdate(var2, var1.getId(), var3, var1.getTimeStamp(), true);
         } catch (Exception var8) {
            throw new StoreException(var8);
         } finally {
            DbPersistence.cleanup(var2, (PreparedStatement)null);
         }

      }
   }

   public ConversationState read(String var1) throws StoreException {
      if (this._tableAccess == null) {
         throw new StoreException("Unable to read conversation, Table does not exist");
      } else {
         Connection var2 = null;

         ConversationState var5;
         try {
            var2 = DbPersistence.getConnection();
            LoadedObject var3 = this._tableAccess.doLoad(var2, var1, false);
            Object var4 = var3.getObject();
            var5 = (ConversationState)var4;
         } catch (Exception var10) {
            throw new StoreException(var10);
         } finally {
            DbPersistence.cleanup(var2, (PreparedStatement)null);
         }

         return var5;
      }
   }

   public ConversationState readForUpdate(String var1) throws StoreException {
      if (this._tableAccess == null) {
         throw new StoreException("Unable to read conversation, Table does not exist");
      } else {
         Connection var2 = null;

         ConversationState var5;
         try {
            var2 = DbPersistence.getConnection();
            LoadedObject var3 = this._tableAccess.doLoad(var2, var1, true);
            Object var4 = var3.getObject();
            var5 = (ConversationState)var4;
         } catch (Exception var10) {
            throw new StoreException(var10);
         } finally {
            DbPersistence.cleanup(var2, (PreparedStatement)null);
         }

         return var5;
      }
   }

   public void delete(String var1) throws StoreException {
      if (this._tableAccess == null) {
         throw new StoreException("Unable to delete conversation, Table does not exist");
      } else {
         Connection var2 = null;

         try {
            var2 = DbPersistence.getConnection();
            this._tableAccess.doRemove(var2, var1);
         } catch (Exception var8) {
            throw new StoreException(var8);
         } finally {
            DbPersistence.cleanup(var2, (PreparedStatement)null);
         }

      }
   }

   private byte[] toByteArray(Object var1) throws StoreException {
      ByteArrayOutputStream var2 = null;
      ObjectOutputStream var3 = null;

      byte[] var4;
      try {
         var2 = new ByteArrayOutputStream();
         var3 = new ObjectOutputStream(var2);
         var3.writeObject(var1);
         var4 = var2.toByteArray();
      } catch (IOException var13) {
         throw new StoreException("Unable to serialize", var13);
      } finally {
         try {
            var3.close();
         } catch (IOException var12) {
            var12.printStackTrace();
         }

      }

      return var4;
   }
}
