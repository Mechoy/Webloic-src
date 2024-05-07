package weblogic.diagnostics.image;

import weblogic.diagnostics.type.StackTraceUtility;
import weblogic.security.Security;
import weblogic.security.SubjectUtils;

class ImageRequester {
   private static final String IMG_PKG = "weblogic.diagnostics.image";
   Exception requestException;

   ImageRequester(Exception var1) {
      this.requestException = var1;
   }

   String getRequester() {
      int var1 = StackTraceUtility.getMatchingFrames(this.requestException, "weblogic.diagnostics.image");
      if (var1 > 0) {
         --var1;
      }

      return StackTraceUtility.removeFrames(this.requestException, var1);
   }

   String getRequesterThreadName() {
      return Thread.currentThread().getName();
   }

   String getRequesterUserId() {
      return SubjectUtils.getUsername(Security.getCurrentSubject());
   }
}
