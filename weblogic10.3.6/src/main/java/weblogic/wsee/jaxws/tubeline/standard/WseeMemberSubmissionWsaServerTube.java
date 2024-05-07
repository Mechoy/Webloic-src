package weblogic.wsee.jaxws.tubeline.standard;

import com.sun.istack.NotNull;
import com.sun.xml.ws.addressing.v200408.MemberSubmissionWsaServerTube;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.server.WSEndpoint;
import weblogic.wsee.jaxws.cluster.ClusterTubelineDeploymentListener;

public class WseeMemberSubmissionWsaServerTube extends MemberSubmissionWsaServerTube {
   public WseeMemberSubmissionWsaServerTube(WSEndpoint var1, @NotNull WSDLPort var2, WSBinding var3, Tube var4) {
      super(var1, var2, var3, var4);
      this.commonConstructorCode();
   }

   public WseeMemberSubmissionWsaServerTube(MemberSubmissionWsaServerTube var1, TubeCloner var2) {
      super(var1, var2);
      this.commonConstructorCode();
   }

   private void commonConstructorCode() {
      this.isEarlyBackchannelCloseAllowed = !ClusterTubelineDeploymentListener.isClusterServer();
   }

   public WseeMemberSubmissionWsaServerTube copy(TubeCloner var1) {
      return new WseeMemberSubmissionWsaServerTube(this, var1);
   }

   protected NextAction doFinalProcessing(WSEndpointReference var1, Packet var2) {
      var2 = WseeWsaServerTube.sharedDoFinalProcessing(var1, var2);
      return super.doFinalProcessing(var1, var2);
   }
}
