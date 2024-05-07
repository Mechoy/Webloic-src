package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.client.ServiceCreationInterceptor;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.developer.WSBindingProvider;
import java.lang.ref.WeakReference;
import weblogic.wsee.jaxws.tubeline.TubeFactory;

public class ClientLateInitTube extends AbstractTubeImpl implements ServiceCreationInterceptor {
   private Tube next;
   private ClientTubeAssemblerContext context;
   private TubeFactory callback;

   public ClientLateInitTube(Tube var1, ClientTubeAssemblerContext var2, TubeFactory var3) {
      this.next = var1;
      this.context = var2;
      this.callback = var3;
   }

   protected ClientLateInitTube(ClientLateInitTube var1, TubeCloner var2) {
      super(var1, var2);
      this.next = var1.next != null ? var2.copy(var1.next) : null;
      this.context = var1.context;
      this.callback = var1.callback;
   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      ClientLateInitTube var2 = new ClientLateInitTube(this, var1);
      if (this.callback != null) {
         ServiceCreationInterceptorFeature var3 = (ServiceCreationInterceptorFeature)this.context.getBinding().getFeature(ServiceCreationInterceptorFeature.class);
         if (var3 == null) {
            var3 = new ServiceCreationInterceptorFeature();
            ((WebServiceFeatureList)this.context.getBinding().getFeatures()).add(var3);
         }

         var3.getInterceptors().add(new WeakReference(var2));
      }

      return var2;
   }

   public void postCreateDispatch(WSBindingProvider var1) {
      if (this.callback != null) {
         Tube var2 = this.callback.createClient(this.next, this.context);
         if (var2 != null) {
            this.next = var2;
         }

         this.callback = null;
      }
   }

   public void postCreateProxy(WSBindingProvider var1, Class<?> var2) {
      Tube var3 = this.callback.createClient(this.next, this.context);
      if (var3 != null) {
         this.next = var3;
      }

      this.callback = null;
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
