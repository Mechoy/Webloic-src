package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.internal.ConsumerProxy;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.messaging.dispatcher.CompletionListener;

public final class ProxyConsumerReceiveRequest extends ProxyRequest implements CompletionListener {
   private static final int EXTVERSION = 1;
   private static final int _HAS_TIMEOUT = 1;
   private static final int _IS_TIMEOUT_NEVER = 2;
   private static final int _IS_TIMEOUT_NO_WAIT = 3;
   private long timeout;
   private transient ConsumerProxy consumerProxy;
   private transient ReceivedTwoWay receivedTwoWay;

   public ProxyConsumerReceiveRequest(long var1) {
      this.timeout = var1;
   }

   public ProxyConsumerReceiveRequest() {
   }

   public long getTimeout() {
      return this.timeout;
   }

   public ConsumerProxy getConsumerProxy() {
      return this.consumerProxy;
   }

   public void setConsumerProxy(ConsumerProxy var1) {
      this.consumerProxy = var1;
   }

   public ReceivedTwoWay getReceivedTwoWay() {
      return this.receivedTwoWay;
   }

   public void setReceivedTwoWay(ReceivedTwoWay var1) {
      this.receivedTwoWay = var1;
   }

   public void onCompletion(Object var1) {
      this.consumerProxy.receiveCompletion(var1, this.receivedTwoWay);
   }

   public void onException(Throwable var1) {
      this.consumerProxy.receiveException(var1, this.receivedTwoWay);
   }

   public int getMarshalTypeCode() {
      return 16;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.timeout == 0L) {
         this.versionFlags.setBit(3);
      } else if (this.timeout == -1L) {
         this.versionFlags.setBit(2);
      } else {
         this.versionFlags.setBit(1);
      }

      this.versionFlags.marshal(var1);
      if (this.versionFlags.isSet(1)) {
         var1.writeLong(this.timeout);
      }

   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      if (this.versionFlags.isSet(1)) {
         this.timeout = var1.readLong();
      }

      if (this.versionFlags.isSet(2)) {
         this.timeout = -1L;
      }

      if (this.versionFlags.isSet(3)) {
         this.timeout = 0L;
      }

   }
}
