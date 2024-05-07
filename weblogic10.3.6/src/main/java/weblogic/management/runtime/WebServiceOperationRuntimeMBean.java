package weblogic.management.runtime;

import java.util.Date;
import javax.xml.soap.SOAPException;

public interface WebServiceOperationRuntimeMBean extends RuntimeMBean {
   int STATELESS_JAVA_TYPE = 1;
   int STATEFUL_JAVA_TYPE = 2;
   int JMS_QUEUE_RECEIVER_TYPE = 3;
   int JMS_TOPIC_SUBSCRIBER_TYPE = 4;
   int JMS_TOPIC_SENDER_TYPE = 5;
   int JMS_QUEUE_SENDER_TYPE = 6;
   int STATELESS_SESSION_EJB_TYPE = 7;

   String getOperationName();

   int getComponentType();

   String getComponentInfo();

   WebServiceHandlerRuntimeMBean[] getHandlers();

   WebServiceHandlerRuntimeMBean getInvocationHandler();

   WebServiceHandlerRuntimeMBean[] getAllHandlers();

   int getInvocationCount();

   long getDispatchTimeTotal();

   long getDispatchTimeHigh();

   long getDispatchTimeLow();

   long getDispatchTimeAverage();

   long getExecutionTimeTotal();

   long getExecutionTimeHigh();

   long getExecutionTimeLow();

   long getExecutionTimeAverage();

   long getResponseTimeTotal();

   long getResponseTimeHigh();

   long getResponseTimeLow();

   long getResponseTimeAverage();

   int getResponseErrorCount();

   SOAPException getLastResponseError();

   void reset();

   Date getLastResetTime();
}
