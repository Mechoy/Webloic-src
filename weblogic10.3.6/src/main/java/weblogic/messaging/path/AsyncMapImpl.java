package weblogic.messaging.path;

import java.io.Serializable;
import java.security.AccessController;
import weblogic.management.provider.ManagementService;
import weblogic.messaging.path.helper.PathHelper;
import weblogic.rmi.extensions.AsyncResult;
import weblogic.rmi.extensions.server.FutureResponse;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class AsyncMapImpl implements AsyncMapRemote {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected String jndiName;
   private final AsyncMap asyncMapDelegate;
   private final ExceptionAdapter exceptionAdapter;

   AsyncMapImpl(String var1, AsyncMap var2, ExceptionAdapter var3) {
      this.jndiName = var1;
      this.exceptionAdapter = var3;
      this.asyncMapDelegate = var2;
   }

   private static void impossible(String var0) {
      throw new Error("must not invoke this '" + var0 + "' signature on " + ManagementService.getRuntimeAccess(kernelId).getServerName());
   }

   public final void get(Serializable var1, AsyncResult var2) {
      impossible("get");
   }

   public final void get(Serializable var1, AsyncResult var2, FutureResponse var3) {
      CompletionAsyncResultAdapter var4 = new CompletionAsyncResultAdapter(var3, var2, this.exceptionAdapter);
      if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
         PathHelper.PathSvcVerbose.debug("AsyncMapImpl get " + var1);
      }

      try {
         this.asyncMapDelegate.get(var1, var4);
      } catch (RuntimeException var6) {
         notifyCaller(var4, var6);
         throw var6;
      } catch (Error var7) {
         notifyCaller(var4, var7);
         throw var7;
      }
   }

   private static void notifyCaller(CompletionAsyncResultAdapter var0, Throwable var1) {
      synchronized(var0) {
         if (var0.hasResult()) {
            return;
         }
      }

      var0.setResult(var1);
   }

   public void remove(Serializable var1, Serializable var2, AsyncResult var3) {
      impossible("remove");
   }

   public void remove(Serializable var1, Serializable var2, AsyncResult var3, FutureResponse var4) {
      CompletionAsyncResultAdapter var5 = new CompletionAsyncResultAdapter(var4, var3, this.exceptionAdapter);
      if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
         PathHelper.PathSvcVerbose.debug("AsyncMapImpl remove " + var1 + ", value:" + var2);
      }

      try {
         this.asyncMapDelegate.remove(var1, var2, var5);
      } catch (RuntimeException var7) {
         notifyCaller(var5, var7);
         throw var7;
      } catch (Error var8) {
         notifyCaller(var5, var8);
         throw var8;
      }
   }

   public void put(Serializable var1, Serializable var2, AsyncResult var3) {
      impossible("put");
   }

   public void put(Serializable var1, Serializable var2, AsyncResult var3, FutureResponse var4) {
      CompletionAsyncResultAdapter var5 = new CompletionAsyncResultAdapter(var4, var3, this.exceptionAdapter);
      if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
         PathHelper.PathSvcVerbose.debug("AsyncMapImpl put " + var1 + ", value:" + var2);
      }

      try {
         this.asyncMapDelegate.put(var1, var2, var5);
      } catch (RuntimeException var7) {
         notifyCaller(var5, var7);
         throw var7;
      } catch (Error var8) {
         notifyCaller(var5, var8);
         throw var8;
      }
   }

   public void putIfAbsent(Serializable var1, Serializable var2, AsyncResult var3) {
      impossible("putIfAbsent");
   }

   public void putIfAbsent(Serializable var1, Serializable var2, AsyncResult var3, FutureResponse var4) {
      CompletionAsyncResultAdapter var5 = new CompletionAsyncResultAdapter(var4, var3, this.exceptionAdapter);
      if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
         PathHelper.PathSvcVerbose.debug("AsyncMapImpl putIfAbsent " + var1 + ", value:" + var2);
      }

      try {
         this.asyncMapDelegate.putIfAbsent(var1, var2, var5);
      } catch (RuntimeException var7) {
         notifyCaller(var5, var7);
         throw var7;
      } catch (Error var8) {
         notifyCaller(var5, var8);
         throw var8;
      }
   }

   public String getJndiName() {
      return this.jndiName;
   }
}
