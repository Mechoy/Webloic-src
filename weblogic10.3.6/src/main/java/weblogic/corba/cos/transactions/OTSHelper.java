package weblogic.corba.cos.transactions;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;
import org.omg.CORBA.Object;
import org.omg.CosTransactions.PropagationContext;
import org.omg.CosTransactions.Status;
import weblogic.corba.j2ee.transaction.Utils;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPReplacer;
import weblogic.iiop.IIOPService;
import weblogic.iiop.IOR;
import weblogic.transaction.InterposedTransactionManager;
import weblogic.transaction.TransactionSystemException;
import weblogic.transaction.TxHelper;
import weblogic.transaction.internal.TransactionImpl;
import weblogic.transaction.internal.XidImpl;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class OTSHelper {
   public static final String OTS_INBOUND_CONTEXT = "weblogic.transaction.ots.inboundContext";
   public static final String OTS_OUTBOUND_CONTEXT = "weblogic.transaction.ots.outboundContext";
   public static final String OTS_MISSING_RESOURCES = "weblogic.transaction.ots.failedResources";
   private static final boolean DEBUG = false;
   private static final DebugCategory debugOTS = Debug.getCategory("weblogic.iiop.ots");
   private static final DebugLogger debugIIOPOTS = DebugLogger.getDebugLogger("DebugIIOPOTS");

   static final boolean isDebugEnabled() {
      return debugOTS.isEnabled() || debugIIOPOTS.isDebugEnabled();
   }

   public static Transaction importTransaction(PropagationContextImpl var0, int var1) throws XAException {
      if ((!var0.isNull() || !var0.isForeign()) && IIOPService.txMechanism != 0) {
         PropagationContext var2 = var0.getPropagationContext();
         Xid var3 = var0.getXid();
         InterposedTransactionManager var4 = TxHelper.getServerInterposedTransactionManager();
         weblogic.transaction.Transaction var5 = null;

         try {
            var5 = (weblogic.transaction.Transaction)var4.getTransaction(var3);
            if (var5 == null) {
               if (isDebugEnabled()) {
                  IIOPLogger.logDebugOTS("importing " + var3);
               }

               ResourceImpl var6 = ResourceImpl.getResource(var3);
               new TransactionRegistrar(var0, var6);
               var4.getXAResource().setTransactionTimeout(var2.timeout);
               var4.getXAResource().start(var3, 0);
               var5 = (weblogic.transaction.Transaction)var4.getTransaction(var3);
               var4.getXAResource().setTransactionTimeout(0);
               var5.setLocalProperty(var1 == 0 ? "weblogic.transaction.ots.inboundContext" : "weblogic.transaction.ots.outboundContext", var0);
               if (isDebugEnabled()) {
                  IIOPLogger.logDebugOTS("imported " + var3 + " -> " + var5.getXid());
               }
            } else {
               if (isDebugEnabled()) {
                  IIOPLogger.logDebugOTS("re-importing " + var3);
               }

               if (var1 == 0 && var5.getProperty("weblogic.transaction.foreignXid") != null) {
                  var4.getXAResource().start(var3, 0);
               }

               if (var5.getLocalProperty(var1 == 0 ? "weblogic.transaction.ots.inboundContext" : "weblogic.transaction.ots.outboundContext") == null) {
                  var5.setLocalProperty(var1 == 0 ? "weblogic.transaction.ots.inboundContext" : "weblogic.transaction.ots.outboundContext", var0);
               }
            }
         } catch (XAException var7) {
            if (var7.errorCode == -8) {
               var4.getXAResource().start(var3, 2097152);
               var5 = (weblogic.transaction.Transaction)var4.getTransaction(var3);
            }

            IIOPLogger.logOTSError("couldn't import transaction", var7);
         }

         return var5;
      } else {
         if (isDebugEnabled()) {
            IIOPLogger.logDebugOTS("received null tx");
         }

         return null;
      }
   }

   public static void forceLocalCoordinator() {
      weblogic.transaction.Transaction var0 = TxHelper.getTransaction();
      if (var0 != null) {
         var0.setLocalProperty("weblogic.transaction.otsTransactionExport", "true");
      }

   }

   public static PropagationContextImpl exportTransaction(weblogic.transaction.internal.PropagationContext var0, int var1) throws Throwable {
      try {
         TransactionImpl var2 = var0.getTransaction();
         return exportTransaction((weblogic.transaction.Transaction)var2, var1);
      } catch (TransactionSystemException var3) {
         throw new XAException(-3);
      }
   }

   static PropagationContextImpl exportTransaction(weblogic.transaction.Transaction var0, int var1) throws Throwable {
      if (IIOPService.txMechanism == 0) {
         return PropagationContextImpl.NULL_CTX;
      } else {
         PropagationContextImpl var2 = (PropagationContextImpl)var0.getLocalProperty(var1 == 0 ? "weblogic.transaction.ots.outboundContext" : "weblogic.transaction.ots.inboundContext");
         if (var2 != null && isDebugEnabled()) {
            IIOPLogger.logDebugOTS("re-exporting " + var0.getXID());
         }

         if (var2 == null) {
            var2 = new PropagationContextImpl(var0);
            var0.setLocalProperty(var1 == 0 ? "weblogic.transaction.ots.outboundContext" : "weblogic.transaction.ots.inboundContext", var2);
            var0.setLocalProperty("weblogic.transaction.otsTransactionExport", "true");
         } else if (var1 == 1 && var0.getProperty("weblogic.transaction.foreignXid") != null) {
            try {
               if (var2.waitForRegistration() != null) {
                  var0.setRollbackOnly(var2.getRegistrationError());
                  throw var2.getRegistrationError();
               }
            } finally {
               if (isDebugEnabled()) {
                  IIOPLogger.logDebugOTS("ending " + var2.getXid());
               }

               TxHelper.getServerInterposedTransactionManager().getXAResource().end(var2.getXid(), 67108864);
            }
         }

         return var2;
      }
   }

   public static final Status jta2otsStatus(int var0) {
      Status var1;
      switch (var0) {
         case 0:
            var1 = Status.StatusActive;
            break;
         case 1:
            var1 = Status.StatusMarkedRollback;
            break;
         case 2:
            var1 = Status.StatusPrepared;
            break;
         case 3:
            var1 = Status.StatusCommitted;
            break;
         case 4:
            var1 = Status.StatusRolledBack;
            break;
         case 5:
         default:
            var1 = Status.StatusUnknown;
            break;
         case 6:
            var1 = Status.StatusNoTransaction;
            break;
         case 7:
            var1 = Status.StatusPreparing;
            break;
         case 8:
            var1 = Status.StatusCommitting;
            break;
         case 9:
            var1 = Status.StatusRollingBack;
      }

      return var1;
   }

   public static final int ots2jtaStatus(Status var0) {
      return Utils.ots2jtaStatus(var0);
   }

   private static final void p(String var0) {
      System.err.println("<OTSHelper> " + var0);
   }

   static final void writeBytes(DataOutput var0, byte[] var1) throws IOException {
      if (var1 == null) {
         var0.writeInt(0);
      } else {
         var0.writeInt(var1.length);
         var0.write(var1);
      }

   }

   static final byte[] readBytes(DataInput var0) throws IOException {
      int var1 = var0.readInt();
      if (var1 == 0) {
         return null;
      } else {
         byte[] var2 = new byte[var1];
         var0.readFully(var2);
         return var2;
      }
   }

   static final void writeXid(DataOutput var0, Xid var1) throws IOException {
      var0.writeInt(var1.getFormatId());
      writeBytes(var0, var1.getGlobalTransactionId());
      writeBytes(var0, var1.getBranchQualifier());
   }

   static final Xid readXid(DataInput var0) throws IOException {
      int var1 = var0.readInt();
      byte[] var2 = readBytes(var0);
      byte[] var3 = readBytes(var0);
      return new XidImpl(var1, var2, var3);
   }

   static final void writeObject(DataOutput var0, Object var1) throws IOException {
      Debug.assertion(var1 != null);
      IOR var2 = (IOR)IIOPReplacer.getReplacer().replaceObject(var1);
      var0.writeUTF(var2.stringify());
   }

   static final Object readObject(DataInput var0) throws IOException {
      IOR var1 = IOR.destringify(var0.readUTF());
      return (Object)IIOPReplacer.getReplacer().resolveObject(var1);
   }
}
