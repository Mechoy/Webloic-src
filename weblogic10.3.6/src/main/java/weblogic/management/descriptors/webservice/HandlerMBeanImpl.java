package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class HandlerMBeanImpl extends XMLElementMBeanDelegate implements HandlerMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_initParams = false;
   private InitParamsMBean initParams;
   private boolean isSet_handlerName = false;
   private String handlerName;
   private boolean isSet_className = false;
   private String className;

   public InitParamsMBean getInitParams() {
      return this.initParams;
   }

   public void setInitParams(InitParamsMBean var1) {
      InitParamsMBean var2 = this.initParams;
      this.initParams = var1;
      this.isSet_initParams = var1 != null;
      this.checkChange("initParams", var2, this.initParams);
   }

   public String getHandlerName() {
      return this.handlerName;
   }

   public void setHandlerName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.handlerName;
      this.handlerName = var1;
      this.isSet_handlerName = var1 != null;
      this.checkChange("handlerName", var2, this.handlerName);
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
      var2.append(ToXML.indent(var1)).append("<handler");
      if (this.isSet_handlerName) {
         var2.append(" name=\"").append(String.valueOf(this.getHandlerName())).append("\"");
      }

      if (this.isSet_className) {
         var2.append(" class-name=\"").append(String.valueOf(this.getClassName())).append("\"");
      }

      var2.append(">\n");
      if (null != this.getInitParams()) {
         var2.append(this.getInitParams().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</handler>\n");
      return var2.toString();
   }
}
