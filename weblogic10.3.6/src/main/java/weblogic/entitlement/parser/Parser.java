package weblogic.entitlement.parser;

import weblogic.entitlement.expression.Difference;
import weblogic.entitlement.expression.EExprRep;
import weblogic.entitlement.expression.EExpression;
import weblogic.entitlement.expression.Empty;
import weblogic.entitlement.expression.GroupIdentifier;
import weblogic.entitlement.expression.GroupList;
import weblogic.entitlement.expression.Intersection;
import weblogic.entitlement.expression.InvalidPredicateClassException;
import weblogic.entitlement.expression.Inverse;
import weblogic.entitlement.expression.PredicateOp;
import weblogic.entitlement.expression.RoleIdentifier;
import weblogic.entitlement.expression.RoleList;
import weblogic.entitlement.expression.Union;
import weblogic.entitlement.expression.UserIdentifier;
import weblogic.entitlement.expression.UserList;
import weblogic.entitlement.util.Escaping;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;

public class Parser {
   public static final char UNION = '|';
   public static final char INTERSECT = '&';
   public static final char DIFFERENCE = '-';
   public static final char COMPLEMENT = '~';
   public static final char PREDICATE = '?';
   public static final String USR_LIST = "Usr";
   public static final String GROUP_LIST = "Grp";
   public static final String ROLE_LIST = "Rol";
   public static final char LIST_START = '(';
   public static final char LIST_END = ')';
   public static final char LIST_SEPARATOR = ',';
   public static final char SUB_EEXPR_OPEN = '{';
   public static final char SUB_EEXPR_CLOSE = '}';
   public static final char SPACE = ' ';
   public static final char TAB = '\t';
   public static final char NEW_LINE = '\n';
   public static final char USR_IDENTIFIER = 'u';
   public static final char GRP_IDENTIFIER = 'g';
   public static final char ROL_IDENTIFIER = 'r';
   public static final char PRE_IDENTIFIER = 'p';
   public static final char EMP_IDENTIFIER = 'e';
   public static final char USER_UNION = 'U';
   public static final char GROUP_UNION = 'G';
   public static final char ROLE_UNION = 'R';
   public static final String UNCHECKED_EXPR = "{ ?weblogic.entitlement.rules.UncheckedPolicy() }";
   public static final String EXCLUDED_EXPR = "{ ?weblogic.entitlement.rules.ExcludedPolicy() }";
   private static final Escaping escaper = new Escaping(new char[]{'#', '|', '&', '-', '~', '(', ')', ',', '{', '}', ' ', '\t', '?', '\n'});

   public static EExpression parseResourceExpression(String var0) throws ParseException, IllegalPredicateArgumentException, InvalidPredicateClassException {
      char[] var1 = var0.toCharArray();
      return parse(var1, new int[]{0}, new int[]{var1.length}, 0, true);
   }

   public static EExpression parseRoleExpression(String var0) throws ParseException, IllegalPredicateArgumentException, InvalidPredicateClassException {
      char[] var1 = var0.toCharArray();
      return parse(var1, new int[]{0}, new int[]{var1.length}, 0, false);
   }

   public static String uncheckedExpr() {
      return "{ ?weblogic.entitlement.rules.UncheckedPolicy() }";
   }

   public static String excludedExpr() {
      return "{ ?weblogic.entitlement.rules.ExcludedPolicy() }";
   }

   public static String roles2Expr(String[] var0) {
      return toExpr(var0, "Rol");
   }

   public static String users2Expr(String[] var0) {
      return toExpr(var0, "Usr");
   }

   public static String groups2Expr(String[] var0) {
      return toExpr(var0, "Grp");
   }

   public static String escape(String var0) {
      return escaper.escapeString(var0);
   }

   public static String unescape(String var0) {
      return escaper.unescapeString(var0);
   }

   private static String toExpr(String[] var0, String var1) {
      if (var0.length == 0) {
         return "";
      } else {
         int var2 = 0;

         for(int var3 = 0; var3 < var0.length; ++var3) {
            var2 += var0[var3].length();
         }

         StringBuffer var5 = new StringBuffer(var2 * 2);
         var5.append('{');
         var5.append(var1);
         var5.append('(');
         var5.append(escaper.escapeString(var0[0]));

         for(int var4 = 1; var4 < var0.length; ++var4) {
            var5.append(',');
            var5.append(escaper.escapeString(var0[var4]));
         }

         var5.append(')');
         var5.append('}');
         return var5.toString();
      }
   }

