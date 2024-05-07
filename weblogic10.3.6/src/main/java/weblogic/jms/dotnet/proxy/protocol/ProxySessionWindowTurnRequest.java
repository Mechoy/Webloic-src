package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxySessionWindowTurnRequest extends ProxyRequest {
   private static final int EXTVERSION = 1;
   private static final int _HAS_SEQUENCENUMBER = 2;
   private long sequenceNumber;

   public ProxySessionWindowTurnRequest() {
   }

   public ProxySessionWindowTurnRequest(long var1) {
      this.versionFlags = new MarshalBitMask(1);
      this.sequenceNumber = var1;
      if (var1 != 0L) {
         this.versionFlags.setBit(2);
      }

   }

   public long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public int getMarshalTypeCode() {
      return 49;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags.marshal(var1);
      if (this.sequenceNumber != 0L) {
         var1.writeLong(this.sequenceNumber);
      }

   }

   public void unmarshal(MarshalReader var1) {
      this.versionFlags = new MarshalBitMask();
      this.versionFlags.unmarshal(var1);
      ProxyUtil.checkVersion(this.versionFlags.getVersion(), 1, 1);
      if (this.versionFlags.isSet(2)) {
         this.sequenceNumber = var1.readLong();
      }

   }
}
