package weblogic.ejb.container.compliance;

import java.util.Collection;
import java.util.Iterator;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.SecurityRoleMapping;
import weblogic.ejb.container.interfaces.SecurityRoleReference;
import weblogic.utils.ErrorCollectionException;

public final class SecurityRoleChecker extends BaseComplianceChecker {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private DeploymentInfo di;

   public SecurityRoleChecker(DeploymentInfo var1) {
      this.di = var1;
   }

   public void checkSecurityRoleRefLinks() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Collection var2 = this.di.getBeanInfos();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         BeanInfo var4 = (BeanInfo)var3.next();
         Collection var5 = var4.getAllSecurityRoleReferences();
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            SecurityRoleReference var7 = (SecurityRoleReference)var6.next();
            String var8 = var7.getReferencedRole();
            if (var8 == null) {
               log.logWarning(this.fmt.NULL_SECURITY_ROLE_REF_LINK(var4.getEJBName(), var7.getRoleName()));
            } else {
               SecurityRoleMapping var9 = this.di.getDeploymentRoles();
               if (!var9.hasRole(var8)) {
                  var1.add(new ComplianceException(this.fmt.INVALID_SECURITY_ROLE_REF_LINK(var4.getEJBName(), var7.getRoleName())));
               }
            }
         }
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }
}
