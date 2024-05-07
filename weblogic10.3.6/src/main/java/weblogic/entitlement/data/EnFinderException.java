package weblogic.entitlement.data;

public class EnFinderException extends Exception {
   public EnFinderException() {
   }

   public EnFinderException(String var1) {
      super(var1);
   }

   public int getTargetIndex() {
      return -1;
   }
}
