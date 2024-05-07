package weblogic.ejb20.persistence.spi;

import java.io.PrintStream;
import weblogic.ejb.container.InternalException;
import weblogic.utils.AssertionError;

public final class PersistenceRuntimeException extends RuntimeException {
   private static final long serialVersionUID = 8026063946735631325L;
   private Throwable causeException;

   public Throwable getCausedByException() {
      return this.causeException;
   }

   public void printStackTrace(PrintStream var1) {
      this.causeException.printStackTrace(var1);
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public String toString() {
      return this.causeException.toString();
   }

   private PersistenceRuntimeException() {
   }

   public PersistenceRuntimeException(Throwable var1) {
      if (var1 == null) {
         throw new AssertionError("Attempted to create a PersistenceRuntimeException with a null causeException.");
      } else {
         if (var1 instanceof InternalException) {
            InternalException var2 = (InternalException)var1;
            if (var2.detail != null) {
               var1 = var2.detail;
            }
         }

         this.causeException = var1;
      }
   }

   public PersistenceRuntimeException(String var1, Throwable var2) {
      super(var1);
      if (var2 == null) {
         throw new AssertionError("Attempted to create a PersistenceRuntimeException with a null nested Exception.");
      } else {
         if (var2 instanceof InternalException) {
            InternalException var3 = (InternalException)var2;
            if (var3.detail != null) {
               var2 = var3.detail;
            }
         }

         this.causeException = var2;
      }
   }
}
