package weblogic.messaging.dispatcher;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.security.AccessController;
import weblogic.jms.common.JMSDebug;
import weblogic.messaging.common.IDImpl;
import weblogic.rmi.RMILogger;
import weblogic.rmi.extensions.server.FutureResponse;
import weblogic.rmi.internal.AsyncResultImpl;
import weblogic.rmi.internal.BasicFutureResponse;
import weblogic.rmi.internal.BasicServerRef;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.Interceptor;
import weblogic.rmi.spi.InterceptorManager;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.MsgOutput;
import weblogic.rmi.spi.OutboundResponse;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.work.IDBasedConstraintEnforcement;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManager;

public class DispatcherServerRef extends BasicServerRef {
   private final DispatcherObjectHandler objectHandler;
   private final WorkManager workManager;
   private final WorkManager oneWayWorkManager;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private int count;
   private int lastMethodId;
   private int lastRequestType;

   public DispatcherServerRef(Object var1) throws RemoteException {
      super(var1);
      FastDispatcherImpl var2 = (FastDispatcherImpl)var1;
      this.workManager = var2.getWorkManager();
      this.oneWayWorkManager = var2.getOneWayWorkManager();
      this.objectHandler = var2.getObjectHandler();
   }

   public final void dispatch(final InboundRequest var1) {
      MsgInput var2 = var1.getMsgInput();

      final byte var3;
      int var4;
      try {
         var3 = var2.readByte();
         if ((var3 & 16) != 0) {
            var4 = var2.readInt();
         } else {
            var4 = 0;
         }
      } catch (IOException var7) {
         trySendThrowableBeforeInterceptor(var1, new UnmarshalException("Could not unmarshal inboundRequest type", var7));
         return;
      }

      if (DispatcherProxy.isTransactional(var3)) {
         Interceptor var5 = InterceptorManager.getManager().getTransactionInterceptor();
         if (var5 != null) {
            try {
               var5.receiveRequest(var1.getTxContext());
            } catch (RemoteException var8) {
               if (!DispatcherProxy.isOneWay(var3)) {
                  trySendThrowable(var1, var8);
               }
            }
         }
      }

      WorkManager var9 = var4 <= 0 && !DispatcherProxy.isOneWay(var3) ? this.workManager : this.oneWayWorkManager;
      if (var4 > 0) {
         IDBasedConstraintEnforcement.getInstance().schedule(var9, new WorkAdapter() {
            public void run() {
               DispatcherServerRef.this.handleRequest(var3, var1);
            }
         }, var4);
      } else {
         var9.schedule(new WorkAdapter() {
            public void run() {
               DispatcherServerRef.this.handleRequest(var3, var1);
            }
         });
      }

   }

   private final void handleRequest(int var1, InboundRequest var2) {
      OutboundResponse var3 = null;

      try {
         if (!DispatcherProxy.isOneWay(var1)) {
            var3 = var2.getOutboundResponse();
            if (DispatcherProxy.isTransactional(var1)) {
               Interceptor var4 = InterceptorManager.getManager().getTransactionInterceptor();
               if (var4 != null) {
                  var4.dispatchRequest(var2.getTxContext());
               }
            }
         }

         AuthenticatedSubject var22 = (AuthenticatedSubject)var2.getSubject();

         try {
            SecurityServiceManager.pushSubject(KERNEL_ID, var22);
            this.invoke(var1, var2, var3);
         } finally {
            SecurityServiceManager.popSubject(KERNEL_ID);
         }
      } catch (Throwable var20) {
         handleThrowable(var2, var3, var20);
      } finally {
         if (DispatcherProxy.isOneWay(var1)) {
            try {
               var2.close();
            } catch (IOException var18) {
               throw new AssertionError(var18);
            }
         }

      }

   }

   private static void handleThrowable(InboundRequest var0, OutboundResponse var1, Throwable var2) {
      if (var2 instanceof RuntimeException) {
         RMILogger.logRuntimeException("dispatch", var2);
      } else if (var2 instanceof Error) {
         RMILogger.logError("dispatch", var2);
      } else {
         RMILogger.logException("dispatch", var2);
      }

      if (var1 != null) {
         try {
            var1.close();
         } catch (IOException var5) {
         }

         try {
            var1 = var0.getOutboundResponse();
            var1.transferThreadLocalContext(var0);
         } catch (IOException var4) {
            RMILogger.logAssociateTX(var4);
         }
      }

      handleThrowable(var2, var1);
   }

