package weblogic.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.servlet.internal.ServletStubImpl;
import weblogic.servlet.internal.WarSource;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.jsp.AddToMapException;
import weblogic.servlet.jsp.JspConfig;
import weblogic.servlet.jsp.JspFactoryImpl;
import weblogic.servlet.jsp.JspStub;
import weblogic.servlet.jsp.ResourceProviderJspStub;
import weblogic.utils.io.FilenameEncoder;

public class JSPServlet extends HttpServlet {
   public static final String DEFAULT_PACKAGE_PREFIX = "jsp_servlet";
   public static final String DEFAULT_PAGE_NAME = "index.jsp";
   public static final Constructor JSP_STUB = getJSPStubConstructor();
   protected JspConfig jspConfig;
   protected WebAppServletContext ourContext;
   private static boolean isCaseSensitive;

   public synchronized void init(ServletConfig var1) throws ServletException {
      super.init(var1);
      this.ourContext = (WebAppServletContext)this.getServletContext();
      this.jspConfig = this.ourContext.getJSPManager().createJspConfig();
      this.jspConfig.setValuesFromInitArgs(var1);
      boolean var2 = this.jspConfig.isVerbose();
      if (var2) {
         this.log("param verbose initialized to: " + var2);
         this.log("param packagePrefix initialized to: " + this.jspConfig.getPackagePrefix());
         this.log("param compilerclass initialized to: " + this.jspConfig.getCompilerClass());
         this.log("param compileCommand initialized to: " + this.jspConfig.getCompileCommand());
         this.log("param compilerval initialized to: " + this.jspConfig.getCompilerval());
         this.log("param pageCheckSeconds initialized to: " + this.jspConfig.getPageCheckSecs());
         this.log("param encoding initialized to: " + this.jspConfig.getEncoding());
         this.log("param superclass initialized to " + this.jspConfig.getSuperClassName());
      }

      if (this.jspConfig.getCompilerval() == null) {
         throw new UnavailableException("Couldn't find init param: compileCommand");
      } else {
         String var3 = this.jspConfig.getWorkingDir();
         if (var3 == null) {
            throw new UnavailableException("Couldn't find init param: workingDir");
         } else {
            File var4 = new File(var3.replace('/', File.separatorChar));
            if (!var4.exists()) {
               if (!var4.mkdirs()) {
                  throw new UnavailableException("Working directory: " + var3 + " was not found and " + "could not be created.");
               }

               this.log("Making new working directory: " + var3);
            }

            try {
               var3 = var4.getCanonicalPath();
            } catch (IOException var6) {
               throw new UnavailableException("Couldn't resolve canonical path for: " + var3);
            }

            if (var2) {
               this.log("param workingDir initialized to: " + var3);
            }

            this.ourContext.addClassPath(var3);
            if (var2) {
               this.log("initialization complete");
            }

         }
      }
   }

