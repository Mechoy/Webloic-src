package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.Flow;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.management.DeploymentException;

public final class CheckLibraryReferenceFlow extends BaseFlow implements Flow {
   public CheckLibraryReferenceFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      try {
         LibraryLoggingUtils.verifyLibraryReferences(this.appCtx.getLibraryManagerAggregate());
      } catch (LoggableLibraryProcessingException var2) {
         throw new DeploymentException(var2.getLoggable().getMessage());
      }
   }
}
