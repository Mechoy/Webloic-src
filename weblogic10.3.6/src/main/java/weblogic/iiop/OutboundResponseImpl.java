package weblogic.iiop;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.MarshalException;
import java.rmi.RemoteException;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.UNKNOWN;
import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import weblogic.corba.j2ee.workarea.WorkAreaContext;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.rmi.RMILogger;
import weblogic.rmi.spi.MsgOutput;
import weblogic.transaction.TxHelper;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextOutput;
import weblogic.workarea.spi.WorkContextMapInterceptor;

final class OutboundResponseImpl implements ReplyStatusConstants, OutboundResponse, ResponseHandler {
   private static final int TYPE_SYSTEM_EXCEPTION = 0;
   private static final int TYPE_USER_EXCEPTION = 1;
   private static final int TYPE_UNCHECKED_EXCEPTION = 2;
   private static final int TYPE_REMOTE_EXCEPTION = 3;
   private static final int TYPE_CHECKED_EXCEPTION = 4;
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private static final DebugCategory debugMarshal = Debug.getCategory("weblogic.iiop.marshal");
   private static final DebugLogger debugIIOPTransport = DebugLogger.getDebugLogger("DebugIIOPTransport");
   private static final DebugLogger debugIIOPMarshal = DebugLogger.getDebugLogger("DebugIIOPMarshal");
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private final EndPoint endPoint;
   private ReplyMessage reply;
   private final Method replyingTo;
   private MsgOutput msgOutput;
   private boolean pending = false;
   private boolean contextsMarshaled = false;
   private InboundRequest requestForReponseHandler;

   public OutboundResponseImpl(EndPoint var1, ReplyMessage var2, Method var3, boolean var4, boolean var5) {
      if (var4) {
         this.msgOutput = var2.getOutputStream();
      } else {
         this.msgOutput = new IDLMsgOutput(var2.getOutputStream());
      }

      this.endPoint = var1;
      this.reply = var2;
      this.replyingTo = var3;
      this.pending = var5;
   }

   public OutboundResponseImpl(EndPoint var1, ReplyMessage var2) {
      this.endPoint = var1;
      this.reply = var2;
      this.msgOutput = var2.getOutputStream();
      this.replyingTo = null;
   }

   static void p(String var0) {
      System.err.println("<OutboundResponseImpl> " + var0);
   }

   public MsgOutput getMsgOutput() {
      this.reply.flush();
      return this.msgOutput;
   }

