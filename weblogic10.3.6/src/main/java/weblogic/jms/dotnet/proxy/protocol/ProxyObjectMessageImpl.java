package weblogic.jms.dotnet.proxy.protocol;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import weblogic.jms.common.ObjectMessageImpl;
import weblogic.jms.common.PayloadFactoryImpl;
import weblogic.jms.common.PayloadStream;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyObjectMessageImpl extends ProxyMessageImpl {
   private static final int EXTVERSION = 1;
   private static final int _HAS_DATA = 1;
   private PayloadStream payload;

   public ProxyObjectMessageImpl() {
   }

   public ProxyObjectMessageImpl(PayloadStream var1) {
      this.payload = var1;
   }

   public ProxyObjectMessageImpl(ObjectMessage var1) throws JMSException {
      super(var1);
      this.payload = ((ObjectMessageImpl)var1).getMessageBody();
   }

   public byte getType() {
      return 4;
   }

   public String toString() {
      return "ObjectMessage[" + this.getMessageID() + " bytes = " + this.payload + "]";
   }

   public void populateJMSMessage(ObjectMessage var1) throws JMSException {
      super.populateJMSMessage(var1);
      ((ObjectMessageImpl)var1).setBodyBytes(this.payload);
   }

   public int getMarshalTypeCode() {
      return 39;
   }

   public void marshal(MarshalWriter var1) {
      super.marshal(var1);
      MarshalBitMask var2 = new MarshalBitMask(1);
      if (this.payload != null && this.payload.getLength() != 0) {
         var2.setBit(1);
      }

      var2.marshal(var1);
      if (var2.isSet(1)) {
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
      int var3 = var2.getVersion();
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      if (var2.isSet(1)) {
         try {
            DataInput var4 = var1.getDataInputStream();
            this.payload = (PayloadStream)PayloadFactoryImpl.createPayload((InputStream)var4);
         } catch (IOException var5) {
            throw new RuntimeException(var5);
         }
      }

   }
}
