package weblogic.wsee.wstx.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import javax.transaction.xa.Xid;

public class BranchXidImpl implements Xid, Externalizable {
   private Xid delegate;

   public BranchXidImpl() {
   }

   public BranchXidImpl(Xid var1) {
      this.delegate = var1;
   }

   public byte[] getBranchQualifier() {
      return this.delegate.getBranchQualifier();
   }

   public int getFormatId() {
      return this.delegate.getFormatId();
   }

   public byte[] getGlobalTransactionId() {
      return this.delegate.getGlobalTransactionId();
   }

   public Xid getDelegate() {
      return this.delegate;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BranchXidImpl)) {
         return false;
      } else {
         BranchXidImpl var2 = (BranchXidImpl)var1;
         return this.getFormatId() == var2.getFormatId() && Arrays.equals(this.getGlobalTransactionId(), var2.getGlobalTransactionId()) && Arrays.equals(this.getBranchQualifier(), var2.getBranchQualifier());
      }
   }

   public int hashCode() {
      return this.delegate.hashCode();
   }

   public String toString() {
      return "BranchXidImpl:" + this.delegate.toString();
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.delegate = (Xid)var1.readObject();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.delegate);
   }
}
