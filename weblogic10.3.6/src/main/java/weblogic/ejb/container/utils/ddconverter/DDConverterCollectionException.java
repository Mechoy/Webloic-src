package weblogic.ejb.container.utils.ddconverter;

import weblogic.utils.ErrorCollectionException;

public final class DDConverterCollectionException extends ErrorCollectionException {
   private static final long serialVersionUID = 7419737480321698710L;

   public DDConverterCollectionException() {
      super("");
   }

   public DDConverterCollectionException(Exception var1) {
      super(var1);
   }
}
