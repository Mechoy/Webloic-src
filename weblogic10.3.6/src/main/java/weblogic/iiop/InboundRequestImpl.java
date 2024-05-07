package weblogic.iiop;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.cert.X509Certificate;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA.portable.InputStream;
import org.omg.PortableServer.POA;
import weblogic.corba.cos.transactions.PropagationContextImpl;
import weblogic.corba.idl.ObjectImpl;
import weblogic.corba.j2ee.workarea.WorkAreaContext;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.protocol.Protocol;
import weblogic.protocol.ServerChannel;
import weblogic.rmi.extensions.server.ActivatableServerReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.MethodDescriptor;
import weblogic.rmi.internal.OIDManager;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.rmi.internal.ServerReference;
import weblogic.rmi.spi.MsgInput;
import weblogic.security.service.ContextHandler;
import weblogic.security.subject.AbstractSubject;
import weblogic.utils.Debug;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.spi.WorkContextMapInterceptor;

final class InboundRequestImpl implements InboundRequest {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private static final String INTERFACE = "_interface";
   private static final String GET_INTERFACE = "_get_interface_def";
   private static final OIDManager oidManager = OIDManager.getInstance();
   private final EndPoint endPoint;
   private final RequestMessage request;
   private MsgInput msgInput;
   private int oid;
   private RuntimeMethodDescriptor md;
   private Method method;
   private ServerReference serverReference;
   private static final Object NULL = new Object();
   private Object cachedTx;
   private boolean pending;
   private boolean rmiType;
   private static final MethodDescriptor NULL_METHOD = new MethodDescriptor();

   static void p(String var0) {
      System.err.println("<InboundRequestImpl> " + var0);
   }

   public InboundRequestImpl(EndPoint var1, RequestMessage var2) throws RemoteException {
      this(var1, var2, -1);
   }

