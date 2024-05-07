package weblogic.connector.external;

import java.util.List;
import java.util.Properties;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;

public interface EndpointActivationUtils {
   EndpointActivationUtils accessor = weblogic.connector.external.impl.EndpointActivationUtils.getAccessor();

   void activateEndpoint(String var1, String var2, String var3, ActivationSpec var4, MessageEndpointFactory var5, MessageDrivenEJBRuntimeMBean var6) throws ResourceAdapterNotFoundException, MissingPropertiesException, EndpointActivationException;

   void deActivateEndpoint(String var1, String var2, String var3, ActivationSpec var4, MessageEndpointFactory var5, MessageDrivenEJBRuntimeMBean var6) throws EndpointActivationException;

   Object getActivationSpec(String var1, String var2) throws ActivationSpecFindOrCreateException, ResourceAdapterNotActiveException, ResourceAdapterNotFoundException;

   List getRequiredConfigProperties(String var1, String var2) throws ResourceAdapterNotFoundException, ActivationSpecFindOrCreateException;

   void suspendInbound(String var1, MessageEndpointFactory var2, Properties var3) throws EndpointActivationException;

   void resumeInbound(String var1, MessageEndpointFactory var2, Properties var3) throws EndpointActivationException;
}
