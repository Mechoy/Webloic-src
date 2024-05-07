package weblogic.messaging.dispatcher;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.ConnectException;
import java.rmi.MarshalException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import weblogic.common.WLObjectInput;
import weblogic.jms.common.JMSDebug;
import weblogic.messaging.ID;
import weblogic.protocol.ServerChannel;
import weblogic.rjvm.JVMID;
import weblogic.rjvm.MsgAbbrevOutputStream;
import weblogic.rjvm.RJVM;
import weblogic.rjvm.RJVMManager;
import weblogic.rjvm.RequestStream;
import weblogic.rjvm.ResponseListener;
import weblogic.rmi.extensions.AsyncResult;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.DisconnectMonitorList;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.server.DisconnectMonitorProvider;
import weblogic.rmi.extensions.server.SmartStubInfo;
import weblogic.rmi.spi.AsyncCallback;
import weblogic.rmi.spi.EndPoint;
import weblogic.rmi.spi.HostID;
import weblogic.rmi.spi.InboundResponse;
import weblogic.rmi.spi.Interceptor;
import weblogic.rmi.spi.InterceptorManager;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.RMIRuntime;
import weblogic.socket.UnrecoverableConnectException;

public class DispatcherProxy implements DispatcherRemote, DispatcherOneWay, Externalizable, SmartStubInfo {
   static final long serialVersionUID = 6780111363122647296L;
   private static final byte RT_ONEWAY = 1;
   private static final byte RT_ASYNC = 2;
   private static final byte RT_TRANSACTIONAL = 4;
   private static final byte RT_INVOCABLE_ID = 8;
   static final byte RT_WORK_ID = 16;
   private static boolean disconnectMonitorInitialized;
   private int oid;
   private JVMID hostID;
   private RJVM rjvm;
   private String objectHandlerClassName;
   private DispatcherObjectHandler objectHandler;
   private transient boolean hostReachable;

   public DispatcherProxy() {
      this.hostReachable = true;
      initializeDisconnectMonitorImpl();
   }

   public DispatcherProxy(int var1, HostID var2, String var3) {
      this();
      this.oid = var1;
      this.hostID = (JVMID)var2;
      this.objectHandlerClassName = var3;
      this.objectHandler = DispatcherObjectHandler.load(var3);
   }

   private static synchronized void initializeDisconnectMonitorImpl() {
      if (!disconnectMonitorInitialized) {
         DisconnectMonitorList var0 = DisconnectMonitorListImpl.getDisconnectMonitorList();
         var0.addDisconnectMonitor(new DispatcherProxyDisconnectMonitorImpl());
         disconnectMonitorInitialized = true;
      }

   }

   public void dispatchAsyncFuture(Request var1, AsyncResult var2) throws RemoteException {
      RequestStream var3 = this.marshal((byte)2, var1);
      var3.sendAsync(this.oid, new DispatcherResponseListener(false, (AsyncCallback)var2, var1, this.objectHandler));
   }

   public void dispatchAsyncTranFuture(Request var1, AsyncResult var2) throws RemoteException {
      RequestStream var3 = this.marshal((byte)6, var1);
      var3.sendAsync(this.oid, new DispatcherResponseListener(true, (AsyncCallback)var2, var1, this.objectHandler));
   }

   public Response dispatchSyncFuture(Request var1) throws RemoteException {
      RequestStream var2 = this.marshal((byte)0, var1);
      return this.unmarshalResponse(false, var1, var2.sendRecv(this.oid));
   }

   public Response dispatchSyncNoTranFuture(Request var1) throws RemoteException {
      RequestStream var2 = this.marshal((byte)0, var1);
      return this.unmarshalResponse(false, var1, var2.sendRecv(this.oid));
   }

   public Response dispatchSyncTranFuture(Request var1) throws RemoteException, DispatcherException {
      RequestStream var2 = this.marshal((byte)4, var1);
      return this.unmarshalResponse(true, var1, var2.sendRecv(this.oid));
   }

   public Response dispatchSyncTranFutureWithId(Request var1, int var2) throws RemoteException, DispatcherException {
      RequestStream var3 = this.marshal((byte)4, var1, var2);
      return this.unmarshalResponse(true, var1, var3.sendRecv(this.oid));
   }

   public void dispatchOneWay(Request var1) throws RemoteException {
      RequestStream var2 = this.marshal((byte)1, var1);
      var2.sendOneWay(this.oid, (byte)101);
   }

   public void dispatchOneWayWithId(Request var1, int var2) throws RemoteException {
      RequestStream var3 = this.marshal((byte)1, var1, var2);
      var3.sendOneWay(this.oid, (byte)101);
   }

