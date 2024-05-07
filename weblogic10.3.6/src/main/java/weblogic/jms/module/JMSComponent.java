package weblogic.jms.module;

import java.security.AccessController;
import weblogic.application.ApplicationContextInternal;
import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JMSComponentRuntimeMBean;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class JMSComponent extends ComponentRuntimeMBeanImpl implements JMSComponentRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Object registeredLock = new Object();
   private boolean registered;
   private int deploymentState;

   public JMSComponent(String var1, String var2, ApplicationContextInternal var3) throws ManagementException {
      super(var1, var2, var3.getRuntime(), false);
   }

   public void open() throws ManagementException {
      synchronized(this.registeredLock) {
         if (!this.registered) {
            PrivilegedActionUtilities.register(this, kernelId);
            this.registered = true;
         }
      }
   }

   public void close() throws ManagementException {
      synchronized(this.registeredLock) {
         if (this.registered) {
            PrivilegedActionUtilities.unregister(this, kernelId);
            this.registered = false;
         }
      }
   }
}
