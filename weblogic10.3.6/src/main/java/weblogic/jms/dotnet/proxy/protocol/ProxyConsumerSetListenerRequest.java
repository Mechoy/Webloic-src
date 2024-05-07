package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyConsumerSetListenerRequest extends ProxyRequest {
   private static final int EXTVERSION = 1;
   private static final int _HAS_LISTENER = 1;
   private static final int _HAS_SEQUENCE_NUMBER = 2;
   private boolean hasListener;
   private long sequenceNumber = 0L;
   private long listenerServiceId;

   public ProxyConsumerSetListenerRequest(boolean var1, long var2, long var4) {
      this.hasListener = var1;
      this.sequenceNumber = var4;
      this.listenerServiceId = var2;
   }

   public boolean getHasListener() {
      return this.hasListener;
   }

   public long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public long getListenerServiceId() {
      return this.listenerServiceId;
   }

   public ProxyConsumerSetListenerRequest() {
   }

   public int getMarshalTypeCode() {
      return 18;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.hasListener) {
         this.versionFlags.setBit(1);
      }

      if (this.sequenceNumber != 0L) {
         this.versionFlags.setBit(2);
      }

      this.versionFlags.marshal(var1);
      if (this.hasListener) {
         var1.writeLong(this.listenerServiceId);
      }

      if (this.sequenceNumber != 0L) {
         var1.writeLong(this.sequenceNumber);
      }

   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      if (this.versionFlags.isSet(1)) {
         this.hasListener = true;
         this.listenerServiceId = var1.readLong();
      }

      if (this.versionFlags.isSet(2)) {
         this.sequenceNumber = var1.readLong();
      }

   }
}
