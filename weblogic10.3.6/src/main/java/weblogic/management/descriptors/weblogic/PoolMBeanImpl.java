package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class PoolMBeanImpl extends XMLElementMBeanDelegate implements PoolMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_initialBeansInFreePool = false;
   private int initialBeansInFreePool = 0;
   private boolean isSet_maxBeansInFreePool = false;
   private int maxBeansInFreePool = 1000;

   public int getInitialBeansInFreePool() {
      return this.initialBeansInFreePool;
   }

   public void setInitialBeansInFreePool(int var1) {
      int var2 = this.initialBeansInFreePool;
      this.initialBeansInFreePool = var1;
      this.isSet_initialBeansInFreePool = var1 != -1;
      this.checkChange("initialBeansInFreePool", var2, this.initialBeansInFreePool);
   }

   public int getMaxBeansInFreePool() {
      return this.maxBeansInFreePool;
   }

   public void setMaxBeansInFreePool(int var1) {
      int var2 = this.maxBeansInFreePool;
      this.maxBeansInFreePool = var1;
      this.isSet_maxBeansInFreePool = var1 != -1;
      this.checkChange("maxBeansInFreePool", var2, this.maxBeansInFreePool);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<pool");
      var2.append(">\n");
      if (this.isSet_maxBeansInFreePool || 1000 != this.getMaxBeansInFreePool()) {
         var2.append(ToXML.indent(var1 + 2)).append("<max-beans-in-free-pool>").append(this.getMaxBeansInFreePool()).append("</max-beans-in-free-pool>\n");
      }

      if (this.isSet_initialBeansInFreePool || 0 != this.getInitialBeansInFreePool()) {
         var2.append(ToXML.indent(var1 + 2)).append("<initial-beans-in-free-pool>").append(this.getInitialBeansInFreePool()).append("</initial-beans-in-free-pool>\n");
      }

      var2.append(ToXML.indent(var1)).append("</pool>\n");
      return var2.toString();
   }
}
