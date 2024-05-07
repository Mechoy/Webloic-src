package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class StatelessSessionDescriptorMBeanImpl extends XMLElementMBeanDelegate implements StatelessSessionDescriptorMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_statelessClustering = false;
   private StatelessClusteringMBean statelessClustering;
   private boolean isSet_pool = false;
   private PoolMBean pool;

   public StatelessClusteringMBean getStatelessClustering() {
      return this.statelessClustering;
   }

   public void setStatelessClustering(StatelessClusteringMBean var1) {
      StatelessClusteringMBean var2 = this.statelessClustering;
      this.statelessClustering = var1;
      this.isSet_statelessClustering = var1 != null;
      this.checkChange("statelessClustering", var2, this.statelessClustering);
   }

   public PoolMBean getPool() {
      return this.pool;
   }

   public void setPool(PoolMBean var1) {
      PoolMBean var2 = this.pool;
      this.pool = var1;
      this.isSet_pool = var1 != null;
      this.checkChange("pool", var2, this.pool);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<stateless-session-descriptor");
      var2.append(">\n");
      if (null != this.getPool()) {
         var2.append(this.getPool().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getStatelessClustering()) {
         var2.append(this.getStatelessClustering().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</stateless-session-descriptor>\n");
      return var2.toString();
   }
}
