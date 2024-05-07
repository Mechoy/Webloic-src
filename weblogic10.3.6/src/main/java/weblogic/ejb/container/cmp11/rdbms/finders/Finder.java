package weblogic.ejb.container.cmp11.rdbms.finders;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp11.rdbms.codegen.TypeUtils;
import weblogic.ejb.container.ejbc.EJBCException;
import weblogic.ejb.container.utils.ClassUtils;
import weblogic.ejb.container.utils.MethodUtils;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public final class Finder {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private String methodName = null;
   private List parameterTypes = null;
   private String ejbQuery = null;
   private String sqlQuery = null;
   private WLQLExpression wlqlExpression = null;
   private List finderExpressions = null;
   private FinderOptions finderOptions = null;
   private SQLQueryExpander sqlExpander = null;
   private boolean isSQL;
   private int[] sqlVariableMap;

   public Finder(String var1, String var2) throws InvalidFinderException {
      this.setName(var1);
      this.setWeblogicQuery(var2);
      this.parameterTypes = new ArrayList();
      this.finderExpressions = new ArrayList();
   }

   public void setUsingSQL(boolean var1) {
      this.isSQL = var1;
   }

   private void setName(String var1) throws InvalidFinderException {
      if (var1 == null) {
         throw new InvalidFinderException(1, var1);
      } else if (var1.equals("")) {
         throw new InvalidFinderException(2, var1);
      } else if (!var1.startsWith("find")) {
         throw new InvalidFinderException(3, var1);
      } else {
         this.methodName = var1;
      }
   }

   public String getName() {
      return this.methodName;
   }

   public void addParameterType(String var1) {
      if (var1.length() > 0) {
         this.parameterTypes.add(var1);
      }

   }

   public Iterator getParameterTypes() {
      return this.parameterTypes.iterator();
   }

   public void setWeblogicQuery(String var1) {
      this.ejbQuery = var1;
      this.sqlQuery = null;
   }

   public String getWeblogicQuery() {
      return this.ejbQuery;
   }

   public String getSQLQuery(Hashtable var1) throws IllegalExpressionException {
      this.computeSQLQuery(var1);
      if (this.sqlExpander.hasWarnings()) {
         Collection var2 = this.sqlExpander.getWarnings();
         if (var2 != null) {
            EJBLogger var3 = new EJBLogger();
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
               Exception var5 = (Exception)var4.next();
               EJBLogger.logWarningFromEJBQLCompiler(var5.getMessage());
            }
         }
      }

      return this.getSQLQuery();
   }

   public String getSQLQuery() {
      return this.sqlQuery;
   }

   public WLQLExpression getWLQLExpression() {
      return this.wlqlExpression;
   }

   public void addFinderExpression(int var1, String var2, String var3) throws InvalidFinderException {
      this.finderExpressions.add(new FinderExpression(var1, var2, var3));
   }

   public void addFinderExpression(FinderExpression var1) {
      this.finderExpressions.add(var1);
   }

   public Iterator getFinderExpressions() {
      return this.finderExpressions.iterator();
   }

   public FinderExpression getFinderExpression(int var1) {
      Iterator var2 = this.getFinderExpressions();

      FinderExpression var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (FinderExpression)var2.next();
      } while(var3.getNumber() != var1);

      return var3;
   }

   public FinderOptions getFinderOptions() {
      return this.finderOptions;
   }

   public void setFinderOptions(FinderOptions var1) {
      this.finderOptions = var1;
   }

   public int getParameterIndex(int var1) {
      if (this.isSQL) {
         return this.sqlVariableMap[var1 - 1];
      } else {
         int[] var2 = this.sqlExpander.getVariableMap();
         return var2[var1 - 1];
      }
   }

   public int getVariableCount() {
      if (this.isSQL) {
         return this.sqlVariableMap.length;
      } else {
         int[] var1 = this.sqlExpander.getVariableMap();
         Debug.assertion(var1 != null);
         return var1.length;
      }
   }

   public FinderExpression getExpressionForVariable(int var1, Class[] var2) {
      FinderExpression var3 = this.getFinderExpression(this.getParameterIndex(var1));
      if (var3 == null) {
         try {
            var3 = new FinderExpression(this.getParameterIndex(var1), "@" + this.getParameterIndex(var1), var2[this.getParameterIndex(var1)].getName());
         } catch (InvalidFinderException var5) {
            throw new AssertionError("Internal logic produced invalid  FinderExpression " + StackTraceUtils.throwable2StackTrace(var5));
         }
      }

      return var3;
   }

   public boolean methodIsEquivalent(Method var1) {
      if (!var1.getName().equals(this.getName())) {
         return false;
      } else {
         String[] var2 = MethodUtils.classesToJavaSourceTypes(var1.getParameterTypes());
         if (this.parameterTypes.size() != var2.length) {
            return false;
         } else {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               if (!var2[var3].equals(this.parameterTypes.get(var3))) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Finder)) {
         return false;
      } else {
         Finder var2 = (Finder)var1;
         if (!this.getName().equals(var2.getName())) {
            return false;
         } else {
            if (this.getWLQLExpression() == null) {
               if (var2.getWLQLExpression() != null) {
                  return false;
               }
            } else if (!this.getWLQLExpression().equals(var2.getWLQLExpression())) {
               return false;
            }

            if (!this.finderExpressions.equals(var2.finderExpressions)) {
               return false;
            } else {
               return this.parameterTypes.equals(var2.parameterTypes);
            }
         }
      }
   }

   public int hashCode() {
      return this.getName().hashCode() ^ this.getWLQLExpression().hashCode();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[Finder ");
      var1.append("methodName = " + this.methodName + "; ");
      var1.append("wlqlQuery = " + this.ejbQuery + "; ");
      var1.append("wlqlExpression = " + this.wlqlExpression + "; ");
      var1.append("sqlQuery = " + this.sqlQuery + " ");
      var1.append("Finder Expressions = " + this.finderExpressions + ";");
      var1.append("Parameter types = " + this.parameterTypes + ";");
      var1.append(" End-Finder]");
      return var1.toString();
   }

   public void parseExpression() throws EJBCException, InvalidFinderException {
      if (!this.isSQL) {
         WLQLParser var1 = new WLQLParser();
         if (this.ejbQuery == null) {
            throw new InvalidFinderException(4, this.ejbQuery);
         } else {
            if (this.ejbQuery.equals("")) {
               this.wlqlExpression = new WLQLExpression(17);
            } else {
               try {
                  this.wlqlExpression = var1.parse(this.ejbQuery);
               } catch (EJBCException var3) {
                  throw var3;
               }
            }

         }
      }
   }

   public void computeSQLQuery(Hashtable var1) throws IllegalExpressionException {
      if (this.isSQL) {
         this.computeLiteralSQLQuery();
      } else {
         try {
            this.sqlExpander = new SQLQueryExpander(this.wlqlExpression, var1);
            this.sqlQuery = this.sqlExpander.toSQL();
            if (!this.sqlQuery.trim().equals("")) {
               this.sqlQuery = " WHERE " + this.sqlQuery;
            }

            if (this.getFinderOptions() != null && this.getFinderOptions().getFindForUpdate()) {
               this.sqlQuery = this.sqlQuery + " FOR UPDATE";
            }
         } catch (IllegalExpressionException var3) {
            var3.setFinder(this);
            throw var3;
         }
      }

   }

   public String toUserLevelString(boolean var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("Finder");
      if (var1) {
         var2.append("\n\t");
      } else {
         var2.append(", ");
      }

      var2.append("Method Name: " + this.methodName);
      if (var1) {
         var2.append("\n\t");
      } else {
         var2.append(", ");
      }

      var2.append("Parameter Types: (");
      Iterator var3 = this.getParameterTypes();

      while(var3.hasNext()) {
         var2.append("" + var3.next());
         if (var3.hasNext()) {
            var2.append(", ");
         }
      }

      var2.append(")");
      if (var1) {
         var2.append("\n\t");
      } else {
         var2.append(", ");
      }

      var2.append("WebLogic Query: " + this.ejbQuery);
      if (var1) {
         var2.append("\n\t");
      } else {
         var2.append(", ");
      }

      var2.append("Finder Expressions: (");
      Iterator var4 = this.getFinderExpressions();

      while(var4.hasNext()) {
         var2.append("" + var4.next());
         if (var4.hasNext()) {
            var2.append(", ");
         }
      }

      var2.append(")");
      return var2.toString();
   }

   public boolean isContentValid() {
      this.sqlQuery = null;

      try {
         this.parseExpression();
      } catch (EJBCException var9) {
         return false;
      } catch (InvalidFinderException var10) {
         return false;
      }

      HashSet var1 = new HashSet();
      Iterator var2 = this.getFinderExpressions();

      while(var2.hasNext()) {
         FinderExpression var3 = (FinderExpression)var2.next();
         int var4 = var3.getNumber();
         if (var1.contains(new Integer(var4))) {
            return false;
         }

         var1.add(new Integer(var4));
         if (var3.getExpressionText() != null && !var3.getExpressionText().trim().equals("")) {
            String var5 = var3.getExpressionType();
            if (var5 != null && !var5.trim().equals("")) {
               Class var6 = null;

               try {
                  var6 = ClassUtils.nameToClass(var5, this.getClass().getClassLoader());
               } catch (ClassNotFoundException var8) {
                  return false;
               }

               if (!TypeUtils.isValidSQLType(var6)) {
                  return false;
               }
               continue;
            }

            return false;
         }

         return false;
      }

      return true;
   }

   public static boolean isQueryValid(String var0) {
      if (var0 != null && !var0.equals("")) {
         try {
            WLQLParser var1 = new WLQLParser();
            var1.parse(var0);
            return true;
         } catch (EJBCException var2) {
            return false;
         }
      } else {
         return false;
      }
   }

   private void computeLiteralSQLQuery() {
      ArrayList var1 = new ArrayList();
      StringBuffer var2 = new StringBuffer();
      var2.append("WHERE ");
      int var3 = this.ejbQuery.length();

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         char var5 = this.ejbQuery.charAt(var4);
         if (var5 != '$') {
            var2.append(var5);
         } else {
            int var6;
            for(var6 = var4; var4 + 1 < var3 && Character.isDigit(this.ejbQuery.charAt(var4 + 1)); ++var4) {
            }

            if (var6 == var4) {
               var2.append('$');
            } else {
               int var8 = 1;
               int var9 = 0;

               for(int var10 = var4; var10 != var6; --var10) {
                  var9 += (this.ejbQuery.charAt(var10) - 48) * var8;
                  var8 *= 10;
               }

               var2.append('?');
               var1.add(new Integer(var9));
            }
         }
      }

      this.sqlQuery = var2.toString();
      this.sqlVariableMap = new int[var1.size()];
      var4 = 0;

      for(Iterator var11 = var1.iterator(); var11.hasNext(); this.sqlVariableMap[var4++] = (Integer)var11.next()) {
      }

   }

   public static class FinderOptions {
      private boolean findForUpdate = false;

      public boolean getFindForUpdate() {
         return this.findForUpdate;
      }

      public void setFindForUpdate(boolean var1) {
         this.findForUpdate = var1;
      }
   }

   public static class FinderExpression {
      private static final boolean debug = false;
      private static final boolean verbose = false;
      private int number;
      private String expressionText;
      private String expressionType;

      public FinderExpression(int var1) {
         this.number = var1;
      }

      public FinderExpression(int var1, String var2, String var3) throws InvalidFinderException {
         if (var1 < 0) {
            throw new InvalidFinderException(5, (String)null);
         } else {
            this.number = var1;
            if (var2 == null) {
               throw new InvalidFinderException(6, (String)null);
            } else {
               this.expressionText = var2;
               if (var3 == null) {
                  throw new InvalidFinderException(7, (String)null);
               } else {
                  this.expressionType = var3;
               }
            }
         }
      }

      public int getNumber() {
         return this.number;
      }

      public void setNumber(int var1) {
         this.number = var1;
      }

      public String getExpressionText() {
         return this.expressionText;
      }

      public String getExpressionType() {
         return this.expressionType;
      }

      public void setExpressionText(String var1) {
         this.expressionText = var1;
      }

      public void setExpressionType(String var1) {
         this.expressionType = var1;
      }

      public String getExpressionWithParams(String[] var1) {
         StringBuffer var2 = new StringBuffer();
         StringBuffer var3 = null;
         String var4 = this.expressionText;

         while(true) {
            while(var4.length() > 0) {
               if (var4.charAt(0) == '@') {
                  var4 = var4.substring(1);
                  var3 = new StringBuffer();

                  while(Character.isDigit(var4.charAt(0))) {
                     var3.append(var4.charAt(0));
                     var4 = var4.substring(1);
                     if (var4.length() <= 0) {
                        break;
                     }
                  }

                  boolean var5 = true;

                  int var8;
                  try {
                     var8 = Integer.parseInt(var3.toString());
                  } catch (NumberFormatException var7) {
                     throw new AssertionError("Internal logic error reading a finder expression:" + StackTraceUtils.throwable2StackTrace(var7));
                  }

                  var2.append(var1[var8]);
               } else {
                  var2.append(var4.charAt(0));
                  var4 = var4.substring(1);
               }
            }

            return var2.toString();
         }
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof FinderExpression)) {
            return false;
         } else {
            FinderExpression var2 = (FinderExpression)var1;
            if (var2.getNumber() != this.getNumber()) {
               return false;
            } else if (!var2.getExpressionText().equals(this.getExpressionText())) {
               return false;
            } else {
               return var2.getExpressionType().equals(this.getExpressionType());
            }
         }
      }

      public int hashCode() {
         return this.number ^ this.expressionText.hashCode() ^ this.expressionType.hashCode();
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append("[Finder.FinderExpression: ");
         var1.append(" number: " + this.number + "; ");
         var1.append(" expressionText: " + this.expressionText + "; ");
         var1.append(" expressionType: " + this.expressionType + "; ");
         var1.append("]");
         return var1.toString();
      }
   }
}
