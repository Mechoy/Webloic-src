package weblogic.jms.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.VersionInfoFactory;

final class PrimitiveObjectMap {
   private HashMap map;
   PayloadStream payload;
   private static PeerInfo LATEST_PEER_INFO = VersionInfoFactory.getPeerInfo();

   PrimitiveObjectMap() {
   }

   PrimitiveObjectMap(DataInput var1, int var2) throws IOException {
      if (var2 >= 30) {
         this.payload = (PayloadStream)PayloadFactoryImpl.createPayload((InputStream)var1);
      } else {
         this.map = JMSUtilities.readBasicMap(var1);
      }

   }

   PrimitiveObjectMap(PrimitiveObjectMap var1) throws javax.jms.JMSException {
      synchronized(var1) {
         try {
            var1.ensurePayload();
         } catch (IOException var5) {
            throw new JMSException(var5);
         }

         this.payload = var1.payload.copyPayloadWithoutSharedStream();
      }
   }

   public Set entrySet() throws javax.jms.JMSException {
      this.ensureMap();
      return this.map.entrySet();
   }

   public Set keySet() throws javax.jms.JMSException {
      this.ensureMap();
      return this.map.keySet();
   }

   public Object put(Object var1, Object var2) throws javax.jms.JMSException {
      this.ensureMap();
      this.invalidatePayload();
      return this.map.put(var1, var2);
   }

   public Object get(Object var1) throws javax.jms.JMSException {
      this.ensureMap();
      return this.map.get(var1);
   }

   public Object remove(Object var1) throws javax.jms.JMSException {
      this.ensureMap();
      Object var2 = this.map.remove(var1);
      if (var2 != null) {
         this.invalidatePayload();
      }

      return var2;
   }

   public boolean containsKey(Object var1) throws javax.jms.JMSException {
      this.ensureMap();
      return this.map.containsKey(var1);
   }

   public boolean isEmpty() {
      if (this.map != null) {
         return this.map.isEmpty();
      } else {
         return this.payload == null;
      }
   }

   public int getSizeInBytes() throws javax.jms.JMSException {
      try {
         this.ensurePayload();
      } catch (IOException var2) {
         throw new JMSException(var2);
      }

      return this.payload.getLength();
   }

   void writeToStream(DataOutput var1, int var2) throws IOException {
      if (var2 >= 30) {
         synchronized(this) {
            this.ensurePayload();
            this.payload.writeLengthAndData(var1);
         }
      } else {
         this.writeToStream(var1, LATEST_PEER_INFO);
      }

   }

   void writeToStream(DataOutput var1, PeerInfo var2) throws IOException {
      try {
         this.ensureMap();
      } catch (javax.jms.JMSException var5) {
         IOException var4 = new IOException(var5.toString());
         var4.initCause(var5);
         throw var4;
      }

      JMSUtilities.writeBasicMap(var1, this.map, var2);
   }

   private void invalidatePayload() {
      synchronized(this) {
         this.payload = null;
      }
   }

   private synchronized void ensureMap() throws javax.jms.JMSException {
      if (this.map == null) {
         if (this.payload != null) {
            try {
               this.map = JMSUtilities.readBasicMap(this.payload.getInputStream());
            } catch (IOException var2) {
               throw new JMSException(var2);
            }
         } else {
            this.map = new HashMap();
         }
      }

   }

   private synchronized void ensurePayload() throws IOException {
      if (this.payload == null) {
         BufferOutputStream var1 = PayloadFactoryImpl.createOutputStream();
         JMSUtilities.writeBasicMap(var1, this.map, LATEST_PEER_INFO);
         var1.close();
         this.payload = (PayloadStream)var1.moveToPayload();
      }

   }
}
