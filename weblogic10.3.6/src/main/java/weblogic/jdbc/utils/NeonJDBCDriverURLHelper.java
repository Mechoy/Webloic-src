package weblogic.jdbc.utils;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import weblogic.jdbc.common.internal.JDBCUtil;

public class NeonJDBCDriverURLHelper extends JDBCURLHelper {
   public String getURL() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      String var2 = this.getDataSource(var1);
      if (!this.isValid(var2)) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().neonDSNameReqd());
      } else if (!this.isValid(var1.getUserName())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbUsernameReqd());
      } else if (!this.isValid(var1.getPassword())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPasswordReqd());
      } else {
         return "jdbc:neon:" + var2 + ";UID=" + var1.getUserName();
      }
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getPassword())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPasswordReqd());
      } else {
         Properties var2 = new Properties();
         return var2;
      }
   }

   private String getDataSource(JDBCDriverInfo var1) {
      Map var2 = var1.getUnknownDriverAttributes();
      if (var2 != null) {
         Iterator var3 = var2.values().iterator();

         while(var3.hasNext()) {
            try {
               JDBCDriverAttribute var4 = (JDBCDriverAttribute)var3.next();
               if (var4.getName().toLowerCase(Locale.ENGLISH).startsWith("datasource")) {
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
