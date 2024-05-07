package weblogic.cacheprovider.coherence.jndi;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.cacheprovider.coherence.CoherenceClusterManager;
import weblogic.jndi.OpaqueReference;

public class CoherenceOpaqueReference implements OpaqueReference {
   private String objectName;
   private ClassLoader loader;
   private String type;
   private volatile Object referent;

   public CoherenceOpaqueReference(String var1, String var2, ClassLoader var3) {
      this.objectName = var1;
      this.type = var2;
      this.loader = var3;
   }

   public Object getReferent(Name var1, Context var2) throws NamingException {
      if (this.referent == null) {
         try {
            CoherenceClusterManager var3 = CoherenceClusterManager.getInstance();
            if (this.type.equals("com.tangosol.net.NamedCache")) {
               this.referent = var3.ensureCache(this.objectName, this.loader);
            } else if (this.type.equals("com.tangosol.net.Service")) {
               this.referent = var3.ensureService(this.objectName, this.loader);
            }
         } catch (Exception var5) {
            NameNotFoundException var4 = new NameNotFoundException(var5.getMessage());
            var4.initCause(var5);
            throw var4;
         }
      }

      if (this.referent != null) {
         return this.referent;
      } else {
         throw new NameNotFoundException(this + " not bound");
      }
   }

   public String toString() {
      return this.type + "::" + this.objectName;
   }
}
