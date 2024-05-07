package weblogic.entitlement.expression;

import weblogic.security.shared.LoggerWrapper;

public abstract class EExprRep implements EExpression {
   protected static int DEPENDS_ON_UNKNOWN;
   private static LoggerWrapper LOG = LoggerWrapper.getInstance("SecurityEEngine");
   private int dependsOn;
   protected boolean Enclosed;

   public EExprRep() {
      this.dependsOn = DEPENDS_ON_UNKNOWN;
      this.Enclosed = false;
   }

   public void SetEnclosed() {
      this.Enclosed = true;
   }

   public final int getDependsOn() {
      if (this.dependsOn == DEPENDS_ON_UNKNOWN) {
         this.dependsOn = this.getDependsOnInternal();
      }

      return this.dependsOn;
   }

   protected abstract int getDependsOnInternal();

   public final String externalize() {
      StringBuffer var1 = new StringBuffer();
      this.writeExternalForm(var1);
      return var1.toString();
   }

   protected abstract void writeExternalForm(StringBuffer var1);

   public String serialize() {
      StringBuffer var1 = new StringBuffer();
      this.outForPersist(var1);
      return var1.toString();
   }

   abstract void outForPersist(StringBuffer var1);

   abstract char getTypeId();

   protected void writeTypeId(StringBuffer var1) {
      var1.append((char)(this.getTypeId() | (this.Enclosed ? 128 : 0)));
   }

   public static EExprRep deserialize(String var0) {
      if (var0 != null && var0.length() != 0) {
         char[] var1 = var0.toCharArray();
         int[] var2 = new int[]{0};
         return deserialize(var1, var2);
      } else {
         return null;
      }
   }

   private static EExprRep deserialize(char[] var0, int[] var1) {
      int var10004 = var1[0];
      int var10001 = var1[0];
      var1[0] = var10004 + 1;
      char var2 = var0[var10001];
      char var3 = (char)(var2 & 127);
      Object var4 = null;
      switch (var3) {
         case '&':
            var4 = new Intersection(getFuncArgs(var0, var1));
            break;
         case '-':
            var4 = new Difference(getFuncArgs(var0, var1));
            break;
         case 'G':
            var4 = new GroupList(getFuncArgs(var0, var1));
            break;
         case 'R':
            var4 = new RoleList(getFuncArgs(var0, var1));
            break;
         case 'U':
            var4 = new UserList(getFuncArgs(var0, var1));
            break;
         case 'e':
            return Empty.EMPTY;
         case 'g':
            var4 = new GroupIdentifier(getStr(var0, var1));
            break;
         case 'p':
            String var5 = getStr(var0, var1);
            var10004 = var1[0];
            var10001 = var1[0];
            var1[0] = var10004 + 1;
            char var6 = var0[var10001];
            String[] var7 = null;
            if (var6 > 0) {
               var7 = new String[var6];

               for(int var8 = 0; var8 < var6; ++var8) {
                  var7[var8] = getStr(var0, var1);
               }
            }

            try {
               var4 = new PredicateOp(var5, var7);
            } catch (Exception var9) {
               if (LOG.isDebugEnabled()) {
                  LOG.debug("Failed to deserialize a predicate in expression", var9);
               }
            }
            break;
         case 'r':
            var4 = new RoleIdentifier(getStr(var0, var1));
            break;
         case 'u':
            var4 = new UserIdentifier(getStr(var0, var1));
            break;
         case '|':
            var4 = new Union(getFuncArgs(var0, var1));
            break;
         case '~':
            var4 = new Inverse(getFuncArgs(var0, var1)[0]);
            break;
         default:
            throw new RuntimeException("Invalid expression type id: " + var3);
      }

      ((EExprRep)var4).Enclosed = var2 > 128;
      return (EExprRep)var4;
   }

   private static String getStr(char[] var0, int[] var1) {
      int var2;
      int var10002;
      for(var2 = var1[0]; var1[0] < var0.length && var0[var1[0]] != '\n'; var10002 = var1[0]++) {
      }

      int var10007 = var1[0];
      int var10004 = var1[0];
      var1[0] = var10007 + 1;
      return new String(var0, var2, var10004 - var2);
   }

   protected static void writeStr(String var0, StringBuffer var1) {
      var1.append(var0).append('\n');
   }

   private static EExprRep[] getFuncArgs(char[] var0, int[] var1) {
      int var10004 = var1[0];
      int var10001 = var1[0];
      var1[0] = var10004 + 1;
      char var2 = var0[var10001];
      EExprRep[] var3 = new EExprRep[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = deserialize(var0, var1);
      }

      return var3;
   }
}
