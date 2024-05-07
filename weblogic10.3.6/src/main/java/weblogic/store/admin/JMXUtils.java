package weblogic.store.admin;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import weblogic.management.ManagementException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentStoreConnection;
import weblogic.store.PersistentStoreException;
import weblogic.store.internal.PersistentStoreImpl;

public class JMXUtils {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   static PersistentStoreRuntimeMBeanImpl createStoreMBean(final PersistentStoreImpl var0) throws PersistentStoreException {
      try {
         return (PersistentStoreRuntimeMBeanImpl)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws ManagementException {
               return new PersistentStoreRuntimeMBeanImpl(var0);
            }
         });
      } catch (PrivilegedActionException var3) {
         Exception var2 = var3.getException();
         if (var2 instanceof ManagementException) {
            throw new PersistentStoreException(var2);
         } else if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         } else {
            throw new AssertionError(var2);
         }
      }
   }

   static void unregisterStoreMBean(final PersistentStoreRuntimeMBeanImpl var0) throws PersistentStoreException {
      try {
         SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws ManagementException {
               var0.unregister();
               return null;
            }
         });
      } catch (PrivilegedActionException var3) {
         Exception var2 = var3.getException();
         if (var2 instanceof ManagementException) {
            throw new PersistentStoreException(var2);
         } else if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         } else {
            throw new AssertionError(var2);
         }
      }
   }

   static void registerConnectionMBean(final PersistentStoreRuntimeMBeanImpl var0, final PersistentStoreConnection var1) throws PersistentStoreException {
      try {
         SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws ManagementException {
               var0.addConnection(new PersistentStoreConnectionRuntimeMBeanImpl(var1, var0));
               return null;
            }
         });
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         if (var3 instanceof ManagementException) {
            throw new PersistentStoreException(var3);
         } else if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new AssertionError(var3);
         }
      }
   }

   static void unregisterConnectionMBean(final PersistentStoreRuntimeMBeanImpl var0, final PersistentStoreConnection var1) throws PersistentStoreException {
      try {
         SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws ManagementException {
               var0.removeConnection(var1);
               return null;
            }
         });
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         if (var3 instanceof ManagementException) {
            throw new PersistentStoreException(var3);
         } else if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new AssertionError(var3);
         }
      }
   }
}
