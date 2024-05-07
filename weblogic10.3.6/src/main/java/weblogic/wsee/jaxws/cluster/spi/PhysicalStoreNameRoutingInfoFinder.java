package weblogic.wsee.jaxws.cluster.spi;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Packet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhysicalStoreNameRoutingInfoFinder implements RoutingInfoFinder {
   private static final Logger LOGGER = Logger.getLogger(PhysicalStoreNameRoutingInfoFinder.class.getName());
   public static final int PRIORITY = 10;

   public void setUsageMode(RoutingInfoFinder.UsageMode var1) {
   }

   public int getFinderPriority() {
      return 10;
   }

   public RoutingInfo findRoutingInfo(HeaderList var1) throws Exception {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Searching PhysicalStoreName headers for routing info");
      }

      Header var2 = var1.get(PhysicalStoreNameHeader.QNAME, true);
      if (var2 != null) {
         String var3 = var2.getStringContent();
         if (var3 != null) {
            return new RoutingInfo(var3, RoutingInfo.Type.PHYSICAL_STORE_NAME);
         }
      }

      return null;
   }

   public RoutingInfo findRoutingInfoFromSoapBody(RoutingInfo var1, Packet var2) throws Exception {
      throw new IllegalArgumentException("Not implemented");
   }
}
