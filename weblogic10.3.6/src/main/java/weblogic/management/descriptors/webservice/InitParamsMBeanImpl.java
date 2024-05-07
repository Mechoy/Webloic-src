package weblogic.management.descriptors.webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class InitParamsMBeanImpl extends XMLElementMBeanDelegate implements InitParamsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_initParams = false;
   private List initParams;

   public InitParamMBean[] getInitParams() {
      if (this.initParams == null) {
         return new InitParamMBean[0];
      } else {
         InitParamMBean[] var1 = new InitParamMBean[this.initParams.size()];
         var1 = (InitParamMBean[])((InitParamMBean[])this.initParams.toArray(var1));
         return var1;
      }
   }

   public void setInitParams(InitParamMBean[] var1) {
      InitParamMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getInitParams();
      }

      this.isSet_initParams = true;
      if (this.initParams == null) {
         this.initParams = Collections.synchronizedList(new ArrayList());
      } else {
         this.initParams.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.initParams.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("InitParams", var2, this.getInitParams());
      }

   }

   public void addInitParam(InitParamMBean var1) {
      this.isSet_initParams = true;
      if (this.initParams == null) {
         this.initParams = Collections.synchronizedList(new ArrayList());
      }

      this.initParams.add(var1);
   }

   public void removeInitParam(InitParamMBean var1) {
      if (this.initParams != null) {
         this.initParams.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<init-params");
      var2.append(">\n");
      if (null != this.getInitParams()) {
         for(int var3 = 0; var3 < this.getInitParams().length; ++var3) {
            var2.append(this.getInitParams()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</init-params>\n");
      return var2.toString();
   }
}
