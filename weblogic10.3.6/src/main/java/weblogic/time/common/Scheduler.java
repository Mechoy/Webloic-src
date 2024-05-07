package weblogic.time.common;

import weblogic.common.ParamSet;
import weblogic.common.ParamSetException;
import weblogic.common.T3Exception;
import weblogic.common.T3ServicesDef;
import weblogic.common.internal.RemoteEntryPoint;

/** @deprecated */
public final class Scheduler extends RemoteEntryPoint {
   private static final long serialVersionUID = -2245180095783249659L;

   public Scheduler() {
   }

   /** @deprecated */
   public Scheduler(Schedulable var1, ParamSet var2) {
      super((Object)var1, var2);
   }

   /** @deprecated */
   public Scheduler(Schedulable var1) {
      super((Object)var1);
   }

   /** @deprecated */
   public Scheduler(String var1, ParamSet var2) {
      super(var1, var2);
   }

   /** @deprecated */
   public Scheduler(String var1) {
      super(var1);
   }

   public void private_initialize(T3ServicesDef var1) throws TimeTriggerException {
      try {
         this.newInstance();
         if (this.theObject() instanceof ScheduleDef) {
            ScheduleDef var2 = (ScheduleDef)this.theObject();
            var2.setServices(var1);
            var2.scheduleInit(this.params());
         }

      } catch (T3Exception var3) {
         throw new TimeTriggerException("Nested Exception: " + var3);
      } catch (ParamSetException var4) {
         throw new TimeTriggerException("Nested Exception: " + var4);
      }
   }
}
