package weblogic.jms.common;

import weblogic.jms.module.JMSBeanHelper;

public class EntityName extends ModuleName {
   private String entityName;
   private String fullyQualifiedEntityName;

   public EntityName(String var1, String var2, String var3) {
      super(var1, var2);
      this.entityName = var3;
      this.fullyQualifiedEntityName = JMSBeanHelper.getDecoratedName(this.getFullyQualifiedModuleName(), this.entityName);
   }

   public EntityName(ModuleName var1, String var2) {
      this(var1.getApplicationName(), var1.getEARModuleName(), var2);
   }

   public String getEntityName() {
      return this.entityName;
   }

   public String getFullyQualifiedEntityName() {
      return this.fullyQualifiedEntityName;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof EntityName) {
         EntityName var2 = (EntityName)var1;
         return var2.fullyQualifiedEntityName.equals(this.fullyQualifiedEntityName);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.fullyQualifiedEntityName.hashCode();
   }

   public String toString() {
      return this.fullyQualifiedEntityName;
   }
}
