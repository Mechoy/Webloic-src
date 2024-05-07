package weblogic.ejb.container;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

public final class InternalException extends Exception {
   private static final long serialVersionUID = -190849337176816255L;
   public final Throwable detail;
   private transient boolean initCauseInvoked = false;

   public void printStackTrace(PrintStream var1) {
      if (this.detail != null) {
         this.detail.printStackTrace(var1);
      } else {
         super.printStackTrace(var1);
      }

   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public String toString() {
      return this.detail != null ? this.detail.toString() : super.toString();
   }

   private InternalException() {
      this.detail = null;
   }

   public InternalException(String var1) {
      super(var1);
      this.detail = null;
   }

   public InternalException(String var1, Throwable var2) {
      super(var1);
      this.detail = var2;
      this.initCauseInvoked = true;
   }

   public synchronized Throwable initCause(Throwable var1) {
      if (this.initCauseInvoked) {
         return this;
      } else {
         this.initCauseInvoked = true;
         return super.initCause(var1);
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.initCauseInvoked = true;
   }
}
