package weblogic.servlet;

import java.io.IOException;
import java.security.AccessController;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.servlet.internal.WebAppServletContext;

public final class AsyncInitServlet implements Servlet {
   private static final boolean DEBUG = false;
   String SERVLET_CLASS_NAME = "weblogic.servlet.AsyncInitServlet.servlet-class-name";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Servlet delegate;
   private ServletConfig config;

   private Servlet createDelegate(ServletConfig var1) throws ServletException {
      String var2 = var1.getInitParameter(this.SERVLET_CLASS_NAME);
      if (var2 == null) {
         throw new ServletException("Required init-param " + this.SERVLET_CLASS_NAME + " not found");
      } else {
         try {
            Class var3 = Class.forName(var2, true, Thread.currentThread().getContextClassLoader());
            return (Servlet)var3.newInstance();
         } catch (ClassNotFoundException var4) {
            throw new ServletException("Can't load " + this.SERVLET_CLASS_NAME + " named " + var2, var4);
         } catch (NoClassDefFoundError var5) {
            throw new ServletException("Can't load " + this.SERVLET_CLASS_NAME + " named " + var2, var5);
         } catch (ClassCastException var6) {
            throw new ServletException("Servlet class " + var2 + " does not implement javax.servlet.Servlet");
         } catch (InstantiationException var7) {
            throw new ServletException("Exception while creating servlet " + var2, var7);
         } catch (IllegalAccessException var8) {
            throw new ServletException("Servlet " + var2 + " does not have a public constructor", var8);
         }
      }
   }

   public void init(ServletConfig var1) throws ServletException {
      this.config = var1;
      if (AsyncInitServlet.DevModeSingleton.isDevMode()) {
         WebLogicServletContext var2 = (WebLogicServletContext)var1.getServletContext();
         if (((WebAppServletContext)var2).isOnDemandDisplayRefresh()) {
            this.initDelegate();
         } else {
            var2.addAsyncInitServlet(this);
         }
      } else {
         this.initDelegate();
      }

   }

   private boolean isShuttingDown() {
      return ManagementService.getRuntimeAccess(kernelId).getServerRuntime().isShuttingDown();
   }

   public void initDelegate() throws ServletException {
      if (!this.isShuttingDown()) {
         try {
            this.delegate = this.createDelegate(this.config);
            this.delegate.init(this.config);
         } catch (Throwable var2) {
            if (!this.isShuttingDown()) {
               if (var2 instanceof ServletException) {
                  throw (ServletException)var2;
               } else {
                  throw new ServletException(var2);
               }
            }
         }
      }
   }

   public ServletConfig getServletConfig() {
      return this.delegate.getServletConfig();
   }

   public void service(ServletRequest var1, ServletResponse var2) throws ServletException, IOException {
      this.delegate.service(var1, var2);
   }

   public String getServletInfo() {
      return this.delegate.getServletInfo();
   }

   public void destroy() {
      if (this.delegate != null) {
         this.delegate.destroy();
      }

   }

   private static class DevModeSingleton {
      private static DevModeSingleton SINGLETON = new DevModeSingleton();
      private final boolean isDevMode;

      private DevModeSingleton() {
         this.isDevMode = !ManagementService.getRuntimeAccess(AsyncInitServlet.kernelId).getDomain().isProductionModeEnabled();
      }

      static boolean isDevMode() {
         return SINGLETON.isDevMode;
      }
   }
}
