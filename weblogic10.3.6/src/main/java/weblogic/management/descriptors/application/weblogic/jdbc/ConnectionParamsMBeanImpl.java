package weblogic.management.descriptors.application.weblogic.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ConnectionParamsMBeanImpl extends XMLElementMBeanDelegate implements ConnectionParamsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_parameters = false;
   private List parameters;

   public ParameterMBean[] getParameters() {
      if (this.parameters == null) {
         return new ParameterMBean[0];
      } else {
         ParameterMBean[] var1 = new ParameterMBean[this.parameters.size()];
         var1 = (ParameterMBean[])((ParameterMBean[])this.parameters.toArray(var1));
         return var1;
      }
   }

   public void setParameters(ParameterMBean[] var1) {
      ParameterMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getParameters();
      }

      this.isSet_parameters = true;
      if (this.parameters == null) {
         this.parameters = Collections.synchronizedList(new ArrayList());
      } else {
         this.parameters.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.parameters.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Parameters", var2, this.getParameters());
      }

   }

   public void addParameter(ParameterMBean var1) {
      this.isSet_parameters = true;
      if (this.parameters == null) {
         this.parameters = Collections.synchronizedList(new ArrayList());
      }

      this.parameters.add(var1);
   }

   public void removeParameter(ParameterMBean var1) {
      if (this.parameters != null) {
         this.parameters.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<connection-params");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</connection-params>\n");
      return var2.toString();
   }
}
