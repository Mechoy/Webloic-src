package weblogic.j2ee.validation;

public final class ComplianceException extends Exception implements IDescriptorError {
   private static final long serialVersionUID = 1874199488237951559L;
   private IDescriptorErrorInfo errorInfo;

   public ComplianceException(String var1) {
      super(var1);
   }

   public ComplianceException(String var1, IDescriptorErrorInfo var2) {
      super(var1);
      this.errorInfo = var2;
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
