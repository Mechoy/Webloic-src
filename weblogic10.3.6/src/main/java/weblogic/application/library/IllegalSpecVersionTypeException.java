package weblogic.application.library;

public class IllegalSpecVersionTypeException extends Exception {
   private final String illegalSpecVersion;

   public IllegalSpecVersionTypeException(String var1) {
      this.illegalSpecVersion = var1;
   }

   public String getSpecVersion() {
      return this.illegalSpecVersion;
   }
}
