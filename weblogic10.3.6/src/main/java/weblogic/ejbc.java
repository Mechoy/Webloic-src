package weblogic;

/** @deprecated */
public class ejbc {
   public static void main(String[] var0) throws Exception {
      System.out.println("\nDEPRECATED: The weblogic.ejbc compiler is deprecated and will be removed in a future version of WebLogic Server.  Please use weblogic.appc instead.\n");
      String var1 = System.getProperty("java.protocol.handler.pkgs");
      if (var1 == null) {
         var1 = "weblogic.utils";
      } else {
         var1 = var1 + "|weblogic.utils";
      }

      System.setProperty("java.protocol.handler.pkgs", var1);

      try {
         (new ejbc20(var0)).run();
      } catch (Error var3) {
         var3.printStackTrace();
         throw var3;
      }
   }
}
