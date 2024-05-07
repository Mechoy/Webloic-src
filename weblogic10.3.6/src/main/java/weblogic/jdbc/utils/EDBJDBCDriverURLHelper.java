package weblogic.jdbc.utils;

import java.util.Properties;
import weblogic.jdbc.common.internal.JDBCUtil;

public class EDBJDBCDriverURLHelper extends JDBCURLHelper {
   public String getURL() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getDbmsHost())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbHostReqd());
      } else {
         String var2 = "jdbc:edb://" + var1.getDbmsHost();
         if (this.isValid(var1.getDbmsPort())) {
            var2 = var2 + ":" + var1.getDbmsPort();
         }

         if (!this.isValid(var1.getDbmsName())) {
            throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbNameReqd());
         } else {
            return var2 + "/" + var1.getDbmsName();
         }
      }
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      Properties var2 = new Properties();
      if (this.isValid(var1.getUserName())) {
         var2.put("user", var1.getUserName());
      }

      return var2;
   }
}
