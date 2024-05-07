package weblogic.wsee.jws.util;

import com.bea.wlw.runtime.core.config.WlwRuntimeConfigDocument;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import weblogic.management.DomainDir;

public class Config extends JwsProperties {
   public static final String XML_CONFIG_FILENAME = "wlw-runtime-config.xml";
   private static HashMap _wlwConfigs = new HashMap();
   private static boolean _domainConfigValid = true;

   public static String getProperty(String var0) {
      return JwsProperties.getProperty(var0);
   }

   private static void setPropertiesByDBType() {
      Connection var0 = null;
      int var1 = 0;
      String var2 = getProperty("weblogic.jws.ConversationDataSource");

      try {
         var0 = Util.getConnection(var2);
         var1 = Util.getDBType(var0);
      } catch (Exception var13) {
      } finally {
         if (null != var0) {
            try {
               var0.close();
            } catch (SQLException var12) {
            }
         }

      }

      switch (var1) {
         case 0:
         case 1:
         case 2:
         case 3:
         default:
            break;
         case 4:
            ConversationMaxKeyLength_default = "550";
            break;
         case 5:
            ConversationMaxKeyLength_default = "300";
            break;
         case 6:
            ConversationMaxKeyLength_default = "600";
      }

      _defaultProps.setProperty("weblogic.jws.ConversationMaxKeyLength", ConversationMaxKeyLength_default);
   }

   public static void setPropertiesFromConfigFile(File var0) {
      JwsProperties.setPropertiesFromConfigFile(var0);
      setPropertiesByDBType();
   }

   public static boolean isExternalConfigValid() {
      return _domainConfigValid;
   }

   public static WlwRuntimeConfigDocument.WlwRuntimeConfig.WlwConfig getExternalConfig(String var0, String var1) {
      String var2 = var0 + var1;
      return (WlwRuntimeConfigDocument.WlwRuntimeConfig.WlwConfig)_wlwConfigs.get(var2);
   }

   static {
      if (JwsProperties._fileProps.getProperty("weblogic.jws.ConversationMaxKeyLength") == null) {
         setPropertiesByDBType();
         JwsProperties._fileProps.setProperty("weblogic.jws.ConversationMaxKeyLength", JwsProperties.ConversationMaxKeyLength_default);
      }

      File var0 = new File(DomainDir.getRootDir(), "wlw-runtime-config.xml");
      if (var0.exists()) {
         try {
            WlwRuntimeConfigDocument.WlwRuntimeConfig var1 = WlwRuntimeConfigDocument.Factory.parse(var0).getWlwRuntimeConfig();
            if (var1.validate()) {
               WlwRuntimeConfigDocument.WlwRuntimeConfig.WlwConfig[] var2 = var1.getWlwConfigArray();

               for(int var3 = 0; var3 < var2.length; ++var3) {
                  WlwRuntimeConfigDocument.WlwRuntimeConfig.WlwConfig var4 = var2[var3];
                  String var5 = var4.getContextPath();
                  String var6 = var4.getApplicationName();
                  String var7 = var6 + var5;
                  _wlwConfigs.put(var7, var4);
               }
            } else {
               _domainConfigValid = false;
            }
         } catch (RuntimeException var8) {
            _domainConfigValid = false;
            throw var8;
         } catch (Exception var9) {
            _domainConfigValid = false;
         }
      }

   }
}
