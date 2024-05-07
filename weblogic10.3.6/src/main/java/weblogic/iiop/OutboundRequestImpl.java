package weblogic.iiop;

import java.io.IOException;
import java.rmi.MarshalException;
import java.rmi.RemoteException;
import java.security.AccessController;
import weblogic.corba.j2ee.workarea.WorkAreaContext;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.ObjectIO;
import weblogic.rmi.spi.AsyncCallback;
import weblogic.rmi.spi.InboundResponse;
import weblogic.rmi.spi.MsgOutput;
import weblogic.rmi.spi.OutboundRequest;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionInterceptor;
import weblogic.transaction.TransactionManager;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextOutput;
import weblogic.workarea.spi.WorkContextMapInterceptor;

public final class OutboundRequestImpl implements OutboundRequest {
   MsgOutput msgOutput;
   private final EndPoint endPoint;
   private final RequestMessage request;
   private RuntimeMethodDescriptor md;
   private Object[] args;
   private boolean rmiType;
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private static final DebugLogger debugIIOPTransport = DebugLogger.getDebugLogger("DebugIIOPTransport");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   OutboundRequestImpl(EndPoint var1, RequestMessage var2, boolean var3, RuntimeMethodDescriptor var4) {
      this.rmiType = var3;
      if (var3) {
         this.msgOutput = var2.getOutputStream();
      } else {
         this.msgOutput = new IDLMsgOutput(var2.getOutputStream());
      }

      this.endPoint = var1;
      this.request = var2;
      var1.setSubject(var2, SecurityServiceManager.getCurrentSubject(kernelId));
      this.md = var4;
   }

   OutboundRequestImpl(RemoteReference var1, RuntimeMethodDescriptor var2) {
      throw new AssertionError("not implemented");
   }

   public MsgOutput getMsgOutput() {
      this.request.flush();
      return this.msgOutput;
   }

   public void marshalCustomCallData() throws IOException {
      this.flush();
   }

   public weblogic.rmi.spi.EndPoint getEndPoint() {
      return this.endPoint;
   }

   public void marshalArgs(Object[] var1) throws MarshalException {
      this.args = var1;
   }

   private void flush() throws MarshalException {
      try {
         Class[] var1 = this.md.getParameterTypes();
         short[] var2 = this.md.getParameterTypeAbbrevs();
         MsgOutput var3 = this.getMsgOutput();
         if (var1.length != 0) {
            for(int var4 = 0; var4 < this.args.length; ++var4) {
               ObjectIO.writeObject(var3, this.args[var4], var1[var4], var2[var4]);
            }

         }
      } catch (IOException var5) {
         throw new MarshalException("failed to marshal " + this.md.getSignature(), var5);
      }
   }

   public void sendOneWay() throws RemoteException {
      if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
         IIOPLogger.logDebugTransport("REQUEST(" + this.request.getRequestID() + "): oneway invoke " + this.request.getOperationName() + "()");
      }

      this.request.setOneWay();
      this.flush();
      this.endPoint.send(this.request.getOutputStream());
   }

   public InboundResponse sendReceive() throws Throwable {
      this.request.setTimeout((long)this.md.getTimeOut());
      if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
         IIOPLogger.logDebugTransport("REQUEST(" + this.request.getRequestID() + "): timeout " + this.md.getTimeOut() + "ms : remote invoke " + this.request.getOperationName() + "()");
      }

      this.flush();
      ReplyMessage var1 = (ReplyMessage)this.endPoint.sendReceive(this.request, this.request.getFlags());
      IOR var2 = this.request.getIOR();
      InboundResponseImpl var3 = new InboundResponseImpl(this.endPoint, var1, this.rmiType, this.md, this.args, var2 == null ? null : var2.getCodebase());
      receivedTxResponse(var3.getTxContext());
      return var3;
   }

   public void sendAsync(AsyncCallback var1) throws RemoteException {
      this.flush();
   }

   public void transferThreadLocalContext() throws IOException {
      WorkContextMapInterceptor var1 = WorkContextHelper.getWorkContextHelper().getLocalInterceptor();
      if (var1 != null) {
         WorkContextOutput var2 = WorkAreaContext.createOutputStream(this.endPoint);
         var1.sendRequest(var2, 4);
         this.endPoint.setMessageServiceContext(this.request, new WorkAreaContext(var2));
      }

   }

   public void setTimeOut(int var1) {
      this.request.setTimeout((long)var1);
   }

   static void receivedTxResponse(Object var0) throws RemoteException {
      TransactionManager var1 = (TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager();
      TransactionInterceptor var2 = var1.getInterceptor();
      var2.receiveResponse(var0);
   }

   public void setTxContext(Object var1) {
      this.endPoint.setOutboundRequestTxContext(this.request, var1);
   }

   public void setReplicaInfo(Object var1) throws IOException {
      if (var1 != null) {
         this.endPoint.setMessageServiceContext(this.request, new VendorInfoCluster(var1));
      }

   }

   public void setActivationID(Object var1) throws IOException {
   }

   public void setContext(int var1, Object var2) throws IOException {
      if (var2 != null) {
         switch (var1) {
            case 4:
               this.endPoint.setMessageServiceContext(this.request, new VendorInfoTrace((byte[])((byte[])var2)));
            default:
         }
      }
   }

   public void setRuntimeMethodDescriptor(RuntimeMethodDescriptor var1) throws IOException {
   }

   public RequestMessage getRequest() {
      return this.request;
   }

   public void close() throws IOException {
      this.msgOutput.close();
   }
}
