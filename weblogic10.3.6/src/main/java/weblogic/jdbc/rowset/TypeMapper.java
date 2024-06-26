package weblogic.jdbc.rowset;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import weblogic.utils.AssertionError;
import weblogic.utils.StackTraceUtils;
import weblogic.xml.schema.types.XSDBase64Binary;
import weblogic.xml.schema.types.XSDBoolean;
import weblogic.xml.schema.types.XSDDateTime;
import weblogic.xml.schema.types.XSDDouble;
import weblogic.xml.schema.types.XSDFloat;
import weblogic.xml.schema.types.XSDInteger;
import weblogic.xml.stream.XMLName;

public final class TypeMapper implements XMLSchemaConstants {
   private static final Map JDBC2XSD = new HashMap();
   private static final Map CONVMAP = new HashMap();
   private static final Map XSD2JDBC = new HashMap();
   private static final Map JDBC_STRING2INT = new HashMap();
   private static Converter defaultConvert = new Converter() {
      public String toXML(Object var1) {
         return var1.toString();
      }

      public Object toJava(String var1) {
         return var1;
      }
   };
   private static Converter boolConverter = new Converter() {
      public String toXML(Object var1) {
         return var1.toString();
      }

      public Object toJava(String var1) {
         return new Boolean(XSDBoolean.convertXml(var1));
      }
   };
   private static Converter doubleConverter = new Converter() {
      public String toXML(Object var1) {
         return var1.toString();
      }

      public Object toJava(String var1) {
         return new Double(XSDDouble.convertXml(var1));
      }
   };
   private static Converter floatConverter = new Converter() {
      public String toXML(Object var1) {
         return var1.toString();
      }

      public Object toJava(String var1) {
         return new Float(XSDFloat.convertXml(var1));
      }
   };
   private static Converter numericConverter = new Converter() {
      public String toXML(Object var1) {
         return var1.toString();
      }

      public Object toJava(String var1) {
         return new BigDecimal(var1);
      }
   };
   private static Converter integerConverter = new Converter() {
      public String toXML(Object var1) {
         return var1.toString();
      }

      public Object toJava(String var1) {
         return new BigDecimal(XSDInteger.convertXml(var1));
      }
   };
   private static Converter base64Converter = new Converter() {
      public String toXML(Object var1) throws IOException, SQLException {
         if (var1 instanceof byte[]) {
            return XSDBase64Binary.getXml((byte[])((byte[])var1));
         } else {
            throw new IOException("Cannot convert Java class " + var1.getClass().getName() + " to a Base64Binary");
         }
      }

      public Object toJava(String var1) {
         return XSDBase64Binary.convertXml(var1);
      }
   };
   private static Converter datetimeConverter = new Converter() {
      public String toXML(Object var1) throws IOException {
         if (var1 instanceof Calendar) {
            return XSDDateTime.getXml((Calendar)var1);
         } else if (var1 instanceof Date) {
            Calendar var2 = Calendar.getInstance();
            var2.setTime((Date)var1);
            return XSDDateTime.getXml(var2);
         } else {
            throw new IOException("Cannot convert Java class " + var1.getClass().getName() + " to a DateTime");
         }
      }

      public Object toJava(String var1) {
         return XSDDateTime.convertXml(var1);
      }
   };
   private static Converter blobConverter = new Converter() {
      public String toXML(Object var1) throws IOException {
         if (var1 instanceof Blob) {
            try {
               byte[] var2 = ((Blob)var1).getBytes(0L, (int)((Blob)var1).length());
               return XSDBase64Binary.getXml(var2);
            } catch (SQLException var3) {
               throw new IOException(StackTraceUtils.throwable2StackTrace(var3));
            }
         } else {
            throw new IOException("Cannot convert Java class " + var1.getClass().getName() + " to a base64");
         }
      }

      public Object toJava(String var1) {
         return new RowSetBlob(XSDBase64Binary.convertXml(var1));
      }
   };
   private static Converter clobConverter = new Converter() {
      public String toXML(Object var1) throws IOException {
         if (var1 instanceof Clob) {
            Clob var2 = (Clob)var1;

            try {
               return var2.getSubString(0L, (int)var2.length());
            } catch (SQLException var4) {
               throw new IOException(StackTraceUtils.throwable2StackTrace(var4));
            }
         } else if (var1 instanceof String) {
            return (String)var1;
         } else {
            throw new IOException("Cannot convert Java class " + var1.getClass().getName() + " to a String");
         }
      }

      public Object toJava(String var1) {
         return new RowSetClob(var1);
      }
   };

   public static XMLName getXSDType(int var0) throws IOException {
      XMLName var1 = (XMLName)JDBC2XSD.get(new Integer(var0));
      if (var1 == null) {
         throw new IOException("No XML Schema type mapping found for JDBC type: " + var0);
      } else {
         return var1;
      }
   }

