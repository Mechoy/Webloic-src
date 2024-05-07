package weblogic.jdbc.utils;

import java.util.Properties;
import weblogic.jdbc.common.internal.JDBCUtil;

public class JnetDirectJDBC4DriverURLHelper extends JDBCURLHelper {
   public String getURL() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getDbmsPort())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPortReqd());
      } else if (!this.isValid(var1.getDbmsHost())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbHostReqd());
      } else {
         return "jdbc:JSQLConnect://" + var1.getDbmsHost() + ":" + var1.getDbmsPort();
      }
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      Properties var2 = new Properties();
      if (this.isValid(var1.getUserName())) {
         var2.put("user", var1.getUserName());
      }

      if (this.isValid(var1.getDbmsName())) {
         var2.put("databaseName", var1.getDbmsName());
      }

      return var2;
   }
}
