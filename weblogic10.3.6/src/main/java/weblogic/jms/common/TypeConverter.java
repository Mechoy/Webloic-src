package weblogic.jms.common;

import weblogic.jms.JMSClientExceptionLogger;

final class TypeConverter {
   public static boolean toBoolean(Object var0) throws javax.jms.MessageFormatException {
      if (var0 == null) {
         return false;
      } else if (var0 instanceof Boolean) {
         return (Boolean)var0;
      } else if (var0 instanceof String) {
         return Boolean.valueOf((String)var0);
      } else {
         throw new MessageFormatException(JMSClientExceptionLogger.logConvertBooleanLoggable(var0.toString()).getMessage());
      }
   }

   public static byte toByte(Object var0) throws javax.jms.MessageFormatException {
      if (var0 == null) {
         throw new NumberFormatException(JMSClientExceptionLogger.logNullByteLoggable().getMessage());
      } else if (var0 instanceof Byte) {
         return (Byte)var0;
      } else if (var0 instanceof String) {
         return Byte.parseByte((String)var0);
      } else {
         throw new MessageFormatException(JMSClientExceptionLogger.logConvertByteLoggable(var0.toString()).getMessage());
      }
   }

   public static short toShort(Object var0) throws javax.jms.MessageFormatException {
      if (var0 == null) {
         throw new NumberFormatException(JMSClientExceptionLogger.logNullShortLoggable().getMessage());
      } else if (!(var0 instanceof Byte) && !(var0 instanceof Short)) {
         if (var0 instanceof String) {
            return Short.parseShort((String)var0);
         } else {
            throw new MessageFormatException(JMSClientExceptionLogger.logConvertShortLoggable(var0.toString()).getMessage());
         }
      } else {
         return ((Number)var0).shortValue();
      }
   }

   public static char toChar(Object var0) throws javax.jms.MessageFormatException {
      if (var0 == null) {
         throw new NullPointerException(JMSClientExceptionLogger.logNullCharLoggable().getMessage());
      } else {
         try {
            Character var1 = (Character)var0;
            return var1;
         } catch (ClassCastException var4) {
            try {
               String var2 = (String)var0;
               if (var2.length() == 1) {
                  return var2.charAt(0);
               }
            } catch (ClassCastException var3) {
            }

            throw new MessageFormatException(JMSClientExceptionLogger.logConvertCharLoggable(var0.toString()).getMessage());
         }
      }
   }

   public static int toInt(Object var0) throws javax.jms.MessageFormatException {
      if (var0 == null) {
         throw new NumberFormatException(JMSClientExceptionLogger.logNullIntLoggable().getMessage());
      } else if (!(var0 instanceof Integer) && !(var0 instanceof Short) && !(var0 instanceof Byte)) {
         if (var0 instanceof String) {
            return Integer.parseInt((String)var0);
         } else {
            throw new MessageFormatException(JMSClientExceptionLogger.logConvertIntLoggable(var0.toString()).getMessage());
         }
      } else {
         return ((Number)var0).intValue();
      }
   }

   public static long toLong(Object var0) throws javax.jms.MessageFormatException {
      if (var0 == null) {
         throw new NumberFormatException(JMSClientExceptionLogger.logNullLongLoggable().getMessage());
      } else if (!(var0 instanceof Long) && !(var0 instanceof Integer) && !(var0 instanceof Short) && !(var0 instanceof Byte)) {
         if (var0 instanceof String) {
            return Long.parseLong((String)var0);
         } else {
            throw new MessageFormatException(JMSClientExceptionLogger.logConvertLongLoggable(var0.toString()).getMessage());
         }
      } else {
         return ((Number)var0).longValue();
      }
   }

   public static float toFloat(Object var0) throws javax.jms.MessageFormatException {
      if (var0 == null) {
         throw new NullPointerException(JMSClientExceptionLogger.logNullFloatLoggable().getMessage());
      } else if (var0 instanceof Float) {
         return ((Number)var0).floatValue();
      } else if (var0 instanceof String) {
         return Float.valueOf((String)var0);
      } else {
         throw new MessageFormatException(JMSClientExceptionLogger.logConvertFloatLoggable(var0.toString()).getMessage());
      }
   }

   public static double toDouble(Object var0) throws javax.jms.MessageFormatException {
      if (var0 == null) {
         throw new NullPointerException(JMSClientExceptionLogger.logNullDoubleLoggable().getMessage());
      } else if (!(var0 instanceof Double) && !(var0 instanceof Float)) {
         if (var0 instanceof String) {
            return Double.valueOf((String)var0);
         } else {
            throw new MessageFormatException(JMSClientExceptionLogger.logConvertDoubleLoggable(var0.toString()).getMessage());
         }
      } else {
         return ((Number)var0).doubleValue();
      }
   }

   public static String toString(Object var0) throws javax.jms.MessageFormatException {
      if (var0 == null) {
         return null;
      } else if (var0 instanceof byte[]) {
         throw new MessageFormatException(JMSClientExceptionLogger.logConvertByteArrayLoggable().getMessage());
      } else {
         return var0 == null ? null : var0.toString();
      }
   }

   public static byte[] toByteArray(Object var0) throws javax.jms.MessageFormatException {
      if (var0 == null) {
         return null;
      } else {
         try {
            return (byte[])((byte[])var0);
         } catch (ClassCastException var2) {
            throw new MessageFormatException(JMSClientExceptionLogger.logConvertToByteArrayLoggable(var0.toString()).getMessage());
         }
      }
   }
}
