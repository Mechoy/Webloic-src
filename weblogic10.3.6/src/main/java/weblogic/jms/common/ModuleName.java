package weblogic.jms.common;

import weblogic.jms.module.JMSBeanHelper;

public class ModuleName {
   private String applicationName;
   private String earModuleName;
   private String fullyQualifiedModuleName;

   public ModuleName(String var1, String var2) {
      this.applicationName = var1;
      this.earModuleName = var2;
      this.fullyQualifiedModuleName = this.earModuleName == null ? this.applicationName : JMSBeanHelper.getDecoratedName(this.applicationName, this.earModuleName);
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public String getEARModuleName() {
      return this.earModuleName;
   }

   public String getFullyQualifiedModuleName() {
      return this.fullyQualifiedModuleName;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof ModuleName) {
         ModuleName var2 = (ModuleName)var1;
         return var2.fullyQualifiedModuleName.equals(this.fullyQualifiedModuleName);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.fullyQualifiedModuleName.hashCode();
   }

   public String toString() {
      return this.fullyQualifiedModuleName;
   }
}
