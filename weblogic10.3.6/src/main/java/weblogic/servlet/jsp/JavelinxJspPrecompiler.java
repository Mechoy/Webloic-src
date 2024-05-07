package weblogic.servlet.jsp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import weblogic.j2ee.descriptor.JspConfigBean;
import weblogic.j2ee.descriptor.JspPropertyGroupBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.JSPServlet;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;
import weblogic.utils.enumerations.ResourceEnumerator;

public class JavelinxJspPrecompiler implements JSPPrecompiler {
   private WebAppServletContext servletContext;
   private ResourceEnumerator resourceEnumerator;
   private File workingDir = null;
   private JspConfig config;
   private StubCompilerContext compilerContext;

   public void compile(WebAppServletContext var1, File var2) throws Exception {
      this.init(var1, var2, var1.getJSPManager().createJspConfig());
      this.compileAll();
   }

   private void init(WebAppServletContext var1, File var2, JspConfig var3) {
      this.servletContext = var1;
      this.compilerContext = new StubCompilerContext(var1);
      String var4 = var3.getWorkingDir();
      WebAppBean var5 = var1.getWebAppModule().getWebAppBean();
      JspConfigBean[] var6 = var5.getJspConfigs();
      HashMap var7 = new HashMap();
      var7.put("*.jsp", "*.jsp");
      var7.put("*.jspx", "*.jspx");

      for(int var8 = 0; var8 < var6.length; ++var8) {
         JspPropertyGroupBean[] var9 = var6[var8].getJspPropertyGroups();

         for(int var10 = 0; var10 < var9.length; ++var10) {
            String[] var11 = var9[var10].getUrlPatterns();

            for(int var12 = 0; var12 < var11.length; ++var12) {
               if (var7.get(var11[var12]) == null) {
                  var7.put(var11[var12], var11[var12]);
               }
            }
         }
      }

      if (var4 != null) {
         this.workingDir = new File(var4.replace('/', File.separatorChar));
      } else {
         this.workingDir = var1.getRootTempDir();
      }

      String[] var13 = new String[]{"*.html", "/classes/*"};
      String[] var14 = (String[])var7.keySet().toArray(new String[var7.size()]);
      this.resourceEnumerator = ResourceEnumerator.makeInstance(var2, var13, var14);
      var1.addClassPath(this.workingDir.getAbsolutePath());
      this.config = var3;
   }

   private void compileAll() throws Exception {
      String var1 = this.config.getPackagePrefix();
      if (var1 == null) {
         var1 = "jsp_servlet";
      }

      ArrayList var2 = new ArrayList();

      try {
         String var3 = null;

         while((var3 = this.resourceEnumerator.getNextURI()) != null) {
            if (!var3.startsWith("/")) {
               var3 = "/" + var3;
            }

            if (this.needsCompilation(var3, var1)) {
               var2.add(var3);
            } else {
               HTTPLogger.logJSPClassUptodate(this.servletContext.getAppDisplayName(), var3);
            }
         }
      } finally {
         this.resourceEnumerator.close();
      }

      byte var13 = 50;
      int var4 = var2.size();
      int var5 = 0;

      for(byte var6 = var13; var5 < var4; var5 += var6) {
         int var7 = Math.min(var6, var4 - var5);
         int var9 = var5 + var7;
         List var10 = var2.subList(var5, var9);
         JspCLLManager.compileJsps(var10, this.compilerContext);
      }

   }

   private boolean needsCompilation(String var1, String var2) {
      Class var3 = null;

      try {
         String var4 = JSPServlet.uri2classname(var2, var1);
         ClassLoader var5 = this.makeLoader(var4);
         var3 = var5.loadClass(var4);
      } catch (ClassNotFoundException var6) {
         HTTPLogger.logPrecompilingJspNoClass(this.servletContext.getAppDisplayName(), var1);
         return true;
      } catch (Throwable var7) {
      }

      if (var3 != null && JspStub.isJSPClassStale(var3, this.servletContext)) {
         HTTPLogger.logPrecompilingStaleJsp(this.servletContext.getAppDisplayName(), var1);
         return true;
      } else {
         return false;
      }
   }

   private ClassLoader makeLoader(String var1) {
      GenericClassLoader var2 = (GenericClassLoader)this.servletContext.getServletClassLoader();
      ClassFinder var3 = var2.getClassFinder();
      JspClassLoader var4 = new JspClassLoader(var3, var2, var1);
      var4.addClassFinderFirst(new ClasspathClassFinder2(this.config.getWorkingDir()));
      return var4;
   }

   static class StubCompilerContext implements JspCLLManager.IJSPCompilerContext {
      private WebAppServletContext context;

      StubCompilerContext(WebAppServletContext var1) {
         this.context = var1;
      }

      public ClassLoader getClassLoader() {
         return this.context.getTagFileHelper().getTagFileClassLoader();
      }

      public Source getSource(String var1) {
         return this.context.getResourceAsSource(var1);
      }

      public String getClasspath() {
         return this.context.getFullClasspath();
      }

      public JspConfig getJspConfig() {
         return this.context.getJSPManager().createJspConfig();
      }

      public String[] getSourcePaths() {
         String var1 = this.context.getResourceFinder("/").getClassPath();
         return StringUtils.splitCompletely(var1, File.pathSeparator);
      }

      public WebAppBean getWebAppBean() {
         return this.context.getWebAppModule().getWebAppBean();
      }

      public WeblogicWebAppBean getWlWebAppBean() {
         return this.context.getWebAppModule().getWlWebAppBean();
      }

      public String getName() {
         return this.context.getName();
      }

      public WebAppServletContext getServletContext() {
         return this.context;
      }

      public String getContextPath() {
         return this.context.getContextPath();
      }

      public void say(String var1) {
      }

      public void sayError(String var1, String var2) {
         HTTPLogger.logJSPPrecompileErrors(this.context.getAppDisplayName(), var1, var2);
      }

      public Set getAdditionalExtensions() {
         return null;
      }
   }
}
