package weblogic.jms.common;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.w3c.dom.Document;
import weblogic.apache.xml.serialize.OutputFormat;
import weblogic.apache.xml.serialize.XMLSerializer;
import weblogic.jms.backend.BEConnectionConsumerImpl;
import weblogic.jms.backend.BEConsumerImpl;
import weblogic.jms.backend.MessageConsumerCreationEvent;
import weblogic.jms.backend.MessageConsumerDestroyEvent;
import weblogic.logging.jms.JMSMessageAddLogRecord;
import weblogic.logging.jms.JMSMessageConsumerCreationLogRecord;
import weblogic.logging.jms.JMSMessageConsumerDestroyLogRecord;
import weblogic.logging.jms.JMSMessageExceededLogRecord;
import weblogic.logging.jms.JMSMessageExpireLogRecord;
import weblogic.logging.jms.JMSMessageForwardLogRecord;
import weblogic.logging.jms.JMSMessageLogRecord;
import weblogic.logging.jms.JMSMessageLogger;
import weblogic.logging.jms.JMSMessageReceiveLogRecord;
import weblogic.logging.jms.JMSMessageRemoveLogRecord;
import weblogic.logging.jms.JMSMessageSendLogRecord;
import weblogic.logging.jms.JMSMessageStoreLogRecord;
import weblogic.logging.jms.JMSSAFMessageLogger;
import weblogic.messaging.kernel.Event;
import weblogic.messaging.kernel.MessageAddEvent;
import weblogic.messaging.kernel.MessageConsumerEvent;
import weblogic.messaging.kernel.MessageEvent;
import weblogic.messaging.kernel.MessageExpirationEvent;
import weblogic.messaging.kernel.MessageReceiveEvent;
import weblogic.messaging.kernel.MessageRedeliveryLimitEvent;
import weblogic.messaging.kernel.MessageRemoveEvent;
import weblogic.messaging.kernel.MessageSendEvent;
import weblogic.utils.StringUtils;

public final class JMSMessageLogHelper {
   private static final String DELIMITER = " ";

   public static final String createLogMessage(MessageImpl var0, List var1, List var2) {
      StringBuffer var3 = new StringBuffer(256);
      StringBuffer var4 = new StringBuffer(256);
      buildLogHeaderString(var1, var0, var3);
      buildLogPropertyString(var2, var0, var4);
      return var3.toString() != null && var3.toString().length() != 0 && var4.toString() != null && var4.toString().length() != 0 ? var3.toString() + " " + var4.toString() : var4.toString();
   }

   public static final void buildLogHeaderString(List var0, MessageImpl var1, StringBuffer var2) {
      String var3 = "";
      if (var0 != null && var0.size() != 0) {
         ListIterator var5 = var0.listIterator(0);
         String var6 = null;
         Object var7 = null;

         while(var5.hasNext()) {
            try {
               var6 = (String)var5.next();
               if (!var6.equalsIgnoreCase("JMSDestination")) {
                  if (var6.equalsIgnoreCase("JMSReplyTo")) {
                     var7 = var1.getJMSReplyTo();
                  } else {
                     var7 = var1.get(var6);
                  }

                  if (var7 == null) {
                     var2.append(var3 + var6 + "=" + var7);
                     var3 = " ";
                  } else {
                     if (var6.equals("JMSExpiration")) {
                        if ((Long)var7 == 0L) {
                           var7 = "Never";
                        } else {
                           var7 = new Date((Long)var7);
                        }
                     } else if (var6.equals("JMSTimestamp")) {
                        var7 = new Date((Long)var7);
                     } else if (!var6.equals("JMSCorrelationID") && !var6.equals("JMSType")) {
                        if (var6.equals("JMSDeliveryTime")) {
                           if ((Long)var7 > 0L) {
                              var7 = new Date((Long)var7);
                           } else {
                              var7 = new Date(var1.getJMSTimestamp());
                           }
                        }
                     } else {
                        var7 = truncatedIt(var7);
                     }

                     var2.append(var3 + var6 + "='" + var7 + "'");
                     var3 = " ";
                  }
               }
            } catch (Exception var9) {
            }
         }

      }
   }

