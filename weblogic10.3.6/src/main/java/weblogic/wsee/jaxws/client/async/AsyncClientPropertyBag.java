package weblogic.wsee.jaxws.client.async;

import com.sun.xml.ws.api.PropertySet;
import com.sun.xml.ws.api.PropertySet.Property;
import com.sun.xml.ws.api.message.Packet;
import weblogic.wsee.jaxws.framework.PropertySetUtil;

public class AsyncClientPropertyBag extends PropertySet {
   public static final String REQUEST_MSG_ID = "weblogic.wsee.jaxws.client.async.RequestMsgID";
   public static PropertySetUtil.PropertySetRetriever<AsyncClientPropertyBag> propertySetRetriever = PropertySetUtil.getRetriever(AsyncClientPropertyBag.class);
   private static final PropertySet.PropertyMap model = parse(AsyncClientPropertyBag.class);
   private String _requestMsgId;
   private Packet _packet;

   protected PropertySet.PropertyMap getPropertyMap() {
      return model;
   }

   public AsyncClientPropertyBag(Packet var1) {
      this._packet = var1;
   }

   public Packet getPacket() {
      return this._packet;
   }

   public void setPacket(Packet var1) {
      this._packet = var1;
   }

   @Property({"weblogic.wsee.jaxws.client.async.RequestMsgID"})
   public String getRequestMessageID() {
      return this._requestMsgId;
   }

   public void setRequestMessageID(String var1) {
      this._requestMsgId = var1;
   }
}
