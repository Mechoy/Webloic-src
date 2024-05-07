package weblogic.wsee.jaxws.cluster.spi;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Packet;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.jaxws.framework.WsUtil;

public class MessageIDRoutingInfoFinder implements RoutingInfoFinder {
   private static final Logger LOGGER = Logger.getLogger(MessageIDRoutingInfoFinder.class.getName());
   public static final int PRIORITY = 0;

   public void setUsageMode(RoutingInfoFinder.UsageMode var1) {
   }

   public int getFinderPriority() {
      return 0;
   }

   public RoutingInfo findRoutingInfo(HeaderList var1) throws Exception {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Searching headers for routing info");
      }

      String var2 = null;
      AddressingVersion[] var3 = AddressingVersion.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         AddressingVersion var6 = var3[var5];
         SOAPVersion[] var7 = SOAPVersion.values();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            SOAPVersion var10 = var7[var9];
            var2 = var1.getRelatesTo(var6, var10);
            if (var2 != null) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Found RelatesTo header with value: " + var2 + ". Looking for routing info.");
               }
               break;
            }
         }

         if (var2 != null) {
            break;
         }
      }

      if (var2 != null) {
         String var11 = WsUtil.getStoreNameFromRoutableUUID(var2);
         if (var11 != null) {
            return new RoutingInfo(var11, RoutingInfo.Type.PHYSICAL_STORE_NAME);
         }
      }

      return null;
   }

   public RoutingInfo findRoutingInfoFromSoapBody(RoutingInfo var1, Packet var2) throws Exception {
      throw new IllegalArgumentException("Not implemented");
   }
}
