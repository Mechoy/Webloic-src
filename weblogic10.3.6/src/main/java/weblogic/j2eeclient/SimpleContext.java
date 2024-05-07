package weblogic.j2eeclient;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.naming.Binding;
import javax.naming.CompoundName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;

public final class SimpleContext implements Context {
   private static Properties syntax = new Properties();
   private static NameParser parser;
   private Map map = new HashMap();

   private Context resolve(Name var1, boolean var2) throws NamingException {
      Object var3 = this.map.get(var1.get(0));
      if (var3 instanceof Context) {
         return (Context)var3;
      } else if (var2 && var3 == null) {
         return this.createSubcontext(var1.get(0));
      } else {
         throw new NameNotFoundException("remaining name: " + var1);
      }
   }

   private Context resolve(Name var1) throws NamingException {
      return this.resolve(var1, false);
   }

   public Object lookup(Name var1) throws NamingException {
      switch (var1.size()) {
         case 0:
            return this;
         case 1:
            String var2 = var1.get(0);
            if (!this.map.containsKey(var2)) {
               throw new NameNotFoundException("remaining name: " + var1);
            }

            Object var3 = this.map.get(var2);
            if (var3 instanceof SimpleReference) {
               var3 = ((SimpleReference)var3).get();
            } else if (var3 instanceof LinkRef) {
               var3 = (new InitialContext()).lookup(((LinkRef)var3).getLinkName());
            }

            return var3;
         default:
            return this.resolve(var1).lookup(var1.getSuffix(1));
      }
   }

   public Object lookup(String var1) throws NamingException {
      return this.lookup(parser.parse(var1));
   }

   public void bind(Name var1, Object var2) throws NamingException {
      switch (var1.size()) {
         case 0:
            throw new NamingException("bind name my not be empty");
         case 1:
            this.map.put(var1.get(0), var2);
            return;
         default:
            this.resolve(var1, true).bind(var1.getSuffix(1), var2);
      }
   }

   public void bind(String var1, Object var2) throws NamingException {
      this.bind(parser.parse(var1), var2);
   }

   public void rebind(Name var1, Object var2) throws NamingException {
      switch (var1.size()) {
         case 0:
            throw new NamingException("rebind name my not be empty");
         case 1:
            this.map.put(var1.get(0), var2);
            return;
         default:
            this.resolve(var1).rebind(var1.getSuffix(1), var2);
      }
   }

   public void rebind(String var1, Object var2) throws NamingException {
      this.rebind(parser.parse(var1), var2);
   }

   public void unbind(Name var1) throws NamingException {
      switch (var1.size()) {
         case 0:
            throw new NamingException("unbind name my not be empty");
         case 1:
            this.map.remove(var1.get(0));
            return;
         default:
            this.resolve(var1).unbind(var1.getSuffix(1));
      }
   }

   public void unbind(String var1) throws NamingException {
      this.unbind(parser.parse(var1));
   }

   public void rename(Name var1, Name var2) throws NamingException {
      this.bind(var2, this.lookup(var1));
      this.unbind(var1);
   }

   public void rename(String var1, String var2) throws NamingException {
      this.bind(var2, this.lookup(var1));
      this.unbind(var1);
   }

   public NamingEnumeration list(Name var1) throws NamingException {
      return (NamingEnumeration)(var1.isEmpty() ? new NamingEnumerationBase(this.map.entrySet().iterator()) {
         public Object nextElement() {
            Map.Entry var1 = (Map.Entry)this.i.next();
            Object var2 = var1.getValue();
            return new NameClassPair(var1.getKey().toString(), var2 == null ? null : var2.getClass().getName());
         }
      } : this.resolve(var1).list(var1.getSuffix(1)));
   }

   public NamingEnumeration list(String var1) throws NamingException {
      return this.list(parser.parse(var1));
   }

   public NamingEnumeration listBindings(Name var1) throws NamingException {
      return (NamingEnumeration)(var1.isEmpty() ? new NamingEnumerationBase(this.map.entrySet().iterator()) {
         public Object nextElement() {
            Map.Entry var1 = (Map.Entry)this.i.next();
            return new Binding(var1.getKey().toString(), var1.getValue());
         }
      } : this.resolve(var1).listBindings(var1.getSuffix(1)));
   }

   public NamingEnumeration listBindings(String var1) throws NamingException {
      return this.listBindings(parser.parse(var1));
   }

   public void destroySubcontext(Name var1) throws NamingException {
      switch (var1.size()) {
         case 0:
            throw new NamingException("destroySubcontext name may not be empty");
         case 1:
            this.map.remove(var1.get(0));
            return;
         default:
            this.resolve(var1).destroySubcontext(var1.getSuffix(1));
      }
   }

   public void destroySubcontext(String var1) throws NamingException {
      this.destroySubcontext(parser.parse(var1));
   }

   public Context createSubcontext(Name var1) throws NamingException {
      switch (var1.size()) {
         case 0:
            throw new NamingException("createSubcontext name may not be empty");
         case 1:
            SimpleContext var2 = new SimpleContext();
            this.map.put(var1.get(0), var2);
            return var2;
         default:
            return this.resolve(var1).createSubcontext(var1.getSuffix(1));
      }
   }

   public Context createSubcontext(String var1) throws NamingException {
      return this.createSubcontext(parser.parse(var1));
   }

   public Object lookupLink(Name var1) throws NamingException {
      throw new OperationNotSupportedException();
   }

   public Object lookupLink(String var1) throws NamingException {
      throw new OperationNotSupportedException();
   }

   public NameParser getNameParser(Name var1) throws NamingException {
      return var1.isEmpty() ? parser : this.resolve(var1).getNameParser(var1.getSuffix(1));
   }

   public NameParser getNameParser(String var1) throws NamingException {
      return this.getNameParser(parser.parse(var1));
   }

   public Name composeName(Name var1, Name var2) throws NamingException {
      throw new OperationNotSupportedException();
   }

   public String composeName(String var1, String var2) throws NamingException {
      throw new OperationNotSupportedException();
   }

   public Object addToEnvironment(String var1, Object var2) throws NamingException {
      throw new OperationNotSupportedException();
   }

   public Object removeFromEnvironment(String var1) throws NamingException {
      throw new OperationNotSupportedException();
   }

   public Hashtable getEnvironment() throws NamingException {
      return null;
   }

   public void close() throws NamingException {
   }

   public String getNameInNamespace() throws NamingException {
      throw new OperationNotSupportedException();
   }

   static {
      syntax.put("jndi.syntax.direction", "left_to_right");
      syntax.put("jndi.syntax.separator", "/");
      parser = new NameParser() {
         public Name parse(String var1) throws NamingException {
            return new CompoundName(var1, SimpleContext.syntax);
         }
      };
   }

   abstract static class NamingEnumerationBase implements NamingEnumeration {
      protected Iterator i;

      protected NamingEnumerationBase(Iterator var1) {
         this.i = var1;
      }

      public Object next() throws NamingException {
         return this.nextElement();
      }

      public boolean hasMore() throws NamingException {
         return this.hasMoreElements();
      }

      public boolean hasMoreElements() {
         return this.i.hasNext();
      }

      public void close() {
      }
   }

   public interface SimpleReference {
      Object get() throws NamingException;
   }
}
