package weblogic.wsee.policy.framework;

public class PolicyException extends Exception {
   public PolicyException(String var1) {
      super(var1);
   }

   public PolicyException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public PolicyException(Throwable var1) {
      super(var1);
   }

   public PolicyException() {
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.getMessage());
      var1.append('\n');
      return var1.toString();
   }
}
