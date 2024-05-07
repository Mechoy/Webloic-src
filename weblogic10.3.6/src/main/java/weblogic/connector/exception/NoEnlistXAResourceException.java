package weblogic.connector.exception;

import javax.resource.ResourceException;

public final class NoEnlistXAResourceException extends ResourceException {
   private static final long serialVersionUID = -4964377128937264002L;

   public NoEnlistXAResourceException(String var1, String var2) {
      super(var1, var2);
   }

   public NoEnlistXAResourceException(String var1) {
      super(var1);
   }
}
