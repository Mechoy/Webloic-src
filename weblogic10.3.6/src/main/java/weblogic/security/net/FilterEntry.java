package weblogic.security.net;

import java.net.InetAddress;

abstract class FilterEntry {
   static final int ALLOW = 0;
   static final int DENY = 1;
   static final int IGNORE = 2;
   private int protomask;
   private boolean action;

   protected FilterEntry(boolean var1, int var2) {
      this.protomask = var2;
      this.action = var1;
   }

   int check(InetAddress var1, int var2, InetAddress var3, int var4) {
      if (this.match(var1, var3, var4) && this.match(var2)) {
         return this.action ? 0 : 1;
      } else {
         return 2;
      }
   }

   protected abstract boolean match(InetAddress var1, InetAddress var2, int var3);

   protected boolean match(int var1) {
      return this.protomask == 0 || (var1 & this.protomask) != 0;
   }
}
