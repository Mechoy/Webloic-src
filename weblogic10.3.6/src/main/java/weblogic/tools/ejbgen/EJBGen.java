package weblogic.tools.ejbgen;

import com.bea.wls.ejbgen.Options;
import com.sun.javadoc.RootDoc;

public class EJBGen {
   public static boolean start(RootDoc var0) {
      return com.bea.wls.ejbgen.EJBGen.start(var0);
   }

   public static int optionLength(String var0) {
      return Options.getInstance().optionLength(var0);
   }

   public static void main(String[] var0) {
      com.bea.wls.ejbgen.EJBGen.main(var0);
   }
}
