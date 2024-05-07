package weblogic.messaging.path;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import weblogic.common.CompletionListener;
import weblogic.common.CompletionRequest;
import weblogic.messaging.path.helper.PathHelper;
import weblogic.rmi.extensions.AsyncResult;
import weblogic.rmi.extensions.AsyncResultFactory;
import weblogic.rmi.extensions.AsyncResultListener;
import weblogic.rmi.extensions.server.FutureResponse;
import weblogic.utils.StackTraceUtils;

public class CompletionAsyncResultAdapter extends CompletionRequest implements CompletionListener, AsyncResultListener {
   private final AsyncResult asyncResult;
   private final FutureResponse futureResponse;
   private final CompletionRequest completionRequest;
   private final ExceptionAdapter exceptionAdapter;
   private static final NullResult singletonNullResult = new NullResult();

   public CompletionAsyncResultAdapter(FutureResponse var1, AsyncResult var2, ExceptionAdapter var3) {
      this.asyncResult = var2;
      this.futureResponse = var1;
      this.exceptionAdapter = var3;
      this.addListener(this);
      this.completionRequest = null;
   }

   public CompletionAsyncResultAdapter(CompletionRequest var1) {
      this.completionRequest = var1;
      this.addListener(this);
      this.asyncResult = null;
      this.futureResponse = null;
      this.exceptionAdapter = null;
   }

   public AsyncResult getCallbackableResult() {
      return AsyncResultFactory.getCallbackableResult(this);
   }

   public final void handleResult(AsyncResult var1) {
      Object var2;
      try {
         var2 = var1.getObject();
         if (var2 instanceof NullResult) {
            var2 = null;
         }
      } catch (Throwable var4) {
         Throwable var3 = wrapException(this.exceptionAdapter, var4);
         this.completionRequest.setResult(var3);
         this.exceptionMonitor(var3);
         return;
      }

      if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
         PathHelper.PathSvcVerbose.debug("CompletionAsyncResultAdapter result " + var2);
      }

      if (var2 instanceof Throwable) {
         var2 = wrapException(this.exceptionAdapter, (Throwable)var2);
      }

      this.completionRequest.setResult(var2);
   }

   static Throwable unwrapException(ExceptionAdapter var0, Throwable var1) {
      if (var0 != null) {
         var1 = var0.unwrapException(var1);
      }

      return var1;
   }

   static Throwable wrapException(ExceptionAdapter var0, Throwable var1) {
      if (var0 != null) {
         var1 = var0.wrapException(var1);
      }

      return var1;
   }

   Throwable wrapException(Throwable var1) {
      return wrapException(this.exceptionAdapter, var1);
   }

   void exceptionMonitor(Throwable var1) {
   }

   public final void onException(CompletionRequest var1, Throwable var2) {
      if (PathHelper.retired && PathHelper.PathSvcVerbose.isDebugEnabled()) {
         PathHelper.PathSvc.debug("debug setResult ", var2);
      }

      this.onCompletion(var1, var2);
   }

   public final void onCompletion(CompletionRequest var1, Object var2) {
      try {
         Throwable var3;
         if (var2 instanceof Throwable) {
            var3 = unwrapException(this.exceptionAdapter, (Throwable)var2);
            var3 = StackTraceUtils.getThrowableWithCause(var3);
            var2 = var3;
         } else if (var2 == null) {
            var3 = null;
            var2 = singletonNullResult;
         } else {
            var3 = null;
         }

         if (var3 != null && this.futureResponse != null) {
            this.futureResponse.sendThrowable(var3);
         } else if (this.asyncResult != null) {
            this.asyncResult.setResult(var2);
            if (this.futureResponse != null) {
               this.futureResponse.send();
            }
         } else {
            this.futureResponse.getMsgOutput().writeObject(var2, var2.getClass());
            this.futureResponse.send();
         }
      } catch (RemoteException var5) {
         if (PathHelper.PathSvc.isDebugEnabled()) {
            PathHelper.PathSvc.debug(var5.getMessage(), var5);
         }
      } catch (IOException var6) {
         if (PathHelper.PathSvc.isDebugEnabled()) {
            PathHelper.PathSvc.debug(var6.getMessage(), var6);
         }
      }

   }

   public static final class NullResult implements Serializable {
      static final long serialVersionUID = -3666697078933257427L;
   }
}
