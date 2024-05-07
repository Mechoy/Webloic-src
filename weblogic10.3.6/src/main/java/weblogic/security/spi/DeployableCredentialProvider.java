package weblogic.security.spi;

/** @deprecated */
public interface DeployableCredentialProvider extends CredentialProvider {
   void deployCredentialMapping(Resource var1, String var2, String var3, String var4) throws ResourceCreationException;

   void undeployCredentialMappings(Resource var1) throws ResourceRemovalException;
}
