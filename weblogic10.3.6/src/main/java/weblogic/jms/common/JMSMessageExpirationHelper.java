package weblogic.jms.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import weblogic.jms.JMSLogger;
import weblogic.utils.StringUtils;

public final class JMSMessageExpirationHelper {
   public static final void logExpiredMessage(MessageImpl var0, List var1, List var2) {
      StringBuffer var3 = new StringBuffer(256);
      StringBuffer var4 = new StringBuffer(256);
      boolean var5 = buildLogHeaderString(var1, var0, var3);
      boolean var6 = buildLogPropertyString(var2, var0, var4);
      if (var5 & var6) {
         JMSLogger.logExpiredMessageHeaderProperty("'" + var0.getJMSMessageID() + "'", var3.toString(), var4.toString());
      } else if (var5) {
         JMSLogger.logExpiredMessageHeader("'" + var0.getJMSMessageID() + "'", var3.toString());
      } else if (var6) {
         JMSLogger.logExpiredMessageProperty("'" + var0.getJMSMessageID() + "'", var4.toString());
      } else {
         JMSLogger.logExpiredMessageNoHeaderProperty("'" + var0.getJMSMessageID() + "'");
      }

   }

   public static final void logExpiredSAFMessage(MessageImpl var0, List var1, List var2) {
      StringBuffer var3 = new StringBuffer(256);
      StringBuffer var4 = new StringBuffer(256);
      boolean var5 = buildLogHeaderString(var1, var0, var3);
      boolean var6 = buildLogPropertyString(var2, var0, var4);
      if (var5 & var6) {
         JMSLogger.logExpiredSAFMessageHeaderProperty("'" + var0.getJMSMessageID() + "'", var3.toString(), var4.toString());
      } else if (var5) {
         JMSLogger.logExpiredSAFMessageHeader("'" + var0.getJMSMessageID() + "'", var3.toString());
      } else if (var6) {
         JMSLogger.logExpiredSAFMessageProperty("'" + var0.getJMSMessageID() + "'", var4.toString());
      } else {
         JMSLogger.logExpiredSAFMessageNoHeaderProperty("'" + var0.getJMSMessageID() + "'");
      }

   }

   public static final boolean buildLogHeaderString(List var0, MessageImpl var1, StringBuffer var2) {
      boolean var3 = false;
      if (var0 != null && var0.size() != 0) {
         ListIterator var5 = var0.listIterator(0);
         String var6 = new String();
         String var7 = null;
         Object var8 = null;

         while(var5.hasNext()) {
            if (!var3) {
               var3 = true;
               var2.insert(0, "<HeaderFields ");
            }

            try {
               var7 = (String)var5.next();
               if (var7.equalsIgnoreCase("JMSDestination")) {
                  var8 = var1.getJMSDestination();
               } else if (var7.equalsIgnoreCase("JMSReplyTo")) {
                  var8 = var1.getJMSReplyTo();
               } else {
                  var8 = var1.get(var7);
               }

               if (var8 != null) {
                  if (var7.equals("JMSExpiration")) {
                     var8 = new Date((Long)var8);
                  } else if (var7.equals("JMSTimestamp")) {
                     var8 = new Date((Long)var8);
                  } else if (!var7.equals("JMSCorrelationID") && !var7.equals("JMSType")) {
                     if (var7.equals("JMSDeliveryTime")) {
                        if ((Long)var8 > 0L) {
                           var8 = new Date((Long)var8);
                        } else {
                           var8 = new Date(var1.getJMSTimestamp());
                        }
                     }
                  } else {
                     var8 = truncatedIt(var8);
                  }

                  var2.append(var6 + var7 + "='" + var8 + "'");
               } else {
                  var2.append(var6 + var7 + "=" + var8);
               }

               if (var6.length() == 0) {
                  var6 = "\n    ";
               }
            } catch (Exception var10) {
            }
         }

         var2.append(" />");
         return var3;
      } else {
         return var3;
      }
   }

