package weblogic.management.descriptors.weblogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class WeblogicEJBJarMBeanImpl extends XMLElementMBeanDelegate implements WeblogicEJBJarMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_idempotentMethods = false;
   private IdempotentMethodsMBean idempotentMethods;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_transactionIsolations = false;
   private List transactionIsolations;
   private boolean isSet_runAsRoleAssignments = false;
   private List runAsRoleAssignments;
   private boolean isSet_encoding = false;
   private String encoding;
   private boolean isSet_disableWarnings = false;
   private List disableWarnings;
   private boolean isSet_securityPermission = false;
   private SecurityPermissionMBean securityPermission;
   private boolean isSet_enableBeanClassRedeploy = false;
   private boolean enableBeanClassRedeploy = false;
   private boolean isSet_weblogicEnterpriseBeans = false;
   private List weblogicEnterpriseBeans;
   private boolean isSet_version = false;
   private String version;
   private boolean isSet_securityRoleAssignments = false;
   private List securityRoleAssignments;

   public IdempotentMethodsMBean getIdempotentMethods() {
      return this.idempotentMethods;
   }

   public void setIdempotentMethods(IdempotentMethodsMBean var1) {
      IdempotentMethodsMBean var2 = this.idempotentMethods;
      this.idempotentMethods = var1;
      this.isSet_idempotentMethods = var1 != null;
      this.checkChange("idempotentMethods", var2, this.idempotentMethods);
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.description;
      this.description = var1;
      this.isSet_description = var1 != null;
      this.checkChange("description", var2, this.description);
   }

   public TransactionIsolationMBean[] getTransactionIsolations() {
      if (this.transactionIsolations == null) {
         return new TransactionIsolationMBean[0];
      } else {
         TransactionIsolationMBean[] var1 = new TransactionIsolationMBean[this.transactionIsolations.size()];
         var1 = (TransactionIsolationMBean[])((TransactionIsolationMBean[])this.transactionIsolations.toArray(var1));
         return var1;
      }
   }

   public void setTransactionIsolations(TransactionIsolationMBean[] var1) {
      TransactionIsolationMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getTransactionIsolations();
      }

      this.isSet_transactionIsolations = true;
      if (this.transactionIsolations == null) {
         this.transactionIsolations = Collections.synchronizedList(new ArrayList());
      } else {
         this.transactionIsolations.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.transactionIsolations.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("TransactionIsolations", var2, this.getTransactionIsolations());
      }

   }

   public void addTransactionIsolation(TransactionIsolationMBean var1) {
      this.isSet_transactionIsolations = true;
      if (this.transactionIsolations == null) {
         this.transactionIsolations = Collections.synchronizedList(new ArrayList());
      }

      this.transactionIsolations.add(var1);
   }

   public void removeTransactionIsolation(TransactionIsolationMBean var1) {
      if (this.transactionIsolations != null) {
         this.transactionIsolations.remove(var1);
      }
   }

   public RunAsRoleAssignmentMBean[] getRunAsRoleAssignments() {
      if (this.runAsRoleAssignments == null) {
         return new RunAsRoleAssignmentMBean[0];
      } else {
         RunAsRoleAssignmentMBean[] var1 = new RunAsRoleAssignmentMBean[this.runAsRoleAssignments.size()];
         var1 = (RunAsRoleAssignmentMBean[])((RunAsRoleAssignmentMBean[])this.runAsRoleAssignments.toArray(var1));
         return var1;
      }
   }

   public void setRunAsRoleAssignments(RunAsRoleAssignmentMBean[] var1) {
      RunAsRoleAssignmentMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getRunAsRoleAssignments();
      }

      this.isSet_runAsRoleAssignments = true;
      if (this.runAsRoleAssignments == null) {
         this.runAsRoleAssignments = Collections.synchronizedList(new ArrayList());
      } else {
         this.runAsRoleAssignments.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.runAsRoleAssignments.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("RunAsRoleAssignments", var2, this.getRunAsRoleAssignments());
      }

   }

   public void addRunAsRoleAssignment(RunAsRoleAssignmentMBean var1) {
      this.isSet_runAsRoleAssignments = true;
      if (this.runAsRoleAssignments == null) {
         this.runAsRoleAssignments = Collections.synchronizedList(new ArrayList());
      }

      this.runAsRoleAssignments.add(var1);
   }

   public void removeRunAsRoleAssignment(RunAsRoleAssignmentMBean var1) {
      if (this.runAsRoleAssignments != null) {
         this.runAsRoleAssignments.remove(var1);
      }
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void setEncoding(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.encoding;
      this.encoding = var1;
      this.isSet_encoding = var1 != null;
      this.checkChange("encoding", var2, this.encoding);
   }

   public String[] getDisableWarnings() {
      if (this.disableWarnings == null) {
         return new String[0];
      } else {
         String[] var1 = new String[this.disableWarnings.size()];
         var1 = (String[])((String[])this.disableWarnings.toArray(var1));
         return var1;
      }
   }

   public void setDisableWarnings(String[] var1) {
      String[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getDisableWarnings();
      }

      this.isSet_disableWarnings = true;
      if (this.disableWarnings == null) {
         this.disableWarnings = Collections.synchronizedList(new ArrayList());
      } else {
         this.disableWarnings.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.disableWarnings.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("DisableWarnings", var2, this.getDisableWarnings());
      }

   }

   public void addDisableWarning(String var1) {
      this.isSet_disableWarnings = true;
      if (this.disableWarnings == null) {
         this.disableWarnings = Collections.synchronizedList(new ArrayList());
      }

      this.disableWarnings.add(var1);
   }

   public SecurityPermissionMBean getSecurityPermission() {
      return this.securityPermission;
   }

   public void setSecurityPermission(SecurityPermissionMBean var1) {
      SecurityPermissionMBean var2 = this.securityPermission;
      this.securityPermission = var1;
      this.isSet_securityPermission = var1 != null;
      this.checkChange("securityPermission", var2, this.securityPermission);
   }

   public boolean getEnableBeanClassRedeploy() {
      return this.enableBeanClassRedeploy;
   }

   public void setEnableBeanClassRedeploy(boolean var1) {
      boolean var2 = this.enableBeanClassRedeploy;
      this.enableBeanClassRedeploy = var1;
      this.isSet_enableBeanClassRedeploy = true;
      this.checkChange("enableBeanClassRedeploy", var2, this.enableBeanClassRedeploy);
   }

   public WeblogicEnterpriseBeanMBean[] getWeblogicEnterpriseBeans() {
      if (this.weblogicEnterpriseBeans == null) {
         return new WeblogicEnterpriseBeanMBean[0];
      } else {
         WeblogicEnterpriseBeanMBean[] var1 = new WeblogicEnterpriseBeanMBean[this.weblogicEnterpriseBeans.size()];
         var1 = (WeblogicEnterpriseBeanMBean[])((WeblogicEnterpriseBeanMBean[])this.weblogicEnterpriseBeans.toArray(var1));
         return var1;
      }
   }

   public void setWeblogicEnterpriseBeans(WeblogicEnterpriseBeanMBean[] var1) {
      WeblogicEnterpriseBeanMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getWeblogicEnterpriseBeans();
      }

      this.isSet_weblogicEnterpriseBeans = true;
      if (this.weblogicEnterpriseBeans == null) {
         this.weblogicEnterpriseBeans = Collections.synchronizedList(new ArrayList());
      } else {
         this.weblogicEnterpriseBeans.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.weblogicEnterpriseBeans.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("WeblogicEnterpriseBeans", var2, this.getWeblogicEnterpriseBeans());
      }

   }

   public void addWeblogicEnterpriseBean(WeblogicEnterpriseBeanMBean var1) {
      this.isSet_weblogicEnterpriseBeans = true;
      if (this.weblogicEnterpriseBeans == null) {
         this.weblogicEnterpriseBeans = Collections.synchronizedList(new ArrayList());
      }

      this.weblogicEnterpriseBeans.add(var1);
   }

   public void removeWeblogicEnterpriseBean(WeblogicEnterpriseBeanMBean var1) {
      if (this.weblogicEnterpriseBeans != null) {
         this.weblogicEnterpriseBeans.remove(var1);
      }
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.version;
      this.version = var1;
      this.isSet_version = var1 != null;
      this.checkChange("version", var2, this.version);
   }

   public SecurityRoleAssignmentMBean[] getSecurityRoleAssignments() {
      if (this.securityRoleAssignments == null) {
         return new SecurityRoleAssignmentMBean[0];
      } else {
         SecurityRoleAssignmentMBean[] var1 = new SecurityRoleAssignmentMBean[this.securityRoleAssignments.size()];
         var1 = (SecurityRoleAssignmentMBean[])((SecurityRoleAssignmentMBean[])this.securityRoleAssignments.toArray(var1));
         return var1;
      }
   }

   public void setSecurityRoleAssignments(SecurityRoleAssignmentMBean[] var1) {
      SecurityRoleAssignmentMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getSecurityRoleAssignments();
      }

      this.isSet_securityRoleAssignments = true;
      if (this.securityRoleAssignments == null) {
         this.securityRoleAssignments = Collections.synchronizedList(new ArrayList());
      } else {
         this.securityRoleAssignments.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.securityRoleAssignments.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("SecurityRoleAssignments", var2, this.getSecurityRoleAssignments());
      }

   }

   public void addSecurityRoleAssignment(SecurityRoleAssignmentMBean var1) {
      this.isSet_securityRoleAssignments = true;
      if (this.securityRoleAssignments == null) {
         this.securityRoleAssignments = Collections.synchronizedList(new ArrayList());
      }

      this.securityRoleAssignments.add(var1);
   }

   public void removeSecurityRoleAssignment(SecurityRoleAssignmentMBean var1) {
      if (this.securityRoleAssignments != null) {
         this.securityRoleAssignments.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<weblogic-ejb-jar");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      int var3;
      if (null != this.getWeblogicEnterpriseBeans()) {
         for(var3 = 0; var3 < this.getWeblogicEnterpriseBeans().length; ++var3) {
            var2.append(this.getWeblogicEnterpriseBeans()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getSecurityRoleAssignments()) {
         for(var3 = 0; var3 < this.getSecurityRoleAssignments().length; ++var3) {
            var2.append(this.getSecurityRoleAssignments()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getRunAsRoleAssignments()) {
         for(var3 = 0; var3 < this.getRunAsRoleAssignments().length; ++var3) {
            var2.append(this.getRunAsRoleAssignments()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getSecurityPermission()) {
         var2.append(this.getSecurityPermission().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getTransactionIsolations()) {
         for(var3 = 0; var3 < this.getTransactionIsolations().length; ++var3) {
            var2.append(this.getTransactionIsolations()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getIdempotentMethods()) {
         var2.append(this.getIdempotentMethods().toXML(var1 + 2)).append("\n");
      }

      if (this.isSet_enableBeanClassRedeploy || this.getEnableBeanClassRedeploy()) {
         var2.append(ToXML.indent(var1 + 2)).append("<enable-bean-class-redeploy>").append(ToXML.capitalize(Boolean.valueOf(this.getEnableBeanClassRedeploy()).toString())).append("</enable-bean-class-redeploy>\n");
      }

      for(var3 = 0; var3 < this.getDisableWarnings().length; ++var3) {
         var2.append(ToXML.indent(var1 + 2)).append("<disable-warning>").append(this.getDisableWarnings()[var3]).append("</disable-warning>\n");
      }

      var2.append(ToXML.indent(var1)).append("</weblogic-ejb-jar>\n");
      return var2.toString();
   }
}
