package weblogic.servlet.internal.session;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ServletSessionRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

final class ServletSessionRuntimeManager implements SessionConstants {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Map runtimesBySessionKey;

   static final ServletSessionRuntimeManager getInstance() {
      return ServletSessionRuntimeManager.SINGLETON.instance;
   }

   private ServletSessionRuntimeManager() {
      this.runtimesBySessionKey = Collections.synchronizedMap(new HashMap(128));
   }

   static String getSessionKey(SessionData var0) {
      return var0.id + "!" + var0.creationTime;
   }

   private ServletSessionRuntimeMBean find(SessionData var1) {
      return (ServletSessionRuntimeMBean)this.runtimesBySessionKey.get(getSessionKey(var1));
   }

   ServletSessionRuntimeMBean findOrCreate(SessionData var1) {
      ServletSessionRuntimeMBean var2 = this.find(var1);
      return var2 != null ? var2 : this.create(var1);
   }

   private synchronized ServletSessionRuntimeMBean create(SessionData var1) {
      ServletSessionRuntimeMBean var2 = this.find(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = (ServletSessionRuntimeMBean)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new CreateAction(var1));
         if (var2 != null) {
            this.runtimesBySessionKey.put(getSessionKey(var1), var2);
         }

         return var2;
      }
   }

   void destroy(SessionData var1) {
      SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new DestroyAction(var1));
   }

   // $FF: synthetic method
   ServletSessionRuntimeManager(Object var1) {
      this();
   }

   private class DestroyAction implements PrivilegedAction {
      SessionData session;

      DestroyAction(SessionData var2) {
         this.session = var2;
      }

      public Object run() {
         String var1 = ServletSessionRuntimeManager.getSessionKey(this.session);

         try {
            ServletSessionRuntimeMBeanImpl var2 = (ServletSessionRuntimeMBeanImpl)ServletSessionRuntimeManager.this.runtimesBySessionKey.remove(var1);
            if (var2 == null) {
               return null;
            }

            var2.unregister();
         } catch (ManagementException var4) {
            String var3 = var1 + " was not properly unregistered.";
            HTTPSessionLogger.logErrorUnregisteringServletSessionRuntime(var3, (Throwable)null);
         }

         return null;
      }
   }

   private class CreateAction implements PrivilegedAction {
      SessionData session;

      CreateAction(SessionData var2) {
         this.session = var2;
      }

      public Object run() {
         try {
            return new ServletSessionRuntimeMBeanImpl(this.session);
         } catch (ManagementException var2) {
            HTTPSessionLogger.logErrorCreatingServletSessionRuntimeMBean(var2);
            return null;
         }
      }
   }

   private static class SINGLETON {
      static ServletSessionRuntimeManager instance = new ServletSessionRuntimeManager();
   }
}
