package weblogic.servlet.logging;

import weblogic.protocol.ServerChannel;

public final class HostLogField implements LogField {
   private int type;
   private int prefix;

   HostLogField(String var1, String var2) {
      if ("ip".equals(var2)) {
         this.type = 5;
      } else if ("dns".equals(var2)) {
         this.type = 6;
      } else {
         this.type = 0;
      }

      if ("c".equals(var1)) {
         this.prefix = 1;
      } else if ("s".equals(var1)) {
         this.prefix = 2;
      } else {
         this.prefix = 0;
      }

   }

   public void logField(HttpAccountingInfo var1, FormatStringBuffer var2) {
      ServerChannel var3 = var1.getServerChannel();
      String var4 = var3.getInetAddress().getHostName();
      String var5 = var3.getAddress() + ":" + var3.getPublicPort();
      if (this.type == 0 || this.prefix == 0) {
         var2.appendValueOrDash((String)null);
      }

      if (this.prefix == 1) {
         switch (this.type) {
            case 5:
               var2.appendValueOrDash(var1.getRemoteAddr());
               return;
            case 6:
               var2.appendValueOrDash(var1.getRemoteHost());
               return;
         }
      } else if (this.prefix == 2) {
         switch (this.type) {
            case 5:
               var2.appendValueOrDash(var5);
               return;
            case 6:
               var2.appendValueOrDash(var4);
               return;
         }
      }

      var2.appendValueOrDash((String)null);
   }
}
