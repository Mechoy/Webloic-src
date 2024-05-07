package weblogic.jdbc.rowset;

import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.SyncResolver;

public class OptimisticConflictException extends SyncProviderException {
   private static final long serialVersionUID = 7684785426862655886L;

   public OptimisticConflictException() {
   }

   public OptimisticConflictException(String var1) {
      super(var1);
   }

   public OptimisticConflictException(SyncResolver var1) {
      super(var1);
   }

   public OptimisticConflictException(String var1, SyncResolver var2) {
      super(var1);
      this.setSyncResolver(var2);
   }

   public OptimisticConflictException(String var1, String var2) {
      super(var1);
   }

   public OptimisticConflictException(String var1, String var2, int var3) {
      super(var1);
   }
}
