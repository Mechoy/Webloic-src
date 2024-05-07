package weblogic.rmi.cluster;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import javax.transaction.Transaction;
import weblogic.jndi.Environment;
import weblogic.jndi.internal.ThreadEnvironment;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.BasicRemoteRef;
import weblogic.rmi.internal.Enrollable;
import weblogic.rmi.internal.dgc.DGCClientHelper;
import weblogic.rmi.spi.HostID;
import weblogic.rmi.spi.InboundResponse;
import weblogic.rmi.spi.OutboundRequest;
import weblogic.rmi.spi.RMIRuntime;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionManager;

public final class EntityRemoteRef extends BasicRemoteRef {
   private static final long serialVersionUID = -3542562121173822208L;
   private static final boolean DEBUG = false;
   private ReplicaHandler replicaHandler;
   private String jndiName;
   private Object pk;
   private Environment environment;
   private Enrollable enrollable;

   protected void finalize() throws Throwable {
      if (this.enrollable != null) {
         this.enrollable.unenroll();
      }

   }

   public EntityRemoteRef(int var1, HostID var2, String var3, Object var4) {
      super(var1, var2);
      this.jndiName = var3;
      this.pk = var4;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof EntityRemoteRef) {
         EntityRemoteRef var2 = (EntityRemoteRef)var1;
         return !var2.pk.equals(this.pk) ? false : var2.jndiName.equals(this.jndiName);
      } else {
         return false;
      }
   }

   public String toString() {
      return super.toString() + " - hostID: '" + this.hostID + "', oid: '" + this.oid + "'" + " PK " + this.pk;
   }

   public final Object invoke(Remote var1, RuntimeMethodDescriptor var2, Object[] var3, Method var4) throws Throwable {
      if (this.environment != null) {
         ThreadEnvironment.push(this.environment);
      }

      try {
         RetryHandler var5 = new RetryHandler();
         int var6 = 0;

         while(true) {
            try {
               var5.setRetryCount(var6);
               Object var7 = this.privateInvoke(var2, var3, var4);
               return var7;
            } catch (RemoteException var12) {
               RemoteReference var8 = this.replicaHandler.failOver((RemoteReference)null, var2, var4, var3, var12, var5);
               this.oid = var8.getObjectID();
               this.hostID = var8.getHostID();
               ++var6;
            }
         }
      } finally {
         if (this.environment != null) {
            ThreadEnvironment.pop();
         }

      }
   }

   private Object privateInvoke(RuntimeMethodDescriptor var1, Object[] var2, Method var3) throws Throwable {
      Transaction var4 = null;
      TransactionManager var5 = (TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager();
      if (!var1.isTransactional()) {
         var4 = var5.forceSuspend();
      }

      OutboundRequest var6 = this.getOutboundRequest(var1);
      InboundResponse var7 = null;

      Object var8;
      try {
         var6.marshalArgs(var2);
         var7 = var6.sendReceive();
         var8 = var7.unmarshalReturn();
      } finally {
         try {
            if (var7 != null) {
               var7.close();
            }
         } catch (IOException var15) {
            throw new UnmarshalException("failed to close response stream", var15);
         }

         if (!var1.isTransactional()) {
            var5.forceResume(var4);
         }

      }

      return var8;
   }

   public EntityRemoteRef() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeObject(this.jndiName);
      var1.writeObject(this.pk);
      if (this.enrollable != null) {
         this.enrollable.renewLease();
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.jndiName = (String)var1.readObject();
      this.pk = var1.readObject();
      HostID var2 = this.getHostID();
      this.environment = new Environment();
      if (!var2.isLocal()) {
         this.environment.setProviderUrl(RMIRuntime.findEndPoint(var2).getClusterURL(var1));
      }

      this.replicaHandler = new EntityBeanReplicaHandler(this.pk, this.jndiName, this.environment);
      this.enrollable = DGCClientHelper.findAndEnroll(this);
   }
}
