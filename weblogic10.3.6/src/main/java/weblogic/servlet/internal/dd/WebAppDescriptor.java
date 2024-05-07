package weblogic.servlet.internal.dd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.WebElementMBean;
import weblogic.management.descriptors.webapp.EjbRefMBean;
import weblogic.management.descriptors.webapp.EnvEntryMBean;
import weblogic.management.descriptors.webapp.ErrorPageMBean;
import weblogic.management.descriptors.webapp.FilterMBean;
import weblogic.management.descriptors.webapp.FilterMappingMBean;
import weblogic.management.descriptors.webapp.ListenerMBean;
import weblogic.management.descriptors.webapp.LoginConfigMBean;
import weblogic.management.descriptors.webapp.MimeMappingMBean;
import weblogic.management.descriptors.webapp.ParameterMBean;
import weblogic.management.descriptors.webapp.ResourceEnvRefMBean;
import weblogic.management.descriptors.webapp.ResourceRefMBean;
import weblogic.management.descriptors.webapp.SecurityConstraintMBean;
import weblogic.management.descriptors.webapp.SecurityRoleMBean;
import weblogic.management.descriptors.webapp.ServletMBean;
import weblogic.management.descriptors.webapp.ServletMappingMBean;
import weblogic.management.descriptors.webapp.SessionConfigMBean;
import weblogic.management.descriptors.webapp.TagLibMBean;
import weblogic.management.descriptors.webapp.UIMBean;
import weblogic.management.descriptors.webapp.WebAppDescriptorMBean;
import weblogic.management.descriptors.webapp.WelcomeFileListMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class WebAppDescriptor extends BaseServletDescriptor implements ToXML, WebAppDescriptorMBean, DescriptorConstants, Comparator {
   private static final long serialVersionUID = -4056254418170227485L;
   private static final String CONTEXT_PARAM = "context-param";
   private static final String DISTRIBUTABLE = "distributable";
   private static final String SERVLET = "servlet";
   private static final String SERVLET_MAPPING = "servlet-mapping";
   private static final String SESSION_CONFIG = "session-config";
   private static final String MIME_MAPPING = "mime-mapping";
   private static final String WELCOME_FILE_LIST = "welcome-file-list";
   private static final String ERROR_PAGE = "error-page";
   private static final String RESOURCE_REF = "resource-ref";
   private static final String RESOURCE_ENV_REF = "resource-env-ref";
   private static final String ENV_ENTRY = "env-entry";
   private static final String EJB_REF = "ejb-ref";
   private static final String EJB_LOCAL_REF = "ejb-local-ref";
   private static final String TAGLIB = "taglib";
   private static final String TAGLIB_URI = "taglib-uri";
   private static final String TAGLIB_LOCATION = "taglib-location";
   private static final String SECURITY_ROLE = "security-role";
   private static final String SECURITY_CONSTRAINT = "security-constraint";
   private static final String LOGIN_CONFIG = "login-config";
   private static final String EVENT_LISTENER = "listener";
   private static final String FILTER = "filter";
   private static final String FILTER_MAPPING = "filter-mapping";
   private UIMBean uiData;
   private List contextParams;
   private List servlets;
   private List servletMaps;
   private SessionConfigMBean sessionConfig;
   private List mimeMaps;
   private WelcomeFileListMBean welcomeFiles;
   private List errorPages;
   private List resourceRefs;
   private List resourceEnvRefs;
   private List envEntries;
   private List ejbRefs;
   private List ejbLocalRefs;
   private List taglibs;
   private List secRoles;
   private List secCons;
   private List eventListeners;
   private List filters;
   private List filterMappings;
   private LoginConfigMBean login;
   private boolean distributable = false;
   private String root;
   private String deployedName;
   private boolean archived;
   private boolean deployed;
   private String descriptorEncoding = null;
   private String descriptorVersion = null;
   private static final String WL_JSP = "weblogic.jsp.";
   private static final String WL_SESSION = "weblogic.httpd.session.";
   private static final String WL_COOKIE = "weblogic.httpd.session.cookie.";

   public String getEncoding() {
      return this.descriptorEncoding;
   }

   public void setEncoding(String var1) {
      this.descriptorEncoding = var1;
   }

   public String getVersion() {
      return this.descriptorVersion;
   }

   public void setVersion(String var1) {
      this.descriptorVersion = var1;
   }

   public WebAppDescriptor() {
      this.sessionConfig = new SessionDescriptor();
   }

   public WebAppDescriptor(Element var1) throws DOMProcessingException {
      this.uiData = new UIDescriptor(var1);
      List var2 = DOMUtils.getOptionalElementsByTagName(var1, "distributable");
      if (var2 != null && var2.size() > 0) {
         this.distributable = true;
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "context-param");
      Iterator var3 = var2.iterator();
      this.contextParams = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.contextParams.add(new ParameterDescriptor((Element)var3.next()));
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "listener");
      var3 = var2.iterator();
      this.eventListeners = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.eventListeners.add(new ListenerDescriptor((Element)var3.next()));
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "security-role");
      var3 = var2.iterator();
      this.secRoles = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.secRoles.add(new SecurityRoleDescriptor((Element)var3.next()));
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "servlet");
      var3 = var2.iterator();
      this.servlets = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.servlets.add(new ServletDescriptor(this, (Element)var3.next()));
      }

      ServletMBean[] var5 = new ServletMBean[this.servlets.size()];
      this.servlets.toArray(var5);
      this.setServlets(var5);
      var2 = DOMUtils.getOptionalElementsByTagName(var1, "servlet-mapping");
      var3 = var2.iterator();
      this.servletMaps = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.servletMaps.add(new ServletMappingDescriptor(this, (Element)var3.next()));
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "filter");
      var3 = var2.iterator();
      this.filters = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.filters.add(new FilterDescriptor((Element)var3.next()));
      }

      FilterMBean[] var6 = new FilterMBean[this.filters.size()];
      this.filters.toArray(var6);
      this.setFilters(var6);
      var2 = DOMUtils.getOptionalElementsByTagName(var1, "filter-mapping");
      var3 = var2.iterator();
      this.filterMappings = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.filterMappings.add(new FilterMappingDescriptor(this, (Element)var3.next()));
      }

      Element var4 = DOMUtils.getOptionalElementByTagName(var1, "session-config");
      if (var4 != null) {
         this.sessionConfig = new SessionDescriptor(var4);
      } else {
         this.sessionConfig = new SessionDescriptor();
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "mime-mapping");
      var3 = var2.iterator();
      this.mimeMaps = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.mimeMaps.add(new MimeMappingDescriptor((Element)var3.next()));
      }

      var4 = DOMUtils.getOptionalElementByTagName(var1, "welcome-file-list");
      if (var4 != null) {
         this.welcomeFiles = new WelcomeFilesDescriptor(var4);
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "error-page");
      var3 = var2.iterator();
      this.errorPages = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.errorPages.add(new ErrorPageDescriptor((Element)var3.next()));
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "resource-ref");
      var3 = var2.iterator();
      this.resourceRefs = new ArrayList(var2.size());

      while(var3.hasNext()) {
         ResourceReference var7 = new ResourceReference((Element)var3.next());
         this.resourceRefs.add(var7);
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "resource-env-ref");
      var3 = var2.iterator();
      this.resourceEnvRefs = new ArrayList(var2.size());

      while(var3.hasNext()) {
         ResourceEnvRef var8 = new ResourceEnvRef((Element)var3.next());
         this.resourceEnvRefs.add(var8);
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "env-entry");
      var3 = var2.iterator();
      this.envEntries = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.envEntries.add(new EnvironmentEntry((Element)var3.next()));
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "ejb-ref");
      var3 = var2.iterator();
      this.ejbRefs = new ArrayList(var2.size());

      while(var3.hasNext()) {
         EJBReference var9 = new EJBReference((Element)var3.next());
         this.ejbRefs.add(var9);
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "ejb-local-ref");
      var3 = var2.iterator();
      this.ejbLocalRefs = new ArrayList(var2.size());

      while(var3.hasNext()) {
         EJBLocalReference var10 = new EJBLocalReference((Element)var3.next());
         this.ejbLocalRefs.add(var10);
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "taglib");
      var3 = var2.iterator();
      this.taglibs = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.taglibs.add(new TaglibDescriptor((Element)var3.next()));
      }

      var2 = DOMUtils.getOptionalElementsByTagName(var1, "security-constraint");
      var3 = var2.iterator();
      this.secCons = new ArrayList(var2.size());

      while(var3.hasNext()) {
         this.secCons.add(new SecurityConstraint(this, (Element)var3.next()));
      }

      var4 = DOMUtils.getOptionalElementByTagName(var1, "login-config");
      if (var4 != null) {
         this.login = new LoginDescriptor(var4);
      }

   }

   public boolean isDeployed() {
      return this.deployed;
   }

   public void setDeployed(boolean var1) {
      this.deployed = var1;
   }

   public boolean isArchived() {
      return this.archived;
   }

   public void setArchived(boolean var1) {
      this.archived = var1;
   }

   public String getRoot() {
      return this.root;
   }

   public void setRoot(String var1) {
      this.root = var1;
   }

   public String getDeployedName() {
      return this.deployedName;
   }

   public void setDeployedName(String var1) {
      this.deployedName = var1;
   }

   public String getDisplayName() {
      return this.getUIData() != null ? this.getUIData().getDisplayName() : null;
   }

   public void setDisplayName(String var1) {
      String var2 = this.getDisplayName();
      if (this.getUIData() != null) {
         this.getUIData().setDisplayName(var1);
      }

      if (!comp(var2, var1)) {
         this.firePropertyChange("displayName", var2, var1);
      }

   }

   public UIMBean getUIData() {
      return this.uiData;
   }

   public void setUIData(UIMBean var1) {
      this.uiData = var1;
   }

   public boolean isDistributable() {
      return this.distributable;
   }

   public void setDistributable(boolean var1) {
      if (var1 != this.isDistributable()) {
         this.distributable = var1;
         this.firePropertyChange("distributable", new Boolean(!var1), new Boolean(var1));
      }

   }

   public ParameterMBean[] getContextParams() {
      if (this.contextParams == null) {
         return new ParameterDescriptor[0];
      } else {
         ParameterDescriptor[] var1 = new ParameterDescriptor[this.contextParams.size()];
         this.contextParams.toArray(var1);
         return var1;
      }
   }

   public void setContextParams(ParameterMBean[] var1) {
      ParameterMBean[] var2 = this.getContextParams();
      this.contextParams = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.contextParams.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("contextParams", var2, var1);
         }

      }
   }

   public void addContextParam(ParameterMBean var1) {
      ParameterMBean[] var2 = this.getContextParams();
      if (var2 == null) {
         var2 = new ParameterMBean[]{var1};
         this.setContextParams(var2);
      } else {
         ParameterMBean[] var3 = new ParameterMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setContextParams(var3);
      }
   }

   public void removeContextParam(ParameterMBean var1) {
      ParameterMBean[] var2 = this.getContextParams();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            ParameterMBean[] var5 = new ParameterMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setContextParams(var5);
         }

      }
   }

   public ListenerMBean[] getListeners() {
      if (this.eventListeners == null) {
         return new ListenerDescriptor[0];
      } else {
         ListenerDescriptor[] var1 = new ListenerDescriptor[this.eventListeners.size()];
         this.eventListeners.toArray(var1);
         return var1;
      }
   }

   public void setListeners(ListenerMBean[] var1) {
      ListenerMBean[] var2 = this.getListeners();
      this.eventListeners = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.eventListeners.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("listeners", var2, var1);
         }

      }
   }

   public void addListener(ListenerMBean var1) {
      ListenerMBean[] var2 = this.getListeners();
      if (var2 == null) {
         var2 = new ListenerMBean[]{var1};
         this.setListeners(var2);
      } else {
         ListenerMBean[] var3 = new ListenerMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setListeners(var3);
      }
   }

   public void removeListener(ListenerMBean var1) {
      ListenerMBean[] var2 = this.getListeners();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            ListenerMBean[] var5 = new ListenerMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setListeners(var5);
         }

      }
   }

   public FilterMBean[] getFilters() {
      if (this.filters == null) {
         return new FilterMBean[0];
      } else {
         FilterMBean[] var1 = new FilterMBean[this.filters.size()];
         this.filters.toArray(var1);
         return var1;
      }
   }

   public void setFilters(FilterMBean[] var1) {
      FilterMBean[] var2 = this.getFilters();
      this.filters = new ArrayList();
      if (var1 != null) {
         Arrays.sort(var1, this);

         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.filters.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("filters", var2, var1);
         }

      }
   }

   public void addFilter(FilterMBean var1) {
      FilterMBean[] var2 = this.getFilters();
      if (var2 == null) {
         var2 = new FilterMBean[1];
         var2[1] = var1;
         this.setFilters(var2);
      } else {
         FilterMBean[] var3 = new FilterMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setFilters(var3);
      }
   }

   public void removeFilter(FilterMBean var1) {
      FilterMBean[] var2 = this.getFilters();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            FilterMBean[] var5 = new FilterMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setFilters(var5);
         }

      }
   }

   public FilterMappingMBean[] getFilterMappings() {
      if (this.filterMappings == null) {
         return new FilterMappingDescriptor[0];
      } else {
         FilterMappingDescriptor[] var1 = new FilterMappingDescriptor[this.filterMappings.size()];
         this.filterMappings.toArray(var1);
         return var1;
      }
   }

   public void setFilterMappings(FilterMappingMBean[] var1) {
      FilterMappingMBean[] var2 = this.getFilterMappings();
      this.filterMappings = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.filterMappings.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("filterMappings", var2, var1);
         }

      }
   }

   public void addFilterMapping(FilterMappingMBean var1) {
      FilterMappingMBean[] var2 = this.getFilterMappings();
      if (var2 == null) {
         var2 = new FilterMappingMBean[1];
         var2[1] = var1;
         this.setFilterMappings(var2);
      } else {
         FilterMappingMBean[] var3 = new FilterMappingMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setFilterMappings(var3);
      }
   }

   public void removeFilterMapping(FilterMappingMBean var1) {
      FilterMappingMBean[] var2 = this.getFilterMappings();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            FilterMappingMBean[] var5 = new FilterMappingMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setFilterMappings(var5);
         }

      }
   }

   public ServletMBean[] getServlets() {
      if (this.servlets == null) {
         return new ServletMBean[0];
      } else {
         ServletMBean[] var1 = new ServletMBean[this.servlets.size()];
         this.servlets.toArray(var1);
         return var1;
      }
   }

   public void setServlets(ServletMBean[] var1) {
      ServletMBean[] var2 = this.getServlets();
      this.servlets = new ArrayList();
      if (var1 != null) {
         int var3;
         for(var3 = 0; var3 < var1.length; ++var3) {
            if (var1[var3] == null) {
               throw new NullPointerException("null element " + var3);
            }
         }

         Arrays.sort(var1, this);

         for(var3 = 0; var3 < var1.length; ++var3) {
            this.servlets.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("servlets", var2, var1);
         }

      }
   }

   public void addServlet(ServletMBean var1) {
      if (this.servlets == null) {
         this.servlets = new ArrayList();
      }

      this.servlets.add(var1);
   }

   public void removeServlet(ServletMBean var1) {
      if (this.servlets != null) {
         this.servlets.remove(var1);
      }

   }

   public ServletMBean getServlet(String var1) {
      if (var1 != null && this.servlets != null) {
         for(int var3 = 0; var3 < this.servlets.size(); ++var3) {
            ServletMBean var2 = (ServletMBean)this.servlets.get(var3);
            if (var1.equals(var2.getName())) {
               return var2;
            }
         }
      }

      return null;
   }

   public String[] getServletNames() {
      ServletDescriptor[] var1 = (ServletDescriptor[])((ServletDescriptor[])this.getServlets());
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = var1[var3].getServletName();
      }

      return var2;
   }

   public ServletMappingMBean[] getServletMappings() {
      if (this.servletMaps == null) {
         return new ServletMappingDescriptor[0];
      } else {
         ServletMappingDescriptor[] var1 = new ServletMappingDescriptor[this.servletMaps.size()];
         return (ServletMappingDescriptor[])((ServletMappingDescriptor[])this.servletMaps.toArray(var1));
      }
   }

   public void setServletMappings(ServletMappingMBean[] var1) {
      ServletMappingMBean[] var2 = this.getServletMappings();
      this.servletMaps = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.servletMaps.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("servletMappings", var2, var1);
         }

      }
   }

   public void addServletMapping(ServletMappingMBean var1) {
      if (this.servletMaps == null) {
         this.servletMaps = new ArrayList();
      }

      this.servletMaps.add(var1);
   }

   public void removeServletMapping(ServletMappingMBean var1) {
      if (this.servletMaps != null) {
         this.servletMaps.remove(var1);
      }

   }

   public SessionConfigMBean getSessionConfig() {
      return this.sessionConfig;
   }

   public void setSessionConfig(SessionConfigMBean var1) {
      this.sessionConfig = var1;
   }

   public MimeMappingMBean[] getMimeMappings() {
      if (this.mimeMaps == null) {
         return new MimeMappingDescriptor[0];
      } else {
         MimeMappingDescriptor[] var1 = new MimeMappingDescriptor[this.mimeMaps.size()];
         this.mimeMaps.toArray(var1);
         return var1;
      }
   }

   public void setMimeMappings(MimeMappingMBean[] var1) {
      MimeMappingMBean[] var2 = this.getMimeMappings();
      this.mimeMaps = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.mimeMaps.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("mimeMappings", var2, var1);
         }

      }
   }

   public void addMimeMapping(MimeMappingMBean var1) {
      if (this.mimeMaps == null) {
         this.mimeMaps = new ArrayList();
      }

      this.mimeMaps.add(var1);
   }

   public void removeMimeMapping(MimeMappingMBean var1) {
      if (this.mimeMaps != null) {
         this.mimeMaps.remove(var1);
      }
   }

   public WelcomeFileListMBean getWelcomeFiles() {
      return this.welcomeFiles;
   }

   public void setWelcomeFiles(WelcomeFileListMBean var1) {
      this.welcomeFiles = var1;
   }

   public ErrorPageMBean[] getErrorPages() {
      if (this.errorPages == null) {
         return new ErrorPageDescriptor[0];
      } else {
         ErrorPageDescriptor[] var1 = new ErrorPageDescriptor[this.errorPages.size()];
         this.errorPages.toArray(var1);
         return var1;
      }
   }

   public void setErrorPages(ErrorPageMBean[] var1) {
      ErrorPageMBean[] var2 = this.getErrorPages();
      this.errorPages = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.errorPages.add(new ErrorPageDescriptor(var1[var3]));
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("errorPages", var2, var1);
         }

      }
   }

   public void addErrorPage(ErrorPageMBean var1) {
      if (this.errorPages == null) {
         this.errorPages = new ArrayList();
      }

      this.errorPages.add(var1);
   }

   public void removeErrorPage(ErrorPageMBean var1) {
      this.errorPages.remove(var1);
   }

   public EnvEntryMBean[] getEnvironmentEntries() {
      if (this.envEntries == null) {
         return new EnvEntryMBean[0];
      } else {
         EnvironmentEntry[] var1 = new EnvironmentEntry[this.envEntries.size()];
         return (EnvEntryMBean[])((EnvEntryMBean[])this.envEntries.toArray(var1));
      }
   }

   public void setEnvironmentEntries(EnvEntryMBean[] var1) {
      EnvEntryMBean[] var2 = this.getEnvironmentEntries();
      this.envEntries = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.envEntries.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("environmentEntries", var2, var1);
         }

      }
   }

   public void addEnvironmentEntry(EnvEntryMBean var1) {
      if (this.envEntries == null) {
         this.envEntries = new ArrayList();
      }

      this.envEntries.add(var1);
   }

   public void removeEnvironmentEntry(EnvEntryMBean var1) {
      if (this.envEntries != null) {
         this.envEntries.remove(var1);
      }
   }

   public ResourceRefMBean[] getResourceReferences() {
      if (this.resourceRefs == null) {
         return new ResourceReference[0];
      } else {
         ResourceReference[] var1 = new ResourceReference[this.resourceRefs.size()];
         return (ResourceRefMBean[])((ResourceRefMBean[])this.resourceRefs.toArray(var1));
      }
   }

   public void setResourceReferences(ResourceRefMBean[] var1) {
      ResourceRefMBean[] var2 = this.getResourceReferences();
      this.resourceRefs = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.resourceRefs.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("resourceReferences", var2, var1);
         }

      }
   }

   public void addResourceReference(ResourceRefMBean var1) {
      if (this.resourceRefs == null) {
         this.resourceRefs = new ArrayList();
      }

      this.resourceRefs.add(var1);
   }

   public void removeResourceReference(ResourceRefMBean var1) {
      if (this.resourceRefs != null) {
         this.resourceRefs.remove(var1);
      }

   }

   public ResourceReference getResourceReference(String var1) {
      if (this.resourceRefs == null) {
         return null;
      } else {
         Iterator var2 = this.resourceRefs.iterator();

         ResourceReference var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (ResourceReference)var2.next();
         } while(!var1.equals(var3.getRefName()));

         return var3;
      }
   }

   public ResourceEnvRefMBean[] getResourceEnvRefs() {
      if (this.resourceEnvRefs == null) {
         return new ResourceEnvRefMBean[0];
      } else {
         ResourceEnvRef[] var1 = new ResourceEnvRef[this.resourceEnvRefs.size()];
         return (ResourceEnvRefMBean[])((ResourceEnvRefMBean[])this.resourceEnvRefs.toArray(var1));
      }
   }

   public void setResourceEnvRefs(ResourceEnvRefMBean[] var1) {
      ResourceEnvRefMBean[] var2 = this.getResourceEnvRefs();
      this.resourceEnvRefs = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.resourceEnvRefs.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("resourceEnvRefs", var2, var1);
         }

      }
   }

   public void addResourceEnvRef(ResourceEnvRefMBean var1) {
      if (this.resourceEnvRefs == null) {
         this.resourceEnvRefs = new ArrayList();
      }

      this.resourceEnvRefs.add(var1);
   }

   public void removeResourceEnvRef(ResourceEnvRefMBean var1) {
      if (this.resourceEnvRefs != null) {
         this.resourceEnvRefs.remove(var1);
      }
   }

   public EjbRefMBean[] getEJBReferences() {
      if (this.ejbRefs == null) {
         return new EjbRefMBean[0];
      } else {
         EJBReference[] var1 = new EJBReference[this.ejbRefs.size()];
         return (EjbRefMBean[])((EjbRefMBean[])this.ejbRefs.toArray(var1));
      }
   }

   public void setEJBReferences(EjbRefMBean[] var1) {
      EjbRefMBean[] var2 = this.getEJBReferences();
      this.ejbRefs = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.ejbRefs.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("ejbReferences", var2, var1);
         }

      }
   }

   public void addEJBReference(EjbRefMBean var1) {
      if (this.ejbRefs == null) {
         this.ejbRefs = new ArrayList();
      }

      this.ejbRefs.add(var1);
   }

   public void removeEJBReference(EjbRefMBean var1) {
      if (this.ejbRefs != null) {
         this.ejbRefs.remove(var1);
      }

   }

   public EJBReference getEJBReference(String var1) {
      if (this.ejbRefs == null) {
         return null;
      } else {
         Iterator var2 = this.ejbRefs.iterator();

         EJBReference var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (EJBReference)var2.next();
         } while(!var1.equals(var3.getEJBRefName()));

         return var3;
      }
   }

   public EjbRefMBean[] getEJBLocalReferences() {
      if (this.ejbLocalRefs == null) {
         return new EjbRefMBean[0];
      } else {
         EjbRefMBean[] var1 = new EjbRefMBean[this.ejbLocalRefs.size()];
         return (EjbRefMBean[])((EjbRefMBean[])this.ejbLocalRefs.toArray(var1));
      }
   }

   public void setEJBLocalReferences(EjbRefMBean[] var1) {
      EjbRefMBean[] var2 = this.getEJBLocalReferences();
      this.ejbLocalRefs = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.ejbLocalRefs.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("ejbLocalReferences", var2, var1);
         }

      }
   }

   public void addEJBLocalReference(EjbRefMBean var1) {
      if (this.ejbLocalRefs == null) {
         this.ejbLocalRefs = new ArrayList();
      }

      this.ejbLocalRefs.add(var1);
   }

   public void removeEJBLocalReference(EjbRefMBean var1) {
      if (this.ejbLocalRefs != null) {
         this.ejbLocalRefs.remove(var1);
      }

   }

   public EJBLocalReference getEJBLocalReference(String var1) {
      if (this.ejbLocalRefs == null) {
         return null;
      } else {
         Iterator var2 = this.ejbLocalRefs.iterator();

         EJBLocalReference var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (EJBLocalReference)var2.next();
         } while(!var1.equals(var3.getEJBRefName()));

         return var3;
      }
   }

   public int getSessionTimeout() {
      SessionDescriptor var1 = (SessionDescriptor)this.getSessionConfig();
      return var1 != null ? var1.getSessionTimeout() : -2;
   }

   public TagLibMBean[] getTagLibs() {
      if (this.taglibs == null) {
         return new TaglibDescriptor[0];
      } else {
         TaglibDescriptor[] var1 = new TaglibDescriptor[this.taglibs.size()];
         return (TagLibMBean[])((TagLibMBean[])this.taglibs.toArray(var1));
      }
   }

   public void setTagLibs(TagLibMBean[] var1) {
      TagLibMBean[] var2 = this.getTagLibs();
      this.taglibs = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.taglibs.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("taglibs", var2, var1);
         }

      }
   }

   public void addTagLib(TagLibMBean var1) {
      if (this.taglibs == null) {
         this.taglibs = new ArrayList();
      }

      this.taglibs.add(var1);
   }

   public void removeTagLib(TagLibMBean var1) {
      if (this.taglibs != null) {
         this.taglibs.remove(var1);
      }
   }

   public SecurityRoleMBean[] getSecurityRoles() {
      if (this.secRoles == null) {
         return new SecurityRoleMBean[0];
      } else {
         SecurityRoleMBean[] var1 = new SecurityRoleMBean[this.secRoles.size()];
         return (SecurityRoleMBean[])((SecurityRoleMBean[])this.secRoles.toArray(var1));
      }
   }

   public void setSecurityRoles(SecurityRoleMBean[] var1) {
      SecurityRoleMBean[] var2 = this.getSecurityRoles();
      this.secRoles = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.secRoles.add(var1[var3]);
      }

   }

   public void addSecurityRole(SecurityRoleMBean var1) {
      if (this.secRoles == null) {
         this.secRoles = new ArrayList();
      }

      this.secRoles.add(var1);
   }

   public void removeSecurityRole(SecurityRoleMBean var1) {
      if (this.secRoles != null) {
         this.secRoles.remove(var1);
      }
   }

   public String[] getSecurityRoleNames() {
      SecurityRoleDescriptor[] var1 = (SecurityRoleDescriptor[])((SecurityRoleDescriptor[])this.getSecurityRoles());
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = var1[var3].getRoleName();
      }

      return var2;
   }

   public SecurityConstraintMBean[] getSecurityConstraints() {
      if (this.secCons == null) {
         return new SecurityConstraint[0];
      } else {
         SecurityConstraint[] var1 = new SecurityConstraint[this.secCons.size()];
         this.secCons.toArray(var1);
         return var1;
      }
   }

   public void setSecurityConstraints(SecurityConstraintMBean[] var1) {
      SecurityConstraintMBean[] var2 = this.getSecurityConstraints();
      this.secCons = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.secCons.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("securityConstraints", var2, var1);
         }

      }
   }

   public void addSecurityConstraint(SecurityConstraintMBean var1) {
      if (this.secCons == null) {
         this.secCons = new ArrayList();
      }

      this.secCons.add(var1);
   }

   public void removeSecurityConstraint(SecurityConstraintMBean var1) {
      if (this.secCons != null) {
         this.secCons.remove(var1);
      }
   }

   public LoginConfigMBean getLoginConfig() {
      return this.login;
   }

   public void setLoginConfig(LoginConfigMBean var1) {
      this.login = var1;
   }

   private ParameterDescriptor getPD(String var1) {
      ParameterDescriptor[] var2 = (ParameterDescriptor[])((ParameterDescriptor[])this.getContextParams());

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var1.equals(var2[var3].getParamName())) {
            return var2[var3];
         }
      }

      return null;
   }

   private boolean getBooleanPD(String var1, boolean var2) {
      ParameterDescriptor var3 = this.getPD(var1);
      return var3 == null ? var2 : "true".equalsIgnoreCase(var3.getParamValue());
   }

   private String getStringPD(String var1, String var2) {
      ParameterDescriptor var3 = this.getPD(var1);
      return var3 == null ? var2 : var3.getParamValue();
   }

   private int getIntPD(String var1, int var2) {
      ParameterDescriptor var3 = this.getPD(var1);
      if (var3 == null) {
         return var2;
      } else {
         String var4 = var3.getParamValue();
         return var4 != null && (var4 = var4.trim()).length() != 0 ? Integer.parseInt(var4) : var2;
      }
   }

   private void addPD(String var1, String var2, String var3) {
      ParameterDescriptor var4 = new ParameterDescriptor(var1, var2, var3);
      ParameterDescriptor[] var5 = (ParameterDescriptor[])((ParameterDescriptor[])this.getContextParams());
      ParameterDescriptor[] var6 = new ParameterDescriptor[var5.length + 1];
      System.arraycopy(var5, 0, var6, 0, var5.length);
      var6[var5.length] = var4;
      this.setContextParams(var6);
   }

   public boolean getWLJSPPrecompile() {
      return this.getBooleanPD("weblogic.jsp.precompile", false);
   }

   public void setWLJSPPrecompile(boolean var1) {
      String var2 = "weblogic.jsp.precompile";
      ParameterDescriptor var3 = this.getPD(var2);
      if (var3 == null) {
         this.addPD(var2, "" + var1, "Controls whether to precompile all web-app JSP's on server startup");
      } else {
         var3.setParamValue("" + var1);
      }

   }

   public String getWLJSPCompileCommand() {
      return this.getStringPD("weblogic.jsp.compileCommand", "javac");
   }

   public void setWLJSPCompileCommand(String var1) {
      String var2 = "weblogic.jsp.compileCommand";
      ParameterDescriptor var3 = this.getPD(var2);
      if (var3 == null) {
         this.addPD(var2, var1, "java compiler executable used to generate JSP pages");
      } else {
         var3.setParamValue(var1);
      }

   }

   public String getWLJSPCompileClass() {
      return this.getStringPD("weblogic.jsp.compilerclass", (String)null);
   }

   public void setWLJSPCompileClass(String var1) {
      String var2 = "weblogic.jsp.compilerclass";
      ParameterDescriptor var3 = this.getPD(var2);
      if (var3 == null) {
         this.addPD(var2, var1, "java compiler executable used to generate JSP pages");
      } else {
         var3.setParamValue(var1);
      }

   }

   public boolean getWLJSPVerbose() {
      return this.getBooleanPD("weblogic.jsp.verbose", true);
   }

   public void setWLJSPVerbose(boolean var1) {
      String var2 = "weblogic.jsp.verbose";
      ParameterDescriptor var3 = this.getPD(var2);
      if (var3 == null) {
         this.addPD(var2, "" + var1, "Enables browser-friendly JSP error reporting and verbose JSP logging");
      } else {
         var3.setParamValue("" + var1);
      }

   }

   public String getWLJSPPackagePrefix() {
      return this.getStringPD("weblogic.jsp.packagePrefix", "jsp_servlet");
   }

   public void setWLJSPPackagePrefix(String var1) {
      String var2 = "weblogic.jsp.packagePrefix";
      ParameterDescriptor var3 = this.getPD(var2);
      if (var3 == null) {
         this.addPD(var2, var1, "java package prefix for generated JSP code");
      } else {
         var3.setParamValue(var1);
      }

   }

   public String getWLJSPEncoding() {
      return this.getStringPD("weblogic.jsp.encoding", "");
   }

   public void setWLJSPEncoding(String var1) {
      String var2 = "weblogic.jsp.encoding";
      ParameterDescriptor var3 = this.getPD(var2);
      if (var3 == null) {
         this.addPD(var2, var1, "default i18n charset for JSP pages in this web application");
      } else {
         var3.setParamValue(var1);
      }

   }

   public boolean getWLJSPKeepgenerated() {
      return this.getBooleanPD("weblogic.jsp.keepgenerated", false);
   }

   public void setWLJSPKeepgenerated(boolean var1) {
      String var2 = "weblogic.jsp.keepgenerated";
      ParameterDescriptor var3 = this.getPD(var2);
      if (var3 == null) {
         this.addPD(var2, "" + var1, "Enables saving generated JSP sources in the JSP working directory - useful for debugging");
      } else {
         var3.setParamValue("" + var1);
      }

   }

   public boolean getWLSessionPersistence() {
      return this.getBooleanPD("weblogic.httpd.session.persistence", false);
   }

   public void setWLSessionPersistence(boolean var1) {
      String var2 = "weblogic.httpd.session.persistence";
      ParameterDescriptor var3 = this.getPD(var2);
      if (var3 == null) {
         this.addPD(var2, "" + var1, "Enables one of WebLogic's persistent HTTP session stores");
      } else {
         var3.setParamValue("" + var1);
      }

   }

   public String getWLSessionPersistenceType() {
      return this.getStringPD("weblogic.httpd.session.persistentStoreType", "file");
   }

   public void setWLSessionPersistenceType(String var1) {
      if (!"file".equals(var1) && !"replicated".equals(var1) && !"replicated_if_clustered".equals(var1) && !"jdbc".equals(var1) && !"cookie".equals(var1)) {
         throw new IllegalArgumentException("persistent store type must be one of: 'memory'|'file'|'replicated'|'replicated_if_clustered'|'jdbc'|'cookie' not '" + var1 + "'");
      } else {
         String var2 = "weblogic.httpd.session.persistentStoreType";
         ParameterDescriptor var3 = this.getPD(var2);
         if (var3 == null) {
            this.addPD(var2, var1, "Selects the type of WebLogic's persistent HTTP session stores");
         } else {
            var3.setParamValue(var1);
         }

      }
   }

   public boolean getWLSessionDebug() {
      return this.getBooleanPD("weblogic.httpd.session.debug", false);
   }

   public void setWLSessionDebug(boolean var1) {
      String var2 = "weblogic.httpd.session.debug";
      ParameterDescriptor var3 = this.getPD(var2);
      if (var3 == null) {
         this.addPD(var2, "" + var1, "Enables verbose logging of session activity for debugging");
      } else {
         var3.setParamValue("" + var1);
      }

   }

   public int getWLSessionIDLength() {
      return this.getIntPD("weblogic.httpd.session.IDLength", 52);
   }

   public void setWLSessionIDLength(int var1) {
      String var2 = "weblogic.httpd.session.IDLength";
      if (var1 >= 10 && var1 <= 200) {
         ParameterDescriptor var3 = this.getPD(var2);
         if (var3 == null) {
            this.addPD(var2, "" + var1, "Specifies the length in chars of the random part of generated session IDs");
         } else {
            var3.setParamValue("" + var1);
         }

      } else {
         throw new IllegalArgumentException("sessionid length must be > 10 && < 200: not " + var1);
      }
   }

   public int getWLSessionInvalidationInterval() {
      return this.getIntPD("weblogic.httpd.session.invalidationIntervalSecs", 60);
   }

   public void setWLSessionInvalidationInterval(int var1) {
      String var2 = "weblogic.httpd.session.invalidationIntervalSecs";
      if (var1 < 20) {
         throw new IllegalArgumentException("invalidation interval seconds must be > 20: not " + var1);
      } else {
         ParameterDescriptor var3 = this.getPD(var2);
         if (var3 == null) {
            this.addPD(var2, "" + var1, "Specifies the frequency in seconds where stored sessions are checked for expiration");
         } else {
            var3.setParamValue("" + var1);
         }

      }
   }

   public int getWLCookieAge() {
      return this.getIntPD("weblogic.httpd.session.cookie.maxAgeSecs", -1);
   }

   public void setWLCookieAge(int var1) {
      String var2 = "weblogic.httpd.session.cookie.maxAgeSecs";
      if (var1 <= 0 && var1 != -1) {
         throw new IllegalArgumentException("cookie timeout must be >0 || == -1: not " + var1);
      } else {
         ParameterDescriptor var3 = this.getPD(var2);
         if (var3 == null) {
            this.addPD(var2, "" + var1, "Specifies in seconds the 'expires' field of HTTP cookies used for session tracking");
         } else {
            var3.setParamValue("" + var1);
         }

      }
   }

   public String getWLSessionPersistentStoreDir() {
      return this.getStringPD("weblogic.httpd.session.persistentStoreDir", "session_db");
   }

   public void setWLSessionPersistentStoreDir(String var1) {
      String var2 = "weblogic.httpd.session.persistentStoreDir";
      ParameterDescriptor var3 = this.getPD(var2);
      if (var3 == null) {
         this.addPD(var2, var1, "filesystem path (absolute or relative to server home) to be used for file persistence");
      } else {
         var3.setParamValue(var1);
      }

   }

   public String getWLSessionPersistentStoreCookieName() {
      return this.getStringPD("weblogic.httpd.session.persistentStoreCookieName", "WLCOOKIE");
   }

   public void setWLSessionPersistentStoreCookieName(String var1) {
      String var2 = "weblogic.httpd.session.persistentStoreCookieName";
      ParameterDescriptor var3 = this.getPD(var2);
      if (var3 == null) {
         this.addPD(var2, var1, "Cookie name used to store the attributes for a cookie based session persistence");
      } else {
         var3.setParamValue(var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
      boolean var2 = true;
      Iterator var1;
      if (this.contextParams != null) {
         for(var1 = this.contextParams.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.servlets != null) {
         for(var1 = this.servlets.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.servletMaps != null) {
         for(var1 = this.servletMaps.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.sessionConfig != null) {
         var2 &= this.check(this.sessionConfig);
      }

      if (this.mimeMaps != null) {
         for(var1 = this.mimeMaps.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.welcomeFiles != null) {
         var2 &= this.check(this.welcomeFiles);
      }

      if (this.errorPages != null) {
         for(var1 = this.errorPages.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.resourceRefs != null) {
         for(var1 = this.resourceRefs.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.resourceEnvRefs != null) {
         for(var1 = this.resourceEnvRefs.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.envEntries != null) {
         for(var1 = this.envEntries.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.ejbRefs != null) {
         for(var1 = this.ejbRefs.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.taglibs != null) {
         for(var1 = this.taglibs.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.secRoles != null) {
         for(var1 = this.secRoles.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.secCons != null) {
         for(var1 = this.secCons.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.eventListeners != null) {
         for(var1 = this.eventListeners.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      if (this.filters != null) {
         for(var1 = this.filters.iterator(); var1.hasNext(); var2 &= this.check((WebElementMBean)var1.next())) {
         }
      }

      WebElementMBean var3;
      if (this.filterMappings != null) {
         for(var1 = this.filterMappings.iterator(); var1.hasNext(); var2 &= this.check(var3)) {
            var3 = (WebElementMBean)var1.next();
         }
      }

      if (this.login != null) {
         var2 &= this.check(this.login);
      }

      if (!var2) {
         String[] var4 = this.getDescriptorErrors();
         throw new DescriptorValidationException(this.arrayToString(var4));
      }
   }

   public String toXML(int var1) {
      String var2 = "";
      String var3 = this.getEncoding();
      if (var3 != null) {
         var2 = var2 + "<?xml version=\"1.0\" encoding=\"" + var3 + "\"?>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<!DOCTYPE web-app PUBLIC \"-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN\" \"http://java.sun.com/dtd/web-app_2_3.dtd\">";
      var2 = var2 + "\n" + this.indentStr(var1) + "<web-app>\n";
      var1 += 2;
      if (this.uiData != null) {
         var2 = var2 + "\n" + this.uiData.toXML(var1);
      }

      if (this.distributable) {
         var2 = var2 + "\n" + this.indentStr(var1) + "<distributable/>\n";
      }

      Iterator var4;
      ParameterDescriptor var5;
      if (this.contextParams != null) {
         for(var4 = this.contextParams.iterator(); var4.hasNext(); var2 = var2 + "\n" + var5.toXML(var1)) {
            var5 = (ParameterDescriptor)var4.next();
         }
      }

      FilterDescriptor var9;
      if (this.filters != null) {
         for(var4 = this.filters.iterator(); var4.hasNext(); var2 = var2 + "\n" + var9.toXML(var1)) {
            var9 = (FilterDescriptor)var4.next();
         }
      }

      FilterMappingDescriptor var10;
      if (this.filterMappings != null) {
         for(var4 = this.filterMappings.iterator(); var4.hasNext(); var2 = var2 + "\n" + var10.toXML(var1)) {
            var10 = (FilterMappingDescriptor)var4.next();
         }
      }

      ListenerDescriptor var11;
      if (this.eventListeners != null) {
         for(var4 = this.eventListeners.iterator(); var4.hasNext(); var2 = var2 + "\n" + var11.toXML(var1)) {
            var11 = (ListenerDescriptor)var4.next();
         }
      }

      ServletDescriptor var12;
      if (this.servlets != null) {
         for(var4 = this.servlets.iterator(); var4.hasNext(); var2 = var2 + "\n" + var12.toXML(var1)) {
            var12 = (ServletDescriptor)var4.next();
         }
      }

      ServletMappingDescriptor var13;
      if (this.servletMaps != null) {
         for(var4 = this.servletMaps.iterator(); var4.hasNext(); var2 = var2 + "\n" + var13.toXML(var1)) {
            var13 = (ServletMappingDescriptor)var4.next();
         }
      }

      if (this.sessionConfig != null) {
         var2 = var2 + "\n" + this.sessionConfig.toXML(var1);
      }

      MimeMappingDescriptor var14;
      if (this.mimeMaps != null) {
         for(var4 = this.mimeMaps.iterator(); var4.hasNext(); var2 = var2 + "\n" + var14.toXML(var1)) {
            var14 = (MimeMappingDescriptor)var4.next();
         }
      }

      if (this.welcomeFiles != null) {
         var2 = var2 + "\n" + this.welcomeFiles.toXML(var1);
      }

      ErrorPageDescriptor var15;
      if (this.errorPages != null) {
         for(var4 = this.errorPages.iterator(); var4.hasNext(); var2 = var2 + "\n" + var15.toXML(var1)) {
            var15 = (ErrorPageDescriptor)var4.next();
         }
      }

      TaglibDescriptor var16;
      if (this.taglibs != null) {
         for(var4 = this.taglibs.iterator(); var4.hasNext(); var2 = var2 + "\n" + var16.toXML(var1)) {
            var16 = (TaglibDescriptor)var4.next();
         }
      }

      if (this.resourceEnvRefs != null) {
         var4 = this.resourceEnvRefs.iterator();

         while(var4.hasNext()) {
            ResourceEnvRef var17 = (ResourceEnvRef)var4.next();

            try {
               var17.validate();
            } catch (DescriptorValidationException var8) {
               HTTPLogger.logDescriptorValidationFailure("web.xml", "resource-env-ref", var8);
               continue;
            }

            var2 = var2 + "\n" + var17.toXML(var1);
         }
      }

      if (this.resourceRefs != null) {
         var4 = this.resourceRefs.iterator();

         while(var4.hasNext()) {
            ResourceReference var18 = (ResourceReference)var4.next();

            try {
               var18.validate();
            } catch (DescriptorValidationException var7) {
               HTTPLogger.logDescriptorValidationFailure("web.xml", "resource-ref", var7);
               continue;
            }

            var2 = var2 + "\n" + var18.toXML(var1);
         }
      }

      SecurityConstraint var19;
      if (this.secCons != null) {
         for(var4 = this.secCons.iterator(); var4.hasNext(); var2 = var2 + "\n" + var19.toXML(var1)) {
            var19 = (SecurityConstraint)var4.next();
         }
      }

      if (this.login != null) {
         var2 = var2 + "\n" + this.login.toXML(var1);
      }

      SecurityRoleDescriptor var20;
      if (this.secRoles != null) {
         for(var4 = this.secRoles.iterator(); var4.hasNext(); var2 = var2 + "\n" + var20.toXML(var1)) {
            var20 = (SecurityRoleDescriptor)var4.next();
         }
      }

      EnvironmentEntry var21;
      if (this.envEntries != null) {
         for(var4 = this.envEntries.iterator(); var4.hasNext(); var2 = var2 + "\n" + var21.toXML(var1)) {
            var21 = (EnvironmentEntry)var4.next();
         }
      }

      EJBReference var22;
      if (this.ejbRefs != null) {
         for(var4 = this.ejbRefs.iterator(); var4.hasNext(); var2 = var2 + "\n" + var22.toXML(var1)) {
            var22 = (EJBReference)var4.next();
         }
      }

      if (this.ejbLocalRefs != null) {
         for(var4 = this.ejbLocalRefs.iterator(); var4.hasNext(); var2 = var2 + "\n" + var22.toXML(var1)) {
            var22 = (EJBReference)var4.next();
         }
      }

      var1 -= 2;
      var2 = var2 + "\n" + this.indentStr(var1) + "</web-app>";
      return var2;
   }

   public int compare(Object var1, Object var2) {
      int var3 = 0;
      if (var1 instanceof ServletMBean) {
         ServletMBean var4 = (ServletMBean)var1;
         ServletMBean var5 = (ServletMBean)var2;
         var3 = var4.getServletName().compareTo(var5.getServletName());
      } else if (var1 instanceof FilterMBean) {
         FilterMBean var7 = (FilterMBean)var1;
         FilterMBean var6 = (FilterMBean)var2;
         var3 = var7.getFilterName().compareTo(var6.getFilterName());
      }

      return var3;
   }

   public boolean equals(Object var1) {
      return var1 == this;
   }
}
