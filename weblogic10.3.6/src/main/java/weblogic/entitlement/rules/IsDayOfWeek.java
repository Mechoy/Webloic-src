package weblogic.entitlement.rules;

import java.util.Calendar;
import java.util.TimeZone;
import javax.security.auth.Subject;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.Resource;

public final class IsDayOfWeek extends DayOfWeekPredicate {
   public IsDayOfWeek() {
      super("IsDayOfWeekPredicateName", "IsDayOfWeekPredicateDescription");
   }

   public boolean evaluate(Subject var1, Resource var2, ContextHandler var3) {
      TimeZone var4 = this.getTimeZone();
      if (var4 == null) {
         var4 = TimeZone.getDefault();
      }

      Calendar var5 = Calendar.getInstance(var4);
      return var5.get(7) == this.getDayOfWeek().getCalendarDayId();
   }
}
