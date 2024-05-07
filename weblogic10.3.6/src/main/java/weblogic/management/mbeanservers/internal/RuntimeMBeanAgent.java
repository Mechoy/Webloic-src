package weblogic.management.mbeanservers.internal;

import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.OperationsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.jmx.JMXLogger;
import weblogic.management.jmx.mbeanserver.WLSMBeanServer;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;
import weblogic.management.jmx.modelmbean.WLSModelMBeanFactory;
import weblogic.management.provider.RegistrationHandler;
import weblogic.management.provider.RegistrationManager;
import weblogic.management.provider.Service;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class RuntimeMBeanAgent {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   final WLSModelMBeanContext context;
   final RegistrationManager access;

   public RuntimeMBeanAgent(WLSModelMBeanContext var1, RegistrationManager var2) {
      this.context = var1;
      this.access = var2;
      var2.initiateRegistrationHandler(this.createRegistrationHandler());
   }

   private RegistrationHandler createRegistrationHandler() {
      return new RegistrationHandler() {
         public void registeredCustom(ObjectName var1, Object var2) {
            try {
               if (RuntimeMBeanAgent.this.context.getNameManager().isClassMapped(var2.getClass())) {
                  RuntimeMBeanAgent.this.context.getNameManager().registerObject(var1, var2);
                  WLSModelMBeanFactory.registerWLSModelMBean(var2, var1, RuntimeMBeanAgent.this.context);
               } else {
                  WLSMBeanServer var3 = (WLSMBeanServer)RuntimeMBeanAgent.this.context.getMBeanServer();
                  var3.getMBeanServer().registerMBean(var2, var1);
               }
            } catch (OperationsException var4) {
               JMXLogger.logRegistrationFailed(var1, var4);
            } catch (MBeanRegistrationException var5) {
               JMXLogger.logRegistrationFailed(var1, var5);
            }

         }

         public void unregisteredCustom(final ObjectName var1) {
            SecurityServiceManager.runAs(RuntimeMBeanAgent.KERNEL_ID, RuntimeMBeanAgent.KERNEL_ID, new PrivilegedAction() {
               public Object run() {
                  Object var1x = RuntimeMBeanAgent.this.context.getNameManager().lookupObject(var1);
                  if (var1x != null) {
                     RuntimeMBeanAgent.this.context.unregister(var1x);
                  } else {
                     try {
                        WLSMBeanServer var2 = (WLSMBeanServer)RuntimeMBeanAgent.this.context.getMBeanServer();
                        var2.getMBeanServer().unregisterMBean(var1);
                     } catch (InstanceNotFoundException var3) {
                        JMXLogger.logUnregisterFailed(var1, var3);
                     } catch (MBeanRegistrationException var4) {
                        JMXLogger.logUnregisterFailed(var1, var4);
                     }
                  }

                  return null;
               }
            });
         }

         public void registered(RuntimeMBean var1, DescriptorBean var2) {
            WLSModelMBeanFactory.registerWLSModelMBean(var1, RuntimeMBeanAgent.this.context);
         }

         public void unregistered(RuntimeMBean var1) {
            this.unregisteredInternal(var1);
         }

         public void registered(Service var1) {
            WLSModelMBeanFactory.registerWLSModelMBean(var1, RuntimeMBeanAgent.this.context);
         }

         public void unregistered(Service var1) {
            this.unregisteredInternal(var1);
         }

         private void unregisteredInternal(Object var1) {
            if (RuntimeMBeanAgent.this.context.getNameManager().isClassMapped(var1.getClass())) {
               ObjectName var2 = RuntimeMBeanAgent.this.context.getNameManager().unregisterObjectInstance(var1);
               if (var2 == null) {
                  JMXLogger.logBeanUnregisterFailed(var1.toString());
               } else {
                  try {
                     MBeanServer var3 = RuntimeMBeanAgent.this.context.getMBeanServer();
                     if (var3 instanceof WLSMBeanServer) {
                        ((WLSMBeanServer)var3).internalUnregisterMBean(var2);
                     } else {
                        var3.unregisterMBean(var2);
                     }
                  } catch (InstanceNotFoundException var4) {
                  } catch (MBeanRegistrationException var5) {
                     JMXLogger.logUnregisterFailed(var2, var5);
                  }

               }
            }
         }
      };
   }
}
