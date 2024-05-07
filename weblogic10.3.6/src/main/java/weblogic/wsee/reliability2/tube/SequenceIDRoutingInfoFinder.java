package weblogic.wsee.reliability2.tube;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import com.sun.xml.ws.api.message.Packet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfo;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfoFinder;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfoFinderRegistry;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.handshake.CloseSequenceMsg;
import weblogic.wsee.reliability.handshake.TerminateSequenceMsg;
import weblogic.wsee.reliability.handshake.WsrmHandshakeMsg;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.headers.WsrmHeaderFactory;

public class SequenceIDRoutingInfoFinder implements RoutingInfoFinder {
   private static final Logger LOGGER = Logger.getLogger(SequenceIDRoutingInfoFinder.class.getName());
   private static boolean _didRegister = false;

   public static void registerIfNeeded() {
      Class var0 = SequenceIDRoutingInfoFinder.class;
      synchronized(SequenceIDRoutingInfoFinder.class) {
         if (!_didRegister) {
            _didRegister = true;
            SequenceIDRoutingInfoFinder var1 = new SequenceIDRoutingInfoFinder();
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
         LOGGER.fine("Searching headers for routing info");
      }

      WsrmConstants.RMVersion[] var3 = WsrmConstants.RMVersion.values();
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         WsrmConstants.RMVersion var6 = var3[var5];
         QName var7 = WsrmHeader.getQName(SequenceHeader.class, var6);
         Header var2 = var1.get(var7, false);
         if (var2 != null) {
            return this.handleSequenceHeader(var2);
         }
      }

      AddressingVersion[] var12 = AddressingVersion.values();
      var4 = var12.length;

      for(var5 = 0; var5 < var4; ++var5) {
         AddressingVersion var13 = var12[var5];
         SOAPVersion[] var14 = SOAPVersion.values();
         int var8 = var14.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            SOAPVersion var10 = var14[var9];
            String var11 = var1.getAction(var13, var10);
            if (var11 != null && WsrmConstants.Action.matchesAnyActionAndRMVersion(var11)) {
               return this.handleRmAction(var11);
            }
         }
      }

      return null;
   }

   public RoutingInfo findRoutingInfoFromSoapBody(RoutingInfo var1, Packet var2) throws Exception {
      Message var3 = var2.getMessage();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Finding RM routing info from SOAP body on action: " + var1.getName());
      }

      WsrmConstants.Action.VersionInfo var4 = WsrmConstants.Action.getVersionInfo(var1.getName());
      String var5 = null;
      switch (var4.action) {
         case TERMINATE_SEQUENCE:
            TerminateSequenceMsg var10 = new TerminateSequenceMsg(var4.rmVersion);

            try {
               this.readSoapBodyIntoHandshakeMessage(var2, var3, var10);
            } catch (SOAPException var9) {
               throw new WsrmException(var9.toString(), var9);
            }

            var5 = var10.getSequenceId();
            break;
         case CLOSE_SEQUENCE:
            CloseSequenceMsg var6 = new CloseSequenceMsg(var4.rmVersion);

            try {
               this.readSoapBodyIntoHandshakeMessage(var2, var3, var6);
            } catch (SOAPException var8) {
               throw new WsrmException(var8.toString(), var8);
            }

            var5 = var6.getSequenceId();
      }

      if (var5 != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Found routable seq ID from SOAP body action: " + var4.action + " seq: " + var5);
         }

         String var11 = WsUtil.getStoreNameFromRoutableUUID(var5);
         if (var11 != null) {
            return new RoutingInfo(var11, RoutingInfo.Type.PHYSICAL_STORE_NAME);
         }
      }

      return null;
   }

   private void readSoapBodyIntoHandshakeMessage(Packet var1, Message var2, WsrmHandshakeMsg var3) throws SOAPException {
      SOAPMessage var4 = var2.readAsSOAPMessage();
      var3.readMsg(var4);
      var2 = Messages.create(var4);
      var1.setMessage(var2);
   }

   private RoutingInfo handleRmAction(String var1) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Found a WS-RM action value: " + var1 + ". Looking for alternate headers/body with routing info");
      }

      WsrmConstants.Action.VersionInfo var3 = WsrmConstants.Action.getVersionInfo(var1);
      RoutingInfo var2;
      switch (var3.action) {
         case TERMINATE_SEQUENCE:
         case CLOSE_SEQUENCE:
            var2 = new RoutingInfo(var1, RoutingInfo.Type.NEED_BODY);
            break;
         default:
            var2 = null;
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("RM finder found " + (var2 != null ? "routing info " + var2 : "no routing info") + " from action message: " + var1);
      }

      return var2;
   }

   private RoutingInfo handleSequenceHeader(Header var1) throws WsrmException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Found sequence header " + var1.getLocalPart() + " for routing info");
      }

      SequenceHeader var2 = (SequenceHeader)WsrmHeaderFactory.getInstance().createWsrmHeaderFromHeader(SequenceHeader.class, var1);
      String var3 = var2.getSequenceId();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Found sequence header with seq " + var3 + ". Looking for routing info");
      }

      String var4 = WsUtil.getStoreNameFromRoutableUUID(var3);
      return var4 != null ? new RoutingInfo(var4, RoutingInfo.Type.PHYSICAL_STORE_NAME) : null;
   }
}
