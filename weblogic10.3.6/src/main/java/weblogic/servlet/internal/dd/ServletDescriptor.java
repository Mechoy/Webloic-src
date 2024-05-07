package weblogic.servlet.internal.dd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.ParameterMBean;
import weblogic.management.descriptors.webapp.RunAsMBean;
import weblogic.management.descriptors.webapp.SecurityRoleRefMBean;
import weblogic.management.descriptors.webapp.ServletMBean;
import weblogic.management.descriptors.webapp.UIMBean;
import weblogic.management.descriptors.webappext.ServletDescriptorMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class ServletDescriptor extends BaseServletDescriptor implements ToXML, ServletMBean {
   private static final String SERVLET_NAME = "servlet-name";
   private static final String SERVLET_CLASS = "servlet-class";
   private static final String JSP_FILE = "jsp-file";
   private static final String INIT_PARAM = "init-param";
   private static final String LOAD_ON_STARTUP = "load-on-startup";
   private static final String SECURITY_ROLE_REF = "security-role-ref";
   private static final String RUN_AS = "run-as";
   private String servletName;
   private UIMBean uiData = new UIDescriptor();
   private String servletClass;
   private String jspFile;
   private List initParams;
   private int loadSeq = -1;
   private List secRoleRefs;
   private RunAsMBean runAs;
   private String initAs;
   private String destroyAs;
   private ServletDescriptorMBean servletDescriptor = null;

   public ServletDescriptor() {
   }

   public ServletDescriptor(ServletMBean var1) {
      this.setServletName(var1.getServletName());
      this.setServletClass(var1.getServletClass());
      this.setUIData(var1.getUIData());
      this.setJspFile(var1.getJspFile());
      this.setInitParams(var1.getInitParams());
      this.setLoadOnStartup(var1.getLoadOnStartup());
      this.setSecurityRoleRefs(var1.getSecurityRoleRefs());
      this.setRunAs(var1.getRunAs());
   }

   public ServletDescriptor(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      this.servletName = DOMUtils.getValueByTagName(var2, "servlet-name");
      this.servletClass = DOMUtils.getOptionalValueByTagName(var2, "servlet-class");
      if (this.servletClass == null) {
         this.jspFile = DOMUtils.getOptionalValueByTagName(var2, "jsp-file");
         if (this.jspFile == null) {
            throw new DOMProcessingException("Servlet node does not contain niether servlet-class nor jsp-file nodes");
         }
      }

      this.uiData = new UIDescriptor(var2);
      String var3 = DOMUtils.getOptionalValueByTagName(var2, "load-on-startup");
      if (var3 != null) {
         try {
            this.loadSeq = Integer.parseInt(var3);
         } catch (NumberFormatException var7) {
            this.loadSeq = -1;
         }
      }

      List var4 = DOMUtils.getOptionalElementsByTagName(var2, "init-param");
      Iterator var5 = var4.iterator();
      this.initParams = new ArrayList(var4.size());

      while(var5.hasNext()) {
         this.initParams.add(new ParameterDescriptor((Element)var5.next()));
      }

      var4 = DOMUtils.getOptionalElementsByTagName(var2, "security-role-ref");
      var5 = var4.iterator();
      this.secRoleRefs = new ArrayList(var4.size());

      while(var5.hasNext()) {
         this.secRoleRefs.add(new SecurityRoleRefDescriptor(var1, (Element)var5.next()));
      }

      Element var6 = DOMUtils.getOptionalElementByTagName(var2, "run-as");
      if (var6 != null) {
         this.runAs = new RunAsDescriptor(var6);
      }

   }

   public String toString() {
      return this.getServletName();
   }

   public String getServletName() {
      return this.servletName != null ? this.servletName : "";
   }

   public void setServletName(String var1) {
      String var2 = this.servletName;
      this.servletName = var1;
      this.checkChange("servletName", var2, var1);
   }

   public UIMBean getUIData() {
      return this.uiData;
   }

   public void setUIData(UIMBean var1) {
      if (var1 != null) {
         this.uiData = var1;
      } else {
         this.uiData = new UIDescriptor();
      }

   }

   public String getServlet() {
      return this.servletClass != null ? this.servletClass : this.jspFile;
   }

   public void setServlet(String var1) {
      if (var1.endsWith(".jsp")) {
         this.setJSPFile(var1);
         this.servletClass = null;
      } else {
         this.setServletClass(var1);
         this.jspFile = null;
      }

   }

   public void setServletCode(String var1) {
      this.setServlet(var1);
   }

   public String getServletCode() {
      return this.getServlet();
   }

   public String getServletClass() {
      return this.servletClass;
   }

   public void setServletClass(String var1) {
      String var2 = this.servletClass;
      this.servletClass = var1;
      if (this.servletClass != null && (this.servletClass = this.servletClass.trim()).length() > 0) {
         this.jspFile = null;
      }

      this.checkChange("servletClass", var2, var1);
   }

   public String getJSPFile() {
      return this.jspFile;
   }

   public void setJSPFile(String var1) {
      String var2 = this.jspFile;
      this.jspFile = var1;
      if (this.jspFile != null && (this.jspFile = this.jspFile.trim()).length() > 0) {
         this.servletClass = null;
      }

      this.checkChange("jspFile", var2, var1);
   }

   public ParameterMBean[] getInitParams() {
      if (this.initParams == null) {
         return new ParameterDescriptor[0];
      } else {
         ParameterDescriptor[] var1 = new ParameterDescriptor[this.initParams.size()];
         this.initParams.toArray(var1);
         return var1;
      }
   }

   public void setInitParams(ParameterMBean[] var1) {
      ParameterMBean[] var2 = this.getInitParams();
      this.initParams = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.initParams.add(var1[var3]);
         }

         this.checkChange("initParams", var2, var1);
      }
   }

   public void addInitParam(ParameterMBean var1) {
      if (this.initParams == null) {
         this.initParams = new ArrayList();
      }

      this.initParams.add(var1);
   }

   public void removeInitParam(ParameterMBean var1) {
      if (this.initParams != null) {
         this.initParams.remove(var1);
      }

   }

   public int getLoadSequence() {
      return this.loadSeq;
   }

   public void setLoadSequence(int var1) {
      int var2 = this.loadSeq;
      this.loadSeq = var1;
      this.checkChange("loadSequence", var2, var1);
   }

   public SecurityRoleRefMBean[] getSecurityRoleRefs() {
      if (this.secRoleRefs == null) {
         return new SecurityRoleRefDescriptor[0];
      } else {
         SecurityRoleRefDescriptor[] var1 = new SecurityRoleRefDescriptor[this.secRoleRefs.size()];
         this.secRoleRefs.toArray(var1);
         return (SecurityRoleRefMBean[])var1;
      }
   }

   public void setSecurityRoleRefs(SecurityRoleRefMBean[] var1) {
      SecurityRoleRefMBean[] var2 = this.getSecurityRoleRefs();
      this.secRoleRefs = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.secRoleRefs.add(var1[var3]);
         }

         this.checkChange("securityRoleRefs", var2, var1);
      }
   }

   public void addSecurityRoleRef(SecurityRoleRefMBean var1) {
      if (this.secRoleRefs == null) {
         this.secRoleRefs = new ArrayList();
      }

      this.secRoleRefs.add(var1);
   }

   public void removeSecurityRoleRef(SecurityRoleRefMBean var1) {
      if (this.secRoleRefs != null) {
         this.secRoleRefs.remove(var1);
      }
   }

   public String getSmallIconFileName() {
      return this.uiData.getSmallIconFileName();
   }

   public void setSmallIconFileName(String var1) {
      String var2 = this.getSmallIconFileName();
      this.uiData.setSmallIconFileName(var1);
      this.checkChange("smallIconFileName", var2, var1);
   }

   public String getLargeIconFileName() {
      return this.uiData.getLargeIconFileName();
   }

   public void setLargeIconFileName(String var1) {
      String var2 = this.getLargeIconFileName();
      this.uiData.setLargeIconFileName(var1);
      this.checkChange("largeIconFileName", var2, var1);
   }

   public String getDisplayName() {
      return this.uiData.getDisplayName();
   }

   public void setDisplayName(String var1) {
      String var2 = this.getDisplayName();
      this.uiData.setDisplayName(var1);
      this.checkChange("displayName", var2, var1);
   }

   public String getJspFile() {
      return this.jspFile;
   }

   public void setJspFile(String var1) {
      String var2 = this.jspFile;
      this.jspFile = var1;
      this.checkChange("jspFile", var2, this.jspFile);
   }

   public String getDescription() {
      return this.uiData.getDescription();
   }

   public void setDescription(String var1) {
      String var2 = this.getDescription();
      this.uiData.setDescription(var1);
      this.checkChange("description", var2, var1);
   }

   public int getLoadOnStartup() {
      return this.loadSeq;
   }

   public void setLoadOnStartup(int var1) {
      int var2 = this.loadSeq;
      this.loadSeq = var1;
      this.checkChange("loadOnStartup", var2, var1);
   }

   public void setRunAs(RunAsMBean var1) {
      RunAsMBean var2 = this.runAs;
      this.runAs = var1;
      this.checkChange("runAs", var2, var1);
   }

   public RunAsMBean getRunAs() {
      return this.runAs;
   }

   public void setInitAs(String var1) {
      String var2 = this.initAs;
      this.initAs = var1;
      this.checkChange("initAs", var2, var1);
   }

   public String getInitAs() {
      return this.initAs;
   }

   public void setServletDescriptor(ServletDescriptorMBean var1) {
      this.servletDescriptor = var1;
   }

   public ServletDescriptorMBean getServletDescriptor() {
      return this.servletDescriptor;
   }

   public void setDestroyAs(String var1) {
      String var2 = this.destroyAs;
      this.destroyAs = var1;
      this.checkChange("destroyAs", var2, var1);
   }

   public String getDestroyAs() {
      return this.destroyAs;
   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      String var2 = this.getServletName();
      if (var2 != null && (var2 = var2.trim()).length() != 0) {
         this.setServletName(var2);
      } else {
         this.addDescriptorError("NO_SERVLET_NAME");
         var1 = false;
      }

      var2 = this.getServletClass();
      if (var2 != null) {
         var2 = var2.trim();
         this.setServletClass(var2);
      }

      var2 = this.getJSPFile();
      if (var2 != null) {
         var2 = var2.trim();
         this.setJSPFile(var2);
      }

      String var3 = this.getServletClass();
      String var4 = this.getJSPFile();
      if (var3 != null && var3.length() > 0 && var4 != null && var4.length() > 0) {
         this.addDescriptorError("MULTIPLE_DEFINES_SERVLET_DEF", this.getServletName());
         var1 = false;
      }

      if ((var3 == null || var3.length() == 0) && (var4 == null || var4.length() == 0)) {
         this.addDescriptorError("NO_SERVLET_DEF", this.getServletName());
         var1 = false;
      }

      if (this.getRunAs() != null) {
         this.setRunAs(this.getRunAs());
      }

      if (!var1) {
         throw new DescriptorValidationException();
      }
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<servlet>\n";
      var1 += 2;
      String var3;
      if (this.uiData != null) {
         var3 = this.getSmallIconFileName();
         String var4 = this.getLargeIconFileName();
         if (var3 != null || var4 != null) {
            var2 = var2 + this.indentStr(var1) + "<icon>\n";
            if (var3 != null) {
               var2 = var2 + this.indentStr(var1 + 2) + "<small-icon>" + var3 + "</small-icon>\n";
            }

            if (var4 != null) {
               var2 = var2 + this.indentStr(var1 + 2) + "<large-icon>" + var4 + "</large-icon>\n";
            }

            var2 = var2 + this.indentStr(var1) + "</icon>\n";
         }
      }

      var2 = var2 + this.indentStr(var1) + "<servlet-name>" + this.servletName + "</servlet-name>\n";
      if (this.uiData != null) {
         var3 = this.getDisplayName();
         if (var3 != null && (var3 = var3.trim()).length() > 0) {
            var2 = var2 + this.indentStr(var1) + "<display-name>" + cdata(var3) + "</display-name>\n";
         }

         var3 = this.getDescription();
         if (var3 != null && (var3 = var3.trim()).length() > 0) {
            var2 = var2 + this.indentStr(var1) + "<description>" + cdata(var3) + "</description>\n";
         }
      }

      if (this.servletClass != null) {
         var2 = var2 + this.indentStr(var1) + "<servlet-class>" + this.servletClass + "</servlet-class>\n";
      } else {
         var2 = var2 + this.indentStr(var1) + "<jsp-file>" + this.jspFile + "</jsp-file>\n";
      }

      Iterator var7;
      if (this.initParams != null) {
         for(var7 = this.initParams.iterator(); var7.hasNext(); var2 = var2 + this.indentStr(var1) + "</init-param>\n") {
            ParameterDescriptor var6 = (ParameterDescriptor)var7.next();
            var2 = var2 + this.indentStr(var1) + "<init-param>\n";
            var1 += 2;
            var2 = var2 + this.indentStr(var1) + "<param-name>" + var6.getParamName() + "</param-name>\n";
            var2 = var2 + this.indentStr(var1) + "<param-value>" + var6.getParamValue() + "</param-value>\n";
            String var5 = var6.getDescription();
            if (var5 != null) {
               var2 = var2 + this.indentStr(var1) + "<description>" + var5 + "</description>\n";
            }

            var1 -= 2;
         }
      }

      if (this.runAs != null) {
         var2 = var2 + this.runAs.toXML(var1);
      }

      if (this.loadSeq != -1) {
         var2 = var2 + this.indentStr(var1) + "<load-on-startup>" + this.loadSeq + "</load-on-startup>\n";
      }

      SecurityRoleRefDescriptor var8;
      if (this.secRoleRefs != null) {
         for(var7 = this.secRoleRefs.iterator(); var7.hasNext(); var2 = var2 + var8.toXML(var1)) {
            var8 = (SecurityRoleRefDescriptor)var7.next();
         }
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</servlet>\n";
      return var2;
   }
}
