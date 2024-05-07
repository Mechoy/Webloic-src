package weblogic.jdbc.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class OracleBIServerURLHelper extends JDBCURLHelper {
   public String getURL() throws JDBCDriverInfoException {
      String var1 = null;
      String var2 = null;
      JDBCDriverInfo var3 = this.getJDBCInfo();
      Map var4 = var3.getUnknownDriverAttributes();
      if (var4 != null) {
         Iterator var5 = var4.values().iterator();

         while(var5.hasNext()) {
            JDBCDriverAttribute var6 = (JDBCDriverAttribute)var5.next();
            if (var6.getName() != null && var6.getValue() != null) {
               if (var6.getName().equals("ServerHost")) {
                  var1 = var6.getValue();
               } else if (var6.getName().equals("ServerPort")) {
                  var2 = var6.getValue();

                  try {
                     Integer.parseInt(var2);
                  } catch (NumberFormatException var11) {
                     throw new JDBCDriverInfoException("serverport");
                  }
               }
            }
         }
      }

      String var12 = "jdbc:oraclebi:";
      if (var1 != null) {
         if (var2 == null) {
            var2 = "9703";
         }

         var12 = var12 + "//" + var1 + ":" + var2 + "/";
      }

      var4 = var3.getUnknownDriverAttributes();
      if (var4 != null) {
         Iterator var13 = var4.values().iterator();

         while(true) {
            JDBCDriverAttribute var7;
            do {
               while(true) {
                  do {
                     do {
                        if (!var13.hasNext()) {
                           return var12;
                        }

                        var7 = (JDBCDriverAttribute)var13.next();
                     } while(var7.getName() == null);
                  } while(var7.getValue() == null);

                  if (var7.getName().equals("Ssl")) {
                     if (!var7.getValue().equals("True") && !var7.getValue().equals("False")) {
                        throw new JDBCDriverInfoException("ssl");
                     }

                     if (!var7.getValue().equals("False")) {
                        break;
                     }
                  } else {
                     if (!var7.getName().equals("TrustAnyServer")) {
                        break;
                     }

                     if (!var7.getValue().equals("True") && !var7.getValue().equals("False")) {
                        throw new JDBCDriverInfoException("trustanyserver");
                     }

                     if (!var7.getValue().equals("False")) {
                        break;
                     }
                  }
               }

               if (var7.getName().equals("LogLevel")) {
                  if (!var7.getValue().equals("SEVERE") && !var7.getValue().equals("WARNING") && !var7.getValue().equals("INFO") && !var7.getValue().equals("CONFIG") && !var7.getValue().equals("FINE") && !var7.getValue().equals("FINER") && !var7.getValue().equals("FINEST")) {
                     throw new JDBCDriverInfoException("loglevel");
                  }
                  break;
               }

               if (var7.getName().equals("PrimaryCcsPort")) {
                  try {
                     Integer.parseInt(var7.getValue());
                     break;
                  } catch (NumberFormatException var10) {
                     throw new JDBCDriverInfoException("primaryccsport");
                  }
               }

               if (var7.getName().equals("SecondaryCcsPort")) {
                  try {
                     Integer.parseInt(var7.getValue());
                     break;
                  } catch (NumberFormatException var9) {
                     throw new JDBCDriverInfoException("secondaryccsport");
                  }
               }
            } while(var7.getName().equals("ServerHost") || var7.getName().equals("ServerPort"));

            var12 = var12 + var7.getName() + "=" + var7.getValue() + ";";
         }
      } else {
         return var12;
      }
   }

   public Properties getProperties() throws JDBCDriverInfoException {
      Properties var1 = new Properties();
      JDBCDriverInfo var2 = this.getJDBCInfo();
      if (this.isValid(var2.getUserName())) {
         var1.put("user", var2.getUserName());
      }

      return var1;
   }
}
