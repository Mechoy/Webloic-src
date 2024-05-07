package weblogic.wtc.wls;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.intf.TCTransactionService;
import com.bea.core.jatmi.intf.TuxedoLoggable;
import java.io.Serializable;
import java.util.Hashtable;
import javax.transaction.SystemException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.transaction.InterposedTransactionManager;
import weblogic.transaction.ServerTransactionManager;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionLogger;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;

public final class WlsTransactionService implements TCTransactionService, Serializable {
   private static TransactionLogger tlg = null;
   private static TransactionManager tm = null;
   private static InterposedTransactionManager itm = null;

   public WlsTransactionService() {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[ WlsTransactionService()");
         if (tlg == null) {
            ntrace.doTrace("tlg == null");
            tm = TxHelper.getTransactionManager();
            tlg = ((ServerTransactionManager)tm).getTransactionLogger();
            itm = TxHelper.getServerInterposedTransactionManager();
         }

         ntrace.doTrace("] WlsTransactionService()/10");
      } else if (tlg == null) {
         tm = TxHelper.getTransactionManager();
         tlg = ((ServerTransactionManager)tm).getTransactionLogger();
         itm = TxHelper.getServerInterposedTransactionManager();
      }

   }

   public void shutdown(int var1) {
   }

   public int getRealTransactionTimeout() {
      Transaction var1 = TxHelper.getTransaction();
      return var1 != null ? (int)(var1.getTimeToLiveMillis() / 1000L) : -1;
   }

   public void registerResource(String var1, XAResource var2) throws SystemException {
      Hashtable var3;
      if (ntrace.isTraceEnabled(4)) {
         ntrace.doTrace("[ /WlsTransactionService/registerResource(name = " + var1 + ", resource = " + var2 + ")");
         if (var1 != null) {
            var3 = new Hashtable();
            var3.put("weblogic.transaction.registration.type", "standard");
            var3.put("weblogic.transaction.registration.settransactiontimeout", "true");
            tm.registerResource(var1, var2, var3);
         }

         ntrace.doTrace("] /WlsTransactionService/registerResource/10");
      } else if (var1 != null) {
         var3 = new Hashtable();
         var3.put("weblogic.transaction.registration.type", "standard");
         var3.put("weblogic.transaction.registration.settransactiontimeout", "true");
         tm.registerResource(var1, var2, var3);
      }

   }

   public void unregisterResource(String var1) {
      if (ntrace.isTraceEnabled(4)) {
         ntrace.doTrace("[ /WlsTransactionService/unregisterResource(name = " + var1 + ")");
         if (var1 != null) {
            try {
               tm.unregisterResource(var1);
            } catch (SystemException var4) {
            }
         }

         ntrace.doTrace("] /WlsTransactionService/unregisterResource/10");
      } else if (var1 != null) {
         try {
            tm.unregisterResource(var1);
         } catch (SystemException var3) {
         }
      }

   }

   public TuxedoLoggable createTuxedoLoggable() {
      return new WlsTuxedoLoggable();
   }

   public TuxedoLoggable createTuxedoLoggable(Xid var1, int var2) {
      return new WlsTuxedoLoggable(var1, var2);
   }

   public XAResource getXAResource() {
      return itm.getXAResource();
   }

   public javax.transaction.Transaction getTransaction() {
      return TxHelper.getTransaction();
   }

   public javax.transaction.Transaction getTransaction(Xid var1) {
      return tm.getTransaction(var1);
   }

   public Xid getXidFromTransaction(javax.transaction.Transaction var1) {
      if (var1 instanceof Transaction) {
         Transaction var2 = (Transaction)var1;
         return var2.getXID();
      } else {
         return null;
      }
   }

   public Xid getXidFromThread() {
      Transaction var1 = TxHelper.getTransaction();
      return var1 != null ? var1.getXID() : null;
   }

   public int getXidFormatId() {
      return 48802;
   }

   public void resumeTransaction(javax.transaction.Transaction var1) throws Exception {
      tm.resume(var1);
   }
}
