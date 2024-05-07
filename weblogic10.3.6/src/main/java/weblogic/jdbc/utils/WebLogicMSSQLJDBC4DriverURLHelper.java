package weblogic.jdbc.utils;

import java.util.Properties;
import weblogic.jdbc.common.internal.JDBCUtil;

public class WebLogicMSSQLJDBC4DriverURLHelper extends JDBCURLHelper {
   public String getURL() throws JDBCDriverInfoException {
      return "jdbc:weblogic:mssqlserver4";
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getDbmsHost())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbHostReqd());
      } else if (!this.isValid(var1.getUserName())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbUsernameReqd());
      } else {
         Properties var2 = new Properties();
         var2.put("server", var1.getDbmsHost());
         if (this.isValid(var1.getDbmsPort())) {
            var2.put("port", var1.getDbmsPort());
         }

         var2.put("user", var1.getUserName());
         if (this.isValid(var1.getDbmsName())) {
            var2.put("db", var1.getDbmsName());
         }

         return var2;
      }
   }
}
