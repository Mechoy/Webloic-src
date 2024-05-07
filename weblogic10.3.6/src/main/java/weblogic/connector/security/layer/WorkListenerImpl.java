package weblogic.connector.security.layer;

import java.security.AccessController;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkListener;
import weblogic.connector.common.RAInstanceManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class WorkListenerImpl extends SecureBaseImpl implements WorkListener {
   public WorkListenerImpl(WorkListener var1, RAInstanceManager var2, AuthenticatedSubject var3) {
      super(var1, var2, "WorkListener", var3);
   }

   public void workAccepted(WorkEvent var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.raIM.getAdapterLayer().pushSubject(var2);

      try {
         ((WorkListener)this.myObj).workAccepted(var1);
      } finally {
         this.raIM.getAdapterLayer().popSubject(var2);
      }

   }

   public void workCompleted(WorkEvent var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.raIM.getAdapterLayer().pushSubject(var2);

      try {
         ((WorkListener)this.myObj).workCompleted(var1);
      } finally {
         this.raIM.getAdapterLayer().popSubject(var2);
      }

   }

   public void workRejected(WorkEvent var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.raIM.getAdapterLayer().pushSubject(var2);

      try {
         ((WorkListener)this.myObj).workRejected(var1);
      } finally {
         this.raIM.getAdapterLayer().popSubject(var2);
      }

   }

   public void workStarted(WorkEvent var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      this.raIM.getAdapterLayer().pushSubject(var2);

      try {
         ((WorkListener)this.myObj).workStarted(var1);
      } finally {
         this.raIM.getAdapterLayer().popSubject(var2);
      }

   }
}
