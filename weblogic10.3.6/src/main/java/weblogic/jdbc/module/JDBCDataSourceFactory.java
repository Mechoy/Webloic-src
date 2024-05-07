package weblogic.jdbc.module;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import javax.sql.DataSource;
import weblogic.common.ResourceException;
import weblogic.j2ee.descriptor.wl.JDBCConnectionPoolBean;
import weblogic.management.configuration.JDBCDataSourceFactoryMBean;

public class JDBCDataSourceFactory {
   private static JDBCDataSourceFactory defaultFactory = new JDBCDataSourceFactory();
   private static Map factoryMap = new Hashtable();
   private JDBCDataSourceFactoryMBean mbean;

   private JDBCDataSourceFactory() {
   }

   private JDBCDataSourceFactory(JDBCDataSourceFactoryMBean var1) {
      this.mbean = var1;
   }

   public static void addDataSourceFactory(JDBCDataSourceFactoryMBean var0) {
      JDBCDataSourceFactory var1 = new JDBCDataSourceFactory(var0);
      factoryMap.put(var0.getFactoryName(), var1);
   }

   public static JDBCDataSourceFactory getDataSourceFactory() {
      return getDataSourceFactory((String)null);
   }

   public static JDBCDataSourceFactory getDataSourceFactory(String var0) {
      return var0 == null ? defaultFactory : (JDBCDataSourceFactory)factoryMap.get(var0);
   }

   public DataSource createDataSource(String var1, String var2, JDBCConnectionPoolBean var3) throws ResourceException {
      LocalDataSource var4 = null;

      try {
         var4 = new LocalDataSource(var3, var1, var2, this.mbean);
         return var4;
      } catch (SQLException var6) {
         throw new ResourceException(var6.getMessage());
      }
   }
}