   private static final void buildLogPropertyString(List var0, MessageImpl var1, StringBuffer var2) {
      String var3 = "";
      Enumeration var5 = null;
      boolean var6 = var1.includeJMSXDeliveryCount(true);

      try {
         var5 = var1.getPropertyNames();
      } catch (javax.jms.JMSException var15) {
      } finally {
         var1.includeJMSXDeliveryCount(var6);
      }

      if (var0 != null && var0.size() != 0 && var5 != null && var5.hasMoreElements()) {
         ListIterator var7 = var0.listIterator(0);
         String var8 = null;
         String var9 = null;
         HashMap var10 = new HashMap();

         while(var7.hasNext()) {
            try {
               var8 = (String)var7.next();
               if (var8.equalsIgnoreCase("%properties%")) {
                  while(var5.hasMoreElements()) {
                     String var11 = (String)var5.nextElement();
                     if (var10.get(var11) == null) {
                        var10.put(var11, var11);
                        var9 = var1.getStringProperty(var11);
                        appendTrucatedStringToLogBuffer(var3, var11, var9, var2);
                        var3 = " ";
                     }
                  }
               } else if (var1.propertyExists(var8) && var10.get(var8) == null) {
                  var10.put(var8, var8);
                  var9 = var1.getStringProperty(var8);
                  appendTrucatedStringToLogBuffer(var3, var8, var9, var2);
                  var3 = " ";
               }
            } catch (Exception var17) {
            }
         }

      }
   }

   private static final void appendTrucatedStringToLogBuffer(String var0, String var1, Object var2, StringBuffer var3) {
      String var4;
      if (var2 != null) {
         var4 = truncatedIt(var2);
      } else {
         var4 = null;
      }

      if (var4 != null) {
         var3.append(var0 + var1 + "='" + var4 + "'");
      } else {
         var3.append(var0 + var1 + "=" + var4);
      }

   }

   private static final String truncatedIt(Object var0) {
      String var1 = var0 instanceof String ? (String)var0 : var0.toString();
      if (var1 == null) {
         return null;
      } else {
         return var1.length() > 32 ? var1.substring(0, 32) + "..." : var1;
      }
   }

   public static final List convertStringToLinkedList(String var0) {
      LinkedList var1 = new LinkedList();
      if (var0 == null) {
         return var1;
      } else {
         var0 = var0.trim();
         if (var0.length() == 0) {
            return var1;
         } else {
            String[] var2 = StringUtils.splitCompletely(var0, ",");
            if ((var2 == null || var2.length == 0) && var0.indexOf(",") == -1) {
               var2 = new String[]{var0};
            }

            for(int var3 = 0; var3 < var2.length; ++var3) {
               String var4 = var2[var3].trim();
               if (!var1.contains(var4)) {
                  var1.add(var4);
               }
            }

            return var1;
         }
      }
   }

   public static final List extractJMSHeaderAndProperty(String var0, StringBuffer var1) {
      LinkedList var2 = new LinkedList();
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      if (var0 == null) {
         return var2;
      } else {
         String[] var6 = StringUtils.splitCompletely(var0, ",");
         if ((var6 == null || var6.length == 0) && var0.indexOf(",") == -1) {
            var6 = new String[]{var0};
         }

         for(int var7 = 0; var7 < var6.length; ++var7) {
            if (MessageImpl.isHeaderField(var6[var7].trim())) {
               if (!var5 && !var2.contains(var6[var7].trim())) {
                  var2.add(var6[var7].trim());
               }
            } else if (var6[var7].trim().startsWith("getJMS")) {
               if (MessageImpl.isHeaderField(var6[var7].trim().substring(3))) {
                  if (!var5 && !var2.contains(var6[var7].trim().substring(3))) {
                     var2.add(var6[var7].trim().substring(3));
                  }
               } else if (!var3) {
                  var1.append(var6[var7].trim() + ", ");
               }
            } else if (var6[var7].trim().equalsIgnoreCase("%Header%")) {
               if (!var5) {
                  var5 = true;
                  if (!var2.contains("JMSCorrelationID")) {
                     var2.add("JMSCorrelationID");
                  }

                  if (!var2.contains("JMSDeliveryMode")) {
                     var2.add("JMSDeliveryMode");
                  }

                  if (!var2.contains("JMSDeliveryTime")) {
                     var2.add("JMSDeliveryTime");
                  }

                  if (!var2.contains("JMSExpiration")) {
                     var2.add("JMSExpiration");
                  }

                  if (!var2.contains("JMSPriority")) {
                     var2.add("JMSPriority");
                  }

                  if (!var2.contains("JMSRedelivered")) {
                     var2.add("JMSRedelivered");
                  }

                  if (!var2.contains("JMSRedeliveryLimit")) {
                     var2.add("JMSRedeliveryLimit");
                  }

                  if (!var2.contains("JMSReplyTo")) {
                     var2.add("JMSReplyTo");
                  }

                  if (!var2.contains("JMSTimestamp")) {
                     var2.add("JMSTimestamp");
                  }

                  if (!var2.contains("JMSType")) {
                     var2.add("JMSType");
                  }
               }
            } else if (var6[var7].trim().equalsIgnoreCase("%properties%")) {
               if (!var3) {
                  var3 = true;
                  var1.append("%properties%, ");
               }
            } else if (var6[var7].trim().equalsIgnoreCase("%body%")) {
               if (!var4) {
                  var4 = true;
                  var1.append("%body%, ");
               }
            } else if (!var3) {
               var1.append(var6[var7].trim() + ", ");
            }
         }

         return var2;
      }
   }

