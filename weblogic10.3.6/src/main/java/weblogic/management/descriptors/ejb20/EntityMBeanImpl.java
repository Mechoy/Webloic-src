package weblogic.management.descriptors.ejb20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.descriptors.ejb11.CMPFieldMBean;
import weblogic.management.descriptors.ejb11.EJBRefMBean;
import weblogic.management.descriptors.ejb11.EnvEntryMBean;
import weblogic.management.descriptors.ejb11.SecurityRoleRefMBean;
import weblogic.management.tools.ToXML;

public class EntityMBeanImpl extends XMLElementMBeanDelegate implements EntityMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_resourceRefs = false;
   private List resourceRefs;
   private boolean isSet_persistenceType = false;
   private String persistenceType;
   private boolean isSet_cmpFields = false;
   private List cmpFields;
   private boolean isSet_localHome = false;
   private String localHome;
   private boolean isSet_ejbClass = false;
   private String ejbClass;
   private boolean isSet_primKeyClass = false;
   private String primKeyClass;
   private boolean isSet_securityIdentity = false;
   private SecurityIdentityMBean securityIdentity;
   private boolean isSet_remote = false;
   private String remote;
   private boolean isSet_queries = false;
   private List queries;
   private boolean isSet_ejbRefs = false;
   private List ejbRefs;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_local = false;
   private String local;
   private boolean isSet_home = false;
   private String home;
   private boolean isSet_reentrant = false;
   private boolean reentrant;
   private boolean isSet_securityRoleRefs = false;
   private List securityRoleRefs;
   private boolean isSet_largeIcon = false;
   private String largeIcon;
   private boolean isSet_displayName = false;
   private String displayName;
   private boolean isSet_ejbLocalRefs = false;
   private List ejbLocalRefs;
   private boolean isSet_primkeyField = false;
   private String primkeyField;
   private boolean isSet_envEntries = false;
   private List envEntries;
   private boolean isSet_resourceEnvRefs = false;
   private List resourceEnvRefs;
   private boolean isSet_ejbName = false;
   private String ejbName;
   private boolean isSet_cmpVersion = false;
   private String cmpVersion = "2.x";
   private boolean isSet_smallIcon = false;
   private String smallIcon;
   private boolean isSet_abstractSchemaName = false;
   private String abstractSchemaName;

   public weblogic.management.descriptors.ejb11.ResourceRefMBean[] getResourceRefs() {
      if (this.resourceRefs == null) {
         return new weblogic.management.descriptors.ejb11.ResourceRefMBean[0];
      } else {
         weblogic.management.descriptors.ejb11.ResourceRefMBean[] var1 = new weblogic.management.descriptors.ejb11.ResourceRefMBean[this.resourceRefs.size()];
         var1 = (weblogic.management.descriptors.ejb11.ResourceRefMBean[])((weblogic.management.descriptors.ejb11.ResourceRefMBean[])this.resourceRefs.toArray(var1));
         return var1;
      }
   }

   public void setResourceRefs(weblogic.management.descriptors.ejb11.ResourceRefMBean[] var1) {
      weblogic.management.descriptors.ejb11.ResourceRefMBean[] var2 = null;
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

   public void addResourceRef(weblogic.management.descriptors.ejb11.ResourceRefMBean var1) {
      this.isSet_resourceRefs = true;
      if (this.resourceRefs == null) {
         this.resourceRefs = Collections.synchronizedList(new ArrayList());
      }

      this.resourceRefs.add(var1);
   }

   public void removeResourceRef(weblogic.management.descriptors.ejb11.ResourceRefMBean var1) {
      if (this.resourceRefs != null) {
         this.resourceRefs.remove(var1);
      }
   }

   public String getPersistenceType() {
      return this.persistenceType;
   }

   public void setPersistenceType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.persistenceType;
      this.persistenceType = var1;
      this.isSet_persistenceType = var1 != null;
      this.checkChange("persistenceType", var2, this.persistenceType);
   }

   public CMPFieldMBean[] getCMPFields() {
      if (this.cmpFields == null) {
         return new CMPFieldMBean[0];
      } else {
         CMPFieldMBean[] var1 = new CMPFieldMBean[this.cmpFields.size()];
         var1 = (CMPFieldMBean[])((CMPFieldMBean[])this.cmpFields.toArray(var1));
         return var1;
      }
   }

   public void setCMPFields(CMPFieldMBean[] var1) {
      CMPFieldMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getCMPFields();
      }

      this.isSet_cmpFields = true;
      if (this.cmpFields == null) {
         this.cmpFields = Collections.synchronizedList(new ArrayList());
      } else {
         this.cmpFields.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.cmpFields.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("CMPFields", var2, this.getCMPFields());
      }

   }

   public void addCMPField(CMPFieldMBean var1) {
      this.isSet_cmpFields = true;
      if (this.cmpFields == null) {
         this.cmpFields = Collections.synchronizedList(new ArrayList());
      }

      this.cmpFields.add(var1);
   }

   public void removeCMPField(CMPFieldMBean var1) {
      if (this.cmpFields != null) {
         this.cmpFields.remove(var1);
      }
   }

   public String getLocalHome() {
      return this.localHome;
   }

   public void setLocalHome(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.localHome;
      this.localHome = var1;
      this.isSet_localHome = var1 != null;
      this.checkChange("localHome", var2, this.localHome);
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

   public String getPrimKeyClass() {
      return this.primKeyClass;
   }

   public void setPrimKeyClass(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.primKeyClass;
      this.primKeyClass = var1;
      this.isSet_primKeyClass = var1 != null;
      this.checkChange("primKeyClass", var2, this.primKeyClass);
   }

   public SecurityIdentityMBean getSecurityIdentity() {
      return this.securityIdentity;
   }

   public void setSecurityIdentity(SecurityIdentityMBean var1) {
      SecurityIdentityMBean var2 = this.securityIdentity;
      this.securityIdentity = var1;
      this.isSet_securityIdentity = var1 != null;
      this.checkChange("securityIdentity", var2, this.securityIdentity);
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

   public QueryMBean[] getQueries() {
      if (this.queries == null) {
         return new QueryMBean[0];
      } else {
         QueryMBean[] var1 = new QueryMBean[this.queries.size()];
         var1 = (QueryMBean[])((QueryMBean[])this.queries.toArray(var1));
         return var1;
      }
   }

   public void setQueries(QueryMBean[] var1) {
      QueryMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getQueries();
      }

      this.isSet_queries = true;
      if (this.queries == null) {
         this.queries = Collections.synchronizedList(new ArrayList());
      } else {
         this.queries.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.queries.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Queries", var2, this.getQueries());
      }

   }

   public void addQuery(QueryMBean var1) {
      this.isSet_queries = true;
      if (this.queries == null) {
         this.queries = Collections.synchronizedList(new ArrayList());
      }

      this.queries.add(var1);
   }

   public void removeQuery(QueryMBean var1) {
      if (this.queries != null) {
         this.queries.remove(var1);
      }
   }

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

   public String getLocal() {
      return this.local;
   }

   public void setLocal(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.local;
      this.local = var1;
      this.isSet_local = var1 != null;
      this.checkChange("local", var2, this.local);
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

   public boolean isReentrant() {
      return this.reentrant;
   }

   public void setReentrant(boolean var1) {
      boolean var2 = this.reentrant;
      this.reentrant = var1;
      this.isSet_reentrant = true;
      this.checkChange("reentrant", var2, this.reentrant);
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

   public EJBLocalRefMBean[] getEJBLocalRefs() {
      if (this.ejbLocalRefs == null) {
         return new EJBLocalRefMBean[0];
      } else {
         EJBLocalRefMBean[] var1 = new EJBLocalRefMBean[this.ejbLocalRefs.size()];
         var1 = (EJBLocalRefMBean[])((EJBLocalRefMBean[])this.ejbLocalRefs.toArray(var1));
         return var1;
      }
   }

   public void setEJBLocalRefs(EJBLocalRefMBean[] var1) {
      EJBLocalRefMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getEJBLocalRefs();
      }

      this.isSet_ejbLocalRefs = true;
      if (this.ejbLocalRefs == null) {
         this.ejbLocalRefs = Collections.synchronizedList(new ArrayList());
      } else {
         this.ejbLocalRefs.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.ejbLocalRefs.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("EJBLocalRefs", var2, this.getEJBLocalRefs());
      }

   }

   public void addEJBLocalRef(EJBLocalRefMBean var1) {
      this.isSet_ejbLocalRefs = true;
      if (this.ejbLocalRefs == null) {
         this.ejbLocalRefs = Collections.synchronizedList(new ArrayList());
      }

      this.ejbLocalRefs.add(var1);
   }

   public void removeEJBLocalRef(EJBLocalRefMBean var1) {
      if (this.ejbLocalRefs != null) {
         this.ejbLocalRefs.remove(var1);
      }
   }

   public String getPrimkeyField() {
      return this.primkeyField;
   }

   public void setPrimkeyField(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.primkeyField;
      this.primkeyField = var1;
      this.isSet_primkeyField = var1 != null;
      this.checkChange("primkeyField", var2, this.primkeyField);
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

   public ResourceEnvRefMBean[] getResourceEnvRefs() {
      if (this.resourceEnvRefs == null) {
         return new ResourceEnvRefMBean[0];
      } else {
         ResourceEnvRefMBean[] var1 = new ResourceEnvRefMBean[this.resourceEnvRefs.size()];
         var1 = (ResourceEnvRefMBean[])((ResourceEnvRefMBean[])this.resourceEnvRefs.toArray(var1));
         return var1;
      }
   }

   public void setResourceEnvRefs(ResourceEnvRefMBean[] var1) {
      ResourceEnvRefMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getResourceEnvRefs();
      }

      this.isSet_resourceEnvRefs = true;
      if (this.resourceEnvRefs == null) {
         this.resourceEnvRefs = Collections.synchronizedList(new ArrayList());
      } else {
         this.resourceEnvRefs.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.resourceEnvRefs.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("ResourceEnvRefs", var2, this.getResourceEnvRefs());
      }

   }

   public void addResourceEnvRef(ResourceEnvRefMBean var1) {
      this.isSet_resourceEnvRefs = true;
      if (this.resourceEnvRefs == null) {
         this.resourceEnvRefs = Collections.synchronizedList(new ArrayList());
      }

      this.resourceEnvRefs.add(var1);
   }

   public void removeResourceEnvRef(ResourceEnvRefMBean var1) {
      if (this.resourceEnvRefs != null) {
         this.resourceEnvRefs.remove(var1);
      }
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

   public String getCMPVersion() {
      return this.cmpVersion;
   }

   public void setCMPVersion(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.cmpVersion;
      this.cmpVersion = var1;
      this.isSet_cmpVersion = var1 != null;
      this.checkChange("cmpVersion", var2, this.cmpVersion);
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

   public String getAbstractSchemaName() {
      return this.abstractSchemaName;
   }

   public void setAbstractSchemaName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.abstractSchemaName;
      this.abstractSchemaName = var1;
      this.isSet_abstractSchemaName = var1 != null;
      this.checkChange("abstractSchemaName", var2, this.abstractSchemaName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<entity");
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

      if (null != this.getLocalHome()) {
         var2.append(ToXML.indent(var1 + 2)).append("<local-home>").append(this.getLocalHome()).append("</local-home>\n");
      }

      if (null != this.getLocal()) {
         var2.append(ToXML.indent(var1 + 2)).append("<local>").append(this.getLocal()).append("</local>\n");
      }

      if (null != this.getEJBClass()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-class>").append(this.getEJBClass()).append("</ejb-class>\n");
      }

      if (null != this.getPersistenceType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<persistence-type>").append(this.getPersistenceType()).append("</persistence-type>\n");
      }

      if (null != this.getPrimKeyClass()) {
         var2.append(ToXML.indent(var1 + 2)).append("<prim-key-class>").append(this.getPrimKeyClass()).append("</prim-key-class>\n");
      }

      var2.append(ToXML.indent(var1 + 2)).append("<reentrant>").append(ToXML.capitalize(Boolean.valueOf(this.isReentrant()).toString())).append("</reentrant>\n");
      if ((this.isSet_cmpVersion || !"2.x".equals(this.getCMPVersion())) && null != this.getCMPVersion()) {
         var2.append(ToXML.indent(var1 + 2)).append("<cmp-version>").append(this.getCMPVersion()).append("</cmp-version>\n");
      }

      if (null != this.getAbstractSchemaName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<abstract-schema-name>").append(this.getAbstractSchemaName()).append("</abstract-schema-name>\n");
      }

      int var3;
      if (null != this.getCMPFields()) {
         for(var3 = 0; var3 < this.getCMPFields().length; ++var3) {
            var2.append(this.getCMPFields()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getPrimkeyField()) {
         var2.append(ToXML.indent(var1 + 2)).append("<primkey-field>").append(this.getPrimkeyField()).append("</primkey-field>\n");
      }

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

      if (null != this.getEJBLocalRefs()) {
         for(var3 = 0; var3 < this.getEJBLocalRefs().length; ++var3) {
            var2.append(this.getEJBLocalRefs()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getSecurityRoleRefs()) {
         for(var3 = 0; var3 < this.getSecurityRoleRefs().length; ++var3) {
            var2.append(this.getSecurityRoleRefs()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getSecurityIdentity()) {
         var2.append(this.getSecurityIdentity().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getResourceRefs()) {
         for(var3 = 0; var3 < this.getResourceRefs().length; ++var3) {
            var2.append(this.getResourceRefs()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getResourceEnvRefs()) {
         for(var3 = 0; var3 < this.getResourceEnvRefs().length; ++var3) {
            var2.append(this.getResourceEnvRefs()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getQueries()) {
         for(var3 = 0; var3 < this.getQueries().length; ++var3) {
            var2.append(this.getQueries()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</entity>\n");
      return var2.toString();
   }
}
