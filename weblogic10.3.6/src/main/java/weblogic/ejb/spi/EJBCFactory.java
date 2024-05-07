package weblogic.ejb.spi;

import weblogic.ejb.container.ejbc.EJBCompiler;
import weblogic.utils.Getopt2;

public final class EJBCFactory {
   public static EJBC createEJBC(Getopt2 var0) {
      return new EJBCompiler(var0);
   }
}