   public RJVM getRJVM() {
      if (this.rjvm == null || this.rjvm.isDead()) {
         this.rjvm = RJVMManager.getRJVMManager().findOrCreate(this.hostID);
      }

      return this.rjvm;
   }

   private RequestStream marshal(byte var1, Request var2) throws RemoteException {
      return this.marshal(var1, var2, 0);
   }

   private RequestStream marshal(byte var1, Request var2, int var3) throws RemoteException {
      if (!this.hostReachable) {
         throw new ConnectException("Unable to reach host");
      } else {
         RJVM var4 = this.getRJVM();

         MsgAbbrevOutputStream var5;
         try {
            var5 = var4.getRequestStream((ServerChannel)null);
         } catch (UnrecoverableConnectException var9) {
            this.hostReachable = false;
            throw new ConnectException("Unable to reach host");
         } catch (IOException var10) {
            throw new RemoteException(var10.getMessage(), var10);
         }

         if (isTransactional(var1)) {
            Interceptor var6 = InterceptorManager.getManager().getTransactionInterceptor();
            if (var6 != null) {
               var5.setTxContext(var6.sendRequest(var4));
            }

            try {
               var5.marshalCustomCallData();
            } catch (IOException var8) {
               throw new MarshalException("failed to marshal : " + var2, var8);
            }
         }

         ID var12 = var2.getInvocableId();
         if (var12 != null) {
            var1 = (byte)(var1 | 8);
         }

         if (var3 > 0) {
            var1 = (byte)(var1 | 16);
         }

         try {
            var5.writeByte(var1);
            if (var3 > 0) {
               var5.writeInt(var3);
            }

            if (var12 != null) {
               var12.writeExternal(var5);
            }

            this.objectHandler.writeRequest(var5, var2);
         } catch (IOException var11) {
            if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
               DispatcherObjectHandler.debugWireOperation("SendReq  ", var1, var2, var3, var12, (Response)null, var11);
            }

            throw new MarshalException("failed to marshal : " + var2, var11);
         }

         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            DispatcherObjectHandler.debugWireOperation("SendReq  ", var1, var2, var3, var12, (Response)null, (Throwable)null);
         }

