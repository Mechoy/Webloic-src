package weblogic.ejb.container.internal;

import java.io.Serializable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.ejb.container.interfaces.Ejb3StatefulHome;
import weblogic.jndi.OpaqueReference;
import weblogic.rmi.extensions.activation.Activator;

public final class OpaqueReferenceImpl implements OpaqueReference, Serializable {
   private final Ejb3StatefulHome home;
   private final String businessIntfName;
   private final transient Activator activator;
   private final transient Class iface;
   private final transient Class businessImplClass;
   private final transient Class generatedRemoteInterface;

   public OpaqueReferenceImpl(Ejb3StatefulHome var1, Class var2, Activator var3, Class var4, Class var5) {
      this.home = var1;
      this.businessImplClass = var2;
      this.activator = var3;
      this.iface = var4;
      this.generatedRemoteInterface = var5;
      this.businessIntfName = var4.getName();
   }

   public Class getBusinessIntfClass() {
      return this.iface;
   }

   public Activator getActivator() {
      return this.activator;
   }

   public Class getBusinessImplClass() {
      return this.businessImplClass;
   }

   public Class getGeneratedRemoteInterface() {
      return this.generatedRemoteInterface;
   }

   public Object getReferent(Name var1, Context var2) throws NamingException {
      try {
         return this.iface != null ? this.home.getBusinessImpl(this.businessImplClass, this.activator, this.iface, this.generatedRemoteInterface) : this.home.getBusinessImpl(this.businessIntfName);
      } catch (Exception var6) {
         Object var4 = var6;
         if (var6.getCause() != null) {
            var4 = var6.getCause();
         }

         NamingException var5 = new NamingException(((Throwable)var4).toString());
         var5.setRootCause((Throwable)var4);
         throw var5;
      }
   }
}
