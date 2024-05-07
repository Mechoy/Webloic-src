package weblogic.servlet.jsp;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import oracle.jsp.provider.JspResourceProvider;
import weblogic.servlet.internal.RequestCallback;
import weblogic.servlet.internal.ServletStubImpl;
import weblogic.servlet.internal.StubLifecycleHelper;
import weblogic.servlet.internal.WarSource;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;

public abstract class JspStub extends ServletStubImpl {
   public static final String DEFAULT_PACKAGE_PREFIX = "jsp_servlet";
   protected JspConfig jsps;
   protected JspResourceProvider resourceProvider;
   private String hardcodedFilePath = null;
   private static final boolean alwaysCheckDisk = initAlwaysCheckDiskProp();
   private long lastStaleCheck;
   private URL sourceUrl = null;
   private GenericClassLoader jspClassLoader;

   public JspStub(String var1, String var2, Map var3, WebAppServletContext var4, JspConfig var5) {
      super(var1, var2, var4, var3);
      this.jsps = var5;
      this.hardcodedFilePath = var1;
      this.resourceProvider = var4.getJspResourceProvider();
   }

   protected String getDefaultContentType() {
      return "text/html";
   }

   protected boolean isServletStale(Servlet var1) {
      StaleIndicator var2 = (StaleIndicator)var1;

      try {
         return var2._isStale();
      } catch (Throwable var4) {
         return true;
      }
   }

   public String getFilePath() {
      return this.hardcodedFilePath;
   }

   public void setFilePath(String var1) {
      this.hardcodedFilePath = var1;
   }

   protected synchronized ClassLoader getNewClassLoader() {
      this.jspClassLoader = null;
      return this.getClassLoader();
   }

   protected ClassLoader getClassLoader() {
      if (this.jspClassLoader == null) {
         TagFileClassLoader var1 = this.getContext().getTagFileHelper().getTagFileClassLoader();
         ClassFinder var2 = var1.getClassFinder();
         this.jspClassLoader = new JspClassLoader(var2, var1, this.className);
         this.jspClassLoader.addClassFinderFirst(new ClasspathClassFinder2(this.jsps.getWorkingDir()));
      }

      return this.jspClassLoader;
   }

   public void reloadJSPOnUpdate(RequestCallback var1) throws IOException, ServletException {
      synchronized(this) {
         if (this.isStale()) {
            this.getLifecycleHelper().destroy();
            this.prepareServlet(var1, false);
         }

      }
   }

   protected void checkForReload(RequestCallback var1) throws ServletException, IOException {
      if (this.lastStaleCheck > 0L && this.getContext().getTagFileHelper().getCLCreationTime() > this.lastStaleCheck) {
         this.lastStaleCheck = 0L;
      }

      long var2 = this.jsps.getPageCheckSecs() * 1000L;
      if (var2 >= 0L && this.lastStaleCheck + var2 <= System.currentTimeMillis()) {
         synchronized(this) {
            if (this.lastStaleCheck + var2 <= System.currentTimeMillis()) {
               if (this.needToPrepare(var1)) {
                  synchronized(this) {
                     this.getLifecycleHelper().destroy();
                     this.prepareServlet(var1, alwaysCheckDisk);
                  }
               }

               this.lastStaleCheck = System.currentTimeMillis();
            }
         }
      }

      super.checkForReload(var1);
   }

   protected final boolean isStale() {
      StubLifecycleHelper var1 = this.getLifecycleHelper();
      if (var1 == null) {
         return false;
      } else {
         boolean var2;
         try {
            Servlet var3 = var1.getServlet();
            boolean var4 = this.isServletStale(var3);
            var1.returnServlet(var3);
            var2 = var4;
         } catch (ServletException var5) {
            var2 = true;
         }

         return var2;
      }
   }

   private boolean isClassLoaderStale() {
      if (this.jspClassLoader == null) {
         return false;
      } else {
         boolean var1 = this.jspClassLoader.getParent() != this.getContext().getTagFileHelper().getTagFileClassLoader();
         return var1;
      }
   }

   private boolean needToPrepare(RequestCallback var1) throws JspFileNotFoundException {
      Source var2 = this.getSourceForRequestURI(var1);
      if (this.resourceProvider == null && (var2 == null || !var2.getURL().equals(this.sourceUrl))) {
         return true;
      } else {
         boolean var3 = this.isStale();
         if (var3) {
            return true;
         } else {
            boolean var4 = this.isClassLoaderStale();
            return var4 && !this.isJspFromArchivedSharedLib(var2);
         }
      }
   }

