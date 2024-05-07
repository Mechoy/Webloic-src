package weblogic.corba.cos.transactions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;
import javax.rmi.PortableRemoteObject;
import javax.transaction.SystemException;
import javax.transaction.xa.Xid;
import org.omg.CORBA.INVALID_TRANSACTION;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CosTransactions.NotPrepared;
import org.omg.CosTransactions.RecoveryCoordinator;
import org.omg.CosTransactions.Resource;
import org.omg.CosTransactions.Status;
import weblogic.corba.idl.ObjectImpl;
import weblogic.iiop.IIOPLogger;
import weblogic.rmi.extensions.activation.Activatable;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.transaction.Transaction;
import weblogic.transaction.TxHelper;
import weblogic.utils.collections.ConcurrentHashMap;

public final class RecoveryCoordinatorImpl extends ObjectImpl implements RecoveryCoordinator, Activatable {
   private RecoveryActivationID aid;
   private static boolean throwONE = Boolean.getBoolean("weblogic.iiop.recoverycoordinator.ThrowONE");

   public RecoveryCoordinatorImpl(Xid var1, Resource var2) {
      this.aid = new RecoveryActivationID(var1, var2);
   }

   RecoveryCoordinatorImpl(Object var1) {
      this.aid = (RecoveryActivationID)var1;
   }

   public static RecoveryCoordinator getRecoveryCoordinator(Xid var0, Resource var1) {
      return (RecoveryCoordinator)((RecoveryFactory)RecoveryFactory.getActivator()).activate(new RecoveryActivationID(var0, var1));
   }

   void release() {
      ((RecoveryFactory)RecoveryFactory.getActivator()).deactivate(this);
   }

   public Object getActivationID() {
      return this.aid;
   }

   public Activator getActivator() {
      return RecoveryFactory.getActivator();
   }

   public Status replay_completion(Resource var1) throws NotPrepared {
      Transaction var2 = this.getTx();
      if (var2 == null) {
         if (throwONE) {
            throw new OBJECT_NOT_EXIST("no transaction was found");
         } else {
            return Status.StatusRolledBack;
         }
      } else {
         try {
            ConcurrentHashMap var3 = (ConcurrentHashMap)var2.getLocalProperty("weblogic.transaction.ots.resources");
            if (var3 == null || var3.get(var1) == null) {
               LinkedList var4 = (LinkedList)var2.getLocalProperty("weblogic.transaction.ots.failedResources");
               if (var4 == null) {
                  var4 = new LinkedList();
                  var2.setLocalProperty("weblogic.transaction.ots.failedResources", var4);
               }

               var4.add(var1);
            }

            Status var6 = OTSHelper.jta2otsStatus(var2.getStatus());
            switch (var6.value()) {
               case 0:
               case 7:
                  throw new NotPrepared();
               default:
                  return var6;
            }
         } catch (SystemException var5) {
            IIOPLogger.logOTSError("replay_completion() failed unexpectedly", var5);
            throw new INVALID_TRANSACTION(var5.getMessage());
         }
      }
   }

   private final Transaction getTx() {
      return (Transaction)TxHelper.getTransactionManager().getTransaction(this.aid.xid);
   }

   private static class RecoveryActivationID implements Externalizable {
      private static final long serialVersionUID = -3321373552041680942L;
      private Xid xid;
      private Resource resource;

      public RecoveryActivationID() {
      }

      private RecoveryActivationID(Xid var1, Resource var2) {
         this.xid = var1;
         this.resource = var2;
      }

      public int hashCode() {
         return this.resource.hashCode() ^ (this.xid == null ? 1 : this.xid.hashCode());
      }

      public boolean equals(Object var1) {
         try {
            if (var1 == null) {
               return false;
            }

            if (this.xid == null && ((RecoveryActivationID)var1).xid == null || ((RecoveryActivationID)var1).xid.equals(this.xid)) {
               return ((RecoveryActivationID)var1).resource.equals(this.resource);
            }
         } catch (ClassCastException var3) {
         }

         return false;
      }

      public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
         this.xid = OTSHelper.readXid(var1);
         Object var2 = var1.readObject();
         this.resource = (Resource)PortableRemoteObject.narrow(var2, Resource.class);
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         OTSHelper.writeXid(var1, this.xid);
         var1.writeObject(this.resource);
      }

      // $FF: synthetic method
      RecoveryActivationID(Xid var1, Resource var2, Object var3) {
         this(var1, var2);
      }
   }
}
