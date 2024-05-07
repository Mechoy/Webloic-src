package weblogic.servlet;

import weblogic.utils.NestedRuntimeException;

public final class XMLProcessingException extends NestedRuntimeException {
   private static final long serialVersionUID = -2396918057516907699L;

   public XMLProcessingException(Throwable var1) {
      super(var1);
   }

   public XMLProcessingException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
