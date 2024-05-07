package weblogic.wsee.reliability2.tube;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.OneWayFeature;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.server.WSEndpointImpl;
import java.io.IOException;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service.Mode;
import weblogic.wsee.wsdl.WsdlException;

public class WsrmServerDispatchFactory implements DispatchFactory {
   private WSEndpointImpl _endpoint;

   public WsrmServerDispatchFactory(WSEndpointImpl var1) {
      this._endpoint = var1;
   }

   public WSBinding getBinding() {
      return this._endpoint.getBinding();
   }

   public SOAPVersion getSOAPVersion() {
      return this._endpoint.getBinding().getSOAPVersion();
   }

   @Nullable
   public WSEndpointReference getDefaultReplyTo(@Nullable Packet var1) {
      boolean var2 = HeaderList.isPacketToSslEndpointAddress(var1, this._endpoint.getBinding().getAddressingVersion(), this._endpoint.getBinding().getSOAPVersion());
      OneWayFeature var3 = (OneWayFeature)this._endpoint.getBinding().getFeature(OneWayFeature.class);
      if (var3 != null && var3.getReplyTo(var2) != null) {
         return var3.getReplyTo(var2);
      } else {
         return var1 == null ? null : WsrmTubeUtils.getEndpointReferenceFromIncomingPacket(var1, this._endpoint);
      }
   }

   public <T> Dispatch<T> createDispatch(@NotNull WSEndpointReference var1, Class<T> var2) throws IOException, WsdlException {
      if (!var2.isAssignableFrom(Message.class) && !var2.isAssignableFrom(Packet.class)) {
         throw new IllegalArgumentException("Can only create Dispatch<Message> or Dispatch<Packet>");
      } else {
         Dispatch var3 = this._endpoint.createResponseDispatch(var2, Mode.MESSAGE, var1);
         var3.getRequestContext().put("javax.xml.ws.service.endpoint.address", var1.getAddress());
         var3.getRequestContext().put("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest", var1);
         var3.getRequestContext().put("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest", var1);
         return var3;
      }
   }
}
