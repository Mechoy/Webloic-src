package weblogic.auddi.uddi;

import weblogic.auddi.util.Logger;

public class MissingParameterException extends FatalErrorException {
   public MissingParameterException(String var1) {
      this((Throwable)null, var1);
   }

   public MissingParameterException(Throwable var1) {
      this(var1, (String)null);
   }

   public MissingParameterException(Throwable var1, String var2) {
      super(var2 + (var1 == null ? "" : var1.getMessage()));
      this.m_nested = var1;
      Logger.debug(var1);
   }

   public Throwable getNestedException() {
      return this.m_nested;
   }
}
