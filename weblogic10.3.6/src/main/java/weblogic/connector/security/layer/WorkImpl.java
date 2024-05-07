package weblogic.connector.security.layer;

import java.security.AccessController;
import javax.resource.spi.work.Work;
import weblogic.connector.common.RAInstanceManager;
import weblogic.kernel.Kernel;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class WorkImpl extends SecureBaseImpl implements Work {
   private ClassLoader rarClassLoader;
   private ClassLoader origClassLoader;

   public WorkImpl(Work var1, RAInstanceManager var2, AuthenticatedSubject var3) {
      super(var1, var2, "Work", var3);
      if (var2 == null) {
         throw new AssertionError("Attempt to create Work outside the context of the Connector container");
      } else {
         this.rarClassLoader = var2.getClassloader();
      }
   }

   public void run() {
      if (Kernel.isServer()) {
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         AdapterLayer var2 = this.raIM != null ? this.raIM.getAdapterLayer() : null;

         try {
            if (var2 == null) {
               throw new AssertionError("Attempt to run Work outside the context of the Connector container");
            }

            var2.pushWorkSubject(var1);
            this.runIt();
         } finally {
            if (var2 != null) {
               var2.popSubject(var1);
            }

         }
      } else {
         this.runIt();
      }

   }

   public void release() {
      if (Kernel.isServer()) {
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         AdapterLayer var2 = this.raIM != null ? this.raIM.getAdapterLayer() : null;

         try {
            if (var2 == null) {
               throw new AssertionError("Attempt to release Work outside the context of the Connector container");
            }

            var2.pushWorkSubject(var1);
            this.releaseIt();
         } finally {
            if (var2 != null) {
               var2.popSubject(var1);
            }

         }
      } else {
         this.releaseIt();
      }

   }

   private void runIt() {
      try {
         this.setClassLoader();
         ((Work)this.myObj).run();
      } finally {
         this.resetClassLoader();
      }

   }

   private void releaseIt() {
      try {
         this.setClassLoader();
         ((Work)this.myObj).release();
      } finally {
         this.resetClassLoader();
      }

   }

   private void setClassLoader() {
      if (this.rarClassLoader != null) {
         this.origClassLoader = Thread.currentThread().getContextClassLoader();
         Thread.currentThread().setContextClassLoader(this.rarClassLoader);
      }

   }

   private void resetClassLoader() {
      if (this.rarClassLoader != null) {
         Thread.currentThread().setContextClassLoader(this.origClassLoader);
      }

   }
}
