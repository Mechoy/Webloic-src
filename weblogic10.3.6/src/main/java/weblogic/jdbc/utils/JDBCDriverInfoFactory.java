package weblogic.jdbc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public final class JDBCDriverInfoFactory {
   private List driverInfos = null;
   private String[] dbVendorNames;

   JDBCDriverInfoFactory(List var1) {
      this.driverInfos = var1;
   }

   public String[] getDBMSVendorNames() {
      if (this.driverInfos == null) {
         return null;
      } else {
         if (this.dbVendorNames == null) {
            HashSet var1 = new HashSet();
            Iterator var2 = this.driverInfos.iterator();

            while(var2.hasNext()) {
               MetaJDBCDriverInfo var3 = (MetaJDBCDriverInfo)var2.next();
               var1.add(var3.getDbmsVendor());
            }

            this.dbVendorNames = new String[var1.size()];
            this.dbVendorNames = (String[])((String[])var1.toArray(this.dbVendorNames));
            Arrays.sort(this.dbVendorNames);
         }

         return this.dbVendorNames;
      }
   }

   public JDBCDriverInfo[] getDriverInfos(String var1) throws Exception {
      TreeSet var2 = new TreeSet();
      Iterator var3 = this.driverInfos.iterator();

      while(true) {
         MetaJDBCDriverInfo var4;
         do {
            if (!var3.hasNext()) {
               JDBCDriverInfo[] var5 = new JDBCDriverInfo[var2.size()];
               var5 = (JDBCDriverInfo[])((JDBCDriverInfo[])var2.toArray(var5));
               Arrays.sort(var5);
               return var5;
            }

            var4 = (MetaJDBCDriverInfo)var3.next();
         } while(var4.getDescription() != null && var4.getDescription().indexOf("GridLink") != -1);

         if (var4.getDbmsVendor().equals(var1)) {
            var2.add(new JDBCDriverInfo(var4));
         }
      }
   }

   public JDBCDriverInfo[] getGridLinkDriverInfos() throws Exception {
      TreeSet var1 = new TreeSet();
      Iterator var2 = this.driverInfos.iterator();

      while(var2.hasNext()) {
         MetaJDBCDriverInfo var3 = (MetaJDBCDriverInfo)var2.next();
         if (var3.getDescription() != null && var3.getDescription().indexOf("GridLink") != -1) {
            var1.add(new JDBCDriverInfo(var3));
         }
      }

      JDBCDriverInfo[] var4 = new JDBCDriverInfo[var1.size()];
      var4 = (JDBCDriverInfo[])((JDBCDriverInfo[])var1.toArray(var4));
      Arrays.sort(var4);
      return var4;
   }

   public JDBCDriverInfo getDriverInfo(String var1) throws JDBCDriverInfoException {
      Iterator var2 = this.driverInfos.iterator();

      MetaJDBCDriverInfo var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MetaJDBCDriverInfo)var2.next();
      } while(!var3.toString().equals(var1));

      return new JDBCDriverInfo(var3);
   }

   public JDBCDriverInfo getDriverInfoByClass(String var1) {
      Iterator var2 = this.driverInfos.iterator();

      MetaJDBCDriverInfo var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (MetaJDBCDriverInfo)var2.next();
      } while(!var3.getDriverClassName().equals(var1));

      return new JDBCDriverInfo(var3);
   }

   public JDBCDriverInfo getDriverInfoByClass(String var1, boolean var2) {
      Iterator var3 = this.driverInfos.iterator();

      MetaJDBCDriverInfo var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (MetaJDBCDriverInfo)var3.next();
      } while(var4.isForXA() != var2 || !var4.getDriverClassName().equals(var1));

      return new JDBCDriverInfo(var4);
   }

   List getJDBCDriverInfos() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.driverInfos.iterator();

      while(true) {
         MetaJDBCDriverInfo var3;
         do {
            if (!var2.hasNext()) {
               return var1;
            }

            var3 = (MetaJDBCDriverInfo)var2.next();
         } while(var3.getDescription() != null && var3.getDescription().indexOf("GridLink") != -1);

         var1.add(new JDBCDriverInfo(var3));
      }
   }
}