   public InboundRequestImpl(EndPoint var1, RequestMessage var2, int var3) throws RemoteException {
      this.oid = -1;
      this.cachedTx = NULL;
      this.pending = false;
      this.rmiType = true;
      this.oid = var3;
      this.endPoint = var1;
      this.request = var2;
      String var4 = var2.getOperationName();
      if (var4.charAt(0) == '_' && var4.equals("_interface")) {
         var4 = "_get_interface_def";
      }

      try {
         RuntimeDescriptor var5 = this.getServerReference().getDescriptor();
         this.md = var5.getMethodDescriptor(var4);
         boolean var6 = false;
         Class[] var7 = var5.getRemoteInterfaces();

         int var8;
         for(var8 = 0; var8 < var7.length; ++var8) {
            if (IDLEntity.class.isAssignableFrom(var7[var8])) {
               var6 = true;
               break;
            }
         }

         if (this.md == null) {
            if (!var6) {
               Object var19 = this.getServerReference().getImplementation();
               ClassLoader var9 = null;
               ClassLoader var10 = Thread.currentThread().getContextClassLoader();

               try {
                  if (var19 == null) {
                     var9 = this.getServerReference().getApplicationClassLoader();
                     Thread.currentThread().setContextClassLoader(var9);
                     var19 = ((ActivatableServerReference)this.getServerReference()).getImplementation(this.getActivationID());
                  }
               } catch (IOException var16) {
                  throw new NoSuchObjectException("Failed to unmarshal activation id");
               } finally {
                  Thread.currentThread().setContextClassLoader(var10);
               }

               Debug.assertion(var19 != null);
               ObjectImpl var11 = new ObjectImpl((Remote)var19);
               oidManager.getReplacement(var11);
               this.serverReference = oidManager.getServerReference(var11);
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  p("Object server ref is : " + this.serverReference);
               }

               this.oid = this.serverReference.getObjectID();
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  p("Object id is : " + this.oid);
               }

               var6 = true;
               this.md = this.serverReference.getDescriptor().getMethodDescriptor(var4);
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  p("Object md is : " + this.md);
               }

               var19 = this.serverReference.getImplementation();
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  p("Object impl is : " + var19);
               }
            } else {
               for(var8 = 0; var8 < var7.length; ++var8) {
                  if (POA.class.isAssignableFrom(var7[var8])) {
                     this.md = NULL_METHOD;
                     break;
                  }
               }
            }
         }

         this.method = this.getServerReference().getDescriptor().getMethod(var4);
         if (this.md != null && !var6) {
            this.msgInput = var2.getInputStream();
         } else {
            this.rmiType = false;
            this.msgInput = new IDLMsgInput(var2.getInputStream());
         }
      } catch (NoSuchObjectException var18) {
         this.msgInput = var2.getInputStream();
         this.oid = var3;
      }

   }

   final ServerReference getServerReference() throws RemoteException {
      if (this.serverReference == null) {
         this.serverReference = oidManager.getServerReference(this.getObjectID());
      }

      return this.serverReference;
   }

   final void registerAsPending() {
      if (!this.isOneWay()) {
         this.pending = true;
         this.endPoint.incrementPendingRequests();
      }

   }

   public final InputStream getInputStream() {
      this.request.flush();
      return this.request.getInputStream();
   }

   public final String getMethod() {
      return this.request.getOperationName();
   }

   public MsgInput getMsgInput() {
      return this.msgInput;
   }

   public boolean isCollocated() {
      return false;
   }

   public weblogic.rmi.spi.EndPoint getEndPoint() {
      return this.endPoint;
   }

   public ServerChannel getServerChannel() {
      return this.endPoint.getServerChannel();
   }

   public Protocol getProtocol() {
      return this.endPoint.isSecure() ? ProtocolHandlerIIOPS.PROTOCOL_IIOPS : ProtocolHandlerIIOP.PROTOCOL_IIOP;
   }

   public AbstractSubject getSubject() {
      return this.request.getSubject();
   }

   public int getObjectID() {
      if (this.oid != -1) {
         return this.oid;
      } else {
         this.oid = this.request.getObjectKey().getObjectID();
         return this.oid;
      }
   }

   public RuntimeMethodDescriptor getRuntimeMethodDescriptor(RuntimeDescriptor var1) {
      return this.md;
   }

   public Object getTxContext() {
      if (this.cachedTx == NULL) {
         if (this.md != null && !this.md.isTransactional()) {
            this.cachedTx = null;
         } else {
            this.cachedTx = this.endPoint.getInboundRequestTxContext(this.request);
         }
      }

      return this.cachedTx;
   }

   public void retrieveThreadLocalContext() throws IOException {
      WorkAreaContext var1 = (WorkAreaContext)this.endPoint.getMessageServiceContext(this.request, 1111834891);
      if (var1 != null) {
         WorkContextMapInterceptor var2 = WorkContextHelper.getWorkContextHelper().getInterceptor();
         var2.receiveRequest(var1.getInputStream());
      }

   }

   public Object getReplicaInfo() throws IOException {
      VendorInfoCluster var1 = (VendorInfoCluster)this.endPoint.getMessageServiceContext(this.request, 1111834883);
      return var1 != null ? var1.version() : null;
   }

   public Object getActivationID() throws IOException {
      return this.request.getObjectKey().getActivationID();
   }

   public weblogic.rmi.spi.OutboundResponse getOutboundResponse() {
      if (this.isOneWay()) {
         return null;
      } else {
         ReplyMessage var1 = new ReplyMessage(this.endPoint, this.request, this.request.getOutboundServiceContexts(), 0);
         ServiceContext var2 = null;
         if (this.md != null && !this.md.isTransactional() && (var2 = this.request.getServiceContext(0)) != null) {
            var1.addServiceContext(((PropagationContextImpl)var2).getNullContext());
         }

         return new OutboundResponseImpl(this.endPoint, var1, this.method, this.rmiType, this.pending);
      }
   }

   public void close() throws IOException {
      this.msgInput.close();
   }

   final boolean isOneWay() {
      return this.request.isOneWay();
   }

   public Object getContext(int var1) throws IOException {
      switch (var1) {
         case 4:
            ServiceContext var2 = null;
            if ((var2 = this.request.getServiceContext(1111834890)) != null) {
               return ((VendorInfoTrace)var2).getTrace();
            }
         default:
            return null;
      }
   }

   public X509Certificate[] getCertificateChain() {
      return null;
   }

   public ContextHandler getContextHandler() {
      return null;
   }
}
