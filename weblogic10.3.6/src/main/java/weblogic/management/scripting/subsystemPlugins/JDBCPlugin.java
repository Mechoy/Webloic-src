package weblogic.management.scripting.subsystemPlugins;

import weblogic.management.MBeanHome;
import weblogic.management.configuration.JDBCConnectionPoolMBean;
import weblogic.management.scripting.plugin.WLSTPlugin;
import weblogic.management.scripting.utils.ErrorInformation;

public class JDBCPlugin extends WLSTPlugin {
   public static JDBCConnectionPoolMBean createPool(String name) throws Exception {
      MBeanHome home = (MBeanHome)interpreter.get("home").__tojava__(MBeanHome.class);
      JDBCConnectionPoolMBean bean = (JDBCConnectionPoolMBean)home.createAdminMBean(name, "JDBCConnectionPool");
      return bean;
   }

   public static Object getPool(String name) throws Exception {
      try {
         MBeanHome home = (MBeanHome)interpreter.get("home", MBeanHome.class);
         return home.getMBean(name, "JDBCConnectionPool");
      } catch (Exception var3) {
         new ErrorInformation(var3, "Exception while getting pool");
         return null;
      }
   }
}
