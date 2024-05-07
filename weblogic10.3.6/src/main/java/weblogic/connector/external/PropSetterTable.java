package weblogic.connector.external;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import weblogic.j2ee.descriptor.ConfigPropertyBean;

public class PropSetterTable {
   HashMap propSetterTable = new HashMap();
   Hashtable propNameToRAPropTable = new Hashtable();

   public Method getSetMethod(ConfigPropertyBean var1) {
      return (Method)this.propSetterTable.get(var1);
   }

   public boolean hasRAProperty(ConfigPropertyBean var1) {
      return this.propSetterTable.containsKey(var1);
   }

   public ConfigPropertyBean getRAProperty(String var1) {
      return (ConfigPropertyBean)this.propNameToRAPropTable.get(var1);
   }

   public void setMethod(ConfigPropertyBean var1, Method var2) {
      this.propSetterTable.put(var1, var2);
      String var3 = var1.getConfigPropertyName();
      if (!this.propNameToRAPropTable.containsKey(var3)) {
         this.propNameToRAPropTable.put(var1.getConfigPropertyName(), var1);
      }

   }
}
