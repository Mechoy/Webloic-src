package weblogic.time.common;

import weblogic.common.ParamSet;
import weblogic.common.ParamSetException;
import weblogic.common.T3ServicesDef;

/** @deprecated */
public interface TriggerDef extends Triggerable {
   /** @deprecated */
   void setServices(T3ServicesDef var1);

   /** @deprecated */
   void triggerInit(ParamSet var1) throws ParamSetException;
}
