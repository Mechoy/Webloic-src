package weblogic.jms.forwarder.internal;

import weblogic.jms.forwarder.DestinationName;

public class DestinationNameImpl implements DestinationName {
   private String configName;
   private String jndiName;

   public DestinationNameImpl(String var1, String var2) {
      this.configName = var1;
      this.jndiName = var2;
   }

   public String getConfigName() {
      return this.configName;
   }

   public String getJNDIName() {
      return this.jndiName;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof DestinationNameImpl)) {
         return false;
      } else {
         DestinationNameImpl var2 = (DestinationNameImpl)var1;
         if (this.configName != null) {
            if (!this.configName.equals(var2.configName)) {
               return false;
            }
         } else if (var2.configName != null) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int var1 = this.configName != null ? this.configName.hashCode() : 0;
      return var1;
   }

   public String toString() {
      return "<JNDIName = " + this.jndiName + " : ConfigName = " + this.configName + ">";
   }
}
