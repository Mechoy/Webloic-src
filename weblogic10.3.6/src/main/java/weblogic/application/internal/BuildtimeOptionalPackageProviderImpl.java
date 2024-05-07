package weblogic.application.internal;

import weblogic.application.compiler.CompilerCtx;

public class BuildtimeOptionalPackageProviderImpl extends OptionalPackageProviderImpl {
   private CompilerCtx ctx;

   public BuildtimeOptionalPackageProviderImpl(CompilerCtx var1) {
      this.ctx = var1;
   }

   protected FlowContext getflowContext() {
      return this.ctx.getApplicationContext();
   }

   protected String getModuleName() {
      return null;
   }
}
