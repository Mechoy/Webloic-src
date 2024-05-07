package weblogic.jdbc.utils;

import java.util.Properties;
import weblogic.jdbc.common.internal.JDBCUtil;

public class OracleJDBCDriverURLHelper extends JDBCURLHelper {
   public String getURL() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getDbmsName())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbNameReqd());
      } else {
         StringBuffer var2 = new StringBuffer("jdbc:oracle:oci:@");
         var2.append(var1.getDbmsName());
         return var2.toString();
      }
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (var1.getDbmsName() == null) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().oracleUserIdReqd());
      } else {
         Properties var2 = new Properties();
         String var3 = var1.getUserName();
         if (var3 != null) {
            var2.put("user", var3);
         }

         return var2;
      }
   }
}
