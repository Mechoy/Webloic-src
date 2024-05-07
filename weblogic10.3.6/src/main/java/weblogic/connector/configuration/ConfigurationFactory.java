package weblogic.connector.configuration;

import weblogic.connector.external.RAInfo;

public class ConfigurationFactory {
   public static final String SCHEMA_VERSION_1_0 = "1.0";
   public static final String CURRENT_CONFIGURATION_VERSION = "1.0";

   public static Configuration getConfiguration(RAInfo var0) {
      return new Configuration_1_0(var0);
   }

   public static Configuration getConfiguration(String var0, RAInfo var1) {
      Configuration_1_0 var2 = null;
      if (var0 != null && var0.trim().length() > 0 && var0.equals("1.0")) {
         var2 = new Configuration_1_0(var1);
      }

      return var2;
   }
}
