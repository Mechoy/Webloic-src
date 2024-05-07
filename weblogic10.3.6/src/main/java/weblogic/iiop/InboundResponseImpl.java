package weblogic.iiop;

import java.io.IOException;
import java.rmi.UnmarshalException;
import javax.transaction.Transaction;
import javax.transaction.TransactionRolledbackException;
import org.omg.CORBA.SystemException;
import weblogic.corba.j2ee.workarea.WorkAreaContext;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.ObjectIO;
import weblogic.rmi.spi.InboundResponse;
import weblogic.rmi.spi.MsgInput;
import weblogic.transaction.TransactionHelper;
import weblogic.utils.StackTraceUtils;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextInput;
import weblogic.workarea.spi.WorkContextMapInterceptor;

public final class InboundResponseImpl implements InboundResponse, ReplyStatusConstants {
   MsgInput msgInput;
   final EndPoint endPoint;
   final ReplyMessage reply;
   final boolean hasExcpetion;
   final RuntimeMethodDescriptor md;
   final Object[] args;
   private final boolean rmiType;

   static void p(String var0) {
      System.err.println("<InboundResponseImpl> " + var0);
   }

   public InboundResponseImpl(EndPoint var1, ReplyMessage var2, boolean var3, RuntimeMethodDescriptor var4, Object[] var5, String var6) {
      IIOPInputStream var7 = var2.getInputStream();
      var7.setPossibleCodebase(var6);
      this.rmiType = var3;
      if (var3) {
         this.msgInput = var7;
      } else {
         this.msgInput = new IDLMsgInput(var7);
      }

      this.endPoint = var1;
      this.reply = var2;
      this.hasExcpetion = var2.getReplyStatus() == 0;
      this.md = var4;
      this.args = var5;
   }

   public IOR needsForwarding() {
      return this.reply.needsForwarding() ? this.reply.getIOR() : null;
   }

   private final Throwable getThrowable() {
      Throwable var1 = this.reply.getThrowable();
      return var1 instanceof SystemException && !(this.msgInput instanceof IDLMsgInput) ? this.reply.getMappedThrowable() : var1;
   }

   public MsgInput getMsgInput() {
      return this.msgInput;
   }

   public Object unmarshalReturn() throws Throwable {
      this.retrieveThreadLocalContext();
      Throwable var1 = this.getThrowable();
      if (var1 != null) {
         if (var1 instanceof TransactionRolledbackException) {
            Transaction var8 = TransactionHelper.getTransactionHelper().getTransaction();
            if (var8 != null) {
               if (var8 instanceof weblogic.transaction.Transaction) {
                  ((weblogic.transaction.Transaction)var8).setRollbackOnly(var1);
               } else {
                  var8.setRollbackOnly();
               }
            }
         }

         throw StackTraceUtils.getThrowableWithCause(var1);
      } else {
         Class var2 = this.md.getReturnType();
         short var3 = this.md.getReturnTypeAbbrev();
         Object var4 = null;

         try {
            var4 = ObjectIO.readObject(this.getMsgInput(), var2, var3);
            return var4;
         } catch (IOException var6) {
            throw new UnmarshalException("failed to unmarshal " + var2, var6);
         } catch (ClassNotFoundException var7) {
            throw new UnmarshalException("failed to unmarshal " + var2, var7);
         }
      }
   }

   public void retrieveThreadLocalContext() throws IOException {
      WorkAreaContext var1 = (WorkAreaContext)this.endPoint.getMessageServiceContext(this.reply, 1111834891);
      WorkContextMapInterceptor var2 = WorkContextHelper.getWorkContextHelper().getInterceptor();
      if (var1 != null) {
         var2.receiveResponse(var1.getInputStream());
      } else {
         var2.receiveResponse((WorkContextInput)null);
      }

   }

   public Object getTxContext() {
      return this.endPoint.getInboundResponseTxContext(this.reply);
   }

   public Object getContext(int var1) throws IOException {
      return null;
   }

   public Object getReplicaInfo() throws IOException {
      return this.endPoint.getMessageServiceContext(this.reply, 1111834883);
   }

   public Object getActivatedPinnedRef() throws IOException {
      return null;
   }

   public void close() throws IOException {
      this.msgInput.close();
   }
}
