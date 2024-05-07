package weblogic.security.spi;

/** @deprecated */
public interface DeployableRoleProvider extends RoleProvider {
   void deployRole(Resource var1, String var2, String[] var3) throws RoleCreationException;

   void undeployRole(Resource var1, String var2) throws RoleRemovalException;
}
