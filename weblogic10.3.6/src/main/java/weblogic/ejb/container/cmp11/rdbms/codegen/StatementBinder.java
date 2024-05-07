package weblogic.ejb.container.cmp11.rdbms.codegen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import weblogic.utils.AssertionError;

public final class StatementBinder {
   public static String getStatementTypeNameForClass(Class var0) {
      if (!var0.isPrimitive()) {
         if (var0 == String.class) {
            return "String";
         } else if (var0 == Boolean.class) {
            return "Boolean";
         } else if (var0 == Byte.class) {
            return "Byte";
         } else if (var0 == Character.class) {
            return "String";
         } else if (var0 == Double.class) {
            return "Double";
         } else if (var0 == Float.class) {
            return "Float";
         } else if (var0 == Integer.class) {
            return "Int";
         } else if (var0 == Long.class) {
            return "Long";
         } else if (var0 == Short.class) {
            return "Short";
         } else if (var0 == Date.class) {
            return "Timestamp";
         } else if (var0 == java.sql.Date.class) {
            return "Date";
         } else if (var0 == Time.class) {
            return "Time";
         } else if (var0 == Timestamp.class) {
            return "Timestamp";
         } else if (var0 == BigDecimal.class) {
            return "BigDecimal";
         } else if (var0.isArray() && var0.getComponentType() == Byte.TYPE) {
            return "Bytes";
         } else {
            return Serializable.class.isAssignableFrom(var0) ? "Bytes" : "Object";
         }
      } else if (var0 == Boolean.TYPE) {
         return "Boolean";
      } else if (var0 == Byte.TYPE) {
         return "Byte";
      } else if (var0 == Character.TYPE) {
         return "String";
      } else if (var0 == Double.TYPE) {
         return "Double";
      } else if (var0 == Float.TYPE) {
         return "Float";
      } else if (var0 == Integer.TYPE) {
         return "Int";
      } else if (var0 == Long.TYPE) {
         return "Long";
      } else if (var0 == Short.TYPE) {
         return "Short";
      } else {
         throw new AssertionError("Missed a case.:" + var0);
      }
   }
}
