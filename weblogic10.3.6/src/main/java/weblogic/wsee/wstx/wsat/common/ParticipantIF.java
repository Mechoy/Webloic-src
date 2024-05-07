package weblogic.wsee.wstx.wsat.common;

public interface ParticipantIF<T> {
   void prepare(T var1);

   void commit(T var1);

   void rollback(T var1);
}
