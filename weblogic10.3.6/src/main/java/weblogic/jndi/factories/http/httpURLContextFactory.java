package weblogic.jndi.factories.http;

import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;
import weblogic.jndi.WLInitialContextFactory;
import weblogic.jndi.internal.AbstractURLContext;
import weblogic.protocol.ServerURL;

public class httpURLContextFactory implements ObjectFactory {
   private static final boolean DEBUG = false;

   public Object getObjectInstance(Object var1, Name var2, Context var3, Hashtable var4) throws NamingException {
      String var5 = null;
      if (var1 instanceof String) {
         var5 = (String)var1;
      }

      return new HTTPContext(var4, var5);
   }

   private static final void p(String var0) {
      System.out.println("<httpURLContextFactory>: " + var0);
   }

   private static class HTTPContext extends AbstractURLContext {
      private Hashtable env;
      private ServerURL url;

      public HTTPContext(Hashtable var1, String var2) throws InvalidNameException {
         this.env = var1;
         if (var2 != null) {
            try {
               this.url = new ServerURL(ServerURL.DEFAULT_URL, var2);
            } catch (MalformedURLException var5) {
               InvalidNameException var4 = new InvalidNameException();
               var4.setRootCause(var5);
               throw var4;
            }
         } else {
            this.url = ServerURL.DEFAULT_URL;
         }

      }

      protected String getURL(String var1) throws InvalidNameException {
         try {
            return var1.indexOf(":") < 0 ? this.url.asUnsyncStringBuffer().append(var1).toString() : (new ServerURL(this.url, var1)).asUnsyncStringBuffer().toString();
         } catch (MalformedURLException var4) {
            InvalidNameException var3 = new InvalidNameException();
            var3.setRootCause(var4);
            throw var3;
         }
      }

      protected Context getContext(String var1) throws NamingException {
         if (this.env == null) {
            this.env = new Hashtable(5);
         }

         this.env.put("java.naming.provider.url", this.getURL(var1));
         this.env.put("java.naming.factory.initial", WLInitialContextFactory.class.getName());
         return new InitialContext(this.env);
      }
   }
}
