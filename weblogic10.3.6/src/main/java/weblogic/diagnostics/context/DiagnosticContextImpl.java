package weblogic.diagnostics.context;

import java.io.IOException;
import java.rmi.dgc.VMID;
import weblogic.workarea.WorkContextInput;
import weblogic.workarea.WorkContextOutput;

public final class DiagnosticContextImpl implements DiagnosticContext {
   private static final char[] int2hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   private static final int SEQID_LENGTH = 16;
   private static long seqID = 0L;
   private static final char[] baseID = createBaseID();
   private String contextId;
   private long dyeVector;
   private String payload;

   public DiagnosticContextImpl() {
      this.init();
   }

   void setContextId(String var1) {
      this.contextId = var1;
   }

   public void init() {
      long var1 = 0L;
      synchronized(baseID) {
         var1 = ++seqID;
      }

      int var3 = baseID.length;
      int var4 = var3 + 16;
      char[] var5 = new char[var4];
      System.arraycopy(baseID, 0, var5, 0, var3);

      for(int var6 = var4 - 1; var6 >= var3; --var6) {
         var5[var6] = int2hex[(int)(var1 & 15L)];
         var1 >>= 4;
      }

      this.contextId = new String(var5);
      this.dyeVector = 0L;
      this.payload = null;
   }

   private static char[] createBaseID() {
      VMID var0 = new VMID();
      String var1 = var0.toString() + "-";
      int var2 = var1.length();
      char[] var3 = new char[var2];
      var1.getChars(0, var2, var3, 0);
      return var3;
   }

   public String getContextId() {
      return this.contextId;
   }

   public void setDye(byte var1, boolean var2) throws InvalidDyeException {
      if (var1 >= 0 && var1 <= 63) {
         long var3 = 1L << var1;
         synchronized(this) {
            if (var2) {
               this.dyeVector |= var3;
            } else {
               this.dyeVector &= ~var3;
            }

         }
      } else {
         throw new InvalidDyeException("Invalid dye index " + var1);
      }
   }

   public boolean isDyedWith(byte var1) throws InvalidDyeException {
      if (var1 >= 0 && var1 <= 63) {
         return (this.dyeVector & 1L << var1) != 0L;
      } else {
         throw new InvalidDyeException("Invalid dye index " + var1);
      }
   }

   public void setDyeVector(long var1) {
      this.dyeVector = var1;
   }

   public long getDyeVector() {
      return this.dyeVector;
   }

   public String getPayload() {
      return this.payload;
   }

   public void setPayload(String var1) {
      this.payload = var1;
   }

   public void writeContext(WorkContextOutput var1) throws IOException {
      var1.writeASCII(this.contextId);
      var1.writeLong(this.dyeVector);
      if (this.payload != null) {
         var1.writeBoolean(true);
         var1.writeASCII(this.payload);
      } else {
         var1.writeBoolean(false);
      }

   }

   public void readContext(WorkContextInput var1) throws IOException {
      this.contextId = var1.readASCII();
      this.dyeVector = var1.readLong();
      if (var1.readBoolean()) {
         this.payload = var1.readASCII();
      } else {
         this.payload = null;
      }

      DiagnosticContextFactory.invalidateCache();
      DiagnosticContextFactory.setJFRThrottled(this);
   }
}
