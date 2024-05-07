package weblogic.management.runtime;

import java.util.Date;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;

public interface WebServiceHandlerRuntimeMBean extends RuntimeMBean {
   Class getHandlerClass();

   Map getHandlerConfig();

   QName[] getHeaders();

   Throwable getInitError();

   int getRequestSOAPFaultsCount();

   SOAPFaultException getLastRequestSOAPFault();

   int getRequestErrorsCount();

   Throwable getLastRequestError();

   int getRequestTerminationsCount();

   int getResponseSOAPFaultsCount();

   SOAPFaultException getLastResponseSOAPFault();

   int getResponseErrorsCount();

   Throwable getLastResponseError();

   int getResponseTerminationsCount();

   void reset();

   Date getLastResetTime();

   boolean isInternal();
}
