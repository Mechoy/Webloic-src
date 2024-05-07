package weblogic.xml.security.signature;

public class InvalidReferenceException extends XMLSignatureException {
   private Reference reference;

   public InvalidReferenceException(String var1, Throwable var2, Reference var3) {
      super(var1, var2);
      this.reference = var3;
   }

   public InvalidReferenceException(String var1, Reference var2) {
      super(var1);
      this.reference = var2;
   }

   public InvalidReferenceException(Throwable var1, Reference var2) {
      super(var1);
      this.reference = var2;
   }

   public Reference getReference() {
      return this.reference;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.reference.getURI()).append(" failed reference validation: ");
      var1.append(this.getMessage());
      return var1.toString();
   }
}
