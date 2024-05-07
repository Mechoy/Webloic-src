package weblogic.jdbc.utils;

import java.util.Properties;
import weblogic.jdbc.common.internal.JDBCUtil;

public class SybaseJDBC4DriverURLHelper extends JDBCURLHelper {
   public String getURL() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getDbmsHost())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbHostReqd());
      } else if (!this.isValid(var1.getDbmsPort())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPortReqd());
      } else {
         String var2 = var1.getDbmsName();
         if (!this.isValid(var2)) {
            var2 = "";
         } else {
            var2 = "/" + var2;
         }

         return "jdbc:sybase:Tds:" + var1.getDbmsHost() + ":" + var1.getDbmsPort() + var2;
      }
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getDbmsHost())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbHostReqd());
      } else if (!this.isValid(var1.getUserName())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbUsernameReqd());
      } else if (!this.isValid(var1.getPassword())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPasswordReqd());
      } else if (!this.isValid(var1.getDbmsPort())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPortReqd());
      } else {
         Properties var2 = new Properties();
         var2.put("networkProtocol", "Tds");
         var2.put("serverName", var1.getDbmsHost());
         var2.put("portNumber", var1.getDbmsPort());
         var2.put("user", var1.getUserName());
         var2.put("userName", var1.getUserName());
         if (this.isValid(var1.getDbmsName())) {
            var2.put("databaseName", var1.getDbmsName());
         }

         var2.put("url", this.getURL());
         return var2;
      }
   }
}
