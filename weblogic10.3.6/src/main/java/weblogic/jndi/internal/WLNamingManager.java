package weblogic.jndi.internal;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.naming.CompositeName;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkException;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Referenceable;
import javax.naming.spi.NamingManager;
import javax.naming.spi.ObjectFactory;
import javax.naming.spi.StateFactory;
import weblogic.jndi.OpaqueReference;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServiceFailureException;

public final class WLNamingManager {
   private static List transportableFactories = null;
   private static List stateFactories = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static void initialize() throws ServiceFailureException {
      Class var0 = WLNamingManager.class;
      synchronized(WLNamingManager.class) {
         if (transportableFactories == null) {
            transportableFactories = new ArrayList();
         }
      }

      try {
         JNDIEnvironment.getJNDIEnvironment().loadTransportableFactories(transportableFactories);
      } catch (ConfigurationException var2) {
         throw new ServiceFailureException(var2);
      }
   }

   public static void addStateFactory(StateFactory var0) throws NamingException {
      if (stateFactories == null) {
         loadStateFactories();
      }

      synchronized(stateFactories) {
         stateFactories.add(0, var0);
      }
   }

   public static void addTransportableFactory(ObjectFactory var0) {
      synchronized(transportableFactories) {
         transportableFactories.add(0, var0);
      }
   }

   public static Object getObjectInstance(Object var0, Name var1, Context var2, Hashtable var3) throws NamingException {
      if (var0 instanceof OpaqueReference) {
         var0 = ((OpaqueReference)var0).getReferent(var1, var2);
      } else if (var0 instanceof LinkRef) {
         String var4 = ((LinkRef)var0).getLinkName();
         InitialContext var5 = null;

         try {
            var5 = new InitialContext(var3);
            var0 = var5.lookup(var4);
         } catch (NamingException var16) {
            LinkException var7 = new LinkException("");
            var7.setLinkRemainingName(new CompositeName(var4));
            var7.setLinkResolvedName(var1);
            var7.setLinkResolvedObj(var0);
            var7.setRootCause(var16);
            throw var7;
         } finally {
            try {
               var5.close();
            } catch (Exception var15) {
            }

         }
      }

      return var0;
   }

   public static Object getTransportableInstance(Object var0, Name var1, Context var2, Hashtable var3) throws NamingException {
      return getReplacement(transportableFactories, var0, var1, var2, var3);
   }

   public static Object getStateToBind(Object var0, Name var1, Context var2, Hashtable var3) throws NamingException {
      if (var0 instanceof WLContextImpl) {
         return ((WLContextImpl)var0).getNode();
      } else if (var0 instanceof Referenceable) {
         return ((Referenceable)var0).getReference();
      } else {
         Object var4 = NamingManager.getStateToBind(var0, var1, var2, var3);
         if (var4 == var0) {
            if (stateFactories == null) {
               loadStateFactories();
            }

            var4 = getReplacement(stateFactories, var0, var1, var2, var3);
         }

         return var4;
      }
   }

   private static Object getReplacement(List var0, Object var1, Name var2, Context var3, Hashtable var4) throws NamingException {
      Object var5 = var1;
      if (var0 == null) {
         return var1;
      } else {
         Iterator var6 = var0.iterator();

         while(var6.hasNext()) {
            Object var7 = var6.next();
            Object var8 = null;

            try {
               if (var7 instanceof ObjectFactory) {
                  var8 = ((ObjectFactory)var7).getObjectInstance(var1, var2, var3, var4);
               } else {
                  var8 = ((StateFactory)var7).getStateToBind(var1, var2, var3, var4);
               }

               if (var8 != null) {
                  var5 = var8;
                  break;
               }
            } catch (NamingException var11) {
               throw var11;
            } catch (RuntimeException var12) {
               throw var12;
            } catch (Exception var13) {
               ConfigurationException var10 = new ConfigurationException(var7.getClass().getName() + " threw an exception");
               var10.setRootCause(var13);
               throw var10;
            }
         }

         if (NamingFactoriesDebugLogger.isDebugEnabled() && var5 != var1) {
            NamingFactoriesDebugLogger.debug("Replacing " + var1.getClass().getName() + " with " + var5.getClass().getName());
         }

         return var5;
      }
   }

   private static void loadStateFactories() {
      Class var0 = WLNamingManager.class;
      synchronized(WLNamingManager.class) {
         if (stateFactories == null) {
            stateFactories = new ArrayList();
         }

      }
   }
}
