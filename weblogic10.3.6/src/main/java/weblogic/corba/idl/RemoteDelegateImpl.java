package weblogic.corba.idl;

import java.io.IOException;
import java.security.AccessController;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CosTransactions.TransactionalObject;
import weblogic.corba.cos.transactions.OTSHelper;
import weblogic.corba.idl.poa.RelativeRequestTimeoutPolicyImpl;
import weblogic.corba.idl.poa.RelativeRoundtripTimeoutPolicyImpl;
import weblogic.corba.idl.poa.ReplyEndTimePolicyImpl;
import weblogic.corba.idl.poa.RequestEndTimePolicyImpl;
import weblogic.corba.iiop.cluster.Selector;
import weblogic.corba.iiop.cluster.SelectorFactory;
import weblogic.corba.j2ee.naming.ORBHelper;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.ClusterComponent;
import weblogic.iiop.EndPoint;
import weblogic.iiop.EndPointManager;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPRemoteRef;
import weblogic.iiop.IIOPService;
import weblogic.iiop.IOR;
import weblogic.iiop.ReplyMessage;
import weblogic.iiop.RequestMessage;
import weblogic.iiop.Utils;
import weblogic.iiop.VendorInfoCluster;
import weblogic.iiop.spi.MessageStream;
import weblogic.kernel.KernelStatus;
import weblogic.rmi.cluster.Version;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;
import weblogic.transaction.ServerTransactionInterceptor;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionInterceptor;
import weblogic.transaction.TransactionManager;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.StackTraceUtils;