         return var5;
      }
   }

   private Response unmarshalResponse(boolean var1, Request var2, weblogic.rjvm.Response var3) throws RemoteException {
      InboundResponse var4 = (InboundResponse)var3;

      try {
         var4.retrieveThreadLocalContext();
      } catch (IOException var22) {
         throw new UnmarshalException("failed to unmarshal response", var22);
      }

      if (var1) {
         try {
            Interceptor var5 = InterceptorManager.getManager().getTransactionInterceptor();
            if (var5 != null) {
               var5.receiveResponse(var3.getTxContext());
            }
         } catch (RemoteException var23) {
            if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
               DispatcherObjectHandler.debugWireOperation("RecvResp ", (byte)15, var2, -1, var2.getInvocableId(), (Response)null, var23);
            }

            throw var23;
         }
      }

      Throwable var24 = var3.getThrowable();
      if (var24 != null) {
         if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
            DispatcherObjectHandler.debugWireOperation("RecvResp ", (byte)15, var2, -1, var2.getInvocableId(), (Response)null, var24);
         }

         if (var24 instanceof Error) {
            throw (Error)var24;
         } else if (var24 instanceof RuntimeException) {
            throw (RuntimeException)var24;
         } else if (var24 instanceof RemoteException) {
            throw (RemoteException)var24;
         } else {
            throw new RemoteRuntimeException(var24);
         }
      } else {
         WLObjectInput var6 = var3.getMsg();

         Response var7;
         try {
            var7 = this.objectHandler.readResponse(var6, var2);
         } catch (IOException var19) {
            throw new UnmarshalException("failed to unmarshal response", var19);
         } catch (ClassNotFoundException var20) {
            throw new UnmarshalException("failed to unmarshal response", var20);
         } finally {
            try {
               var6.close();
            } catch (IOException var18) {
               throw new AssertionError(var18);
            }
         }

         return var7;
      }
   }

   static boolean isOneWay(int var0) {
      return (var0 & 1) != 0;
   }

   static boolean isAsync(int var0) {
      return (var0 & 2) != 0;
   }

   static boolean isTransactional(int var0) {
      return (var0 & 4) != 0;
   }

   static boolean hasInvocableID(int var0) {
      return (var0 & 8) != 0;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(this.oid);
      var1.writeObject(this.hostID);
      var1.writeUTF("weblogic.jms.dispatcher.DispatcherObjectHandler");
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.oid = var1.readInt();
      this.hostID = (JVMID)var1.readObject();
      String var2 = var1.readUTF();

      try {
         this.objectHandler = DispatcherObjectHandler.load(var2);
      } catch (NoClassDefFoundError var4) {
         if (MessagingEnvironment.getMessagingEnvironment().isServer()) {
            throw var4;
         }

         this.objectHandler = DispatcherObjectHandler.load("weblogic.jms.dispatcher.FEDispatcherObjectHandler");
      } catch (AssertionError var5) {
         if (MessagingEnvironment.getMessagingEnvironment().isServer()) {
            throw var5;
         }

         this.objectHandler = DispatcherObjectHandler.load("weblogic.jms.dispatcher.FEDispatcherObjectHandler");
      }

   }

   public Object getSmartStub(Object var1) {
      return this;
   }

   private static final class InboundResponseWrapper implements InboundResponse {
      private final InboundResponse inboundResponse;
      private final Request request;
      private final Throwable thr;
      private DispatcherObjectHandler objectHandler;

      private InboundResponseWrapper(InboundResponse var1, Request var2, Throwable var3, DispatcherObjectHandler var4) {
         this.inboundResponse = var1;
         this.request = var2;
         this.thr = var3;
         this.objectHandler = var4;
      }

      public MsgInput getMsgInput() {
         return this.inboundResponse.getMsgInput();
      }

      public Object unmarshalReturn() throws Throwable {
         if (this.thr != null) {
            throw this.thr;
         } else {
            return this.objectHandler.readResponse(this.getMsgInput(), this.request);
         }
      }

      public void retrieveThreadLocalContext() throws IOException {
         this.inboundResponse.retrieveThreadLocalContext();
      }

      public Object getTxContext() {
         return this.inboundResponse.getTxContext();
      }

      public Object getReplicaInfo() throws IOException {
         return this.inboundResponse.getReplicaInfo();
      }

      public Object getActivatedPinnedRef() throws IOException {
         return this.inboundResponse.getActivatedPinnedRef();
      }

      public Object getContext(int var1) throws IOException {
         return this.inboundResponse.getContext(var1);
      }

      public void close() throws IOException {
         this.inboundResponse.close();
      }

      // $FF: synthetic method
      InboundResponseWrapper(InboundResponse var1, Request var2, Throwable var3, DispatcherObjectHandler var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }

   private static final class DispatcherProxyDisconnectMonitorImpl implements DisconnectMonitorProvider {
      private DispatcherProxyDisconnectMonitorImpl() {
      }

      public boolean addDisconnectListener(Remote var1, DisconnectListener var2) {
         HostID var3 = getHostIDFromStub(var1);
         if (var3 != null) {
            EndPoint var4 = RMIRuntime.findEndPoint(var3);
            if (var4 == null || var4.isDead()) {
               RMIRuntime.getRMIRuntime();
               var4 = RMIRuntime.findOrCreateEndPoint(var3);
            }

            if (var4 != null) {
               return var4.addDisconnectListener(var1, var2);
            }
         }

         return false;
      }

      public boolean removeDisconnectListener(Remote var1, DisconnectListener var2) {
         HostID var3 = getHostIDFromStub(var1);
         if (var3 != null) {
            EndPoint var4 = RMIRuntime.findEndPoint(var3);
            if (var4 != null) {
               var4.removeDisconnectListener(var1, var2);
            }

            return true;
         } else {
            return false;
         }
      }

      private static HostID getHostIDFromStub(Remote var0) {
         return var0 instanceof DispatcherProxy ? ((DispatcherProxy)var0).hostID : null;
      }

      // $FF: synthetic method
      DispatcherProxyDisconnectMonitorImpl(Object var1) {
         this();
      }
   }

   private static final class DispatcherResponseListener implements ResponseListener {
      private final boolean transactional;
      private final AsyncCallback callback;
      private final Request request;
      private final DispatcherObjectHandler objectHandler;

      DispatcherResponseListener(boolean var1, AsyncCallback var2, Request var3, DispatcherObjectHandler var4) {
         this.transactional = var1;
         this.callback = var2;
         this.request = var3;
         this.objectHandler = var4;
      }

      public synchronized void response(weblogic.rjvm.Response var1) {
         InboundResponse var2 = (InboundResponse)var1;

         try {
            var2.retrieveThreadLocalContext();
         } catch (IOException var6) {
            throw new AssertionError(var6);
         }

         if (this.transactional) {
            Interceptor var3 = InterceptorManager.getManager().getTransactionInterceptor();

            try {
               var3.receiveResponse(var2.getTxContext());
            } catch (RemoteException var5) {
               throw new AssertionError(var5);
            }
         }

         this.callback.setInboundResponse(new InboundResponseWrapper(var2, this.request, var1.getThrowable(), this.objectHandler));
      }
   }
}
