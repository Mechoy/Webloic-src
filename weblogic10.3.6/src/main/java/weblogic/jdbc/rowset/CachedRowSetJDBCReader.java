package weblogic.jdbc.rowset;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javax.sql.RowSetInternal;
import javax.sql.RowSetReader;

public final class CachedRowSetJDBCReader implements RowSetReader, Serializable {
   private static final long serialVersionUID = 8780947650868222680L;

   public void readData(RowSetInternal var1) throws SQLException {
      if (!(var1 instanceof CachedRowSetImpl)) {
         throw new SQLException("WLSyncProvider only supports WLS CachedRowSet Implmentation.");
      } else {
         CachedRowSetImpl var2 = (CachedRowSetImpl)var1;
         Connection var3 = var2.getConnection();
         CallableStatement var4 = null;
         ResultSet var5 = null;

         try {
            try {
               var4 = var3.prepareCall(var2.getCommand(), 1004, 1008);
            } catch (SQLException var19) {
               var4 = var3.prepareCall(var2.getCommand());
            }

            Iterator var6 = var2.getParameters().iterator();

            while(var6.hasNext()) {
               WLParameter var7 = (WLParameter)var6.next();
               var7.setParam(var4);
            }

            int var21 = var2.getQueryTimeout();
            if (var21 != 0) {
               var4.setQueryTimeout(var21);
            }

            if (!var2.getEscapeProcessing()) {
               var4.setEscapeProcessing(false);
            }

            var4.execute();
            var5 = var4.getResultSet();
            if (var5 != null) {
               var2.populateInternal(var5);
            }
         } finally {
            try {
               if (var5 != null) {
                  var5.close();
               }
            } catch (Exception var18) {
            }

            try {
               if (var4 != null) {
                  var4.close();
               }
            } catch (Exception var17) {
            }

         }

      }
   }
}
