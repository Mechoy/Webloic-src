package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.server.EndpointAwareTube;
import weblogic.wsee.jaxws.tubeline.TubeFactory;

public class EndpointAwareLateInitTube extends ServerLateInitTube implements EndpointAwareTube {
   private WSBinding binding;
   private boolean isSetEndpointCalled = false;

   public EndpointAwareLateInitTube(WSBinding var1, TubeFactory var2) {
      super((Tube)null, (ServerTubeAssemblerContext)null, var2);
      this.binding = var1;
   }

   public EndpointAwareLateInitTube(ServerLateInitTube var1, TubeCloner var2) {
      super(var1, var2);
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      EndpointAwareLateInitTube var2 = new EndpointAwareLateInitTube(this, var1);
      this.initializeCopy(var2);
      return var2;
   }

   public void setEndpoint(WSEndpoint var1) {
      if (this.isInitOccurred() && !this.isSetEndpointCalled) {
         ((EndpointAwareTube)this.next).setEndpoint(var1);
         this.isSetEndpointCalled = true;
      }

   }

   protected WSBinding getBinding() {
      return this.binding;
   }

   public void postCreateEndpoint(WSEndpoint var1) {
      super.postCreateEndpoint(var1);
      if (!this.isSetEndpointCalled) {
         ((EndpointAwareTube)this.next).setEndpoint(var1);
         this.isSetEndpointCalled = true;
      }

   }
}
