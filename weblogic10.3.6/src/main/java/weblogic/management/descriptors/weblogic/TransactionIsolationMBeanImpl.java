package weblogic.management.descriptors.weblogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.descriptors.ejb11.MethodMBean;
import weblogic.management.tools.ToXML;

public class TransactionIsolationMBeanImpl extends XMLElementMBeanDelegate implements TransactionIsolationMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_methods = false;
   private List methods;
   private boolean isSet_isolationLevel = false;
   private String isolationLevel;

   public MethodMBean[] getMethods() {
      if (this.methods == null) {
         return new MethodMBean[0];
      } else {
         MethodMBean[] var1 = new MethodMBean[this.methods.size()];
         var1 = (MethodMBean[])((MethodMBean[])this.methods.toArray(var1));
         return var1;
      }
   }

   public void setMethods(MethodMBean[] var1) {
      MethodMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getMethods();
      }

      this.isSet_methods = true;
      if (this.methods == null) {
         this.methods = Collections.synchronizedList(new ArrayList());
      } else {
         this.methods.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.methods.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Methods", var2, this.getMethods());
      }

   }

   public void addMethod(MethodMBean var1) {
      this.isSet_methods = true;
      if (this.methods == null) {
         this.methods = Collections.synchronizedList(new ArrayList());
      }

      this.methods.add(var1);
   }

   public void removeMethod(MethodMBean var1) {
      if (this.methods != null) {
         this.methods.remove(var1);
      }
   }

   public String getIsolationLevel() {
      return this.isolationLevel;
   }

   public void setIsolationLevel(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.isolationLevel;
      this.isolationLevel = var1;
      this.isSet_isolationLevel = var1 != null;
      this.checkChange("isolationLevel", var2, this.isolationLevel);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<transaction-isolation");
      var2.append(">\n");
      if (null != this.getIsolationLevel()) {
         var2.append(ToXML.indent(var1 + 2)).append("<isolation-level>").append(this.getIsolationLevel()).append("</isolation-level>\n");
      }

      if (null != this.getMethods()) {
         for(int var3 = 0; var3 < this.getMethods().length; ++var3) {
            var2.append(this.getMethods()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</transaction-isolation>\n");
      return var2.toString();
   }
}
