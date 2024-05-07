package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxySessionRecoverRequest extends ProxyRequest {
   private static final int EXTVERSION = 1;
   private static final int _IS_DO_ROLLBACK = 1;
   private static final int _HAS_SEQUENCENUMBER = 2;
   private boolean doRollback;
   private long sequenceNumber;

   public ProxySessionRecoverRequest(long var1, boolean var3) {
      this.sequenceNumber = var1;
      this.doRollback = var3;
   }

   public final boolean isDoRollback() {
      return this.doRollback;
   }

   public final long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public ProxySessionRecoverRequest() {
   }

   public int getMarshalTypeCode() {
      return 32;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags = new MarshalBitMask(1);
      if (this.doRollback) {
         this.versionFlags.setBit(1);
      }

      if (this.sequenceNumber != 0L) {
         this.versionFlags.setBit(2);
      }

      this.versionFlags.marshal(var1);
      if (this.sequenceNumber != 0L) {
         var1.writeLong(this.sequenceNumber);
      }

   }

   public void unmarshal(MarshalReader var1) {
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      this.doRollback = var2.isSet(1);
      if (var2.isSet(2)) {
         this.sequenceNumber = var1.readLong();
      }

   }
}
