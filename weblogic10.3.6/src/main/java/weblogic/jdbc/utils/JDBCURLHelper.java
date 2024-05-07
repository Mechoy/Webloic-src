package weblogic.jdbc.utils;

import java.util.Properties;

public abstract class JDBCURLHelper {
   private JDBCDriverInfo info;

   public JDBCURLHelper() {
   }

   protected JDBCURLHelper(JDBCDriverInfo var1) {
      this.info = var1;
   }

   protected void setJDBCDriverInfo(JDBCDriverInfo var1) {
      this.info = var1;
   }

   public JDBCDriverInfo getJDBCInfo() {
      return this.info;
   }

   public abstract String getURL() throws JDBCDriverInfoException;

   public abstract Properties getProperties() throws JDBCDriverInfoException;

   public boolean isValid(String var1) {
      return var1 != null && var1.length() > 0;
   }
}
