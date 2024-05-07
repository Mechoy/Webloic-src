package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.List;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.persistence.spi.EjbEntityRef;
import weblogic.logging.Loggable;
import weblogic.utils.ErrorCollectionException;

public final class ExprID extends BaseExpr implements Expr, SingleExprIDHolder, ExpressionTypes {
   private static final char DOT = '.';
   public static final int ABSTRACT_SCHEMA_NAME = 0;
   public static final int RANGE_VARIABLE = 1;
   public static final int COLLECTION_MEMBER_ID = 2;
   public static final int PATH_EXPRESSION_CMP = 3;
   public static final int PATH_EXPRESSION_CMR = 4;
   private String ejbqlId;
   private String dealiasedEjbqlId;
   private String sqlString = null;
   private boolean prepForSQLGenDone = false;
   private char[] allowBackwardIdChars = new char[]{'@', '-'};

   protected ExprID(int var1, String var2) {
      super(var1, var2);
      this.ejbqlId = var2;
      this.debugClassName = "ExprID - '" + this.ejbqlId + "' ";
   }

   public void init_method() throws ErrorCollectionException {
      if (this.ejbqlId != null && this.ejbqlId.length() > 0) {
         this.validateID();
      } else {
         throw new AssertionError(" ERROR encountered a NULL or EMPTY ejbqlId ");
      }
   }

   public void calculate_method() throws ErrorCollectionException {
   }

   protected Expr invertForNOT() throws ErrorCollectionException {
      return this;
   }

   public String toSQL() throws ErrorCollectionException {
      if (this.sqlString == null) {
         this.calcTableAndColumnForCmpField();
      }

      return this.sqlString;
   }

   public void appendEJBQLTokens(List var1) {
      this.appendPreEJBQLTokensToList(var1);
      this.appendMainEJBQLTokenToList(var1);
      this.appendPostEJBQLTokensToList(var1);
   }

   public ExprID getExprID() {
      return this;
   }

   private void validateID() throws ErrorCollectionException {
      char[] var1 = this.ejbqlId.toCharArray();
      if (var1.length > 0 && var1[0] != '.' && !Character.isJavaIdentifierStart(var1[0])) {
         Loggable var2 = EJBLogger.logInvalidStartCharacterForEJBQLIdentifierLoggable(var1[0], this.ejbqlId);
         Exception var3 = new Exception(var2.getMessage());
         this.globalContext.addWarning(var3);
         if (this.allowBackwardsCompatibleChar(var1[0])) {
            var2 = EJBLogger.logEJBQLCharAllowedForBackwardsCompatibilityLoggable(var1[0], this.ejbqlId);
            var3 = new Exception(var2.getMessage());
            this.globalContext.addWarning(var3);
         }
      }

      if (var1.length > 1) {
         for(int var5 = 1; var5 < var1.length; ++var5) {
            if (var1[var5] != '.' && !Character.isJavaIdentifierPart(var1[var5])) {
               Loggable var6 = EJBLogger.logInvalidPartCharacterForEJBQLIdentifierLoggable(var1[var5], this.ejbqlId);
               Exception var4 = new Exception(var6.getMessage());
               this.globalContext.addWarning(var4);
               if (this.allowBackwardsCompatibleChar(var1[var5])) {
                  var6 = EJBLogger.logEJBQLCharAllowedForBackwardsCompatibilityLoggable(var1[var5], this.ejbqlId);
                  var4 = new Exception(var6.getMessage());
                  this.globalContext.addWarning(var4);
               }
            }
         }
      }

   }