   public static String getXMLValue(int var0, Object var1) throws IOException, SQLException {
      Converter var2 = (Converter)CONVMAP.get(new Integer(var0));
      if (var2 == null) {
         throw new IOException("No XML Schema type mapping found for JDBC type: " + var0);
      } else {
         return var2.toXML(var1);
      }
   }

   public static Object getJavaValue(int var0, String var1) throws IOException, SQLException {
      Converter var2 = (Converter)CONVMAP.get(new Integer(var0));
      if (var2 == null) {
         throw new IOException("No XML Schema type mapping found for JDBC type: " + var0);
      } else {
         return var2.toJava(var1);
      }
   }

   private static void initJava2Xsd() {
      JDBC2XSD.put(new Integer(16), XSD_BOOLEAN);
      CONVMAP.put(new Integer(16), boolConverter);
      JDBC2XSD.put(new Integer(-7), XSD_BOOLEAN);
      CONVMAP.put(new Integer(-7), boolConverter);
      JDBC2XSD.put(new Integer(-6), XSD_BYTE);
      CONVMAP.put(new Integer(-6), defaultConvert);
      JDBC2XSD.put(new Integer(5), XSD_SHORT);
      CONVMAP.put(new Integer(5), defaultConvert);
      JDBC2XSD.put(new Integer(4), XSD_INT);
      CONVMAP.put(new Integer(4), defaultConvert);
      JDBC2XSD.put(new Integer(-5), XSD_LONG);
      CONVMAP.put(new Integer(-5), defaultConvert);
      JDBC2XSD.put(new Integer(6), XSD_FLOAT);
      CONVMAP.put(new Integer(6), floatConverter);
      JDBC2XSD.put(new Integer(7), XSD_DOUBLE);
      CONVMAP.put(new Integer(7), doubleConverter);
      JDBC2XSD.put(new Integer(8), XSD_DOUBLE);
      CONVMAP.put(new Integer(8), doubleConverter);
      JDBC2XSD.put(new Integer(2), XSD_DECIMAL);
      CONVMAP.put(new Integer(2), numericConverter);
      JDBC2XSD.put(new Integer(3), XSD_DECIMAL);
      CONVMAP.put(new Integer(3), numericConverter);
      JDBC2XSD.put(new Integer(1), XSD_STRING);
      CONVMAP.put(new Integer(1), defaultConvert);
      JDBC2XSD.put(new Integer(12), XSD_STRING);
      CONVMAP.put(new Integer(12), defaultConvert);
      JDBC2XSD.put(new Integer(-1), XSD_STRING);
      CONVMAP.put(new Integer(-1), defaultConvert);
      JDBC2XSD.put(new Integer(91), XSD_DATETIME);
      CONVMAP.put(new Integer(91), datetimeConverter);
      JDBC2XSD.put(new Integer(92), XSD_DATETIME);
      CONVMAP.put(new Integer(92), datetimeConverter);
      JDBC2XSD.put(new Integer(93), XSD_DATETIME);
      CONVMAP.put(new Integer(93), datetimeConverter);
      JDBC2XSD.put(new Integer(-102), XSD_DATETIME);
      CONVMAP.put(new Integer(-102), datetimeConverter);
      JDBC2XSD.put(new Integer(-2), XSD_BASE64BINARY);
      CONVMAP.put(new Integer(-2), base64Converter);
      JDBC2XSD.put(new Integer(-3), XSD_BASE64BINARY);
      CONVMAP.put(new Integer(-3), base64Converter);
      JDBC2XSD.put(new Integer(-4), XSD_BASE64BINARY);
      CONVMAP.put(new Integer(-4), base64Converter);
      JDBC2XSD.put(new Integer(2004), XSD_BASE64BINARY);
      CONVMAP.put(new Integer(2004), blobConverter);
      JDBC2XSD.put(new Integer(2005), XSD_STRING);
      CONVMAP.put(new Integer(2005), clobConverter);
   }

   private static void initXsd2Java() {
      XSD2JDBC.put("string", new Integer(12));
      XSD2JDBC.put("integer", new Integer(2));
      XSD2JDBC.put("int", new Integer(4));
      XSD2JDBC.put("long", new Integer(-5));
      XSD2JDBC.put("short", new Integer(5));
      XSD2JDBC.put("decimal", new Integer(3));
      XSD2JDBC.put("float", new Integer(6));
      XSD2JDBC.put("double", new Integer(8));
      XSD2JDBC.put("boolean", new Integer(16));
      XSD2JDBC.put("byte", new Integer(5));
      XSD2JDBC.put("dateTime", new Integer(91));
      XSD2JDBC.put("base64Binary", new Integer(-2));
      XSD2JDBC.put("hexBinary", new Integer(-2));
   }

