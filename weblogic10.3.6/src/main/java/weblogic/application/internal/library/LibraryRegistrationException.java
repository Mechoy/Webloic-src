package weblogic.application.internal.library;

import weblogic.management.DeploymentException;

public class LibraryRegistrationException extends DeploymentException {
   public LibraryRegistrationException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public LibraryRegistrationException(Throwable var1) {
      this("", var1);
   }

   public LibraryRegistrationException(String var1) {
      this(var1, (Throwable)null);
   }
}
