package weblogic.jms.extensions;

import weblogic.jms.common.SecHelper;
import weblogic.security.internal.ServerAuthenticate;

public class ClientSAFEncrypt {
   private static final String QUIT_WORD = "quit";
   private static final String TAG_START = "<password-encrypted>";
   private static final String TAG_END = "</password-encrypted>";

   private String composeLine(String var1) {
      return "<password-encrypted>" + var1 + "</password-encrypted>";
   }

   private void go(String[] var1) throws Throwable {
      char[] var2;
      String var3;
      if (var1.length <= 0) {
         var3 = ServerAuthenticate.promptValue("Password Key (\"quit\" to end): ", false);
         if (var3 == null || "quit".equals(var3)) {
            return;
         }

         var2 = var3.toCharArray();
      } else {
         var2 = var1[0].toCharArray();
      }

      if (var1.length <= 1) {
         while(true) {
            var3 = ServerAuthenticate.promptValue("Password (\"quit\" to end): ", false);
            if (var3 == null || "quit".equals(var3)) {
               break;
            }

            System.out.println(this.composeLine(SecHelper.encryptString(var2, var3)));
         }
      } else {
         for(int var5 = 1; var5 < var1.length; ++var5) {
            String var4 = var1[var5];
            System.out.println(this.composeLine(SecHelper.encryptString(var2, var4)));
         }
      }

   }

   public static void main(String[] var0) {
      ClientSAFEncrypt var1 = new ClientSAFEncrypt();

      try {
         var1.go(var0);
      } catch (Throwable var5) {
         int var3 = 0;

         for(Throwable var4 = var5; var4 != null; var4 = var4.getCause()) {
            System.err.println("\nERROR: run threw an exception: level " + var3);
            ++var3;
            var4.printStackTrace();
         }
      }

   }
}