   public void send() throws RemoteException {
      try {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
            IIOPLogger.logDebugTransport("REPLY(" + this.reply.getRequestID() + "): sending");
         }

         this.reply.flush();
         if (this.pending) {
            this.endPoint.decrementPendingRequests();
         }

         this.endPoint.send(this.reply.getOutputStream());
      } catch (IOException var9) {
         throw new MarshalException("IOException while sending", var9);
      } finally {
         try {
            this.close();
         } catch (IOException var8) {
            throw new MarshalException("IOException while closing", var8);
         }
      }

   }

   public void sendThrowable(Throwable var1) {
      this.reply.flush();
      if (this.pending) {
         this.endPoint.decrementPendingRequests();
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
         IIOPLogger.logDebugTransport("REPLY(" + this.reply.getRequestID() + "): sending EXCEPTION(" + var1.getClass().getName() + ")");
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugMarshal.isEnabled() || debugIIOPMarshal.isDebugEnabled()) {
         RMILogger.logException("sending exception", var1);
      }

      try {
         switch (this.getExceptionType(var1)) {
            case 0:
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  p("writeSystemException " + var1.getClass());
               }

               this.writeSystemException((SystemException)var1);
               break;
            case 1:
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  p("writeUserException " + var1.getClass());
               }

               this.writeUserException((UserException)var1);
               break;
            case 3:
               SystemException var2 = Utils.mapRemoteToCORBAException((RemoteException)var1);
               if (var2 != null) {
                  if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                     p("writeSystemException (re) " + var1.getClass() + " mapped to " + var2.getClass());
                  }

                  this.writeSystemException(var2);
                  break;
               }
            case 2:
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  p("writeUncheckedException " + var1.getClass());
               }

               this.writeUncheckedException(var1);
               break;
            case 4:
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  p("writeCheckedException " + var1.getClass());
               }

               this.writeCheckedException((Exception)var1);
         }
      } catch (SystemException var23) {
         IIOPLogger.logMarshalExceptionFailure(var1.getClass().toString(), var23);

         try {
            this.writeMarshalException();
         } catch (Throwable var19) {
            IIOPLogger.logCompleteMarshalExceptionFailure(var1.getClass().toString(), var19);

            try {
               this.close();
            } catch (IOException var17) {
            }

            return;
         }
      }

      try {
         this.endPoint.send(this.reply.getOutputStream());
         return;
      } catch (Throwable var21) {
         Throwable var24 = var21;

         try {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("failed endPoint.send (writeMarshalException)");
            }

            this.writeMarshalException();
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("failed endPoint.send (send)");
            }

            this.endPoint.send(this.reply.getOutputStream());
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("failed endPoint.send (done)");
            }

            IIOPLogger.logSendExceptionFailed(var1.getClass().toString(), var24);
            return;
         } catch (Throwable var20) {
            IIOPLogger.logSendExceptionCompletelyFailed(var1.getClass().toString(), var20);
         }
      } finally {
         try {
            this.close();
         } catch (IOException var18) {
         }

      }

   }

   public void transferThreadLocalContext(weblogic.rmi.spi.InboundRequest var1) throws IOException {
      if (!this.contextsMarshaled) {
         this.contextsMarshaled = true;
         this.setTxContext(TxHelper.getTransactionManager().getInterceptor().sendResponse(var1.getTxContext()));
         WorkContextMapInterceptor var2 = WorkContextHelper.getWorkContextHelper().getLocalInterceptor();
         if (var2 != null) {
            WorkContextOutput var3 = WorkAreaContext.createOutputStream(this.endPoint);
            var2.sendResponse(var3, 4);
            this.endPoint.setMessageServiceContext(this.reply, new WorkAreaContext(var3));
         }

      }
   }

   public void setTxContext(Object var1) {
      this.endPoint.setOutboundResponseTxContext(this.reply, var1);
   }

   public void setContext(int var1, Object var2) {
   }

   public void setReplicaInfo(Object var1) {
      if (var1 != null) {
         this.endPoint.setMessageServiceContext(this.reply, new VendorInfoCluster(var1));
      } else {
         this.reply.removeServiceContext(1111834883);
      }

   }

   private int getExceptionType(Throwable var1) {
      if (var1 instanceof SystemException) {
         return 0;
      } else if (var1 instanceof UserException) {
         return 1;
      } else if (!(var1 instanceof RuntimeException) && !(var1 instanceof Error)) {
         return var1 instanceof RemoteException ? 3 : 4;
      } else {
         return 2;
      }
   }

   private void writeUserException(UserException var1) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("writeUserException(" + var1.getClass().getName() + ")");
      }

      IIOPOutputStream var2 = (IIOPOutputStream)this.createExceptionReply();
      var2.write_IDLEntity(var1, var1.getClass());
   }

   private Class getDeclaredException(Class var1) {
      if (this.replyingTo == null) {
         return var1;
      } else {
         Class[] var2 = this.replyingTo.getExceptionTypes();
         Class var3 = null;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].isAssignableFrom(var1) && !var2[var4].equals(RemoteException.class) && (var3 == null || var3.isAssignableFrom(var2[var4]))) {
               var3 = var2[var4];
            }
         }

         if (var3 == null) {
            var3 = var1;
         }

         return var3;
      }
   }

   private void writeCheckedException(Exception var1) {
      this.reply = this.createReplyForUserException();
      this.reply.flush();
      IIOPOutputStream var2 = this.reply.getOutputStream();
      Class var3 = this.getDeclaredException(var1.getClass());
      String var4 = Utils.getIDFromException(var3);
      var2.write_string(var4);
      var2.write_value(var1, (Class)var3);
   }

   private void writeUncheckedException(Throwable var1) {
      this.reply = this.createReplyForSystemException();
      this.reply.addExceptionServiceContext(new UnknownExceptionInfo(var1));
      this.reply.flush();
      IIOPOutputStream var2 = this.reply.getOutputStream();
      var2.write_string(Utils.getRepositoryID(UNKNOWN.class));
      var2.write_long(0);
      var2.write_long(CompletionStatus.COMPLETED_MAYBE.value());
   }

   private void writeSystemException(SystemException var1) {
      this.reply = this.createReplyForSystemException();
      this.reply.flush();
      IIOPOutputStream var2 = this.reply.getOutputStream();
      String var3 = Utils.getRepositoryID(var1.getClass());
      var2.write_string(var3);
      var2.write_long(var1.minor);
      var2.write_long(var1.completed.value());
   }

   private void writeMarshalException() {
      this.reply = this.createReplyForSystemException();
      this.reply.flush();
      IIOPOutputStream var1 = this.reply.getOutputStream();
      String var2 = Utils.getRepositoryID(MARSHAL.class);
      var1.write_string(var2);
      var1.write_long(0);
      var1.write_long(CompletionStatus.COMPLETED_MAYBE.value());
   }

   public void close() throws IOException {
      this.msgOutput.close();
   }

   public ResponseHandler createResponseHandler(InboundRequest var1) {
      this.requestForReponseHandler = var1;
      return this;
   }

   public OutputStream createExceptionReply() {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("createExceptionReply()");
      }

      this.reply = this.createReplyForUserException();

      try {
         this.transferThreadLocalContext(this.requestForReponseHandler);
      } catch (IOException var2) {
         throw Utils.mapToCORBAException(var2);
      }

      this.reply.flush();
      return this.reply.getOutputStream();
   }

   public OutputStream createReply() {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("createReply()");
      }

      try {
         this.transferThreadLocalContext(this.requestForReponseHandler);
      } catch (IOException var2) {
         throw Utils.mapToCORBAException(var2);
      }

      this.reply.flush();
      return this.reply.getOutputStream();
   }

   private ReplyMessage createReplyForUserException() {
      return new ReplyMessage(this.endPoint, this.reply, 1);
   }

   private ReplyMessage createReplyForSystemException() {
      return new ReplyMessage(this.endPoint, this.reply, 2);
   }
}
