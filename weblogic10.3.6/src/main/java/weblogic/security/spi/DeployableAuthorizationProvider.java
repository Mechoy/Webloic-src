package weblogic.security.spi;

/** @deprecated */
public interface DeployableAuthorizationProvider extends AuthorizationProvider {
   void deployPolicy(Resource var1, String[] var2) throws ResourceCreationException;

   void undeployPolicy(Resource var1) throws ResourceRemovalException;
}
