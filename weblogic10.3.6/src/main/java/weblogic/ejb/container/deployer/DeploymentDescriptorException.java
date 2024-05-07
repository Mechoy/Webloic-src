package weblogic.ejb.container.deployer;

import weblogic.j2ee.validation.IDescriptorError;
import weblogic.j2ee.validation.IDescriptorErrorInfo;
import weblogic.utils.NestedException;

public final class DeploymentDescriptorException extends NestedException implements IDescriptorError {
   private static final long serialVersionUID = -2315890241012901760L;
   private IDescriptorErrorInfo errorInfo;

   public DeploymentDescriptorException(String var1, IDescriptorErrorInfo var2) {
      super(var1);
      this.errorInfo = var2;
   }

   public DeploymentDescriptorException(Throwable var1, IDescriptorErrorInfo var2) {
      super(var1);
      this.errorInfo = var2;
   }

   public DeploymentDescriptorException(String var1, Throwable var2, IDescriptorErrorInfo var3) {
      super(var1, var2);
      this.errorInfo = var3;
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
