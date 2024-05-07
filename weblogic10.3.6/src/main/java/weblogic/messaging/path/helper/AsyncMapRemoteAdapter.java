package weblogic.messaging.path.helper;

import java.io.Serializable;
import java.rmi.RemoteException;
import weblogic.common.CompletionRequest;
import weblogic.messaging.path.AsyncMap;
import weblogic.messaging.path.AsyncMapRemote;
import weblogic.messaging.path.CompletionAsyncResultAdapter;
import weblogic.messaging.path.ExceptionAdapter;

class AsyncMapRemoteAdapter implements AsyncMap {
   private final String jndiName;
   private final AsyncMapRemote asyncMapRemote;
   private final ExceptionAdapter exceptionAdapter;

   AsyncMapRemoteAdapter(String var1, AsyncMapRemote var2, ExceptionAdapter var3) {
      this.jndiName = var1;
      this.asyncMapRemote = var2;
      this.exceptionAdapter = var3;
   }

   private void emergencyClose(Exception var1) {
      PathHelper.manager().handleException(var1, this.jndiName, this);
   }

   public void putIfAbsent(Object var1, Object var2, CompletionRequest var3) {
      if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
         PathHelper.PathSvcVerbose.debug("RemoteAdapter Key:" + var1 + ", value:" + var2);
      }

      try {
         this.asyncMapRemote.putIfAbsent((Serializable)var1, (Serializable)var2, (new RemoteCatcher(var3)).getCallbackableResult());
      } catch (RuntimeException var5) {
         notifyCaller(var3, var5, this.exceptionAdapter);
         throw var5;
      } catch (Error var6) {
         notifyCaller(var3, var6, this.exceptionAdapter);
         throw var6;
      } catch (RemoteException var7) {
         this.emergencyClose(var7);
         notifyCaller(var3, var7, this.exceptionAdapter);
      }
   }

   private static void notifyCaller(CompletionRequest var0, Throwable var1, ExceptionAdapter var2) {
      synchronized(var0) {
         if (var0.hasResult()) {
            return;
         }
      }

      var0.setResult(var2.wrapException(var1));
   }

   public void put(Object var1, Object var2, CompletionRequest var3) {
      try {
         this.asyncMapRemote.put((Serializable)var1, (Serializable)var2, (new RemoteCatcher(var3)).getCallbackableResult());
      } catch (RuntimeException var5) {
         notifyCaller(var3, var5, this.exceptionAdapter);
         throw var5;
      } catch (Error var6) {
         notifyCaller(var3, var6, this.exceptionAdapter);
         throw var6;
      } catch (RemoteException var7) {
         this.emergencyClose(var7);
         notifyCaller(var3, var7, this.exceptionAdapter);
      }
   }

   public void get(Object var1, CompletionRequest var2) {
      try {
         this.asyncMapRemote.get((Serializable)var1, (new RemoteCatcher(var2)).getCallbackableResult());
      } catch (RuntimeException var4) {
         notifyCaller(var2, var4, this.exceptionAdapter);
         throw var4;
      } catch (Error var5) {
         notifyCaller(var2, var5, this.exceptionAdapter);
         throw var5;
      } catch (RemoteException var6) {
         this.emergencyClose(var6);
         notifyCaller(var2, var6, this.exceptionAdapter);
      }
   }

   public void remove(Object var1, Object var2, CompletionRequest var3) {
      try {
         this.asyncMapRemote.remove((Serializable)var1, (Serializable)var2, (new RemoteCatcher(var3)).getCallbackableResult());
      } catch (RuntimeException var5) {
         notifyCaller(var3, var5, this.exceptionAdapter);
         throw var5;
      } catch (Error var6) {
         notifyCaller(var3, var6, this.exceptionAdapter);
         throw var6;
      } catch (RemoteException var7) {
         this.emergencyClose(var7);
         notifyCaller(var3, var7, this.exceptionAdapter);
      }
   }

   private class RemoteCatcher extends CompletionAsyncResultAdapter {
      AsyncMapRemoteAdapter remoteAdapter = AsyncMapRemoteAdapter.this;

      RemoteCatcher(CompletionRequest var2) {
         super(var2);
      }

      void exceptionMonitor(Throwable var1) {
         if (var1 instanceof RemoteException) {
            this.remoteAdapter.emergencyClose((RemoteException)var1);
         }

      }
   }
}
