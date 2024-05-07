package weblogic.iiop;

import java.rmi.RemoteException;
import java.rmi.ServerError;
import java.rmi.ServerException;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.UNKNOWN;
import org.omg.CORBA.portable.IDLEntity;
import weblogic.corba.utils.RepositoryId;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.csi.SASServiceContext;
import weblogic.kernel.Kernel;

public final class ReplyMessage extends Message implements MessageTypeConstants {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private int reply_status;
   private RepositoryId exceptionId;
   private int minorCode;
   private CompletionStatus completionStatus;
   private Throwable throwable;
   private IOR ior;

   public ReplyMessage(EndPoint var1, MessageHeader var2, IIOPInputStream var3) {
      this.endPoint = var1;
      this.msgHdr = var2;
      this.inputStream = var3;
      if (!this.isFragmented()) {
         this.flush();
      } else {
         this.request_id = var3.peek_long();
      }

   }

   public ReplyMessage(EndPoint var1, MessageHeader var2) {
      this.endPoint = var1;
      this.msgHdr = var2;
      this.addContexts();
   }

   public ReplyMessage(EndPoint var1, RequestMessage var2, ServiceContextList var3, IOR var4) {
      this(var1, var2, var3, 3);
      this.ior = var4;
   }

   private final void addContexts() {
      if (!this.endPoint.getFlag(2)) {
         this.addServiceContext(SendingContextRunTime.getSendingContextRuntime());
         this.endPoint.setFlag(2);
      }

      if (!this.endPoint.getFlag(4)) {
         this.addServiceContext(VendorInfo.VENDOR_INFO);
         this.endPoint.setFlag(4);
      }

   }

   public ReplyMessage(EndPoint var1, RequestMessage var2, ServiceContextList var3, int var4) {
      this(var1, var2.getRequestID(), var2.getMinorVersion(), var3, var4, var2.getMaxStreamFormatVersion());
   }

   public ReplyMessage(EndPoint var1, ReplyMessage var2, int var3) {
      this(var1, var2.getRequestID(), var2.getMinorVersion(), var2.getServiceContexts(), var3, var2.getMaxStreamFormatVersion());
   }

   private ReplyMessage(EndPoint var1, int var2, int var3, ServiceContextList var4, int var5, byte var6) {
      super(var4);
      this.endPoint = var1;
      this.msgHdr = new MessageHeader(1, var3);
      this.request_id = var2;
      this.reply_status = var5;
      this.setMaxStreamFormatVersion(var6);
      this.addContexts();
   }

   public IOR getIOR() {
      return this.ior;
   }

   public final int getReplyStatus() {
      return this.reply_status;
   }

   void setThrowable(Throwable var1) {
      this.throwable = var1;
   }

   public Throwable getThrowable() {
      if (this.throwable != null) {
         return this.throwable;
      } else {
         switch (this.reply_status) {
            case 0:
            default:
               break;
            case 1:
               IIOPInputStream var4 = this.getInputStream();
               Class var2 = Utils.getClassFromID(this.exceptionId);
               if (var2 == null) {
                  this.throwable = new INTERNAL("Bad UserException: " + this.exceptionId, 0, CompletionStatus.COMPLETED_MAYBE);
               } else if (IDLEntity.class.isAssignableFrom(var2)) {
                  this.throwable = (Throwable)var4.read_IDLEntity(var2);
               } else {
                  var4.read_string();
                  this.throwable = (Throwable)var4.read_value(var2);
               }
               break;
            case 2:
               try {
                  SystemException var1 = (SystemException)Utils.getClassFromID(this.exceptionId).newInstance();
                  var1.minor = this.minorCode;
                  var1.completed = this.completionStatus;
                  this.throwable = var1;
               } catch (Exception var3) {
                  this.throwable = new INTERNAL("Bad SystemException: " + this.exceptionId, 0, CompletionStatus.COMPLETED_MAYBE);
               }
         }

         return this.throwable;
      }
   }

   public Throwable getMappedThrowable() {
      Throwable var1 = this.getThrowable();
      return var1 instanceof SystemException ? this.mapSystemException((SystemException)var1) : var1;
   }

   public String getStatusAsString() {
      switch (this.reply_status) {
         case 0:
         default:
            return "SUCCESS";
         case 1:
            return "USER_EXCEPTION(" + this.exceptionId + ")";
         case 2:
            return "SYSTEM_EXCEPTION(" + this.exceptionId + ")";
         case 3:
            return "LOCATION_FORWARD";
         case 4:
            return "LOCATION_FORWARD_PERM";
         case 5:
            return "NEEDS_ADDRESSING_MODE";
      }
   }

