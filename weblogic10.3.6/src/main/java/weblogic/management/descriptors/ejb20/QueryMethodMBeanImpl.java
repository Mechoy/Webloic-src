package weblogic.management.descriptors.ejb20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class QueryMethodMBeanImpl extends XMLElementMBeanDelegate implements QueryMethodMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_methodIntf = false;
   private String methodIntf;
   private boolean isSet_methodName = false;
   private String methodName;
   private boolean isSet_methodParams = false;
   private MethodParamsMBean methodParams;

   public String getMethodIntf() {
      return this.methodIntf;
   }

   public void setMethodIntf(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.methodIntf;
      this.methodIntf = var1;
      this.isSet_methodIntf = var1 != null;
      this.checkChange("methodIntf", var2, this.methodIntf);
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setMethodName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.methodName;
      this.methodName = var1;
      this.isSet_methodName = var1 != null;
      this.checkChange("methodName", var2, this.methodName);
   }

   public MethodParamsMBean getMethodParams() {
      return this.methodParams;
   }

   public void setMethodParams(MethodParamsMBean var1) {
      MethodParamsMBean var2 = this.methodParams;
      this.methodParams = var1;
      this.isSet_methodParams = var1 != null;
      this.checkChange("methodParams", var2, this.methodParams);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<query-method");
      var2.append(">\n");
      if (null != this.getMethodName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<method-name>").append(this.getMethodName()).append("</method-name>\n");
      }

      if (null != this.getMethodIntf()) {
         var2.append(ToXML.indent(var1 + 2)).append("<method-intf>").append(this.getMethodIntf()).append("</method-intf>\n");
      }

      if (null != this.getMethodParams()) {
         var2.append(this.getMethodParams().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</query-method>\n");
      return var2.toString();
   }
}
