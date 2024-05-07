package weblogic.jdbc.utils;

import java.util.Properties;
import weblogic.jdbc.common.internal.JDBCUtil;

public class PointBaseJDBC4DriverURLHelper extends JDBCURLHelper {
   public String getURL() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getDbmsHost())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbHostReqd());
      } else if (!this.isValid(var1.getDbmsPort())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPortReqd());
      } else if (!this.isValid(var1.getDbmsName())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbNameReqd());
      } else {
         return "jdbc:pointbase:server://" + var1.getDbmsHost() + ":" + var1.getDbmsPort() + "/" + var1.getDbmsName();
      }
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      Properties var2 = new Properties();
      var2.put("databaseName", this.getURL());
      if (this.isValid(var1.getUserName())) {
         var2.put("user", var1.getUserName());
      }

      return var2;
   }
}
