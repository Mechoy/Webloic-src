package weblogic.ejb.container.swap;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.ejb.EJBContext;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import weblogic.deployment.EntityManagerFactoryProxyImpl;
import weblogic.deployment.EntityManagerInvocationHandlerFactory;
import weblogic.deployment.PersistenceUnitInfoImpl;
import weblogic.deployment.PersistenceUnitRegistry;
import weblogic.deployment.TransactionalEntityManagerProxyImpl;
import weblogic.ejb.container.deployer.EJBModule;
import weblogic.ejb.container.internal.BaseEJBLocalHome;
import weblogic.ejb.container.internal.BaseLocalObject;
import weblogic.ejb20.internal.LocalHandleImpl;
import weblogic.ejb20.internal.LocalHomeHandleImpl;
import weblogic.ejb20.swap.HandleReplacer;
import weblogic.ejb20.swap.HomeHandleReplacer;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.utils.AssertionError;
import weblogic.utils.io.Replacer;

final class EJBReplacer implements Replacer {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private static final Replacer remoteReplacer = RemoteObjectReplacer.getReplacer();
   private EJBContext ctx = null;
   private UserTransaction utx = null;

   public EJBReplacer() {
   }

   void setContext(EJBContext var1) {
      this.ctx = var1;
   }

   public Object replaceObject(Object var1) throws IOException {
      IOException var4;
      if (var1 instanceof EJBObject) {
         final EJBObject var13 = (EJBObject)var1;

         try {
            return SecurityManager.runAs(kernelId, kernelId, new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  return new HandleReplacer(var13.getHandle());
               }
            });
         } catch (PrivilegedActionException var5) {
            var4 = new IOException("Exception while replacing EO");
            var4.initCause(var5.getException());
            throw var4;
         }
      } else if (var1 instanceof EJBLocalObject) {
         BaseLocalObject var12 = (BaseLocalObject)var1;
         return var12.getLocalHandleObject();
      } else if (var1 instanceof EJBHome) {
         final EJBHome var11 = (EJBHome)var1;

         try {
            return SecurityManager.runAs(kernelId, kernelId, new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  return new HomeHandleReplacer(var11.getHomeHandle());
               }
            });
         } catch (PrivilegedActionException var6) {
            var4 = new IOException("Exception while replacing Home");
            var4.initCause(var6.getException());
            throw var4;
         }
      } else if (var1 instanceof EJBLocalHome) {
         BaseEJBLocalHome var10 = (BaseEJBLocalHome)var1;
         return var10.getLocalHomeHandleObject();
      } else if (var1 instanceof EJBContext) {
         return new weblogic.ejb20.swap.EJBReplacer.EJBContextReplacement();
      } else if (var1 instanceof Context) {
         Context var9 = (Context)var1;

         try {
            String var3 = var9.getNameInNamespace();
            return var3.indexOf("comp/env") <= 0 && var3.indexOf("comp.env") <= 0 ? var1 : new weblogic.ejb20.swap.EJBReplacer.EnvironmentContextReplacement(var3);
         } catch (NamingException var7) {
            return var1;
         }
      } else if (var1 instanceof TransactionalEntityManagerProxyImpl) {
         TransactionalEntityManagerProxyImpl var8 = (TransactionalEntityManagerProxyImpl)var1;
         return new TransactionalEntityManagerProxyReplacer(var8.getAppName(), var8.getModuleName(), var8.getUnqualifiedUnitName());
      } else if (var1 instanceof EntityManagerFactoryProxyImpl) {
         EntityManagerFactoryProxyImpl var2 = (EntityManagerFactoryProxyImpl)var1;
         return new EntityManagerFactoryReplacement(var2.getAppName(), var2.getModuleName(), var2.getUnitName());
      } else {
         return var1 instanceof UserTransaction ? new weblogic.ejb20.swap.EJBReplacer.UserTransactionReplacement() : remoteReplacer.replaceObject(remoteReplacer.replaceObject(var1));
      }
   }

   public Object resolveObject(Object var1) throws IOException {
      if (var1 instanceof HandleReplacer) {
         HandleReplacer var19 = (HandleReplacer)var1;
         return var19.getHandle().getEJBObject();
      } else if (var1 instanceof HomeHandleReplacer) {
         HomeHandleReplacer var18 = (HomeHandleReplacer)var1;
         return var18.getHomeHandle().getEJBHome();
      } else if (var1 instanceof LocalHandleImpl) {
         LocalHandleImpl var17 = (LocalHandleImpl)var1;
         return var17.getEJBLocalObject();
      } else if (var1 instanceof LocalHomeHandleImpl) {
         LocalHomeHandleImpl var16 = (LocalHomeHandleImpl)var1;
         return var16.getEJBLocalHome();
      } else if (var1 instanceof weblogic.ejb20.swap.EJBReplacer.EJBContextReplacement) {
         return this.ctx;
      } else {
         String var3;
         if (var1 instanceof weblogic.ejb20.swap.EJBReplacer.EnvironmentContextReplacement) {
            weblogic.ejb20.swap.EJBReplacer.EnvironmentContextReplacement var15 = (weblogic.ejb20.swap.EJBReplacer.EnvironmentContextReplacement)var1;
            var3 = var15.getName();
            var3 = "java:" + var3;

            try {
               InitialContext var20 = new InitialContext();
               return var20.lookup(var3);
            } catch (NamingException var11) {
               throw new AssertionError("Unexpected Exception during activation: " + var11);
            }
         } else {
            String var4;
            String var5;
            EJBModule var6;
            if (var1 instanceof TransactionalEntityManagerProxyReplacer) {
               TransactionalEntityManagerProxyReplacer var14 = (TransactionalEntityManagerProxyReplacer)var1;
               var3 = var14.getAppName();
               var4 = var14.getModuleName();
               var5 = var14.getUnitName();
               var6 = EJBModule.findModule(var3, var4);
               return EntityManagerInvocationHandlerFactory.createTransactionalEntityManagerInvocationHandler(var3, var4, var5, var6.getPersistenceUnitRegistry());
            } else if (var1 instanceof EntityManagerFactoryReplacement) {
               EntityManagerFactoryReplacement var13 = (EntityManagerFactoryReplacement)var1;
               var3 = var13.getAppName();
               var4 = var13.getModuleName();
               var5 = var13.getUnitName();
               var6 = EJBModule.findModule(var3, var4);
               PersistenceUnitRegistry var7 = var6.getPersistenceUnitRegistry();
               PersistenceUnitInfoImpl var8 = var7.getPersistenceUnit(var5);
               EntityManagerFactory var9 = var8.getEntityManagerFactory();
               EntityManagerFactoryProxyImpl var10 = (EntityManagerFactoryProxyImpl)Proxy.getInvocationHandler(var9);
               return var10;
            } else if (var1 instanceof weblogic.ejb20.swap.EJBReplacer.UserTransactionReplacement) {
               if (this.utx == null) {
                  try {
                     InitialContext var2 = new InitialContext();
                     this.utx = (UserTransaction)var2.lookup("javax.transaction.UserTransaction");
                  } catch (NamingException var12) {
                     throw new AssertionError("Unexpected Exception during activation: " + var12);
                  }
               }

               return this.utx;
            } else {
               return remoteReplacer.resolveObject(var1);
            }
         }
      }
   }

   public void insertReplacer(Replacer var1) {
   }

   public static class EntityManagerFactoryReplacement implements Serializable {
      private String appName;
      private String moduleName;
      private String unitName;

      public EntityManagerFactoryReplacement(String var1, String var2, String var3) {
         this.appName = var1;
         this.moduleName = var2;
         this.unitName = var3;
      }

      public String getAppName() {
         return this.appName;
      }

      public String getModuleName() {
         return this.moduleName;
      }

      public String getUnitName() {
         return this.unitName;
      }
   }
}
