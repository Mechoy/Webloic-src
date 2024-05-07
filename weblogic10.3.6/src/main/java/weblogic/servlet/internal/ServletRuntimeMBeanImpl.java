package weblogic.servlet.internal;

import java.io.PrintStream;
import java.net.MalformedURLException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServletRuntimeMBean;
import weblogic.protocol.ServerURL;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.utils.URLMapping;
import weblogic.utils.concurrent.atomic.AtomicFactory;
import weblogic.utils.concurrent.atomic.AtomicInteger;
import weblogic.utils.concurrent.atomic.AtomicLong;

public final class ServletRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ServletRuntimeMBean {
   private static final long serialVersionUID = -7688216863798470821L;
   private int reloadCount;
   private final AtomicInteger invokeCount;
   private final AtomicLong totalExecutionTime;
   private long highExecutionTime;
   private long lowExecutionTime;
   private ServletStubImpl stub;

   private ServletRuntimeMBeanImpl(String var1, ServletStubImpl var2) throws ManagementException {
      super(var1, var2.getContext().getRuntimeMBean(), true, "Servlets");
      this.invokeCount = AtomicFactory.createAtomicInteger();
      this.totalExecutionTime = AtomicFactory.createAtomicLong();
      this.lowExecutionTime = -1L;
      this.stub = var2;
   }

   public static ServletRuntimeMBeanImpl newInstance(final ServletStubImpl var0) throws ManagementException {
      try {
         return (ServletRuntimeMBeanImpl)SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, WebAppConfigManager.KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return new ServletRuntimeMBeanImpl(var0.getServletName(), var0);
            }
         });
      } catch (PrivilegedActionException var3) {
         Exception var2 = var3.getException();
         if (var2 instanceof ManagementException) {
            throw (ManagementException)var2;
         } else {
            return null;
         }
      }
   }

   public void destroy() {
      try {
         SecurityServiceManager.runAs(WebAppConfigManager.KERNEL_ID, WebAppConfigManager.KERNEL_ID, new PrivilegedExceptionAction() {
            public Object run() throws ManagementException {
               ServletRuntimeMBeanImpl.this.unregister();
               return null;
            }
         });
      } catch (PrivilegedActionException var3) {
         Exception var2 = var3.getException();
         if (var2 instanceof ManagementException) {
            HTTPLogger.logErrorUnregisteringServletRuntime(this.getObjectName(), var2);
         }
      }

   }

   public String getServletName() {
      return this.stub.getServletName();
   }

   public String getServletClassName() {
      return this.stub.getClassName();
   }

   void incReloadCount() {
      ++this.reloadCount;
   }

   public int getReloadTotalCount() {
      return this.reloadCount;
   }

   void incInvocationCount() {
      this.invokeCount.incrementAndGet();
   }

   public int getInvocationTotalCount() {
      return this.invokeCount.get();
   }

   public int getPoolMaxCapacity() {
      StubLifecycleHelper var1 = this.stub.getLifecycleHelper();
      return var1 == null ? 0 : var1.getPoolCapacity();
   }

   void addExecutionTimeTotal(long var1) {
      this.totalExecutionTime.addAndGet(var1);
   }

   public long getExecutionTimeTotal() {
      return this.totalExecutionTime.get();
   }

   void setExecutionTimeHighLow(long var1) {
      if (var1 > this.highExecutionTime) {
         this.highExecutionTime = var1;
      }

      if (var1 < this.lowExecutionTime || this.lowExecutionTime < 0L) {
         this.lowExecutionTime = var1;
      }

   }

   public int getExecutionTimeHigh() {
      return (int)this.highExecutionTime;
   }

   public int getExecutionTimeLow() {
      return (int)(this.lowExecutionTime < 0L ? 0L : this.lowExecutionTime);
   }

   public int getExecutionTimeAverage() {
      int var1 = this.invokeCount.get();
      return var1 <= 0 ? 0 : (int)(this.getExecutionTimeTotal() / (long)var1);
   }

   public String getServletPath() {
      URLMapping var1 = this.getServletContext().getServletMapping();
      Object[] var2 = var1.values();
      if (var2 == null) {
         return null;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            URLMatchHelper var4 = (URLMatchHelper)var2[var3];
            if (var4.getServletStub() == this.stub) {
               return var4.getPattern();
            }
         }

         return null;
      }
   }

   public String getContextPath() {
      return this.getServletContext().getContextPath();
   }

   public String getURL() {
      String var1 = this.getServletPath();
      if (var1 == null) {
         return null;
      } else {
         HttpServer var2 = this.getServletContext().getServer();
         String var3 = var2.getVirtualHostNames()[0];
         if (var3 == null) {
            var3 = var2.getListenAddress();
         }

         String var4 = this.getContextPath();
         if (var4.equals("/")) {
            var4 = "";
         }

         String var5 = ChannelHelper.getURL(ProtocolHandlerHTTP.PROTOCOL_HTTP);
         if (var5 == null) {
            return null;
         } else {
            ServerURL var6;
            try {
               var6 = new ServerURL(var5);
            } catch (MalformedURLException var8) {
               return null;
            }

            int var7 = var6.getPort();
            if (var6.getProtocol().equalsIgnoreCase("http") && var7 == 80) {
               var7 = -1;
            }

            return var6.getProtocol() + "://" + var3 + (var7 == -1 ? "" : ":" + var7) + var4 + var1;
         }
      }
   }

   public String[] getURLPatterns() {
      URLMapping var1 = this.getServletContext().getServletMapping();
      ArrayList var2 = new ArrayList();
      Object[] var3 = var1.values();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            URLMatchHelper var5 = (URLMatchHelper)var3[var4];
            if (var5 != null && var5.getServletStub() == this.stub) {
               var2.add(var5.getPattern());
            }
         }
      }

      return (String[])((String[])var2.toArray(new String[var2.size()]));
   }

   public boolean isInternalServlet() {
      return this.stub.isInternalServlet();
   }

   private WebAppServletContext getServletContext() {
      return (WebAppServletContext)this.stub.getServletContext();
   }

   public static void dumpServlet(PrintStream var0, ServletRuntimeMBean var1) {
      println(var0, "        SERVLET NAME: " + var1.getName());
      println(var0, "        ReloadTotalCount: " + var1.getReloadTotalCount());
      println(var0, "        InvocationTotalCount: " + var1.getInvocationTotalCount());
      println(var0, "        PoolMaxCapacity: " + var1.getPoolMaxCapacity());
      println(var0, "        ExecutionTimeTotal: " + var1.getExecutionTimeTotal());
      println(var0, "        ExecutionTimeHigh: " + var1.getExecutionTimeHigh());
      println(var0, "        ExecutionTimeLow: " + var1.getExecutionTimeLow());
      println(var0, "        ExecutionTimeAverage: " + var1.getExecutionTimeAverage());
   }

   private static void println(PrintStream var0, String var1) {
      var0.println(var1 + "<br>");
   }

   // $FF: synthetic method
   ServletRuntimeMBeanImpl(String var1, ServletStubImpl var2, Object var3) throws ManagementException {
      this(var1, var2);
   }
}
