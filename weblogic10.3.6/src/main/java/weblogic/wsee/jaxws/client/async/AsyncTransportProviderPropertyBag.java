package weblogic.wsee.jaxws.client.async;

import com.sun.xml.ws.api.PropertySet;
import com.sun.xml.ws.api.PropertySet.Property;
import com.sun.xml.ws.api.message.Packet;
import weblogic.wsee.jaxws.framework.PropertySetUtil;
import weblogic.wsee.jaxws.tubeline.standard.WseeWsaPropertyBag;

public class AsyncTransportProviderPropertyBag extends PropertySet {
   public static final String RESPONSE_PACKET = "weblogic.wsee.jaxws.client.async.ResponsePacket";
   public static final String RESPONSE_ENDPOINT = "weblogic.wsee.jaxws.client.async.ResponseEndpoint";
   public static final String REQUEST_CONTEXT_REMOVAL_CALLBACK = "weblogic.wsee.jaxws.client.async.RequestContextRemovalCallback";
   public static PropertySetUtil.PropertySetRetriever<AsyncTransportProviderPropertyBag> propertySetRetriever = PropertySetUtil.getRetriever(AsyncTransportProviderPropertyBag.class);
   private static final PropertySet.PropertyMap model = parse(AsyncTransportProviderPropertyBag.class);
   private Packet _packet;
   private WseeWsaPropertyBag _wseeWsaProps;
   private AsyncResponseEndpoint _responseEndpoint;
   private AsyncTransportProvider.RequestContextRemovalCallback _requestContextRemovalCallback;

   protected PropertySet.PropertyMap getPropertyMap() {
      return model;
   }

   public AsyncTransportProviderPropertyBag(Packet var1) {
      this._packet = var1;
   }

   public Packet getPacket() {
      return this._packet;
   }

   public void setPacket(Packet var1) {
      this._packet = var1;
   }

   @Property({"weblogic.wsee.jaxws.client.async.ResponsePacket"})
   public Packet getResponsePacket() {
      this._wseeWsaProps = (WseeWsaPropertyBag)WseeWsaPropertyBag.propertySetRetriever.getFromPacket(this._packet);
      return this._wseeWsaProps != null ? this._wseeWsaProps.getResponsePacket() : null;
   }

   public void setResponsePacket(Packet var1) {
      this._wseeWsaProps = (WseeWsaPropertyBag)WseeWsaPropertyBag.propertySetRetriever.getFromPacket(this._packet);
      if (this._wseeWsaProps == null) {
         this._wseeWsaProps = new WseeWsaPropertyBag();
         this._packet.addSatellite(this._wseeWsaProps);
      }

      this._wseeWsaProps.setResponsePacket(var1);
   }

   @Property({"weblogic.wsee.jaxws.client.async.ResponseEndpoint"})
   public AsyncResponseEndpoint getResponseEndpoint() {
      return this._responseEndpoint;
   }

   public void setResponseEndpoint(AsyncResponseEndpoint var1) {
      this._responseEndpoint = var1;
   }

   @Property({"weblogic.wsee.jaxws.client.async.RequestContextRemovalCallback"})
   public AsyncTransportProvider.RequestContextRemovalCallback getRequestContextRemovalCallback() {
      return this._requestContextRemovalCallback;
   }

   public void setRequestContextRemovalCallback(AsyncTransportProvider.RequestContextRemovalCallback var1) {
      this._requestContextRemovalCallback = var1;
   }
}
