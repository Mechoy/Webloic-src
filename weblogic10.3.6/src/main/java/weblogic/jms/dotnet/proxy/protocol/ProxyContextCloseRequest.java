package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyContextCloseRequest extends ProxyRequest {
   private static final int EXTVERSION = 1;
   private static final int _IS_CLOSE_ALL = 1;
   private boolean closeAll;

   public ProxyContextCloseRequest(boolean var1) {
      this.closeAll = var1;
   }

   public boolean isCloseAll() {
      return this.closeAll;
   }

   public ProxyContextCloseRequest() {
   }

   public int getMarshalTypeCode() {
      return 7;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.closeAll) {
         this.versionFlags.setBit(1);
      }

      this.versionFlags.marshal(var1);
   }

   public void unmarshal(MarshalReader var1) {
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      if (var2.isSet(1)) {
         this.closeAll = true;
      }

   }
}
