package weblogic.marathon.ddinit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.ErrorPageBean;
import weblogic.j2ee.descriptor.FilterBean;
import weblogic.j2ee.descriptor.FilterMappingBean;
import weblogic.j2ee.descriptor.LoginConfigBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.ServletMappingBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WelcomeFileListBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.marathon.fs.Entry;
import weblogic.marathon.fs.FS;
import weblogic.marathon.fs.FSUtils;
import weblogic.servlet.internal.dd.TaglibDescriptor;
import weblogic.servlet.jsp.dd.AttributeDescriptor;
import weblogic.servlet.jsp.dd.TLDDescriptor;
import weblogic.servlet.jsp.dd.TagDescriptor;
import weblogic.utils.StringUtils;
import weblogic.utils.classfile.ClassFileBean;
import weblogic.utils.classfile.MethodBean;

public class WebInit extends ModuleInit {
   private ArrayList servlets = new ArrayList();
   private List filters = new ArrayList();
   private List listeners = new ArrayList();
   private ArrayList tags = new ArrayList();
   TLDDescriptor tld;
   TaglibDescriptor tagdesc;
   private Descriptor webAppRoot;
   private Descriptor wlWebAppRoot;
   private WebAppBean webApp;
   private WeblogicWebAppBean wlWebApp;
   private static final String SERVLET = "javax.servlet.Servlet";
   private static final String HTTP_SERVLET = "javax.servlet.http.HttpServlet";
   private static final String CONTEXT_LISTENER = "javax.servlet.ServletContextListener";
   private static final String TAG = "javax.servlet.jsp.tagext.Tag";
   private static final String BODY_TAG = "javax.servlet.jsp.tagext.BodyTag";
   private static final String ITER_TAG = "javax.servlet.jsp.tagext.IterationTag";
   private static final String TCF_TAG = "javax.servlet.jsp.tagext.TryCatchFinally";
   private static final String FILTER = "javax.servlet.Filter";
   private static final String TAG_SUPPORT = "javax.servlet.jsp.tagext.TagSupport";
   private static final String BODY_SUPPORT = "javax.servlet.jsp.tagext.BodyTagSupport";

   public WebInit(FS var1) {
      super(var1);
      EditableDescriptorManager var2 = new EditableDescriptorManager();
      this.webAppRoot = var2.createDescriptorRoot(WebAppBean.class);
      this.webApp = (WebAppBean)this.webAppRoot.getRootBean();
      this.wlWebAppRoot = var2.createDescriptorRoot(WeblogicWebAppBean.class);
      this.wlWebApp = (WeblogicWebAppBean)this.wlWebAppRoot.getRootBean();
   }

   protected String getClassPrefixPath() {
      return "WEB-INF/classes/";
   }

   private ServletBean getServletDesc(String var1) {
      ServletBean var2 = this.webApp.createServlet();
      var2.setServletClass(var1);
      String[] var3 = StringUtils.splitCompletely(var1, ".");
      var2.setServletName(var3[var3.length - 1]);
      return var2;
   }

   public ServletBean getServletDesc(ClassFileBean var1) {
      String var2 = var1.getName();
      return this.getServletDesc(var2);
   }

   public ServletMappingBean getServletMapping(ServletBean var1) {
      ServletMappingBean var2 = this.webApp.createServletMapping();
      var2.setServletName(var1.getServletName());
      var2.setUrlPatterns(new String[]{"/" + var1.getServletName() + "/*"});
      return var2;
   }

