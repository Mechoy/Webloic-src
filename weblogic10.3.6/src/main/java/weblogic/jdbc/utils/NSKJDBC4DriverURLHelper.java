package weblogic.jdbc.utils;

import java.util.Properties;
import weblogic.jdbc.common.internal.JDBCUtil;

public class NSKJDBC4DriverURLHelper extends JDBCURLHelper {
   private String MAX_STATEMENTS_PROPERTY_VALUE = "100";
   private String URL = "jdbc:sqlmx:";

   public String getURL() throws JDBCDriverInfoException {
      return this.URL;
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      Properties var2 = new Properties();
      if (!this.isValid(var1.getDbmsName())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().nskCatalogNameReqd());
      } else if (!this.isValid(var1.getUserName())) {
         throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().nskUserNameReqd());
      } else {
         var2.put("catalog", var1.getDbmsName());
         var2.put("maxStatements", this.MAX_STATEMENTS_PROPERTY_VALUE);
         var2.put("schema", var1.getUserName());
         var2.put("mploc", "$" + var1.getDbmsName() + "." + var1.getUserName());
         return var2;
      }
   }
}
