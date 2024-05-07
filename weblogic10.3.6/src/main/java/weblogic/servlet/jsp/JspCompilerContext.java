package weblogic.servlet.jsp;

import java.util.Set;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;

public class JspCompilerContext implements JspCLLManager.IJSPCompilerContext {
   private String name;
   private GenericClassLoader classloader;
   private JspConfig config;
   private String[] sourcePaths;
   private WebAppBean webAppBean;
   private WeblogicWebAppBean wlWebAppBean;
   private boolean verbose;
   private ClassFinder resourceFinder;

   public JspCompilerContext(String var1) {
      this.name = var1;
      this.verbose = false;
   }

   public void setClassLoader(GenericClassLoader var1) {
      this.classloader = var1;
   }

   public ClassLoader getClassLoader() {
      return this.classloader;
   }

   public String getClasspath() {
      return this.classloader.getClassPath();
   }

   public void setJspConfig(JspConfig var1) {
      this.config = var1;
   }

   public JspConfig getJspConfig() {
      return this.config;
   }

   public void setSourcePaths(String[] var1) {
      this.sourcePaths = var1;
   }

   public String[] getSourcePaths() {
      return this.sourcePaths;
   }

   public void setWebAppBean(WebAppBean var1) {
      this.webAppBean = var1;
   }

   public WebAppBean getWebAppBean() {
      return this.webAppBean;
   }

   public void setWlWebAppBean(WeblogicWebAppBean var1) {
      this.wlWebAppBean = var1;
   }

   public WeblogicWebAppBean getWlWebAppBean() {
      return this.wlWebAppBean;
   }

   public String getName() {
      return this.name;
   }

   public WebAppServletContext getServletContext() {
      return null;
   }

   public String getContextPath() {
      return "";
   }

   public void say(String var1) {
      if (this.verbose) {
         System.out.println(this.name + " Compiling " + var1);
      }

   }

   public void sayError(String var1, String var2) {
      System.err.println(this.name + " Error encountered while compiling '" + var1 + "' \r\n" + var2);
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   boolean isVerbose() {
      return this.verbose;
   }

   public void setResourceFinder(ClassFinder var1) {
      this.resourceFinder = var1;
   }

   public Source getSource(String var1) {
      return this.resourceFinder.getSource(var1);
   }

   public Set getAdditionalExtensions() {
      return null;
   }
}
