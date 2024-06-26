package weblogic.jms.safclient.agent.internal;

import java.security.AccessControlException;
import java.security.AccessController;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.omg.CORBA.COMM_FAILURE;
import weblogic.jndi.ClientEnvironment;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;

public final class ClientEnvironmentImpl implements ClientEnvironment {
   private static final String ICF = "weblogic.jndi.WLInitialContextFactory";
   private static final AbstractSubject kernelID = getKernelIdentity();
   private Hashtable properties = new Hashtable();
   private AbstractSubject subject;
   private Context context;

   private static final AbstractSubject getKernelIdentity() {
      try {
         return (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
      } catch (AccessControlException var1) {
         return null;
      }
   }

   public ClientEnvironmentImpl() {
      this.properties.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
   }

   public void setProviderURL(String var1) {
      if (var1 == null) {
         this.properties.remove("java.naming.provider.url");
      } else {
         this.properties.put("java.naming.provider.url", var1);
      }
   }

   public void setSecurityPrincipal(String var1) {
      if (var1 == null) {
         this.properties.remove("java.naming.security.principal");
      } else {
         this.properties.put("java.naming.security.principal", var1);
      }
   }

   public void setSecurityCredentials(Object var1) {
      if (var1 == null) {
         this.properties.remove("java.naming.security.credentials");
      } else {
         this.properties.put("java.naming.security.credentials", var1);
      }
   }

   public void setEnableServerAffinity(boolean var1) {
      this.properties.put("weblogic.jndi.enableServerAffinity", var1 ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
   }

   public Context getContext() throws NamingException {
      if (this.context != null) {
         return this.context;
      } else {
         try {
            this.context = new InitialContext(this.properties);
         } catch (COMM_FAILURE var2) {
            throw new NamingException(var2.getMessage());
         }

         this.subject = SubjectManager.getSubjectManager().getCurrentSubject(kernelID);
         return this.context;
      }
   }

   public AbstractSubject getSubject() {
      return this.subject;
   }

   public Hashtable getProperties() {
      return (Hashtable)this.properties.clone();
   }
}
