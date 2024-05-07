package weblogic.management.descriptors.ejb11;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EJBJarMBeanImpl extends XMLElementMBeanDelegate implements EJBJarMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_enterpriseBeans = false;
   private EnterpriseBeansMBean enterpriseBeans;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_encoding = false;
   private String encoding;
   private boolean isSet_largeIcon = false;
   private String largeIcon;
   private boolean isSet_ejbClientJar = false;
   private String ejbClientJar;
   private boolean isSet_smallIcon = false;
   private String smallIcon;
   private boolean isSet_version = false;
   private String version;
   private boolean isSet_displayName = false;
   private String displayName;
   private boolean isSet_assemblyDescriptor = false;
   private AssemblyDescriptorMBean assemblyDescriptor;

   public EnterpriseBeansMBean getEnterpriseBeans() {
      return this.enterpriseBeans;
   }

   public void setEnterpriseBeans(EnterpriseBeansMBean var1) {
      EnterpriseBeansMBean var2 = this.enterpriseBeans;
      this.enterpriseBeans = var1;
      this.isSet_enterpriseBeans = var1 != null;
      this.checkChange("enterpriseBeans", var2, this.enterpriseBeans);
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

   public String getEJBClientJar() {
      return this.ejbClientJar;
   }

   public void setEJBClientJar(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbClientJar;
      this.ejbClientJar = var1;
      this.isSet_ejbClientJar = var1 != null;
      this.checkChange("ejbClientJar", var2, this.ejbClientJar);
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

   public AssemblyDescriptorMBean getAssemblyDescriptor() {
      return this.assemblyDescriptor;
   }

   public void setAssemblyDescriptor(AssemblyDescriptorMBean var1) {
      AssemblyDescriptorMBean var2 = this.assemblyDescriptor;
      this.assemblyDescriptor = var1;
      this.isSet_assemblyDescriptor = var1 != null;
      this.checkChange("assemblyDescriptor", var2, this.assemblyDescriptor);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<ejb-jar");
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

      if (null != this.getEnterpriseBeans()) {
         var2.append(this.getEnterpriseBeans().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getAssemblyDescriptor()) {
         var2.append(this.getAssemblyDescriptor().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getEJBClientJar()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-client-jar>").append(this.getEJBClientJar()).append("</ejb-client-jar>\n");
      }

      var2.append(ToXML.indent(var1)).append("</ejb-jar>\n");
      return var2.toString();
   }
}
