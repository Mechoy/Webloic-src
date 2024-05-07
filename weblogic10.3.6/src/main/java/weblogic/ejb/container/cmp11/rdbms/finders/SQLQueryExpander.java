package weblogic.ejb.container.cmp11.rdbms.finders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.utils.ToStringUtils;
import weblogic.logging.Loggable;

public class SQLQueryExpander implements WLQLExpressionTypes {
   private static final boolean verbose = false;
   private static final char DOT = '.';
   private int variableCount = 0;
   protected WLQLExpression queryExpression = null;
   private Hashtable parameterMap = null;
   private List variableMap = null;
   private Collection warnings = null;

   public SQLQueryExpander(WLQLExpression var1, Hashtable var2) {
      this.queryExpression = var1;
      this.parameterMap = var2;
      this.variableMap = new LinkedList();
   }

   public String toSQL() throws IllegalExpressionException {
      return this.toSQL(this.queryExpression);
   }

   public String toSQL(WLQLExpression var1) throws IllegalExpressionException {
      switch (var1.type()) {
         case 0:
            return this.join(var1, "AND");
         case 1:
            return this.join(var1, "OR");
         case 2:
            return "NOT " + this.toSQL(var1.term(0));
         case 3:
            return this.twoTerm(var1, "=");
         case 4:
            return this.twoTerm(var1, "<");
         case 5:
            return this.twoTerm(var1, ">");
         case 6:
            return this.twoTerm(var1, "<=");
         case 7:
            return this.twoTerm(var1, ">=");
         case 8:
            return this.twoTerm(var1, "LIKE");
         case 9:
            return this.getIdentifier(var1.getSval());
         case 10:
            return "'" + ToStringUtils.escapedQuotesToString(var1.getSval()) + "'";
         case 11:
            return var1.getSval();
         case 12:
            return var1.getSpecialName() + " " + this.join(var1, ",");
         case 13:
            return this.getVariable(var1);
         case 14:
            return this.toSQL(var1.term(0)) + " IS NULL";
         case 15:
            return this.toSQL(var1.term(0)) + " IS NOT NULL";
         case 16:
            return this.toSQL(var1.term(1)) + " ORDER BY " + var1.term(0).getSval();
         case 17:
            return "";
         default:
            throw new IllegalExpressionException(2, WLQLExpressionTypes.TYPE_NAMES[var1.getType()]);
      }
   }

   public String[] getParameterNames(WLQLExpression var1) {
      Vector var2 = new Vector();
      this.getParameterNames(var1, var2);
      String[] var3 = new String[var2.size()];
      var2.copyInto(var3);
      return var3;
   }

   public void getParameterNames(WLQLExpression var1, Vector var2) {
      if (var1.type() == 13) {
         var2.addElement(var1.getSval());
      } else {
         int var3 = 0;

         for(int var4 = var1.numTerms(); var3 < var4; ++var3) {
            this.getParameterNames(var1.term(var3), var2);
         }
      }

   }

   public int[] getVariableMap() {
      int[] var1 = new int[this.variableMap.size()];
      Iterator var2 = this.variableMap.iterator();

      Integer var4;
      for(int var3 = 0; var2.hasNext(); var1[var3++] = var4) {
         var4 = (Integer)var2.next();
      }

      return var1;
   }

   boolean hasWarnings() {
      if (this.warnings == null) {
         return false;
      } else {
         return this.warnings.size() != 0;
      }
   }

   Collection getWarnings() {
      return (Collection)(this.warnings == null ? new ArrayList() : this.warnings);
   }

   private void addWarning(Exception var1) {
      Collection var2 = this.getWarnings();
      var2.add(var1);
   }

   protected String join(WLQLExpression var1, String var2) throws IllegalExpressionException {
      int var3 = var1.numTerms();
      StringBuffer var4 = new StringBuffer("(");

      for(int var5 = 0; var5 < var3; ++var5) {
         var4.append(this.toSQL(var1.term(var5)));
         if (var5 < var3 - 1) {
            var4.append(" ").append(var2).append(" ");
         }
      }

      var4.append(")");
      return var4.toString();
   }

   protected String twoTerm(WLQLExpression var1, String var2) throws IllegalExpressionException {
      String var3 = this.toSQL(var1.term(0));
      String var4 = this.toSQL(var1.term(1));
      String var5 = "(" + var3 + " " + var2 + " " + var4 + ")";
      return var5;
   }

   private String getIdentifier(String var1) throws IllegalExpressionException {
      String var2 = (String)this.parameterMap.get(var1);
      if (null == var2) {
         throw new IllegalExpressionException(1, var1);
      } else {
         char[] var3 = var2.toCharArray();
         if (var3.length > 0 && var3[0] != '.' && !Character.isJavaIdentifierStart(var3[0])) {
            Loggable var4 = EJBLogger.logInvalidStartCharacterForEJBQLIdentifierLoggable(var3[0], var2);
            IllegalExpressionException var5 = new IllegalExpressionException(1, var4.getMessage());
            this.addWarning(var5);
         }

         if (var3.length > 1) {
            for(int var7 = 1; var7 < var3.length; ++var7) {
               if (var3[var7] != '.' && !Character.isJavaIdentifierPart(var3[var7])) {
                  Loggable var8 = EJBLogger.logInvalidPartCharacterForEJBQLIdentifierLoggable(var3[var7], var2);
                  IllegalExpressionException var6 = new IllegalExpressionException(1, var8.getMessage());
                  this.addWarning(var6);
               }
            }
         }

         return var2;
      }
   }

   private String getVariable(WLQLExpression var1) {
      this.variableMap.add(new Integer(var1.getSval()));
      ++this.variableCount;
      return "?";
   }
}
