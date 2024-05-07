package weblogic.ejb.container.cmp.rdbms.finders;

import weblogic.ejb.container.EJBTextTextFormatter;
import weblogic.j2ee.validation.IDescriptorError;
import weblogic.j2ee.validation.IDescriptorErrorInfo;
import weblogic.utils.PlatformConstants;

public final class IllegalExpressionException extends Exception implements PlatformConstants, IDescriptorError {
   private static final long serialVersionUID = -4466917394467675048L;
   public static final int PATH_EXPRESSION_OUTSIDE_OF_QUERY_TREE = 5;
   public static final int WARNING = 6;
   public static final int ERROR_ENCOUNTERED = 7;
   private int errorCode;
   private String identifier;
   private IDescriptorErrorInfo errorInfo;

   public IllegalExpressionException(int var1, String var2) {
      this.errorCode = 0;
      this.identifier = null;
      this.errorCode = var1;
      this.identifier = var2;
   }

   public IllegalExpressionException(int var1, String var2, IDescriptorErrorInfo var3) {
      this(var1, var2);
      this.errorInfo = var3;
   }

   public String getMessage() {
      StringBuffer var1 = new StringBuffer();
      EJBTextTextFormatter var2 = new EJBTextTextFormatter();
      switch (this.errorCode) {
         case 5:
         case 7:
         default:
            var1.append(var2.errorEncountered(this.identifier));
            break;
         case 6:
            var1.append(var2.warning(this.identifier));
      }

      var1.append(EOL);
      return var1.toString();
   }

   public int getErrorCode() {
      return this.errorCode;
   }

   public boolean hasErrorInfo() {
      return this.errorInfo != null;
   }

   public IDescriptorErrorInfo getErrorInfo() {
      return this.errorInfo;
   }

   public void setDescriptorErrorInfo(IDescriptorErrorInfo var1) {
      this.errorInfo = var1;
   }
}