   private static EExprRep parse(char[] var0, int[] var1, int[] var2, int var3, boolean var4) throws ParseException, IllegalPredicateArgumentException, InvalidPredicateClassException {
      int var5 = 0;
      int var6 = var1[0];

      for(int var7 = 0; var6 < var2[0] && var7 >= 0; ++var6) {
         if (var0[var6] == '{') {
            ++var7;
         } else if (var0[var6] == '}') {
            --var7;
         }

         if (var7 == 0) {
            ++var5;
         }
      }

      var6 = 2 + var5 / 2;
      String[] var17 = new String[var6];
      EExprRep[] var8 = new EExprRep[var6];
      int var9 = 0;
      Object var10 = null;
      char[] var11 = new char[2];
      int var12 = -1;
      boolean var14 = false;
      skipSpace(var0, var1, var2, (String)null);

      EExprRep[] var15;
      for(; var1[0] < var2[0]; skipSpace(var0, var1, var2, (String)null)) {
         int var13;
         int var16;
         int var10002;
         switch (var0[var1[0]]) {
            case '&':
            case '-':
            case '|':
            case '~':
               if (var10 != null) {
                  var8[var9++] = (EExprRep)var10;
                  var10 = null;
               }

               if (var12 >= 0 && var11[var12] == var0[var1[0]]) {
                  if ('~' == var0[var1[0]]) {
                     --var12;
                  }
               } else {
                  if (var12 >= 0 && '~' != var0[var1[0]]) {
                     var15 = new EExprRep[var9];
                     System.arraycopy(var8, 0, var15, 0, var9);
                     switch (var11[var12--]) {
                        case '&':
                           var10 = new Intersection(var15);
                           break;
                        case '-':
                           var10 = new Difference(var15);
                           break;
                        case '|':
                           var10 = new Union(var15);
                     }

                     byte var19 = 0;
                     var9 = var19 + 1;
                     var8[var19] = (EExprRep)var10;
                     var10 = null;
                  }

                  ++var12;
                  var11[var12] = var0[var1[0]];
               }
               break;
            case '?':
               if (var10 != null) {
                  throw new ParseException(syntaxMsg("Operator missing", var0, var1[0]));
               }

               var10002 = var1[0]++;
               skipSpace(var0, var1, var2, "Predicate class name is expected");

               for(var13 = var1[0]; var1[0] < var2[0] && var0[var1[0]] != '(' && !isWhitespace(var0[var1[0]]); var10002 = var1[0]++) {
                  validateChar(var0[var1[0]], var1[0]);
               }

               String var23 = new String(var0, var13, var1[0] - var13);
               skipSpace(var0, var1, var2, "Missing '('.");
               var13 = parseToCloseParam(var0, var1, var2, var17, true);

               String[] var24;
               for(var24 = new String[var13]; var13-- > 0; var24[var13] = escaper.unescapeString(var17[var13])) {
               }

               var10 = new PredicateOp(var23, var24);
               break;
            case 'G':
            case 'g':
               if (var0[var1[0] + 1] != 'r' || var0[var1[0] + 2] != 'p') {
                  throw new ParseException(syntaxMsg("Unknown word", var0, var1[0]));
               }

               if (var10 != null) {
                  throw new ParseException(syntaxMsg("Operator missing", var0, var1[0]));
               }

               var1[0] += 3;
               skipSpace(var0, var1, var2, "Incomplete 'Grp()' construct.");
               var13 = parseToCloseParam(var0, var1, var2, var17, false);
               if (var13 == 0) {
                  throw new ParseException("Missing arguments for 'Grp()' construct.");
               }

               if (var13 == 1) {
                  var10 = new GroupIdentifier(escaper.unescapeString(var17[0]));
               } else {
                  GroupIdentifier[] var22 = new GroupIdentifier[var13];

                  for(var16 = 0; var16 < var13; ++var16) {
                     var22[var16] = new GroupIdentifier(escaper.unescapeString(var17[var16]));
                  }

                  var10 = new GroupList(var22);
               }
               break;
            case 'R':
            case 'r':
               if (var0[var1[0] + 1] != 'o' || var0[var1[0] + 2] != 'l') {
                  throw new ParseException(syntaxMsg("Unknown word", var0, var1[0]));
               }

               if (var10 != null) {
                  throw new ParseException(syntaxMsg("Operator missing", var0, var1[0]));
               }

               if (!var4) {
                  throw new ParseException(syntaxMsg("Role within role is not allowed", var0, var1[0]));
               }

               var1[0] += 3;
               skipSpace(var0, var1, var2, "Incomplete 'Rol()' construct.");
               var13 = parseToCloseParam(var0, var1, var2, var17, false);
               if (var13 == 0) {
                  throw new ParseException("Missing arguments for 'Rol()' construct.");
               }

               if (var13 == 1) {
                  var10 = new RoleIdentifier(escaper.unescapeString(var17[0]));
               } else {
                  RoleIdentifier[] var21 = new RoleIdentifier[var13];

                  for(var16 = 0; var16 < var13; ++var16) {
                     var21[var16] = new RoleIdentifier(escaper.unescapeString(var17[var16]));
                  }

                  var10 = new RoleList(var21);
               }
               break;
            case 'U':
            case 'u':
               if (var0[var1[0] + 1] == 's' && var0[var1[0] + 2] == 'r') {
                  if (var10 != null) {
                     throw new ParseException(syntaxMsg("Operator missing", var0, var1[0]));
                  }

                  var1[0] += 3;
                  skipSpace(var0, var1, var2, "Incomplete 'Usr()' construct.");
                  var13 = parseToCloseParam(var0, var1, var2, var17, false);
                  if (var13 == 0) {
                     throw new ParseException("Missing arguments for 'Usr()' construct.");
                  }

                  if (var13 == 1) {
                     var10 = new UserIdentifier(escaper.unescapeString(var17[0]));
                  } else {
                     UserIdentifier[] var20 = new UserIdentifier[var13];

                     for(var16 = 0; var16 < var13; ++var16) {
                        var20[var16] = new UserIdentifier(escaper.unescapeString(var17[var16]));
                     }

                     var10 = new UserList(var20);
                  }
                  break;
               }

               throw new ParseException(syntaxMsg("Unknown word", var0, var1[0]));
            case '{':
               if (var10 != null) {
                  throw new ParseException(syntaxMsg("Operator missing", var0, var1[0]));
               }

               var10002 = var1[0]++;
               var10 = parse(var0, var1, var2, var3 + 1, var4);
               break;
            case '}':
               if (var3 == 0) {
                  throw new ParseException(syntaxMsg("Extra '}'.", var0, var1[0]));
               }

               if (var10 != null) {
                  var8[var9++] = (EExprRep)var10;
                  var10 = null;
               }

               if (var9 > 0) {
                  if (var12 >= 0) {
                     var15 = new EExprRep[var9];
                     System.arraycopy(var8, 0, var15, 0, var9);
                     switch (var11[var12--]) {
                        case '&':
                           var10 = new Intersection(var15);
                           break;
                        case '-':
                           var10 = new Difference(var15);
                           break;
                        case '|':
                           var10 = new Union(var15);
                     }

                     boolean var18 = false;
                  } else {
                     var10 = var8[0];
                  }
               }

               if (var12 >= 0) {
                  throw new ParseException(syntaxMsg("Missing operand for operator '" + var11[var12] + "'.", var0, var1[0]));
               }

               var10002 = var1[0]++;
               if (var10 != null && var10 != Empty.EMPTY) {
                  ((EExprRep)var10).SetEnclosed();
               }

               return (EExprRep)(var10 == null ? Empty.EMPTY : var10);
            default:
               throw new ParseException(syntaxMsg("Unknown word", var0, var1[0]));
         }

         if (var10 == null) {
            var10002 = var1[0]++;
         } else if (var12 >= 0 && var11[var12] == '~') {
            --var12;
            var10 = new Inverse((EExprRep)var10);
         }
      }

      if (var12 >= 0) {
         if (var10 != null) {
            var8[var9++] = (EExprRep)var10;
            var10 = null;
         }

         var15 = new EExprRep[var9];
         System.arraycopy(var8, 0, var15, 0, var9);
         switch (var11[var12--]) {
            case '&':
               var10 = new Intersection(var15);
               break;
            case '-':
               var10 = new Difference(var15);
               break;
            case '|':
               var10 = new Union(var15);
         }
      }

      if (var3 != 0) {
         throw new ParseException(syntaxMsg("Unbalanced '{'.", var0, var1[0]));
      } else {
         return (EExprRep)var10;
      }
   }

