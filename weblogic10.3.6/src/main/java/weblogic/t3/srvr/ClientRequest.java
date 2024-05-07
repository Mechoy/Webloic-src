package weblogic.t3.srvr;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import weblogic.common.T3Exception;
import weblogic.common.T3Executable;
import weblogic.common.T3ExecutableLazy;
import weblogic.common.internal.Manufacturable;
import weblogic.common.internal.ObjectFactory;
import weblogic.kernel.T3SrvrLogger;
import weblogic.protocol.ServerChannel;
import weblogic.rjvm.RemoteRequest;
import weblogic.rjvm.ReplyStream;
import weblogic.rmi.MarshalException;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

class ClientRequest implements Runnable {
   private String clss;
   private Object payload;
   private ClientContext cc;
   private RemoteRequest req;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   ClientRequest(String var1, Object var2, ClientContext var3, RemoteRequest var4) {
      this.clss = var1;
      this.payload = var2;
      this.cc = var3;
      this.req = var4;
   }

   private void tryToSendObject(Object var1) throws IOException {
      ReplyStream var2 = this.req.getResponseStream();

      MarshalException var4;
      try {
         var2.writeObjectWL(var1);
         var2.send();
      } catch (IOException var5) {
         var4 = new MarshalException("", var5);
         T3SrvrLogger.logSendObjectMarshalFailedIO(var5);
         this.req.getResponseStream().sendThrowable(var4);
      } catch (RuntimeException var6) {
         var4 = new MarshalException("", var6);
         T3SrvrLogger.logSendObjectMarshalFailedRTE(var6);
         this.req.getResponseStream().sendThrowable(var4);
      }

   }

   public void run() {
      AuthenticatedSubject var1 = this.cc.getSubject();
      Manufacturable var2 = null;
      ServerHelper.setClientInfo(this.cc.getRJVM(), (ServerChannel)null);

      try {
         if (this.clss != null) {
            var2 = ObjectFactory.get(this.clss);
            if (var2 instanceof T3Executable) {
               Object var26 = null;
               Object var4 = null;

               try {
                  final T3Executable var5 = (T3Executable)var2;
                  var26 = SecurityServiceManager.runAs(kernelId, var1, new PrivilegedExceptionAction() {
                     public Object run() throws Exception {
                        return var5.execute(ClientRequest.this.cc, ClientRequest.this.payload);
                     }
                  });
               } catch (PrivilegedActionException var20) {
                  var4 = var20.getException();
               } catch (Throwable var21) {
                  var4 = var21;
               }

               if (var4 != null) {
                  if (!(var4 instanceof T3Exception) && !(var4 instanceof Error) && !(var4 instanceof RuntimeException)) {
                     var4 = new T3Exception("Exception executing a client request", (Throwable)var4);
                  }

                  this.req.getResponseStream().sendThrowable((Throwable)var4);
                  return;
               }

               this.tryToSendObject(var26);
               return;
            }

            if (var2 instanceof T3ExecutableLazy) {
               try {
                  final T3ExecutableLazy var3 = (T3ExecutableLazy)var2;
                  SecurityServiceManager.runAs(kernelId, var1, new PrivilegedExceptionAction() {
                     public Object run() throws Exception {
                        var3.executeLazy(ClientRequest.this.cc, ClientRequest.this.payload);
                        return null;
                     }
                  });
               } catch (PrivilegedActionException var22) {
                  T3SrvrLogger.logFailureOfT3ExecutableLazy(var22.getException());
               } catch (Throwable var23) {
                  T3SrvrLogger.logFailureOfT3ExecutableLazy(var23);
               }

               return;
            }
         }

         T3SrvrLogger.logExecutionClassNoRetrieveT3Exec(this.clss);
      } catch (IOException var24) {
         throw new RuntimeException(var24);
      } finally {
         try {
            this.req.close();
         } catch (IOException var19) {
            var19.printStackTrace();
         }

         this.req = null;
         this.cc.decWorkQueueDepth();
         if (var2 != null) {
            ObjectFactory.put(var2);
         }

      }
   }
}
