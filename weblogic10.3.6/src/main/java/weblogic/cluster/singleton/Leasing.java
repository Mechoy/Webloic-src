package weblogic.cluster.singleton;

public interface Leasing {
   boolean tryAcquire(String var1) throws LeasingException;

   void acquire(String var1, LeaseObtainedListener var2) throws LeasingException;

   void release(String var1) throws LeasingException;

   String findOwner(String var1) throws LeasingException;

   void addLeaseLostListener(LeaseLostListener var1);

   void removeLeaseLostListener(LeaseLostListener var1);
}
