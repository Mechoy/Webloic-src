package weblogic.wsee.reliability;

import java.util.List;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.async.AsyncPostCallContext;

public interface ReliabilityErrorContext {
   String getOperationName();

   String getTargetName();

   SOAPMessage getSOAPMessage();

   /** @deprecated */
   ReliableDeliveryException getFault();

   List<Throwable> getFaults();

   String getFaultSummaryMessage();

   AsyncPostCallContext getAsyncPostCallContext();
}
