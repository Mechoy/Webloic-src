package weblogic.corba.idl.poa;

import org.omg.Messaging.RequestEndTimePolicy;
import org.omg.TimeBase.UtcT;
import org.omg.TimeBase.UtcTHelper;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPOutputStream;

public class RequestEndTimePolicyImpl extends PolicyImpl implements RequestEndTimePolicy {
   private UtcT endTime;
   public static final long UNITS_PER_MILLI = 10000L;
   private static final long UTCT_DELTA_MILLIS = 12219292800000L;

   public static final long java2Utc(long var0) {
      return (var0 + 12219292800000L) * 10000L;
   }

   public static final long utc2Java(long var0) {
      return var0 / 10000L - 12219292800000L;
   }

   public RequestEndTimePolicyImpl(long var1) {
      super(28, 0);
      this.endTime = new UtcT(java2Utc(var1), 0, (short)0, (short)0);
   }

   public RequestEndTimePolicyImpl(UtcT var1) {
      super(28, 0);
      this.endTime = new UtcT(var1.time, var1.inacclo, var1.inacchi, var1.tdf);
   }

   public RequestEndTimePolicyImpl(IIOPInputStream var1) {
      super(28, 0);
      this.read(var1);
   }

   public UtcT end_time() {
      return this.endTime;
   }

   public long endTime() {
      return utc2Java(this.endTime.time);
   }

   public long relativeTimeoutMillis() {
      long var1 = utc2Java(this.endTime.time) - System.currentTimeMillis();
      return var1 < 0L ? 0L : var1;
   }

   protected void readEncapsulatedPolicy(IIOPInputStream var1) {
      this.endTime = UtcTHelper.read(var1);
   }

   protected void writeEncapsulatedPolicy(IIOPOutputStream var1) {
      UtcTHelper.write(var1, this.endTime);
   }
}
