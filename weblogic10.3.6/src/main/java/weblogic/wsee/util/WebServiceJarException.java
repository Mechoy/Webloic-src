package weblogic.wsee.util;

import weblogic.utils.NestedException;

public class WebServiceJarException extends NestedException {
   public WebServiceJarException() {
   }

   public WebServiceJarException(String var1) {
      super(var1);
   }

   public WebServiceJarException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
