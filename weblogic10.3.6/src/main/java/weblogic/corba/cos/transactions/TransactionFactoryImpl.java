package weblogic.corba.cos.transactions;

import java.rmi.server.ServerNotActiveException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.INVALID_TRANSACTION;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CosTransactions.Control;
import org.omg.CosTransactions.PropagationContext;
import org.omg.CosTransactions.TransactionFactoryHelper;
import org.omg.CosTransactions._TransactionFactoryImplBase;
import weblogic.common.internal.PeerInfo;
import weblogic.iiop.Connection;
import weblogic.iiop.EndPoint;
import weblogic.iiop.IOR;
import weblogic.iiop.ObjectKey;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.internal.InitialReferenceConstants;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionManager;

public final class TransactionFactoryImpl extends _TransactionFactoryImplBase implements InitialReferenceConstants {
   private static TransactionFactoryImpl singleton;
   public static final String TYPE_ID = TransactionFactoryHelper.id();
   private static final IOR ior;

   public static TransactionFactoryImpl getTransactionFactory() {
      return singleton == null ? createSingleton() : singleton;
   }

   private static synchronized TransactionFactoryImpl createSingleton() {
      if (singleton == null) {
         singleton = new TransactionFactoryImpl();
      }

      return singleton;
   }

   private TransactionFactoryImpl() {
   }

   public IOR getIOR() {
      return ior;
   }

   public Control create(int var1) {
      try {
         TransactionManager var2 = (TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager();
         var2.begin("CORBA", var1);
         Transaction var3 = (Transaction)var2.getTransaction();
         var2.suspend();
         EndPoint var4 = (EndPoint)ServerHelper.getClientEndPoint();
         Connection var5 = var4.getConnection();
         if (OTSHelper.isDebugEnabled()) {
            p("creating UserTransaction on: " + var5);
         }

         if ((var4.getPeerInfo() == PeerInfo.FOREIGN || var4.getPeerInfo() == null) && var5.getTxContext() == null) {
            var5.setTxContext(var3);
            return new ControlImpl(var3.getXID(), var5);
         } else {
            return new ControlImpl(var3.getXID(), (Connection)null);
         }
      } catch (NotSupportedException var6) {
         throw new NO_IMPLEMENT(var6.getMessage());
      } catch (ServerNotActiveException var7) {
         throw new BAD_INV_ORDER(var7.getMessage());
      } catch (SystemException var8) {
         throw new INVALID_TRANSACTION(var8.getMessage());
      }
   }

   public Control recreate(PropagationContext var1) {
      throw new NO_IMPLEMENT();
   }

   protected static void p(String var0) {
      System.err.println("<TransactionFactoryImpl> " + var0);
   }

   static {
      ior = new IOR(TYPE_ID, new ObjectKey(TYPE_ID, 17));
   }
}
