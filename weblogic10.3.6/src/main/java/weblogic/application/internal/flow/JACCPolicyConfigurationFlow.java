package weblogic.application.internal.flow;

import java.security.Policy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyContextException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.Flow;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.wl.ApplicationSecurityRoleAssignmentBean;
import weblogic.management.DeploymentException;
import weblogic.security.jacc.RoleMapper;
import weblogic.security.jacc.RoleMapperFactory;

public final class JACCPolicyConfigurationFlow extends BaseFlow implements Flow {
   public JACCPolicyConfigurationFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      if (this.appCtx.useJACC()) {
         this.handleRoleMapping();
      }
   }

   public void activate() throws DeploymentException {
      if (this.appCtx.useJACC()) {
         PolicyConfiguration[] var1 = this.appCtx.getJACCPolicyConfigurations();
         this.linkPolicyConfigurations(var1);
         this.commitPolicyConfigurations(var1);
         this.refreshPolicy(var1);
      }
   }

   public void unprepare() {
      if (this.appCtx.useJACC()) {
         this.refreshPolicy(this.appCtx.getJACCPolicyConfigurations());
      }
   }

   private void handleRoleMapping() throws DeploymentException {
      Map var1 = this.processRoleMappings();
      if (var1 != null) {
         RoleMapperFactory var2;
         try {
            var2 = RoleMapperFactory.getRoleMapperFactory();
         } catch (ClassNotFoundException var4) {
            throw new DeploymentException(var4);
         } catch (PolicyContextException var5) {
            throw new DeploymentException(var5);
         }

         RoleMapper var3 = var2.getRoleMapper(this.appCtx.getApplicationId(), false);
         var3.addAppRolesToPrincipalMap(var1);
      }

   }

   private void refreshPolicy(PolicyConfiguration[] var1) {
      if (var1.length > 0) {
         Policy.getPolicy().refresh();
      }

   }

   private void linkPolicyConfigurations(PolicyConfiguration[] var1) throws DeploymentException {
      if (var1.length != 1) {
         for(int var2 = var1.length - 1; var2 > 0; --var2) {
            try {
               var1[var2].linkConfiguration(var1[var2 - 1]);
            } catch (PolicyContextException var4) {
               throw new DeploymentException(var4);
            }
         }

      }
   }

   private void commitPolicyConfigurations(PolicyConfiguration[] var1) throws DeploymentException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         try {
            var1[var2].commit();
         } catch (PolicyContextException var4) {
            throw new DeploymentException(var4);
         }
      }

   }

   private String[] getSecurityRoleNames() {
      String[] var1 = null;
      ApplicationBean var2 = this.appCtx.getApplicationDD();
      if (var2 == null) {
         return null;
      } else {
         SecurityRoleBean[] var3 = var2.getSecurityRoles();
         if (var3 != null && var3.length != 0) {
            var1 = new String[var3.length];

            for(int var4 = 0; var4 < var3.length; ++var4) {
               var1[var4] = var3[var4].getRoleName();
            }
         }

         return var1;
      }
   }

   private Map processRoleMappings() throws DeploymentException {
      String[] var1 = this.getSecurityRoleNames();
      Map var2 = this.getRoleToPrincipalsMapping();
      if (var2 == null) {
         return null;
      } else {
         if (var1 == null || var1.length == 0) {
            var1 = (String[])((String[])var2.keySet().toArray(new String[var2.size()]));
         }

         if (var1 != null && var1.length != 0) {
            HashMap var3 = new HashMap(var1.length);
            HashSet var4 = new HashSet();

            for(int var5 = 0; var5 < var1.length; ++var5) {
               String var6 = var1[var5];
               String[] var7 = (String[])((String[])var2.get(var6));
               if (var7 == null) {
                  var4.add(var6);
               } else {
                  var3.put(var6, var7);
               }
            }

            if (!var4.isEmpty()) {
               throw new DeploymentException("Cannot find a role mapping for the following roles: " + var4);
            } else {
               return var3;
            }
         } else {
            return null;
         }
      }
   }

   private Map getRoleToPrincipalsMapping() {
      if (this.appCtx.getWLApplicationDD() == null) {
         return null;
      } else if (this.appCtx.getWLApplicationDD().getSecurity() == null) {
         return null;
      } else {
         ApplicationSecurityRoleAssignmentBean[] var1 = this.appCtx.getWLApplicationDD().getSecurity().getSecurityRoleAssignments();
         if (var1 != null && var1.length != 0) {
            HashMap var2 = new HashMap();

            for(int var3 = 0; var3 < var1.length; ++var3) {
               String[] var4 = var1[var3].getPrincipalNames();
               if (var4 != null && var4.length > 0) {
                  var2.put(var1[var3].getRoleName(), var4);
               }
            }

            return var2;
         } else {
            return null;
         }
      }
   }
}
