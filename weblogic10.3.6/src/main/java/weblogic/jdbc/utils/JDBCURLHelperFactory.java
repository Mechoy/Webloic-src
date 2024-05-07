package weblogic.jdbc.utils;

import weblogic.utils.AssertionError;

public class JDBCURLHelperFactory {
   private JDBCURLHelperFactory() {
   }

   public static JDBCURLHelperFactory newInstance() {
      return new JDBCURLHelperFactory();
   }

   public JDBCURLHelper getJDBCURLHelper(JDBCDriverInfo var1) throws JDBCURLHelperException {
      if (var1 == null) {
         throw new AssertionError("JDBCDriverInfo can't be null");
      } else {
         String var2 = var1.getURLHelperClassName();
         if (var2 != null && var2.length() >= 1) {
            try {
               JDBCURLHelper var3 = (JDBCURLHelper)Class.forName(var2).newInstance();
               var3.setJDBCDriverInfo(var1);
               return var3;
            } catch (ClassCastException var4) {
               throw new JDBCURLHelperException("Exception instantiating JDBCURLHelperClass", var4);
            } catch (ClassNotFoundException var5) {
               throw new JDBCURLHelperException("Exception instantiating JDBCURLHelperClass", var5);
            } catch (InstantiationException var6) {
               throw new JDBCURLHelperException("Exception instantiating JDBCURLHelperClass", var6);
            } catch (IllegalAccessException var7) {
               throw new JDBCURLHelperException("Exception instantiating JDBCURLHelperClass", var7);
            }
         } else {
            throw new JDBCURLHelperException("URLHelperClassName is invalid for " + var1.getDriverVendor() + "'s " + var1.getDbmsVendor() + " driver");
         }
      }
   }
}
