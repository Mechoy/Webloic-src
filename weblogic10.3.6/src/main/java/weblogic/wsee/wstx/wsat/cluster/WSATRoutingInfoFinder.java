package weblogic.wsee.wstx.wsat.cluster;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Packet;
import java.util.Iterator;
import javax.xml.namespace.QName;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfo;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfoFinder;

public class WSATRoutingInfoFinder implements RoutingInfoFinder {
   private final DebugLogger debugWSAT = DebugLogger.getDebugLogger("DebugWSAT");

   public void setUsageMode(RoutingInfoFinder.UsageMode var1) {
   }

   public int getFinderPriority() {
      return 0;
   }

   public RoutingInfo findRoutingInfo(HeaderList var1) throws Exception {
      String var2 = null;
      this.debug("findRoutingInfo headers:" + var1);
      String var3 = "http://weblogic.wsee.wstx.wsat/ws/2008/10/wsat";
      String var4 = "wls-wsat";
      String var5 = "routing";
      QName var6 = new QName(var3, var5, var4);
      Iterator var7 = var1.getHeaders(var6, true);
      this.debug("findRoutingInfo iteraror:" + var7);

      while(var7.hasNext()) {
         var2 = ((Header)var7.next()).getStringContent();
      }

      this.debug("findRoutingInfo serverName:" + var2);
      return var2 == null ? null : new RoutingInfo(var2, RoutingInfo.Type.SERVER_NAME);
   }

   public RoutingInfo findRoutingInfoFromSoapBody(RoutingInfo var1, Packet var2) throws Exception {
      this.debug("findRoutingInfoFromSoapBody returning null");
      return null;
   }

   private void debug(String var1) {
      this.debugWSAT.debug("[WSATRoutingInfoFinder]:" + var1);
   }
}
