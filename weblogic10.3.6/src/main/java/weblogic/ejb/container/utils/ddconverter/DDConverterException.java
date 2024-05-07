package weblogic.ejb.container.utils.ddconverter;

import weblogic.utils.NestedException;

public final class DDConverterException extends NestedException {
   private static final long serialVersionUID = 3950002593798950436L;

   public DDConverterException() {
   }

   public DDConverterException(String var1) {
      super(var1);
   }

   public DDConverterException(Throwable var1) {
      super(var1);
   }

   public DDConverterException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
