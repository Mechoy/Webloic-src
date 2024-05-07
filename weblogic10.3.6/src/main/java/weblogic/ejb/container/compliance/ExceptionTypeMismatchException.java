package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;

class ExceptionTypeMismatchException extends Exception {
   private final Method method;
   private final Class exception;

   ExceptionTypeMismatchException(Method var1, Class var2) {
      this.method = var1;
      this.exception = var2;
   }

   final Method getMethod() {
      return this.method;
   }

   final Class getException() {
      return this.exception;
   }
}
