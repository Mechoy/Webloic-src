package weblogic.jndi.remote;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import weblogic.jndi.internal.WLNamingManager;
import weblogic.rmi.extensions.RemoteRuntimeException;

public final class AttributesWrapper implements RemoteAttributes {
   private Attributes delegate;
   private Hashtable env;

   protected Hashtable env() {
      return this.env;
   }

   protected Attributes delegate() {
      return this.delegate;
   }

   public AttributesWrapper(Attributes var1, Hashtable var2) {
      this.delegate = var1;
      this.env = var2;
   }

   public Object clone() {
      return this.makeTransportable(this.delegate.clone());
   }

   public Attribute get(String var1) {
      return this.makeTransportable(this.delegate.get(var1));
   }

   public NamingEnumeration getAll() {
      return this.makeTransportable(this.delegate.getAll());
   }

   public NamingEnumeration getIDs() {
      return this.makeTransportable(this.delegate.getIDs());
   }

   public boolean isCaseIgnored() {
      return this.delegate.isCaseIgnored();
   }

   public Attribute put(Attribute var1) {
      Attribute var2 = var1;
      if (var1 instanceof AttributeWrapper) {
         var2 = ((AttributeWrapper)var1).delegate();
      }

      return this.makeTransportable(this.delegate.put(var2));
   }

   public Attribute put(String var1, Object var2) {
      return this.makeTransportable(this.delegate.put(var1, var2));
   }

   public Attribute remove(String var1) {
      return this.makeTransportable(this.delegate.remove(var1));
   }

   public int size() {
      return this.delegate.size();
   }

   protected final Object makeTransportable(Object var1) {
      try {
         return WLNamingManager.getTransportableInstance(var1, (Name)null, (Context)null, this.env);
      } catch (NamingException var3) {
         throw new RemoteRuntimeException("Failed to create a transportable instance of " + var1 + " due to :", var3);
      }
   }

   protected final NamingEnumeration makeTransportable(NamingEnumeration var1) {
      return (NamingEnumeration)this.makeTransportable((Object)var1);
   }

   protected final Attribute makeTransportable(Attribute var1) {
      return (Attribute)this.makeTransportable((Object)var1);
   }
}
