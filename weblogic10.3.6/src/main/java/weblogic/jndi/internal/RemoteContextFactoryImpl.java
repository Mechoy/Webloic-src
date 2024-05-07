package weblogic.jndi.internal;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.NotContextException;
import javax.naming.spi.InitialContextFactory;

public class RemoteContextFactoryImpl implements RemoteContextFactory {
   public Context getContext(Hashtable var1, String var2) throws NamingException {
      Hashtable var4 = null;

      try {
         if (var1 != null) {
            var4 = (Hashtable)var1.get("weblogic.jndi.delegate.environment");
         }
      } catch (ClassCastException var12) {
         throw new NoInitialContextException("Property weblogic.jndi.delegate.environment (" + var1.get("weblogic.jndi.delegate.environment") + ") " + "is not an instance of Hashtable");
      }

      Context var3;
      if (var4 == null) {
         var3 = RootNamingNode.getSingleton().getContext(var1);
      } else {
         String var5 = (String)var4.get("java.naming.factory.initial");
         if (var5 == null) {
            throw new NoInitialContextException("Property weblogic.jndi.delegate.environment.java.naming.factory.initial is null");
         }

         InitialContextFactory var6;
         try {
            var6 = (InitialContextFactory)Class.forName(var5).newInstance();
         } catch (ClassNotFoundException var9) {
            throw new NoInitialContextException("Failed to find weblogic.jndi.delegate.environment.java.naming.factory.initial (" + var5 + ") " + "on server");
         } catch (ClassCastException var10) {
            throw new NoInitialContextException("Class specified by weblogic.jndi.delegate.environment.java.naming.factory.initial (" + var5 + ") " + "does not implement InitialContextFactory");
         } catch (Exception var11) {
            NoInitialContextException var8 = new NoInitialContextException("Failed to instantiate weblogic.jndi.delegate.environment.java.naming.factory.initial (" + var5 + ") " + "on server");
            var8.setRootCause(var11);
            throw var8;
         }

         var3 = var6.getInitialContext(var4);
      }

      if (var2 != null) {
         Object var13 = var3.lookup(var2);
         if (!(var13 instanceof Context)) {
            throw new NotContextException(var2 + " is no a context");
         }

         var3 = (Context)var13;
      } else {
         var3 = (Context)WLNamingManager.getTransportableInstance(var3, (Name)null, (Context)null, var1);
      }

      return var3;
   }

   static void initialize() {
   }
}
