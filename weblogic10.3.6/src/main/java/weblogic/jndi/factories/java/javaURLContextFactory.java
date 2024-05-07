package weblogic.jndi.factories.java;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;
import weblogic.corba.j2ee.naming.ORBHelper;
import weblogic.ejb20.internal.HandleDelegateImpl;
import weblogic.j2eeclient.SimpleContext;
import weblogic.jndi.internal.AbstractURLContext;
import weblogic.kernel.KernelStatus;
import weblogic.kernel.ThreadLocalStack;
import weblogic.management.internal.SecurityHelper;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.transaction.TransactionHelper;
import weblogic.work.j2ee.J2EEWorkManager;
import weblogic.workarea.WorkContextHelper;

public final class javaURLContextFactory implements ObjectFactory {
   private static Object runtimeMBeanServer = null;
   private static Object domainRuntimeMBeanServer = null;
   private static ThreadLocalStack threadContext = new ThreadLocalStack(true);

   public static void setRuntimeMBeanServer(Object var0) {
      if (runtimeMBeanServer != null) {
         throw new AssertionError("The RuntimeMBeanServer can only be established once.");
      } else {
         runtimeMBeanServer = var0;
      }
   }

   public static void setDomainRuntimeMBeanServer(Object var0) {
      if (domainRuntimeMBeanServer != null) {
         throw new AssertionError("The DomainRuntimeMBeanServer can only be establised once.");
      } else {
         domainRuntimeMBeanServer = var0;
      }
   }

   public static void pushContext(Context var0) {
      threadContext.push(var0);
   }

   public static void popContext() {
      threadContext.pop();
   }

   public static Context getDefaultContext(AuthenticatedSubject var0) {
      SecurityHelper.assertIfNotKernel(var0);
      return javaURLContextFactory.DefaultContextMaker.DEFAULT_CONTEXT;
   }

   public Object getObjectInstance(Object var1, Name var2, Context var3, Hashtable var4) throws NamingException {
      Object var5 = (Context)threadContext.peek();
      if (var5 == null) {
         var5 = javaURLContextFactory.DefaultContextMaker.DEFAULT_CONTEXT;
      }

      if (var5 instanceof JavaURLContext) {
         return var5;
      } else {
         if (!(var5 instanceof ReadOnlyContextWrapper)) {
            var5 = new ReadOnlyContextWrapper((Context)var5);
         }

         return new JavaURLContext((Context)var5, var4);
      }
   }

   public static void main(String[] var0) throws Exception {
      (new javaURLContextFactory()).getObjectInstance((Object)null, (Name)null, (Context)null, (Hashtable)null);
   }

   private static class JavaURLContext extends AbstractURLContext {
      private Context actualCtx;

      public JavaURLContext(Context var1, Hashtable var2) {
         this.actualCtx = var1;
      }

      protected String removeURL(String var1) throws InvalidNameException {
         return !var1.startsWith("java:comp/") && !var1.startsWith("java:global/") ? super.removeURL(var1) : var1.substring(5);
      }

      protected Context getContext(String var1) throws NamingException {
         return this.actualCtx;
      }
   }

   private static final class DefaultContextMaker {
      private static final Context DEFAULT_CONTEXT = createDefaultContext();

      private static final Context createDefaultContext() {
         SimpleContext var0 = new SimpleContext();
         Object var1 = null;

         try {
            Context var2 = var0.createSubcontext("comp");
            var2.bind("UserTransaction", new SimpleContext.SimpleReference() {
               public Object get() throws NamingException {
                  return TransactionHelper.getTransactionHelper().getUserTransaction();
               }
            });
            var2.bind("TransactionSynchronizationRegistry", new SimpleContext.SimpleReference() {
               public Object get() throws NamingException {
                  return TransactionHelper.getTransactionHelper().getTransactionManager();
               }
            });
            if (!KernelStatus.isServer()) {
               var1 = new HandleDelegateImpl();
            } else {
               var1 = new weblogic.ejb20.portable.HandleDelegateImpl();
               Context var3 = var2.createSubcontext("jmx");
               var3.bind("runtime", new SimpleContext.SimpleReference() {
                  public Object get() throws NamingException {
                     return javaURLContextFactory.runtimeMBeanServer;
                  }
               });
               var3.bind("domainRuntime", new SimpleContext.SimpleReference() {
                  public Object get() throws NamingException {
                     return javaURLContextFactory.domainRuntimeMBeanServer;
                  }
               });
               Context var4 = var0.createSubcontext("global");
               Context var5 = var4.createSubcontext("wm");
               var5.bind("default", new SimpleContext.SimpleReference() {
                  public Object get() throws NamingException {
                     return J2EEWorkManager.getDefault();
                  }
               });
            }

            var2.bind("HandleDelegate", var1);
            WorkContextHelper.bind(var2);
            DisconnectMonitorListImpl.bindToJNDI(var2);
            var2.bind("ORB", new SimpleContext.SimpleReference() {
               public Object get() throws NamingException {
                  return ORBHelper.getORBHelper().getLocalORB();
               }
            });
            return var0;
         } catch (NamingException var6) {
            throw new AssertionError(var6);
         }
      }
   }
}
