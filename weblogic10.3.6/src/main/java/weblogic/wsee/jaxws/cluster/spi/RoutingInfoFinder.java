package weblogic.wsee.jaxws.cluster.spi;

import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Packet;

public interface RoutingInfoFinder {
   void setUsageMode(UsageMode var1);

   int getFinderPriority();

   RoutingInfo findRoutingInfo(HeaderList var1) throws Exception;

   RoutingInfo findRoutingInfoFromSoapBody(RoutingInfo var1, Packet var2) throws Exception;

   public static enum UsageMode {
      FRONT_END_ROUTING,
      IN_PLACE_ROUTING;
   }
}
