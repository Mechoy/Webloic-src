package weblogic.application.internal.flow;

import java.security.AccessController;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.Flow;
import weblogic.jndi.factories.java.javaURLContextFactory;
import weblogic.jndi.internal.ApplicationNamingInfo;
import weblogic.jndi.internal.ApplicationNamingNode;
import weblogic.kernel.Kernel;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class EnvContextFlow extends BaseFlow implements Flow {
   private static Hashtable env = new Hashtable(2);
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public EnvContextFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() {
      if (Kernel.isServer()) {
         ApplicationNamingInfo var1 = new ApplicationNamingInfo();

         try {
            Context var2 = null;
            Context var3 = (new ApplicationNamingNode("/", var1)).getContext(env);
            this.appCtx.setRootContext(var3);
            Context var4 = javaURLContextFactory.getDefaultContext(kernelId);
            Object var5 = var4.lookup("comp");
            var3.bind("comp", var5);
            var2 = var3.createSubcontext("app");
            this.appCtx.setEnvContext(var2);
         } catch (NamingException var6) {
            throw new AssertionError(var6);
         }
      }
   }

   static {
      env.put("weblogic.jndi.createIntermediateContexts", "true");
      env.put("weblogic.jndi.replicateBindings", "false");
   }
}
