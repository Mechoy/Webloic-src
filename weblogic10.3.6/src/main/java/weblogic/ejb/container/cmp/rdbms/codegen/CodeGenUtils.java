package weblogic.ejb.container.cmp.rdbms.codegen;

import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.utils.MethodUtils;

public class CodeGenUtils {
   public static final String VAR_PREFIX = "__WL_";
   public static final String SUPER_PREFIX = "__WL_super_";
   public static final String INTERNAL_PREFIX = "__WL_internal_";

   public static Class getSnapshotClass(RDBMSBean var0, Class var1) {
      return !var0.isValidSQLType(var1) ? byte[].class : var1;
   }

   public static String snapshotNameForVar(String var0) {
      return "__WL_snapshot_" + var0;
   }

   public static String fieldVarName(String var0) {
      return "__WL_" + var0 + "_field_";
   }

   public static String fieldRemovedVarName(String var0) {
      return "__WL_removed_" + var0 + "_field_";
   }

   public static String cacheRelationshipMethodName(String var0) {
      return "__WL_cache" + fieldVarName(var0);
   }

   public static String getCMRFieldGetterMethodName(String var0) {
      return MethodUtils.getMethodName(var0);
   }

   public static String getCMRFieldFinderMethodName(RDBMSBean var0, String var1) {
      String var2 = var0.finderMethodName(var0.getCMPBeanDescriptor(), var1);
      return var0.isManyToManyRelation(var1) ? MethodUtils.convertToEjbSelectInternalName(var2, new Class[0]) : MethodUtils.convertToFinderName(var2);
   }
}
