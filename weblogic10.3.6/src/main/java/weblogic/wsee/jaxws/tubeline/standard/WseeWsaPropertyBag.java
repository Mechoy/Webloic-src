package weblogic.wsee.jaxws.tubeline.standard;

import com.sun.xml.ws.api.PropertySet;
import com.sun.xml.ws.api.PropertySet.Property;
import com.sun.xml.ws.api.message.Packet;
import weblogic.wsee.jaxws.framework.PropertySetUtil;

public class WseeWsaPropertyBag extends PropertySet {
   public static final String RESPONSE_PACKET = "weblogic.wsee.jaxws.tubeline.standard.WseeWsaResponsePacket";
   public static final String RESPONSE_THROWABLE = "weblogic.wsee.jaxws.tubeline.standard.WseeWsaResponseThrowable";
   public static PropertySetUtil.PropertySetRetriever<WseeWsaPropertyBag> propertySetRetriever = PropertySetUtil.getRetriever(WseeWsaPropertyBag.class);
   private static final PropertySet.PropertyMap model = parse(WseeWsaPropertyBag.class);
   private Packet _responsePacket;
   private Throwable _responseThrowable;

   protected PropertySet.PropertyMap getPropertyMap() {
      return model;
   }

   @Property({"weblogic.wsee.jaxws.tubeline.standard.WseeWsaResponseThrowable"})
   public Throwable getResponseThrowable() {
      return this._responseThrowable;
   }

   public void setResponseThrowable(Throwable var1) {
      this._responseThrowable = var1;
   }

   @Property({"weblogic.wsee.jaxws.tubeline.standard.WseeWsaResponsePacket"})
   public Packet getResponsePacket() {
      return this._responsePacket;
   }

   public void setResponsePacket(Packet var1) {
      this._responsePacket = var1;
   }
}
