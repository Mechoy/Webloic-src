package weblogic.entitlement.rules;

import java.util.Calendar;
import javax.security.auth.Subject;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.Resource;

public final class AfterDayOfMonth extends DayOfMonthPredicate {
   public AfterDayOfMonth() {
      super("AfterDayOfMonthPredicateName", "AfterDayOfMonthPredicateDescription");
   }

   public boolean evaluate(Subject var1, Resource var2, ContextHandler var3) {
      Calendar var4 = this.getCurrentDate();
      return var4.get(5) > this.getDayOfMonth(var4);
   }
}
