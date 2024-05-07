package weblogic.diagnostics.watch;

import weblogic.diagnostics.i18n.DiagnosticsTextWatchTextFormatter;

public final class ConsoleUtils {
   private static final String[] RELATIONAL_OPERATORS = new String[]{"<", "<=", ">", ">=", "=", "!=", "IN", "LIKE", "MATCHES"};
   private static final String[] LOGICAL_OPERATORS = new String[]{"AND", "OR", "NOT", "("};
   private static final String ATTR_SEPARATOR = "//";
   private static final String BEGIN_ENCLOSER = "${";
   private static final String END_ENCLOSER = "}";
   private static final String TYPE_BEGIN_DELIMITER = "[";
   private static final String TYPE_END_DELIMITER = "]";

   public static String[] getRelationalOperators() {
      return RELATIONAL_OPERATORS;
   }

   public static String[] getLogicalOperators() {
      return LOGICAL_OPERATORS;
   }

   public static String buildWatchVariableExpression(String var0, String var1, String var2) {
      return buildWatchVariableExpression("ServerRuntime", var0, var1, var2);
   }

   public static String buildWatchVariableExpression(String var0, String var1, String var2, String var3) {
      String var4 = var2 == null ? null : var2.trim();
      String var5 = var1 == null ? null : var1.trim();
      String var6 = var3 == null ? null : var3.trim();
      if (var6 != null && var6.length() != 0) {
         if (var4 != null && var4.length() != 0 || var5 != null && var5.length() != 0) {
            StringBuffer var7 = new StringBuffer();
            var7.append("${");
            if (var0 != null && var0.trim().length() > 0) {
               var7.append(var0.trim());
               var7.append("//");
            }

            if (var5 != null && var5.length() > 0) {
               var7.append("[");
               var7.append(var5);
               var7.append("]");
            }

            if (var4 != null && var4.length() > 0) {
               var7.append(var4);
            }

            var7.append("//");
            var7.append(var6);
            var7.append("}");
            return var7.toString();
         } else {
            throw new IllegalArgumentException(DiagnosticsTextWatchTextFormatter.getInstance().getIncompleteWatchVariableConsoleText());
         }
      } else {
         throw new IllegalArgumentException(DiagnosticsTextWatchTextFormatter.getInstance().getEmptyWatchAttributeConsoleText());
      }
   }
}
