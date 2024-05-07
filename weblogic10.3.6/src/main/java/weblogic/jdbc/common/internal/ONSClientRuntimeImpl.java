package weblogic.jdbc.common.internal;

import java.util.ArrayList;
import java.util.List;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ONSClientRuntimeMBean;
import weblogic.management.runtime.ONSDaemonRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class ONSClientRuntimeImpl extends RuntimeMBeanDelegate implements ONSClientRuntimeMBean {
   JDBCDataSourceBean dsBean;
   List<ONSDaemonRuntimeMBean> daemons = new ArrayList();

   public ONSClientRuntimeImpl(JDBCDataSourceBean var1, String var2, RuntimeMBean var3) throws ManagementException {
      super(var2, var3, true);
      this.dsBean = var1;
      this.createONSDaemons();
   }

   public ONSDaemonRuntimeMBean[] getONSDaemonRuntimes() {
      return (ONSDaemonRuntimeMBean[])((ONSDaemonRuntimeMBean[])this.daemons.toArray(new ONSDaemonRuntimeMBean[this.daemons.size()]));
   }

   private void createONSDaemons() throws NumberFormatException, ManagementException {
      String var1 = this.dsBean.getJDBCOracleParams().getOnsNodeList();
      if (var1 != null && var1.length() != 0) {
         String[] var2 = var1.split("[\\s,]");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String[] var4 = var2[var3].split("[\\s:]");
            if (var4.length < 2) {
               if (JdbcDebug.JDBCRAC.isDebugEnabled()) {
                  JdbcDebug.JDBCRAC.debug("invalid ONS host/port: " + var2[var3]);
               }
            } else {
               String var5 = this.name + "_" + var3;
               ONSDaemonRuntimeImpl var6 = new ONSDaemonRuntimeImpl(var4[0], Integer.valueOf(var4[1]), this.dsBean.getJDBCOracleParams().getOnsWalletFile(), this.dsBean.getJDBCOracleParams().getOnsWalletPassword(), var5, this);
               if (JdbcDebug.JDBCRAC.isDebugEnabled()) {
                  JdbcDebug.JDBCRAC.debug("created ONSDaemonRuntimeMBean " + var5);
               }

               this.daemons.add(var6);
            }
         }

      }
   }
}
