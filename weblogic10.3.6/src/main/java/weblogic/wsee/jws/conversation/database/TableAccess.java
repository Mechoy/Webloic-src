package weblogic.wsee.jws.conversation.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

interface TableAccess {
   String getTableName();

   void setTableName(String var1);

   HashMap ensureTableCreated(Connection var1) throws SQLException;

   LoadedObject doLoad(Connection var1, String var2, boolean var3) throws SQLException, IOException;

   boolean doStoreByInsert(Connection var1, String var2, byte[] var3, long var4) throws SQLException, IOException;

   boolean doStoreByUpdate(Connection var1, String var2, byte[] var3, long var4, boolean var6) throws SQLException, IOException;

   boolean doRemove(Connection var1, String var2) throws SQLException;

   boolean keyExists(Connection var1, String var2) throws SQLException;
}
