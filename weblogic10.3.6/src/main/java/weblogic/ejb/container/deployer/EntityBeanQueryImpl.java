package weblogic.ejb.container.deployer;

import weblogic.ejb.container.interfaces.EntityBeanQuery;
import weblogic.j2ee.descriptor.QueryBean;

public final class EntityBeanQueryImpl implements EntityBeanQuery {
   QueryBean qmb = null;

   public EntityBeanQueryImpl(QueryBean var1) {
      this.qmb = var1;
   }

   public String getDescription() {
      return this.qmb.getDescription();
   }

   public String getMethodSignature() {
      StringBuffer var1 = (new StringBuffer(this.getMethodName())).append("(");
      String[] var2 = this.getMethodParams();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var3 > 0) {
            var1.append(",");
         }

         var1.append(var2[var3]);
      }

      var1.append(")");
      return var1.toString();
   }

   public String getMethodName() {
      return this.qmb.getQueryMethod().getMethodName();
   }

   public String[] getMethodParams() {
      return this.qmb.getQueryMethod().getMethodParams().getMethodParams();
   }

   public String getQueryText() {
      return this.qmb.getEjbQl();
   }

   public String getResultTypeMapping() {
      return this.qmb.getResultTypeMapping();
   }
}
