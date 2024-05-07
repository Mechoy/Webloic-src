package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.EjbRefMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class EJBReference extends BaseServletDescriptor implements EjbRefMBean {
   private static final long serialVersionUID = 6384556811917373836L;
   String description;
   String ejbLink;
   String ejbRefName;
   String ejbRefType;
   String home;
   String remote;
   String runAs;

   public EJBReference() {
   }

   public EJBReference(String var1, String var2, String var3, String var4, String var5, String var6) {
      this.setDescription(var1);
      this.setEJBRefName(var2);
      this.setEJBRefType(var3);
      this.setHomeInterfaceName(var4);
      this.setRemoteInterfaceName(var5);
      this.setEJBLinkName(var6);
   }

   public EJBReference(EjbRefMBean var1) {
      this.setDescription(var1.getDescription());
      this.setEJBLinkName(var1.getEJBLinkName());
      this.setEJBRefName(var1.getEJBRefName());
      this.setEJBRefType(var1.getEJBRefType());
      this.setHomeInterfaceName(var1.getHomeInterfaceName());
      this.setRemoteInterfaceName(var1.getRemoteInterfaceName());
      this.setRunAs(var1.getRunAs());
   }

   public EJBReference(Element var1) throws DOMProcessingException {
      this.setDescription(DOMUtils.getOptionalValueByTagName(var1, "description"));
      this.setEJBRefName(DOMUtils.getValueByTagName(var1, "ejb-ref-name"));
      this.setEJBRefType(DOMUtils.getValueByTagName(var1, "ejb-ref-type"));
      this.setHomeInterfaceName(DOMUtils.getValueByTagName(var1, "home"));
      this.setRemoteInterfaceName(DOMUtils.getValueByTagName(var1, "remote"));
      this.setEJBLinkName(DOMUtils.getOptionalValueByTagName(var1, "ejb-link"));
      this.setRunAs(DOMUtils.getOptionalValueByTagName(var1, "run-as"));
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      String var2 = this.description;
      this.description = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("description", var2, var1);
      }

   }

   public String getEJBRefName() {
      return this.ejbRefName;
   }

   public void setEJBRefName(String var1) {
      String var2 = this.ejbRefName;
      this.ejbRefName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("ejbRefName", var2, var1);
      }

   }

   public String getEJBRefType() {
      return this.ejbRefType;
   }

   public void setEJBRefType(String var1) {
      String var2 = this.ejbRefType;
      this.ejbRefType = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("ejbRefType", var2, var1);
      }

   }

   public String getHomeInterfaceName() {
      return this.home;
   }

   public void setHomeInterfaceName(String var1) {
      String var2 = this.home;
      this.home = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("homeInterfaceName", var2, var1);
      }

   }

   public String getEJBLinkName() {
      return this.ejbLink;
   }

   public void setEJBLinkName(String var1) {
      String var2 = this.ejbLink;
      this.ejbLink = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("ejbLinkName", var2, var1);
      }

   }

   public String getRemoteInterfaceName() {
      return this.remote;
   }

   public void setRemoteInterfaceName(String var1) {
      String var2 = this.remote;
      this.remote = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("remoteInterfaceName", var2, var1);
      }

   }

   public String getRunAs() {
      return this.runAs;
   }

   public void setRunAs(String var1) {
      String var2 = this.runAs;
      this.runAs = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("runAs", var2, var1);
      }

   }

   public String toString() {
      return this.getEJBRefName();
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<ejb-ref>\n";
      var1 += 2;
      String var3 = this.getDescription();
      if (var3 != null) {
         var2 = var2 + this.indentStr(var1) + "<description>" + var3 + "</description>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<ejb-ref-name>" + this.getEJBRefName() + "</ejb-ref-name>\n";
      var2 = var2 + this.indentStr(var1) + "<ejb-ref-type>" + this.getEJBRefType() + "</ejb-ref-type>\n";
      var2 = var2 + this.indentStr(var1) + "<home>" + this.getHomeInterfaceName() + "</home>\n";
      var2 = var2 + this.indentStr(var1) + "<remote>" + this.getRemoteInterfaceName() + "</remote>\n";
      String var4 = this.getEJBLinkName();
      if (var4 != null) {
         var2 = var2 + this.indentStr(var1) + "<ejb-link>" + var4 + "</ejb-link>\n";
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</ejb-ref>\n";
      return var2;
   }

   public boolean isLocalLink() {
      return false;
   }
}
