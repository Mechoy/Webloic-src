package weblogic.wsee.jaxws.security;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Packet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.NodeList;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfo;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfoFinder;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfoFinderRegistry;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.xml.crypto.wss.WSSConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class SCTIDRoutingInfoFinder implements RoutingInfoFinder {
   private static final Logger LOGGER = Logger.getLogger(SCTIDRoutingInfoFinder.class.getName());
   private static boolean _didRegister = false;

   public static void registerIfNeeded() {
      Class var0 = SCTIDRoutingInfoFinder.class;
      synchronized(SCTIDRoutingInfoFinder.class) {
         if (!_didRegister) {
            _didRegister = true;
            SCTIDRoutingInfoFinder var1 = new SCTIDRoutingInfoFinder();
            RoutingInfoFinderRegistry.getInstance().addFinder(var1);
         }

      }
   }

   public void setUsageMode(RoutingInfoFinder.UsageMode var1) {
   }

   public int getFinderPriority() {
      return 100;
   }

   public RoutingInfo findRoutingInfo(HeaderList var1) throws Exception {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Searching headers for SCT routing info");
      }

      Header var2 = var1.get(WSSConstants.SECURITY_QNAME, false);
      if (var2 == null) {
         return null;
      } else {
         String var3;
         try {
            MessageFactory var4 = WLMessageFactory.getInstance().getMessageFactory(false);
            SOAPMessage var5 = var4.createMessage();
            var2.writeTo(var5);
            SOAPHeader var6 = var5.getSOAPHeader();
            NodeList var7 = var6.getElementsByTagNameNS("http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512", "Identifier");
            if (var7.getLength() <= 0) {
               return null;
            }

            var3 = DOMUtils.getTextData(var7.item(0));
         } catch (SOAPException var8) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Exception obtaining SCT id" + var8);
            }

            throw var8;
         } catch (DOMProcessingException var9) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Exception obtaining SCT id" + var9);
            }

            throw var9;
         }

         if (var3 != null) {
            String var10 = WsUtil.getStoreNameFromRoutableUUID(var3);
            if (var10 != null) {
               return new RoutingInfo(var10, RoutingInfo.Type.PHYSICAL_STORE_NAME);
            }
         }

         return null;
      }
   }

   public RoutingInfo findRoutingInfoFromSoapBody(RoutingInfo var1, Packet var2) throws Exception {
      throw new IllegalArgumentException("Not implemented");
   }
}
