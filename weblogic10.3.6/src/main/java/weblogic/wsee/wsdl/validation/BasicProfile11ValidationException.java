package weblogic.wsee.wsdl.validation;

import weblogic.wsee.util.Verbose;

public class BasicProfile11ValidationException extends WsdlValidationException {
   private static final boolean verbose = Verbose.isVerbose(BasicProfile11ValidationException.class);

   public BasicProfile11ValidationException(BasicProfile11ErrorMessage var1) {
      super(var1.toString());
   }

   public BasicProfile11ValidationException(String var1) {
      super(var1);
   }

   public BasicProfile11ValidationException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