   public static String addSubscriberInfo(BEConsumerImpl var0) {
      if (var0 instanceof BEConnectionConsumerImpl) {
         return "CC";
      } else if (var0.getSession() != null && var0.getSession().getConnection() != null) {
         String var1 = var0.getSession().getConnection().getAddress();
         if (var1 == null) {
            return null;
         } else {
            int var2 = var1.indexOf("|");
            return var2 < 0 ? null : "MC:CA(" + var1.substring(0, var2) + "):OAMI(" + var1.substring(var2 + 1) + ".connection" + ((JMSID)var0.getSession().getConnection().getId()).getCounter() + ".session" + ((JMSID)var0.getSession().getId()).getCounter() + ".consumer" + ((JMSID)var0.getId()).getCounter() + ")";
         }
      } else {
         return null;
      }
   }

   public static void logMessageEvent(JMSMessageEventLogListener var0, Event var1) {
      JMSMessageLogger var2 = var0.getJMSMessageLogger();
      JMSMessageLogRecord var3 = null;
      String var4 = null;
      String var5 = null;
      String var6 = null;
      String var7 = null;
      if (var1 instanceof MessageConsumerEvent) {
         String var8 = ((MessageConsumerEvent)var1).getUserBlob();
         if (var8 != null) {
            int var9 = var8.indexOf("#");
            if (var9 != -1) {
               var6 = var8.substring(0, var9);
               var5 = var8.substring(var9 + 1);
            }
         }
      }

      if (var0 != null && var0 instanceof BEConsumerImpl) {
         BEConsumerImpl var10 = (BEConsumerImpl)var0;
         if (var1 instanceof MessageConsumerCreationEvent) {
            var7 = ((MessageConsumerEvent)var1).getSelector();
         }

         if (var10.isDurable()) {
            var4 = "DS:" + var10.getClientID() + "." + var10.getSubscriptionName();
         }

         if (var1 instanceof MessageSendEvent || var4 == null) {
            var6 = addSubscriberInfo(var10);
         }
      }

      if (var5 == null) {
         var5 = var1.getSubjectName();
      }

      if (var1 instanceof MessageSendEvent) {
         if (var2 instanceof JMSSAFMessageLogger) {
            var3 = createStoreLogRecord(var2, var0, (MessageSendEvent)var1, var5, var4 != null ? var4 : var6);
         } else {
            var3 = createSendLogRecord(var2, var0, (MessageSendEvent)var1, var5, var4 != null ? var4 : var6);
         }
      } else if (var1 instanceof MessageAddEvent) {
         var3 = createAddLogRecord(var2, var0, (MessageAddEvent)var1, var5, var4);
      } else if (var1 instanceof MessageReceiveEvent) {
         if (var2 instanceof JMSSAFMessageLogger) {
            var3 = createForwardLogRecord(var2, var0, (MessageReceiveEvent)var1, var5, var4 != null ? var4 + "[" + var6 + "]" : var6);
         } else {
            var3 = createReceiveLogRecord(var2, var0, (MessageReceiveEvent)var1, var5, var4 != null ? var4 + "[" + var6 + "]" : var6);
         }
      } else if (var1 instanceof MessageExpirationEvent) {
         var3 = createExpireLogRecord(var2, var0, (MessageExpirationEvent)var1, var5, var4);
      } else if (var1 instanceof MessageRedeliveryLimitEvent) {
         var3 = createRedeliveryLimitLogRecord(var2, var0, (MessageRedeliveryLimitEvent)var1, var5, var4);
      } else if (var1 instanceof MessageRemoveEvent) {
         var3 = createRemoveLogRecord(var2, var0, (MessageRemoveEvent)var1, var5, var4);
      } else if (var1 instanceof MessageConsumerCreationEvent) {
         var3 = createConsumerCreationLogRecord(var2, var0, (MessageConsumerCreationEvent)var1, var5, var4 != null ? var4 + "[" + var6 + "]" : var6, var7);
      } else if (var1 instanceof MessageConsumerDestroyEvent) {
         var3 = createConsumerDestroyLogRecord(var2, var0, (MessageConsumerDestroyEvent)var1, var5, var4 != null ? var4 + "[" + var6 + "]" : var6);
      }

      if (var3 != null) {
         var2.log(var3);
      }

   }

