package weblogic.entitlement.rules;

import java.util.Date;
import java.util.TimeZone;
import javax.security.auth.Subject;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.PredicateArgument;
import weblogic.security.service.ContextHandler;
import weblogic.security.spi.Resource;

public final class After extends BasePredicate {
   private static final String VERSION = "1.0";
   private static final PredicateArgument DATE_ARG = new DatePredicateArgument(86399999L);
   private static final PredicateArgument TIME_ZONE_ARG = new TimeZonePredicateArgument();
   private static PredicateArgument[] arguments;
   private long dateTime;
   private TimeZone timeZone = null;

   public After() {
      super("AfterPredicateName", "AfterPredicateDescription");
   }

   public void init(String[] var1) throws IllegalPredicateArgumentException {
      if (var1 != null && var1.length >= 1) {
         if (var1.length > 2) {
            throw new IllegalPredicateArgumentException("Maximum two arguments are expected");
         } else {
            this.dateTime = this.parseDate(var1[0]).getTime();
            this.timeZone = var1.length > 1 ? this.parseTimeZone(var1[1]) : null;
         }
      } else {
         throw new IllegalPredicateArgumentException("At least one argument is expected");
      }
   }

   private Date parseDate(String var1) throws IllegalPredicateArgumentException {
      return (Date)DATE_ARG.parseExprValue(var1);
   }

   private TimeZone parseTimeZone(String var1) throws IllegalPredicateArgumentException {
      return (TimeZone)TIME_ZONE_ARG.parseExprValue(var1);
   }

   public boolean evaluate(Subject var1, Resource var2, ContextHandler var3) {
      long var4 = System.currentTimeMillis();
      if (this.timeZone == null) {
         return var4 > this.dateTime;
      } else {
         return var4 + (long)TimeZone.getDefault().getRawOffset() > this.dateTime + (long)this.timeZone.getRawOffset();
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

   static {
      arguments = new PredicateArgument[]{DATE_ARG, TIME_ZONE_ARG};
   }
}
