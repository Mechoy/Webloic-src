package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.internal.ProducerProxy;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.messaging.dispatcher.CompletionListener;

public final class ProxyProducerSendRequest extends ProxyRequest implements CompletionListener {
   private static final int EXTVERSION = 1;
   private static final int _HAS_TIMEOUT = 1;
   private static final int _HAS_DESTINATION = 2;
   private ProxyMessageImpl message;
   private ProxyDestinationImpl destination;
   private long sendTimeout = -1L;
   private boolean noResponse;
   private transient ProducerProxy producerProxy;
   private transient ReceivedTwoWay receivedTwoWay;

   public ProxyProducerSendRequest(ProxyMessageImpl var1, ProxyDestinationImpl var2, long var3, int var5, boolean var6) {
      this.message = var1;
      this.destination = var2;
      this.sendTimeout = var3;
      this.noResponse = var6;
   }

   public boolean isNoResponse() {
      return this.noResponse;
   }

   public ProxyMessageImpl getMessage() {
      return this.message;
   }

   public ProxyDestinationImpl getDestination() {
      return this.destination;
   }

   public long getSendTimeout() {
      return this.sendTimeout;
   }

   public void setProducerProxy(ProducerProxy var1) {
      this.producerProxy = var1;
   }

   public ReceivedTwoWay getReceivedTwoWay() {
      return this.receivedTwoWay;
   }

   public void setReceivedTwoWay(ReceivedTwoWay var1) {
      this.receivedTwoWay = var1;
   }

   public void onCompletion(Object var1) {
      this.producerProxy.receiveCompletion(this, var1, this.receivedTwoWay);
   }

   public void onException(Throwable var1) {
      this.producerProxy.receiveException(var1, this.receivedTwoWay);
   }

   public ProxyProducerSendRequest() {
   }

   public int getMarshalTypeCode() {
      return 25;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.sendTimeout != -1L) {
         this.versionFlags.setBit(1);
      }

      if (this.destination != null) {
         this.versionFlags.setBit(2);
      }

      this.versionFlags.marshal(var1);
      var1.writeByte(this.message.getType());
      this.message.marshal(var1);
      if (this.destination != null) {
         this.destination.marshal(var1);
      }

      if (this.sendTimeout != -1L) {
         var1.writeLong(this.sendTimeout);
      }

   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      byte var2 = var1.readByte();
      this.message = ProxyMessageImpl.createMessageImpl(var2);
      this.message.unmarshal(var1);
      if (this.versionFlags.isSet(2)) {
         this.destination = new ProxyDestinationImpl();
         this.destination.unmarshal(var1);
      }

      if (this.versionFlags.isSet(1)) {
         this.sendTimeout = var1.readLong();
      }

   }
}
