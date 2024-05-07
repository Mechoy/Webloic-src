package weblogic.transaction.internal;

public class StartupClass {
   static int mainCallCount;

   public static void main(String[] var0) {
      ++mainCallCount;
      TransactionRecoveryService.initialize();
   }
}
