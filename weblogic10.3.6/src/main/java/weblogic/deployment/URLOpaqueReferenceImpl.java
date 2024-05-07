package weblogic.deployment;

import java.net.MalformedURLException;
import java.net.URL;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.jndi.OpaqueReference;
import weblogic.utils.AssertionError;

public final class URLOpaqueReferenceImpl implements OpaqueReference {
   private final String jndiName;
   private final String applicationName;

   public URLOpaqueReferenceImpl(String var1, String var2) {
      this.jndiName = var1;
      this.applicationName = var2;
   }

   public Object getReferent(Name var1, Context var2) throws NamingException {
      try {
         return new URL(this.jndiName);
      } catch (MalformedURLException var4) {
         throw new AssertionError("It should not reach here " + var4);
      }
   }
}
