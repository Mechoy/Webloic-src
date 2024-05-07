package weblogic.management.descriptors.ejb11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class MethodParamsMBeanImpl extends XMLElementMBeanDelegate implements MethodParamsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_methodParams = false;
   private List methodParams;

   public String[] getMethodParams() {
      if (this.methodParams == null) {
         return new String[0];
      } else {
         String[] var1 = new String[this.methodParams.size()];
         var1 = (String[])((String[])this.methodParams.toArray(var1));
         return var1;
      }
   }

   public void setMethodParams(String[] var1) {
      String[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getMethodParams();
      }

      this.isSet_methodParams = true;
      if (this.methodParams == null) {
         this.methodParams = Collections.synchronizedList(new ArrayList());
      } else {
         this.methodParams.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.methodParams.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("MethodParams", var2, this.getMethodParams());
      }

   }

   public void addMethodParam(String var1) {
      this.isSet_methodParams = true;
      if (this.methodParams == null) {
         this.methodParams = Collections.synchronizedList(new ArrayList());
      }

      this.methodParams.add(var1);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<method-params");
      var2.append(">\n");

      for(int var3 = 0; var3 < this.getMethodParams().length; ++var3) {
         var2.append(ToXML.indent(var1 + 2)).append("<method-param>").append(this.getMethodParams()[var3]).append("</method-param>\n");
      }

      var2.append(ToXML.indent(var1)).append("</method-params>\n");
      return var2.toString();
   }
}
