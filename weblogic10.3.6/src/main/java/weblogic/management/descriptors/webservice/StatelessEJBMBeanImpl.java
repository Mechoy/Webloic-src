package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class StatelessEJBMBeanImpl extends XMLElementMBeanDelegate implements StatelessEJBMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_componentName = false;
   private String componentName;
   private boolean isSet_ejbLink = false;
   private EJBLinkMBean ejbLink;
   private boolean isSet_jndiName = false;
   private JNDINameMBean jndiName;

   public String getComponentName() {
      return this.componentName;
   }

   public void setComponentName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.componentName;
      this.componentName = var1;
      this.isSet_componentName = var1 != null;
      this.checkChange("componentName", var2, this.componentName);
   }

   public EJBLinkMBean getEJBLink() {
      return this.ejbLink;
   }

   public void setEJBLink(EJBLinkMBean var1) {
      EJBLinkMBean var2 = this.ejbLink;
      this.ejbLink = var1;
      this.isSet_ejbLink = var1 != null;
      this.checkChange("ejbLink", var2, this.ejbLink);
   }

   public JNDINameMBean getJNDIName() {
      return this.jndiName;
   }

   public void setJNDIName(JNDINameMBean var1) {
      JNDINameMBean var2 = this.jndiName;
      this.jndiName = var1;
      this.isSet_jndiName = var1 != null;
      this.checkChange("jndiName", var2, this.jndiName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<stateless-ejb");
      if (this.isSet_componentName) {
         var2.append(" name=\"").append(String.valueOf(this.getComponentName())).append("\"");
      }

      var2.append(">\n");
      if (null != this.getEJBLink()) {
         var2.append(this.getEJBLink().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getJNDIName()) {
         var2.append(this.getJNDIName().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</stateless-ejb>\n");
      return var2.toString();
   }
}
