package weblogic.jms.dotnet.proxy.protocol;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import weblogic.jms.common.BytesMessageImpl;
import weblogic.jms.common.PayloadFactoryImpl;
import weblogic.jms.common.PayloadStream;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyBytesMessageImpl extends ProxyMessageImpl {
   private static final int EXTVERSION = 1;
   private static final int _HAS_DATA = 1;
   private PayloadStream payload;

   public ProxyBytesMessageImpl() {
   }

   public ProxyBytesMessageImpl(BytesMessage var1) throws JMSException {
      super(var1);
      this.payload = ((BytesMessageImpl)var1).getPayload();
   }

   public byte getType() {
      return 1;
   }

   public void populateJMSMessage(BytesMessage var1) throws JMSException {
      super.populateJMSMessage(var1);
      ((BytesMessageImpl)var1).setPayload(this.payload);
   }

   public String toString() {
      return "BytesMessage[" + this.getMessageID() + " payload = " + this.payload + "]";
   }

   public int getMarshalTypeCode() {
      return 36;
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
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      if (var2.isSet(1)) {
         try {
            DataInput var3 = var1.getDataInputStream();
            this.payload = (PayloadStream)PayloadFactoryImpl.createPayload((InputStream)var3);
         } catch (IOException var4) {
            throw new RuntimeException(var4);
         }
      }

   }
}
