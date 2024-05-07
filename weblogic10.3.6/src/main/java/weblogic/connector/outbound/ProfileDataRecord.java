package weblogic.connector.outbound;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import weblogic.diagnostics.instrumentation.EventPayload;

public class ProfileDataRecord implements EventPayload {
   public static final String TYPE_CONN_USAGE = "WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.USAGE";
   public static final String TYPE_CONN_LEAK = "WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.LEAK";
   public static final String TYPE_CONN_WAIT = "WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.WAIT";
   public static final String TYPE_CONN_RESV_FAIL = "WEBLOGIC.CONNECTOR.OUTBOUND.CONNECTIONPOOL.RESVFAIL";
   private String type;
   private String poolName;
   private String timestamp;
   private Properties props;

   public ProfileDataRecord(String var1, String var2, Properties var3) {
      this.type = var1;
      this.poolName = var2;
      this.timestamp = (new Date()).toString();
      this.props = var3;
   }

   public String getType() {
      return this.type;
   }

   public String getPoolName() {
      return this.poolName;
   }

   public String getTimestamp() {
      return this.timestamp;
   }

   public Properties getProperties() {
      if (this.props == null) {
         this.props = new Properties();
      }

      return this.props;
   }

   public String getPropertiesString() {
      String var1 = "{ ";
      Enumeration var2 = this.getProperties().propertyNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         String var4 = this.props.getProperty(var3);
         var1 = var1 + var3 + " = " + var4;
         if (var2.hasMoreElements()) {
            var1 = var1 + ", ";
         }
      }

      var1 = var1 + " }";
      return var1;
   }
}
