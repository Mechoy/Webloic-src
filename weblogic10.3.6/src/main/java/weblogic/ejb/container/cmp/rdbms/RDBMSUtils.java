package weblogic.ejb.container.cmp.rdbms;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import weblogic.ejb.container.persistence.spi.CmrField;
import weblogic.ejb.container.persistence.spi.EjbRelationshipRole;
import weblogic.ejb.container.persistence.spi.RoleSource;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;

public final class RDBMSUtils extends weblogic.ejb.spi.RDBMSUtils {
   public static final String ONE = "One";
   public static final String MANY = "Many";
   public static final String EJB_CREATE = "ejbCreate";
   public static final String EJB_POST_CREATE = "ejbPostCreate";
   public static final String COMMIT = "commit";
   public static final String DEFAULT_GROUP_NAME = "defaultGroup";
   public static final short STATE_INVALID = -2;
   public static final short STATE_VALID = 0;
   public static final String[] validRdbmsCmp20JarPublicIds = new String[]{"-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB RDBMS Persistence//EN", "-//BEA Systems, Inc.//DTD WebLogic 7.0.0 EJB RDBMS Persistence//EN", "-//BEA Systems, Inc.//DTD WebLogic 8.1.0 EJB RDBMS Persistence//EN"};
   public static final String RDBMS_ERROR_BUNDLE = "weblogic.ejb.container.cmp.rdbms.compliance.RDBMSComplianceErrorBundle";
   public static final short GEN_KEY_PK_CLASS_INTEGER = 0;
   public static final short GEN_KEY_PK_CLASS_LONG = 1;
   public static final short IDENTITY = 1;
   public static final short SEQUENCE = 2;
   public static final short SEQUENCE_TABLE = 3;
   public static final short RELATIONSHIP_TYPE_NOT_APPLICABLE = -1;
   public static final short CMP_FIELD = 0;
   public static final short SINGLE_BEAN_NO_RELATION = 1;
   public static final short ONE_TO_ONE_RELATION_FK_ON_LHS = 2;
   public static final short ONE_TO_ONE_RELATION_FK_ON_RHS = 3;
   public static final short ONE_TO_MANY_RELATION = 4;
   public static final short MANY_TO_ONE_RELATION = 5;
   public static final short MANY_TO_MANY_RELATION = 6;
   public static final short REMOTE_RELATION_FK_ON_LHS = 7;
   public static final short REMOTE_RELATION_W_JOIN_TABLE = 8;
   private static final String CONTAINER_SEQUENCE_SUFFIX = "_WL";
   public static final String ACCESS_ORDER = "AccessOrder";
   public static final String VALUE_ORDER = "ValueOrder";

   public static String relationshipTypeToString(int var0) {
      switch (var0) {
         case -1:
            return "relationship type unknown or not applicable";
         case 0:
            return "cmp-field";
         case 1:
            return "single bean only, no relationship";
         case 2:
            return "one-to-one relationship, foreign key on left hand side";
         case 3:
            return "one-to-one relationship, foreign key on right hand side";
         case 4:
            return "one-to-many relationship";
         case 5:
            return "many-to-one relationship";
         case 6:
            return "many-to-many relationship";
         case 7:
            return "remote relationship foreign key on left hand side";
         case 8:
            return "remote relationship involving a join table";
         default:
            return "unknown relationship type";
      }
   }

   public static String getContainerSequenceName(String var0) {
      Debug.assertion(!isContainerSequenceName(var0), "called getContainerSequenceName on '" + var0 + "' which is already a containerSequenceName");
      return var0 + "_WL";
   }

   public static boolean isContainerSequenceName(String var0) {
      return var0.indexOf("_WL") != -1;
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

   public static String getCmrFieldName(EjbRelationshipRole var0, EjbRelationshipRole var1) {
      String var2 = null;
      CmrField var3 = var0.getCmrField();
      if (var3 != null) {
         var2 = var3.getName();
      } else {
         RoleSource var4 = var1.getRoleSource();
         String var5 = var4.getEjbName();
         CmrField var6 = var1.getCmrField();
         var2 = MethodUtils.decapitalize(ClassUtils.makeLegalName(var5)) + "_" + var6.getName();
      }

      return var2;
   }

   public static String escQuotedID(String var0) {
      assert var0 != null;

      StringBuffer var1 = new StringBuffer();
      char[] var2 = var0.toCharArray();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3] == '"') {
            var1.append('\\');
         }