   private boolean allowBackwardsCompatibleChar(char var1) {
      for(int var2 = 0; var2 < this.allowBackwardIdChars.length; ++var2) {
         if (this.allowBackwardIdChars[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   public int getRelationshipTypeForPathExpressionWithNoSQLGen() throws ErrorCollectionException {
      try {
         return this.queryTree.getRelationshipTypeForPathExpressionWithNoSQLGen(this.getDealiasedEjbqlID());
      } catch (Exception var2) {
         this.markExcAndThrowCollectionException(var2);
         return -1;
      }
   }

   public boolean isPathExpressionEndingInCmpFieldWithNoSQLGen() throws ErrorCollectionException {
      int var1 = this.getRelationshipTypeForPathExpressionWithNoSQLGen();
      return var1 == 0;
   }

   private boolean isPathExpressionEndingInCmpFieldWithSQLGen() throws ErrorCollectionException {
      if (!this.prepForSQLGenDone) {
         this.prepareIdentifierForSQLGen();
      }

      String var1 = this.getDealiasedEjbqlID();
      if (var1 == null) {
         return false;
      } else {
         boolean var2 = false;

         try {
            var2 = this.globalContext.pathExpressionEndsInField(this.queryTree, var1);
         } catch (Exception var4) {
            this.markExcAndThrowCollectionException(var4);
         }

         return var2;
      }
   }

   public boolean isPathExpressionEndingInRemoteInterfaceWithSQLGen() throws ErrorCollectionException {
      if (!this.prepForSQLGenDone) {
         this.prepareIdentifierForSQLGen();
      }

      String var1 = this.getDealiasedEjbqlID();
      if (var1 == null) {
         return false;
      } else {
         boolean var2 = false;

         try {
            var2 = this.globalContext.pathExpressionEndsInRemoteInterface(this.queryTree, var1);
         } catch (IllegalExpressionException var4) {
            this.markExcAndThrowCollectionException(var4);
         }

         return var2;
      }
   }

   public boolean isPathExpressionEndingInBlobClobColumnWithSQLGen() throws ErrorCollectionException {
      if (!this.prepForSQLGenDone) {
         this.prepareIdentifierForSQLGen();
      }

      if (!this.isPathExpressionEndingInCmpFieldWithSQLGen()) {
         return false;
      } else {
         RDBMSBean var1 = this.getRDBMSBeanWithSQLGen();
         String var2 = QueryContext.getLastFieldFromId(this.getEjbqlID());
         return var1.isBlobCmpColumnTypeForField(var2) || var1.isClobCmpColumnTypeForField(var2);
      }
   }

   public RDBMSBean getRDBMSBeanWithSQLGen() throws ErrorCollectionException {
      RDBMSBean var1 = null;
      if (!this.prepForSQLGenDone) {
         this.prepareIdentifierForSQLGen();
      }

      String var2 = this.getDealiasedEjbqlID();

      try {
         var1 = this.queryTree.getLastRDBMSBeanForPathExpression(var2);
      } catch (Exception var4) {
         this.markExcAndThrowCollectionException(var4);
      }

      if (var1 == null) {
         this.markExcAndThrowCollectionException(new IllegalExpressionException(7, "Fatal Internal Error, could not get RDBMSBean for path expression: '" + var2 + "'"));
      }

      return var1;
   }

   public EjbEntityRef getEntityRefWithSQLGen(String var1) throws ErrorCollectionException {
      RDBMSBean var2 = null;

      try {
         var2 = this.getRDBMSBeanWithSQLGen();
      } catch (Exception var4) {
         this.markExcAndThrowCollectionException(var4);
      }

      if (var2 == null) {
         throw new ErrorCollectionException(" getEntityRefFromRDBMSBean passed NULL RDBMSBean ! ");
      } else {
         EjbEntityRef var3 = var2.getEjbEntityRef(var1);
         if (var3 == null) {
            this.markExcAndThrowCollectionException(new Exception(" <cmr-field> " + var1 + " could not get EjbEntityRef from RDBMSBean: " + var2.getEjbName() + " ! "));
         }

         return var3;
      }
   }

   public String getEjbqlID() {
      return this.ejbqlId;
   }

   public void setEjbqlID(String var1) {
      this.ejbqlId = var1;
      this.prepForSQLGenDone = false;
   }

   public String getDealiasedEjbqlID() {
      if (debugLogger.isDebugEnabled()) {
         debug(" getDealiasedEjbqlID  on '" + this.ejbqlId + "'");
      }

      if (debugLogger.isDebugEnabled() && this.globalContext == null) {
         debug("  globalContext is  NULL !");
      }

      this.dealiasedEjbqlId = this.globalContext.replaceIdAliases(this.ejbqlId);
      return this.dealiasedEjbqlId;
   }

   public void setSQL(String var1) {
      this.sqlString = var1;
   }

   public String calcTableAndColumnForCmpField() throws ErrorCollectionException {
      if (!this.prepForSQLGenDone) {
         this.prepareIdentifierForSQLGen();
      }

      String var1 = this.getDealiasedEjbqlID();
      if (!this.isPathExpressionEndingInCmpFieldWithSQLGen()) {
         this.markExcAndThrowCollectionException(new IllegalExpressionException(7, " Internal Error:  in ExprID.calcTableAndColumnForCmpField() attempt to execute Method on a pathExpression that is not terminated by a cmp-field:  '" + var1 + "'"));
      }

      String var2 = null;

      try {
         JoinNode var3 = this.getJoinTreeForID();
         var2 = JoinNode.getTableAndField(this.queryTree, var3, var1);
         if (!this.queryTree.thisQueryNodeOwnsId(var1)) {
         }
      } catch (IllegalExpressionException var4) {
         this.markExcAndThrowCollectionException(var4);
      }

      this.setSQL(var2 + " ");
      return var2;
   }

   public JoinNode getJoinTreeForID() throws ErrorCollectionException {
      String var1 = this.getDealiasedEjbqlID();
      JoinNode var2 = null;

      try {
         var2 = this.queryTree.getJoinTreeForId(var1);
      } catch (IllegalExpressionException var4) {
         throw new ErrorCollectionException(var4.getMessage());
      }

      if (var2 == null) {
         this.markExcAndThrowCollectionException(new IllegalExpressionException(7, " Internal Error:  in ExprID.getJoinTreeForID() attempt to get joinTree for ID: '" + var1 + "' yielded a NULL joinTree."));
      }

      return var2;
   }

   public JoinNode getJoinNodeForLastCmrFieldWithSQLGen() throws ErrorCollectionException {
      String var1 = this.getDealiasedEjbqlID();
      if (!this.prepForSQLGenDone) {
         this.prepareIdentifierForSQLGen();
      }

      JoinNode var2 = this.getJoinTreeForID();
      JoinNode var3 = null;

      try {
         var3 = JoinNode.getNode(var2, var1);
      } catch (IllegalExpressionException var5) {
         this.markExcAndThrowCollectionException(var5);
      }

      return var3;
   }

   public String getFirstField() {
      return JoinNode.getFirstFieldFromId(this.getDealiasedEjbqlID());
   }

   public String getLastField() {
      return JoinNode.getLastFieldFromId(this.getDealiasedEjbqlID());
   }

   public int countPathNodes() {
      return JoinNode.countPathNodes(this.getEjbqlID());
   }

   public void prepareIdentifierForSQLGen() throws ErrorCollectionException {
      try {
         this.queryTree.prepareIdentifierForSQLGen(this.getDealiasedEjbqlID());
      } catch (IllegalExpressionException var2) {
         this.markExcAndThrowCollectionException(var2);
      }

      this.prepForSQLGenDone = true;
   }

   public static ExprID newInitExprID(QueryContext var0, QueryNode var1, String var2) throws ErrorCollectionException {
      ExprID var3 = new ExprID(17, var2);
      var3.init(var0, var1);
      return var3;
   }

   public static String calcTableAndColumn(QueryContext var0, QueryNode var1, String var2) throws ErrorCollectionException {
      ExprID var3 = new ExprID(17, var2);
      var3.init(var0, var1);
      var3.calculate();
      return var3.calcTableAndColumnForCmpField();
   }

   private static void debug(String var0) {
      debugLogger.debug("[ExprID] " + var0);
   }
}
