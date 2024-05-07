package weblogic.wsee.handler;

import weblogic.utils.NestedException;
import weblogic.xml.stream.Location;

public class DDProcessingException extends NestedException {
   private StringBuffer locationString = new StringBuffer();

   public DDProcessingException() {
   }

   public DDProcessingException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public DDProcessingException(String var1) {
      super(var1);
   }

   public DDProcessingException(String var1, Location var2) {
      super(var1);
      int var3 = var2.getLineNumber();
      int var4 = var2.getColumnNumber();
      if (var3 > 0) {
         this.locationString.append(" (Line ").append(var3);
         if (var4 > 0) {
            this.locationString.append(", Column ").append(var4);
         }

         this.locationString.append(")");
      }

   }

   public String getMessage() {
      return this.locationString.length() > 0 ? super.getMessage() + this.locationString.toString() : super.getMessage();
   }

   public String getLocalizedMessage() {
      return this.getMessage();
   }
}
