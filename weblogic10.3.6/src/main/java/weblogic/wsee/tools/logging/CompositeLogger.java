package weblogic.wsee.tools.logging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeLogger implements Logger {
   private List<Logger> loggers = new ArrayList();

   public void addLogger(Logger var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("logger must not be null");
      } else {
         this.loggers.add(var1);
      }
   }

   public void log(EventLevel var1, LogEvent var2) {
      Iterator var3 = this.loggers.iterator();

      while(var3.hasNext()) {
         Logger var4 = (Logger)var3.next();
         var4.log(var1, var2);
      }

   }

   public void log(EventLevel var1, String var2) {
      Iterator var3 = this.loggers.iterator();

      while(var3.hasNext()) {
         Logger var4 = (Logger)var3.next();
         var4.log(var1, var2);
      }

   }
}
