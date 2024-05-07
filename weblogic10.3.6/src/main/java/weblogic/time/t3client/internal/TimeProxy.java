package weblogic.time.t3client.internal;

import weblogic.common.T3Exception;
import weblogic.common.T3Executable;
import weblogic.t3.srvr.ExecutionContext;
import weblogic.time.common.ScheduledTriggerDef;
import weblogic.time.common.TimeTriggerException;
import weblogic.time.common.internal.TimeEventGenerator;
import weblogic.time.t3client.ScheduledTrigger;

public final class TimeProxy implements T3Executable {
   public void initialize() {
   }

   public void destroy() {
   }

   public Object execute(ExecutionContext var1, Object var2) throws T3Exception {
      TimeMsg var3 = (TimeMsg)var2;
      Object var4 = null;
      ScheduledTriggerDef var6;
      switch (var3.cmd) {
         case 1:
         case 2:
            ScheduledTrigger var11 = var3.sch;

            try {
               ScheduledTriggerDef var7 = var1.getServices().time().getScheduledTrigger(var11.getScheduler(), var11.getTrigger());
               var4 = new Integer(var7.schedule());
               TimeEventGenerator.getOne().register(var4, var7);
               break;
            } catch (TimeTriggerException var10) {
               throw new T3Exception("nested exception:", var10);
            }
         case 3:
            var6 = TimeEventGenerator.getOne().unregister(new Integer(var3.key));

            try {
               if (var6 != null) {
                  var4 = new Boolean(var6.cancel());
               } else {
                  var4 = new Boolean(false);
               }
               break;
            } catch (TimeTriggerException var8) {
               throw new T3Exception("TimeTriggerException: " + var8);
            }
         case 4:
         case 5:
            var6 = TimeEventGenerator.getOne().registered(new Integer(var3.key));

            try {
               if (var6 == null) {
                  throw new TimeTriggerException("Unknown ScheduledTrigger, id=" + var3.key);
               }

               var6.setDaemon(var3.cmd == 4);
               break;
            } catch (TimeTriggerException var9) {
               throw new T3Exception("TimeTriggerException: " + var9);
            }
         case 6:
            var4 = var3;
            break;
         default:
            throw new T3Exception("Unknown TimeMsg Command: " + var3.cmd);
      }

      return var4;
   }
}
