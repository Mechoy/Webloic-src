package weblogic.ejb.container.swap;

import java.io.Serializable;

public class TransactionalEntityManagerProxyReplacer implements Serializable {
   private String appName;
   private String moduleName;
   private String unitName;

   public TransactionalEntityManagerProxyReplacer(String var1, String var2, String var3) {
      this.appName = var1;
      this.moduleName = var2;
      this.unitName = var3;
   }

   public String getAppName() {
      return this.appName;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String getUnitName() {
      return this.unitName;
   }
}
