package weblogic.jndi.internal;

import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import weblogic.protocol.ServerURL;

public abstract class AbstractURLContext implements Context {
   protected abstract Context getContext(String var1) throws NamingException;

   protected String removeURL(String var1) throws InvalidNameException {
      try {
         return var1.indexOf(":") < 0 ? var1 : (new ServerURL(ServerURL.DEFAULT_URL, var1)).getFile();
      } catch (MalformedURLException var4) {
         InvalidNameException var3 = new InvalidNameException();
         var3.setRootCause(var4);
         throw var3;
      }
   }

   public final Object addToEnvironment(String var1, Object var2) throws NamingException {
      throw new OperationNotSupportedException();
   }

   public final void bind(String var1, Object var2) throws NamingException {
      this.getContext(var1).bind(this.removeURL(var1), var2);
   }

   public final void bind(Name var1, Object var2) throws NamingException {
      this.getContext(var1.toString()).bind(this.removeURL(var1.toString()), var2);
   }

   public final void close() throws NamingException {
      throw new OperationNotSupportedException();
   }

   public final String composeName(String var1, String var2) throws NamingException {
      return var1;
   }

   public final Name composeName(Name var1, Name var2) throws NamingException {
      return (Name)var1.clone();
   }

   public final Context createSubcontext(String var1) throws NamingException {
      return this.getContext(var1).createSubcontext(this.removeURL(var1));
   }

   public final Context createSubcontext(Name var1) throws NamingException {
      return this.getContext(var1.toString()).createSubcontext(this.removeURL(var1.toString()));
   }

   public final void destroySubcontext(String var1) throws NamingException {
      this.getContext(var1).destroySubcontext(this.removeURL(var1));
   }

   public final void destroySubcontext(Name var1) throws NamingException {
      this.getContext(var1.toString()).destroySubcontext(this.removeURL(var1.toString()));
   }

   public final Hashtable getEnvironment() throws NamingException {
      throw new OperationNotSupportedException();
   }

   public final String getNameInNamespace() throws NamingException {
      throw new OperationNotSupportedException();
   }

   public final NameParser getNameParser(String var1) throws NamingException {
      throw new OperationNotSupportedException();
   }

   public final NameParser getNameParser(Name var1) throws NamingException {
      throw new OperationNotSupportedException();
   }

   public final NamingEnumeration list(String var1) throws NamingException {
      return this.getContext(var1).list(this.removeURL(var1));
   }

   public final NamingEnumeration list(Name var1) throws NamingException {
      return this.getContext(var1.toString()).list(this.removeURL(var1.toString()));
   }

   public final NamingEnumeration listBindings(String var1) throws NamingException {
      return this.getContext(var1).listBindings(this.removeURL(var1));
   }

   public final NamingEnumeration listBindings(Name var1) throws NamingException {
      return this.getContext(var1.toString()).listBindings(this.removeURL(var1.toString()));
   }

   public final Object lookup(String var1) throws NamingException {
      return this.getContext(var1).lookup(this.removeURL(var1));
   }

   public final Object lookup(Name var1) throws NamingException {
      return this.getContext(var1.toString()).lookup(this.removeURL(var1.toString()));
   }

   public final Object lookupLink(String var1) throws NamingException {
      return this.getContext(var1).lookupLink(this.removeURL(var1));
   }

   public final Object lookupLink(Name var1) throws NamingException {
      return this.getContext(var1.toString()).lookupLink(this.removeURL(var1.toString()));
   }

   public final void rebind(String var1, Object var2) throws NamingException {
      this.getContext(var1).rebind(this.removeURL(var1), var2);
   }

   public final void rebind(Name var1, Object var2) throws NamingException {
      this.getContext(var1.toString()).rebind(this.removeURL(var1.toString()), var2);
   }

   public final Object removeFromEnvironment(String var1) throws NamingException {
      throw new OperationNotSupportedException();
   }

   public final void rename(String var1, String var2) throws NamingException {
      this.getContext(var1).rename(this.removeURL(var1), var2);
   }

   public final void rename(Name var1, Name var2) throws NamingException {
      this.getContext(var1.toString()).rename(this.removeURL(var1.toString()), var2.toString());
   }

   public final void unbind(String var1) throws NamingException {
      this.getContext(var1).unbind(this.removeURL(var1));
   }

   public final void unbind(Name var1) throws NamingException {
      this.getContext(var1.toString()).unbind(this.removeURL(var1.toString()));
   }
}
