package weblogic.connector.external.impl;

import weblogic.connector.external.SecurityPermissionInfo;
import weblogic.j2ee.descriptor.SecurityPermissionBean;

public class SecurityPermissionInfoImpl implements SecurityPermissionInfo {
   private SecurityPermissionBean securityPerm;

   public SecurityPermissionInfoImpl(SecurityPermissionBean var1) {
      this.securityPerm = var1;
   }

   public String getDescription() {
      String[] var1 = this.securityPerm.getDescriptions();
      return var1[0];
   }

   public String[] getDescriptions() {
      return this.securityPerm.getDescriptions();
   }

   public String getSpec() {
      return this.securityPerm.getSecurityPermissionSpec();
   }
}
