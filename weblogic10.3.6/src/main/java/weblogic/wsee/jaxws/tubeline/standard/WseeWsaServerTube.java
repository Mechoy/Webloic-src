package weblogic.wsee.jaxws.tubeline.standard;

import com.sun.istack.NotNull;
import com.sun.xml.ws.addressing.W3CWsaServerTube;
import com.sun.xml.ws.addressing.WsaServerTube;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.server.WSEndpoint;
import weblogic.wsee.jaxws.cluster.ClusterTubelineDeploymentListener;

public class WseeWsaServerTube extends W3CWsaServerTube {
   public WseeWsaServerTube(WSEndpoint var1, @NotNull WSDLPort var2, WSBinding var3, Tube var4) {
      super(var1, var2, var3, var4);
      this.commonConstructorCode();
   }

   public WseeWsaServerTube(WsaServerTube var1, TubeCloner var2) {
      super(var1, var2);
      this.commonConstructorCode();
   }

   private void commonConstructorCode() {
      this.isEarlyBackchannelCloseAllowed = !ClusterTubelineDeploymentListener.isClusterServer();
   }

   public WseeWsaServerTube copy(TubeCloner var1) {
      return new WseeWsaServerTube(this, var1);
   }

   protected NextAction doFinalProcessing(WSEndpointReference var1, Packet var2) {
      var2 = sharedDoFinalProcessing(var1, var2);
      return super.doFinalProcessing(var1, var2);
   }

   public static Packet sharedDoFinalProcessing(WSEndpointReference var0, Packet var1) {
      WseeWsaPropertyBag var2 = (WseeWsaPropertyBag)WseeWsaPropertyBag.propertySetRetriever.getFromPacket(var1);
      if (var2.getResponsePacket() != null) {
         if (var0 != null && !var0.isAnonymous()) {
            var1 = var2.getResponsePacket();
         } else if (var1.getMessage() == null) {
            var1 = var2.getResponsePacket();
         }
      }

      var2.setResponsePacket((Packet)null);
      return var1;
   }
}
