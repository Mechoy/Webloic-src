package weblogic.messaging.saf;

public class SAFTransportType {
   public static final int WSRM = 2;
   public static final int WSRM_JAXWS = 3;

   public static boolean isConnectionless(int var0) {
      switch (var0) {
         case 2:
            return true;
         case 3:
            return true;
         default:
            return false;
      }
   }
}
