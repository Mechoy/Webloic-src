package weblogic.xml.security.signature;

public class ReferenceValidationException extends SignatureValidationException {
   private InvalidReferenceException[] invalidReferenceExceptions;

   public ReferenceValidationException(String var1, InvalidReferenceException[] var2) {
      super(var1);
      this.invalidReferenceExceptions = var2;
   }

   public InvalidReferenceException[] getInvalidReferenceExceptions() {
      return this.invalidReferenceExceptions;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ReferenceValidationException: " + this.getMessage());

      for(int var2 = 0; var2 < this.invalidReferenceExceptions.length; ++var2) {
         var1.append(this.invalidReferenceExceptions[var2].toString());
      }

      return var1.toString();
   }
}
