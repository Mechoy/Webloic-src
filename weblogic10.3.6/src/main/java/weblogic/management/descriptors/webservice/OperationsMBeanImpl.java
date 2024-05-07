package weblogic.management.descriptors.webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class OperationsMBeanImpl extends XMLElementMBeanDelegate implements OperationsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_operations = false;
   private List operations;

   public OperationMBean[] getOperations() {
      if (this.operations == null) {
         return new OperationMBean[0];
      } else {
         OperationMBean[] var1 = new OperationMBean[this.operations.size()];
         var1 = (OperationMBean[])((OperationMBean[])this.operations.toArray(var1));
         return var1;
      }
   }

   public void setOperations(OperationMBean[] var1) {
      OperationMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getOperations();
      }

      this.isSet_operations = true;
      if (this.operations == null) {
         this.operations = Collections.synchronizedList(new ArrayList());
      } else {
         this.operations.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.operations.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Operations", var2, this.getOperations());
      }

   }

   public void addOperation(OperationMBean var1) {
      this.isSet_operations = true;
      if (this.operations == null) {
         this.operations = Collections.synchronizedList(new ArrayList());
      }

      this.operations.add(var1);
   }

   public void removeOperation(OperationMBean var1) {
      if (this.operations != null) {
         this.operations.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<operations");
      var2.append(">\n");
      if (null != this.getOperations()) {
         for(int var3 = 0; var3 < this.getOperations().length; ++var3) {
            var2.append(this.getOperations()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</operations>\n");
      return var2.toString();
   }
}
