package weblogic.wsee.monitoring;

public interface OperationStats {
   void reportInvocation(long var1, long var3, long var5);

   void reportOnewayInvocation(long var1, long var3);

   void reportError(Throwable var1);

   void reportResponseError(Throwable var1);
}
