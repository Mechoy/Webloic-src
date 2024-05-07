package weblogic.entitlement.rules;

import java.util.Calendar;
import javax.security.auth.Subject;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.Resource;

public final class BeforeDayOfMonth extends DayOfMonthPredicate {
   public BeforeDayOfMonth() {
      super("BeforeDayOfMonthPredicateName", "BeforeDayOfMonthPredicateDescription");
   }

   public boolean evaluate(Subject var1, Resource var2, ContextHandler var3) {
      Calendar var4 = this.getCurrentDate();
      return var4.get(5) < this.getDayOfMonth(var4);
   }
}
