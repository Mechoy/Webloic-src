package weblogic.entitlement.expression;

import weblogic.entitlement.engine.EEngine;
import weblogic.entitlement.engine.ESubject;
import weblogic.entitlement.engine.PredicateRegistry;
import weblogic.entitlement.engine.ResourceNode;
import weblogic.entitlement.engine.UnregisteredPredicateException;
import weblogic.security.providers.authorization.IllegalPredicateArgumentException;
import weblogic.security.providers.authorization.Predicate;
import weblogic.security.service.ContextHandler;

public class PredicateOp extends EExprRep {
   private Predicate mPredicate;
   private String[] mArgs;

   public PredicateOp() {
      this.mPredicate = null;
      this.mArgs = null;
   }

   public PredicateOp(String var1) throws InvalidPredicateClassException, IllegalPredicateArgumentException {
      this(var1, (String[])null);
   }

   public PredicateOp(String var1, String[] var2) throws InvalidPredicateClassException, IllegalPredicateArgumentException {
      this.mPredicate = null;
      this.mArgs = null;
      this.mPredicate = EEngine.validatePredicate(var1);
      this.mArgs = var2;
      String[] var3 = new String[var2 == null ? 0 : var2.length];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var2[var4].startsWith("\"") && var2[var4].endsWith("\"")) {
            var3[var4] = var2[var4].substring(1, var2[var4].length() - 1);
         } else {
            var3[var4] = var2[var4];
         }
      }

      this.mPredicate.init(var3);
   }

   protected int getDependsOnInternal() {
      return 4;
   }

   public final boolean evaluate(ESubject var1, ResourceNode var2, ContextHandler var3, PredicateRegistry var4) {
      if (!var4.isRegistered(this.getPredicateClassName())) {
         throw new UnregisteredPredicateException(this.getPredicateClassName());
      } else {
         return this.mPredicate.evaluate(var1.getSubject(), var2.getResource(), var3);
      }
   }

   public String getPredicateClassName() {
      return this.mPredicate.getClass().getName();
   }

   public String[] getPredicateArguments() {
      return this.mArgs;
   }

   char getTypeId() {
      return 'p';
   }

   void outForPersist(StringBuffer var1) {
      this.writeTypeId(var1);
      writeStr(this.getPredicateClassName(), var1);
      int var2 = this.mArgs == null ? 0 : this.mArgs.length;
      var1.append((char)var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         writeStr(this.mArgs[var3], var1);
      }

   }

   protected void writeExternalForm(StringBuffer var1) {
      if (this.Enclosed) {
         var1.append('{');
      }

      var1.append('?');
      var1.append(this.getPredicateClassName());
      var1.append('(');
      String[] var2 = this.getPredicateArguments();
      if (var2 != null && var2.length > 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var3 > 0) {
               var1.append(',');
            }

            var1.append(var2[var3]);
         }
      }

      var1.append(')');
      if (this.Enclosed) {
         var1.append('}');
      }

   }
}