   private static final String createMessageLogMessage(JMSMessageEventLogListener var0, MessageImpl var1) {
      try {
         return getMessageDocument(var1, var0.getMessageLoggingJMSHeaders(), var0.getMessageLoggingUserProperties());
      } catch (javax.jms.JMSException var3) {
         return null;
      }
   }

   private static final JMSMessageLogRecord createSendLogRecord(JMSMessageLogger var0, JMSMessageEventLogListener var1, MessageSendEvent var2, String var3, String var4) {
      return new JMSMessageSendLogRecord(getMillis(var0, var2), getNano(var0, var2), getJMSMessageContent(var1, var2), var1.getListenerName(), ((MessageImpl)var2.getMessage()).getJMSMessageID(), ((MessageImpl)var2.getMessage()).getJMSCorrelationID(), var3, var4, var2.getXid());
   }

   private static final JMSMessageLogRecord createStoreLogRecord(JMSMessageLogger var0, JMSMessageEventLogListener var1, MessageSendEvent var2, String var3, String var4) {
      return new JMSMessageStoreLogRecord(getMillis(var0, var2), getNano(var0, var2), getJMSMessageContent(var1, var2), var1.getListenerName(), ((MessageImpl)var2.getMessage()).getJMSMessageID(), ((MessageImpl)var2.getMessage()).getJMSCorrelationID(), var3, var4, var2.getXid());
   }

   private static final JMSMessageLogRecord createAddLogRecord(JMSMessageLogger var0, JMSMessageEventLogListener var1, MessageAddEvent var2, String var3, String var4) {
      return new JMSMessageAddLogRecord(getMillis(var0, var2), getNano(var0, var2), getJMSMessageContent(var1, var2), var1.getListenerName(), ((MessageImpl)var2.getMessage()).getJMSMessageID(), ((MessageImpl)var2.getMessage()).getJMSCorrelationID(), var3, var4, var2.getXid());
   }

   private static final JMSMessageLogRecord createReceiveLogRecord(JMSMessageLogger var0, JMSMessageEventLogListener var1, MessageReceiveEvent var2, String var3, String var4) {
      return new JMSMessageReceiveLogRecord(getMillis(var0, var2), getNano(var0, var2), getJMSMessageContent(var1, var2), var1.getListenerName(), ((MessageImpl)var2.getMessage()).getJMSMessageID(), ((MessageImpl)var2.getMessage()).getJMSCorrelationID(), var3, var4, var2.getXid());
   }

   private static final JMSMessageLogRecord createForwardLogRecord(JMSMessageLogger var0, JMSMessageEventLogListener var1, MessageReceiveEvent var2, String var3, String var4) {
      return new JMSMessageForwardLogRecord(getMillis(var0, var2), getNano(var0, var2), getJMSMessageContent(var1, var2), var1.getListenerName(), ((MessageImpl)var2.getMessage()).getJMSMessageID(), ((MessageImpl)var2.getMessage()).getJMSCorrelationID(), var3, var4, var2.getXid());
   }

