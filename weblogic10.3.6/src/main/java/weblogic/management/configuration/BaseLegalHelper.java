package weblogic.management.configuration;

import java.util.ArrayList;
import javax.management.InstanceNotFoundException;
import weblogic.management.Admin;
import weblogic.management.WebLogicObjectName;

public class BaseLegalHelper {
   protected static ConfigurationMBean nameToMBean(WebLogicObjectName var0) {
      if (var0 == null) {
         return null;
      } else {
         try {
            return (ConfigurationMBean)Admin.getInstance().getMBeanHome().getMBean(var0);
         } catch (InstanceNotFoundException var2) {
            throw new AssertionError("mbean not found " + var0 + ", " + var2);
         }
      }
   }

   protected static ConfigurationMBean[] namesToMBeans(WebLogicObjectName[] var0, Object[] var1) {
      ArrayList var2 = new ArrayList(var0.length);

      for(int var3 = 0; var3 < var0.length; ++var3) {
         var2.add(nameToMBean(var0[var3]));
      }

      ConfigurationMBean[] var4 = (ConfigurationMBean[])((ConfigurationMBean[])var2.toArray(var1));
      return var4;
   }
}