final class RemoteDelegateImpl extends DelegateImpl {
   private static final AbstractSubject kernelId = (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private static final DebugLogger debugIIOPTransport = DebugLogger.getDebugLogger("DebugIIOPTransport");
   private IOR currentIOR;
   private ClusterComponent replicas;
   private Version version;
   private int current = 0;
   private Selector selector;

   public RemoteDelegateImpl(IOR var1) {
      super(var1);
      this.getIOR().getProfile();
   }

   protected RemoteDelegateImpl(RemoteDelegateImpl var1) {
      super((DelegateImpl)var1);
      this.currentIOR = var1.currentIOR;
      this.replicas = var1.replicas;
      this.version = var1.version;
      this.current = var1.current;
      this.selector = var1.selector;
   }

   protected Delegate copy() {
      return new RemoteDelegateImpl(this);
   }

   public boolean is_a(Object var1, String var2) {
      String[] var3 = ((org.omg.CORBA.portable.ObjectImpl)var1)._ids();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].equals(var2)) {
            return true;
         }
      }

      OutputStream var16 = this.request(var1, "_is_a", true);
      boolean var5 = false;
      InputStream var6 = null;

      boolean var8;
      try {
         var16.write_string(var2);
         var6 = this.invoke(var1, var16);
         var5 = var6.read_boolean();
         return var5;
      } catch (ApplicationException var13) {
         return var5;
      } catch (RemarshalException var14) {
         var8 = this.is_a(var1, var2);
      } finally {
         this.releaseReply(var1, var6);
      }

      return var8;
   }

   public boolean non_existent(Object var1) {
      OutputStream var2 = this.request(var1, "_non_existent", true);
      boolean var3 = false;
      InputStream var4 = null;

      boolean var6;
      try {
         var4 = this.invoke(var1, var2);
         var3 = var4.read_boolean();
         return var3;
      } catch (ApplicationException var11) {
         return var3;
      } catch (RemarshalException var12) {
         var6 = this.non_existent(var1);
      } finally {
         this.releaseReply(var1, var4);
      }

      return var6;
   }

   public boolean is_local(Object var1) {
      return false;
   }

   public Object get_interface_def(Object var1) {
      OutputStream var2 = this.request(var1, "_interface", true);
      Object var3 = null;
      InputStream var4 = null;

      Object var6;
      try {
         var4 = this.invoke(var1, var2);
         var3 = var4.read_Object();
         return var3;
      } catch (ApplicationException var11) {
         return var3;
      } catch (RemarshalException var12) {
         var6 = this.get_interface_def(var1);
      } finally {
         this.releaseReply(var1, var4);
      }

      return var6;
   }

   public OutputStream request(Object var1, String var2, boolean var3) {
      try {
         IOR var4 = this.getInvocationIOR();
         EndPoint var5 = EndPointManager.findOrCreateEndPoint(var4);
         RequestMessage var6 = new RequestMessage(var5, var4, var2, !var3);
         if (this.version != null) {
            var5.setMessageServiceContext(var6, new VendorInfoCluster(this.version));
         }

         Policy var7;
         if ((var7 = this.getPolicy(32)) != null) {
            var6.setTimeout(((RelativeRoundtripTimeoutPolicyImpl)var7).relativeExpiryMillis());
         } else if ((var7 = this.getPolicy(31)) != null) {
            var6.setTimeout(((RelativeRequestTimeoutPolicyImpl)var7).relativeExpiryMillis());
         } else if ((var7 = this.getPolicy(28)) != null) {
            var6.setTimeout(((RequestEndTimePolicyImpl)var7).relativeTimeoutMillis());
         } else if ((var7 = this.getPolicy(30)) != null) {
            var6.setTimeout(((ReplyEndTimePolicyImpl)var7).relativeTimeoutMillis());
         }

         var5.setSubject(var6, (AuthenticatedSubject)SubjectManager.getSubjectManager().getCurrentSubject(kernelId));
         if (var4.getProfile().isTransactional() || IIOPService.txMechanism == 3 || var1 instanceof TransactionalObject) {
            if (KernelStatus.isServer()) {
               OTSHelper.forceLocalCoordinator();
            }

            TransactionManager var8 = (TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager();
            ServerTransactionInterceptor var9 = (ServerTransactionInterceptor)var8.getInterceptor();
            var5.setOutboundRequestTxContext(var6, var9.sendRequest((EndPoint)null));
         }

         var6.flush();
         return var6.getOutputStream();
      } catch (IOException var10) {
         throw Utils.mapToCORBAException(var10);
      }
   }

   public InputStream invoke(Object var1, OutputStream var2) throws ApplicationException, RemarshalException {
      RequestMessage var3 = (RequestMessage)((MessageStream)var2).getMessage();
      if (var3.isOneWay()) {
         this.sendOneway(var3);
         return null;
      } else {
         return this.invoke(var1, var3);
      }
   }

   public Request request(Object var1, String var2) {
      return new RequestImpl(var1, this, var2, (NVList)null, (NamedValue)null, (ExceptionList)null);
   }

   public Request create_request(Object var1, Context var2, String var3, NVList var4, NamedValue var5, ExceptionList var6, ContextList var7) {
      return new RequestImpl(var1, this, var3, var4, var5, var6);
   }

   public Request create_request(Object var1, Context var2, String var3, NVList var4, NamedValue var5) {
      return new RequestImpl(var1, this, var3, var4, var5, (ExceptionList)null);
   }

   InputStream invoke(Object var1, RequestMessage var2) throws ApplicationException, RemarshalException {
      try {
         try {
            EndPoint var3 = var2.getEndPoint();
            if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
               IIOPLogger.logDebugTransport("REQUEST(" + var2.getRequestID() + "): remote IDL invoke " + var2.getOperationName() + "()");
            }

            ReplyMessage var4 = (ReplyMessage)var3.sendReceive(var2);
            return this.postInvoke(var1, var2, var4);
         } catch (IOException var7) {
            throw Utils.mapToCORBAException(var7);
         }
      } catch (SystemException var8) {
         if (this.replicas != null && ORBHelper.isRecoverableORBFailure(var8, this.replicas.getIdempotent())) {
            synchronized(this.replicas) {
               if (this.replicas.getIORs().size() > 1) {
                  this.replicas.getIORs().remove(this.replicas.getIORs().indexOf(var2.getIOR()));
                  throw new RemarshalException();
               }
            }
         }

         throw var8;
      }
   }

   IIOPInputStream postInvoke(Object var1, RequestMessage var2, ReplyMessage var3) throws ApplicationException, RemarshalException, IOException {
      EndPoint var4 = var3.getEndPoint();
      if (var2.getIOR().getProfile().isTransactional() || IIOPService.txMechanism == 3 || var1 instanceof TransactionalObject) {
         TransactionManager var5 = (TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager();
         TransactionInterceptor var6 = var5.getInterceptor();
         var6.receiveResponse(var4.getInboundResponseTxContext(var3));
      }

      if (var3.needsForwarding()) {
         if (this.replicas != null) {
            synchronized(this.replicas) {
               int var13 = this.replicas.getIORs().indexOf(var2.getIOR());
               this.replicas.getIORs().remove(var13);
               this.replicas.getIORs().add(var13, var3.getIOR());
            }
         }

         this.currentIOR = var3.getIOR();
         throw new RemarshalException();
      } else {
         VendorInfoCluster var11 = (VendorInfoCluster)var4.getMessageServiceContext(var3, 1111834883);
         if (var11 != null) {
            synchronized(this) {
               var11.setClusterInfo(this.replicas);
               this.replicas = var11.getClusterInfo();
               this.version = (Version)var11.version();
            }
         }

         if (var3.getReplyStatus() == 1) {
            throw new ApplicationException(var3.getExceptionId().toString(), var3.getInputStream());
         } else {
            Throwable var12 = var3.getThrowable();
            if (var12 != null) {
               if (KernelStatus.isServer() && var12 instanceof TRANSACTION_ROLLEDBACK) {
                  Transaction var7 = (Transaction)TransactionHelper.getTransactionHelper().getTransaction();
                  if (var7 != null) {
                     var7.setRollbackOnly(var12);
                  }
               }

               throw (SystemException)StackTraceUtils.getThrowableWithCause(var12);
            } else {
               return var3.getInputStream();
            }
         }
      }
   }

   void sendDeferred(RequestMessage var1) {
      try {
         EndPoint var2 = var1.getEndPoint();
         if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
            IIOPLogger.logDebugTransport("REQUEST(" + var1.getRequestID() + "): deferred IDL invoke " + var1.getOperationName() + "()");
         }

         var2.sendDeferred(var1);
      } catch (IOException var3) {
         throw Utils.mapToCORBAException(var3);
      }
   }

   void sendOneway(RequestMessage var1) {
      try {
         EndPoint var2 = var1.getEndPoint();
         if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
            IIOPLogger.logDebugTransport("REQUEST(" + var1.getRequestID() + "): oneway IDL invoke " + var1.getOperationName() + "()");
         }

         var2.send(var1.getOutputStream());
      } catch (IOException var3) {
         throw Utils.mapToCORBAException(var3);
      }
   }

   protected IOR getInvocationIOR() throws IOException {
      if (this.currentIOR != null) {
         if (this.replicas != null) {
            this.current = this.selector.select(this.current, this.replicas.getIORs().size());
            this.currentIOR = (IOR)this.replicas.getIORs().get(this.current);
         }

         return this.currentIOR;
      } else {
         synchronized(this) {
            if (this.currentIOR == null) {
               this.currentIOR = IIOPRemoteRef.locateIORForRequest(this.getIOR());
               this.replicas = (ClusterComponent)this.currentIOR.getProfile().getComponent(1111834883);
               if (this.replicas != null) {
                  this.selector = SelectorFactory.getSelector(this.replicas.getClusterAlgorithm());
                  this.version = this.replicas.getVersion();
                  this.current = 0;
               }
            }
         }

         return this.getInvocationIOR();
      }
   }
}
