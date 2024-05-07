package weblogic.common;

public class ParamSetException extends Exception {
   public ParamSetException(String var1) {
      super("[" + var1 + "]");
   }

   public ParamSetException(String var1, boolean var2) {
      super(var1);
   }
}
