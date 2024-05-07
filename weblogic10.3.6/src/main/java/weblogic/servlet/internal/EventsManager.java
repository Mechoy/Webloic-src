package weblogic.servlet.internal;

import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import weblogic.j2ee.descriptor.ListenerBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.WebLogicServletContextListener;
import weblogic.servlet.internal.session.SharedSessionData;

public final class EventsManager {
   private final List wlCtxListeners = new ArrayList();
   private final List ctxListeners = new ArrayList();
   private final List ctxAttrListeners = new ArrayList();
   private final List sessListeners = new ArrayList();
   private final List sessAttrListeners = new ArrayList();
   private final List requestListeners = new ArrayList();
   private final List requestAttrListeners = new ArrayList();
   private final WebAppServletContext context;
   private boolean hasRequestListeners = false;
   private RegistrationListener regListener;
   private static final String[] internalListeners = new String[]{"weblogic.wsee.deploy.ServletDeployListener", "weblogic.security.internal.SAMLServletContextListener"};
   private static final String internalSessAttrListener = "weblogic.servlet.internal.session.WLSessionAttributeChangedListener";

   public EventsManager(WebAppServletContext var1) {
      this.context = var1;
   }

   boolean hasRequestListeners() {
      return this.hasRequestListeners;
   }

   void registerPreparePhaseListeners() throws DeploymentException {
      this.registerEventListeners(true);
   }

   void registerEventListeners() throws DeploymentException {
      this.registerEventListeners(false);
   }

   private void registerEventListeners(boolean var1) throws DeploymentException {
      String var2 = null;

      try {
         for(int var3 = 0; var3 < internalListeners.length; ++var3) {
            var2 = internalListeners[var3];
            Class var10 = Class.forName(var2);
            if (WebLogicServletContextListener.class.isAssignableFrom(var10) == var1) {
               this.registerEventListener(internalListeners[var3], var1);
            }
         }

         if (!var1) {
            this.context.getServer();
            if (!HttpServer.isProductionModeEnabled()) {
               this.registerEventListener("weblogic.servlet.internal.session.WLSessionAttributeChangedListener", var1);
            }
         }

         if (this.context.getHelper() != null) {
            Set var8 = this.context.getHelper().getTagListeners(false);
            if (var8 != null && !var8.isEmpty()) {
               Iterator var11 = var8.iterator();

               while(var11.hasNext()) {
                  var2 = (String)var11.next();
                  Class var5 = this.context.getServletClassLoader().loadClass(var2);
                  if (WebLogicServletContextListener.class.isAssignableFrom(var5) == var1) {
                     this.registerEventListener(var2, var1);
                  }
               }
            }
         }

         if (this.context.getWebAppModule() != null) {
            WebAppBean var9 = this.context.getWebAppModule().getWebAppBean();
            if (var9 != null) {
               ListenerBean[] var12 = var9.getListeners();
               if (var12 != null) {
                  for(int var13 = 0; var13 < var12.length; ++var13) {
                     var2 = var12[var13].getListenerClass();
                     Class var6 = this.context.getServletClassLoader().loadClass(var2);
                     if (WebLogicServletContextListener.class.isAssignableFrom(var6) == var1) {
                        this.registerEventListener(var12[var13].getListenerClass(), var1);
                     }
                  }
               }
            }
         }

      } catch (ClassNotFoundException var7) {
         Loggable var4 = HTTPLogger.logCouldNotLoadListenerLoggable(var2, var7);
         var4.log();
         throw new DeploymentException(var4.getMessage(), var7);
      }
   }

