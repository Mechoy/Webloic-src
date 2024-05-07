package weblogic.management.descriptors.ejb20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.descriptors.ejb11.ContainerTransactionMBean;
import weblogic.management.descriptors.ejb11.SecurityRoleMBean;
import weblogic.management.tools.ToXML;

public class AssemblyDescriptorMBeanImpl extends XMLElementMBeanDelegate implements AssemblyDescriptorMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_methodPermissions = false;
   private List methodPermissions;
   private boolean isSet_excludeList = false;
   private ExcludeListMBean excludeList;
   private boolean isSet_securityRoles = false;
   private List securityRoles;
   private boolean isSet_containerTransactions = false;
   private List containerTransactions;

   public weblogic.management.descriptors.ejb11.MethodPermissionMBean[] getMethodPermissions() {
      if (this.methodPermissions == null) {
         return new weblogic.management.descriptors.ejb11.MethodPermissionMBean[0];
      } else {
         weblogic.management.descriptors.ejb11.MethodPermissionMBean[] var1 = new weblogic.management.descriptors.ejb11.MethodPermissionMBean[this.methodPermissions.size()];
         var1 = (weblogic.management.descriptors.ejb11.MethodPermissionMBean[])((weblogic.management.descriptors.ejb11.MethodPermissionMBean[])this.methodPermissions.toArray(var1));
         return var1;
      }
   }

   public void setMethodPermissions(weblogic.management.descriptors.ejb11.MethodPermissionMBean[] var1) {
      weblogic.management.descriptors.ejb11.MethodPermissionMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getMethodPermissions();
      }

      this.isSet_methodPermissions = true;
      if (this.methodPermissions == null) {
         this.methodPermissions = Collections.synchronizedList(new ArrayList());
      } else {
         this.methodPermissions.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.methodPermissions.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("MethodPermissions", var2, this.getMethodPermissions());
      }

   }

   public void addMethodPermission(weblogic.management.descriptors.ejb11.MethodPermissionMBean var1) {
      this.isSet_methodPermissions = true;
      if (this.methodPermissions == null) {
         this.methodPermissions = Collections.synchronizedList(new ArrayList());
      }

      this.methodPermissions.add(var1);
   }

   public void removeMethodPermission(weblogic.management.descriptors.ejb11.MethodPermissionMBean var1) {
      if (this.methodPermissions != null) {
         this.methodPermissions.remove(var1);
      }
   }

   public ExcludeListMBean getExcludeList() {
      return this.excludeList;
   }

   public void setExcludeList(ExcludeListMBean var1) {
      ExcludeListMBean var2 = this.excludeList;
      this.excludeList = var1;
      this.isSet_excludeList = var1 != null;
      this.checkChange("excludeList", var2, this.excludeList);
   }

   public SecurityRoleMBean[] getSecurityRoles() {
      if (this.securityRoles == null) {
         return new SecurityRoleMBean[0];
      } else {
         SecurityRoleMBean[] var1 = new SecurityRoleMBean[this.securityRoles.size()];
         var1 = (SecurityRoleMBean[])((SecurityRoleMBean[])this.securityRoles.toArray(var1));
         return var1;
      }
   }

   public void setSecurityRoles(SecurityRoleMBean[] var1) {
      SecurityRoleMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getSecurityRoles();
      }

      this.isSet_securityRoles = true;
      if (this.securityRoles == null) {
         this.securityRoles = Collections.synchronizedList(new ArrayList());
      } else {
         this.securityRoles.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.securityRoles.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("SecurityRoles", var2, this.getSecurityRoles());
      }

   }

   public void addSecurityRole(SecurityRoleMBean var1) {
      this.isSet_securityRoles = true;
      if (this.securityRoles == null) {
         this.securityRoles = Collections.synchronizedList(new ArrayList());
      }

      this.securityRoles.add(var1);
   }

   public void removeSecurityRole(SecurityRoleMBean var1) {
      if (this.securityRoles != null) {
         this.securityRoles.remove(var1);
      }
   }

   public ContainerTransactionMBean[] getContainerTransactions() {
      if (this.containerTransactions == null) {
         return new ContainerTransactionMBean[0];
      } else {
         ContainerTransactionMBean[] var1 = new ContainerTransactionMBean[this.containerTransactions.size()];
         var1 = (ContainerTransactionMBean[])((ContainerTransactionMBean[])this.containerTransactions.toArray(var1));
         return var1;
      }
   }

   public void setContainerTransactions(ContainerTransactionMBean[] var1) {
      ContainerTransactionMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getContainerTransactions();
      }

      this.isSet_containerTransactions = true;
      if (this.containerTransactions == null) {
         this.containerTransactions = Collections.synchronizedList(new ArrayList());
      } else {
         this.containerTransactions.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.containerTransactions.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("ContainerTransactions", var2, this.getContainerTransactions());
      }

   }

   public void addContainerTransaction(ContainerTransactionMBean var1) {
      this.isSet_containerTransactions = true;
      if (this.containerTransactions == null) {
         this.containerTransactions = Collections.synchronizedList(new ArrayList());
      }

      this.containerTransactions.add(var1);
   }

   public void removeContainerTransaction(ContainerTransactionMBean var1) {
      if (this.containerTransactions != null) {
         this.containerTransactions.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<assembly-descriptor");
      var2.append(">\n");
      int var3;
      if (null != this.getSecurityRoles()) {
         for(var3 = 0; var3 < this.getSecurityRoles().length; ++var3) {
            var2.append(this.getSecurityRoles()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getMethodPermissions()) {
         for(var3 = 0; var3 < this.getMethodPermissions().length; ++var3) {
            var2.append(this.getMethodPermissions()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getContainerTransactions()) {
         for(var3 = 0; var3 < this.getContainerTransactions().length; ++var3) {
            var2.append(this.getContainerTransactions()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getExcludeList()) {
         var2.append(this.getExcludeList().toXML(var1 + 2)).append("\n");
      }

      var2.append(ToXML.indent(var1)).append("</assembly-descriptor>\n");
      return var2.toString();
   }
}
