package weblogic.jdbc.utils;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import weblogic.jdbc.common.internal.JDBCUtil;

public class OracleRACJDBC4DriverURLHelper extends JDBCURLHelper {
   public String getURL() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      if (!this.isValid(var1.getDbmsHost())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbHostReqd());
      } else if (!this.isValid(var1.getDbmsPort())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPortReqd());
      } else {
         String var2 = this.getServiceName(var1);
         String var3 = this.getProtocol(var1);
         if (!this.isValid(var3)) {
            var3 = "TCP";
         }

         StringBuffer var4 = new StringBuffer("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=" + var3 + ")(HOST=");
         var4.append(var1.getDbmsHost());
         var4.append(")(PORT=");
         var4.append(var1.getDbmsPort());
         var4.append(")))");
         if (this.isValid(var2) || this.isValid(var1.getDbmsName())) {
            var4.append("(CONNECT_DATA=");
         }

         if (this.isValid(var2)) {
            var4.append("(SERVICE_NAME=");
            var4.append(var2);
            var4.append(")");
         }

         if (this.isValid(var1.getDbmsName())) {
            var4.append("(INSTANCE_NAME=");
            var4.append(var1.getDbmsName());
            var4.append(")");
         }

         if (this.isValid(var2) || this.isValid(var1.getDbmsName())) {
            var4.append(")");
         }

         var4.append(")");
         return var4.toString();
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

   private String getServiceName(JDBCDriverInfo var1) {
      Map var2 = var1.getUnknownDriverAttributes();
      if (var2 != null) {
         Iterator var3 = var2.values().iterator();

         while(var3.hasNext()) {
            try {
               JDBCDriverAttribute var4 = (JDBCDriverAttribute)var3.next();
               if (var4.getName().toLowerCase(Locale.ENGLISH).startsWith("servicename")) {
                  if (var4.getValue() == null) {
                     return null;
                  }

                  return var4.getValue();
               }
            } catch (Exception var5) {
            }
         }
      }

      return null;
   }

   private String getProtocol(JDBCDriverInfo var1) {
      Map var2 = var1.getUnknownDriverAttributes();
      if (var2 != null) {
         Iterator var3 = var2.values().iterator();

         while(var3.hasNext()) {
            try {
               JDBCDriverAttribute var4 = (JDBCDriverAttribute)var3.next();
               if (var4.getName().toLowerCase(Locale.ENGLISH).startsWith("protocol")) {
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
