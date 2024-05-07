package weblogic.cacheprovider.coherence;

public class CoherenceException extends Exception {
   private static final long serialVersionUID = 354496553045662647L;

   public CoherenceException() {
      super("");
   }

   public CoherenceException(String var1) {
      super(var1);
   }

   public CoherenceException(Throwable var1) {
      super(var1);
   }

   public CoherenceException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
