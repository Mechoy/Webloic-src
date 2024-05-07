package weblogic.jndi.remote;

import java.util.Hashtable;
import javax.naming.CompositeName;
import javax.naming.ConfigurationException;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

public final class DirContextWrapper extends ContextWrapper implements RemoteDirContext {
   private DirContext delegate;

   public DirContextWrapper(DirContext var1) {
      super(var1);
      this.delegate = var1;
   }

   public DirContextWrapper(DirContext var1, Hashtable var2) {
      super(var1, var2);
      this.delegate = var1;
   }

   public void bind(String var1, Object var2, Attributes var3) throws NamingException {
      this.delegate.bind(var1, var2, var3);
   }

   public void bind(Name var1, Object var2, Attributes var3) throws NamingException {
      this.delegate.bind(var1, var2, var3);
   }

   public DirContext createSubcontext(String var1, Attributes var2) throws NamingException {
      return this.createSubcontext((Name)(new CompositeName(var1)), var2);
   }

   public DirContext createSubcontext(Name var1, Attributes var2) throws NamingException {
      return this.makeTransportable(this.delegate.createSubcontext(var1, var2));
   }

   public Attributes getAttributes(String var1) throws NamingException {
      return (Attributes)this.makeTransportable(this.delegate.getAttributes(var1));
   }

   public Attributes getAttributes(Name var1) throws NamingException {
      return (Attributes)this.makeTransportable(this.delegate.getAttributes(var1));
   }

   public Attributes getAttributes(String var1, String[] var2) throws NamingException {
      return (Attributes)this.makeTransportable(this.delegate.getAttributes(var1, var2));
   }

   public Attributes getAttributes(Name var1, String[] var2) throws NamingException {
      return this.delegate.getAttributes(var1, var2);
   }

   public void modifyAttributes(String var1, int var2, Attributes var3) throws NamingException {
      this.delegate.modifyAttributes(var1, var2, var3);
   }

   public void modifyAttributes(Name var1, int var2, Attributes var3) throws NamingException {
      this.delegate.modifyAttributes(var1, var2, var3);
   }

   public void modifyAttributes(String var1, ModificationItem[] var2) throws NamingException {
      this.delegate.modifyAttributes(var1, var2);
   }

   public void modifyAttributes(Name var1, ModificationItem[] var2) throws NamingException {
      this.delegate.modifyAttributes(var1, var2);
   }

   public void rebind(String var1, Object var2, Attributes var3) throws NamingException {
      this.delegate.rebind(var1, var2, var3);
   }

   public void rebind(Name var1, Object var2, Attributes var3) throws NamingException {
      this.delegate.rebind(var1, var2, var3);
   }

   public DirContext getSchema(String var1) throws NamingException {
      return this.getSchema((Name)(new CompositeName(var1)));
   }

   public DirContext getSchema(Name var1) throws NamingException {
      return this.makeTransportable(this.delegate.getSchema(var1));
   }

   public DirContext getSchemaClassDefinition(String var1) throws NamingException {
      return this.getSchemaClassDefinition((Name)(new CompositeName(var1)));
   }

   public DirContext getSchemaClassDefinition(Name var1) throws NamingException {
      return this.makeTransportable(this.delegate.getSchemaClassDefinition(var1));
   }

   public NamingEnumeration search(String var1, Attributes var2) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.search(var1, var2), this.env());
   }

   public NamingEnumeration search(Name var1, Attributes var2) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.search(var1, var2), this.env());
   }

   public NamingEnumeration search(String var1, Attributes var2, String[] var3) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.search(var1, var2, var3), this.env());
   }

   public NamingEnumeration search(Name var1, Attributes var2, String[] var3) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.search(var1, var2, var3), this.env());
   }

   public NamingEnumeration search(String var1, String var2, SearchControls var3) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.search(var1, var2, var3), this.env());
   }

   public NamingEnumeration search(Name var1, String var2, SearchControls var3) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.search(var1, var2, var3), this.env());
   }

   public NamingEnumeration search(String var1, String var2, Object[] var3, SearchControls var4) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.search(var1, var2, var3, var4), this.env());
   }

   public NamingEnumeration search(Name var1, String var2, Object[] var3, SearchControls var4) throws NamingException {
      return new NamingEnumerationWrapper(this.delegate.search(var1, var2, var3, var4), this.env());
   }

   protected final DirContext makeTransportable(DirContext var1) throws NamingException {
      try {
         return (DirContext)this.makeTransportable(var1);
      } catch (ClassCastException var3) {
         throw new ConfigurationException("A TransportableObjectFactory converted " + var1.toString() + " into a object that does not implement DirContext");
      }
   }
}
