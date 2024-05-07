package weblogic.ejb20.cmp.rdbms.finders;

import weblogic.ejb.container.EJBTextTextFormatter;
import weblogic.j2ee.validation.IDescriptorError;
import weblogic.j2ee.validation.IDescriptorErrorInfo;
import weblogic.utils.PlatformConstants;

public final class InvalidFinderException extends Exception implements PlatformConstants, IDescriptorError {
   private static final long serialVersionUID = 110338118255488893L;
   public static final int NULL_NAME = 1;
   public static final int EMPTY_NAME = 2;
   public static final int INVALID_NAME_PREFIX = 3;
   public static final int NULL_QUERY = 4;
   public static final int INVALID_EXPRESSION_NUMBER = 5;
   public static final int EMPTY_EXPRESSION_TEXT = 6;
   public static final int EMPTY_EXPRESSION_TYPE = 7;
   public static final int INVALID_QUERY = 8;
   private int errorType;
   private String failedString;
   private IDescriptorErrorInfo errorInfo;

   public InvalidFinderException(int var1, String var2) {
      this.errorType = 0;
      this.failedString = null;
      this.errorType = var1;
      this.failedString = var2;
   }

   public InvalidFinderException(int var1, String var2, IDescriptorErrorInfo var3) {
      this(var1, var2);
      this.errorInfo = var3;
   }

   public int getErrorType() {
      return this.errorType;
   }

   public String getFailedString() {
      return this.failedString;
   }

   public String getMessage() {
      StringBuffer var1 = new StringBuffer();
      EJBTextTextFormatter var2 = new EJBTextTextFormatter();
      switch (this.errorType) {
         case 1:
            var1.append(var2.nullName(this.failedString));
            break;
         case 2:
            var1.append(var2.emptyName(this.failedString));
            break;
         case 3:
            var1.append(var2.invalidNamePrefix(this.failedString));
            break;
         case 4:
            var1.append(var2.nullQuery(this.failedString));
            break;
         case 5:
            var1.append(var2.invalidExpressionNumber(this.failedString));
            break;
         case 6:
            var1.append(var2.emptyExpressionText(this.failedString));
            break;
         case 7:
            var1.append(var2.emptyExpressionType(this.failedString));
            break;
         case 8:
            var1.append(var2.invalidQuery(this.failedString));
      }

      var1.append(EOL);
      return var1.toString();
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
