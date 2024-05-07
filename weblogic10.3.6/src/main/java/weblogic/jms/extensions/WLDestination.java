package weblogic.jms.extensions;

import javax.jms.Destination;
import javax.jms.JMSException;

public interface WLDestination extends Destination {
   boolean isQueue();

   boolean isTopic();

   String getCreateDestinationArgument() throws JMSException;
}
