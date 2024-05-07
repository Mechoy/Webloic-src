package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;
import weblogic.xml.stream.XMLName;

public class FaultMBeanImpl extends XMLElementMBeanDelegate implements FaultMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_faultType = false;
   private XMLName faultType;
   private boolean isSet_faultName = false;
   private String faultName;
   private boolean isSet_className = false;
   private String className;

   public XMLName getFaultType() {
      return this.faultType;
   }

   public void setFaultType(XMLName var1) {
      XMLName var2 = this.faultType;
      this.faultType = var1;
      this.isSet_faultType = var1 != null;
      this.checkChange("faultType", var2, this.faultType);
   }

   public String getFaultName() {
      return this.faultName;
   }

   public void setFaultName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.faultName;
      this.faultName = var1;
      this.isSet_faultName = var1 != null;
      this.checkChange("faultName", var2, this.faultName);
   }

   public String getClassName() {
      return this.className;
   }

   public void setClassName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.className;
      this.className = var1;
      this.isSet_className = var1 != null;
      this.checkChange("className", var2, this.className);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<fault");
      if (this.isSet_faultType) {
         var2.append(" xmlns:").append(this.getFaultType().getPrefix()).append("=\"").append(this.getFaultType().getNamespaceUri()).append("\" type=\"").append(this.getFaultType().getQualifiedName()).append("\"");
      }

      if (this.isSet_className) {
         var2.append(" class-name=\"").append(String.valueOf(this.getClassName())).append("\"");
      }

      if (this.isSet_faultName) {
         var2.append(" name=\"").append(String.valueOf(this.getFaultName())).append("\"");
      }

      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</fault>\n");
      return var2.toString();
   }
}
