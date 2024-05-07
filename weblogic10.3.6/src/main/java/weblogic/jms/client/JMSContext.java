package weblogic.jms.client;

import java.security.AccessController;
import javax.naming.Context;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.kernel.KernelStatus;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;

public class JMSContext {
   private static final AbstractSubject KERNEL_ID = (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
   private static final SubjectManager subjectManager = SubjectManager.getSubjectManager();
   protected ClassLoader classLoader;
   protected Context context;
   protected AbstractSubject subject;
   private static boolean isWLSServerSet;
   private static boolean isWLSServer;

   public JMSContext() {
      this.subject = subjectManager.getCurrentSubject(KERNEL_ID);
      if (KernelStatus.isServer()) {
         this.classLoader = Thread.currentThread().getContextClassLoader();
         if (isWLSServer()) {
            this.context = this.getLocalJNDIContext();
         }
      }

   }

   private static synchronized boolean isWLSServer() {
      if (isWLSServerSet) {
         return isWLSServer;
      } else {
         try {
            Class.forName("weblogic.jms.common.JMSServerUtilities");
            isWLSServer = true;
         } catch (ClassNotFoundException var1) {
         }

         isWLSServerSet = true;
         return isWLSServer;
      }
   }

   public Context getContext() {
      return this.context;
   }

   public void setContext(Context var1) {
      this.context = var1;
   }

   public ClassLoader getClassLoader() {
      return this.classLoader;
   }

   public void setClassLoader(ClassLoader var1) {
      this.classLoader = var1;
   }

   public AbstractSubject getSubject() {
      return this.subject;
   }

   public void setSubject(AbstractSubject var1) {
      this.subject = var1;
   }

   public AbstractSubject getKernelId() {
      return KERNEL_ID;
   }

   public SubjectManager getSubjectManager() {
      return subjectManager;
   }

   static JMSContext push(JMSContext var0) {
      return push((Thread)null, var0, false);
   }

   static JMSContext push(JMSContext var0, boolean var1) {
      return push((Thread)null, var0, var1);
   }

   static void pop(JMSContext var0) {
      pop((Thread)null, var0, false);
   }

   static void pop(JMSContext var0, boolean var1) {
      pop((Thread)null, var0, var1);
   }

   static JMSContext push(Thread var0, JMSContext var1, boolean var2) {
      if (var2 && var1 != null) {
         pushSubject(var1.getSubject());
      }

      if (KernelStatus.isServer()) {
         Thread var3 = var0 != null ? var0 : Thread.currentThread();
         JMSContext var4 = new JMSContext();
         if (var1 != null) {
            ClassLoader var5 = var1.getClassLoader();
            if (var5 != var4.getClassLoader()) {
               var3.setContextClassLoader(var5);
            }

            if (isWLSServer() && var1.getContext() != null) {
               pushLocalJNDIContext(var1.getContext());
            }
         }

         return var4;
      } else {
         return null;
      }
   }

   static void pop(Thread var0, JMSContext var1, boolean var2) {
      if (KernelStatus.isServer()) {
         Thread var3 = var0 != null ? var0 : Thread.currentThread();
         if (var1 != null) {
            if (isWLSServer() && var1.getContext() != null) {
               popLocalJNDIContext();
            }

            var3.setContextClassLoader(var1.getClassLoader());
         }
      }

      if (var2) {
         popSubject();
      }

   }

   static void pushSubject(AbstractSubject var0) {
      subjectManager.pushSubject(KERNEL_ID, var0);
   }

   static void popSubject() {
      subjectManager.popSubject(KERNEL_ID);
   }

   private Context getLocalJNDIContext() {
      return JMSServerUtilities.getLocalJNDIContext();
   }

   private static void pushLocalJNDIContext(Context var0) {
      JMSServerUtilities.pushLocalJNDIContext(var0);
   }

   private static void popLocalJNDIContext() {
      JMSServerUtilities.popLocalJNDIContext();
   }

   static boolean equals(JMSContext var0, JMSContext var1) {
      if (var0.getClassLoader() != var1.getClassLoader()) {
         return false;
      } else if (var0.getContext() != var1.getContext()) {
         return false;
      } else {
         return var0.getSubject() == var1.getSubject();
      }
   }
}
