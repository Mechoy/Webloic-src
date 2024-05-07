package weblogic.spring.monitoring;

import java.io.Serializable;
import weblogic.management.runtime.SpringBeanDependencyValue;

public class SpringBeanDependencyValueImpl implements Serializable, SpringBeanDependencyValue {
   private final int injectionType;
   private final String key;
   private final String value;

   public SpringBeanDependencyValueImpl(int var1, String var2, String var3) {
      this.injectionType = var1;
      this.key = var2;
      this.value = var3;
   }

   public int getInjectionType() {
      return this.injectionType;
   }

   public String getKey() {
      return this.key;
   }

   public String getStringValue() {
      return this.value;
   }
}
