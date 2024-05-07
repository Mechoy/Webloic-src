package weblogic.ejb.container.cmp.rdbms;

import java.io.Serializable;

public class DynamicEJBQLArgumentWrapper implements Serializable {
   private String argumentName;
   private boolean isOracleNLSDataType;

   public DynamicEJBQLArgumentWrapper(String var1, boolean var2) {
      this.argumentName = var1;
      this.isOracleNLSDataType = var2;
   }

   public String getArgumentName() {
      return this.argumentName;
   }

   public boolean isOracleNLSDataType() {
      return this.isOracleNLSDataType;
   }

   public String toString() {
      return this.argumentName.toString();
   }

   public int hashCode() {
      return this.argumentName.hashCode();
   }
}
