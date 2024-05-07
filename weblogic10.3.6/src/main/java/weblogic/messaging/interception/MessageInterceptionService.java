package weblogic.messaging.interception;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.messaging.interception.interfaces.InterceptionService;
import weblogic.messaging.interception.internal.InterceptionServiceImpl;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class MessageInterceptionService extends AbstractServerService {
   public static String MessageInterceptionService_JNDIName = "weblogic.MessageInterception";
   private static InterceptionService interceptionService = InterceptionServiceImpl.getInterceptionService();
   private static Context nonReplicatedCtx;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static InterceptionService getSingleton() {
      return interceptionService;
   }

   public void start() throws ServiceFailureException {
      try {
         Environment var1 = new Environment();
         var1.setCreateIntermediateContexts(true);
         var1.setReplicateBindings(false);
         nonReplicatedCtx = var1.getInitialContext();

         try {
            SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
               public Object run() throws NamingException {
                  MessageInterceptionService.nonReplicatedCtx.bind(MessageInterceptionService.MessageInterceptionService_JNDIName, MessageInterceptionService.interceptionService);
                  return null;
               }
            });
         } catch (PrivilegedActionException var3) {
            throw new NamingException(var3.toString());
         }
      } catch (NamingException var4) {
         throw new ServiceFailureException(MIExceptionLogger.logSetupJNDIExceptionLoggable(MessageInterceptionService_JNDIName).getMessage(), var4);
      }

      MILogger.logStartMessageInterceptionService();
   }
}
