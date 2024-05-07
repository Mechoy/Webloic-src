package weblogic.wsee.reliability2.tube;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.client.ServiceCreationInterceptor;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.BindingImpl;
import com.sun.xml.ws.developer.WSBindingProvider;
import java.util.Set;
import javax.xml.ws.Binding;
import weblogic.wsee.jaxws.EndpointCreationInterceptor;
import weblogic.wsee.reliability.WsrmConstants;

public abstract class AbstractWsrmTube extends AbstractFilterTubeImpl implements ServiceCreationInterceptor, EndpointCreationInterceptor {
   protected AbstractWsrmTube(Tube var1) {
      super(var1);
   }

   protected AbstractWsrmTube(AbstractFilterTubeImpl var1, TubeCloner var2) {
      super(var1, var2);
   }

   public void postCreateProxy(@NotNull WSBindingProvider var1, @NotNull Class<?> var2) {
      Binding var3 = var1.getBinding();
      addKnownHeaders(var3);
   }

   public void postCreateDispatch(@NotNull WSBindingProvider var1) {
      Binding var2 = var1.getBinding();
      addKnownHeaders(var2);
   }

   public void postCreateEndpoint(WSEndpoint var1) {
      addKnownHeaders(var1.getBinding());
   }

   static void addKnownHeaders(Binding var0) {
      if (var0 instanceof BindingImpl) {
         BindingImpl var1 = (BindingImpl)var0;
         Set var2 = WsrmConstants.HeaderElement.getHeaderElementQNameSet();
         var1.getKnownHeaders().addAll(var2);
      }

      addKnownWsaHeaders(var0);
   }

   private static void addKnownWsaHeaders(Binding var0) {
      if (var0 instanceof BindingImpl) {
         Set var1 = ((BindingImpl)var0).getKnownHeaders();
         AddressingVersion[] var2 = AddressingVersion.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            AddressingVersion var5 = var2[var4];
            var1.add(var5.actionTag);
            var1.add(var5.faultDetailTag);
            var1.add(var5.faultToTag);
            var1.add(var5.fromTag);
            var1.add(var5.messageIDTag);
            var1.add(var5.relatesToTag);
            var1.add(var5.replyToTag);
            var1.add(var5.toTag);
         }
      }

   }
}
