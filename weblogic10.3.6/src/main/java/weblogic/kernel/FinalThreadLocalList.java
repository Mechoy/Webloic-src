package weblogic.kernel;

import weblogic.server.ServiceFailureException;
import weblogic.utils.Debug;

public final class FinalThreadLocalList {
   private static String[] CLASS_LIST = new String[]{"weblogic.workarea.WorkContextHelper", "weblogic.security.subject.DelegatingSubjectStack", "weblogic.transaction.internal.TransactionManagerImpl", "weblogic.trace.Trace", "weblogic.rmi.cluster.ThreadPreferredHost", "weblogic.rmi.extensions.server.ServerHelper", "weblogic.store.gxa.internal.GXAResourceImpl", "weblogic.jdbc.common.internal.ConnectionPool", "weblogic.jndi.internal.ThreadEnvironment", "weblogic.jndi.factories.java.javaURLContextFactory", "weblogic.security.acl.internal.Security", "weblogic.servlet.internal.ServletRequestImpl", "weblogic.logging.LogEntryInitializer", "weblogic.j2ee.MethodInvocationHelper", "weblogic.ejb.container.internal.AllowedMethodsHelper", "weblogic.ejb.container.internal.BaseRemoteObject", "weblogic.ejb.container.internal.CallerSubjectStack"};

   private FinalThreadLocalList() {
   }

   public static void initialize() throws ServiceFailureException {
      Debug.assertion(!FinalThreadLocal.isFinalized());

      try {
         for(int var0 = 0; var0 < CLASS_LIST.length; ++var0) {
            Class var1 = Class.forName(CLASS_LIST[var0]);
         }

      } catch (ExceptionInInitializerError var2) {
         throw new ServiceFailureException(var2.getException());
      } catch (ClassNotFoundException var3) {
         throw new ServiceFailureException(var3.getException());
      }
   }
}
