package weblogic.javaee;

public enum IsolationLevel {
   READ_UNCOMMITTED(1, "TransactionReadUncommitted"),
   READ_COMMITTED(2, "TransactionReadCommitted"),
   REPEATABLE_READ(4, "TransactionRepeatableRead"),
   SERIALIZABLE(8, "TransactionSerializable");

   private final int _connectionConstant;
   private final String _weblogicIsolationString;

   private IsolationLevel(int var3, String var4) {
      this._connectionConstant = var3;
      this._weblogicIsolationString = var4;
   }

   public int getConnectionConstant() {
      return this._connectionConstant;
   }

   public static IsolationLevel fromConnectionConstant(int var0) {
      switch (var0) {
         case 1:
            return READ_UNCOMMITTED;
         case 2:
            return READ_COMMITTED;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            throw new IllegalArgumentException(Integer.valueOf(var0).toString());
         case 4:
            return REPEATABLE_READ;
         case 8:
            return SERIALIZABLE;
      }
   }

   public String getWeblogicIsolationString() {
      return this._weblogicIsolationString;
   }
}
