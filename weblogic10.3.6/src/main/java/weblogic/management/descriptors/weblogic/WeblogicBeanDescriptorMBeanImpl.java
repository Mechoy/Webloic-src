package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class WeblogicBeanDescriptorMBeanImpl extends XMLElementMBeanDelegate implements WeblogicBeanDescriptorMBean {
   static final long serialVersionUID = 1L;

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<weblogic-bean-descriptor");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</weblogic-bean-descriptor>\n");
      return var2.toString();
   }
}
