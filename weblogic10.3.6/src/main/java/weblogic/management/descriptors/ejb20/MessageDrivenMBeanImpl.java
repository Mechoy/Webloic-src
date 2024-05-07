package weblogic.management.descriptors.ejb20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.descriptors.ejb11.EJBRefMBean;
import weblogic.management.descriptors.ejb11.EnvEntryMBean;
import weblogic.management.tools.ToXML;

public class MessageDrivenMBeanImpl extends XMLElementMBeanDelegate implements MessageDrivenMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_ejbRefs = false;
   private List ejbRefs;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_resourceRefs = false;
   private List resourceRefs;
   private boolean isSet_messageDrivenDestination = false;
   private MessageDrivenDestinationMBean messageDrivenDestination;
   private boolean isSet_largeIcon = false;
   private String largeIcon;
   private boolean isSet_transactionType = false;
   private String transactionType;
   private boolean isSet_displayName = false;
   private String displayName;
   private boolean isSet_ejbLocalRefs = false;
   private List ejbLocalRefs;
   private boolean isSet_ejbClass = false;
   private String ejbClass;
   private boolean isSet_subscriptionDurability = false;
   private String subscriptionDurability;
   private boolean isSet_envEntries = false;
   private List envEntries;
   private boolean isSet_securityIdentity = false;
   private SecurityIdentityMBean securityIdentity;
   private boolean isSet_acknowledgeMode = false;
   private String acknowledgeMode;
   private boolean isSet_resourceEnvRefs = false;
   private List resourceEnvRefs;
   private boolean isSet_ejbName = false;
   private String ejbName;
   private boolean isSet_smallIcon = false;
   private String smallIcon;
   private boolean isSet_messageSelector = false;
   private String messageSelector;

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

   public MessageDrivenDestinationMBean getMessageDrivenDestination() {
      return this.messageDrivenDestination;
   }

   public void setMessageDrivenDestination(MessageDrivenDestinationMBean var1) {
      MessageDrivenDestinationMBean var2 = this.messageDrivenDestination;
      this.messageDrivenDestination = var1;
      this.isSet_messageDrivenDestination = var1 != null;
      this.checkChange("messageDrivenDestination", var2, this.messageDrivenDestination);
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

   public String getSubscriptionDurability() {
      return this.subscriptionDurability;
   }

   public void setSubscriptionDurability(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.subscriptionDurability;
      this.subscriptionDurability = var1;
      this.isSet_subscriptionDurability = var1 != null;
      this.checkChange("subscriptionDurability", var2, this.subscriptionDurability);
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

   public SecurityIdentityMBean getSecurityIdentity() {
      return this.securityIdentity;
   }

   public void setSecurityIdentity(SecurityIdentityMBean var1) {
      SecurityIdentityMBean var2 = this.securityIdentity;
      this.securityIdentity = var1;
      this.isSet_securityIdentity = var1 != null;
      this.checkChange("securityIdentity", var2, this.securityIdentity);
   }

   public String getAcknowledgeMode() {
      return this.acknowledgeMode;
   }

   public void setAcknowledgeMode(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.acknowledgeMode;
      this.acknowledgeMode = var1;
      this.isSet_acknowledgeMode = var1 != null;
      this.checkChange("acknowledgeMode", var2, this.acknowledgeMode);
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

   public String getMessageSelector() {
      return this.messageSelector;
   }

   public void setMessageSelector(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.messageSelector;
      this.messageSelector = var1;
      this.isSet_messageSelector = var1 != null;
      this.checkChange("messageSelector", var2, this.messageSelector);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<message-driven");
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

      if (null != this.getEJBClass()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-class>").append(this.getEJBClass()).append("</ejb-class>\n");
      }

      if (null != this.getTransactionType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<transaction-type>").append(this.getTransactionType()).append("</transaction-type>\n");
      }

      if (null != this.getMessageSelector()) {
         var2.append(ToXML.indent(var1 + 2)).append("<message-selector>").append(this.getMessageSelector()).append("</message-selector>\n");
      }

      if (null != this.getAcknowledgeMode()) {
         var2.append(ToXML.indent(var1 + 2)).append("<acknowledge-mode>").append(this.getAcknowledgeMode()).append("</acknowledge-mode>\n");
      }

      if (null != this.getMessageDrivenDestination()) {
         var2.append(this.getMessageDrivenDestination().toXML(var1 + 2)).append("\n");
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

      if (null != this.getEJBLocalRefs()) {
         for(var3 = 0; var3 < this.getEJBLocalRefs().length; ++var3) {
            var2.append(this.getEJBLocalRefs()[var3].toXML(var1 + 2));
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

      var2.append(ToXML.indent(var1)).append("</message-driven>\n");
      return var2.toString();
   }
}
