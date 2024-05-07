package weblogic.connector.external.impl;

import weblogic.j2ee.descriptor.wl.ConfigPropertiesBean;
import weblogic.j2ee.descriptor.wl.ConfigPropertyBean;

public class DDLayerUtils {
   protected static ConfigPropInfoImpl getConfigPropInfoWithOverrides(ConfigPropInfoImpl var0, ConfigPropertiesBean var1) {
      String var2 = var0.getValue();
      if (var1 != null && var1.getProperties() != null && var1.getProperties().length > 0) {
         ConfigPropertyBean[] var3 = var1.getProperties();

         for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
            if (var0.getName().equalsIgnoreCase(var3[var4].getName())) {
               var2 = var3[var4].getValue();
               break;
            }
         }
      }

      return new ConfigPropInfoImpl(var0.getConfigPropertyBean(), var2);
   }
}
