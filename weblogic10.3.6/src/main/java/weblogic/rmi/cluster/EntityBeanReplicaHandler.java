package weblogic.rmi.cluster;

import java.lang.reflect.Method;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.rmi.extensions.RemoteHelper;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.transaction.TransactionHelper;
import weblogic.utils.AssertionError;

final class EntityBeanReplicaHandler implements ReplicaHandler {
   private final Object pk;
   private final String jndiName;
   private int homeListSize = 0;
   private final Environment env;

   EntityBeanReplicaHandler(Object var1, String var2, Environment var3) {
      this.pk = var1;
      this.jndiName = var2;
      this.env = var3;
   }

   protected boolean isRecoverableFailure(RuntimeMethodDescriptor var1, RemoteException var2) {
      return var1.isIdempotent() ? RemoteHelper.isRecoverableFailure(var2) : RemoteHelper.isRecoverablePreInvokeFailure(var2);
   }

   public RemoteReference loadBalance(RemoteReference var1, Method var2, Object[] var3) {
      throw new AssertionError("Should never call loadbalance");
   }

   public final RemoteReference failOver(RemoteReference var1, RuntimeMethodDescriptor var2, Method var3, Object[] var4, RemoteException var5, RetryHandler var6) throws RemoteException {
      int var7 = var6.getRetryCount();
      Object var8 = null;
      if (var7 != 0 && var7 > this.homeListSize) {
         throw var5;
      } else if (this.isRecoverableFailure(var2, var5) && TransactionHelper.getTransactionHelper().getTransaction() == null) {
         Context var9 = null;

         RemoteReference var13;
         try {
            var9 = this.env.getInitialContext();
            Object var10 = var9.lookup(this.jndiName);
            if (var7 == 0) {
               this.homeListSize = this.getListSize(var10);
            }

            Method var11 = var10.getClass().getMethod("findByPrimaryKey", this.pk.getClass());
            StubInfoIntf var12 = (StubInfoIntf)var11.invoke(var10, this.pk);
            var13 = var12.getStubInfo().getRemoteRef();
         } catch (NamingException var23) {
            throw new NoSuchObjectException(var23.toString() + " ClusterAddress (a DNS name) should be set for automatic failover. Check edocs on Configuring a cluster");
         } catch (Exception var24) {
            if (!(var24 instanceof ConnectException) && !(var24 instanceof ConnectIOException) && !(var24 instanceof UnknownHostException) && !(var24 instanceof java.net.ConnectException)) {
               throw var5;
            }

            throw new RemoteException("Couldn't reach " + this.env.getProviderUrl() + ", you should set ClusterAddress (a DNS name) for automatic " + " failover. Check edocs on Configuring a cluster");
         } finally {
            if (var9 != null) {
               try {
                  var9.close();
               } catch (NamingException var22) {
               }
            }

         }

         return var13;
      } else {
         throw var5;
      }
   }

   public ReplicaList getReplicaList() {
      return null;
   }

   public void resetReplicaList(ReplicaList var1) {
   }

   public void resetRefreshedCount() {
   }

   private int getListSize(Object var1) {
      StubInfoIntf var2 = (StubInfoIntf)var1;
      ClusterableRemoteRef var3 = (ClusterableRemoteRef)var2.getStubInfo().getRemoteRef();
      return var3.getReplicaCount();
   }
}
