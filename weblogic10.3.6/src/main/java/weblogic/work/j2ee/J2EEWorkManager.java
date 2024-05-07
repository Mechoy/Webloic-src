package weblogic.work.j2ee;

import commonj.work.Work;
import commonj.work.WorkItem;
import commonj.work.WorkListener;
import commonj.work.WorkRejectedException;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.protocol.LocalServerIdentity;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.subject.AbstractSubject;
import weblogic.work.DaemonTaskWorkManager;
import weblogic.work.InheritableThreadContext;
import weblogic.work.ServerWorkAdapter;
import weblogic.work.ShutdownCallback;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;
import weblogic.work.commonj.CommonjWorkManagerImpl;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.spi.WorkContextMapInterceptor;

public final class J2EEWorkManager extends CommonjWorkManagerImpl {
   private DaemonTaskWorkManager daemonTaskWM;
   private static final J2EEWorkManager DEFAULT = new J2EEWorkManager(WorkManagerFactory.getInstance().getDefault());

   private J2EEWorkManager(WorkManager var1) {
      super(var1);
   }

   public static commonj.work.WorkManager getDefault() {
      return DEFAULT;
   }

   public static commonj.work.WorkManager getDefault(String var0) {
      ApplicationContextInternal var1 = ApplicationAccess.getApplicationAccess().getApplicationContext(var0);
      return var1 == null ? null : new J2EEWorkManager(var1.getWorkManagerCollection().getDefault());
   }

   public static commonj.work.WorkManager get(String var0, String var1, String var2) {
      ApplicationContextInternal var3 = ApplicationAccess.getApplicationAccess().getApplicationContext(var0);
      return new J2EEWorkManager(var3.getWorkManagerCollection().get(var1, var2));
   }

   public WorkItem schedule(Work var1, WorkListener var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("null work instance");
      } else {
         WorkStatus var3 = new WorkStatus(var1);
         if (var2 != null) {
            var2.workAccepted(var3);
         }

         if (var1.isDaemon()) {
            if (this.daemonTaskWM == null) {
               synchronized(this) {
                  if (this.daemonTaskWM == null) {
                     this.daemonTaskWM = new DaemonTaskWorkManager(this.getDelegate());
                  }
               }

               this.daemonTaskWM.start();
            }

            this.daemonTaskWM.schedule(new WorkWithListener(var1, var2, var3, InheritableThreadContext.getContext()));
         } else {
            this.workManager.schedule(new WorkWithListener(var1, var2, var3, InheritableThreadContext.getContext()));
         }

         return var3;
      }
   }

   public void shutdown(ShutdownCallback var1) {
      if (this.daemonTaskWM != null) {
         this.daemonTaskWM.shutdown(var1);
      }

   }

   private static final class WorkStatus extends CommonjWorkManagerImpl.WorkStatus {
      private static final long VM_DIFFERENTIATOR = LocalServerIdentity.getIdentity().getTransientIdentity().getIdentityAsLong();
      private long serverIdentity;

      private WorkStatus(Work var1) {
         super(var1);
         this.serverIdentity = VM_DIFFERENTIATOR;
      }

      public String toString() {
         return "[" + VM_DIFFERENTIATOR + "][" + this.counter + "] executing: " + this.work;
      }

      public int hashCode() {
         return (int)(this.counter ^ VM_DIFFERENTIATOR);
      }

      public int compareTo(Object var1) {
         try {
            return this.compare((WorkStatus)var1);
         } catch (ClassCastException var3) {
            return -1;
         }
      }

      public int compare(WorkStatus var1) {
         if (this.counter > var1.counter) {
            return 1;
         } else if (this.counter < var1.counter) {
            return -1;
         } else if (this.serverIdentity > var1.serverIdentity) {
            return 1;
         } else {
            return this.serverIdentity < var1.serverIdentity ? -1 : 0;
         }
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof WorkStatus)) {
            return false;
         } else {
            WorkStatus var2 = (WorkStatus)var1;
            return this.counter == var2.counter && this.serverIdentity == var2.serverIdentity;
         }
      }

      // $FF: synthetic method
      WorkStatus(Work var1, Object var2) {
         this(var1);
      }
   }

   private static final class WorkWithListener extends ServerWorkAdapter {
      private final Work work;
      private final WorkListener listener;
      private final WorkStatus status;
      private final InheritableThreadContext inheritableThreadContext;
      private WorkContextMapInterceptor workAreaContext;

      private WorkWithListener(Work var1, WorkListener var2, WorkStatus var3, InheritableThreadContext var4) {
         this.work = var1;
         this.listener = var2;
         this.status = var3;
         this.inheritableThreadContext = var4;
         this.workAreaContext = WorkContextHelper.getWorkContextHelper().getInterceptor().copyThreadContexts(2);
      }

      protected AuthenticatedSubject getAuthenticatedSubject() {
         AbstractSubject var1 = this.inheritableThreadContext.getSubject();
         return var1 instanceof AuthenticatedSubject ? (AuthenticatedSubject)var1 : null;
      }

      private boolean isAdminRequest() {
         AuthenticatedSubject var1 = this.getAuthenticatedSubject();
         return var1 != null && SubjectUtils.doesUserHaveAnyAdminRoles(var1);
      }

      public Runnable overloadAction(final String var1) {
         return this.isAdminRequest() ? null : new Runnable() {
            public void run() {
               WorkWithListener.this.status.setType(2);
               WorkWithListener.this.status.setThrowable(new WorkRejectedException(var1));

               try {
                  if (WorkWithListener.this.listener != null) {
                     WorkWithListener.this.listener.workRejected(WorkWithListener.this.status);
                  }
               } catch (Throwable var2) {
               }

            }
         };
      }

      public Runnable cancel(String var1) {
         return this.status.getStatus() != 1 ? null : this.overloadAction(var1);
      }

      public void run() {
         try {
            if (this.inheritableThreadContext != null) {
               this.inheritableThreadContext.push();
            }

            if (this.workAreaContext != null) {
               WorkContextHelper.getWorkContextHelper().getInterceptor().restoreThreadContexts(this.workAreaContext);
            }

            this.status.setType(3);

            try {
               if (this.listener != null) {
                  this.listener.workStarted(this.status);
               }
            } catch (Throwable var12) {
            }

            try {
               this.work.run();
            } catch (Throwable var11) {
               this.status.setThrowable(var11);
            }
         } finally {
            this.status.setType(4);

            try {
               if (this.listener != null) {
                  this.listener.workCompleted(this.status);
               }
            } catch (Throwable var10) {
            }

            if (this.inheritableThreadContext != null) {
               this.inheritableThreadContext.pop();
            }

         }

      }

      public void release() {
         this.work.release();
      }

      // $FF: synthetic method
      WorkWithListener(Work var1, WorkListener var2, WorkStatus var3, InheritableThreadContext var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }
}
