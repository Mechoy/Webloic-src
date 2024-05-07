package weblogic.management.descriptors.ejb20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class EJBLocalRefMBeanImpl extends XMLElementMBeanDelegate implements EJBLocalRefMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_ejbLink = false;
   private String ejbLink;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_local = false;
   private String local;
   private boolean isSet_ejbRefName = false;
   private String ejbRefName;
   private boolean isSet_ejbRefType = false;
   private String ejbRefType;
   private boolean isSet_localHome = false;
   private String localHome;

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

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<ejb-local-ref");
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

      if (null != this.getLocalHome()) {
         var2.append(ToXML.indent(var1 + 2)).append("<local-home>").append(this.getLocalHome()).append("</local-home>\n");
      }

      if (null != this.getLocal()) {
         var2.append(ToXML.indent(var1 + 2)).append("<local>").append(this.getLocal()).append("</local>\n");
      }

      if (null != this.getEJBLink()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-link>").append(this.getEJBLink()).append("</ejb-link>\n");
      }

      var2.append(ToXML.indent(var1)).append("</ejb-local-ref>\n");
      return var2.toString();
   }
}
