package weblogic.ejb.container.cmp11.rdbms;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import weblogic.utils.AssertionError;
import weblogic.xml.jaxp.WebLogicDocumentBuilderFactory;

public final class RDBMSUtils extends weblogic.ejb.spi.RDBMSUtils {
   public static final String PARSER_NAME = "com.sun.xml.parser.ValidatingParser";
   public static final String WEBLOGIC_RDBMS_BEAN = "weblogic-rdbms-bean";
   public static final String POOL_NAME = "pool-name";
   public static final String SCHEMA_NAME = "schema-name";
   public static final String TABLE_NAME = "table-name";
   public static final String ATTRIBUTE_MAP = "attribute-map";
   public static final String FINDER_LIST = "finder-list";
   public static final String OBJECT_LINK = "object-link";
   public static final String BEAN_FIELD = "bean-field";
   public static final String DBMS_COLUMN = "dbms-column";
   public static final String FINDER = "finder";
   public static final String METHOD_NAME = "method-name";
   public static final String METHOD_PARAMS = "method-params";
   public static final String METHOD_PARAM = "method-param";
   public static final String FINDER_QUERY = "finder-query";
   public static final String FINDER_EXPRESSION = "finder-expression";
   public static final String EXPRESSION_NUMBER = "expression-number";
   public static final String EXPRESSION_TEXT = "expression-text";
   public static final String EXPRESSION_TYPE = "expression-type";
   public static final String FINDER_OPTIONS = "finder-options";
   public static final String FIND_FOR_UPDATE = "find-for-update";
   public static final String OPTIONS = "options";
   public static final String USE_QUOTED_NAMES = "use-quoted-names";
   public static final String TRANSACTION_ISOLATION = "transaction-isolation";
   public static final String TRANSACTION_READ_UNCOMMITTED = "TRANSACTION_READ_UNCOMMITTED";
   public static final String TRANSACTION_READ_COMMITTED = "TRANSACTION_READ_COMMITTED";
   public static final String TRANSACTION_REPEATABLE_READ = "TRANSACTION_REPEATABLE_READ";
   public static final String TRANSACTION_SERIALIZABLE = "TRANSACTION_SERIALIZABLE";
   public static final String TRANSACTION_NONE = "TRANSACTION_NONE";
   public static final String TRANSACTION_ISOLATION_UNKNOWN = "TRANSACTION_ISOLATION_UNKNOWN";
   public static final String[] validRdbmsCmp11JarPublicIds = new String[]{"-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB RDBMS Persistence//EN", "-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB 1.1 RDBMS Persistence//EN"};

   public static DocumentBuilder getDocumentBuilder() {
      try {
         DocumentBuilder var0 = (new WebLogicDocumentBuilderFactory()).newDocumentBuilder();
         var0.setEntityResolver(new BeanEntityResolver());
         return var0;
      } catch (ParserConfigurationException var1) {
         throw new AssertionError(var1);
      }
   }

   public static String isolationLevelToString(Integer var0) {
      if (var0 == null) {
         return "(default)";
      } else {
         switch (var0) {
            case 0:
               return "TRANSACTION_NONE";
            case 1:
               return "TRANSACTION_READ_UNCOMMITTED";
            case 2:
               return "TRANSACTION_READ_COMMITTED";
            case 3:
            case 5:
            case 6:
            case 7:
            default:
               return "TRANSACTION_ISOLATION_UNKNOWN";
            case 4:
               return "TRANSACTION_REPEATABLE_READ";
            case 8:
               return "TRANSACTION_SERIALIZABLE";
         }
      }
   }

   public static String selectForUpdateToString(int var0) {
      switch (var0) {
         case 0:
            return "";
         case 1:
            return " FOR UPDATE ";
         case 2:
            return " FOR UPDATE NOWAIT ";
         default:
            throw new AssertionError("Unknown selectForUpdate type: '" + var0 + "'");
      }
   }

   public static String throwable2StackTrace(Throwable var0) {
      if (var0 == null) {
         var0 = new Throwable("[Null exception passed, creating stack trace for offending caller]");
      }

      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      var0.printStackTrace(new PrintStream(var1));
      return var1.toString();
   }

   public static String setterMethodName(String var0) {
      return "set" + var0.substring(0, 1).toUpperCase(Locale.ENGLISH) + var0.substring(1);
   }

   public static String getterMethodName(String var0) {
      return "get" + var0.substring(0, 1).toUpperCase(Locale.ENGLISH) + var0.substring(1);
   }
}
