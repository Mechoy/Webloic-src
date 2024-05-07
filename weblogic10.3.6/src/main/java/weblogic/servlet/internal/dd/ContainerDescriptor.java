package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.ContainerDescriptorMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class ContainerDescriptor extends BaseServletDescriptor implements ContainerDescriptorMBean {
   private static final String CHECK_AUTH_ON_FORWARD = "check-auth-on-forward";
   private static final String REDIRECT_WITH_ABSOLUTE_URL = "redirect-with-absolute-url";
   private static final String INDEX_DIRECTORY_ENABLED = "index-directory-enabled";
   private static final String FILTER_DISPATCHED_REQUESTS_ENABLED = "filter-dispatched-requests-enabled";
   private static final String SERVLET_RELOAD_CHECK_SECS = "servlet-reload-check-secs";
   private static final String SINGLE_THREADED_MODEL_POOLSIZE = "single-threaded-servlet-pool-size";
   private static final String SESSION_MONITORING_ENABLED = "session-monitoring-enabled";
   private static final String PREFER_WEBINF_CLASSES = "prefer-web-inf-classes";
   private static final String INDEX_DIRECTORY_SORT_BY = "index-directory-sort-by";
   private static final String SAVE_SESSIONS_ENABLED = "save-sessions-enabled";
   private static final String DEFAULT_MIME_TYPE = "default-mime-type";
   private boolean checkAuthOnForward = false;
   private boolean redirectWithAbsoluteURL = true;
   private boolean preferWebInfClasses = false;
   private boolean filterDispatchedRequestsEnabled = true;
   private boolean indexDirectoryEnabled = false;
   private String indexDirectorySortBy = null;
   private boolean sessionMonitoringEnabled = false;
   private boolean saveSessionsEnabled = false;
   private int servletReloadCheckSecs = 1;
   private int singleThreadedServletPoolSize = 5;
   private boolean filterDispatchedRequestsEnabledSet = false;
   private boolean indexDirectoryEnabledSet = false;
   private boolean sessionMonitoringEnabledSet = false;
   private boolean saveSessionsEnabledSet = false;
   private boolean servletReloadCheckSecsSet = false;
   private boolean singleThreadedServletPoolSizeSet = false;
   private boolean preferWebInfClassesSet = false;
   private String defaultMimeType = null;

   public ContainerDescriptor() {
   }

   public ContainerDescriptor(Element var1) throws DOMProcessingException {
      String var2 = DOMUtils.getOptionalValueByTagName(var1, "check-auth-on-forward");
      if (var2 != null && (var2.equals("") || var2.equalsIgnoreCase("true"))) {
         this.checkAuthOnForward = true;
      }

      var2 = DOMUtils.getOptionalValueByTagName(var1, "redirect-with-absolute-url");
      if (var2 != null && var2.equalsIgnoreCase("false")) {
         this.redirectWithAbsoluteURL = false;
      }

      var2 = DOMUtils.getOptionalValueByTagName(var1, "prefer-web-inf-classes");
      if (var2 != null) {
         this.preferWebInfClasses = var2.equalsIgnoreCase("true");
         this.preferWebInfClassesSet = true;
      }

      var2 = DOMUtils.getOptionalValueByTagName(var1, "filter-dispatched-requests-enabled");
      if (var2 != null) {
         this.filterDispatchedRequestsEnabledSet = true;
         this.filterDispatchedRequestsEnabled = var2.equalsIgnoreCase("true");
      }

      var2 = DOMUtils.getOptionalValueByTagName(var1, "index-directory-enabled");
      if (var2 != null) {
         this.indexDirectoryEnabledSet = true;
         this.indexDirectoryEnabled = var2.equalsIgnoreCase("true");
      }

      var2 = DOMUtils.getOptionalValueByTagName(var1, "save-sessions-enabled");
      if (var2 != null) {
         this.saveSessionsEnabledSet = true;
         this.saveSessionsEnabled = var2.equalsIgnoreCase("true");
      }

      this.indexDirectorySortBy = DOMUtils.getOptionalValueByTagName(var1, "index-directory-sort-by");
      if (this.indexDirectorySortBy != null) {
         if (this.indexDirectorySortBy.equalsIgnoreCase("NAME")) {
            this.indexDirectorySortBy = "NAME";
         } else if (this.indexDirectorySortBy.equalsIgnoreCase("LAST_MODIFIED")) {
            this.indexDirectorySortBy = "LAST_MODIFIED";
         } else {
            if (!this.indexDirectorySortBy.equalsIgnoreCase("SIZE")) {
               HTTPLogger.logInvalidIndexDirectorySortBy(this.indexDirectorySortBy);
               throw new DOMProcessingException("Invalid value assigned for element index-directory-sort-by in weblogic.xml: " + this.indexDirectorySortBy);
            }

            this.indexDirectorySortBy = "SIZE";
         }
      }

      var2 = DOMUtils.getOptionalValueByTagName(var1, "session-monitoring-enabled");
      if (var2 != null) {
         this.sessionMonitoringEnabledSet = true;
         this.sessionMonitoringEnabled = var2.equalsIgnoreCase("true");
      }

      var2 = DOMUtils.getOptionalValueByTagName(var1, "servlet-reload-check-secs");
      if (var2 != null) {
         try {
            this.servletReloadCheckSecs = Integer.parseInt(var2);
            this.servletReloadCheckSecsSet = true;
         } catch (NumberFormatException var5) {
            this.servletReloadCheckSecs = 1;
         }
      }

      var2 = DOMUtils.getOptionalValueByTagName(var1, "single-threaded-servlet-pool-size");
      if (var2 != null) {
         try {
            this.singleThreadedServletPoolSize = Integer.parseInt(var2);
            this.singleThreadedServletPoolSizeSet = true;
         } catch (NumberFormatException var4) {
            this.singleThreadedServletPoolSize = 5;
         }
      }

      this.defaultMimeType = DOMUtils.getOptionalValueByTagName(var1, "default-mime-type");
   }

   public boolean isCheckAuthOnForwardEnabled() {
      return this.checkAuthOnForward;
   }

   public void setCheckAuthOnForwardEnabled(boolean var1) {
      boolean var2 = this.checkAuthOnForward;
      this.checkAuthOnForward = var1;
      if (var2 != var1) {
         this.firePropertyChange("checkAuthOnForwardEnabled", new Boolean(!var1), new Boolean(var1));
      }

   }

   public String getRedirectContentType() {
      return null;
   }

   public void setRedirectContentType(String var1) {
   }

   public String getRedirectContent() {
      return null;
   }

   public void setRedirectContent(String var1) {
   }

   public boolean isRedirectWithAbsoluteURLEnabled() {
      return this.redirectWithAbsoluteURL;
   }

   public void setRedirectWithAbsoluteURLEnabled(boolean var1) {
      boolean var2 = this.redirectWithAbsoluteURL;
      this.redirectWithAbsoluteURL = var1;
      if (var2 != var1) {
         this.firePropertyChange("redirectWithAbsoluteURLEnabled", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isFilterDispatchedRequestsEnabled() {
      return this.filterDispatchedRequestsEnabled;
   }

   public void setFilterDispatchedRequestsEnabled(boolean var1) {
      boolean var2 = this.filterDispatchedRequestsEnabled;
      this.filterDispatchedRequestsEnabled = var1;
      if (var2 != var1) {
         this.firePropertyChange("filterDispatchedRequestsEnabled", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isIndexDirectoryEnabled() {
      return this.indexDirectoryEnabled;
   }

   public void setIndexDirectoryEnabled(boolean var1) {
      boolean var2 = this.indexDirectoryEnabled;
      this.indexDirectoryEnabled = var1;
      if (var2 != var1) {
         this.firePropertyChange("indexDirectoryEnabled", new Boolean(!var1), new Boolean(var1));
      }

   }

   public String getIndexDirectorySortBy() {
      return this.indexDirectorySortBy;
   }

   public void setIndexDirectorySortBy(String var1) {
      String var2 = this.indexDirectorySortBy;
      this.indexDirectorySortBy = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("indexDirectorySortBy", var2, var1);
      }

   }

   public boolean isSessionMonitoringEnabled() {
      return this.sessionMonitoringEnabled;
   }

   public void setSessionMonitoringEnabled(boolean var1) {
      boolean var2 = this.sessionMonitoringEnabled;
      this.sessionMonitoringEnabled = var1;
      if (var2 != var1) {
         this.firePropertyChange("sessionMonitoringEnabled", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isSaveSessionsEnabled() {
      return this.saveSessionsEnabled;
   }

   public void setSaveSessionsEnabled(boolean var1) {
      boolean var2 = this.saveSessionsEnabled;
      this.saveSessionsEnabled = var1;
      if (var2 != var1) {
         this.firePropertyChange("saveSessionsEnabled", new Boolean(!var1), new Boolean(var1));
      }

   }

   public int getServletReloadCheckSecs() {
      return this.servletReloadCheckSecs;
   }

   public void setServletReloadCheckSecs(int var1) {
      int var2 = this.servletReloadCheckSecs;
      this.servletReloadCheckSecs = var1;
      if (var2 != var1) {
         this.firePropertyChange("servletReloadCheckSecs", new Integer(var2), new Integer(var1));
      }

   }

   public int getSingleThreadedServletPoolSize() {
      return this.singleThreadedServletPoolSize;
   }

   public void setSingleThreadedServletPoolSize(int var1) {
      int var2 = this.singleThreadedServletPoolSize;
      this.singleThreadedServletPoolSize = var1;
      if (var2 != var1) {
         this.firePropertyChange("singleThreadedServletPoolSize", new Integer(var2), new Integer(var1));
      }

   }

   public boolean isPreferWebInfClasses() {
      return this.preferWebInfClasses;
   }

   public void setPreferWebInfClasses(boolean var1) {
      boolean var2 = this.preferWebInfClasses;
      this.preferWebInfClasses = var1;
      if (var2 != var1) {
         this.firePropertyChange("preferWebInfClasses", new Boolean(!var1), new Boolean(var1));
      }

   }

   public void setDefaultMimeType(String var1) {
      String var2 = this.defaultMimeType;
      this.defaultMimeType = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("defaultMimeType", var2, var1);
      }

   }

   public boolean isFilterDispatchedRequestsEnabledSet() {
      return this.filterDispatchedRequestsEnabledSet;
   }

   public boolean isIndexDirectoryEnabledSet() {
      return this.indexDirectoryEnabledSet;
   }

   public boolean isSaveSessionsEnabledSet() {
      return this.saveSessionsEnabledSet;
   }

   public boolean isSessionMonitoringEnabledSet() {
      return this.sessionMonitoringEnabledSet;
   }

   public boolean isServletReloadCheckSecsSet() {
      return this.servletReloadCheckSecsSet;
   }

   public boolean isSingleThreadedServletPoolSizeSet() {
      return this.singleThreadedServletPoolSizeSet;
   }

   public boolean isPreferWebInfClassesSet() {
      return this.preferWebInfClassesSet;
   }

   public String getDefaultMimeType() {
      return this.defaultMimeType;
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
      if (this.indexDirectorySortBy != null && !this.indexDirectorySortBy.equals("NAME") && !this.indexDirectorySortBy.equals("LAST_MODIFIED") && !this.indexDirectorySortBy.equals("SIZE")) {
         HTTPLogger.logInvalidIndexDirectorySortBy(this.indexDirectorySortBy);
         throw new DescriptorValidationException("Invalid value assigned for element index-directory-sort-by in weblogic.xml: " + this.indexDirectorySortBy);
      }
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      boolean var3 = false;
      if (this.checkAuthOnForward) {
         var2 = var2 + this.indentStr(var1 + 2) + "<" + "check-auth-on-forward" + "/>\n";
         var3 = true;
      }

      if (!this.filterDispatchedRequestsEnabled) {
         var2 = var2 + this.indentStr(var1 + 2) + "<" + "filter-dispatched-requests-enabled" + ">" + this.filterDispatchedRequestsEnabled + "</" + "filter-dispatched-requests-enabled" + ">\n";
         var3 = true;
      }

      if (!this.redirectWithAbsoluteURL) {
         var2 = var2 + this.indentStr(var1 + 2) + "<" + "redirect-with-absolute-url" + ">" + this.redirectWithAbsoluteURL + "</" + "redirect-with-absolute-url" + ">\n";
         var3 = true;
      }

      if (this.indexDirectoryEnabled) {
         var2 = var2 + this.indentStr(var1 + 2) + "<" + "index-directory-enabled" + ">" + this.indexDirectoryEnabled + "</" + "index-directory-enabled" + ">\n";
         var3 = true;
      }

      if (this.indexDirectorySortBy != null) {
         var2 = var2 + this.indentStr(var1 + 2) + "<" + "index-directory-sort-by" + ">" + this.indexDirectorySortBy + "</" + "index-directory-sort-by" + ">\n";
         var3 = true;
      }

      if (this.servletReloadCheckSecs != 1) {
         var2 = var2 + this.indentStr(var1 + 2) + "<" + "servlet-reload-check-secs" + ">" + this.servletReloadCheckSecs + "</" + "servlet-reload-check-secs" + ">\n";
         var3 = true;
      }

      if (this.singleThreadedServletPoolSize != 5) {
         var2 = var2 + this.indentStr(var1 + 2) + "<" + "single-threaded-servlet-pool-size" + ">" + this.singleThreadedServletPoolSize + "</" + "single-threaded-servlet-pool-size" + ">\n";
         var3 = true;
      }

      if (this.sessionMonitoringEnabled) {
         var2 = var2 + this.indentStr(var1 + 2) + "<" + "session-monitoring-enabled" + ">" + this.sessionMonitoringEnabled + "</" + "session-monitoring-enabled" + ">\n";
         var3 = true;
      }

      if (this.saveSessionsEnabled) {
         var2 = var2 + this.indentStr(var1 + 2) + "<" + "save-sessions-enabled" + ">" + this.saveSessionsEnabled + "</" + "save-sessions-enabled" + ">\n";
         var3 = true;
      }

      if (this.preferWebInfClasses) {
         var2 = var2 + this.indentStr(var1 + 2) + "<" + "prefer-web-inf-classes" + ">" + this.preferWebInfClasses + "</" + "prefer-web-inf-classes" + ">\n";
         var3 = true;
      }

      if (this.defaultMimeType != null) {
         var2 = var2 + this.indentStr(var1 + 2) + "<" + "default-mime-type" + ">" + this.defaultMimeType + "</" + "default-mime-type" + ">\n";
         var3 = true;
      }

      return var3 ? this.indentStr(var1) + "<container-descriptor>\n" + var2 + this.indentStr(var1) + "</container-descriptor>\n" : "";
   }
}
