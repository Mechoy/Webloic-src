package weblogic.corba.idl.poa;

import org.omg.Messaging.ReplyEndTimePolicy;
import org.omg.TimeBase.UtcT;
import org.omg.TimeBase.UtcTHelper;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPOutputStream;

public class ReplyEndTimePolicyImpl extends PolicyImpl implements ReplyEndTimePolicy {
   private UtcT endTime;

   public ReplyEndTimePolicyImpl(long var1) {
      super(30, 0);
      this.endTime = new UtcT(RequestEndTimePolicyImpl.java2Utc(var1), 0, (short)0, (short)0);
   }

   public ReplyEndTimePolicyImpl(UtcT var1) {
      super(30, 0);
      this.endTime = new UtcT(var1.time, var1.inacclo, var1.inacchi, var1.tdf);
   }

   public ReplyEndTimePolicyImpl(IIOPInputStream var1) {
      super(30, 0);
      this.read(var1);
   }

   public UtcT end_time() {
      return this.endTime;
   }

   public long endTime() {
      return RequestEndTimePolicyImpl.utc2Java(this.endTime.time);
   }

   public long relativeTimeoutMillis() {
      long var1 = RequestEndTimePolicyImpl.utc2Java(this.endTime.time) - System.currentTimeMillis();
      return var1 < 0L ? 0L : var1;
   }

   protected void readEncapsulatedPolicy(IIOPInputStream var1) {
      this.endTime = UtcTHelper.read(var1);
   }

   protected void writeEncapsulatedPolicy(IIOPOutputStream var1) {
      UtcTHelper.write(var1, this.endTime);
   }
}
