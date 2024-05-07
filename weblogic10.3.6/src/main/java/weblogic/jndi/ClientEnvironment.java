package weblogic.jndi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.security.subject.AbstractSubject;

public interface ClientEnvironment {
   void setProviderURL(String var1);

   void setSecurityPrincipal(String var1);

   void setSecurityCredentials(Object var1);

   void setEnableServerAffinity(boolean var1);

   Context getContext() throws NamingException;

   AbstractSubject getSubject();

   Hashtable getProperties();
}
