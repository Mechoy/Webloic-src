package weblogic.management.runtime;

import java.util.Date;
import javax.xml.soap.SOAPException;

public interface WebServiceRuntimeMBean extends RuntimeMBean {
   String getServiceName();

   String getURI();

   String getWSDLUrl();

   String getHomePageURL();

   int getWSDLHitCount();

   int getHomePageHitCount();

   WebServiceOperationRuntimeMBean[] getOperations();

   WebServiceHandlerRuntimeMBean[] getHandlers();

   WebServiceHandlerRuntimeMBean[] getAllHandlers();

   int getMalformedRequestCount();

   SOAPException getLastMalformedRequestError();

   void reset();

   Date getLastResetTime();
}
