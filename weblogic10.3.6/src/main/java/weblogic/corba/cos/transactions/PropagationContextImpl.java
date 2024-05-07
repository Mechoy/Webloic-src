package weblogic.corba.cos.transactions;

import javax.transaction.xa.Xid;
import org.omg.CORBA.Any;
import org.omg.CosTransactions.Coordinator;
import org.omg.CosTransactions.PropagationContext;
import org.omg.CosTransactions.PropagationContextHelper;
import org.omg.CosTransactions.Terminator;
import org.omg.CosTransactions.TransIdentity;
import org.omg.CosTransactions.otid_t;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPOutputStream;
import weblogic.iiop.IIOPService;
import weblogic.iiop.ServiceContext;
import weblogic.transaction.Transaction;
import weblogic.transaction.TxHelper;

public class PropagationContextImpl extends ServiceContext {
   private PropagationContext ctx;
   private Xid xid;
   private boolean registered;
   private Throwable registrationError;
   public static PropagationContextImpl NULL_CTX = new PropagationContextImpl();
   private static final byte[] NULL_BRANCH = new byte[]{11, 14, 10, 3};

   private PropagationContextImpl() {
      this(new otid_t(0, 0, new byte[0]));
   }

   private PropagationContextImpl(otid_t var1) {
      super(0);
      this.registered = true;
      TransIdentity var2 = new TransIdentity((Coordinator)null, (Terminator)null, var1);
      this.ctx = new PropagationContext(0, var2, new TransIdentity[0], (Any)null);
   }

   public PropagationContextImpl(Transaction var1) {
      super(0);
      this.registered = true;
      this.xid = var1.getXID();
      if (OTSHelper.isDebugEnabled()) {
         IIOPLogger.logDebugOTS("exporting " + var1.getXID());
      }

      byte[] var2 = var1.getXID().getGlobalTransactionId();
      byte[] var3 = var1.getXID().getBranchQualifier();
      if ((var3 == null || var3.length == 0) && var1.getXID().getFormatId() == 48801) {
         var3 = NULL_BRANCH;
      }

      int var4 = var3 == null ? 0 : var3.length;
      byte[] var5 = new byte[var2.length + var4];
      System.arraycopy(var2, 0, var5, 0, var2.length);
      if (var4 > 0) {
         System.arraycopy(var3, 0, var5, var2.length, var3.length);
      }

      otid_t var6 = new otid_t(var1.getXID().getFormatId(), var4, var5);
      if (IIOPService.txMechanism != 0) {
         TransIdentity var7 = new TransIdentity(new CoordinatorImpl(var1.getXID()), (Terminator)null, var6);
         Integer var8 = (Integer)var1.getProperty("weblogic.transaction.timeoutSeconds");
         this.ctx = new PropagationContext(var8 == null ? 0 : var8, var7, new TransIdentity[0], (Any)null);
      } else {
         this.ctx = new PropagationContext(0, new TransIdentity((Coordinator)null, (Terminator)null, var6), new TransIdentity[0], (Any)null);
      }

   }

   public PropagationContextImpl(PropagationContext var1) {
      super(0);
      this.registered = true;
      this.ctx = var1;
   }

   public final PropagationContext getPropagationContext() {
      return this.ctx;
   }

   public final Xid getXid() {
      return this.xid;
   }

   public final boolean isNull() {
      return this.ctx == null || this.ctx.current.coord == null && this.ctx.current.term == null;
   }

   public final PropagationContextImpl getNullContext() {
      return this.ctx != null && this.ctx.current.otid != null && this.ctx.current.otid.tid.length != 0 ? new PropagationContextImpl(this.ctx.current.otid) : NULL_CTX;
   }

   public final boolean isForeign() {
      return this.ctx == null || this.ctx.current.otid.formatID != 48801;
   }

   public PropagationContextImpl(IIOPInputStream var1) {
      super(0);
      this.registered = true;
      this.readEncapsulatedContext(var1);
   }

   public final synchronized Throwable waitForRegistration() {
      while(!this.registered) {
         try {
            this.wait();
         } catch (InterruptedException var2) {
         }
      }

      return this.registrationError;
   }

   public final synchronized void notifyRegistration() {
      this.registrationError = null;
      this.registered = true;
      this.notify();
   }

   public final synchronized void notifyRegistration(Throwable var1) {
      this.registrationError = var1;
      this.registered = true;
      this.notify();
   }

   public final void requiresRegistration() {
      if (this.registered) {
         synchronized(this) {
            this.registered = false;
         }
      }
   }

   final Throwable getRegistrationError() {
      return this.registrationError;
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      PropagationContextHelper.write(var1, this.ctx);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      this.ctx = PropagationContextHelper.read(var1);
      if (!this.isNull() || !this.isForeign()) {
         int var2 = this.ctx.current.otid.tid.length - this.ctx.current.otid.bqual_length;
         byte[] var3 = new byte[var2];
         System.arraycopy(this.ctx.current.otid.tid, 0, var3, 0, var2);
         if (this.ctx.current.otid.bqual_length > 0) {
            byte[] var4 = new byte[this.ctx.current.otid.bqual_length];
            System.arraycopy(this.ctx.current.otid.tid, var2, var4, 0, this.ctx.current.otid.bqual_length);
            if (var4.length == NULL_BRANCH.length && this.ctx.current.otid.formatID == 48801 && var4[0] == NULL_BRANCH[0] && var4[1] == NULL_BRANCH[1] && var4[2] == NULL_BRANCH[2] && var4[3] == NULL_BRANCH[3]) {
               this.xid = TxHelper.createXid(this.ctx.current.otid.formatID, var3, (byte[])null);
            } else {
               this.xid = TxHelper.createXid(this.ctx.current.otid.formatID, var3, var4);
            }
         } else {
            this.xid = TxHelper.createXid(this.ctx.current.otid.formatID, var3, (byte[])null);
         }

      }
   }

   public String toString() {
      return "PropagationContextImpl: context (" + this.ctx + ")";
   }

   protected static final void p(String var0) {
      System.err.println("<PropagationContextImpl> " + var0);
   }
}
