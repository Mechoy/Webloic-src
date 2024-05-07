package weblogic.jndi.remote;

import java.util.Hashtable;
import javax.naming.CompositeName;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import weblogic.jndi.internal.WLNamingManager;

public class ContextWrapper implements RemoteContext {
   private static final Hashtable DEFAULT_ENV = new Hashtable();
   private Context delegate;
   private Hashtable env;

   protected Hashtable env() {
      return this.env;
   }

   public ContextWrapper(Context var1) {
      this.env = DEFAULT_ENV;
      this.delegate = var1;
   }

   public ContextWrapper(Context var1, Hashtable var2) {
      this(var1);
      this.env = var2;
   }

   public void bind(String var1, Object var2) throws NamingException {
      this.delegate.bind(var1, var2);
   }

   public void bind(Name var1, Object var2) throws NamingException {
      this.delegate.bind(var1, var2);
   }

   public void close() throws NamingException {
      this.delegate.close();
   }

   public String composeName(String var1, String var2) throws NamingException {
      return this.delegate.composeName(var1, var2);
   }

   public Name composeName(Name var1, Name var2) throws NamingException {
      return this.delegate.composeName(var1, var2);
   }

   public Context createSubcontext(String var1) throws NamingException {
      return this.createSubcontext((Name)(new CompositeName(var1)));
   }

   public Context createSubcontext(Name var1) throws NamingException {
      return this.makeTransportable(this.delegate.createSubcontext(var1));
   }

   public void destroySubcontext(String var1) throws NamingException {
      this.delegate.destroySubcontext(var1);
   }

   public void destroySubcontext(Name var1) throws NamingException {
      this.delegate.destroySubcontext(var1);
   }

   public String getNameInNamespace() throws NamingException {
      return this.delegate.getNameInNamespace();
   }

   public NameParser getNameParser(String var1) throws NamingException {
      return this.delegate.getNameParser(var1);
   }

   public NameParser getNameParser(Name var1) throws NamingException {
      return this.delegate.getNameParser(var1);
   }

   public NamingEnumeration list(String var1) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.list(var1), this.env);
   }

   public NamingEnumeration list(Name var1) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.list(var1), this.env);
   }

   public NamingEnumeration listBindings(String var1) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.listBindings(var1), this.env);
   }

   public NamingEnumeration listBindings(Name var1) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.listBindings(var1), this.env);
   }

   public Object lookup(String var1) throws NamingException {
      return this.lookup((Name)(new CompositeName(var1)));
   }

   public Object lookup(Name var1) throws NamingException {
      return this.makeTransportable(this.delegate.lookup(var1));
   }

   public Object lookupLink(String var1) throws NamingException {
      return this.lookupLink((Name)(new CompositeName(var1)));
   }

   public Object lookupLink(Name var1) throws NamingException {
      return this.makeTransportable(this.delegate.lookupLink(var1));
   }

   public void rebind(String var1, Object var2) throws NamingException {
      this.delegate.rebind(var1, var2);
   }

   public void rebind(Name var1, Object var2) throws NamingException {
      this.delegate.rebind(var1, var2);
   }

   public void rename(String var1, String var2) throws NamingException {
      this.delegate.rename(var1, var2);
   }

   public void rename(Name var1, Name var2) throws NamingException {
      this.delegate.rename(var1, var2);
   }

   public void unbind(String var1) throws NamingException {
      this.delegate.unbind(var1);
   }

   public void unbind(Name var1) throws NamingException {
      this.delegate.unbind(var1);
   }

   public Object removeFromEnvironment(String var1) throws NamingException {
      return this.delegate.removeFromEnvironment(var1);
   }

   public Object addToEnvironment(String var1, Object var2) throws NamingException {
      return this.delegate.addToEnvironment(var1, var2);
   }

   public Hashtable getEnvironment() throws NamingException {
      return this.delegate.getEnvironment();
   }

   protected final Object makeTransportable(Object var1) throws NamingException {
      return WLNamingManager.getTransportableInstance(var1, (Name)null, (Context)null, this.env);
   }

   protected final Context makeTransportable(Context var1) throws NamingException {
      try {
         return (Context)this.makeTransportable((Object)var1);
      } catch (ClassCastException var3) {
         throw new ConfigurationException("A TransportableObjectFactory converted " + var1.toString() + " into a object that does not implement Context");
      }
   }
}
