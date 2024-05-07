package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class StatementMBeanImpl extends XMLElementMBeanDelegate implements StatementMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_profilingEnabled = false;
   private boolean profilingEnabled;

   public boolean isProfilingEnabled() {
      return this.profilingEnabled;
   }

   public void setProfilingEnabled(boolean var1) {
      boolean var2 = this.profilingEnabled;
      this.profilingEnabled = var1;
      this.isSet_profilingEnabled = true;
      this.checkChange("profilingEnabled", var2, this.profilingEnabled);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<statement");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</statement>\n");
      return var2.toString();
   }
}
