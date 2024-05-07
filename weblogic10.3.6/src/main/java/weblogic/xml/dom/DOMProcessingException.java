package weblogic.xml.dom;

import java.io.PrintWriter;
import weblogic.utils.NestedException;

public class DOMProcessingException extends NestedException {
   public DOMProcessingException() {
   }

   public DOMProcessingException(String var1) {
      super(var1);
   }

   public DOMProcessingException(Throwable var1) {
      super(var1);
   }

   public DOMProcessingException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public void writeErrorCondition(PrintWriter var1) {
      var1.println("[Begin DOMProcessingException:");
      var1.println("End DOMProcessingException]");
   }
}
