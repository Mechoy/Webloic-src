package weblogic.time.common;

import weblogic.common.ParamSet;
import weblogic.common.ParamSetException;
import weblogic.common.T3ServicesDef;

/** @deprecated */
public interface ScheduleDef extends Schedulable {
   /** @deprecated */
   void setServices(T3ServicesDef var1);

   /** @deprecated */
   void scheduleInit(ParamSet var1) throws ParamSetException;
}
