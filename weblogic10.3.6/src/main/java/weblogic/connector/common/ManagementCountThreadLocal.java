package weblogic.connector.common;

class ManagementCountThreadLocal {
   private static ThreadLocal managementCount = new ThreadLocal() {
      protected synchronized Object initialValue() {
         return new Integer(0);
      }
   };

   static int get() {
      return (Integer)((Integer)managementCount.get());
   }

   static void increment() {
      Integer var0 = new Integer(get() + 1);
      managementCount.set(var0);
   }

   static void decrement() {
      Integer var0 = new Integer(get() - 1);
      if (var0 >= 0) {
         managementCount.set(var0);
      }

   }
}
