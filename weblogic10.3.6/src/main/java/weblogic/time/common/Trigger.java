package weblogic.time.common;

import weblogic.common.ParamSet;
import weblogic.common.ParamSetException;
import weblogic.common.T3Exception;
import weblogic.common.T3ServicesDef;
import weblogic.common.internal.RemoteEntryPoint;

/** @deprecated */
public final class Trigger extends RemoteEntryPoint {
   private static final long serialVersionUID = -1055933006857913665L;

   public Trigger() {
   }

   /** @deprecated */
   public Trigger(Triggerable var1, ParamSet var2) {
      super((Object)var1, var2);
   }

   /** @deprecated */
   public Trigger(Triggerable var1) {
      super((Object)var1);
   }

   /** @deprecated */
   public Trigger(String var1, ParamSet var2) {
      super(var1, var2);
   }

   /** @deprecated */
   public Trigger(String var1) {
      super(var1);
   }

   /** @deprecated */
   public void private_set_instance(Triggerable var1) {
      this.theObject = var1;
   }

   /** @deprecated */
   public void private_initialize(T3ServicesDef var1) throws TimeTriggerException {
      try {
         if (this.newInstance() instanceof TriggerDef) {
            TriggerDef var2 = (TriggerDef)this.theObject();
            var2.setServices(var1);
            var2.triggerInit(this.params());
         }

      } catch (ParamSetException var3) {
         throw new TimeTriggerException("Nested Exception: " + var3);
      } catch (T3Exception var4) {
         throw new TimeTriggerException("Nested Exception: " + var4);
      }
   }

   /** @deprecated */
   public void private_initialize(T3ServicesDef var1, Triggerable var2) throws TimeTriggerException {
      this.theObject = var2;
   }
}
