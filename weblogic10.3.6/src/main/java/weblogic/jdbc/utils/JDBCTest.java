package weblogic.jdbc.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class JDBCTest {
   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         System.err.println("Usage JDBCTest <files ...>");
         System.exit(1);
      }

      JDBCConnectionMetaDataParser var1 = new JDBCConnectionMetaDataParser(var0[0]);
      JDBCURLHelperFactory var2 = JDBCURLHelperFactory.newInstance();
      JDBCDriverInfoFactory var3 = var1.getJDBCDriverInfoFactory();
      String[] var4 = var3.getDBMSVendorNames();
      System.out.println("List of DataBase Vendors");

      for(int var5 = 0; var5 < var4.length; ++var5) {
         System.out.println(var4[var5]);
      }

      System.out.println("\n\n");
      List var12 = var3.getJDBCDriverInfos();

      for(Iterator var6 = var12.iterator(); var6.hasNext(); System.out.println("\n")) {
         JDBCDriverInfo var7 = (JDBCDriverInfo)var6.next();
         System.out.println("==========================================================================");
         System.out.println(var7.displayString());
         System.out.println(var7.getDriverClassName() + "  " + var7.getURLHelperClassName());
         if (var7.isServerNameRequired()) {
            System.out.println("ServerName is required");
            System.out.println("           setting to testdb");
            var7.setDbmsName("testdb");
         }

         if (var7.isPortRequired()) {
            System.out.println("PortName is required");
            System.out.println("         default is : " + var7.getDbmsPortDefault());
            System.out.println("         setting to 7777");
            var7.setDbmsPort("7777");
         }

         if (var7.isHostNameRequired()) {
            System.out.println("HostName is required");
            System.out.println("         default is : " + var7.getDbmsHostDefault());
            System.out.println("         setting to localhost");
            var7.setDbmsHost("localhost");
         }

         if (var7.isUserNameRequired()) {
            System.out.println("UserName is required");
            System.out.println("         setting to scott");
            var7.setUserName("scott");
         }

         if (var7.isPassWordRequired()) {
            System.out.println("Password is required");
            System.out.println("         setting to tiger");
            var7.setPassword("tiger");
         }

         System.out.println("isDriverInClasspath() : " + var7.isDriverInClasspath());
         JDBCURLHelper var8 = var2.getJDBCURLHelper(var7);
         System.out.println("URL is : " + var8.getURL());
         Properties var9 = var8.getProperties();
         System.out.print("Properties are : ");
         if (var9 != null) {
            System.out.print("\n");
            Iterator var10 = var9.keySet().iterator();

            while(var10.hasNext()) {
               String var11 = (String)var10.next();
               System.out.print(var11 + " : " + (String)var9.get(var11) + "\n");
            }
         } else {
            System.out.print("none");
         }
      }

   }
}
