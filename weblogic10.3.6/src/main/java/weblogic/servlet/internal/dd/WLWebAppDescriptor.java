package weblogic.servlet.internal.dd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.AuthFilterMBean;
import weblogic.management.descriptors.webappext.CharsetParamsMBean;
import weblogic.management.descriptors.webappext.ContainerDescriptorMBean;
import weblogic.management.descriptors.webappext.JspDescriptorMBean;
import weblogic.management.descriptors.webappext.PreprocessorMBean;
import weblogic.management.descriptors.webappext.PreprocessorMappingMBean;
import weblogic.management.descriptors.webappext.ReferenceDescriptorMBean;
import weblogic.management.descriptors.webappext.RunAsRoleAssignmentMBean;
import weblogic.management.descriptors.webappext.SecurityPermissionMBean;
import weblogic.management.descriptors.webappext.SecurityRoleAssignmentMBean;
import weblogic.management.descriptors.webappext.ServletDescriptorMBean;
import weblogic.management.descriptors.webappext.SessionDescriptorMBean;
import weblogic.management.descriptors.webappext.URLMatchMapMBean;
import weblogic.management.descriptors.webappext.VirtualDirectoryMappingMBean;
import weblogic.management.descriptors.webappext.WebAppExtDescriptorMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class WLWebAppDescriptor extends BaseServletDescriptor implements ToXML, WebAppExtDescriptorMBean, DescriptorConstants {
   private static final long serialVersionUID = -7317362224751399947L;
   private static final String CHARSET_PARAMS = "charset-params";
   private static final String CONTAINER_DESCRIPTOR = "container-descriptor";
   private static final String CONTEXT_ROOT = "context-root";
   private static final String DISPATCH_POLICY = "wl-dispatch-policy";
   private static final String DESCRIPTION = "description";
   private static final String VERSION = "weblogic-version";
   private static final String S_R_ASSIGNMENT = "security-role-assignment";
   private static final String REFERENCE = "reference-descriptor";
   private static final String SESSION = "session-descriptor";
   private static final String JSP = "jsp-descriptor";
   private static final String AUTH_FILTER = "auth-filter";
   private static final String VIRTUAL_DIRECTORY_MAPPING = "virtual-directory-mapping";
   private static final String URL_MATCH_MAP = "url-match-map";
   private static final String PREPROCESSOR = "preprocessor";
   private static final String PREPROCESSOR_MAPPING = "preprocessor-mapping";
   private static final String SECURITY_PERMISSION = "security-permission";
   private static final String INIT_AS = "init-as";
   private static final String DESTROY_AS = "destroy-as";
   private static final String SERVLET_DESCRIPTOR = "servlet-descriptor";
   private static final String RUN_AS_ROLE_ASSIGNMENT = "run-as-role-assignment";
   private String wlVersion;
   private String wlDescription;
   private SecurityRoleAssignmentMBean[] securityRoleAssignments;
   private VirtualDirectoryMappingMBean[] virtualDirectories;
   private ReferenceDescriptorMBean refDes;
   private SessionDescriptorMBean sessDes;
   private JspDescriptorMBean jspDes;
   private AuthFilterMBean wlAuthFilter;
   private URLMatchMapMBean urlMatchMap;
   private ContainerDescriptorMBean containerDescriptor;
   private CharsetParamsMBean charsetParams;
   private PreprocessorMBean[] preprocessors;
   private PreprocessorMappingMBean[] preprocessorMaps;
   private SecurityPermissionMBean securityPermission;
   private String contextRoot;
   private String dispatchPolicy;
   private ServletDescriptorMBean[] servletDescriptors;
   private ArrayList migrationServletDescriptors;
   private RunAsRoleAssignmentMBean[] runAsRoleAssignments;
   private String descriptorEncoding = null;
   private String descriptorVersion = null;

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

   public WLWebAppDescriptor() {
      this.jspDes = new WLJspDescriptor();
      this.sessDes = new WLSessionDescriptor();
   }

   public WLWebAppDescriptor(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      this.wlDescription = DOMUtils.getOptionalValueByTagName(var2, "description");
      this.wlVersion = DOMUtils.getOptionalValueByTagName(var2, "weblogic-version");
      String var6 = DOMUtils.getOptionalValueByTagName(var2, "auth-filter");
      if (var6 != null && var6.trim().length() > 0) {
         this.wlAuthFilter = new AuthFilter(var6);
      }

      String var7 = DOMUtils.getOptionalValueByTagName(var2, "url-match-map");
      if (var7 != null && var7.length() != 0) {
         this.urlMatchMap = new URLMatchMapDescriptor(var7);
      }

      Element var5 = DOMUtils.getOptionalElementByTagName(var2, "container-descriptor");
      if (var5 != null) {
         this.containerDescriptor = new ContainerDescriptor(var5);
      }

      var5 = DOMUtils.getOptionalElementByTagName(var2, "security-permission");
      if (var5 != null) {
         this.securityPermission = new SecurityPermissionDescriptor(var5);
      }

      var5 = DOMUtils.getOptionalElementByTagName(var2, "charset-params");
      if (var5 != null) {
         this.charsetParams = new CharsetParams(var5);
      }

      List var3 = DOMUtils.getOptionalElementsByTagName(var2, "security-role-assignment");
      Iterator var4 = var3.iterator();
      SecurityRoleAssignmentMBean[] var8 = new SecurityRoleAssignmentMBean[var3.size()];

      int var9;
      for(var9 = 0; var4.hasNext() && var9 < var8.length; ++var9) {
         var8[var9] = new SecurityRoleAssignment(var1, (Element)var4.next());
      }

      this.securityRoleAssignments = var8;
      var3 = DOMUtils.getOptionalElementsByTagName(var2, "servlet-descriptor");
      int var10;
      if (var3.size() > 0) {
         var4 = var3.iterator();
         ServletDescriptorMBean[] var14 = new ServletDescriptorMBean[var3.size()];

         for(var10 = 0; var10 < var14.length; ++var10) {
            var14[var10] = new WLServletDescriptor(var1, (Element)var4.next());
         }

         this.servletDescriptors = var14;
      } else {
         this.servletDescriptors = new ServletDescriptorMBean[0];
      }

      var3 = DOMUtils.getOptionalElementsByTagName(var2, "init-as");
      boolean var11;
      int var12;
      WLServletDescriptor var21;
      if (var3.size() > 0) {
         var4 = var3.iterator();

         for(var9 = 0; var9 < var3.size(); ++var9) {
            InitAsDescriptor var15 = new InitAsDescriptor(var1, (Element)var4.next());
            var11 = false;
            if (this.servletDescriptors != null) {
               for(var12 = 0; var12 < this.servletDescriptors.length; ++var12) {
                  if (this.servletDescriptors[var12].getServletName().equals(var15.getServletName())) {
                     this.servletDescriptors[var12].setInitAsPrincipalName(var15.getPrincipalName());
                     var11 = true;
                  }
               }
            }

            if (!var11) {
               var21 = new WLServletDescriptor(var1, var15.getServletName());
               var21.setInitAsPrincipalName(var15.getPrincipalName());
               if (this.migrationServletDescriptors == null) {
                  this.migrationServletDescriptors = new ArrayList();
               }

               this.migrationServletDescriptors.add(var21);
            }
         }
      }

      var3 = DOMUtils.getOptionalElementsByTagName(var2, "destroy-as");
      if (var3.size() > 0) {
         var4 = var3.iterator();

         for(var9 = 0; var9 < var3.size(); ++var9) {
            DestroyAsDescriptor var16 = new DestroyAsDescriptor(var1, (Element)var4.next());
            var11 = false;
            if (this.servletDescriptors != null) {
               for(var12 = 0; var12 < this.servletDescriptors.length; ++var12) {
                  if (this.servletDescriptors[var12].getServletName().equals(var16.getServletName())) {
                     this.servletDescriptors[var12].setDestroyAsPrincipalName(var16.getPrincipalName());
                     var11 = true;
                  }
               }
            }

            if (!var11 && this.migrationServletDescriptors != null) {
               for(Iterator var22 = this.migrationServletDescriptors.iterator(); var22.hasNext(); var11 = true) {
                  ServletDescriptorMBean var13 = (ServletDescriptorMBean)var22.next();
                  var13.setDestroyAsPrincipalName(var16.getPrincipalName());
               }
            }

            if (!var11) {
               var21 = new WLServletDescriptor(var1, var16.getServletName());
               var21.setDestroyAsPrincipalName(var16.getPrincipalName());
               if (this.migrationServletDescriptors == null) {
                  this.migrationServletDescriptors = new ArrayList();
               }

               this.migrationServletDescriptors.add(var21);
            }
         }
      }

      var3 = DOMUtils.getOptionalElementsByTagName(var2, "run-as-role-assignment");
      if (var3.size() > 0) {
         var4 = var3.iterator();
         RunAsRoleAssignmentMBean[] var17 = new RunAsRoleAssignmentMBean[var3.size()];

         for(var10 = 0; var4.hasNext() && var10 < var17.length; ++var10) {
            var17[var10] = new RunAsRoleAssignment(var1, (Element)var4.next());
         }

         this.runAsRoleAssignments = var17;
      } else {
         this.runAsRoleAssignments = null;
      }

      var3 = DOMUtils.getOptionalElementsByTagName(var2, "virtual-directory-mapping");
      if (var3.size() > 0) {
         var4 = var3.iterator();
         VirtualDirectoryMappingMBean[] var18 = new VirtualDirectoryMappingMBean[var3.size()];

         for(var10 = 0; var4.hasNext() && var10 < var18.length; ++var10) {
            var18[var10] = new VirtualDirectoryMappingDescriptor((Element)var4.next());
         }

         this.virtualDirectories = var18;
      } else {
         this.virtualDirectories = null;
      }

      var3 = DOMUtils.getOptionalElementsByTagName(var2, "preprocessor");
      if (var3.size() > 0) {
         var4 = var3.iterator();
         PreprocessorMBean[] var19 = new PreprocessorMBean[var3.size()];

         for(var10 = 0; var4.hasNext() && var10 < var19.length; ++var10) {
            var19[var10] = new PreprocessorDescriptor((Element)var4.next());
         }

         this.preprocessors = var19;
      }

      var3 = DOMUtils.getOptionalElementsByTagName(var2, "preprocessor-mapping");
      if (var3.size() > 0) {
         var4 = var3.iterator();
         PreprocessorMappingMBean[] var20 = new PreprocessorMappingMBean[var3.size()];

         for(var10 = 0; var4.hasNext() && var10 < var20.length; ++var10) {
            var20[var10] = new PreprocessorMappingDescriptor(this, (Element)var4.next());
         }

         if (var20 != null) {
            this.preprocessorMaps = var20;
         }
      }

      var5 = DOMUtils.getOptionalElementByTagName(var2, "reference-descriptor");
      if (var5 != null) {
         this.refDes = new ReferenceDescriptor(var1, var5);
      }

      var5 = DOMUtils.getOptionalElementByTagName(var2, "session-descriptor");
      if (var5 != null) {
         this.sessDes = new WLSessionDescriptor(var5);
      }

      var5 = DOMUtils.getOptionalElementByTagName(var2, "jsp-descriptor");
      if (var5 != null) {
         this.jspDes = new WLJspDescriptor(var5);
      }

      this.contextRoot = DOMUtils.getOptionalValueByTagName(var2, "context-root");
      this.dispatchPolicy = DOMUtils.getOptionalValueByTagName(var2, "wl-dispatch-policy");
      if (this.sessDes == null) {
         this.sessDes = new WLSessionDescriptor();
      }

      if (this.jspDes == null) {
         this.jspDes = new WLJspDescriptor();
      }

   }

   public String getWebLogicVersion() {
      return this.wlVersion;
   }

   public void setWebLogicVersion(String var1) {
      String var2 = this.getWebLogicVersion();
      if (!comp(var2, var1)) {
         this.wlVersion = var1;
         this.firePropertyChange("wlVersion", var2, var1);
      }

   }

   public AuthFilterMBean getAuthFilter() {
      return this.wlAuthFilter;
   }

   public void setAuthFilter(AuthFilterMBean var1) {
      this.wlAuthFilter = var1;
   }

   public URLMatchMapMBean getURLMatchMap() {
      return this.urlMatchMap;
   }

   public void setURLMatchMap(URLMatchMapMBean var1) {
      this.urlMatchMap = var1;
   }

   public String getDescription() {
      return this.wlDescription;
   }

   public void setDescription(String var1) {
      String var2 = this.getDescription();
      if (!comp(var2, var1)) {
         this.wlDescription = var1;
         this.firePropertyChange("description", var2, var1);
      }

   }

   public ReferenceDescriptorMBean getReferenceDescriptor() {
      return this.refDes;
   }

   public void setReferenceDescriptor(ReferenceDescriptorMBean var1) {
      this.refDes = var1;
   }

   public SessionDescriptorMBean getSessionDescriptor() {
      return this.sessDes;
   }

   public void setSessionDescriptor(SessionDescriptorMBean var1) {
      this.sessDes = var1;
   }

   public JspDescriptorMBean getJspDescriptor() {
      return this.jspDes;
   }

   public void setJspDescriptor(JspDescriptorMBean var1) {
      this.jspDes = var1;
   }

   public void setSecurityRoleAssignments(SecurityRoleAssignmentMBean[] var1) {
      SecurityRoleAssignmentMBean[] var2 = this.securityRoleAssignments;
      this.securityRoleAssignments = var1;
   }

   public RunAsRoleAssignmentMBean[] getRunAsRoleAssignments() {
      if (this.runAsRoleAssignments == null) {
         this.runAsRoleAssignments = new RunAsRoleAssignmentMBean[0];
      }

      return this.runAsRoleAssignments;
   }

   public void setRunAsRoleAssignments(RunAsRoleAssignmentMBean[] var1) {
      this.runAsRoleAssignments = var1;
   }

   public SecurityRoleAssignmentMBean[] getSecurityRoleAssignments() {
      if (this.securityRoleAssignments == null) {
         this.securityRoleAssignments = new SecurityRoleAssignmentMBean[0];
      }

      return this.securityRoleAssignments;
   }

   public void addSecurityRoleAssignment(SecurityRoleAssignmentMBean var1) {
      SecurityRoleAssignmentMBean[] var2 = this.getSecurityRoleAssignments();
      if (var2 == null) {
         var2 = new SecurityRoleAssignmentMBean[]{var1};
         this.setSecurityRoleAssignments(var2);
      } else {
         SecurityRoleAssignmentMBean[] var3 = new SecurityRoleAssignmentMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setSecurityRoleAssignments(var3);
      }
   }

   public void removeSecurityRoleAssignment(SecurityRoleAssignmentMBean var1) {
      SecurityRoleAssignmentMBean[] var2 = this.getSecurityRoleAssignments();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            SecurityRoleAssignmentMBean[] var5 = new SecurityRoleAssignmentMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setSecurityRoleAssignments(var5);
         }

      }
   }

   public void setVirtualDirectoryMappings(VirtualDirectoryMappingMBean[] var1) {
      VirtualDirectoryMappingMBean[] var2 = this.virtualDirectories;
      this.virtualDirectories = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("virtualDirectoryMappings", var2, var1);
      }

   }

   public VirtualDirectoryMappingMBean[] getVirtualDirectoryMappings() {
      if (this.virtualDirectories == null) {
         this.virtualDirectories = new VirtualDirectoryMappingMBean[0];
      }

      return this.virtualDirectories;
   }

   public void addVirtualDirectoryMapping(VirtualDirectoryMappingMBean var1) {
      VirtualDirectoryMappingMBean[] var2 = this.getVirtualDirectoryMappings();
      if (var2 == null) {
         var2 = new VirtualDirectoryMappingMBean[]{var1};
         this.setVirtualDirectoryMappings(var2);
      } else {
         VirtualDirectoryMappingMBean[] var3 = new VirtualDirectoryMappingMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setVirtualDirectoryMappings(var3);
      }
   }

   public void removeVirtualDirectoryMapping(VirtualDirectoryMappingMBean var1) {
      VirtualDirectoryMappingMBean[] var2 = this.getVirtualDirectoryMappings();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            VirtualDirectoryMappingMBean[] var5 = new VirtualDirectoryMappingMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setVirtualDirectoryMappings(var5);
         }

      }
   }

   public boolean containsServletDescriptor(ServletDescriptorMBean var1) {
      if (this.servletDescriptors == null) {
         return false;
      } else {
         int var2 = this.servletDescriptors.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (var1 == this.servletDescriptors[var3]) {
               return true;
            }
         }

         return false;
      }
   }

   public void addServletDescriptor(ServletDescriptorMBean var1) {
      if (var1 == null) {
         throw new NullPointerException("null arg");
      } else {
         if (this.servletDescriptors == null) {
            this.servletDescriptors = new ServletDescriptorMBean[0];
         }

         int var2 = this.servletDescriptors.length;
         ServletDescriptorMBean[] var3 = new ServletDescriptorMBean[var2 + 1];
         System.arraycopy(this.servletDescriptors, 0, var3, 0, var2);
         var3[var2] = var1;
         this.servletDescriptors = var3;
      }
   }

   public void removeServletDescriptor(ServletDescriptorMBean var1) {
      if (this.servletDescriptors != null && var1 != null) {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < this.servletDescriptors.length; ++var3) {
            if (this.servletDescriptors[var3] != var1) {
               var2.add(this.servletDescriptors[var3]);
            }
         }

         ServletDescriptorMBean[] var4 = new ServletDescriptorMBean[var2.size()];
         var2.toArray(var4);
         this.servletDescriptors = var4;
      }
   }

   public void setContextRoot(String var1) {
      String var2 = this.contextRoot;
      this.contextRoot = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("contextRoot", var2, var1);
      }

   }

   public String getContextRoot() {
      return this.contextRoot;
   }

   public void setDispatchPolicy(String var1) {
      String var2 = this.dispatchPolicy;
      this.dispatchPolicy = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("dispatchPolicy", var2, var1);
      }

   }

   public String getDispatchPolicy() {
      return this.dispatchPolicy;
   }

   public void setContainerDescriptor(ContainerDescriptorMBean var1) {
      this.containerDescriptor = var1;
   }

   public ContainerDescriptorMBean getContainerDescriptor() {
      return this.containerDescriptor;
   }

   public void setCharsetParams(CharsetParamsMBean var1) {
      this.charsetParams = var1;
   }

   public CharsetParamsMBean getCharsetParams() {
      return this.charsetParams;
   }

   public void setSecurityPermissionMBean(SecurityPermissionMBean var1) {
      this.securityPermission = var1;
   }

   public SecurityPermissionMBean getSecurityPermissionMBean() {
      return this.securityPermission;
   }

   public void setPreprocessors(PreprocessorMBean[] var1) {
      PreprocessorMBean[] var2 = this.preprocessors;
      this.preprocessors = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("preprocessors", var2, var1);
      }

   }

   public PreprocessorMBean[] getPreprocessors() {
      return this.preprocessors;
   }

   public void addPreprocessorMBean(PreprocessorMBean var1) {
      PreprocessorMBean[] var2 = this.getPreprocessors();
      if (var2 == null) {
         var2 = new PreprocessorMBean[]{var1};
         this.setPreprocessors(var2);
      } else {
         PreprocessorMBean[] var3 = new PreprocessorMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setPreprocessors(var3);
      }
   }

   public void removePreprocessorMBean(PreprocessorMBean var1) {
      PreprocessorMBean[] var2 = this.getPreprocessors();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            PreprocessorMBean[] var5 = new PreprocessorMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setPreprocessors(var5);
         }

      }
   }

   public void setPreprocessorMappings(PreprocessorMappingMBean[] var1) {
      PreprocessorMappingMBean[] var2 = this.preprocessorMaps;
      this.preprocessorMaps = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("preprocessorMappings", var2, var1);
      }

   }

   public PreprocessorMappingMBean[] getPreprocessorMappings() {
      return this.preprocessorMaps;
   }

   public void addPreprocessorMappingMBean(PreprocessorMappingMBean var1) {
      PreprocessorMappingMBean[] var2 = this.getPreprocessorMappings();
      if (var2 == null) {
         var2 = new PreprocessorMappingMBean[]{var1};
         this.setPreprocessorMappings(var2);
      } else {
         PreprocessorMappingMBean[] var3 = new PreprocessorMappingMBean[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setPreprocessorMappings(var3);
      }
   }

   public void removePreprocessorMappingMBean(PreprocessorMappingMBean var1) {
      PreprocessorMappingMBean[] var2 = this.getPreprocessorMappings();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            PreprocessorMappingMBean[] var5 = new PreprocessorMappingMBean[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setPreprocessorMappings(var5);
         }

      }
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
      boolean var1 = true;
      int var2;
      if (this.securityRoleAssignments != null) {
         for(var2 = 0; var2 < this.securityRoleAssignments.length; ++var2) {
            var1 &= this.check(this.securityRoleAssignments[var2]);
         }
      }

      if (this.virtualDirectories != null) {
         for(var2 = 0; var2 < this.virtualDirectories.length; ++var2) {
            var1 &= this.check(this.virtualDirectories[var2]);
         }
      }

      if (this.refDes != null) {
         var1 &= this.check(this.refDes);
      }

      if (this.sessDes != null) {
         var1 &= this.check(this.sessDes);
      }

      if (this.jspDes != null) {
         var1 &= this.check(this.jspDes);
      }

      if (this.wlAuthFilter != null) {
         var1 &= this.check(this.wlAuthFilter);
      }

      if (this.urlMatchMap != null) {
         var1 &= this.check(this.urlMatchMap);
      }

      if (this.containerDescriptor != null) {
         var1 &= this.check(this.containerDescriptor);
      }

      if (this.charsetParams != null) {
         var1 &= this.check(this.charsetParams);
      }

      if (this.preprocessors != null) {
         for(var2 = 0; var2 < this.preprocessors.length; ++var2) {
            var1 &= this.check(this.preprocessors[var2]);
         }
      }

      if (this.preprocessorMaps != null) {
         for(var2 = 0; var2 < this.preprocessorMaps.length; ++var2) {
            var1 &= this.check(this.preprocessorMaps[var2]);
         }
      }

      if (!var1) {
         String[] var3 = this.getDescriptorErrors();
         throw new DescriptorValidationException(this.arrayToString(var3));
      }
   }

   public String toXML(int var1) {
      String var2 = "";
      String var3 = this.getEncoding();
      if (var3 != null) {
         var2 = var2 + "<?xml version=\"1.0\" encoding=\"" + var3 + "\"?>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<!DOCTYPE weblogic-web-app PUBLIC \"-//BEA Systems, Inc.//DTD Web Application 8.1//EN\" \"http://www.bea.com/servers/wls810/dtd/weblogic810-web-jar.dtd\">" + "\n";
      var2 = var2 + "\n" + this.indentStr(var1) + "<weblogic-web-app>\n";
      var1 += 2;
      if (this.wlDescription != null) {
         var2 = var2 + "\n" + this.indentStr(var1) + "<description>" + this.wlDescription + "</description>\n";
      }

      if (this.wlVersion != null) {
         var2 = var2 + "\n" + this.indentStr(var1) + "<weblogic-version>" + this.wlVersion + "</weblogic-version>\n";
      }

      int var4;
      if (this.securityRoleAssignments != null) {
         for(var4 = 0; var4 < this.securityRoleAssignments.length; ++var4) {
            SecurityRoleAssignmentMBean var5 = this.securityRoleAssignments[var4];
            var2 = var2 + "\n" + var5.toXML(var1);
         }
      }

      if (this.runAsRoleAssignments != null) {
         for(var4 = 0; var4 < this.runAsRoleAssignments.length; ++var4) {
            RunAsRoleAssignmentMBean var7 = this.runAsRoleAssignments[var4];
            String var6 = var7.toXML(var1);
            if (var6 != null && var6.length() > 0) {
               var2 = var2 + '\n' + var6;
            }
         }
      }

      if (this.refDes != null) {
         var2 = var2 + "\n" + this.refDes.toXML(var1);
      }

      if (this.sessDes != null) {
         var2 = var2 + "\n" + this.sessDes.toXML(var1);
      }

      if (this.jspDes != null) {
         var2 = var2 + "\n" + this.jspDes.toXML(var1);
      }

      if (this.wlAuthFilter != null) {
         var2 = var2 + "\n" + this.wlAuthFilter.toXML(var1);
      }

      if (this.containerDescriptor != null) {
         var2 = var2 + "\n" + this.containerDescriptor.toXML(var1);
      }

      if (this.charsetParams != null) {
         var2 = var2 + "\n" + this.charsetParams.toXML(var1);
      }

      if (this.virtualDirectories != null) {
         for(var4 = 0; var4 < this.virtualDirectories.length; ++var4) {
            VirtualDirectoryMappingMBean var8 = this.virtualDirectories[var4];
            var2 = var2 + "\n" + var8.toXML(var1);
         }
      }

      if (this.urlMatchMap != null) {
         var2 = var2 + "\n" + this.urlMatchMap.toXML(var1);
      }

      if (this.preprocessors != null) {
         for(var4 = 0; var4 < this.preprocessors.length; ++var4) {
            PreprocessorMBean var9 = this.preprocessors[var4];
            var2 = var2 + "\n" + var9.toXML(var1);
         }
      }

      if (this.preprocessorMaps != null) {
         for(var4 = 0; var4 < this.preprocessorMaps.length; ++var4) {
            PreprocessorMappingMBean var10 = this.preprocessorMaps[var4];
            var2 = var2 + "\n" + var10.toXML(var1);
         }
      }

      if (this.securityPermission != null) {
         var2 = var2 + "\n" + this.securityPermission.toXML(var1);
      }

      if (this.contextRoot != null && this.contextRoot.trim().length() > 0) {
         var2 = var2 + "\n" + this.indentStr(var1) + "<context-root>" + this.contextRoot + "</context-root>";
      }

      if (this.dispatchPolicy != null && this.dispatchPolicy.trim().length() > 0) {
         var2 = var2 + "\n" + this.indentStr(var1) + "<wl-dispatch-policy>" + this.dispatchPolicy + "</wl-dispatch-policy>";
      }

      if (this.servletDescriptors != null) {
         for(var4 = 0; var4 < this.servletDescriptors.length; ++var4) {
            var2 = var2 + "\n" + this.servletDescriptors[var4].toXML(var1);
         }
      }

      ServletDescriptorMBean var11;
      if (this.migrationServletDescriptors != null) {
         for(Iterator var12 = this.migrationServletDescriptors.iterator(); var12.hasNext(); var2 = var2 + "\n" + var11.toXML(var1)) {
            var11 = (ServletDescriptorMBean)var12.next();
         }
      }

      var1 -= 2;
      var2 = var2 + "\n" + this.indentStr(var1) + "</weblogic-web-app>\n";
      return var2;
   }
}