   private static void skipSpace(char[] var0, int[] var1, int[] var2, String var3) throws ParseException {
      while(var1[0] < var2[0]) {
         if (!isWhitespace(var0[var1[0]])) {
            return;
         }

         int var10002 = var1[0]++;
      }

      if (var3 != null) {
         throw new ParseException(syntaxMsg(var3, var0, var1[0]));
      }
   }

   private static boolean isWhitespace(char var0) {
      return var0 == ' ' || var0 == '\t' || var0 == '\n';
   }

   private static int parseToCloseParam(char[] var0, int[] var1, int[] var2, String[] var3, boolean var4) throws ParseException {
      int var10004 = var1[0];
      int var10001 = var1[0];
      var1[0] = var10004 + 1;
      if (var0[var10001] != '(') {
         throw new ParseException(syntaxMsg("Missing '('.", var0, var1[0]));
      } else {
         skipSpace(var0, var1, var2, "Missing ')'.");
         int var5 = 0;
         int var6 = -1;
         boolean var7 = false;
         int var8 = var1[0];
         int var10002;
         if (var4 && var0[var1[0]] == '"') {
            var7 = true;
            var10002 = var1[0]++;
         }

         while(true) {
            while(true) {
               if (var7) {
                  if (var1[0] == var2[0]) {
                     throw new ParseException(syntaxMsg("Unbalanced quote '\"'.", var0, var1[0]));
                  }

                  if (var0[var1[0]] != '"') {
                     break;
                  }

                  var6 = ++var1[0];
                  var7 ^= true;
               } else {
                  if (var1[0] == var2[0]) {
                     throw new ParseException(syntaxMsg("Missing ')'.", var0, var1[0]));
                  }

                  if (var0[var1[0]] == '(') {
                     throw new ParseException(syntaxMsg("Unexpected nested '('.", var0, var1[0]));
                  }

                  if (var0[var1[0]] == '{') {
                     throw new ParseException(syntaxMsg("Unexpected '{' inside '(...)'.", var0, var1[0]));
                  }

                  if (isWhitespace(var0[var1[0]])) {
                     if (var6 == -1) {
                        var6 = var1[0];
                     }

                     skipSpace(var0, var1, var2, "Missing ')'.");
                  }

                  if (var0[var1[0]] != ',' && var0[var1[0]] != ')') {
                     if (var6 != -1) {
                        throw new ParseException(syntaxMsg("Missing ',' delimiter.", var0, var1[0]));
                     }
                     break;
                  }

                  if (var6 == -1) {
                     var6 = var1[0];
                  }

                  if (var6 == var8) {
                     if (var4) {
                        var10004 = var1[0];
                        var10001 = var1[0];
                        var1[0] = var10004 + 1;
                        if (var0[var10001] == ')') {
                           skipSpace(var0, var1, var2, (String)null);
                           return var5;
                        }
                     }

                     throw new ParseException(syntaxMsg("Missing name.", var0, var1[0]));
                  }

                  var3[var5++] = new String(var0, var8, var6 - var8);
                  var10004 = var1[0];
                  var10001 = var1[0];
                  var1[0] = var10004 + 1;
                  if (var0[var10001] == ')') {
                     skipSpace(var0, var1, var2, (String)null);
                     return var5;
                  }

                  skipSpace(var0, var1, var2, "Missing ')'.");
                  var8 = var1[0];
                  var6 = -1;
               }
            }

            validateChar(var0[var1[0]], var1[0]);
            var10002 = var1[0]++;
         }
      }
   }

   private static void validateChar(char var0, int var1) throws ParseException {
      switch (var0) {
         case '&':
         case ')':
         case ',':
         case '-':
         case '?':
         case '{':
         case '|':
         case '~':
            throw new ParseException("Character '" + var0 + "'not allowed.", var1);
         default:
      }
   }

   private static String syntaxMsg(String var0, char[] var1, int var2) {
      return var0 + " for '" + new String(var1) + "' at position:" + var2;
   }

   public static void main(String[] var0) throws Exception {
      EExpression var1 = parseResourceExpression(var0[0]);
      System.out.println("Expression: " + var1.externalize());
   }
}
