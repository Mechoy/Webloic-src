package weblogic.jdbc.utils;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import weblogic.jdbc.common.internal.JDBCUtil;

public class WLInformixJDBC4DriverURLHelper extends JDBCURLHelper {
   public String getURL() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getDbmsHost())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbHostReqd());
      } else if (!this.isValid(var1.getDbmsPort())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPortReqd());
      } else if (!this.isValid(var1.getUserName())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbUsernameReqd());
      } else if (!this.isValid(var1.getPassword())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPasswordReqd());
      } else if (!this.isValid(var1.getDbmsName())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbNameReqd());
      } else if (!this.isValid(this.getInformixServer(var1))) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().informixSvrNameReqd());
      } else {
         return "jdbc:weblogic:informix://" + var1.getDbmsHost() + ":" + var1.getDbmsPort() + ";informixServer=" + this.getInformixServer(var1) + ";databaseName=" + var1.getDbmsName();
      }
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      Properties var2 = new Properties();
      var2.put("serverName", var1.getDbmsHost());
      var2.put("portNumber", var1.getDbmsPort());
      var2.put("user", var1.getUserName());
      var2.put("databaseName", var1.getDbmsName());
      var2.put("informixServer", this.getInformixServer(var1));
      return var2;
   }

   private String getInformixServer(JDBCDriverInfo var1) {
      Map var2 = var1.getUnknownDriverAttributes();
      if (var2 != null) {
         Iterator var3 = var2.values().iterator();

         while(var3.hasNext()) {
            try {
               JDBCDriverAttribute var4 = (JDBCDriverAttribute)var3.next();
               if (var4.getName().toLowerCase(Locale.ENGLISH).startsWith("informixserver")) {
                  if (var4.getValue() == null) {
                     return "";
                  }

                  return var4.getValue();
               }
            } catch (Exception var5) {
            }
         }
      }

      return "";
   }
}