   public void service(HttpServletRequest var1, HttpServletResponse var2) throws IOException, ServletException {
      ServletStubImpl var3 = null;
      StringBuilder var4 = new StringBuilder(30);
      String var5 = (String)var1.getAttribute("javax.servlet.include.servlet_path");
      String var6 = (String)var1.getAttribute("javax.servlet.include.path_info");
      if (var5 == null && var6 == null) {
         var5 = var1.getServletPath();
         var6 = var1.getPathInfo();
      }

      if (var5 != null) {
         var4.append(var5);
      }

      if (var6 != null) {
         if (!this.jspConfig.isExactMapping() && var5 != null) {
            String var7 = var6.toLowerCase();
            boolean var8 = false;
            int var13;
            if ((var13 = var7.indexOf(".jsp/")) != -1) {
               var6 = var6.substring(0, var13 + 4);
               var4.append(var6);
            } else if ((var13 = var7.indexOf(".jspx/")) != -1) {
               var6 = var6.substring(0, var13 + 5);
               var4.append(var6);
            } else {
               var4.append(var6);
            }
         } else {
            var4.append(var6);
         }
      }

      int var12 = var4.length();
      if (var12 == 0) {
         var4.append('/');
         var4.append(this.jspConfig.getDefaultFilename());
      } else if (var4.charAt(var12 - 1) == '/') {
         var4.append(this.jspConfig.getDefaultFilename());
      }

      String var14 = FilenameEncoder.resolveRelativeURIPath(var4.toString());
      if (this.ourContext.getJSPManager().getResourceProviderClass() == null) {
         WarSource var9 = this.ourContext.getResourceAsSource(var14);
         if (var9 == null) {
            var2.sendError(404);
            return;
         }

         if (var9.length() == 0L) {
            var2.setContentLength(0);
            return;
         }
      }

      WebAppServletContext var15 = (WebAppServletContext)this.getServletContext();
      var3 = var15.getServletStub(var14);
      if (var3 != null && var3 instanceof JspStub && !var3.getClassName().equals(this.getClass().getName())) {
         throw new AddToMapException(var14, var3);
      } else if (this.ourContext.getJspResourceProvider() != null) {
         ResourceProviderJspStub var11 = new ResourceProviderJspStub(var14, this.ourContext, this.jspConfig);
         throw new AddToMapException(var14, var11);
      } else {
         JspStub var10 = getNewJspStub(var14, uri2classname(this.jspConfig.getPackagePrefix(), var14), (Map)null, this.ourContext, this.jspConfig);
         throw new AddToMapException(var14, var10);
      }
   }

   public static JspStub getNewJspStub(String var0, String var1, Map var2, WebAppServletContext var3, JspConfig var4) throws ServletException {
      try {
         return (JspStub)JSP_STUB.newInstance(var0, var1, var2, var3, var4);
      } catch (InstantiationException var6) {
         throw new AssertionError(var6);
      } catch (IllegalAccessException var7) {
         throw new AssertionError(var7);
      } catch (InvocationTargetException var8) {
         throw new AssertionError(var8);
      }
   }

   private static Constructor getJSPStubConstructor() {
      try {
         Class var0 = Class.forName("weblogic.servlet.jsp.JavelinxJSPStub");
         return var0.getConstructor(String.class, String.class, Map.class, WebAppServletContext.class, JspConfig.class);
      } catch (ClassNotFoundException var1) {
         throw new AssertionError("Unable to find class weblogic.servlet.jsp.JavelinxJSPStub");
      } catch (NoSuchMethodException var2) {
         throw new AssertionError("Unable to find constructor weblogic.servlet.jsp.JavelinxJSPStub(String, String, Map, WebAppServletContext, JspConfig)");
      }
   }

   public static String uri2classname(String var0, String var1) {
      try {
         Class var2 = Class.forName("weblogic.jsp.internal.jsp.utils.JavaTransformUtils");
         Method var3;
         String var4;
         if (!var1.endsWith(".tag") && !var1.endsWith(".tagx")) {
            var3 = var2.getMethod("getFullClassName", String.class, Boolean.TYPE, String.class);
            var4 = (String)var3.invoke((Object)null, var1, Boolean.FALSE, var0);
            return var4;
         } else {
            var3 = var2.getMethod("computeTagClassName", String.class, String.class);
            var4 = (String)var3.invoke((Object)null, var1, var0);
            return var4;
         }
      } catch (Exception var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else {
            throw new RuntimeException(var5);
         }
      }
   }

   static {
      JspFactoryImpl.init();
      isCaseSensitive = false;

      try {
         String var0 = System.getProperty("weblogic.jsp.windows.caseSensitive");
         String var1 = System.getProperty("weblogic.jsp.caseSensitive");
         if ("true".equalsIgnoreCase(var0) || "true".equalsIgnoreCase(var1)) {
            isCaseSensitive = true;
         }
      } catch (Throwable var2) {
      }

   }
}
