package weblogic.cluster.singleton;

import java.io.IOException;
import java.util.Set;

public class RemoteLeasingBasisImpl implements RemoteLeasingBasis {
   private LeasingBasis basis;

   public RemoteLeasingBasisImpl(LeasingBasis var1) {
      this.basis = var1;
   }

   public boolean acquire(String var1, String var2, int var3) throws IOException {
      try {
         return this.basis.acquire(var1, var2, var3);
      } catch (LeasingException var6) {
         IOException var5 = new IOException("Error while obtaining lease.");
         var5.initCause(var6);
         throw var5;
      }
   }

   public void release(String var1, String var2) throws IOException {
      this.basis.release(var1, var2);
   }

   public String findOwner(String var1) throws IOException {
      return this.basis.findOwner(var1);
   }

   public String findPreviousOwner(String var1) throws IOException {
      return this.basis.findPreviousOwner(var1);
   }

   public int renewAllLeases(int var1, String var2) throws IOException, MissedHeartbeatException {
      return this.basis.renewAllLeases(var1, var2);
   }

   public int renewLeases(String var1, Set var2, int var3) throws IOException, MissedHeartbeatException {
      return this.basis.renewLeases(var1, var2, var3);
   }

   public String[] findExpiredLeases(int var1) throws IOException {
      return this.basis.findExpiredLeases(var1);
   }
}
