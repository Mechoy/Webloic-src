package weblogic.messaging.dispatcher;

public class DispatcherException extends Exception {
   static final long serialVersionUID = -2703860484874964564L;

   public DispatcherException(String var1) {
      super(var1);
   }

   public DispatcherException(Throwable var1) {
      super(var1);
   }

   public DispatcherException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