   protected void initDescriptors() {
      String var4;
      int var7;
      for(var7 = 0; var7 < this.servlets.size(); ++var7) {
         var4 = (String)this.servlets.get(var7);
         ServletBean var1 = this.getServletDesc(var4);
         this.getServletMapping(var1);
      }

      for(var7 = 0; var7 < this.listeners.size(); ++var7) {
         var4 = (String)this.listeners.get(var7);
         this.webApp.createListener().setListenerClass(var4);
      }

      for(var7 = 0; var7 < this.filters.size(); ++var7) {
         FilterBean var5 = this.webApp.createFilter();
         var4 = (String)this.filters.get(var7);
         var5.setFilterClass(var4);
         int var8 = var4.lastIndexOf(46);
         if (var8 > 0) {
            var4 = var4.substring(var8 + 1);
         }

         var5.setFilterName(var4);
         FilterMappingBean var6 = this.webApp.createFilterMapping();
         var6.setFilterName(var5.getFilterName());
         var6.setUrlPatterns(new String[]{'/' + var4 + "/*"});
      }

      if (this.tags.size() > 0) {
         this.initTaglib();
      }

      WelcomeFileListBean var11 = this.webApp.createWelcomeFileList();
      var11.addWelcomeFile("index.jsp");
      var11.addWelcomeFile("index.html");
      var11.addWelcomeFile("index.htm");
      if (this.baseFS.exists("error.jsp")) {
         ErrorPageBean var9 = this.webApp.createErrorPage();
         var9.setExceptionType("java.lang.Exception");
         var9.setLocation("/error.jsp");
      }

      LoginConfigBean var10 = this.webApp.createLoginConfig();
      var10.setRealmName("WebApp");
      if (this.baseFS.exists("login.jsp")) {
         var10.setAuthMethod("FORM");
         var10.getFormLoginConfig().setFormLoginPage("/login.jsp");
         var10.getFormLoginConfig().setFormErrorPage("/error.jsp");
      } else {
         var10.setAuthMethod("BASIC");
      }

   }

   protected void writeDescriptors() throws IOException {
      FS var1 = this.getBaseFS();
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      this.webAppRoot.toXML(var2);
      var1.put("WEB-INF/web.xml", var2.toByteArray());
      var2.reset();
      this.wlWebAppRoot.toXML(var2);
      var1.put("WEB-INF/weblogic.xml", var2.toByteArray());
   }

   public boolean hasJSPsInRoot(FS var1) {
      boolean var2 = false;

      try {
         String[] var3 = FSUtils.getPaths(var1, "", ".jsp", false, true);
         var2 = this.hasAPathInRoot(var3);
         if (!var2) {
            var3 = FSUtils.getPaths(var1, "", ".html", false, true);
            var2 = this.hasAPathInRoot(var3);
         }
      } catch (IOException var4) {
      }

      return var2;
   }