   private static final JMSMessageLogRecord createExpireLogRecord(JMSMessageLogger var0, JMSMessageEventLogListener var1, MessageExpirationEvent var2, String var3, String var4) {
      return new JMSMessageExpireLogRecord(getMillis(var0, var2), getNano(var0, var2), getJMSMessageContent(var1, var2), var1.getListenerName(), ((MessageImpl)var2.getMessage()).getJMSMessageID(), ((MessageImpl)var2.getMessage()).getJMSCorrelationID(), var3, var4, var2.getXid());
   }

   private static final JMSMessageLogRecord createRemoveLogRecord(JMSMessageLogger var0, JMSMessageEventLogListener var1, MessageRemoveEvent var2, String var3, String var4) {
      return new JMSMessageRemoveLogRecord(getMillis(var0, var2), getNano(var0, var2), getJMSMessageContent(var1, var2), var1.getListenerName(), ((MessageImpl)var2.getMessage()).getJMSMessageID(), ((MessageImpl)var2.getMessage()).getJMSCorrelationID(), var3, var4, var2.getXid());
   }

   private static final JMSMessageLogRecord createRedeliveryLimitLogRecord(JMSMessageLogger var0, JMSMessageEventLogListener var1, MessageRedeliveryLimitEvent var2, String var3, String var4) {
      return new JMSMessageExceededLogRecord(getMillis(var0, var2), getNano(var0, var2), getJMSMessageContent(var1, var2), var1.getListenerName(), ((MessageImpl)var2.getMessage()).getJMSMessageID(), ((MessageImpl)var2.getMessage()).getJMSCorrelationID(), var3, var4, var2.getXid());
   }

   private static final JMSMessageLogRecord createConsumerCreationLogRecord(JMSMessageLogger var0, JMSMessageEventLogListener var1, MessageConsumerCreationEvent var2, String var3, String var4, String var5) {
      return new JMSMessageConsumerCreationLogRecord(getMillis(var0, var2), getNano(var0, var2), var1.getListenerName(), var3, var4, var5);
   }

   private static final JMSMessageLogRecord createConsumerDestroyLogRecord(JMSMessageLogger var0, JMSMessageEventLogListener var1, MessageConsumerDestroyEvent var2, String var3, String var4) {
      return new JMSMessageConsumerDestroyLogRecord(getMillis(var0, var2), getNano(var0, var2), var1.getListenerName(), var3, var4);
   }

   private static boolean isMessageBodyNeeded(List var0) {
      if (var0 == null) {
         return false;
      } else {
         Iterator var1 = var0.iterator();

         String var2;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            var2 = (String)var1.next();
         } while(!"%body%".equalsIgnoreCase(var2));

         return true;
      }
   }

   private static String getMessageDocument(MessageImpl var0, List var1, List var2) throws javax.jms.JMSException {
      if ((var1 == null || var1.size() == 0) && (var2 == null || var2.size() == 0)) {
         return null;
      } else {
         Document var3 = XMLHelper.getDocument(var0, var1, var2, isMessageBodyNeeded(var2));

         try {
            StringWriter var4 = new StringWriter();
            OutputFormat var5 = new OutputFormat(var3);
            XMLSerializer var6 = new XMLSerializer(var4, var5);
            var6.serialize(var3);
            return var4.toString();
         } catch (IOException var7) {
            throw new JMSException("Wrap IOException inside", var7);
         }
      }
   }

   private static long getMillis(JMSMessageLogger var0, Event var1) {
      return var0.doNano() ? var0.getStartMillisTime() + (var1.getNanoseconds() - var0.getStartNanoTime()) / 1000000L : var1.getMilliseconds();
   }

   private static long getNano(JMSMessageLogger var0, Event var1) {
      return var0.doNano() ? (var1.getNanoseconds() - var0.getStartNanoTime()) % 1000000L : 0L;
   }

   private static final String getJMSMessageContent(JMSMessageEventLogListener var0, MessageEvent var1) {
      MessageImpl var2 = (MessageImpl)var1.getMessage();
      var2 = var2.cloneit();
      var2.setDeliveryCount(var1.getDeliveryCount());
      return createMessageLogMessage(var0, var2);
   }
}
