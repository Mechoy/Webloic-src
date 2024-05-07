package weblogic.auddi.uddi;

public class LanguageErrorException extends UDDIException {
   public LanguageErrorException() {
      this((String)null);
   }

   public LanguageErrorException(String var1) {
      super(10060, var1 == null ? "" : var1);
   }
}
