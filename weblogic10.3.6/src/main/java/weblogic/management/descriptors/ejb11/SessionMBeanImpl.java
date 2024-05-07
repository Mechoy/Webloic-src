package weblogic.management.descriptors.ejb11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class SessionMBeanImpl extends XMLElementMBeanDelegate implements SessionMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_ejbRefs = false;
   private List ejbRefs;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_home = false;
   private String home;
   private boolean isSet_resourceRefs = false;
   private List resourceRefs;
   private boolean isSet_sessionType = false;
   private String sessionType;
   private boolean isSet_securityRoleRefs = false;
   private List securityRoleRefs;
   private boolean isSet_largeIcon = false;
   private String largeIcon;
   private boolean isSet_transactionType = false;
   private String transactionType;
   private boolean isSet_displayName = false;
   private String displayName;
   private boolean isSet_ejbClass = false;
   private String ejbClass;
   private boolean isSet_envEntries = false;
   private List envEntries;
   private boolean isSet_remote = false;
   private String remote;
   private boolean isSet_ejbName = false;
   private String ejbName;
   private boolean isSet_smallIcon = false;
   private String smallIcon;

   public EJBRefMBean[] getEJBRefs() {
      if (this.ejbRefs == null) {
         return new EJBRefMBean[0];
      } else {
         EJBRefMBean[] var1 = new EJBRefMBean[this.ejbRefs.size()];
         var1 = (EJBRefMBean[])((EJBRefMBean[])this.ejbRefs.toArray(var1));
         return var1;
      }
   }

   public void setEJBRefs(EJBRefMBean[] var1) {
      EJBRefMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getEJBRefs();
      }

      this.isSet_ejbRefs = true;
      if (this.ejbRefs == null) {
         this.ejbRefs = Collections.synchronizedList(new ArrayList());
      } else {
         this.ejbRefs.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.ejbRefs.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("EJBRefs", var2, this.getEJBRefs());
      }

   }

   public void addEJBRef(EJBRefMBean var1) {
      this.isSet_ejbRefs = true;
      if (this.ejbRefs == null) {
         this.ejbRefs = Collections.synchronizedList(new ArrayList());
      }

      this.ejbRefs.add(var1);
   }

   public void removeEJBRef(EJBRefMBean var1) {
      if (this.ejbRefs != null) {
         this.ejbRefs.remove(var1);
      }
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

   public String getHome() {
      return this.home;
   }

   public void setHome(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.home;
      this.home = var1;
      this.isSet_home = var1 != null;
      this.checkChange("home", var2, this.home);
   }

   public ResourceRefMBean[] getResourceRefs() {
      if (this.resourceRefs == null) {
         return new ResourceRefMBean[0];
      } else {
         ResourceRefMBean[] var1 = new ResourceRefMBean[this.resourceRefs.size()];
         var1 = (ResourceRefMBean[])((ResourceRefMBean[])this.resourceRefs.toArray(var1));
         return var1;
      }
   }

   public void setResourceRefs(ResourceRefMBean[] var1) {
      ResourceRefMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getResourceRefs();
      }

      this.isSet_resourceRefs = true;
      if (this.resourceRefs == null) {
         this.resourceRefs = Collections.synchronizedList(new ArrayList());
      } else {
         this.resourceRefs.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.resourceRefs.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("ResourceRefs", var2, this.getResourceRefs());
      }

   }

   public void addResourceRef(ResourceRefMBean var1) {
      this.isSet_resourceRefs = true;
      if (this.resourceRefs == null) {
         this.resourceRefs = Collections.synchronizedList(new ArrayList());
      }

      this.resourceRefs.add(var1);
   }

   public void removeResourceRef(ResourceRefMBean var1) {
      if (this.resourceRefs != null) {
         this.resourceRefs.remove(var1);
      }
   }

   public String getSessionType() {
      return this.sessionType;
   }

   public void setSessionType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.sessionType;
      this.sessionType = var1;
      this.isSet_sessionType = var1 != null;
      this.checkChange("sessionType", var2, this.sessionType);
   }

   public SecurityRoleRefMBean[] getSecurityRoleRefs() {
      if (this.securityRoleRefs == null) {
         return new SecurityRoleRefMBean[0];
      } else {
         SecurityRoleRefMBean[] var1 = new SecurityRoleRefMBean[this.securityRoleRefs.size()];
         var1 = (SecurityRoleRefMBean[])((SecurityRoleRefMBean[])this.securityRoleRefs.toArray(var1));
         return var1;
      }
   }

   public void setSecurityRoleRefs(SecurityRoleRefMBean[] var1) {
      SecurityRoleRefMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getSecurityRoleRefs();
      }

      this.isSet_securityRoleRefs = true;
      if (this.securityRoleRefs == null) {
         this.securityRoleRefs = Collections.synchronizedList(new ArrayList());
      } else {
         this.securityRoleRefs.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.securityRoleRefs.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("SecurityRoleRefs", var2, this.getSecurityRoleRefs());
      }

   }

   public void addSecurityRoleRef(SecurityRoleRefMBean var1) {
      this.isSet_securityRoleRefs = true;
      if (this.securityRoleRefs == null) {
         this.securityRoleRefs = Collections.synchronizedList(new ArrayList());
      }

      this.securityRoleRefs.add(var1);
   }

   public void removeSecurityRoleRef(SecurityRoleRefMBean var1) {
      if (this.securityRoleRefs != null) {
         this.securityRoleRefs.remove(var1);
      }
   }

   public String getLargeIcon() {
      return this.largeIcon;
   }

   public void setLargeIcon(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.largeIcon;
      this.largeIcon = var1;
      this.isSet_largeIcon = var1 != null;
      this.checkChange("largeIcon", var2, this.largeIcon);
   }

   public String getTransactionType() {
      return this.transactionType;
   }

   public void setTransactionType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.transactionType;
      this.transactionType = var1;
      this.isSet_transactionType = var1 != null;
      this.checkChange("transactionType", var2, this.transactionType);
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.displayName;
      this.displayName = var1;
      this.isSet_displayName = var1 != null;
      this.checkChange("displayName", var2, this.displayName);
   }

   public String getEJBClass() {
      return this.ejbClass;
   }

   public void setEJBClass(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbClass;
      this.ejbClass = var1;
      this.isSet_ejbClass = var1 != null;
      this.checkChange("ejbClass", var2, this.ejbClass);
   }

   public EnvEntryMBean[] getEnvEntries() {
      if (this.envEntries == null) {
         return new EnvEntryMBean[0];
      } else {
         EnvEntryMBean[] var1 = new EnvEntryMBean[this.envEntries.size()];
         var1 = (EnvEntryMBean[])((EnvEntryMBean[])this.envEntries.toArray(var1));
         return var1;
      }
   }

   public void setEnvEntries(EnvEntryMBean[] var1) {
      EnvEntryMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getEnvEntries();
      }

      this.isSet_envEntries = true;
      if (this.envEntries == null) {
         this.envEntries = Collections.synchronizedList(new ArrayList());
      } else {
         this.envEntries.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.envEntries.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("EnvEntries", var2, this.getEnvEntries());
      }

   }

   public void addEnvEntry(EnvEntryMBean var1) {
      this.isSet_envEntries = true;
      if (this.envEntries == null) {
         this.envEntries = Collections.synchronizedList(new ArrayList());
      }

      this.envEntries.add(var1);
   }

   public void removeEnvEntry(EnvEntryMBean var1) {
      if (this.envEntries != null) {
         this.envEntries.remove(var1);
      }
   }

   public String getRemote() {
      return this.remote;
   }

   public void setRemote(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.remote;
      this.remote = var1;
      this.isSet_remote = var1 != null;
      this.checkChange("remote", var2, this.remote);
   }

   public String getEJBName() {
      return this.ejbName;
   }

   public void setEJBName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbName;
      this.ejbName = var1;
      this.isSet_ejbName = var1 != null;
      this.checkChange("ejbName", var2, this.ejbName);
   }

   public String getSmallIcon() {
      return this.smallIcon;
   }

   public void setSmallIcon(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.smallIcon;
      this.smallIcon = var1;
      this.isSet_smallIcon = var1 != null;
      this.checkChange("smallIcon", var2, this.smallIcon);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<session");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getDisplayName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<display-name>").append(this.getDisplayName()).append("</display-name>\n");
      }

      if (null != this.getSmallIcon()) {
         var2.append(ToXML.indent(var1 + 2)).append("<small-icon>").append(this.getSmallIcon()).append("</small-icon>\n");
      }

      if (null != this.getLargeIcon()) {
         var2.append(ToXML.indent(var1 + 2)).append("<large-icon>").append(this.getLargeIcon()).append("</large-icon>\n");
      }

      if (null != this.getEJBName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-name>").append(this.getEJBName()).append("</ejb-name>\n");
      }

      if (null != this.getHome()) {
         var2.append(ToXML.indent(var1 + 2)).append("<home>").append(this.getHome()).append("</home>\n");
      }

      if (null != this.getRemote()) {
         var2.append(ToXML.indent(var1 + 2)).append("<remote>").append(this.getRemote()).append("</remote>\n");
      }

      if (null != this.getEJBClass()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-class>").append(this.getEJBClass()).append("</ejb-class>\n");
      }

      if (null != this.getSessionType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<session-type>").append(this.getSessionType()).append("</session-type>\n");
      }

      if (null != this.getTransactionType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<transaction-type>").append(this.getTransactionType()).append("</transaction-type>\n");
      }

      int var3;
      if (null != this.getEnvEntries()) {
         for(var3 = 0; var3 < this.getEnvEntries().length; ++var3) {
            var2.append(this.getEnvEntries()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getEJBRefs()) {
         for(var3 = 0; var3 < this.getEJBRefs().length; ++var3) {
            var2.append(this.getEJBRefs()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getSecurityRoleRefs()) {
         for(var3 = 0; var3 < this.getSecurityRoleRefs().length; ++var3) {
            var2.append(this.getSecurityRoleRefs()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getResourceRefs()) {
         for(var3 = 0; var3 < this.getResourceRefs().length; ++var3) {
            var2.append(this.getResourceRefs()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</session>\n");
      return var2.toString();
   }
}