   private void setSourceUrl(Source var1) {
      if (var1 != null) {
         this.sourceUrl = var1.getURL();
      }

   }

   private Source getSourceForRequestURI(RequestCallback var1) throws JspFileNotFoundException {
      if (this.getContext() != null) {
         String var2 = this.getRequestURI(var1);
         if (var2 != null) {
            return this.getContext().getResourceAsSource(var2);
         }
      }

      return null;
   }

   protected String getRequestURI(RequestCallback var1) {
      return this.hardcodedFilePath != null ? this.hardcodedFilePath : var1.getIncludeURI();
   }

   protected void prepareServlet(RequestCallback var1) throws ServletException, IOException {
      this.prepareServlet(var1, true);
      if (this.lastStaleCheck == 0L) {
         this.lastStaleCheck = System.currentTimeMillis();
      }

   }

   private void prepareServlet(RequestCallback var1, boolean var2) throws ServletException, IOException {
      Source var3 = this.getSourceForRequestURI(var1);
      String var4 = this.getRequestURI(var1);
      if (var3 == null && this.resourceProvider == null && this.getContext() != null && var4 != null) {
         throw new JspFileNotFoundException("Requested JSP source file '" + var4 + "' no longer exists");
      } else {
         this.setSourceUrl(var3);
         ClassLoader var5 = this.getNewClassLoader();
         boolean var6 = true;
         if (var2) {
            try {
               Class var7 = var5.loadClass(this.getClassName());
               if (!isJSPClassStale(var7, this.getContext())) {
                  var6 = false;
               }
            } catch (ThreadDeath var26) {
               throw var26;
            } catch (Throwable var27) {
            }
         }

         if (var6) {
            Thread var28 = Thread.currentThread();
            ClassLoader var8 = var28.getContextClassLoader();

            try {
               var28.setContextClassLoader(var5);
               this.compilePage(var1);
            } catch (ServletException var20) {
               throw var20;
            } catch (CompilationException var21) {
               throw var21;
            } catch (RuntimeException var22) {
               throw var22;
            } catch (IOException var23) {
               throw var23;
            } catch (Exception var24) {
               throw new ServletException("JSP compilation of " + var4 + " failed: " + var24.toString(), var24);
            } finally {
               var28.setContextClassLoader(var8);
            }
         }

         if (this.jsps.getPageCheckSecs() >= 0L) {
            this.getContext().getTagFileHelper().reloadIfNecessary(this.jsps.getPageCheckSecs());
         }

         if (var6) {
            this.getNewClassLoader();
         }

         super.prepareServlet(var1);
      }
   }

   static void p(String var0) {
      System.err.println("[JspStub]: " + var0);
   }

   private static boolean initAlwaysCheckDiskProp() {
      return Boolean.getBoolean("weblogic.jsp.alwaysCheckDisk");
   }

   public static boolean isJSPClassStale(Class var0, StaleChecker var1) {
      try {
         Class[] var2 = new Class[]{StaleChecker.class};
         Method var3 = var0.getMethod("_staticIsStale", var2);
         if (var3 == null) {
            return true;
         } else {
            int var4 = var3.getModifiers();
            if (Modifier.isStatic(var4) && Modifier.isPublic(var4)) {
               Class[] var5 = var3.getParameterTypes();
               if (var5 != null && var5.length == 1 && var5[0] == StaleChecker.class) {
                  if (var3.getReturnType() != Boolean.TYPE) {
                     return true;
                  } else {
                     Object[] var6 = new Object[]{var1};
                     Object var7 = var3.invoke((Object)null, var6);
                     return !(var7 instanceof Boolean) ? true : (Boolean)var7;
                  }
               } else {
                  return true;
               }
            } else {
               return true;
            }
         }
      } catch (Exception var8) {
         return true;
      }
   }

   private boolean isJspFromArchivedSharedLib(Source var1) {
      if (var1 instanceof WarSource) {
         WarSource var2 = (WarSource)var1;
         if (var2.isFromArchive() && var2.isFromLibrary()) {
            return true;
         }
      }

      return false;
   }

   protected abstract void compilePage(RequestCallback var1) throws Exception;
}