   private boolean hasAPathInRoot(String[] var1) {
      boolean var2 = false;
      if (var1.length != 0) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = var1[var3];
            if (var4.endsWith("/")) {
               var4 = var4.substring(0, var4.length() - 1);
            }

            var2 = var4.indexOf("/") == -1;
            if (var2) {
               break;
            }
         }
      }

      return var2;
   }

   public boolean hasWebInfInRoot(FS var1) {
      boolean var2 = false;

      try {
         String[] var3 = FSUtils.getPaths(var1, "WEB-INF/", "");
         var2 = this.hasAPathInRoot(var3);
      } catch (IOException var4) {
      }

      return var2;
   }

   public boolean hasJSPs(FS var1) {
      boolean var2 = false;

      try {
         String[] var3 = FSUtils.getPaths(var1, "", ".jsp");
         if (var3.length != 0) {
            var2 = true;
         }
      } catch (IOException var4) {
      }

      return var2;
   }

   protected void loadClasses() {
      if (this.m_classes.size() <= 0) {
         String[] var1 = new String[0];

         try {
            this.inform("Searching for classes in " + this.getClassPrefixPath());
            var1 = FSUtils.getPaths(this.baseFS, this.getClassPrefixPath(), ".class");
         } catch (IOException var11) {
            this.inform("Error while searching for class files in " + this.baseFS.getPath());
            this.inform(var11.getMessage());
         }

         for(int var2 = 0; var2 < var1.length; ++var2) {
            ClassFileBean var3 = null;

            try {
               Entry var4 = this.baseFS.getEntry(var1[var2]);
               InputStream var5 = var4.getInputStream();

               try {
                  var3 = ClassFileBean.load(var5);
               } finally {
                  var5.close();
               }
            } catch (Exception var12) {
               var12.printStackTrace();
               continue;
            }

            this.m_classes.add(var3);
         }

      }
   }

   private boolean isServlet(ClassFileBean var1) {
      return this.hasInterface(var1, "javax.servlet.Servlet");
   }

   private boolean isTag(ClassFileBean var1) {
      return this.hasInterface(var1, "javax.servlet.jsp.tagext.Tag");
   }

   protected void searchForComponents() {
      this.setFound(this.hasWebInfInRoot(this.baseFS));
      if (!this.getFound()) {
         this.setFound(this.hasJSPsInRoot(this.baseFS));
      }

      List var1 = this.getClasses();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ClassFileBean var3 = (ClassFileBean)var2.next();
         if (!var3.isAbstract() && var3.isPublic()) {
            if (this.isServlet(var3)) {
               this.verbose("found servlet class: '" + var3.getName() + "'");
               this.servlets.add(var3.getName());
               this.setFound(true);
            }

            if (this.hasInterface(var3, "javax.servlet.ServletContextListener")) {
               this.verbose("found servlet context listener class: '" + var3.getName() + "'");
               this.listeners.add(var3.getName());
               this.setFound(true);
            }

            if (this.isTag(var3)) {
               this.verbose("found JSP tag class: '" + var3.getName() + "'");
               this.tags.add(var3);
               this.setFound(true);
            }

            if (this.hasInterface(var3, "javax.servlet.Filter")) {
               this.verbose("found filter class: '" + var3.getName() + "'");
               this.filters.add(var3.getName());
               this.setFound(true);
            }
         }
      }

   }

   private void initTaglib() {
      if (this.tags.size() != 0) {
         this.tagdesc = new TaglibDescriptor();
         TaglibDescriptor var1 = this.tagdesc;
         var1.setURI("tags");
         var1.setLocation("/WEB-INF/taglib.tld");
         this.tld = new TLDDescriptor();
         this.tld.set12(true);
         this.tld.setJspVersion("1.2");
         this.tld.setTaglibVersion("1.0.0");
         this.tld.setDisplayName("automatic tags");
         this.tld.setDescription("taglib automatically generated by WebLogic DDInit");
         ClassFileBean[] var2 = new ClassFileBean[this.tags.size()];
         this.tags.toArray(var2);
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            try {
               TagDescriptor var5 = this.initTD(var2[var4]);
               if (var5 != null) {
                  var3.add(var5);
               }
            } catch (Exception var6) {
               this.verbose("failure analyzing tag class: " + var6);
            }
         }

         TagDescriptor[] var7 = new TagDescriptor[var3.size()];
         var3.toArray(var7);
         this.tld.setTags(var7);
      }
   }

   private TagDescriptor initTD(ClassFileBean var1) throws Exception {
      int var2 = var1.getModifiers();
      if (var1.isAbstract()) {
         return null;
      } else {
         TagDescriptor var3 = new TagDescriptor();
         var3.set12(true);
         String var4 = var1.getName();
         var3.setClassname(var4);
         int var5 = var4.lastIndexOf(46);
         if (var5 > 0) {
            var4 = var4.substring(var5 + 1);
         }

         var4 = var4.toLowerCase();
         var3.setName(var4);
         ArrayList var6 = new ArrayList();
         MethodBean[] var7 = var1.getPublicMethods();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            MethodBean var9 = var7[var8];
            String var10 = var9.getName();
            if (var10.startsWith("set") && var9.getReturnType().equals("void")) {
               String[] var11 = var9.getParams();
               if (var11.length == 1 && !var10.equals("setPageContext") && !var10.equals("setParent") && !var10.equals("setBodyContent")) {
                  AttributeDescriptor var12 = new AttributeDescriptor();
                  var12.setName(var10.substring(3).toLowerCase());
                  var12.setType(var11[0]);
                  var12.setRequired(false);
                  var12.setRtexpr(true);
                  var6.add(var12);
               }
            }
         }

         AttributeDescriptor[] var13 = new AttributeDescriptor[var6.size()];
         var6.toArray(var13);
         var3.setAtts(var13);
         return var3;
      }
   }

   private static void printUsage() {
      System.out.println("usage: java weblogic.marathon.ddinit.WebInit <base-directory or jar>");
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length != 1) {
         printUsage();
         System.exit(0);
      }

      String var1 = var0[0];
      FS var2 = FS.mount(new File(var1));
      WebInit var3 = new WebInit(var2);
      var3.execute();
      var2.save();
      var2.close();
      System.out.println("filters=" + var3.filters.size() + " servlets=" + var3.servlets.size() + " tags=" + var3.tags.size());
      Iterator var4 = var3.tags.iterator();

      while(var4.hasNext()) {
         ClassFileBean var5 = (ClassFileBean)var4.next();
         System.err.println(" tag class: " + var5.getName());
      }

   }
}