   private static String removePrefix(String var0) {
      int var1 = var0.indexOf(58);
      return var1 == -1 ? var0 : var0.substring(var1 + 1);
   }

   static int getDbType(String var0) throws IOException {
      Integer var1 = (Integer)XSD2JDBC.get(removePrefix(var0));
      if (var1 == null) {
         throw new IOException("Type: " + var0 + " has no corresponding JDBC type");
      } else {
         return var1;
      }
   }

   static int getJDBCTypeFromString(String var0) throws IOException {
      Integer var1 = (Integer)JDBC_STRING2INT.get(var0);
      if (var1 == null) {
         throw new IOException("Unexpected JDBC type: " + var0);
      } else {
         return var1;
      }
   }

   static String getJDBCTypeAsString(int var0) throws IOException {
      switch (var0) {
         case -102:
            return "TIMESTAMP WITH TIME ZONE";
         case -7:
            return "BIT";
         case -6:
            return "TINYINT";
         case -5:
            return "BIGINT";
         case -4:
            return "LONGVARBINARY";
         case -3:
            return "VARBINARY";
         case -2:
            return "BINARY";
         case -1:
            return "LONGVARCHAR";
         case 0:
            return "NULL";
         case 1:
            return "CHAR";
         case 2:
            return "NUMERIC";
         case 3:
            return "DECIMAL";
         case 4:
            return "INTEGER";
         case 5:
            return "SMALLINT";
         case 6:
            return "FLOAT";
         case 7:
            return "REAL";
         case 8:
            return "DOUBLE";
         case 12:
            return "VARCHAR";
         case 16:
            return "BOOLEAN";
         case 70:
            return "DATALINK";
         case 91:
            return "DATE";
         case 92:
            return "TIME";
         case 93:
            return "TIMESTAMP";
         case 1111:
            return "OTHER";
         case 2000:
            return "JAVA_OBJECT";
         case 2001:
            return "DISTINCT";
         case 2002:
            return "STRUCT";
         case 2003:
            return "ARRAY";
         case 2004:
            return "BLOB";
         case 2005:
            return "CLOB";
         case 2006:
            return "REF";
         default:
            throw new AssertionError("Unexpected type: " + var0);
      }
   }

   private static final void initJDBC2String() {
      JDBC_STRING2INT.put("ARRAY", new Integer(2003));
      JDBC_STRING2INT.put("BIGINT", new Integer(-5));
      JDBC_STRING2INT.put("BINARY", new Integer(-2));
      JDBC_STRING2INT.put("BIT", new Integer(-7));
      JDBC_STRING2INT.put("BLOB", new Integer(2004));
      JDBC_STRING2INT.put("BOOLEAN", new Integer(16));
      JDBC_STRING2INT.put("CHAR", new Integer(1));
      JDBC_STRING2INT.put("CLOB", new Integer(2005));
      JDBC_STRING2INT.put("DATALINK", new Integer(70));
      JDBC_STRING2INT.put("DATE", new Integer(91));
      JDBC_STRING2INT.put("DECIMAL", new Integer(3));
      JDBC_STRING2INT.put("DISTINCT", new Integer(2001));
      JDBC_STRING2INT.put("DOUBLE", new Integer(8));
      JDBC_STRING2INT.put("FLOAT", new Integer(6));
      JDBC_STRING2INT.put("INTEGER", new Integer(4));
      JDBC_STRING2INT.put("JAVA_OBJECT", new Integer(2000));
      JDBC_STRING2INT.put("LONGVARBINARY", new Integer(-4));
      JDBC_STRING2INT.put("LONGVARCHAR", new Integer(-1));
      JDBC_STRING2INT.put("NULL", new Integer(0));
      JDBC_STRING2INT.put("NUMERIC", new Integer(2));
      JDBC_STRING2INT.put("OTHER", new Integer(1111));
      JDBC_STRING2INT.put("REAL", new Integer(7));
      JDBC_STRING2INT.put("REF", new Integer(2006));
      JDBC_STRING2INT.put("SMALLINT", new Integer(5));
      JDBC_STRING2INT.put("STRUCT", new Integer(2002));
      JDBC_STRING2INT.put("TIME", new Integer(92));
      JDBC_STRING2INT.put("TIMESTAMP", new Integer(93));
      JDBC_STRING2INT.put("TIMESTAMP WITH TIME ZONE", new Integer(-102));
      JDBC_STRING2INT.put("TINYINT", new Integer(-6));
      JDBC_STRING2INT.put("VARBINARY", new Integer(-3));
      JDBC_STRING2INT.put("VARCHAR", new Integer(12));
   }

   static {
      initXsd2Java();
      initJava2Xsd();
      initJDBC2String();
   }

   interface Converter {
      String toXML(Object var1) throws IOException, SQLException;

      Object toJava(String var1) throws IOException;
   }
}
