package weblogic.management.descriptors.ejb11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class MethodPermissionMBeanImpl extends XMLElementMBeanDelegate implements MethodPermissionMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_description = false;
   private String description;
   private boolean isSet_roleNames = false;
   private List roleNames;
   private boolean isSet_methods = false;
   private List methods;

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.description;
      this.description = var1;
      this.isSet_description = var1 != null;
      this.checkChange("description", var2, this.description);
   }

   public String[] getRoleNames() {
      if (this.roleNames == null) {
         return new String[0];
      } else {
         String[] var1 = new String[this.roleNames.size()];
         var1 = (String[])((String[])this.roleNames.toArray(var1));
         return var1;
      }
   }

   public void setRoleNames(String[] var1) {
      String[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getRoleNames();
      }

      this.isSet_roleNames = true;
      if (this.roleNames == null) {
         this.roleNames = Collections.synchronizedList(new ArrayList());
      } else {
         this.roleNames.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.roleNames.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("RoleNames", var2, this.getRoleNames());
      }

   }

   public void addRoleName(String var1) {
      this.isSet_roleNames = true;
      if (this.roleNames == null) {
         this.roleNames = Collections.synchronizedList(new ArrayList());
      }

      this.roleNames.add(var1);
   }

   public void removeRoleName(String var1) {
      if (this.roleNames != null) {
         this.roleNames.remove(var1);
      }
   }

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

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<method-permission");
      var2.append(">\n");
      if (null != this.getDescription()) {
         var2.append(ToXML.indent(var1 + 2)).append("<description>").append("<![CDATA[" + this.getDescription() + "]]>").append("</description>\n");
      }

      int var3;
      for(var3 = 0; var3 < this.getRoleNames().length; ++var3) {
         var2.append(ToXML.indent(var1 + 2)).append("<role-name>").append(this.getRoleNames()[var3]).append("</role-name>\n");
      }

      if (null != this.getMethods()) {
         for(var3 = 0; var3 < this.getMethods().length; ++var3) {
            var2.append(this.getMethods()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</method-permission>\n");
      return var2.toString();
   }
}
