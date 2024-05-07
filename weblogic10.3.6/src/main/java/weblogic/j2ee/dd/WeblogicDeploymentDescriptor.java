package weblogic.j2ee.dd;

import weblogic.management.descriptors.application.weblogic.EjbMBean;
import weblogic.management.descriptors.application.weblogic.JdbcConnectionPoolMBean;
import weblogic.management.descriptors.application.weblogic.SecurityMBean;
import weblogic.management.descriptors.application.weblogic.WeblogicApplicationMBean;
import weblogic.management.descriptors.application.weblogic.WeblogicApplicationMBeanImpl;
import weblogic.management.descriptors.application.weblogic.XMLMBean;
import weblogic.management.tools.ToXML;
import weblogic.utils.io.XMLWriter;

public class WeblogicDeploymentDescriptor extends WeblogicApplicationMBeanImpl implements WeblogicApplicationMBean {
   static final long serialVersionUID = 1L;
   static final String WL_DOCTYPE = "<!DOCTYPE weblogic-application PUBLIC '-//BEA Systems, Inc.//DTD WebLogic Application 9.0.0//EN' 'http://www.bea.com/servers/wls900/dtd/weblogic-application_3_0.dtd'>\n";

   public EjbMBean getEjb() {
      return super.getEjb();
   }

   public void setEjb(EjbMBean var1) {
      super.setEjb(var1);
   }

   public XMLMBean getXML() {
      return super.getXML();
   }

   public void setXML(XMLMBean var1) {
      super.setXML(var1);
   }

   public JdbcConnectionPoolMBean[] getJdbcConnectionPools() {
      return super.getJdbcConnectionPools();
   }

   public void setJdbcConnectionPools(JdbcConnectionPoolMBean[] var1) {
      super.setJdbcConnectionPools(var1);
   }

   public void addJdbcConnectionPool(JdbcConnectionPoolMBean var1) {
      super.addJdbcConnectionPool(var1);
   }

   public void removeJdbcConnectionPool(JdbcConnectionPoolMBean var1) {
      super.removeJdbcConnectionPool(var1);
   }

   public SecurityMBean getSecurity() {
      return super.getSecurity();
   }

   public void setSecurity(SecurityMBean var1) {
      super.setSecurity(var1);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      String var3 = super.getEncoding();
      if (var3 != null) {
         var2.append(ToXML.indent(var1)).append("<?xml version=\"1.0\" encoding=\"" + var3 + "\"?>\n");
      }

      var2.append(ToXML.indent(var1)).append("<!DOCTYPE weblogic-application PUBLIC '-//BEA Systems, Inc.//DTD WebLogic Application 9.0.0//EN' 'http://www.bea.com/servers/wls900/dtd/weblogic-application_3_0.dtd'>\n");
      var2.append(super.toXML(var1));
      return var2.toString();
   }

   public void toXML(XMLWriter var1) {
      var1.println(this.toXML(2));
   }
}