         var1.append(var2[var3]);
      }

      return var1.toString();
   }

   public static String head(String var0) {
      int var1 = var0.lastIndexOf(".");
      return var1 == -1 ? "" : var0.substring(0, var1);
   }

   public static String tail(String var0) {
      int var1 = var0.lastIndexOf(".");
      return var0.substring(var1 + 1);
   }

   public static String sqlTypeToString(int var0) {
      switch (var0) {
         case -7:
            return "java.sql.Types.BIT";
         case -6:
            return "java.sql.Types.TINYINT";
         case -5:
            return "java.sql.Types.BIGINT";
         case -4:
            return "java.sql.Types.LONGVARBINARY";
         case -3:
            return "java.sql.Types.VARBINARY";
         case -2:
            return "java.sql.Types.BINARY";
         case -1:
            return "java.sql.Types.LONGVARCHAR";
         case 0:
            return "java.sql.Types.NULL";
         case 1:
            return "java.sql.Types.CHAR";
         case 2:
            return "java.sql.Types.NUMERIC";
         case 3:
            return "java.sql.Types.DECIMAL";
         case 4:
            return "java.sql.Types.INTEGER";
         case 5:
            return "java.sql.Types.SMALLINT";
         case 6:
            return "java.sql.Types.FLOAT";
         case 7:
            return "java.sql.Types.REAL";
         case 8:
            return "java.sql.Types.DOUBLE";
         case 12:
            return "java.sql.Types.VARCHAR";
         case 91:
            return "java.sql.Types.DATE";
         case 92:
            return "java.sql.Types.TIME";
         case 93:
            return "java.sql.Types.TIMESTAMP";
         case 1111:
            return "java.sql.Types.OTHER";
         case 2000:
            return "java.sql.Types.JAVA_OBJECT";
         case 2001:
            return "java.sql.Types.DISTINCT";
         case 2002:
            return "java.sql.Types.STRUCT";
         case 2003:
            return "java.sql.Types.ARRAY";
         case 2004:
            return "java.sql.Types.BLOB";
         case 2005:
            return "java.sql.Types.CLOB";
         case 2006:
            return "java.sql.Types.REF";
         default:
            return "Invalid SQL type: " + var0;
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

   public static boolean dbSupportForSingleLeftOuterJoin(int var0) {
      return dbSupportForSingleLeftOuterJoinANSI(var0) || dbSupportForSingleLeftOuterJoinInWhereClause(var0);
   }

   public static boolean dbSupportForMultiLeftOuterJoin(int var0) {
      return dbSupportForMultiLeftOuterJoinANSI(var0) || dbSupportForMultiLeftOuterJoinInWhereClause(var0);
   }

   public static boolean dbSupportForSingleLeftOuterJoinANSI(int var0) {
      switch (var0) {
         case 4:
         case 6:
         case 7:
         case 8:
         case 9:
            return true;
         case 5:
         default:
            return false;
      }
   }

   public static boolean dbSupportForMultiLeftOuterJoinANSI(int var0) {
      switch (var0) {
         case 4:
         case 7:
         case 8:
         case 9:
            return true;
         case 5:
         case 6:
         default:
            return false;
      }
   }

   public static boolean dbSupportForSingleLeftOuterJoinInWhereClause(int var0) {
      switch (var0) {
         case 0:
         case 4:
         case 7:
         case 8:
         default:
            return false;
         case 1:
            return true;
         case 2:
         case 5:
            return true;
         case 3:
            return false;
         case 6:
            return false;
      }
   }

   public static boolean dbSupportForMultiLeftOuterJoinInWhereClause(int var0) {
      switch (var0) {
         case 0:
         case 4:
         case 7:
         case 8:
         default:
            return false;
         case 1:
            return true;
         case 2:
         case 5:
            return false;
         case 3:
            return false;
         case 6:
            return false;
      }
   }

   public static String getFROMClauseSelectForUpdate(int var0, int var1) {
      switch (var0) {
         case 0:
         case 1:
         case 3:
         case 4:
         case 6:
         case 8:
         case 9:
            return "";
         case 2:
         case 7:
            if (var1 == 1) {
               return " WITH(UPDLOCK) ";
            }

            if (var1 == 2) {
               return "";
            }

            if (var1 == 0) {
               return "";
            }
            break;
         case 5:
            if (var1 == 1) {
               return " HOLDLOCK ";
            }

            if (var1 == 2) {
               return "";
            }

            if (var1 == 0) {
               return "";
            }
            break;
         default:
            Debug.assertion(false, "Undefined database type " + var0);
      }

      return "";
   }

   public static void printEnvironment() {
      try {
         InitialContext var0 = new InitialContext();
         NamingEnumeration var1 = var0.list("java:comp/env");
         Debug.say("java:comp/env----------------------------");
         if (!var1.hasMore()) {
            Debug.say("Context is empty.");
         }

         while(var1.hasMore()) {
            NameClassPair var2 = (NameClassPair)var1.next();
            Debug.say("name- " + var2.getName() + ", " + "class- " + var2.getClassName());
         }

         var1.close();
      } catch (Exception var3) {
         Debug.say("Error while printing Environment:");
         var3.printStackTrace();
      }

   }

   public static short getGenKeyTypeAsConstant(String var0) {
      if (var0.equalsIgnoreCase("Identity")) {
         return 1;
      } else if (var0.equalsIgnoreCase("Sequence")) {
         return 2;
      } else {
         return (short)(var0.equalsIgnoreCase("SequenceTable") ? 3 : -1);
      }
   }

   public static boolean isOracleNLSDataType(RDBMSBean var0, String var1, Map var2) {
      if (var2 != null && !var2.isEmpty() && var1.indexOf(46) != -1) {
         int var3 = var1.indexOf(46);
         String var4 = (String)var2.get(var1.substring(0, var3));
         if (var4 != null) {
            Map var5 = var0.getRdbmsBeanMap();
            if (var5 != null && !var5.isEmpty()) {
               Iterator var6 = var5.values().iterator();
               RDBMSBean var7 = null;

               while(var6.hasNext()) {
                  RDBMSBean var8 = (RDBMSBean)var6.next();
                  if (var4.equals(var8.getAbstractSchemaName())) {
                     var7 = var8;
                     break;
                  }
               }

               if (var7 != null) {
                  return isOracleNLSDataType(var7, var1.substring(var3 + 1));
               }
            }
         }

         return isOracleNLSDataType(var0, var1);
      } else {
         return isOracleNLSDataType(var0, var1);
      }
   }

   public static boolean isOracleNLSDataType(RDBMSBean var0, String var1) {
      if (var0.getDatabaseType() != 1) {
         return false;
      } else {
         String var2 = var0.getField(var1);
         if (!var1.equals(var2)) {
            String var3 = null;
            if (var2 == null) {
               int var4 = var1.indexOf(".");
               if (var4 != -1) {
                  var2 = var1.substring(0, var4);
                  var3 = var1.substring(var4 + 1);
               }
            }

            int var5;
            for(RDBMSBean var6 = var0; var2 != null && var6.getCmrFieldNames().contains(var2); var3 = var3.substring(var5 + 1)) {
               var6 = var6.getRelatedRDBMSBean(var2);
               if (var3 == null) {
                  String var7 = var0.getCmpColumnForVariable(var1);
                  if (var7 != null) {
                     var3 = var0.getRelatedPkFieldName(var2, var7);
                     return "NCHAR".equalsIgnoreCase(var6.getCmpColumnTypeForField(var3)) || "NVARCHAR2".equalsIgnoreCase(var6.getCmpColumnTypeForField(var3)) || "NCLOB".equalsIgnoreCase(var6.getCmpColumnTypeForField(var3));
                  }
                  break;
               }

               var5 = var3.indexOf(".");
               if (var5 == -1) {
                  return "NCHAR".equalsIgnoreCase(var6.getCmpColumnTypeForField(var3)) || "NVARCHAR2".equalsIgnoreCase(var6.getCmpColumnTypeForField(var3)) || "NCLOB".equalsIgnoreCase(var6.getCmpColumnTypeForField(var3));
               }

               var2 = var3.substring(0, var5);
            }
         }

         if (!"NCHAR".equalsIgnoreCase(var0.getCmpColumnTypeForField(var1)) && !"NVARCHAR2".equalsIgnoreCase(var0.getCmpColumnTypeForField(var1)) && !"NCLOB".equalsIgnoreCase(var0.getCmpColumnTypeForField(var1))) {
            return false;
         } else {
            return true;
         }
      }
   }
}
