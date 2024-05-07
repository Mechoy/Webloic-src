package weblogic.jms.dotnet.proxy.protocol;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.extensions.WLMessageProducer;

public class ProxyProducerCreateResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private static final int _HAS_EXTENSIONS = 1;
   private static final int _HAS_DEFAULT_UOO = 2;
   private static final int _HAS_COMPRESSION_THRESHOLD = 3;
   private long id;
   private int deliveryMode;
   private int priority;
   private long timeToLive;
   private long timeToDeliver;
   private long sendTimeOut;
   private String unitOfOrder;
   private long compressionThreshold;
   private boolean hasExtensions;

   public ProxyProducerCreateResponse(long var1, MessageProducer var3) throws JMSException {
      this.id = var1;
      this.deliveryMode = var3.getDeliveryMode();
      this.priority = var3.getPriority();
      this.timeToLive = var3.getTimeToLive();
      if (var3 instanceof WLMessageProducer) {
         this.hasExtensions = true;
         this.timeToDeliver = ((WLMessageProducer)var3).getTimeToDeliver();
         this.sendTimeOut = ((WLMessageProducer)var3).getSendTimeout();
         this.unitOfOrder = ((WLMessageProducer)var3).getUnitOfOrder();
         this.compressionThreshold = (long)((WLMessageProducer)var3).getCompressionThreshold();
      }

   }

   public long getProducerId() {
      return this.id;
   }

   public int getDeliveryMode() {
      return this.deliveryMode;
   }

   public int getPriority() {
      return this.priority;
   }

   public long getTimeToLive() {
      return this.timeToLive;
   }

   public long getTimeToDeliver() {
      return this.timeToDeliver;
   }

   public long getSendTimeOut() {
      return this.sendTimeOut;
   }

   public String getUnitOfOrder() {
      return this.unitOfOrder;
   }

   public long getCompressionThreshold() {
      return this.compressionThreshold;
   }

   public ProxyProducerCreateResponse() {
   }

   public int getMarshalTypeCode() {
      return 24;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.hasExtensions) {
         this.versionFlags.setBit(1);
      }

      if (this.unitOfOrder != null) {
         this.versionFlags.setBit(2);
      }

      if (this.compressionThreshold != 2147483647L) {
         this.versionFlags.setBit(3);
      }

      this.versionFlags.marshal(var1);
      var1.writeLong(this.id);
      var1.writeInt(this.deliveryMode);
      var1.writeInt(this.priority);
      var1.writeLong(this.timeToLive);
      if (this.hasExtensions) {
         var1.writeLong(this.timeToDeliver);
         var1.writeLong(this.sendTimeOut);
         if (this.unitOfOrder != null) {
            var1.writeString(this.unitOfOrder);
         }

         if (this.compressionThreshold != 2147483647L) {
            var1.writeLong(this.compressionThreshold);
         }
      }

   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      this.id = var1.readLong();
      this.deliveryMode = var1.readInt();
      this.priority = var1.readInt();
      this.timeToLive = var1.readLong();
      if (this.versionFlags.isSet(1)) {
         this.timeToDeliver = var1.readLong();
         this.sendTimeOut = var1.readLong();
         if (this.versionFlags.isSet(2)) {
            this.unitOfOrder = var1.readString();
         }

         if (this.versionFlags.isSet(3)) {
            this.compressionThreshold = var1.readLong();
         }
      }

   }
}
