package weblogic.servlet.internal.dd.compliance;

import weblogic.j2ee.validation.IDescriptorError;
import weblogic.j2ee.validation.IDescriptorErrorInfo;

public class ComplianceException extends Exception implements IDescriptorError {
   private IDescriptorErrorInfo info;

   public ComplianceException(String var1) {
      super(var1);
      this.info = null;
   }

   public ComplianceException(String var1, DescriptorErrorInfo var2) {
      super(var1);
      this.info = var2;
   }

   public boolean hasErrorInfo() {
      return this.info != null;
   }

   public IDescriptorErrorInfo getErrorInfo() {
      return this.info;
   }

   public void setDescriptorErrorInfo(IDescriptorErrorInfo var1) {
      this.info = var1;
   }
}
