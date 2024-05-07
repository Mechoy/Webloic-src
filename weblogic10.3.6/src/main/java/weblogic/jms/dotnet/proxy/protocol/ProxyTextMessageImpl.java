package weblogic.jms.dotnet.proxy.protocol;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import weblogic.jms.common.PayloadFactoryImpl;
import weblogic.jms.common.PayloadText;
import weblogic.jms.common.TextMessageImpl;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyTextMessageImpl extends ProxyMessageImpl {
   private static final int EXTVERSION = 1;
   private static final int _HAS_STRING_DATA = 1;
   private String text;
   private PayloadText payload;
   private static final boolean mydebug = false;

   public ProxyTextMessageImpl() {
   }

   public ProxyTextMessageImpl(TextMessage var1) throws JMSException {
      super(var1);
      Object var2 = ((TextMessageImpl)var1).getMessageBody();
      if (var2 instanceof PayloadText) {
         this.payload = (PayloadText)var2;
      } else {
         this.text = (String)var2;
         if (this.text == null) {
            this.text = var1.getText();
         }
      }

   }

   public byte getType() {
      return 6;
   }

   public ProxyTextMessageImpl(String var1) {
      this.text = var1;
   }

   public void setText(String var1) throws JMSException {
      this.text = var1;
   }

   public String getText() throws JMSException {
      if (this.text != null) {
         return this.text;
      } else if (this.payload != null) {
         try {
            this.text = this.payload.readUTF8();
            this.payload = null;
            return this.text;
         } catch (IOException var2) {
            throw new weblogic.jms.common.JMSException(var2);
         }
      } else {
         return this.text;
      }
   }

   public String toString() {
      return "TextMessage[" + this.getMessageID() + ", " + (this.text == null ? "null" : (this.text.length() < 40 ? this.text : this.text.substring(0, 30) + "...")) + "]";
   }

   public void populateJMSMessage(TextMessage var1) throws JMSException {
      super.populateJMSMessage(var1);
      if (this.payload != null) {
         ((TextMessageImpl)var1).setUTF8Buffer(this.payload);
      }

   }

   public int getMarshalTypeCode() {
      return 35;
   }

   public void marshal(MarshalWriter var1) {
      super.marshal(var1);
      MarshalBitMask var2 = new MarshalBitMask(1);
      if (this.text != null || this.payload != null) {
         var2.setBit(1);
      }

      var2.marshal(var1);
      if (this.text != null) {
         var1.writeString(this.text);
      } else if (this.payload != null) {
         try {
            this.payload.writeLengthAndData(var1.getDataOutputStream());
         } catch (IOException var4) {
            throw new RuntimeException(var4);
         }
      }

   }

   public void unmarshal(MarshalReader var1) {
      super.unmarshal(var1);
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      if (var2.isSet(1)) {
         try {
            DataInput var3 = var1.getDataInputStream();
            this.payload = (PayloadText)PayloadFactoryImpl.createPayload((InputStream)var3);
         } catch (IOException var4) {
            throw new RuntimeException(var4);
         }
      }

   }
}
