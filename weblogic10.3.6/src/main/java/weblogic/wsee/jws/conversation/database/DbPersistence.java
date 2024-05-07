package weblogic.wsee.jws.conversation.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.wsee.jws.util.Config;
import weblogic.wsee.jws.util.Util;

class DbPersistence {
   private static TableAccess createTableAccess(Connection var0, String var1) throws SQLException {
      // $FF: Couldn't be decompiled
   }

   static TableAccess initTableAccess(String var0) throws SQLException, SystemException, InvalidTransactionException {
      Connection var1 = null;
      TableAccess var2 = null;
      Transaction var3 = null;
      TransactionManager var4 = null;

      try {
         var4 = TxHelper.getTransactionManager();
         if (var4 != null) {
            var3 = var4.suspend();
         }

         var1 = getConnection();
         var2 = createTableAccess(var1, var0);
         if (!Config.iterativeDevDisabled()) {
            var2.ensureTableCreated(var1);
         }

         var1.commit();
      } finally {
         cleanup(var1, (PreparedStatement)null);
         if (var3 != null) {
            var4.resume(var3);
         }

      }

      return var2;
   }

   static Connection getConnection() throws SQLException {
      try {
         return Util.getConnection("java:comp/env/ConversationDataSource");
      } catch (NamingException var3) {
         try {
            return Util.getConnection(Config.getProperty("weblogic.jws.ConversationDataSource"));
         } catch (NamingException var2) {
            throw new EJBException(var2);
         }
      }
   }

   static void cleanup(Connection var0, PreparedStatement var1) {
      Exception var2 = null;
      Exception var3 = null;

      try {
         close((Statement)var1);
      } catch (Exception var6) {
         var2 = var6;
      }

      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (Exception var5) {
         var3 = var5;
      }

      if (var2 != null) {
         throw new EJBException(var2);
      } else if (var3 != null) {
         throw new EJBException(var3);
      }
   }

   static void close(Statement var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (SQLException var2) {
      }

   }

   static void close(ResultSet var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (SQLException var2) {
      }

   }

   static void close(OutputStream var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
      }

   }

   static void close(InputStream var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
      }

   }
}
