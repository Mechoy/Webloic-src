package weblogic.auddi.soap;

import weblogic.auddi.NestedException;

public class SOAPWrapperException extends NestedException {
   private static final long serialVersionUID = -7017182100119968618L;

   public SOAPWrapperException(String var1) {
      super(var1);
   }

   public SOAPWrapperException(Exception var1, String var2) {
      super(var1, var2);
   }

   public SOAPWrapperException(Exception var1) {
      super(var1, var1.getMessage());
   }
}
