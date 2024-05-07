package weblogic.wsee.monitoring;

public interface HandlerStats {
   void reportInitError(Throwable var1);

   void reportRequestTermination();

   void reportRequestError(Throwable var1);

   void reportResponseTermination();

   void reportResponseError(Throwable var1);
}
