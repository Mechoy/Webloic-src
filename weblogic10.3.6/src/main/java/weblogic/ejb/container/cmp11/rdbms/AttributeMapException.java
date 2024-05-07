package weblogic.ejb.container.cmp11.rdbms;

import weblogic.utils.PlatformConstants;

public final class AttributeMapException extends Exception implements PlatformConstants {
   private static final long serialVersionUID = 3852028467888368719L;
   public static final int COLUMN_NOT_FOUND = 1;
   public static final int FIELD_NOT_FOUND = 2;
   public static final int EXTRA_ENTRY_IN_ATTRIBUTE_MAP = 3;
   public static final int MISSING_ENTRY_IN_ATTRIBUTE_MAP = 4;
   private int errorCode;
   private String value;

   public AttributeMapException(int var1, String var2) {
      super("Illegal setting in the RDBMS Bean attribute-map element.");
      this.errorCode = var1;
      this.value = var2;
   }

   public String getMessage() {
      StringBuffer var1 = new StringBuffer();
      switch (this.errorCode) {
         case 1:
            var1.append("Error in WebLogic CMP RDBMS deployment descriptor.  ");
            var1.append("Column '" + this.value + "', referenced in attribute-map element, ");
            var1.append("was not found in database table.");
            break;
         case 2:
            var1.append("Error in WebLogic CMP RDBMS deployment descriptor.  ");
            var1.append("Field '" + this.value + "', referenced in attribute-map element, ");
            var1.append("was not found in EJB bean class.");
            break;
         case 3:
            var1.append("Error in WebLogic CMP RDBMS deployment descriptor.  ");
            var1.append("Field '" + this.value + "', referenced in attribute-map element, ");
            var1.append("was not declared as a container-managed field in ejb-jar.xml.");
            break;
         case 4:
            var1.append("Error in WebLogic CMP RDBMS deployment descriptor.  ");
            var1.append("Container managed field '" + this.value + "', declared in ejb-jar.xml, ");
            var1.append("is missing from the attribute-map element.");
            break;
         default:
            throw new AssertionError("AttributeMapException error code " + this.errorCode + " does not exist.");
      }

      return var1.toString();
   }
}