   public final RepositoryId getExceptionId() {
      return this.exceptionId;
   }

   private final Throwable mapSystemException(SystemException var1) {
      if (var1 instanceof UNKNOWN) {
         UnknownExceptionInfo var2 = (UnknownExceptionInfo)this.getServiceContext(9);
         if (var2 != null && var2.getNestedThrowable() != null) {
            Throwable var3 = var2.getNestedThrowable();
            if (var3 instanceof Error) {
               return new ServerError("Error occurred in server thread", (Error)var3);
            }

            if (var3 instanceof RemoteException) {
               return new ServerException("RemoteException occurred in server thread", (Exception)var3);
            }

            if (var3 instanceof RuntimeException) {
               return var3;
            }
         }
      }

      return Utils.mapSystemException(var1);
   }

   public void write(IIOPOutputStream var1) {
      this.msgHdr.write(var1);
      switch (this.getMinorVersion()) {
         case 0:
         case 1:
            this.writeServiceContexts(var1);
            var1.write_long(this.request_id);
            var1.write_long(this.reply_status);
            break;
         case 2:
            var1.write_long(this.request_id);
            var1.write_long(this.reply_status);
            this.writeServiceContexts(var1);
      }

      this.alignOnEightByteBoundry(var1);
      if (this.reply_status == 3 && this.ior != null) {
         this.ior.write(var1);
      }

   }

   public void read(IIOPInputStream var1) {
      switch (this.getMinorVersion()) {
         case 0:
         case 1:
            this.readServiceContexts(var1);
            this.request_id = var1.read_long();
            this.reply_status = var1.read_long();
            break;
         case 2:
            this.request_id = var1.read_long();
            this.reply_status = var1.read_long();
            this.readServiceContexts(var1);
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("request_id = " + this.request_id + ", reply_status = " + this.reply_status);
      }

      this.alignOnEightByteBoundry(var1);
      switch (this.reply_status) {
         case 1:
            var1.mark(0);
            this.exceptionId = var1.read_repository_id();
            var1.reset();
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("exceptionId = " + this.exceptionId);
            }
            break;
         case 2:
            this.exceptionId = var1.read_repository_id();
            this.minorCode = var1.read_long();
            int var2 = var1.read_long();
            switch (var2) {
               case 0:
                  this.completionStatus = CompletionStatus.COMPLETED_YES;
                  return;
               case 1:
                  this.completionStatus = CompletionStatus.COMPLETED_NO;
                  return;
               case 2:
                  this.completionStatus = CompletionStatus.COMPLETED_MAYBE;
                  return;
               default:
                  throw new INTERNAL("BAD completion status: " + var2, 0, CompletionStatus.COMPLETED_MAYBE);
            }
         case 3:
         case 4:
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("reading ior");
            }

            this.ior = new IOR(var1, true);
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("ior = " + this.ior);
            }
      }

   }

   protected final void readServiceContexts(IIOPInputStream var1) {
      this.serviceContexts.read(var1);
      CodeSet var2 = (CodeSet)this.serviceContexts.getServiceContext(1);
      if (var2 != null) {
         var1.setCodeSets(var2.getCharCodeSet(), var2.getWcharCodeSet());
      }

      SendingContextRunTime var3 = (SendingContextRunTime)this.serviceContexts.getServiceContext(6);
      if (var3 != null) {
         this.endPoint.setRemoteCodeBase(var3.getCodeBase());
      }

      SASServiceContext var4 = (SASServiceContext)this.serviceContexts.getServiceContext(15);
      if (var4 != null) {
         var4.handleSASReply(this.endPoint);
      }

      if (this.endPoint != null && this.endPoint.getPeerInfo() == null) {
         VendorInfo var5 = (VendorInfo)this.serviceContexts.getServiceContext(1111834880);
         if (var5 != null) {
            this.endPoint.setPeerInfo(var5.getPeerInfo());
         }
      }

   }

   public final void addExceptionServiceContext(ServiceContext var1) {
      this.addServiceContext(var1);
   }

   public boolean needsForwarding() {
      switch (this.reply_status) {
         case 3:
         case 4:
            return true;
         default:
            return false;
      }
   }

   protected static void p(String var0) {
      System.err.println("<ReplyMessage> " + var0);
   }
}
