package weblogic.wsee.wstx.wsat.common;

public interface CoordinatorIF<T> {
   void preparedOperation(T var1);

   void abortedOperation(T var1);

   void readOnlyOperation(T var1);

   void committedOperation(T var1);

   void replayOperation(T var1);
}