   void registerEventsListeners(List var1) throws DeploymentException {
      if (var1 != null && !var1.isEmpty()) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            this.registerEventListener(var3);
         }

      }
   }

   synchronized void registerEventListener(String var1) throws DeploymentException {
      this.registerEventListener(var1, false);
   }

   void notifyContextPreparedEvent() throws DeploymentException {
      Iterator var1 = this.wlCtxListeners.iterator();

      WebLogicServletContextListener var2;
      Throwable var6;
      do {
         if (!var1.hasNext()) {
            return;
         }

         var2 = (WebLogicServletContextListener)var1.next();
         ServletContextEvent var3 = new ServletContextEvent(this.context);
         FireContextPreparedAction var4 = new FireContextPreparedAction(var2, var3);
         AuthenticatedSubject var5 = this.context.getApplicationContext().getDeploymentInitiator();
         if (var5 == null) {
            var5 = SubjectUtils.getAnonymousSubject();
         }

         var6 = (Throwable)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, var5, var4);
      } while(var6 == null);

      HTTPLogger.logListenerFailed(var2.getClass().getName(), var6);
      throw new DeploymentException(var6);
   }

   void notifyContextCreatedEvent() throws DeploymentException {
      Iterator var1 = this.ctxListeners.iterator();

      ServletContextListener var2;
      Throwable var6;
      do {
         if (!var1.hasNext()) {
            return;
         }

         var2 = (ServletContextListener)var1.next();
         ServletContextEvent var3 = new ServletContextEvent(this.context);
         FireContextListenerAction var4 = new FireContextListenerAction(true, var2, var3);
         AuthenticatedSubject var5 = this.context.getApplicationContext().getDeploymentInitiator();
         if (var5 == null) {
            var5 = SubjectUtils.getAnonymousSubject();
         }

         var6 = (Throwable)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, var5, var4);
      } while(var6 == null);

      HTTPLogger.logListenerFailed(var2.getClass().getName(), var6);
      throw new DeploymentException(var6);
   }

   void notifyContextDestroyedEvent() {
      for(int var1 = this.ctxListeners.size() - 1; var1 >= 0; --var1) {
         ServletContextListener var2 = (ServletContextListener)this.ctxListeners.get(var1);
         ServletContextEvent var3 = new ServletContextEvent(this.context);
         FireContextListenerAction var4 = new FireContextListenerAction(false, var2, var3);
         AuthenticatedSubject var5 = this.context.getApplicationContext().getDeploymentInitiator();
         if (var5 == null) {
            var5 = SubjectUtils.getAnonymousSubject();
         }

         Throwable var6 = (Throwable)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, var5, var4);
         if (var6 != null) {
            HTTPLogger.logListenerFailed(var2.getClass().getName(), var6);
         }
      }

      this.destroyListeners(this.wlCtxListeners);
      this.destroyListeners(this.ctxListeners);
      this.destroyListeners(this.ctxAttrListeners);
      this.destroyListeners(this.sessListeners);
      this.destroyListeners(this.sessAttrListeners);
      this.destroyListeners(this.requestListeners);
      this.destroyListeners(this.requestAttrListeners);
   }

   private void destroyListeners(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.context.getComponentCreator().notifyPreDestroy(var2.next());
      }

      this.notifyEventListenersRemoved(var1);
      var1.clear();
   }

   void notifyContextAttributeChange(String var1, Object var2, Object var3) {
      var3 = this.unwrapAttribute(var3);
      Iterator var4 = this.ctxAttrListeners.iterator();

      while(var4.hasNext()) {
         ServletContextAttributeListener var5 = (ServletContextAttributeListener)var4.next();
         if (var3 == null) {
            if (var2 != null) {
               var5.attributeAdded(new ServletContextAttributeEvent(this.context, var1, var2));
            }
         } else if (var2 == null) {
            var5.attributeRemoved(new ServletContextAttributeEvent(this.context, var1, var3));
         } else {
            if (var3.equals(var2)) {
               return;
            }

            var5.attributeReplaced(new ServletContextAttributeEvent(this.context, var1, var3));
         }
      }

   }

   private Object unwrapAttribute(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         Object var2 = null;

         try {
            if (var1 instanceof AttributeWrapper) {
               var2 = ((AttributeWrapper)var1).getObject(this.context);
            } else {
               var2 = var1;
            }
         } catch (ClassNotFoundException var4) {
            HTTPLogger.logUnableToDeserializeAttribute(this.context.getLogContext(), var4);
         } catch (IOException var5) {
            HTTPLogger.logUnableToDeserializeAttribute(this.context.getLogContext(), var5);
         } catch (RuntimeException var6) {
            HTTPLogger.logUnableToDeserializeAttribute(this.context.getLogContext(), var6);
         }

         return var2;
      }
   }

   public void notifySessionLifetimeEvent(HttpSession var1, boolean var2) {
      Iterator var3 = this.sessListeners.iterator();

      while(var3.hasNext()) {
         HttpSessionListener var4 = (HttpSessionListener)var3.next();

         try {
            if (var2) {
               var4.sessionCreated(new HttpSessionEvent(var1));
            } else {
               var4.sessionDestroyed(new HttpSessionEvent(var1));
            }
         } catch (Throwable var6) {
            HTTPLogger.logListenerFailed(var4.getClass().getName(), var6);
         }
      }

      if (this.context.getSessionContext().getConfigMgr().isSessionSharingEnabled() && !(var1 instanceof SharedSessionData)) {
         WebAppServletContext[] var7 = this.context.getServer().getServletContextManager().getAllContexts();
         if (var7 == null || var7.length < 2) {
            return;
         }

         for(int var5 = 0; var5 < var7.length; ++var5) {
            if (var7[var5] != this.context) {
               var7[var5].getEventsManager().notifySessionLifetimeEvent(new SharedSessionData(var1, var7[var5]), var2);
            }
         }
      }

   }

   public void notifySessionAttributeChange(HttpSession var1, String var2, Object var3, Object var4) {
      Iterator var5 = this.sessAttrListeners.iterator();

      while(var5.hasNext()) {
         HttpSessionAttributeListener var6 = (HttpSessionAttributeListener)var5.next();
         if (var3 == null) {
            if (var4 != null) {
               var6.attributeAdded(new HttpSessionBindingEvent(var1, var2, var4));
            }
         } else if (var4 == null) {
            var6.attributeRemoved(new HttpSessionBindingEvent(var1, var2, var3));
         } else {
            var6.attributeReplaced(new HttpSessionBindingEvent(var1, var2, var3));
         }
      }

      if (this.context.getSessionContext().getConfigMgr().isSessionSharingEnabled() && !(var1 instanceof SharedSessionData)) {
         WebAppServletContext[] var8 = this.context.getServer().getServletContextManager().getAllContexts();
         if (var8 == null || var8.length < 2) {
            return;
         }

         for(int var7 = 0; var7 < var8.length; ++var7) {
            if (var8[var7] != this.context) {
               var8[var7].getEventsManager().notifySessionAttributeChange(new SharedSessionData(var1, var8[var7]), var2, var3, var4);
            }
         }
      }

   }

   void notifyRequestLifetimeEvent(ServletRequest var1, boolean var2) {
      Iterator var3 = this.requestListeners.iterator();

      while(var3.hasNext()) {
         ServletRequestListener var4 = (ServletRequestListener)var3.next();
         if (var2) {
            var4.requestInitialized(new ServletRequestEvent(this.context, var1));
         } else {
            var4.requestDestroyed(new ServletRequestEvent(this.context, var1));
         }
      }

   }

   void notifyRequestAttributeEvent(ServletRequest var1, String var2, Object var3, Object var4) {
      var3 = this.unwrapAttribute(var3);
      Iterator var5 = this.requestAttrListeners.iterator();

      while(var5.hasNext()) {
         ServletRequestAttributeListener var6 = (ServletRequestAttributeListener)var5.next();
         if (var3 == null) {
            if (var4 != null) {
               var6.attributeAdded(new ServletRequestAttributeEvent(this.context, var1, var2, var4));
            }
         } else if (var4 == null) {
            var6.attributeRemoved(new ServletRequestAttributeEvent(this.context, var1, var2, var3));
         } else {
            var6.attributeReplaced(new ServletRequestAttributeEvent(this.context, var1, var2, var3));
         }
      }

   }

   private synchronized void registerEventListener(String var1, boolean var2) throws DeploymentException {
      if (var1 == null) {
         throw new DeploymentException("listener-class is null");
      } else {
         Loggable var4;
         try {
            Object var3;
            if (!var2) {
               var3 = this.context.getComponentCreator().createListenerInstance(var1);
            } else {
               Class var10 = this.context.getServletClassLoader().loadClass(var1);
               var3 = var10.newInstance();
            }

            boolean var11 = false;
            if (var3 instanceof WebLogicServletContextListener) {
               this.wlCtxListeners.add(var3);
               var11 = true;
            }

            if (var3 instanceof ServletContextListener) {
               this.ctxListeners.add(var3);
               var11 = true;
            }

            if (var3 instanceof ServletContextAttributeListener) {
               this.ctxAttrListeners.add(var3);
               var11 = true;
            }

            if (var3 instanceof HttpSessionListener) {
               this.sessListeners.add(var3);
               var11 = true;
            }

            if (var3 instanceof HttpSessionAttributeListener) {
               this.sessAttrListeners.add(var3);
               var11 = true;
            }

            if (var3 instanceof ServletRequestListener) {
               this.requestListeners.add(var3);
               this.hasRequestListeners = true;
               var11 = true;
            }

            if (var3 instanceof ServletRequestAttributeListener) {
               this.requestAttrListeners.add(var3);
               this.hasRequestListeners = true;
               var11 = true;
            }

            if (var11) {
               this.notifyEventListenerAdded(var3);
            } else {
               HTTPLogger.logNotAListener(var3.getClass().getName());
            }

         } catch (ClassNotFoundException var5) {
            var4 = HTTPLogger.logCouldNotLoadListenerLoggable(var1, var5);
            var4.log();
            throw new DeploymentException(var4.getMessage(), var5);
         } catch (NoClassDefFoundError var6) {
            var4 = HTTPLogger.logCouldNotLoadListenerLoggable(var1, var6);
            var4.log();
            throw new DeploymentException(var4.getMessage(), var6);
         } catch (InstantiationException var7) {
            var4 = HTTPLogger.logCouldNotLoadListenerLoggable(var1, var7);
            var4.log();
            throw new DeploymentException(var4.getMessage(), var7);
         } catch (IllegalAccessException var8) {
            var4 = HTTPLogger.logCouldNotLoadListenerLoggable(var1, var8);
            var4.log();
            throw new DeploymentException(var4.getMessage(), var8);
         } catch (ClassCastException var9) {
            var4 = HTTPLogger.logCouldNotLoadListenerLoggable(var1, var9);
            var4.log();
            throw new DeploymentException(var4.getMessage(), var9);
         }
      }
   }

   public boolean isListenerRegistered(String var1) {
      Iterator var2 = this.ctxListeners.iterator();
      if (this.contains(var2, var1)) {
         return true;
      } else {
         var2 = this.wlCtxListeners.iterator();
         if (this.contains(var2, var1)) {
            return true;
         } else {
            var2 = this.ctxAttrListeners.iterator();
            if (this.contains(var2, var1)) {
               return true;
            } else {
               var2 = this.sessListeners.iterator();
               if (this.contains(var2, var1)) {
                  return true;
               } else {
                  var2 = this.sessAttrListeners.iterator();
                  if (this.contains(var2, var1)) {
                     return true;
                  } else {
                     var2 = this.requestListeners.iterator();
                     if (this.contains(var2, var1)) {
                        return true;
                     } else {
                        var2 = this.requestAttrListeners.iterator();
                        return this.contains(var2, var1);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean contains(Iterator var1, String var2) {
      while(true) {
         if (var1.hasNext()) {
            if (!var2.equals(var1.next().getClass().getName())) {
               continue;
            }

            return true;
         }

         return false;
      }
   }

   public synchronized void setRegistrationListener(RegistrationListener var1) {
      this.regListener = var1;
      this.notifyEventListenersAdded(this.wlCtxListeners);
      this.notifyEventListenersAdded(this.ctxListeners);
      this.notifyEventListenersAdded(this.ctxAttrListeners);
      this.notifyEventListenersAdded(this.sessListeners);
      this.notifyEventListenersAdded(this.sessAttrListeners);
      this.notifyEventListenersAdded(this.requestListeners);
      this.notifyEventListenersAdded(this.requestAttrListeners);
   }

   private void notifyEventListenersAdded(List var1) {
      if (this.regListener != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            this.notifyEventListenerAdded(var2.next());
         }
      }

   }

   private void notifyEventListenerAdded(Object var1) {
      RegistrationListener var2 = this.regListener;
      if (var2 != null) {
         var2.eventListenerAdded(var1);
      }

   }

   private void notifyEventListenersRemoved(List var1) {
      if (this.regListener != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            this.notifyEventListenerRemoved(var2.next());
         }
      }

   }

   private void notifyEventListenerRemoved(Object var1) {
      RegistrationListener var2 = this.regListener;
      if (var2 != null) {
         var2.eventListenerRemoved(var1);
      }

   }

   public interface RegistrationListener {
      void eventListenerAdded(Object var1);

      void eventListenerRemoved(Object var1);
   }

   private static final class FireContextPreparedAction implements PrivilegedAction {
      private final WebLogicServletContextListener listener;
      private final ServletContextEvent event;

      FireContextPreparedAction(WebLogicServletContextListener var1, ServletContextEvent var2) {
         this.listener = var1;
         this.event = var2;
      }

      public Object run() {
         try {
            this.listener.contextPrepared(this.event);
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }
   }

   private static final class FireContextListenerAction implements PrivilegedAction {
      private final ServletContextListener listener;
      private final ServletContextEvent event;
      private final boolean init;

      FireContextListenerAction(boolean var1, ServletContextListener var2, ServletContextEvent var3) {
         this.listener = var2;
         this.event = var3;
         this.init = var1;
      }

      public Object run() {
         try {
            if (this.init) {
               this.listener.contextInitialized(this.event);
            } else {
               this.listener.contextDestroyed(this.event);
            }
         } catch (Throwable var2) {
            if (this.init) {
               return var2;
            }

            HTTPLogger.logListenerFailed(this.listener.getClass().getName(), var2);
         }

         return null;
      }
   }
}
