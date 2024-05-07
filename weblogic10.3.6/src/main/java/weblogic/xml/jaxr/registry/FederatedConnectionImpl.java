package weblogic.xml.jaxr.registry;

import java.util.Properties;
import javax.xml.registry.FederatedConnection;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.provider.ProviderInfo;

public class FederatedConnectionImpl extends ConnectionImpl implements FederatedConnection {
   public FederatedConnectionImpl(Properties var1, ProviderInfo var2) throws JAXRException {
      super(var1, var2);
   }
}
