package weblogic.jdbc.common.internal;

import weblogic.management.ManagementException;
import weblogic.management.runtime.JDBCDataSourceTaskRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.TaskRuntimeMBeanImpl;

public class JDBCDataSourceTaskRuntimeMBeanImpl extends TaskRuntimeMBeanImpl implements JDBCDataSourceTaskRuntimeMBean {
   public JDBCDataSourceTaskRuntimeMBeanImpl(String var1, RuntimeMBean var2, boolean var3) throws ManagementException {
      super(var1, var2, var3);
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public void setStatus(String var1) {
      this.status = var1;
   }

   public void setBeginTime(long var1) {
      this.beginTime = var1;
   }

   public void setEndTime(long var1) {
      this.endTime = var1;
   }

   public void setError(Exception var1) {
      this.error = var1;
   }
}
