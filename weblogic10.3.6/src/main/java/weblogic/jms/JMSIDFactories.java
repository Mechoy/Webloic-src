package weblogic.jms;

import weblogic.messaging.common.IDFactory;
import weblogic.messaging.common.MessageIDFactory;

public class JMSIDFactories {
   public static IDFactory idFactory = new IDFactory();
   public static MessageIDFactory messageIDFactory = new MessageIDFactory();
}
