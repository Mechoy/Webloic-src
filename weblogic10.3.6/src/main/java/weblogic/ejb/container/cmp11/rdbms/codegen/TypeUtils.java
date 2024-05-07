package weblogic.ejb.container.cmp11.rdbms.codegen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.utils.AssertionError;

public final class TypeUtils {
   private static byte[] byteArray = new byte[1];

   public static int getSQLTypeForClass(Class var0) throws EJBCException {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return -2;
         }

         if (var0 == Byte.TYPE) {
            return 4;
         }

         if (var0 == Character.TYPE) {
            return 1;
         }

         if (var0 == Double.TYPE) {
            return 8;
         }

         if (var0 == Float.TYPE) {
            return 6;
         }

         if (var0 == Integer.TYPE) {
            return 4;
         }

         if (var0 == Long.TYPE) {
            return 4;
         }

         if (var0 == Short.TYPE) {
            return 4;
         }
      } else {
         if (var0 == String.class) {
            return 12;
         }

         if (var0 == BigDecimal.class) {
            return 2;
         }

         if (var0 == Boolean.class) {
            return -2;
         }

         if (var0 == Byte.class) {
            return 4;
         }

         if (var0 == Character.class) {
            return 1;
         }

         if (var0 == Double.class) {
            return 8;
         }

         if (var0 == Float.class) {
            return 6;
         }

         if (var0 == Integer.class) {
            return 4;
         }

         if (var0 == Long.class) {
            return 4;
         }

         if (var0 == Short.class) {
            return 4;
         }

         if (var0 == Date.class) {
            return 91;
         }

         if (var0 == java.sql.Date.class) {
            return 91;
         }

         if (var0 == Time.class) {
            return 92;
         }

         if (var0 == Timestamp.class) {
            return 93;
         }

         if (var0 == byteArray.getClass()) {
            return -3;
         }

         if (Serializable.class.isAssignableFrom(var0)) {
            return -3;
         }
      }

      throw new EJBCException("CMP11 Could not handle a SQL type in TypeUtils.getSQLTypeForClass:  type = " + var0);
   }

   public static boolean isValidSQLType(Class var0) {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
            return true;
         }

         if (var0 == Byte.TYPE) {
            return true;
         }

         if (var0 == Character.TYPE) {
            return true;
         }

         if (var0 == Double.TYPE) {
            return true;
         }

         if (var0 == Float.TYPE) {
            return true;
         }

         if (var0 == Integer.TYPE) {
            return true;
         }

         if (var0 == Long.TYPE) {
            return true;
         }

         if (var0 == Short.TYPE) {
            return true;
         }
      } else {
         if (var0 == String.class) {
            return true;
         }

         if (var0 == Boolean.class) {
            return true;
         }

         if (var0 == Byte.class) {
            return true;
         }

         if (var0 == Character.class) {
            return true;
         }

         if (var0 == Double.class) {
            return true;
         }

         if (var0 == Float.class) {
            return true;
         }

         if (var0 == Integer.class) {
            return true;
         }

         if (var0 == Long.class) {
            return true;
         }

         if (var0 == Short.class) {
            return true;
         }

         if (var0 == Date.class) {
            return true;
         }

         if (var0 == java.sql.Date.class) {
            return true;
         }

         if (var0 == Time.class) {
            return true;
         }

         if (var0 == Timestamp.class) {
            return true;
         }

         if (var0 == BigDecimal.class) {
            return true;
         }

         if (var0 == byteArray.getClass()) {
            return true;
         }
      }

      return false;
   }

   public static String getResultSetMethodPostfix(Class var0) {
      return getMethodPostfix(var0);
   }

   public static String getPreparedStatementMethodPostfix(Class var0) {
      return getMethodPostfix(var0);
   }

   public static String getMethodPostfix(Class var0) {
      if (var0.isPrimitive()) {
         if (var0 == Boolean.TYPE) {
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
            throw new AssertionError("Didn't handle a potential SQL type case in TypeUtils.getMethodPostfix: type = " + var0);
         }
      } else if (var0 == String.class) {
         return "String";
      } else if (var0 == BigDecimal.class) {
         return "BigDecimal";
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
      } else {
         return var0.isArray() && var0.getComponentType() == Byte.TYPE ? "Bytes" : "Bytes";
      }
   }
}
