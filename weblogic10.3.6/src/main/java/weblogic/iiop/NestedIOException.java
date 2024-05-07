package weblogic.iiop;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.PrintStream;
import java.io.PrintWriter;
import weblogic.utils.NestedThrowable;
import weblogic.utils.NestedThrowable.Util;

public final class NestedIOException extends IOException implements NestedThrowable {
   static final long serialVersionUID = 7225672770828528184L;
   private Throwable nested;

   public NestedIOException() {
   }

   public NestedIOException(String var1) {
      super(var1);
   }

   public NestedIOException(Throwable var1) {
      this.nested = var1;
   }

   public NestedIOException(String var1, Throwable var2) {
      super(var1);
      this.nested = var2;
   }

   public Throwable getNestedException() {
      return this.getNested();
   }

   public Throwable getNested() {
      return this.nested;
   }

   public String superToString() {
      return super.toString();
   }

   public void superPrintStackTrace(PrintStream var1) {
      super.printStackTrace(var1);
   }

   public void superPrintStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
   }

   public Object writeReplace() throws ObjectStreamException {
      return this.nested;
   }

   public String toString() {
      return Util.toString(this);
   }

   public void printStackTrace(PrintStream var1) {
      Util.printStackTrace(this, var1);
   }

   public void printWriter(PrintWriter var1) {
      Util.printStackTrace(this, var1);
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }
}
