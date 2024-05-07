package weblogic.management.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import weblogic.utils.StackTraceUtils;

public class ErrorCollectionException extends Exception {
   public static final long serialVersionUID = -6671291145364224060L;
   private List exceptionList;

   public ErrorCollectionException() {
      this("");
   }

   public ErrorCollectionException(String var1) {
      super(var1);
      this.exceptionList = new LinkedList();
   }

   public ErrorCollectionException(Throwable var1) {
      this("");
      this.add(var1);
   }

   public Collection getExceptions() {
      return this.exceptionList;
   }

   public boolean isEmpty() {
      return this.exceptionList.isEmpty();
   }

   public String getBaseMessage() {
      return super.getMessage();
   }

   public String getMessage() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.getMessage());

      String var4;
      for(Iterator var2 = this.getExceptions().iterator(); var2.hasNext(); var1.append("\n\t" + var4)) {
         Throwable var3 = (Throwable)var2.next();
         var4 = var3.getMessage();
         if (var4 == null) {
            var4 = StackTraceUtils.throwable2StackTrace(var3);
         }
      }

      return var1.toString();
   }

   public void add(Throwable var1) {
      this.exceptionList.add(var1);
   }
}
