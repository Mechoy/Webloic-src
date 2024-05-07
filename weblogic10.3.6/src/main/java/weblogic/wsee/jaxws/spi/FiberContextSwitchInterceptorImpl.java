package weblogic.wsee.jaxws.spi;

import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.FiberContextSwitchInterceptor;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.xml.ws.WebServiceException;
import weblogic.kernel.KernelStatus;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.subject.SubjectManager;

public class FiberContextSwitchInterceptorImpl implements FiberContextSwitchInterceptor {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static boolean isOnServer = KernelStatus.isServer();
   private final AuthenticatedSubject currentSubject;

   public FiberContextSwitchInterceptorImpl() {
      this.currentSubject = (AuthenticatedSubject)SubjectManager.getSubjectManager().getCurrentSubject(kernelId);
   }

   public <R, P> R execute(Fiber var1, final P var2, final FiberContextSwitchInterceptor.Work<R, P> var3) {
      if (!isOnServer) {
         return var3.execute(var2);
      } else {
         try {
            return SecurityServiceManager.runAs(kernelId, this.currentSubject, new PrivilegedExceptionAction() {
               public Object run() {
                  return var3.execute(var2);
               }
            });
         } catch (PrivilegedActionException var6) {
            Exception var5 = var6.getException();
            if (var5 instanceof WebServiceException) {
               throw (WebServiceException)var5;
            } else {
               throw new WebServiceException(var5);
            }
         }
      }
   }
}
