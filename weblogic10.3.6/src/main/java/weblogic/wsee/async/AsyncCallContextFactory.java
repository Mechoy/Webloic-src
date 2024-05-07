package weblogic.wsee.async;

public class AsyncCallContextFactory {
   public static AsyncPreCallContext getAsyncPreCallContext() {
      return new AsyncPreCallContextImpl();
   }

   static AsyncPostCallContext getAsyncPostCallContext() {
      return new AsyncPostCallContextImpl();
   }
}
