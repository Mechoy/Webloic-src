package weblogic.iiop;

import javax.rmi.CORBA.Util;

public class repid {
   public static final void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         System.out.println("weblogic.iiop.repid <classname>");
      }

      for(int var1 = 0; var1 < var0.length; ++var1) {
         Class var2 = Class.forName(var0[var1]);
         System.out.println(var2.getName() + ": " + Util.createValueHandler().getRMIRepositoryID(var2));
      }

   }
}
