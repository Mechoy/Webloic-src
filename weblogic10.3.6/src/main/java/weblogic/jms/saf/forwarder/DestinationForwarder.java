package weblogic.jms.saf.forwarder;

import weblogic.jms.extensions.WLMessageProducer;

public interface DestinationForwarder extends WLMessageProducer {
   String CREATE_TIMERNAME = "weblogic.jms.JMSProducerTimer";
   long CREATE_TIMERINTERVAL = 10000L;
   long CREATE_INITIALDELAY = 100000L;
   int QUEUE_TYPE = 1;
   int TOPIC_TYPE = 2;

   int getDestinationType();

   long getForwarderCreateTimerInterval();

   void setForwarderCreateTimerInterval(long var1);

   long getForwarderCreateInitialDelay();

   void setForwarderCreateInitialDelay(long var1);
}