   private final void invoke(int var1, InboundRequest var2, OutboundResponse var3) throws Exception {
      int var4 = this.count++;
      int var5 = this.lastMethodId;
      int var6 = this.lastRequestType;
      this.lastRequestType = var1;

      Request var7;
      try {
         MsgInput var8 = var2.getMsgInput();
         IDImpl var9;
         if (DispatcherProxy.hasInvocableID(var1)) {
            var9 = new IDImpl();
            var9.readExternal(var8);
         } else {
            var9 = null;
         }

         int var10 = var8.readInt();
         this.lastMethodId = var10;
         var7 = this.objectHandler.readRequest(var10, var8, var9);
      } catch (IOException var12) {
         throw new UnmarshalException("error unmarshalling arguments, count=" + var4 + ", lastMethodId" + var5 + ", lastRequestType" + var6, var12);
      } catch (ClassNotFoundException var13) {
         throw new UnmarshalException("error unmarshalling arguments; count=" + var4 + ", lastMethodId" + var5 + ", lastRequestType" + var6, var13);
      }

      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("Dispatching : " + var7 + " OneWay=" + DispatcherProxy.isOneWay(var1) + " Async=" + DispatcherProxy.isAsync(var1) + " Transactional=" + DispatcherProxy.isTransactional(var1));
      }

      if (!DispatcherProxy.isOneWay(var1)) {
         OutboundResponseWrapper var14 = new OutboundResponseWrapper(var2, var3, var7, this.objectHandler);
         var7.setFutureResponse(var14);
         if (DispatcherProxy.isAsync(var1)) {
            AsyncResultImpl var15 = new AsyncResultImpl(var2, var14);
            var7.setAsyncResult(var15);
         }

         if (DispatcherProxy.isTransactional(var1)) {
            var7.setTranInfo(2);
         } else {
            var7.setTranInfo(0);
         }
      }

      try {
         var7.wrappedFiniteStateMachine();
      } catch (Throwable var11) {
         var7.notifyResult(var11, false);
      }

   }

   protected boolean deferredUnmarshal() {
      return false;
   }

   static final class OutboundResponseWrapper extends BasicFutureResponse implements FutureResponse, OutboundResponse, MsgOutput {
      private final DispatcherObjectHandler objectHandler;
      private final OutboundResponse outboundResponse;
      private MsgOutput msgOutputDelegate;
      private final Request request;

      private OutboundResponseWrapper(InboundRequest var1, OutboundResponse var2, Request var3, DispatcherObjectHandler var4) {
         super(var1, var2);
         this.outboundResponse = var2;
         this.request = var3;
         this.objectHandler = var4;
      }

      public MsgOutput getMsgOutput() throws RemoteException {
         this.msgOutputDelegate = super.getMsgOutput();
         return this;
      }

      public void writeObject(Object var1, Class var2) throws IOException {
         if (var1 instanceof Throwable) {
            if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
               DispatcherObjectHandler.debugWireOperation("SendResp ", (byte)15, this.request, -1, this.request.getInvocableId(), (Response)null, (Throwable)var1);
            }

            this.outboundResponse.sendThrowable((Throwable)var1);
         } else {
            this.objectHandler.writeResponse(this.msgOutputDelegate, this.request, (Response)var1);
         }

      }

      public void writeObject(Object var1) throws IOException {
         this.objectHandler.writeResponse(this.msgOutputDelegate, this.request, (Response)var1);
      }

      public void write(int var1) throws IOException {
         this.msgOutputDelegate.write(var1);
      }

      public void write(byte[] var1) throws IOException {
         this.msgOutputDelegate.write(var1);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         this.msgOutputDelegate.write(var1, var2, var3);
      }

      public void flush() throws IOException {
         this.msgOutputDelegate.flush();
      }

      public void writeBoolean(boolean var1) throws IOException {
         this.msgOutputDelegate.writeBoolean(var1);
      }

      public void writeByte(int var1) throws IOException {
         this.msgOutputDelegate.writeByte(var1);
      }

      public void writeShort(int var1) throws IOException {
         this.msgOutputDelegate.writeShort(var1);
      }

      public void writeChar(int var1) throws IOException {
         this.msgOutputDelegate.writeChar(var1);
      }

      public void writeInt(int var1) throws IOException {
         this.msgOutputDelegate.writeInt(var1);
      }

      public void writeLong(long var1) throws IOException {
         this.msgOutputDelegate.writeLong(var1);
      }

      public void writeFloat(float var1) throws IOException {
         this.msgOutputDelegate.writeFloat(var1);
      }

      public void writeDouble(double var1) throws IOException {
         this.msgOutputDelegate.writeDouble(var1);
      }

      public void writeBytes(String var1) throws IOException {
         this.msgOutputDelegate.writeBytes(var1);
      }

      public void writeChars(String var1) throws IOException {
         this.msgOutputDelegate.writeChars(var1);
      }

      public void writeUTF(String var1) throws IOException {
         this.msgOutputDelegate.writeUTF(var1);
      }

      // $FF: synthetic method
      OutboundResponseWrapper(InboundRequest var1, OutboundResponse var2, Request var3, DispatcherObjectHandler var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }
}
