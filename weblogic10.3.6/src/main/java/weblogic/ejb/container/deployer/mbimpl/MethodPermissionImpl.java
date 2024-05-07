package weblogic.ejb.container.deployer.mbimpl;

import java.util.ArrayList;
import java.util.Collection;
import weblogic.ejb.container.interfaces.MethodPermission;
import weblogic.j2ee.descriptor.MethodBean;
import weblogic.j2ee.descriptor.MethodPermissionBean;

public final class MethodPermissionImpl implements MethodPermission {
   MethodPermissionBean m_bean;

   public MethodPermissionImpl(MethodPermissionBean var1) {
      this.m_bean = var1;
   }

   public Collection getAllMethodDescriptors() {
      ArrayList var1 = new ArrayList();
      MethodBean[] var2 = this.m_bean.getMethods();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.add(new MethodDescriptorImpl(var2[var3]));
      }

      return var1;
   }

   public Collection getAllRoleNames() {
      ArrayList var1 = new ArrayList();
      String[] var2 = this.m_bean.getRoleNames();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.add(var2[var3]);
      }

      return var1;
   }

   public boolean isUnchecked() {
      return this.m_bean.getUnchecked() != null;
   }
}