   private static final boolean buildLogPropertyString(List var0, MessageImpl var1, StringBuffer var2) {
      boolean var3 = false;
      if (var0 != null && var0.size() != 0 && var1.hasProperties()) {
         ListIterator var5 = var0.listIterator(0);
         String var6 = new String();
         String var7 = null;
         String var8 = null;
         HashMap var9 = new HashMap();

         while(var5.hasNext()) {
            try {
               var7 = (String)var5.next();
               if (var7.equalsIgnoreCase("%Properties%")) {
                  if (!var3) {
                     var3 = true;
                  }

                  Iterator var10 = null;
                  boolean var11 = var1.includeJMSXDeliveryCount(true);

                  try {
                     var10 = var1.getPropertyNameCollection().iterator();
                  } finally {
                     var1.includeJMSXDeliveryCount(var11);
                  }

                  while(var10.hasNext()) {
                     String var12 = (String)var10.next();
                     if (var9.get(var12) == null) {
                        var9.put(var12, var12);
                        var8 = var1.getStringProperty(var12);
                        appendTrucatedStringToLogBuffer(var6, var12, var8, var2);
                        if (var6.length() == 0) {
                           var6 = "\n    ";
                        }
                     }
                  }
               } else if (var1.propertyExists(var7) && var9.get(var7) == null) {
                  var9.put(var7, var7);
                  if (!var3) {
                     var3 = true;
                  }

                  var8 = var1.getStringProperty(var7);
                  appendTrucatedStringToLogBuffer(var6, var7, var8, var2);
                  if (var6.length() == 0) {
                     var6 = "\n    ";
                  }
               }
            } catch (Exception var17) {
            }
         }

         var2.insert(0, "<UserProperties ");
         var2.append(" />");
         return var3;
      } else {
         return var3;
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
      if (var0 == null) {
         return null;
      } else {
         LinkedList var1 = new LinkedList();
         String[] var2 = StringUtils.splitCompletely(var0, ",");
         if ((var2 == null || var2.length == 0) && var0.indexOf(",") == -1) {
            var2 = new String[]{var0};
         }

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (!var1.contains(var2[var3].trim())) {
               var1.add(var2[var3].trim());
            }
         }

         return var1;
      }
   }

   public static final List extractJMSHeaderAndProperty(String var0, StringBuffer var1) {
      LinkedList var2 = new LinkedList();
      boolean var3 = false;
      boolean var4 = false;
      if (var0 == null) {
         return null;
      } else {
         String[] var5 = StringUtils.splitCompletely(var0, ",");
         if ((var5 == null || var5.length == 0) && var0.indexOf(",") == -1) {
            var5 = new String[]{var0};
         }

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (MessageImpl.isHeaderField(var5[var6].trim())) {
               if (!var4 && !var2.contains(var5[var6].trim())) {
                  var2.add(var5[var6].trim());
               }
            } else if (var5[var6].trim().startsWith("getJMS")) {
               if (MessageImpl.isHeaderField(var5[var6].trim().substring(3))) {
                  if (!var4 && !var2.contains(var5[var6].trim().substring(3))) {
                     var2.add(var5[var6].trim().substring(3));
                  }
               } else if (!var3) {
                  var1.append(var5[var6].trim() + ", ");
               }
            } else if (var5[var6].trim().equalsIgnoreCase("%Header%")) {
               if (!var4) {
                  var4 = true;
                  if (!var2.contains("JMSCorrelationID")) {
                     var2.add("JMSCorrelationID");
                  }

                  if (!var2.contains("JMSDeliveryMode")) {
                     var2.add("JMSDeliveryMode");
                  }

                  if (!var2.contains("JMSDeliveryTime")) {
                     var2.add("JMSDeliveryTime");
                  }

                  if (!var2.contains("JMSDestination")) {
                     var2.add("JMSDestination");
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
            } else if (var5[var6].trim().equalsIgnoreCase("%Properties%")) {
               if (!var3) {
                  var3 = true;
                  var1.append("%Properties%, ");
               }
            } else if (!var3) {
               var1.append(var5[var6].trim() + ", ");
            }
         }

         return var2;
      }
   }
}
