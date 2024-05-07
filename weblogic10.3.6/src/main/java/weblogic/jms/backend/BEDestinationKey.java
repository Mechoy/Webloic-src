package weblogic.jms.backend;

import weblogic.application.ModuleException;
import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.jms.common.MessageImpl;

public class BEDestinationKey {
   private static final int KEY_TYPE_JMS_MESSAGEID = 0;
   private static final int KEY_TYPE_JMS_TIMESTAMP = 1;
   private static final int KEY_TYPE_JMS_CORRELATIONID = 2;
   private static final int KEY_TYPE_JMS_PRIORITY = 3;
   private static final int KEY_TYPE_JMS_EXPIRATION = 4;
   private static final int KEY_TYPE_JMS_TYPE = 5;
   private static final int KEY_TYPE_JMS_REDELIVERED = 6;
   private static final int KEY_TYPE_JMS_DELIVERY_TIME = 7;
   private static final int KEY_TYPE_JMS_BEA_SIZE = 8;
   private static final int KEY_TYPE_JMS_BEA_UNITOFORDER = 9;
   private static final int KEY_TYPE_BOOLEAN = 15;
   private static final int KEY_TYPE_BYTE = 16;
   private static final int KEY_TYPE_SHORT = 17;
   private static final int KEY_TYPE_INT = 18;
   private static final int KEY_TYPE_LONG = 19;
   private static final int KEY_TYPE_FLOAT = 20;
   private static final int KEY_TYPE_DOUBLE = 21;
   private static final int KEY_TYPE_STRING = 22;
   static final String JMS_MESSAGE_ID = "JMSMessageID";
   private static final Integer DEFAULT_INTEGER = new Integer(Integer.MIN_VALUE);
   private static final Short DEFAULT_SHORT = new Short((short)Short.MIN_VALUE);
   private static final Long DEFAULT_LONG = new Long(Long.MIN_VALUE);
   private static final Float DEFAULT_FLOAT = new Float(Float.MIN_VALUE);
   private static final Byte DEFAULT_BYTE = new Byte((byte)-128);
   private static final Double DEFAULT_DOUBLE = new Double(Double.MIN_VALUE);
   private static final String DEFAULT_STRING = new String();
   private static final Boolean DEFAULT_BOOLEAN = new Boolean(true);
   protected static final int KEY_DIRECTION_ASCENDING = 0;
   private static final int KEY_DIRECTION_DESCENDING = 1;
   private static final int HEADER_PROPERTY = 0;
   private static final int USER_PROPERTY = 1;
   private BEDestinationImpl destination;
   private final String name;
   protected final String property;
   private int propType = 0;
   protected int keyType = -2;
   protected int direction = 0;

   public BEDestinationKey(BEDestinationImpl var1, DestinationKeyBean var2) throws ModuleException {
      this.property = var2.getProperty();
      this.name = var2.getName();
      this.destination = var1;
      String var3 = var2.getSortOrder();
      if (var3.equalsIgnoreCase("Descending")) {
         this.direction = 1;
      }

      String var4 = var2.getKeyType();
      if (this.property.equalsIgnoreCase("JMSMessageID")) {
         this.keyType = 0;
      } else if (this.property.equalsIgnoreCase("JMSTimestamp")) {
         this.keyType = 1;
      } else if (this.property.equalsIgnoreCase("JMSCorrelationID")) {
         this.keyType = 2;
      } else if (this.property.equalsIgnoreCase("JMSPriority")) {
         this.keyType = 3;
      } else if (this.property.equalsIgnoreCase("JMSExpiration")) {
         this.keyType = 4;
      } else if (this.property.equalsIgnoreCase("JMSType")) {
         this.keyType = 5;
      } else if (this.property.equalsIgnoreCase("JMSRedelivered")) {
         this.keyType = 6;
      } else if (this.property.equalsIgnoreCase("JMSDeliveryTime")) {
         this.keyType = 7;
      } else if (this.property.equalsIgnoreCase("JMS_BEA_Size")) {
         this.keyType = 8;
      } else if (this.property.equalsIgnoreCase("JMS_BEA_UnitOfOrder")) {
         this.keyType = 9;
      } else if (var4.equalsIgnoreCase("Boolean")) {
         this.keyType = 15;
         this.propType = 1;
      } else if (var4.equalsIgnoreCase("Byte")) {
         this.keyType = 16;
         this.propType = 1;
      } else if (var4.equalsIgnoreCase("Short")) {
         this.keyType = 17;
         this.propType = 1;
      } else if (var4.equalsIgnoreCase("Int")) {
         this.keyType = 18;
         this.propType = 1;
      } else if (var4.equalsIgnoreCase("Long")) {
         this.keyType = 19;
         this.propType = 1;
      } else if (var4.equalsIgnoreCase("Float")) {
         this.keyType = 20;
         this.propType = 1;
      } else if (var4.equalsIgnoreCase("Double")) {
         this.keyType = 21;
         this.propType = 1;
      } else {
         if (!var4.equalsIgnoreCase("String")) {
            throw new ModuleException("JMS: One or more missing attributes for destination key '" + this.name + "'");
         }

         this.keyType = 22;
         this.propType = 1;
      }

   }

   public BEDestinationKey(BEDestinationImpl var1) {
      this.property = "JMSMessageID";
      this.name = "JMSMessageID";
      this.direction = 0;
      this.keyType = 0;
      this.destination = var1;
   }

