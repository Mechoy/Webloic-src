package weblogic.entitlement.rules;

import java.util.Calendar;
import java.util.TimeZone;
import javax.security.auth.Subject;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.PredicateArgument;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.Resource;

public final class TimePredicate extends BasePredicate {
   private static final String VERSION = "1.0";
   private static PredicateArgument[] arguments = new PredicateArgument[]{new TimePredicateArgument("TimePredicateStartTimeArgumentName", "TimePredicateStartTimeArgumentDescription", (TimeOfDay)null), new TimePredicateArgument("TimePredicateEndTimeArgumentName", "TimePredicateEndTimeArgumentDescription", (TimeOfDay)null), new TimeZonePredicateArgument()};
   private TimeOfDay startTime = null;
   private TimeOfDay endTime = null;
   private TimeZone timeZone = null;

   public TimePredicate() {
      super("TimePredicateName", "TimePredicateDescription");
   }

   public void init(String[] var1) throws IllegalPredicateArgumentException {
      if (var1.length == 0) {
         throw new IllegalPredicateArgumentException("At least one argument is expected");
      } else if (var1.length > 3) {
         throw new IllegalPredicateArgumentException("Maximum three arguments are expected");
      } else {
         this.startTime = this.parseTime(var1[0]);
         this.endTime = var1.length > 1 ? this.parseTime(var1[1]) : null;
         this.timeZone = var1.length > 2 ? this.parseTimeZone(var1[2]) : null;
      }
   }

   private TimeOfDay parseTime(String var1) throws IllegalPredicateArgumentException {
      return (TimeOfDay)arguments[0].parseExprValue(var1);
   }

   private TimeZone parseTimeZone(String var1) throws IllegalPredicateArgumentException {
      return (TimeZone)arguments[2].parseExprValue(var1);
   }

   public boolean evaluate(Subject var1, Resource var2, ContextHandler var3) {
      if (this.startTime == null && this.endTime == null) {
         return false;
      } else {
         TimeZone var4 = this.timeZone == null ? TimeZone.getDefault() : this.timeZone;
         Calendar var5 = Calendar.getInstance(var4);
         int var6 = (var5.get(11) * 3600 + var5.get(12) * 60 + var5.get(13)) * 1000;
         int var7 = this.startTime.getTime();
         if (this.endTime == null) {
            return var6 >= var7;
         } else {
            int var8 = this.endTime.getTime();
            return var7 <= var8 ? var6 >= var7 && var6 < var8 : var6 >= var7 || var6 < var8;
         }
      }
   }

   public String getVersion() {
      return "1.0";
   }

   public int getArgumentCount() {
      return arguments.length;
   }

   public PredicateArgument getArgument(int var1) {
      return arguments[var1];
   }
}
