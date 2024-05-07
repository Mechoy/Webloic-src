package weblogic.ejb.container.internal;

import javax.security.jacc.PolicyConfiguration;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.security.jacc.RoleMapper;
import weblogic.security.service.EJBResource;

public final class ManagerHelper {
   private SecurityHelper helper;
   private String name;
   private String ejbName;
   private String pid;
   private String src;
   private PolicyConfiguration cf;
   private RoleMapper rm;

   public ManagerHelper(String var1, String var2, String var3, String var4, PolicyConfiguration var5, RoleMapper var6) {
      this.name = var1;
      this.ejbName = var2;
      this.pid = var3;
      this.src = var4;
      this.cf = var5;
      this.rm = var6;
   }

   public boolean isResWritable(EJBResource var1, String var2, String var3) {
      this.initialize();
      return this.helper.isCallerInRole(this.ejbName, var1, var2, var3);
   }

   public EJBResource createEJBResource(DeploymentInfo var1) {
      return SecurityHelper.createEJBResource(var1);
   }

   private void initialize() {
      if (this.helper == null) {
         try {
            this.helper = new SecurityHelper(this.name, this.cf, this.pid, this.src, this.rm);
         } catch (Throwable var2) {
            throw new AssertionError("could not create SecurityHelper: " + var2.getMessage());
         }
      }

   }
}
