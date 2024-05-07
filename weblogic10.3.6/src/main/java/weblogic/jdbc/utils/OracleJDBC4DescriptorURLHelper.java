package weblogic.jdbc.utils;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import weblogic.jdbc.common.internal.AddressList;
import weblogic.jdbc.common.internal.JDBCUtil;

public class OracleJDBC4DescriptorURLHelper extends JDBCURLHelper {
   public OracleJDBC4DescriptorURLHelper() {
   }

   public OracleJDBC4DescriptorURLHelper(JDBCDriverInfo var1) {
      super(var1);
   }

   public String getURL() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      AddressList var2 = var1.getHostPorts();
      if (var2 != null && var2.size() != 0) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            AddressList.HostPort var4 = (AddressList.HostPort)var3.next();
            if (!this.isValid(var4.host)) {
               throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbHostReqd());
            }

            if (var4.port <= 0) {
               throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPortReqd());
            }
         }
      } else {
         if (!this.isValid(var1.getDbmsHost())) {
            throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbHostReqd());
         }

         if (!this.isValid(var1.getDbmsPort())) {
            throw new JDBCDriverInfoException(JDBCUtil.getTextFormatter().dbPortReqd());
         }

         var2 = new AddressList();
         var2.add(var1.getDbmsHost(), Integer.valueOf(var1.getDbmsPort()));
      }

      String var8 = this.getProtocol(var1);
      if (!this.isValid(var8)) {
         var8 = "TCP";
      }

      String var9 = this.getServiceName(var1);
      if (!this.isValid(var9)) {
         throw new JDBCDriverInfoException("Service name required");
      } else {
         StringBuffer var5 = new StringBuffer("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=");
         Iterator var6 = var2.iterator();

         while(var6.hasNext()) {
            AddressList.HostPort var7 = (AddressList.HostPort)var6.next();
            if (var7.protocol != null) {
               var5.append("(ADDRESS=(PROTOCOL=" + var7.protocol + ")(HOST=");
            } else {
               var5.append("(ADDRESS=(PROTOCOL=" + var8 + ")(HOST=");
            }

            var5.append(var7.host);
            var5.append(")(PORT=");
            var5.append(var7.port);
            var5.append("))");
         }

         var5.append(")");
         var5.append("(CONNECT_DATA=");
         var5.append("(SERVICE_NAME=");
         var5.append(var9);
         var5.append(")");
         var5.append(")");
         var5.append(")");
         return var5.toString();
      }
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      JDBCDriverInfo var1 = this.getJDBCInfo();
      Properties var2 = new Properties();
      String var3 = var1.getUserName();
      if (var3 != null) {
         var2.put("user", var3);
      }

      return var2;
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
