package weblogic.wsee.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.rpc.JAXRPCException;

public class WLJAXRPCException extends JAXRPCException {
   private static final String ERROR = "------------------ Real exception is: ----------";

   public WLJAXRPCException() {
   }

   public WLJAXRPCException(String var1) {
      super(var1);
   }

   public WLJAXRPCException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public WLJAXRPCException(Throwable var1) {
      super(var1);
   }

   public void printStackTrace() {
      super.printStackTrace();
      Throwable var1 = this.getLinkedCause();
      if (var1 != null) {
         System.err.println("------------------ Real exception is: ----------");
         var1.printStackTrace();
      }

   }

   public void printStackTrace(PrintStream var1) {
      super.printStackTrace(var1);
      Throwable var2 = this.getLinkedCause();
      if (var2 != null) {
         var1.println("------------------ Real exception is: ----------");
         var2.printStackTrace(var1);
      }

   }

   public void printStackTrace(PrintWriter var1) {
      super.printStackTrace(var1);
      Throwable var2 = this.getLinkedCause();
      if (var2 != null) {
         var1.println("------------------ Real exception is: ----------");
         var2.printStackTrace(var1);
      }

   }
}
