package weblogic.servlet.internal;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.UnavailableException;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.http.FutureResponseModel;
import weblogic.utils.collections.StackPool;

public final class StubLifecycleHelper {
   private final ServletStubImpl stub;
   private final Class clazz;
   private final boolean isSingleThreadModel;
   private final boolean isFutureResponseServlet;
   private final ClassLoader contextLoader;
   private Servlet theServlet;
   private StackPool servletPool;
   private UnavailableException unavailException;
   private long unavailTime;
   private boolean permUnavailable = false;

   StubLifecycleHelper(ServletStubImpl var1, Class var2, ClassLoader var3) throws ServletException {
      this.stub = var1;
      this.clazz = var2;
      this.contextLoader = var3;
      this.isSingleThreadModel = SingleThreadModel.class.isAssignableFrom(this.clazz);
      this.isFutureResponseServlet = FutureResponseModel.class.isAssignableFrom(var2);
      if (this.isSingleThreadModel) {
         int var4 = this.stub.getContext().getConfigManager().getSingleThreadedServletPoolSize();
         this.servletPool = new StackPool(var4);

         for(int var5 = 0; var5 < var4; ++var5) {
            Servlet var6 = this.createOneInstance();
            this.servletPool.add(var6);
         }
      } else {
         this.theServlet = this.createOneInstance();
      }

   }

   private synchronized Servlet createOneInstance() throws ServletException {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = var1.getContextClassLoader();
      var1.setContextClassLoader(this.contextLoader);

      Servlet var3;
      try {
         var3 = this.stub.getSecurityHelper().createServlet(this.clazz);
      } catch (UnavailableException var8) {
         this.unavailException = var8;
         int var4 = this.unavailException.getUnavailableSeconds();
         if (var4 > 0) {
            this.unavailTime = System.currentTimeMillis() + (long)(var4 * 1000);
         } else {
            this.permUnavailable = true;
         }

         throw var8;
      } finally {
         var1.setContextClassLoader(var2);
      }

      return var3;
   }

   public synchronized void makeUnavailable(UnavailableException var1) {
      this.unavailException = var1;
      int var2 = this.unavailException.getUnavailableSeconds();
      if (var2 > 0) {
         this.unavailTime = System.currentTimeMillis() + (long)(var2 * 1000);
      } else {
         this.permUnavailable = true;
      }

   }

   public int getPoolCapacity() {
      return this.isSingleThreadModel ? this.servletPool.capacity() : 0;
   }

   public Servlet getServlet() throws ServletException {
      this.checkIfUnavailable();
      if (this.isSingleThreadModel) {
         Servlet var1 = (Servlet)this.servletPool.remove();
         return var1 != null ? var1 : this.createOneInstance();
      } else {
         return this.theServlet;
      }
   }

   private void checkIfUnavailable() throws UnavailableException {
      if (this.unavailException != null) {
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug(this.stub.getContext().getLogContext() + ": servlet " + this.stub.getServletName() + " is unavaialable", this.unavailException);
         }

         if (this.permUnavailable) {
            HTTPLogger.logPermUnavailable(this.stub.getContext().getLogContext(), this.stub.getServletName());
            throw this.unavailException;
         }

         if (this.unavailTime > System.currentTimeMillis()) {
            HTTPLogger.logTimeUnavailable(this.stub.getContext().getLogContext(), this.stub.getServletName(), this.unavailTime - System.currentTimeMillis() / 1000L);
            throw this.unavailException;
         }

         this.unavailException = null;
      }

   }

   public final void returnServlet(Servlet var1) {
      if (this.isSingleThreadModel) {
         if (this.clazz == var1.getClass() && this.servletPool.add(var1)) {
            return;
         }

         this.stub.getSecurityHelper().destroyServlet(var1);
      }

   }

   public synchronized void destroy() {
      if (HTTPDebugLogger.isEnabled()) {
         HTTPDebugLogger.debug("destroyServlet: Destroying all servlets named: '" + this.stub.getServletName() + "'");
      }

      if (this.isSingleThreadModel) {
         while(!this.servletPool.isEmpty()) {
            this.destroyOneInstance((Servlet)this.servletPool.remove());
         }
      } else {
         this.destroyOneInstance(this.theServlet);
      }

   }

   private void destroyOneInstance(Servlet var1) {
      Thread var2 = Thread.currentThread();
      ClassLoader var3 = var2.getContextClassLoader();
      var2.setContextClassLoader(this.contextLoader);

      try {
         this.stub.getSecurityHelper().destroyServlet(var1);
      } catch (NullPointerException var8) {
         HTTPLogger.logNPEDuringServletDestroy(this.stub.getContext().getLogContext(), this.clazz.getName(), var8);
      } finally {
         var2.setContextClassLoader(var3);
      }

   }

   public boolean isSingleThreadModel() {
      return this.isSingleThreadModel;
   }

   public boolean isFutureResponseServlet() {
      return this.isFutureResponseServlet;
   }

   public ClassLoader getContextLoader() {
      return this.contextLoader;
   }
}
