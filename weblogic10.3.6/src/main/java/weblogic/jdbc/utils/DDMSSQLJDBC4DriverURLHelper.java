package weblogic.jdbc.utils;

import java.util.Properties;
import weblogic.jdbc.common.internal.JDBCUtil;

public class DDMSSQLJDBC4DriverURLHelper extends JDBCURLHelper {
   public String getURL() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getDbmsHost())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbHostReqd());
      } else if (!this.isValid(var1.getDbmsPort())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPortReqd());
      } else {
         String var2 = "jdbc:datadirect:sqlserver://" + var1.getDbmsHost();
         if (var1.getDbmsPort() != null) {
            var2 = var2 + ":" + var1.getDbmsPort();
         }

         return var2;
      }
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getUserName())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbUsernameReqd());
      } else {
         Properties var2 = new Properties();
         var2.put("serverName", var1.getDbmsHost());
         var2.put("portNumber", var1.getDbmsPort());
         var2.put("user", var1.getUserName());
         if (this.isValid(var1.getDbmsName())) {
            var2.put("databaseName", var1.getDbmsName());
         }

         return var2;
      }
   }
}
