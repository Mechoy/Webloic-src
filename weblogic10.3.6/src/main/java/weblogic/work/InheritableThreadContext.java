package weblogic.work;

import java.security.AccessController;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.jndi.factories.java.javaURLContextFactory;
import weblogic.kernel.KernelStatus;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;

public class InheritableThreadContext {
   private static final AbstractSubject kernelId = (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
   private final SubjectManager subjectManager = SubjectManager.getSubjectManager();
   private ClassLoader classLoader;
   private ClassLoader savedClassLoader;
   private Context context;
   private AbstractSubject subject;
   private int flags;
   private static final int SUBJECT_PUSHED = 1;
   private static final int CONTEXT_PUSHED = 2;
   private static final int CLASS_LOADER_PUSHED = 4;

   public static InheritableThreadContext getContext() {
      return new InheritableThreadContext();
   }

   private InheritableThreadContext() {
      this.subject = this.subjectManager.getCurrentSubject(kernelId);
      if (KernelStatus.isServer()) {
         this.classLoader = Thread.currentThread().getContextClassLoader();

         try {
            javaURLContextFactory var1 = new javaURLContextFactory();
            this.context = (Context)var1.getObjectInstance((Object)null, (Name)null, (Context)null, (Hashtable)null);
         } catch (NamingException var2) {
         }
      }

   }

   public AbstractSubject getSubject() {
      return this.subject;
   }

   public void push() {
      if (this.flags != 0) {
         throw new IllegalStateException();
      } else {
         this.subjectManager.pushSubject(kernelId, this.subject);
         this.flags |= 1;
         if (KernelStatus.isServer()) {
            javaURLContextFactory.pushContext(this.context);
            this.flags |= 2;
            Thread var1 = Thread.currentThread();
            this.savedClassLoader = var1.getContextClassLoader();
            var1.setContextClassLoader(this.classLoader);
            this.flags |= 4;
         }

      }
   }

   public void pop() {
      if ((this.flags & 1) != 0) {
         this.subjectManager.popSubject(kernelId);
         this.flags &= -2;
      }

      if ((this.flags & 2) != 0) {
         javaURLContextFactory.popContext();
         this.flags &= -3;
      }

      if ((this.flags & 4) != 0) {
         Thread.currentThread().setContextClassLoader(this.savedClassLoader);
         this.flags &= -5;
      }

   }

   public ClassLoader pushMultiThread() {
      this.subjectManager.pushSubject(kernelId, this.subject);
      if (KernelStatus.isServer()) {
         javaURLContextFactory.pushContext(this.context);
         Thread var1 = Thread.currentThread();
         ClassLoader var2 = var1.getContextClassLoader();
         var1.setContextClassLoader(this.classLoader);
         return var2;
      } else {
         return null;
      }
   }

   public void popMultiThread(ClassLoader var1) {
      this.subjectManager.popSubject(kernelId);
      if (KernelStatus.isServer()) {
         javaURLContextFactory.popContext();
         if (var1 != null) {
            Thread.currentThread().setContextClassLoader(var1);
         }
      }

   }
}
