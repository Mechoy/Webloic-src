package weblogic.store.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.JDBCDriverParamsBean;
import weblogic.jdbc.common.internal.JDBCMBeanConverter;
import weblogic.jdbc.utils.BasicDataSource;
import weblogic.kernel.KernelStatus;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.JDBCStoreMBean;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.store.PersistentStoreException;
import weblogic.store.RuntimeHandler;
import weblogic.store.StoreLogger;
import weblogic.store.io.jdbc.JDBCStoreIO;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.store.xa.internal.PersistentStoreXAImpl;

public class JDBCAdminHandler extends AdminHandler {
   public static final String TABLE_NAME = "WLStore";

   public void activate(DeploymentMBean var1) throws DeploymentException {
      try {
         this.store = makeStore(this.name, this.overrideResourceName, (JDBCStoreMBean)var1, (ClearOrEncryptedService)null, new RuntimeHandlerImpl());
      } catch (PersistentStoreException var3) {
         StoreLogger.logStoreDeploymentFailed(this.name, var3.toString(), var3);
         throw new DeploymentException(var3);
      }

      this.prepareConfig((JDBCStoreMBean)var1);
      super.activate(var1);
   }

   public static DataSource createDataSource(JDBCStoreMBean var0, ClearOrEncryptedService var1) throws PersistentStoreException {
      return createDataSource(var0, var1, true);
   }

   public static DataSource createDataSource(JDBCStoreMBean var0, ClearOrEncryptedService var1, boolean var2) throws PersistentStoreException {
      try {
         if (var0.getDataSource() == null) {
            throw new PersistentStoreException("The JDBC store " + var0.getName() + " has no data source");
         } else {
            JDBCDataSourceBean var3 = var0.getDataSource().getJDBCResource();
            if (var3 != null && var3.getJDBCDataSourceParams() != null) {
               if (var2 && KernelStatus.isServer() && JDBCMBeanConverter.getLegacyType(var3) == 0) {
                  String var18 = parseJNDIName(var3.getJDBCDataSourceParams().getJNDINames());

                  try {
                     InitialContext var20 = new InitialContext();

                     DataSource var22;
                     try {
                        var22 = (DataSource)var20.lookup(var18);
                     } finally {
                        var20.close();
                     }

                     return var22;
                  } catch (NamingException var14) {
                     throw new PersistentStoreException("Can't find JDBC DataSource " + var18 + ": " + var14, var14);
                  }
               } else {
                  JDBCDriverParamsBean var4 = var3.getJDBCDriverParams();
                  if (var4 == null) {
                     throw new PersistentStoreException("Can't connect using JDBC DataSource because there is no jdbc-driver-params element and no JNDI name");
                  } else {
                     if (KernelStatus.isServer()) {
                        try {
                           BasicDataSource var19 = new BasicDataSource("jdbc:weblogic:pool:" + var3.getName(), "weblogic.jdbc.pool.Driver", (Properties)null, (String)null);
                           Connection var21 = var19.getConnection();
                           var21.close();
                           return var19;
                        } catch (Exception var16) {
                        }
                     }

                     String var5 = var4.getPassword();
                     if (var1 != null && isEmptyString(var5) && !isEmptyBytes(var4.getPasswordEncrypted())) {
                        byte[] var6 = var1.decryptBytes(var4.getPasswordEncrypted());

                        try {
                           var5 = new String(var6, "UTF-8");
                        } catch (IOException var15) {
                           throw new PersistentStoreException(var15);
                        }
                     }

                     return new BasicDataSource(var3, var5);
                  }
               }
            } else {
               throw new PersistentStoreException("The data source for JDBC store " + var0.getName() + " does not exist");
            }
         }
      } catch (SQLException var17) {
         throw new PersistentStoreException(var17);
      }
   }

   public static PersistentStoreXA makeStore(String var0, String var1, JDBCStoreMBean var2, ClearOrEncryptedService var3, RuntimeHandler var4) throws PersistentStoreException {
      return makeStore(var0, var1, var2, var3, var4, true);
   }

   public static PersistentStoreXA makeStore(String var0, String var1, JDBCStoreMBean var2, ClearOrEncryptedService var3, RuntimeHandler var4, boolean var5) throws PersistentStoreException {
      JDBCStoreIO var6 = makeStoreIO(var0, var2, var3, var5);
      return new PersistentStoreXAImpl(var0, var6, var1, var4);
   }

   private static JDBCStoreIO makeStoreIO(String var0, JDBCStoreMBean var1, ClearOrEncryptedService var2) throws PersistentStoreException {
      return makeStoreIO(var0, var1, var2, true);
   }

   private static JDBCStoreIO makeStoreIO(String var0, JDBCStoreMBean var1, ClearOrEncryptedService var2, boolean var3) throws PersistentStoreException {
      String var4 = var1.getPrefixName();
      if (var4 != null) {
         var4 = var4.trim();
      }

      String var5;
      if (var4 == null) {
         var5 = "WLStore";
      } else {
         var5 = var4 + "WLStore";
      }

      DataSource var6 = createDataSource(var1, var2, var3);
      if (var6 instanceof BasicDataSource && ((BasicDataSource)var6).isXADataSource()) {
         throw new PersistentStoreException("XA data sources are not supported for the JDBC store");
      } else {
         boolean var7 = true;
         boolean var8 = true;
         boolean var9 = true;
         int var10 = var1.getDeletesPerBatchMaximum();
         int var11 = var1.getInsertsPerBatchMaximum();
         int var12 = var1.getDeletesPerStatementMaximum();
         return new JDBCStoreIO(var0, var6, var5, var1.getCreateTableDDLFile(), var10, var11, var12);
      }
   }

   public static void deleteStore(String var0, JDBCStoreMBean var1, ClearOrEncryptedService var2) throws PersistentStoreException {
      makeStoreIO(var0, var1, var2).destroy();
   }

   private static String parseJNDIName(String[] var0) {
      assert var0 != null && var0.length > 0;

      assert !isEmptyString(var0[0]);

      return var0[0];
   }

   private void prepareConfig(JDBCStoreMBean var1) {
      if (this.config == null) {
         this.config = new HashMap();
      }

      this.config.put("ThreeStepThreshold", var1.getThreeStepThreshold());
      this.config.put("WorkerCount", var1.getWorkerCount());
      this.config.put("WorkerPreferredBatchSize", var1.getWorkerPreferredBatchSize());
   }
}
