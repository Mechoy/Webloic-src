package commonj.work;

import java.util.ArrayList;
import java.util.List;

public class WorkCompletedException extends WorkException {
   List exceptionList;

   public WorkCompletedException() {
   }

   public WorkCompletedException(String var1) {
      super(var1);
   }

   public WorkCompletedException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public WorkCompletedException(Throwable var1) {
      super(var1);
   }

   public WorkCompletedException(String var1, List var2) {
      super(var1);
      this.exceptionList = var2;
   }

   public List getExceptionList() {
      if (this.exceptionList == null && this.getCause() != null) {
         ArrayList var1 = new ArrayList();
         var1.add(this.getCause());
         return var1;
      } else {
         return this.exceptionList;
      }
   }
}
