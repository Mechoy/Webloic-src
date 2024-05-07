package weblogic.security.net;

import java.net.InetAddress;
import java.util.Locale;

class SlowFilterEntry extends FilterEntry {
   private String pattern;
   private int localAddress;
   private int localPort;

   SlowFilterEntry(boolean var1, int var2, String var3, int var4, int var5) {
      super(var1, var2);
      this.pattern = var3.toLowerCase(Locale.ENGLISH).substring(1);
      this.localAddress = var4;
      this.localPort = var5;
   }

   protected boolean match(InetAddress var1, InetAddress var2, int var3) {
      return var1.getHostName().toLowerCase(Locale.ENGLISH).endsWith(this.pattern) && (ConnectionFilterImpl.addressToInt(var2) == this.localAddress || this.localAddress == -1) && (var3 == this.localPort || this.localPort == -1);
   }
}
