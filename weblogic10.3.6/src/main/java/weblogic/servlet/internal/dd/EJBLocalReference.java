package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.descriptors.webapp.EjbRefMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class EJBLocalReference extends EJBReference implements EjbRefMBean {
   public EJBLocalReference() {
   }

   public EJBLocalReference(String var1, String var2, String var3, String var4, String var5, String var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   public EJBLocalReference(EjbRefMBean var1) {
      super(var1);
   }

   public EJBLocalReference(Element var1) throws DOMProcessingException {
      this.setDescription(DOMUtils.getOptionalValueByTagName(var1, "description"));
      this.setEJBRefName(DOMUtils.getValueByTagName(var1, "ejb-ref-name"));
      this.setEJBRefType(DOMUtils.getValueByTagName(var1, "ejb-ref-type"));
      this.setHomeInterfaceName(DOMUtils.getValueByTagName(var1, "local-home"));
      this.setRemoteInterfaceName(DOMUtils.getValueByTagName(var1, "local"));
      this.setEJBLinkName(DOMUtils.getOptionalValueByTagName(var1, "ejb-link"));
      this.setRunAs(DOMUtils.getOptionalValueByTagName(var1, "run-as"));
   }

   public String toString() {
      return "EJBLocalReference(" + this.hashCode() + "," + this.getEJBRefName() + ")";
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<ejb-local-ref>\n";
      var1 += 2;
      String var3 = this.getDescription();
      if (var3 != null) {
         var2 = var2 + this.indentStr(var1) + "<description>" + var3 + "</description>\n";
      }

      var2 = var2 + this.indentStr(var1) + "<ejb-ref-name>" + this.getEJBRefName() + "</ejb-ref-name>\n";
      var2 = var2 + this.indentStr(var1) + "<ejb-ref-type>" + this.getEJBRefType() + "</ejb-ref-type>\n";
      var2 = var2 + this.indentStr(var1) + "<local-home>" + this.getHomeInterfaceName() + "</local-home>\n";
      var2 = var2 + this.indentStr(var1) + "<local>" + this.getRemoteInterfaceName() + "</local>\n";
      String var4 = this.getEJBLinkName();
      if (var4 != null) {
         var2 = var2 + this.indentStr(var1) + "<ejb-link>" + var4 + "</ejb-link>\n";
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</ejb-local-ref>\n";
      return var2;
   }

   public boolean isLocalLink() {
      return true;
   }
}
