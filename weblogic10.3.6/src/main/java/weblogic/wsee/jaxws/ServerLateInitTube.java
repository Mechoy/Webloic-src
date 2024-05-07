package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import java.lang.ref.WeakReference;
import weblogic.wsee.jaxws.tubeline.TubeFactory;

public class ServerLateInitTube extends AbstractTubeImpl implements EndpointCreationInterceptor {
   protected Tube next;
   private ServerTubeAssemblerContext context;
   private TubeFactory callback;

   public ServerLateInitTube(Tube var1, ServerTubeAssemblerContext var2, TubeFactory var3) {
      this.next = var1;
      this.context = var2;
      this.callback = var3;
   }

   protected ServerLateInitTube(ServerLateInitTube var1, TubeCloner var2) {
      super(var1, var2);
      this.next = var1.next != null ? var2.copy(var1.next) : null;
      this.context = var1.context;
      this.callback = var1.callback;
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      ServerLateInitTube var2 = new ServerLateInitTube(this, var1);
      this.initializeCopy(var2);
      return var2;
   }

   protected void initializeCopy(ServerLateInitTube var1) {
      if (this.callback != null) {
         EndpointCreationInterceptorFeature var2 = (EndpointCreationInterceptorFeature)this.getBinding().getFeature(EndpointCreationInterceptorFeature.class);
         if (var2 == null) {
            var2 = new EndpointCreationInterceptorFeature();
            ((WebServiceFeatureList)this.getBinding().getFeatures()).add(var2);
         }

         var2.getInterceptors().add(new WeakReference(var1));
      }

   }

   protected WSBinding getBinding() {
      return this.context.getEndpoint().getBinding();
   }

   public void postCreateEndpoint(WSEndpoint var1) {
      if (this.callback != null) {
         Tube var2 = this.callback.createServer(this.next, this.context);
         if (var2 != null) {
            this.next = var2;
         }

         this.callback = null;
      }
   }

   protected boolean isInitOccurred() {
      return this.callback == null;
   }

   public void preDestroy() {
      if (this.next != null) {
         this.next.preDestroy();
      }

   }

   public NextAction processException(Throwable var1) {
      return this.doThrow(var1);
   }

   public NextAction processRequest(Packet var1) {
      return this.doInvoke(this.next, var1);
   }

   public NextAction processResponse(Packet var1) {
      return this.doReturnWith(var1);
   }

   public String toString() {
      return this.callback != null ? super.toString() : this.next.toString();
   }
}
