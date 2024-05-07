package weblogic.ejb.container.deployer.mbimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import weblogic.ejb.container.interfaces.RoleDescriptor;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.wl.SecurityRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;

public final class RoleDescriptorImpl implements RoleDescriptor {
   private Collection m_securityPrincipals = new ArrayList();
   private String m_name;
   private boolean m_isExternallyDefined;

   public RoleDescriptorImpl(EjbDescriptorBean var1, SecurityRoleBean var2) {
      this.m_name = var2.getRoleName();
      WeblogicEjbJarBean var3 = var1.getWeblogicEjbJarBean();
      SecurityRoleAssignmentBean[] var4 = var3.getSecurityRoleAssignments();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var4[var5].getRoleName().equals(this.m_name)) {
            this.m_securityPrincipals.addAll(Arrays.asList((Object[])var4[var5].getPrincipalNames()));
            if (var4[var5].getExternallyDefined() != null) {
               this.m_isExternallyDefined = true;
            } else {
               this.m_isExternallyDefined = false;
            }
         }
      }

   }

   public String getName() {
      return this.m_name;
   }

   public Collection getAllSecurityPrincipals() {
      return this.m_securityPrincipals;
   }

   public boolean isExternallyDefined() {
      return this.m_isExternallyDefined;
   }
}
