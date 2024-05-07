package weblogic.deploy.api.spi.deploy.internal;

import javax.enterprise.deploy.spi.status.ProgressObject;

final class FailedOperationException extends Throwable {
   private ProgressObject po;

   FailedOperationException(ProgressObject var1) {
      super((String)null);
      this.po = var1;
   }

   ProgressObject getProgressObject() {
      return this.po;
   }
}
