package weblogic.jndi.factories.java;

import java.util.Hashtable;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.event.EventContext;
import javax.naming.event.NamingListener;

public final class ReadOnlyContextWrapper implements EventContext {
   private final Context context;
   private final EventContext eventContext;

   public ReadOnlyContextWrapper(Context var1) {
      this.context = var1;
      if (var1 instanceof EventContext) {
         this.eventContext = (EventContext)var1;
      } else {
         this.eventContext = null;
      }

   }

   public String getNameInNamespace() throws NamingException {
      return this.context.getNameInNamespace();
   }

   public void close() throws NamingException {
      this.context.close();
   }

   public Object lookup(Name var1) throws NamingException {
      return this.wrapIfContext(this.context.lookup(var1));
   }

   public Object lookup(String var1) throws NamingException {
      return this.wrapIfContext(this.context.lookup(var1));
   }

   public Object lookupLink(Name var1) throws NamingException {
      return this.wrapIfContext(this.context.lookupLink(var1));
   }

   public Object lookupLink(String var1) throws NamingException {
      return this.wrapIfContext(this.context.lookupLink(var1));
   }

   public void bind(Name var1, Object var2) throws NamingException {
      throw this.newOperationNotSupportedException("bind", var1);
   }

   public void bind(String var1, Object var2) throws NamingException {
      throw this.newOperationNotSupportedException("bind", var1);
   }

   public void rebind(Name var1, Object var2) throws NamingException {
      throw this.newOperationNotSupportedException("rebind", var1);
   }

   public void rebind(String var1, Object var2) throws NamingException {
      throw this.newOperationNotSupportedException("rebind", var1);
   }

   public void unbind(Name var1) throws NamingException {
      throw this.newOperationNotSupportedException("unbind", var1);
   }

   public void unbind(String var1) throws NamingException {
      throw this.newOperationNotSupportedException("unbind", var1);
   }

   public void rename(Name var1, Name var2) throws NamingException {
      throw this.newOperationNotSupportedException("rename", var1);
   }

   public void rename(String var1, String var2) throws NamingException {
      throw this.newOperationNotSupportedException("rename", var1);
   }

   public NamingEnumeration list(Name var1) throws NamingException {
      return this.context.list(var1);
   }

   public NamingEnumeration list(String var1) throws NamingException {
      return this.context.list(var1);
   }

   public NamingEnumeration listBindings(Name var1) throws NamingException {
      return this.context.listBindings(var1);
   }

   public NamingEnumeration listBindings(String var1) throws NamingException {
      return this.context.listBindings(var1);
   }

   public NameParser getNameParser(Name var1) throws NamingException {
      return this.context.getNameParser(var1);
   }

   public NameParser getNameParser(String var1) throws NamingException {
      return this.context.getNameParser(var1);
   }

   public Name composeName(Name var1, Name var2) throws NamingException {
      return this.context.composeName(var1, var2);
   }

   public String composeName(String var1, String var2) throws NamingException {
      return this.context.composeName(var1, var2);
   }

   public Context createSubcontext(Name var1) throws NamingException {
      throw this.newOperationNotSupportedException("createSubcontext", var1);
   }

   public Context createSubcontext(String var1) throws NamingException {
      throw this.newOperationNotSupportedException("createSubcontext", var1);
   }

   public void destroySubcontext(Name var1) throws NamingException {
      throw this.newOperationNotSupportedException("destroySubcontext", var1);
   }

   public void destroySubcontext(String var1) throws NamingException {
      throw this.newOperationNotSupportedException("destroySubcontext", var1);
   }

   public Hashtable getEnvironment() throws NamingException {
      return this.context.getEnvironment();
   }

   public Object addToEnvironment(String var1, Object var2) throws NamingException {
      return this.context.addToEnvironment(var1, var2);
   }

   public Object removeFromEnvironment(String var1) throws NamingException {
      return this.context.removeFromEnvironment(var1);
   }

   public String toString() {
      return this.context.toString();
   }

   private Object wrapIfContext(Object var1) {
      return !(var1 instanceof ReadOnlyContextWrapper) && var1 instanceof Context ? new ReadOnlyContextWrapper((Context)var1) : var1;
   }

   private OperationNotSupportedException newOperationNotSupportedException(String var1, Name var2) {
      OperationNotSupportedException var3 = new OperationNotSupportedException(var1 + " not allowed in a ReadOnlyContext");
      var3.setRemainingName(var2);
      return var3;
   }

   private OperationNotSupportedException newOperationNotSupportedException(String var1, String var2) {
      CompositeName var3 = null;

      try {
         var3 = new CompositeName(var2);
      } catch (InvalidNameException var5) {
      }

      return this.newOperationNotSupportedException(var1, (Name)var3);
   }

   public void addNamingListener(Name var1, int var2, NamingListener var3) throws NamingException {
      this.addNamingListener(var1.toString(), var2, var3);
   }

   public void addNamingListener(String var1, int var2, NamingListener var3) throws NamingException {
      if (this.eventContext != null) {
         this.eventContext.addNamingListener(var1, var2, var3);
      }

   }

   public void removeNamingListener(NamingListener var1) throws NamingException {
      if (this.eventContext != null) {
         this.eventContext.removeNamingListener(var1);
      }

   }

   public boolean targetMustExist() throws NamingException {
      return true;
   }
}
