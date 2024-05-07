package weblogic.security.net;

import java.net.InetAddress;

class FastFilterEntry extends FilterEntry {
   private int addrMask;
   private int netMask;
   private int localAddress;
   private int localPort;

   FastFilterEntry(boolean var1, int var2, int var3, int var4, int var5, int var6) {
      super(var1, var2);
      this.addrMask = var3 & var4;
      this.netMask = var4;
      this.localAddress = var5;
      this.localPort = var6;
   }

   protected boolean match(InetAddress var1, InetAddress var2, int var3) {
      if (this.addrMask == 0) {
         return true;
      } else {
         return (ConnectionFilterImpl.addressToInt(var1) & this.netMask) == this.addrMask && (ConnectionFilterImpl.addressToInt(var2) == this.localAddress || this.localAddress == -1) && (var3 == this.localPort || this.localPort == -1);
      }
   }
}
