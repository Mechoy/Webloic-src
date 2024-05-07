package weblogic.jndi.remote;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import weblogic.jndi.internal.WLNamingManager;
import weblogic.rmi.extensions.RemoteRuntimeException;

public final class AttributeWrapper implements RemoteAttribute {
   private Attribute delegate;
   private Hashtable env;

   protected Hashtable env() {
      return this.env;
   }

   public Attribute delegate() {
      return this.delegate;
   }

   public AttributeWrapper(Attribute var1, Hashtable var2) {
      this.delegate = var1;
      this.env = var2;
   }

   public boolean add(Object var1) {
      return this.delegate.add(var1);
   }

   public void add(int var1, Object var2) {
      this.delegate.add(var1, var2);
   }

   public void clear() {
      this.delegate.clear();
   }

   public Object clone() {
      try {
         return this.makeTransportable(this.delegate.clone());
      } catch (NamingException var2) {
         throw new RemoteRuntimeException("Failed to create a transportable instance of " + this.delegate + " due to :", var2);
      }
   }

   public boolean contains(Object var1) {
      return this.delegate.contains(var1);
   }

   public Object get() throws NamingException {
      return this.makeTransportable(this.delegate.get());
   }

   public Object get(int var1) throws NamingException {
      return this.makeTransportable(this.delegate.get(var1));
   }

   public NamingEnumeration getAll() throws NamingException {
      return this.makeTransportable(this.delegate.getAll());
   }

   public DirContext getAttributeDefinition() throws NamingException {
      return this.makeTransportable(this.delegate.getAttributeDefinition());
   }

   public DirContext getAttributeSyntaxDefinition() throws NamingException {
      return this.makeTransportable(this.delegate.getAttributeSyntaxDefinition());
   }

   public String getID() {
      return this.delegate.getID();
   }

   public boolean isOrdered() {
      return this.delegate.isOrdered();
   }

   public Object remove(int var1) {
      try {
         return this.makeTransportable(this.delegate.remove(var1));
      } catch (NamingException var3) {
         return null;
      }
   }

   public boolean remove(Object var1) {
      return this.delegate.remove(var1);
   }

   public Object set(int var1, Object var2) {
      try {
         return this.makeTransportable(this.delegate.set(var1, var2));
      } catch (NamingException var4) {
         return null;
      }
   }

   public int size() {
      return this.delegate.size();
   }

   protected final Object makeTransportable(Object var1) throws NamingException {
      return WLNamingManager.getTransportableInstance(var1, (Name)null, (Context)null, this.env);
   }

   protected final NamingEnumeration makeTransportable(NamingEnumeration var1) throws NamingException {
      return (NamingEnumeration)this.makeTransportable((Object)var1);
   }

   protected final DirContext makeTransportable(DirContext var1) throws NamingException {
      return (DirContext)this.makeTransportable((Object)var1);
   }
}
