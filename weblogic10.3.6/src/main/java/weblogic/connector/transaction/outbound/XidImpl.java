package weblogic.connector.transaction.outbound;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.transaction.xa.Xid;

public class XidImpl implements Xid, Externalizable {
   private static final long serialVersionUID = -7374798779597471467L;
   private Xid tmXid;

   public XidImpl() {
   }

   XidImpl(Xid var1) {
      this.tmXid = var1;
   }

   public boolean equals(Object var1) {
      boolean var2 = this == var1 || var1 != null && var1 instanceof Xid && this.matchingFormatId((Xid)var1) && this.matchingGlobalTransactionId((Xid)var1) && this.matchingBranchQualifier((Xid)var1);
      return var2;
   }

   public int hashCode() {
      byte[] var1 = this.getGlobalTransactionId();
      byte[] var2 = this.getBranchQualifier();
      int var3 = 0;
      int var4;
      if (var1 != null) {
         for(var4 = 0; var4 < var1.length; ++var4) {
            var3 += var1[var4];
         }
      }

      if (var2 != null) {
         for(var4 = 0; var4 < var2.length; ++var4) {
            var3 += var2[var4];
         }
      }

      return var3;
   }

   public byte[] getBranchQualifier() {
      return this.tmXid.getBranchQualifier();
   }

   public int getFormatId() {
      return this.tmXid.getFormatId();
   }

   public byte[] getGlobalTransactionId() {
      return this.tmXid.getGlobalTransactionId();
   }

   public String toString() {
      return this.tmXid.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      ((weblogic.transaction.internal.XidImpl)this.tmXid).writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.tmXid = new weblogic.transaction.internal.XidImpl();
      ((weblogic.transaction.internal.XidImpl)this.tmXid).readExternal(var1);
   }

   private boolean matchingFormatId(Xid var1) {
      return this.getFormatId() == var1.getFormatId();
   }

   private boolean matchingGlobalTransactionId(Xid var1) {
      byte[] var2 = this.getGlobalTransactionId();
      byte[] var3 = var1.getGlobalTransactionId();
      return matchByteArrays(var2, var3);
   }

   private boolean matchingBranchQualifier(Xid var1) {
      byte[] var2 = this.getBranchQualifier();
      byte[] var3 = var1.getBranchQualifier();
      return matchByteArrays(var2, var3);
   }

   private static boolean matchByteArrays(byte[] var0, byte[] var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 != null && var1 != null) {
         if (var0.length != var1.length) {
            return false;
         } else {
            for(int var2 = 0; var2 < var0.length; ++var2) {
               if (var0[var2] != var1[var2]) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }
}
