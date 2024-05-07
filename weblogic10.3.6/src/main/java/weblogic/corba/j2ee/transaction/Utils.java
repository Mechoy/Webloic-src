package weblogic.corba.j2ee.transaction;

import org.omg.CosTransactions.Status;

public final class Utils {
   public static final int ots2jtaStatus(Status var0) {
      byte var1;
      switch (var0.value()) {
         case 0:
            var1 = 0;
            break;
         case 1:
            var1 = 1;
            break;
         case 2:
            var1 = 2;
            break;
         case 3:
            var1 = 3;
            break;
         case 4:
            var1 = 4;
            break;
         case 5:
         default:
            var1 = 5;
            break;
         case 6:
            var1 = 6;
            break;
         case 7:
            var1 = 7;
            break;
         case 8:
            var1 = 8;
            break;
         case 9:
            var1 = 9;
      }

      return var1;
   }
}