   boolean isDefault() {
      return this.direction == 0 && this.keyType == 0 && (this.property == null || this.property == "JMSMessageID" || this.property != null && this.property.equalsIgnoreCase("JMSMessageID")) && (this.name == null || this.name == "JMSMessageID" || this.name != null && this.name.equalsIgnoreCase("JMSMessageID"));
   }

   long compareKey(MessageImpl var1, MessageImpl var2, boolean var3) {
      long var4 = 0L;
      if (this.propType == 0) {
         switch (this.keyType) {
            case 0:
               var4 = (long)var1.getId().compare(var2.getId());
               break;
            case 1:
               var4 = var1.getJMSTimestamp() - var2.getJMSTimestamp();
               break;
            case 2:
               if (var1.getJMSCorrelationID() != null && var2.getJMSCorrelationID() != null) {
                  var4 = (long)var1.getJMSCorrelationID().compareTo(var2.getJMSCorrelationID());
               } else if (var1.getJMSCorrelationID() != null) {
                  var4 = 1L;
               } else if (var2.getJMSCorrelationID() != null) {
                  var4 = -1L;
               } else {
                  var4 = 0L;
               }
               break;
            case 3:
               var4 = (long)(var1.getJMSPriority() - var2.getJMSPriority());
               break;
            case 4:
               var4 = var1.getJMSExpiration() - var2.getJMSExpiration();
               break;
            case 5:
               if (var1.getJMSType() != null && var2.getJMSType() != null) {
                  var4 = (long)var1.getJMSType().compareTo(var2.getJMSType());
               } else if (var1.getJMSType() != null) {
                  var4 = 1L;
               } else if (var2.getJMSType() != null) {
                  var4 = -1L;
               } else {
                  var4 = 0L;
               }
               break;
            case 6:
               if (var1.getJMSRedelivered() && !var2.getJMSRedelivered()) {
                  var4 = 1L;
               } else if (!var1.getJMSRedelivered() && var2.getJMSRedelivered()) {
                  var4 = -1L;
               }
               break;
            case 7:
               var4 = var1.getDeliveryTime() - var2.getDeliveryTime();
               break;
            case 8:
               var4 = var1.size() - var2.size();
               break;
            case 9:
               if (var1.getUnitOfOrder() != null && var2.getUnitOfOrder() != null) {
                  var4 = (long)var1.getUnitOfOrder().compareTo(var2.getUnitOfOrder());
               } else if (var1.getUnitOfOrder() != null) {
                  var4 = 1L;
               } else if (var2.getUnitOfOrder() != null) {
                  var4 = -1L;
               } else {
                  var4 = 0L;
               }
         }
      } else {
         Object var6;
         Object var7;
         try {
            var6 = var1.getObjectProperty(this.property);
            var7 = var2.getObjectProperty(this.property);
            if (var6 == null && var7 == null) {
               return var4;
            }
         } catch (Throwable var9) {
            return var4;
         }

         boolean var8 = false;
         switch (this.keyType) {
            case 15:
               if (!(var6 instanceof Boolean)) {
                  var6 = DEFAULT_BOOLEAN;
               }

               if (!(var7 instanceof Boolean)) {
                  var7 = DEFAULT_BOOLEAN;
               }

               if ((Boolean)var6 && !(Boolean)var7) {
                  var4 = 1L;
               } else if (!(Boolean)var6 && (Boolean)var7) {
                  var4 = -1L;
               }

               var8 = true;
               break;
            case 16:
               if (!(var6 instanceof Byte)) {
                  var6 = DEFAULT_BYTE;
               }

               if (!(var7 instanceof Byte)) {
                  var7 = DEFAULT_BYTE;
               }
               break;
            case 17:
               if (!(var6 instanceof Short)) {
                  var6 = DEFAULT_SHORT;
               }

               if (!(var7 instanceof Short)) {
                  var7 = DEFAULT_SHORT;
               }
               break;
            case 18:
               if (!(var6 instanceof Integer)) {
                  var6 = DEFAULT_INTEGER;
               }

               if (!(var7 instanceof Integer)) {
                  var7 = DEFAULT_INTEGER;
               }
               break;
            case 19:
               if (!(var6 instanceof Long)) {
                  var6 = DEFAULT_LONG;
               }

               if (!(var7 instanceof Long)) {
                  var7 = DEFAULT_LONG;
               }
               break;
            case 20:
               if (!(var6 instanceof Float)) {
                  var6 = DEFAULT_FLOAT;
               }

               if (!(var7 instanceof Float)) {
                  var7 = DEFAULT_FLOAT;
               }
               break;
            case 21:
               if (!(var6 instanceof Double)) {
                  var6 = DEFAULT_DOUBLE;
               }

               if (!(var7 instanceof Double)) {
                  var7 = DEFAULT_DOUBLE;
               }
               break;
            case 22:
               if (!(var6 instanceof String)) {
                  var6 = DEFAULT_STRING;
               }

               if (!(var7 instanceof String)) {
                  var7 = DEFAULT_STRING;
               }
         }

         if (!var8) {
            var4 = (long)((Comparable)var6).compareTo(var7);
         }
      }

      if (var3) {
         return this.direction == 0 ? -var4 : var4;
      } else {
         return this.direction == 0 ? var4 : -var4;
      }
   }

   public String toString() {
      return "BEDestinationKey  propType=" + this.propType + ", keyType=" + this.keyType + ", direction=" + this.direction + ", name=" + this.name + ", property=" + this.property + " Dest=" + this.destination;
   }
}
