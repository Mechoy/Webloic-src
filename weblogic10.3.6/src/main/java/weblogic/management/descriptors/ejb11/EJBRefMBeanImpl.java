package weblogic.management.descriptors.ejb11;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EJBRefMBeanImpl extends XMLElementMBeanDelegate implements EJBRefMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_ejbLink = false;
   private String ejbLink;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_home = false;
   private String home;
   private boolean isSet_remote = false;
   private String remote;
   private boolean isSet_ejbRefName = false;
   private String ejbRefName;
   private boolean isSet_ejbRefType = false;
   private String ejbRefType;

   public String getEJBLink() {
      return this.ejbLink;
   }

   public void setEJBLink(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbLink;
      this.ejbLink = var1;
      this.isSet_ejbLink = var1 != null;
      this.checkChange("ejbLink", var2, this.ejbLink);
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

   public String getEJBRefName() {
      return this.ejbRefName;
   }

   public void setEJBRefName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbRefName;
      this.ejbRefName = var1;
      this.isSet_ejbRefName = var1 != null;
      this.checkChange("ejbRefName", var2, this.ejbRefName);
   }

   public String getEJBRefType() {
      return this.ejbRefType;
   }

   public void setEJBRefType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbRefType;
      this.ejbRefType = var1;
      this.isSet_ejbRefType = var1 != null;
      this.checkChange("ejbRefType", var2, this.ejbRefType);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<ejb-ref");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      if (null != this.getEJBRefName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-ref-name>").append(this.getEJBRefName()).append("</ejb-ref-name>\n");
      }

      if (null != this.getEJBRefType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-ref-type>").append(this.getEJBRefType()).append("</ejb-ref-type>\n");
      }

      if (null != this.getHome()) {
         var2.append(ToXML.indent(var1 + 2)).append("<home>").append(this.getHome()).append("</home>\n");
      }

      if (null != this.getRemote()) {
         var2.append(ToXML.indent(var1 + 2)).append("<remote>").append(this.getRemote()).append("</remote>\n");
      }

      if (null != this.getEJBLink()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-link>").append(this.getEJBLink()).append("</ejb-link>\n");
      }

      var2.append(ToXML.indent(var1)).append("</ejb-ref>\n");
      return var2.toString();
   }
}
