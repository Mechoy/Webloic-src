package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.ejb.container.cmp.rdbms.RDBMSUtils;
import weblogic.ejb.container.dd.DDConstants;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.logging.Loggable;
import weblogic.utils.Debug;

public class JoinNode {
   private static final DebugLogger debugLogger;
   private JoinNode prevNode;
   private String prevCMRField;
   private RDBMSBean thisBean;
   private String tableAlias;
   private String tableName;
   private Map otherTableNameAliasMap;
   private Map otherTableName2JoinSQL;
   private List forceOtherTableName2JoinSQL;
   private int relationshipType;
   private boolean isManyToMany;
   private boolean isRemoteInterface;
   private boolean prevNodeOwnsFK;
   private List joinColumns;
   private boolean doLeftOuterJoin;
   private boolean dbType;
   private String tableAliasMN;
   private String tableNameMN;
   private Map children;
   private QueryContext queryContext;
   public static final int PATHS_DISTINCT = -1;
   public static final int PATHS_EQUAL = 0;
   public static final int PATHS_SUBSET = 1;

   public JoinNode(JoinNode var1, String var2, RDBMSBean var3, String var4, String var5, int var6, boolean var7, boolean var8, String var9, QueryContext var10, List var11) {
      this.prevNode = var1;
      this.prevCMRField = var2;
      this.thisBean = var3;
      this.tableName = var4;
      this.tableAlias = var5;
      this.relationshipType = var6;
      this.isManyToMany = var7;
      this.isRemoteInterface = var8;
      this.tableAliasMN = var9;
      this.queryContext = var10;
      this.children = new HashMap();
      this.otherTableNameAliasMap = new HashMap();
      this.otherTableName2JoinSQL = new HashMap();
      this.joinColumns = var11;
   }

   public QueryContext getQueryContext() {
      return this.queryContext;
   }

   private int getDatabaseType() {
      return this.thisBean.getDatabaseType();
   }

   public void setDoLeftOuterJoin(boolean var1) throws IllegalExpressionException {
      int var2 = this.getDatabaseType();
      Loggable var3;
      if (var2 == 0) {
         var3 = EJBLogger.logCannotDoOuterJoinForUnspecifiedDBLoggable();
         throw new IllegalExpressionException(7, var3.getMessage());
      } else {
         if (this.isManyToMany && !this.isRemoteInterface) {
            if (!RDBMSUtils.dbSupportForMultiLeftOuterJoin(var2)) {
               var3 = EJBLogger.logCannotDoMultiOuterJoinForDBLoggable(this.prevNode.getRDBMSBean().getEjbName(), this.thisBean.getEjbName(), DDConstants.getDBNameForType(var2));
               throw new IllegalExpressionException(7, var3.getMessage());
            }
         } else if (!RDBMSUtils.dbSupportForSingleLeftOuterJoin(var2)) {
            var3 = EJBLogger.logCannotDoOuterJoinForDBLoggable(this.prevNode.getRDBMSBean().getEjbName(), this.thisBean.getEjbName(), DDConstants.getDBNameForType(var2));
            throw new IllegalExpressionException(7, var3.getMessage());
         }

         this.doLeftOuterJoin = var1;
      }
   }

   public boolean getDoLeftOuterJoin() {
      return this.doLeftOuterJoin;
   }

   public int getAllDoLeftOuterJoinCount() {
      int var1 = 0;
      if (this.doLeftOuterJoin) {
         if (this.isManyToMany) {
            var1 = 2;
         } else {
            var1 = 1;
         }
      }

      JoinNode var3;
      for(Iterator var2 = this.children.values().iterator(); var2.hasNext(); var1 += var3.getAllDoLeftOuterJoinCount()) {
         var3 = (JoinNode)var2.next();
      }

      return var1;
   }

   boolean isLeftOuterJoinANSIRoot() {
      if (!this.hasChildren()) {
         return false;
      } else if (this.isDoLeftOuterJoinANSI()) {
         return false;
      } else {
         Iterator var1 = this.getChildrenIterator();

         JoinNode var2;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            var2 = (JoinNode)var1.next();
         } while(!var2.isDoLeftOuterJoinANSI());

         return true;
      }
   }

   boolean isDoLeftOuterJoinANSI() {
      if (!this.getDoLeftOuterJoin()) {
         return false;
      } else {
         int var1 = this.getDatabaseType();
         switch (var1) {
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
   }

   public JoinNode getPrevNode() {
      return this.prevNode;
   }

   public String getPrevCMRField() {
      return this.prevCMRField;
   }

   public int getRelationshipType() {
      return this.relationshipType;
   }

   public boolean getIsManyToMany() {
      return this.isManyToMany;
   }

   public String getJoinTableAlias() {
      return this.tableAliasMN;
   }

   public String getTableName() {
      return this.tableName;
   }

   public String getTableAlias() {
      return this.tableAlias;
   }

   public void setTableAlias(String var1) {
      this.tableAlias = var1;
   }

   public List getTableAlias(List var1) {
      if (this.tableAlias.length() > 0) {
         var1.add(this.tableAlias);
      }

      if (this.isManyToMany) {
         var1.add(this.tableAliasMN);
      }

      Iterator var2 = this.otherTableNameAliasMap.values().iterator();

      while(var2.hasNext()) {
         var1.add(var2.next());
      }

      var2 = this.children.values().iterator();

      while(var2.hasNext()) {
         JoinNode var3 = (JoinNode)var2.next();
         var3.getTableAlias(var1);
      }

      return var1;
   }

   public boolean otherTableNameContains(String var1) {
      return this.otherTableNameAliasMap.containsKey(var1);
   }

   public String getAnyTableNameAlias(String var1) throws IllegalExpressionException {
      return var1.equals(this.tableName) ? this.tableAlias : this.getOtherTableNameAlias(var1);
   }

   public String getOtherTableNameAlias(String var1) throws IllegalExpressionException {
      if (var1.equals(this.tableName)) {
         throw new IllegalExpressionException(7, " Internal Error: attempt to getOtherTableNameAlias on default table: '" + var1 + "', this indicates an internal problem.  Should not be attempting to getOtherTableNameAlias " + "on the default JoinNode.  contact technical support");
      } else {
         String var2 = (String)this.otherTableNameAliasMap.get(var1);
         if (var2 != null) {
            return var2;
         } else if (!this.thisBean.hasTable(var1)) {
            throw new IllegalExpressionException(7, "Attempt to register Table '" + var1 + "'. for Bean: '" + this.thisBean.getEjbName() + "'  but that Table is not mapped to the Bean.");
         } else {
            var2 = this.registerTable(var1);
            this.otherTableNameAliasMap.put(var1, var2);
            return var2;
         }
      }
   }

   public boolean isExcluded(List var1) {
      if (var1 == null) {
         return false;
      } else if (var1.size() == 0) {
         return false;
      } else if (var1.contains(this.getTableAlias())) {
         return true;
      } else {
         return var1.contains(this.getJoinTableAlias());
      }
   }

   public boolean hasChild(String var1) {
      return this.children.containsKey(var1);
   }

   public JoinNode getChild(String var1) {
      return (JoinNode)this.children.get(var1);
   }

   public void putChild(String var1, JoinNode var2) {
      this.children.put(var1, var2);
   }

   boolean hasChildren() {
      return this.getChildren().keySet().size() > 0;
   }

   public Map getChildren() {
      return this.children;
   }

   Iterator getChildrenIterator() {
      return this.getChildren().values().iterator();
   }

   String getFROMDeclaration(List var1, int var2) throws IllegalExpressionException {
      StringBuffer var3 = new StringBuffer();
      if (this.getDatabaseType() == 1) {
         this.getOracleFROM(var1, var2, var3);
      } else {
         this.addANSIFROM(var1, var2, var3);
         this.addNonANSIFROM(var1, var2, var3);
      }

      return var3.toString();
   }

   private void addANSIFROM(List var1, int var2, StringBuffer var3) {
      if (!this.isExcluded(var1) && (this.isLeftOuterJoinANSIRoot() || this.isDoLeftOuterJoinANSI())) {
         String var4 = this.getTableAlias();
         if (var4 != null && var4.length() > 0) {
            String var5 = this.queryContext.getTableNameForAlias(var4);
            if (this.isLeftOuterJoinANSIRoot()) {
               this.addTableToFROM(var5, var4, var2, var3);
            } else {
               String var6 = this.getJoinTableAlias();
               if (var6 != null && var6.length() > 0) {
                  String var7 = this.queryContext.getTableNameForAlias(var6);
                  this.addANSIOuterJoinTableToFROM(var7, var6, var2, var3);
               }

               this.addANSIOuterJoinTableToFROM(var5, var4, var2, var3);
            }
         }
      }

      Iterator var8 = this.getChildrenIterator();

      while(var8.hasNext()) {
         JoinNode var9 = (JoinNode)var8.next();
         var9.addANSIFROM(var1, var2, var3);
      }

   }

   private void addNonANSIFROM(List var1, int var2, StringBuffer var3) {
      Iterator var7;
      if (!this.isExcluded(var1)) {
         String var5;
         String var6;
         if (!this.isLeftOuterJoinANSIRoot() && !this.isDoLeftOuterJoinANSI()) {
            String var4 = this.getTableAlias();
            if (var4 != null && var4.length() > 0) {
               var5 = this.queryContext.getTableNameForAlias(var4);
               this.addTableToFROM(var5, var4, var2, var3);
            }

            var5 = this.getJoinTableAlias();
            if (var5 != null && var5.length() > 0) {
               var6 = this.queryContext.getTableNameForAlias(var5);
               this.addTableToFROM(var6, var5, var2, var3);
            }
         }

         if (this.otherTableNameAliasMap.keySet().size() > 0) {
            var7 = this.otherTableNameAliasMap.keySet().iterator();

            while(var7.hasNext()) {
               var5 = (String)var7.next();
               var6 = (String)this.otherTableNameAliasMap.get(var5);
               this.addTableToFROM(var5, var6, var2, var3);
            }
         }
      }

      var7 = this.getChildrenIterator();

      while(var7.hasNext()) {
         JoinNode var8 = (JoinNode)var7.next();
         var8.addNonANSIFROM(var1, var2, var3);
      }

   }

   private void getOracleFROM(List var1, int var2, StringBuffer var3) throws IllegalExpressionException {
      List var4;
      try {
         var4 = getTableAliasList(this);
      } catch (Exception var8) {
         throw new IllegalExpressionException(7, var8.getMessage());
      }

      for(int var5 = var4.size() - 1; var5 >= 0; --var5) {
         String var6 = (String)var4.get(var5);
         String var7 = this.queryContext.getTableNameForAlias(var6);
         this.addTableToFROM(var7, var6, var2, var3);
      }

   }

   private void addTableToFROM(String var1, String var2, int var3, StringBuffer var4) {
      if (var4.length() > 0) {
         var4.append(", ");
      }

      var4.append(RDBMSUtils.escQuotedID(var1));
      var4.append(" ");
      var4.append(var2);
      var4.append(" ");
      var4.append(this.queryContext.getFROMClauseSelectForUpdate(var3));
   }

   private void addANSIOuterJoinTableToFROM(String var1, String var2, int var3, StringBuffer var4) {
      if (this.isUseInnerJoin()) {
         var4.append("INNER JOIN ");
      } else {
         var4.append("LEFT OUTER JOIN ");
      }

      var4.append(RDBMSUtils.escQuotedID(var1));
      var4.append(" ");
      var4.append(var2);
      var4.append(" ");
      var4.append(this.queryContext.getFROMClauseSelectForUpdate(var3));
      var4.append(" ON ");
      String var5 = "AND ";
      Iterator var6 = this.joinColumns.iterator();

      while(var6.hasNext()) {
         List var7 = (List)var6.next();
         Debug.assertion(var7.size() == 2, "Internal Error !!  For Prev Bean: '" + this.prevNode.getRDBMSBean().getEjbName() + "' and Bean: '" + this.thisBean.getEjbName() + "', we expect all joinColumn List elements to contain 2 Columns. " + "Instead we encountered one with '" + var7.size() + "' Columns !");
         Iterator var8 = var7.iterator();
         String var9 = (String)var8.next();
         String var10 = (String)var8.next();
         if (var10.startsWith(var2 + ".")) {
            var4.append(var9).append(" = ").append(var10).append(" ");
            if (var6.hasNext()) {
               var4.append(var5);
            }
         }
      }

      String var11 = var4.substring(var4.length() - var5.length());
      if (var11.equals(var5)) {
         var4.delete(var4.length() - var5.length(), var4.length());
      }

   }

   public void setJoinSQL(List var1) {
      this.joinColumns = var1;
   }

   public void addJoinSQL(List var1) {
      this.joinColumns.add(var1);
   }

   public String getJoinSQL() throws IllegalExpressionException {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.joinColumns.iterator();

      while(true) {
         int var7;
         do {
            do {
               if (!var2.hasNext()) {
                  if (this.prevNode != null && this.prevCMRField != null) {
                     String var8 = this.prevNode.getJoinSQLForCmrf(this.prevCMRField);
                     if (var8 != null) {
                        if (this.joinColumns.size() > 0) {
                           var1.append(" AND ");
                        }

                        var1.append(var8);
                     }
                  }

                  return var1.toString();
               }

               List var3 = (List)var2.next();
               if (var3.size() != 2) {
                  throw new IllegalExpressionException(7, "Internal Error !!  For Prev Bean: '" + this.prevNode.getRDBMSBean().getEjbName() + "' and Bean: '" + this.thisBean.getEjbName() + "', we expect all joinColumn List elements to contain 2 Columns. " + "Instead we encountered one with '" + var3.size() + "' Columns !");
               }

               Iterator var4 = var3.iterator();
               String var5 = (String)var4.next();
               String var6 = (String)var4.next();
               if (this.getDoLeftOuterJoin()) {
                  if (!this.isDoLeftOuterJoinANSI()) {
                     var7 = this.getDatabaseType();
                     switch (var7) {
                        case 1:
                           if (this.isUseInnerJoin()) {
                              var1.append(var6).append(" = ").append(var5);
                           } else {
                              var1.append(var6).append(" (+)= ").append(var5);
                           }
                           break;
                        case 2:
                        case 5:
                           var1.append(var5).append(" *= ").append(var6);
                           break;
                        case 3:
                        case 4:
                        default:
                           throw new IllegalExpressionException(7, "Internal Error !!  For Prev Bean: '" + this.prevNode.getRDBMSBean().getEjbName() + "' and Bean: '" + this.thisBean.getEjbName() + "', we were asked to do a Left Outer Join in the WHERE Clause for Database: '" + var7 + "', but we don't know how to do this !");
                     }
                  }
               } else {
                  var1.append(var5).append(" = ").append(var6);
               }
            } while(!var2.hasNext());

            var7 = this.getDatabaseType();
         } while(this.getDoLeftOuterJoin() && this.isDoLeftOuterJoinANSI() && (var7 == 4 || var7 == 9 || var7 == 7 || var7 == 8 || var7 == 6));

         var1.append(" AND ");
      }
   }

   public RDBMSBean getRDBMSBean() {
      return this.thisBean;
   }

   public void parseJoin(String var1) throws IllegalExpressionException {
      if (debugLogger.isDebugEnabled()) {
         debug("  parse pathExpression: " + var1);
      }

      if (var1.length() != 0) {
         String var2 = "";
         String var3 = null;
         String var4;
         if (var1.indexOf(".") == -1) {
            var4 = this.thisBean.getCmpColumnForField(var1);
            if (var4 == null) {
               var4 = this.thisBean.getCmpColumnForVariable(var1);
            }

            if (var4 != null) {
               return;
            }

            var3 = var1;
         } else {
            int var29 = var1.indexOf(".");
            var3 = var1.substring(0, var29);
            var2 = var1.substring(var29 + 1);
         }

         var4 = null;
         Object var5 = null;
         if (this.children.containsKey(var3)) {
            JoinNode var30 = (JoinNode)this.children.get(var3);
            var30.parseJoin(var2);
         } else {
            StringBuffer var6 = new StringBuffer();
            RDBMSBean var8 = this.thisBean;
            String var10;
            String var11;
            String var17;
            String var18;
            if (var8.isRemoteField(var3)) {
               Loggable var32;
               if (var2.length() > 0) {
                  var32 = EJBLogger.logfinderTerminalCMRNotRemoteLoggable(var3, var8.getEjbName());
                  throw new IllegalExpressionException(7, var32.getMessage());
               } else if (var8.containsFkField(var3)) {
                  if (debugLogger.isDebugEnabled()) {
                     debug(" Bean contains FK Field. NO Join Node For Bean Type: Remote Interface. ");
                  }

               } else {
                  if (debugLogger.isDebugEnabled()) {
                     debug(" Doing join table JOIN for Bean: " + var8.getEjbName() + ", cmr-field: " + var3 + " to Remote Interface ");
                  }

                  if (!var8.isForeignKeyField(var3)) {
                     var32 = EJBLogger.logfinderCouldNotGetFKColumnsLoggable(var8.getEjbName(), var3, "JOIN Calculation");
                     throw new IllegalExpressionException(7, var32.getMessage());
                  } else {
                     String var31 = this.getFKTableAliasAndSQLForCmrf(var3);
                     var10 = var8.getJoinTableName(var3);
                     var11 = this.registerTable(var10);
                     Map var34 = var8.getColumnMapForCmrfAndPkTable(var3, var10);
                     if (var34 == null) {
                        throw new IllegalExpressionException(7, " could not find Map of foreign keys and primary keys table for Bean: " + var8.getEjbName() + "  Remote cmr-field: " + var3);
                     } else {
                        ArrayList var35 = new ArrayList();
                        Iterator var38 = var34.keySet().iterator();

                        while(var38.hasNext()) {
                           String var37 = (String)var38.next();
                           String var40 = (String)var34.get(var37);
                           var17 = var31 + "." + RDBMSUtils.escQuotedID(var40);
                           var18 = var11 + "." + RDBMSUtils.escQuotedID(var37);
                           var35.add(createJoinListEntry(var17, var18));
                        }

                        if (debugLogger.isDebugEnabled()) {
                           debug(" add new join table Join Node for Remote Interface with JOIN: " + var6.toString());
                        }

                        JoinNode var39 = this.newJoinNode(this, var3, (RDBMSBean)null, var10, "", 8, true, true, var11, var35);
                        this.children.put(var3, var39);
                     }
                  }
               }
            } else {
               RDBMSBean var9 = var8.getRelatedRDBMSBean(var3);
               if (var9 == null) {
                  Loggable var33 = EJBLogger.logejbqlIdNotFieldAndNotBeanLoggable(var3);
                  throw new IllegalExpressionException(7, var33.getMessage());
               } else {
                  String var12;
                  String var13;
                  Map var14;
                  ArrayList var15;
                  Iterator var16;
                  String var19;
                  String var20;
                  String var23;
                  if (var8.isManyToManyRelation(var3)) {
                     if (debugLogger.isDebugEnabled()) {
                        debug(" Doing Many-to-Many JOIN for Bean: " + var8.getEjbName() + ", cmr-field: " + var3);
                     }

                     var10 = var8.getJoinTableName(var3);
                     var11 = this.registerTable(var10);
                     if (debugLogger.isDebugEnabled()) {
                        debug(" cmrfield: " + var3 + ", joinTable name: " + var10 + ", joinTableAlias: " + var11);
                     }

                     if (debugLogger.isDebugEnabled()) {
                        debug("processing symmetric field: '" + var3 + "'");
                     }

                     var12 = this.getTableName();
                     var13 = this.getTableAlias();
                     if (debugLogger.isDebugEnabled()) {
                        debug(" the LHS Table is: '" + var12 + "',  with Alias: '" + var13 + "'");
                     }

                     if (var8.isSymmetricField(var3)) {
                        if (debugLogger.isDebugEnabled()) {
                           debug("processing     symmetric field: '" + var3 + "'");
                        }

                        var14 = var8.getSymColumnMapForCmrfAndPkTable(var3, var12);
                     } else {
                        if (debugLogger.isDebugEnabled()) {
                           debug("processing non-symmetric field: '" + var3 + "'");
                        }

                        if (!var8.isForeignKeyField(var3)) {
                           Loggable var36 = EJBLogger.logfinderCMRFieldNotFKLoggable(var8.getEjbName(), var3, "M-N Relationship JOIN Calculation");
                           throw new IllegalExpressionException(7, var36.getMessage());
                        }

                        var14 = var8.getColumnMapForCmrfAndPkTable(var3, this.tableName);
                     }

                     if (var14 == null) {
                        throw new IllegalExpressionException(7, " could not find Map of foreign keys and primary keys table for EJB: '" + var8.getEjbName() + "'  cmr-field: '" + var3 + "'   dest PK Table: '" + var12 + "'  Join Table Name: '" + var10 + "'.   Please check your RDBMS Deployment Descriptors for this EJB.");
                     }

                     var15 = new ArrayList();
                     var16 = var14.keySet().iterator();

                     while(var16.hasNext()) {
                        var17 = (String)var16.next();
                        var18 = (String)var14.get(var17);
                        var19 = var13 + "." + RDBMSUtils.escQuotedID(var18);
                        var20 = var11 + "." + RDBMSUtils.escQuotedID(var17);
                        var15.add(createJoinListEntry(var19, var20));
                     }

                     if (debugLogger.isDebugEnabled()) {
                        debug(" joinSQL after 'from' side processed  '" + var6.toString() + "'");
                     }

                     var17 = RDBMSUtils.escQuotedID(var9.chooseTableAsJoinTarget());
                     var18 = this.registerTable(var17);
                     var19 = var8.getRelatedFieldName(var3);
                     if (debugLogger.isDebugEnabled()) {
                        debug(" processing other side of M-N join:  for RHS Bean '" + var9.getEjbName() + "',  we've chosen RHS Table: '" + var17 + "'  with TableAlias: '" + var18 + "'");
                     }

                     var14 = var9.getColumnMapForCmrfAndPkTable(var19, var17);
                     if (var14 == null) {
                        throw new IllegalExpressionException(7, " could not find Map of foreign keys and primary keys table for Bean: '" + var9.getEjbName() + "'   dest PK Table: '" + var10 + "'   cmr-field: '" + var19 + "'.   Please check your RDBMS Deployment Descriptors for this EJB.");
                     }

                     var16 = var14.keySet().iterator();
                     var6.append(" AND ");

                     while(var16.hasNext()) {
                        var20 = (String)var16.next();
                        String var21 = (String)var14.get(var20);
                        String var22 = var11 + "." + RDBMSUtils.escQuotedID(var20);
                        var23 = var18 + "." + RDBMSUtils.escQuotedID(var21);
                        var15.add(createJoinListEntry(var22, var23));
                     }

                     if (debugLogger.isDebugEnabled()) {
                        debug(" add new Many-to-Many Join Node with JOIN: " + var6.toString());
                     }

                     JoinNode var41 = this.newJoinNode(this, var3, var9, var17, var18, 6, true, false, var11, var15);
                     this.children.put(var3, var41);
                     if (var2.length() > 0) {
                        var41.parseJoin(var2);
                     }
                  } else {
                     var10 = this.tableAlias;
                     var11 = this.tableName;
                     var15 = null;
                     var16 = null;
                     var17 = null;
                     var18 = null;
                     var19 = null;
                     var20 = null;
                     boolean var42 = var8.isForeignKeyField(var3);
                     byte var43;
                     if (var42) {
                        if (debugLogger.isDebugEnabled()) {
                           debug("\n\n parseJoin 1-N or 1-1.  prevHasFK.  about to call getFKTableAliasAndSQLForCmrf on cmrfield: '" + var3 + "'\n\n");
                        }

                        var23 = this.getFKTableAliasAndSQLForCmrf(var3);
                        var12 = RDBMSUtils.escQuotedID(var9.chooseTableAsJoinTarget());
                        var13 = this.registerTable(var12);
                        var20 = var13;
                        var17 = var23;
                        var14 = var8.getColumnMapForCmrfAndPkTable(var3, var12);
                        if (var8.isOneToManyRelation(var3)) {
                           var43 = 5;
                        } else {
                           var43 = 2;
                        }
                     } else {
                        var18 = var8.getRelatedFieldName(var3);
                        if (var18.length() < 1) {
                           throw new IllegalExpressionException(7, "Could not find cmr-field in Bean: " + var9.getEjbName() + " that points to EJBean: " + var8.getEjbName());
                        }

                        var12 = var9.getTableForCmrField(var18);
                        var13 = this.registerTable(var12);
                        var20 = var10;
                        var17 = var13;
                        var14 = var9.getColumnMapForCmrfAndPkTable(var18, this.tableName);
                        if (var8.isOneToManyRelation(var3)) {
                           var43 = 4;
                        } else {
                           var43 = 3;
                        }
                     }

                     var16 = var14.keySet().iterator();
                     if (var16.hasNext() && var6.length() > 0) {
                        var6.append(" AND ");
                     }

                     ArrayList var44 = new ArrayList();

                     while(var16.hasNext()) {
                        int var24 = var9.getDatabaseType();
                        String var25 = (String)var16.next();
                        String var26 = (String)var14.get(var25);
                        String var27 = var17 + "." + RDBMSUtils.escQuotedID(var25);
                        String var28 = var20 + "." + RDBMSUtils.escQuotedID(var26);
                        if (var42) {
                           var44.add(createJoinListEntry(var27, var28));
                        } else {
                           var44.add(createJoinListEntry(var28, var27));
                        }
                     }

                     if (debugLogger.isDebugEnabled()) {
                        debug(" add new Join Node with JOIN: " + var6.toString());
                     }

                     JoinNode var45 = this.newJoinNode(this, var3, var9, var12, var13, var43, false, false, "", var44);
                     if (debugLogger.isDebugEnabled()) {
                        debug(" add new JoinNode to children ");
                     }

                     this.children.put(var3, var45);
                     if (this.queryContext.mainQueryContainsInSelectListForCachingElement(var8, var9)) {
                        var45.setDoLeftOuterJoin(true);
                     }

                     if (var2.length() > 0) {
                        var45.parseJoin(var2);
                     }
                  }

               }
            }
         }
      }
   }

   public String getFKTableAliasAndSQLForCmrf(String var1) throws IllegalExpressionException {
      String var2 = this.thisBean.getTableForCmrField(var1);
      if (var2 == null) {
         throw new IllegalExpressionException(7, " could not find foreign key table for Bean: " + this.thisBean.getEjbName() + "  cmr-field: " + var1);
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("  in getFKTableAliasAndSQLForCmrf  for cmrField '" + var1 + "',  theFKTable is '" + var2 + "',  default TableName is: '" + this.tableName + "'");
         }

         String var3;
         if (!var2.equals(this.tableName)) {
            var3 = this.getOtherTableNameAlias(var2);
            this.getInnerBeanJoinSQLMaybe(var2);
         } else {
            var3 = this.tableAlias;
         }

         return var3;
      }
   }

   private String getInnerBeanJoinSQLMaybe(String var1) throws IllegalExpressionException {
      return this.tableName.equals(var1) ? null : this.getInnerBeanJoinSQL(var1);
   }

   private String getInnerBeanJoinSQL(String var1) throws IllegalExpressionException {
      String var2 = (String)this.otherTableName2JoinSQL.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         StringBuffer var3 = new StringBuffer();
         String var4 = this.getOtherTableNameAlias(var1);
         Map var5 = this.thisBean.getPKCmpf2ColumnForTable(this.tableName);
         if (var5 == null) {
            throw new IllegalExpressionException(7, "Internal Error in getInnerBeanJoinSQL, thisTablePKColMap:  Bean: '" + this.thisBean.getEjbName() + "', could not get Primary Key Field to Column Map for Table: '" + this.tableName + "'");
         } else {
            Map var6 = this.thisBean.getPKCmpf2ColumnForTable(var1);
            if (var6 == null) {
               throw new IllegalExpressionException(7, "Internal Errorin getInnerBeanJoinSQL, destTablePKColMap:  Bean: '" + this.thisBean.getEjbName() + "', could not get Primary Key Field to Column Map for Table: '" + var1 + "'");
            } else {
               Iterator var7 = var5.keySet().iterator();

               while(var7.hasNext()) {
                  String var8 = (String)var7.next();
                  String var9 = (String)var5.get(var8);
                  String var10 = (String)var6.get(var8);
                  var3.append(this.tableAlias + "." + RDBMSUtils.escQuotedID(var9) + " = " + var4 + "." + RDBMSUtils.escQuotedID(var10));
                  if (var7.hasNext()) {
                     var3.append(" AND ");
                  }
               }

               var2 = var3.toString();
               this.otherTableName2JoinSQL.put(var1, var2);
               return var2;
            }
         }
      }
   }

   public String getJoinSQLForCmrf(String var1) {
      String var2 = this.thisBean.getTableForCmrField(var1);
      if (var2 == null) {
         return null;
      } else {
         String var3 = (String)this.otherTableName2JoinSQL.get(var2);
         return var3;
      }
   }

   void forceInternalMultiTableJoinMaybe(QueryNode var1, String var2) throws IllegalExpressionException {
      if (!this.tableName.equals(var2)) {
         this.forceInternalMultiTableJoin(var1, var2);
      }
   }

   void forceInternalMultiTableJoin(QueryNode var1, String var2) throws IllegalExpressionException {
      String var3 = this.getTableAlias();
      String var4 = this.getOtherTableNameAlias(var2);
      String var5 = this.getInnerBeanJoinSQLMaybe(var2);
      if (var5 != null) {
         var1.addCmpFieldJoinSQL(var3, var4, var5);
      }

   }

   private String registerTable(String var1) {
      return this.queryContext.registerTable(var1);
   }

   public static String getPathWithoutTrailingCmpField(JoinNode var0, String var1) throws IllegalExpressionException {
      if (var1 == null) {
         return "";
      } else {
         var0.parseJoin(var1);
         if (!endsInField(var0, var1)) {
            return var1;
         } else {
            int var2 = var1.lastIndexOf(".");
            if (var2 == -1) {
               throw new IllegalExpressionException(7, "Path Expression: '" + var1 + "' is a cmp-field without " + "a Range Variable or cmr-field pointing to it.  " + "cmp-fields alone are not allowed in EJB QL Queries, " + "they must be qualified.  Please re-examine your query.");
            } else {
               return var1.substring(0, var2);
            }
         }
      }
   }

   public static int getRelationshipTypeForPathExpression(QueryContext var0, JoinNode var1, String var2) throws IllegalExpressionException {
      String var3 = var2;
      if (var0 != null) {
         var3 = var0.replaceIdAliases(var2);
      }

      var1.parseJoin(var2);
      int var4 = countPathNodes(var3);
      if (var4 < 1) {
         return -1;
      } else if (var4 == 1) {
         return 1;
      } else if (endsInField(var1, var3)) {
         return 0;
      } else {
         JoinNode var5 = getNode(var1, var3);
         return var5.getRelationshipType();
      }
   }

   public static int getRelationshipTypeForPathExpressionWithNoSQLGen(QueryContext var0, JoinNode var1, String var2) throws IllegalExpressionException {
      JoinNode var3 = var0.makeTrialJoinRoot(var1, var2);
      return getRelationshipTypeForPathExpression(var0, var3, var2);
   }

   public static RDBMSBean getLastRDBMSBeanForPathExpressionWithNoSQLGen(QueryContext var0, JoinNode var1, String var2) throws IllegalExpressionException {
      JoinNode var3 = var0.makeTrialJoinRoot(var1, var2);
      var3.parseJoin(var2);
      JoinNode var4 = getNode(var3, var2);
      return var4.getRDBMSBean();
   }

   public static boolean endsInField(JoinNode var0, String var1) throws IllegalExpressionException {
      JoinNode var2 = var0;
      String var3 = "";

      for(StringTokenizer var4 = new StringTokenizer(var1, "."); var4.hasMoreTokens(); var2 = var2.getChild(var3)) {
         var3 = (String)var4.nextElement();
         if (!var2.hasChild(var3)) {
            RDBMSBean var5 = var2.getRDBMSBean();
            if (var5.isRemoteField(var3)) {
               return false;
            }

            String var6 = var5.getCmpColumnForField(var3);
            if (var6 == null) {
               var6 = var5.getCmpColumnForVariable(var3);
            }

            if (var6 != null) {
               if (var4.hasMoreTokens()) {
                  throw new IllegalExpressionException(7, "called endsInField on a pathExpression with an embedded field: " + var1);
               }

               return true;
            }

            if (var5.getCmrFieldNames().contains(var3)) {
               return false;
            }

            throw new IllegalExpressionException(7, "called endsInField on a pathExpression with field: '" + var3 + "' that's neither Field nor Bean: " + var1);
         }
      }

      return false;
   }

   public static boolean endsInLocalRelationship(JoinNode var0, String var1) throws IllegalExpressionException {
      if (endsInRemoteInterface(var0, var1)) {
         return false;
      } else if (endsInField(var0, var1)) {
         return false;
      } else {
         JoinNode var2 = var0;
         String var3 = getLastFieldFromId(var1);
         StringTokenizer var4 = new StringTokenizer(var1, ".");

         while(var4.hasMoreTokens()) {
            String var5 = (String)var4.nextElement();
            if (var2.hasChild(var5)) {
               if (var3.equals(var5)) {
                  RDBMSBean var6 = var2.getRDBMSBean();
                  if (var6.getCmrFieldNames().contains(var3)) {
                     return true;
                  }

                  throw new IllegalExpressionException(7, "unable to determine if  pathExpression '" + var1 + "' is terminated by a Remote Relationship, a cmp-field or a Local Relationship. " + "   It appears to be none of them.");
               }

               var2 = var2.getChild(var5);
            }
         }

         throw new IllegalExpressionException(7, "unable to determine if  pathExpression '" + var1 + "' is terminated by a Remote Relationship, a cmp-field or a Local Relationship. " + "   It appears to be none of them.    Out of Tokens.");
      }
   }

   public static boolean endsInRemoteInterface(JoinNode var0, String var1) throws IllegalExpressionException {
      try {
         if (endsInField(var0, var1)) {
            return false;
         }
      } catch (IllegalExpressionException var7) {
      }

      JoinNode var2 = var0;
      String var3 = "";

      for(StringTokenizer var4 = new StringTokenizer(var1, "."); var4.hasMoreTokens(); var2 = var2.getChild(var3)) {
         var3 = (String)var4.nextElement();
         if (!var2.hasChild(var3) || !var4.hasMoreTokens()) {
            RDBMSBean var5 = var2.getRDBMSBean();
            String var6 = var5.getCmpColumnForField(var3);
            if (var6 == null) {
               var6 = var5.getCmpColumnForVariable(var3);
            }

            if (var6 != null) {
               return false;
            } else if (var5.isRemoteField(var3)) {
               if (var4.hasMoreTokens()) {
                  throw new IllegalExpressionException(7, "called endsInRemoteInterface on a pathExpression.  The Remote Interface is NOT the last field in the Path as defined in the EJB 2.0 public draft  spec  section 10.2.4.6: " + var1);
               } else {
                  return true;
               }
            } else if (var4.hasMoreTokens()) {
               throw new IllegalExpressionException(7, "called endsInRemoteInterface on an unparsed pathExpression: " + var1);
            } else {
               return false;
            }
         }
      }

      return false;
   }

   public static void getJoinSQLForPath(JoinNode var0, String var1, List var2, StringBuffer var3) throws IllegalExpressionException {
      boolean var4 = false;
      String var6 = "";
      if (var1.length() != 0) {
         StringTokenizer var7 = new StringTokenizer(var1, ".");
         var6 = (String)var7.nextElement();
         JoinNode var5 = var0.getChild(var6);
         if (var5 == null) {
            throw new IllegalExpressionException(7, "Internal Error in JoinNode.getJoinSQLForPath, root node missing for path: '" + var1 + "'");
         } else {
            while(var7.hasMoreTokens()) {
               var6 = (String)var7.nextElement();
               if (!var5.hasChild(var6)) {
                  break;
               }

               var5 = var5.getChild(var6);
               boolean var8 = false;
               if (var2 != null) {
                  label55: {
                     Iterator var9 = var2.iterator();

                     String var10;
                     do {
                        if (!var9.hasNext()) {
                           break label55;
                        }

                        var10 = (String)var9.next();
                     } while(var10.compareTo(var5.getTableAlias()) != 0 && var10.compareTo(var5.getJoinTableAlias()) != 0);

                     var8 = true;
                  }
               }

               if (var8) {
                  break;
               }

               String var11 = var5.getJoinSQL();
               if (var11.length() > 0) {
                  if (var4) {
                     var3.append(" AND ");
                  }

                  var3.append(var11);
                  var4 = true;
               }
            }

         }
      }
   }

   public static String getTableAndField(QueryNode var0, JoinNode var1, String var2) throws IllegalExpressionException {
      if (debugLogger.isDebugEnabled()) {
         debug(" called getTableAndField on pathExpression: '" + var2 + "'");
      }

      String var4;
      if (!endsInRemoteInterface(var1, var2)) {
         Loggable var13 = EJBLogger.logfinderPathEndsInXNotYLoggable("Bean", "Field");
         if (!endsInField(var1, var2)) {
            throw new IllegalExpressionException(7, "JoinNode.getTableAndField " + var13.getMessage());
         } else {
            if (debugLogger.isDebugEnabled()) {
               debug("  processing local cmp-field ");
            }

            var4 = null;
            String var14 = "";
            JoinNode var16 = var1;

            for(StringTokenizer var17 = new StringTokenizer(var2, "."); var17.hasMoreTokens(); var16 = var16.getChild(var14)) {
               var14 = (String)var17.nextElement();
               if (!var16.hasChild(var14)) {
                  return writeTableAndFieldForLocalCmrf(var0, var16, var14);
               }
            }

            return writeTableAndFieldForLocalCmrf(var0, var16, var14);
         }
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug("  processing RemoteInterface ");
         }

         JoinNode var3 = var1;
         var4 = "";

         for(StringTokenizer var5 = new StringTokenizer(var2, "."); var5.hasMoreTokens(); var3 = var3.getChild(var4)) {
            var4 = (String)var5.nextElement();
            if (!var3.hasChild(var4) || !var5.hasMoreTokens()) {
               RDBMSBean var6 = var3.getRDBMSBean();
               if (!var6.isRemoteField(var4)) {
                  throw new IllegalExpressionException(7, "called getTableAndField on a pathExpression for a Remote Interface but the <cmr-field> " + var4 + " seems not to be pointing to a Remote Field. " + var2);
               } else {
                  List var7 = var6.getForeignKeyColNames(var4);
                  if (var7 == null) {
                     throw new IllegalExpressionException(7, "called getTableAndField on a pathExpression for a Remote Interface but the <cmr-field> " + var4 + " from Bean " + var6.getEjbName() + " does not point to Foreign Key Column Information. " + var2);
                  } else {
                     Iterator var8 = var6.getForeignKeyColNames(var4).iterator();
                     if (!var8.hasNext()) {
                        Loggable var19 = EJBLogger.logfinderCouldNotGetFKColumnsLoggable(var6.getEjbName(), var4, "JoinNode.getTableAndField on Remote Interface: " + var2);
                        throw new IllegalExpressionException(7, var19.getMessage());
                     } else if (var6.containsFkField(var4)) {
                        String var18 = (String)var8.next();
                        StringBuffer var20 = new StringBuffer();
                        var20.append(var3.getTableAlias());
                        var20.append(".");
                        var20.append(RDBMSUtils.escQuotedID(var18));
                        return var20.toString();
                     } else {
                        Map var9 = var6.getCmpFieldToColumnMap();
                        String var10 = (String)var8.next();
                        String var11 = (String)var9.get(var6.getRelatedPkFieldName(var4, var10));
                        StringBuffer var12 = new StringBuffer();
                        var3 = var3.getChild(var4);
                        if (var3 == null) {
                           throw new IllegalExpressionException(7, "called getTableAndField on a pathExpression for a Remote Interface but the <cmr-field> " + var4 + " from Bean " + var6.getEjbName() + "  did not yield an expected JoinNode which is " + "required to get the JoinTable info. " + " Path Expression: '" + var2 + "'");
                        } else {
                           var12.append(var3.getJoinTableAlias());
                           var12.append(".");
                           var12.append(RDBMSUtils.escQuotedID(var6.getRemoteColumn(var4)));
                           return var12.toString();
                        }
                     }
                  }
               }
            }
         }

         Loggable var15 = EJBLogger.logfinderCouldNotGetTableAndFieldLoggable(var2);
         throw new IllegalExpressionException(7, "JoinNode processing: " + var15.getMessage());
      }
   }

   private static String writeTableAndFieldForLocalCmrf(QueryNode var0, JoinNode var1, String var2) throws IllegalExpressionException {
      RDBMSBean var3 = var1.getRDBMSBean();
      if (debugLogger.isDebugEnabled()) {
         debug(" writeTableAndFieldForLocalCmrf got RDBMSBean for '" + var3.getEjbName() + "'");
      }

      String var4 = var1.getTableName();
      String var6 = "";
      String var7 = null;
      String var5;
      if (var3.isPrimaryKeyField(var2)) {
         if (debugLogger.isDebugEnabled()) {
            debug(" processing PK field: '" + var2 + "'");
         }

         var6 = var1.getTableAlias();
         if (debugLogger.isDebugEnabled()) {
            debug(" defaultTableName is: '" + var4 + "' tableAlias is: '" + var6 + "'");
         }

         var7 = var3.getColumnForCmpFieldAndTable(var2, var4);
      } else {
         if (debugLogger.isDebugEnabled()) {
            debug(" processing non-PK field: '" + var2 + "'");
         }

         var5 = var3.getTableForCmpField(var2);
         if (debugLogger.isDebugEnabled()) {
            debug(" Table Name that contains non-PK field: '" + var2 + "' is '" + var5 + "'");
         }

         if (var5 != null) {
            var7 = var3.getColumnForCmpFieldAndTable(var2, var5);
            if (var7 != null) {
               if (!var5.equals(var4)) {
                  var6 = var1.getOtherTableNameAlias(var5);
                  var1.forceInternalMultiTableJoin(var0, var5);
               } else {
                  var6 = var1.getTableAlias();
               }
            }
         }
      }

      if (var7 == null) {
         if (debugLogger.isDebugEnabled()) {
            debug(" processing synthesized cmrfield variableName field: '" + var2 + "'");
         }

         var7 = var3.getCmpColumnForVariable(var2);
         if (var7 == null) {
            throw new IllegalExpressionException(7, "Error.  For Bean: '" + var3.getEjbName() + "' could not find a Foreign Key Column Name for the field: '" + var2 + "' check your RDBMS Deployment Descriptors for Errors or Omissions.");
         }

         var5 = var3.getTableForVariable(var2);
         if (debugLogger.isDebugEnabled()) {
            debug(" column name for cmrfield variableName field: '" + var2 + "', is: '" + var7 + "'");
            debug(" table  name for cmrfield variableName field: '" + var2 + "', is: '" + var5 + "'");
         }

         if (var5 == null) {
            throw new IllegalExpressionException(7, "Internal Error.  For Bean: '" + var3.getEjbName() + "' could not get Foreign Key Table Name for variable name: '" + var7 + "'.");
         }

         if (!var5.equals(var4)) {
            var6 = var1.getOtherTableNameAlias(var5);
            var1.forceInternalMultiTableJoin(var0, var5);
         } else {
            var6 = var1.getTableAlias();
         }
      }

      if (var7 != null) {
         StringBuffer var8 = new StringBuffer();
         var8.append(var6);
         var8.append(".");
         var8.append(RDBMSUtils.escQuotedID(var7));
         return var8.toString();
      } else {
         throw new IllegalExpressionException(7, "called getTableAndField on an unparsed pathExpression ending in: '" + var2 + "'");
      }
   }

   private static boolean lhsBeanHasFKForLocal11or1NPath(QueryNode var0, String var1) throws IllegalExpressionException {
      if (debugLogger.isDebugEnabled()) {
         debug(" lhsBeanHasFKForLocal11or1NPath  for path: '" + var1 + "'");
      }

      String var2 = chopLastFieldFromId(var1);
      String var3 = getLastFieldFromId(var1);
      JoinNode var4 = var0.getJoinTree();

      String var6;
      for(StringTokenizer var5 = new StringTokenizer(var2, "."); var5.hasMoreTokens(); var4 = var4.getChild(var6)) {
         var6 = (String)var5.nextElement();
         if (!var4.hasChild(var6)) {
            throw new IllegalExpressionException(7, "Internal Error.  lhsBeanHasFKFor11or1NPath:  Attempt to get CMR Table and Foreign Key Columns for path: '" + var2 + ",  cmr-field: '" + var3 + "'.  Could not traverse the JoinTree for the path: '" + var2 + "'.  It's possible that this path has not been previously parsed.");
         }
      }

      RDBMSBean var7 = var4.getRDBMSBean();
      if (var7.isRemoteField(var3)) {
         throw new IllegalExpressionException(7, "Internal Error.  lhsBeanHasFKFor11or1NPath:  Attempt to get CMR Table and Foreign Key Columns for path: '" + var2 + ",  cmr-field: '" + var3 + "'.  This looks like a Remote Relationship.  This method should " + "only be called on 1-1 or 1-N Local Relationships.");
      } else if (var7.isManyToManyRelation(var3)) {
         throw new IllegalExpressionException(7, "Internal Error.  lhsBeanHasFKFor11or1NPath:  Attempt to get CMR Table and Foreign Key Columns for path: '" + var2 + ",  cmr-field: '" + var3 + "'.  This looks like a Many To Many Relationship.  This method should " + "only be called on 1-1 or 1-N Local Relationships.");
      } else {
         return var7.isForeignKeyField(var3);
      }
   }

   public static List getTableAndFKColumnListForLocal11or1NPath(QueryNode var0, String var1) throws IllegalExpressionException {
      if (debugLogger.isDebugEnabled()) {
         debug("  getTableAndFKColumnListForLocal11or1NPath for path: '" + var1 + "'");
      }

      return lhsBeanHasFKForLocal11or1NPath(var0, var1) ? getTableAndFKColumnListForLocal11or1NPathFKonLHS(var0, var1) : getTableAndFKColumnListForLocal11or1NPathFKonRHS(var0, var1);
   }

   private static List getTableAndFKColumnListForLocal11or1NPathFKonLHS(QueryNode var0, String var1) throws IllegalExpressionException {
      String var2 = "JoinNode.getTableAndFKColumnListForLocal11or1NForPathFKonLHS";
      if (debugLogger.isDebugEnabled()) {
         debug(var2 + "  for path: '" + var1 + "'");
      }

      String var3 = chopLastFieldFromId(var1);
      String var4 = getLastFieldFromId(var1);
      JoinNode var5 = var0.getJoinTree();

      String var7;
      for(StringTokenizer var6 = new StringTokenizer(var3, "."); var6.hasMoreTokens(); var5 = var5.getChild(var7)) {
         var7 = (String)var6.nextElement();
         if (!var5.hasChild(var7)) {
            throw new IllegalExpressionException(7, "Internal Error.  " + var2 + ": " + "Attempt to get CMR Table and Foreign Key Columns for " + "path: '" + var3 + ",  cmr-field: '" + var4 + "'.  Could not traverse the JoinTree for the path: '" + var3 + "'.  It's possible that this path has not been previously parsed.");
         }
      }

      RDBMSBean var14 = var5.getRDBMSBean();
      String var9 = var14.getTableForCmrField(var4);
      if (var9 == null) {
         throw new IllegalExpressionException(7, "Internal Error.  " + var2 + ": " + "Could not get Table Name that contains the Foreign Keys for Bean: '" + var14.getEjbName() + "'  and cmr-field: '" + var4 + "'");
      } else {
         String var10 = var5.getTableName();
         String var8;
         if (!var9.equals(var10)) {
            var8 = var5.getOtherTableNameAlias(var9);
            var5.forceInternalMultiTableJoin(var0, var9);
         } else {
            var8 = var5.getTableAlias();
         }

         ArrayList var11 = new ArrayList();

         String var13;
         for(Iterator var12 = var14.getForeignKeyColNames(var4).iterator(); var12.hasNext(); var11.add(var8 + "." + var13)) {
            var13 = (String)var12.next();
            if (debugLogger.isDebugEnabled()) {
               debug(var2 + " adding entry: '" + var8 + "." + var13);
            }
         }

         return var11;
      }
   }

   private static List getTableAndFKColumnListForLocal11or1NPathFKonRHS(QueryNode var0, String var1) throws IllegalExpressionException {
      String var2 = "JoinNode.getTableAndFKColumnListForLocal11or1NForPathFKonRHS";
      if (debugLogger.isDebugEnabled()) {
         debug(var2 + " for path: '" + var1 + "'");
      }

      if (var1.indexOf(".") == -1) {
         throw new IllegalExpressionException(7, "Internal Error.  " + var2 + ": " + " the input pathExpression is required to represent at least 1 Relationship (between 2 Beans), " + "the pathExpression: '" + var1 + "' apparently does not.");
      } else {
         JoinNode var3 = var0.getJoinTree();
         var3.parseJoin(var1);
         String var4 = getLastFieldFromId(var1);
         String var5 = chopLastFieldFromId(var1);
         if (var5.length() < 1) {
            throw new IllegalExpressionException(7, "Internal Error.  " + var2 + ": " + " the input pathExpression is required to represent at least 1 Relationship (between 2 Beans), " + "the pathExpression: '" + var1 + "' apparently does not.");
         } else {
            JoinNode var6 = getNode(var3, var5);
            RDBMSBean var7 = var6.getRDBMSBean();
            JoinNode var8 = getNode(var3, var1);
            RDBMSBean var9 = var8.getRDBMSBean();
            String var10 = var7.getRelatedFieldName(var4);
            if (var10 == null) {
               throw new IllegalExpressionException(7, "Internal Error.  " + var2 + ": " + " for path: '" + var1 + "', for the cmr-field: '" + var4 + "', we could not get the RelatedFieldName from the Bean represented by the cmr-field: '" + var4 + "', that points back to the Previous Bean. ");
            } else {
               String var11 = var9.getTableForCmrField(var10);
               if (var11 == null) {
                  throw new IllegalExpressionException(7, "Internal Error.  " + var2 + ": " + " for path: '" + var1 + "', for the cmr-field: '" + var4 + "', we could not get the name of the Table on Bean: '" + var9.getEjbName() + "' that holds the Foreign Key Columns for the Relationship.  " + "RelatedFieldName used for lookup was: '" + var10 + "'");
               } else {
                  List var12 = var9.getForeignKeyColNames(var10);
                  if (var12 == null) {
                     throw new IllegalExpressionException(7, "Internal Error.  " + var2 + ": " + " for path: '" + var1 + "', for the cmr-field: '" + var4 + "', for Bean: '" + var9.getEjbName() + "' that holds the Foreign Key Columns for the Relationship.  " + "We could not get the Foreign Key Column Map for: RelatedFieldName '" + var10 + "'");
                  } else {
                     String var13 = var8.getAnyTableNameAlias(var11);
                     ArrayList var14 = new ArrayList();

                     String var16;
                     for(Iterator var15 = var12.iterator(); var15.hasNext(); var14.add(var13 + "." + var16)) {
                        var16 = (String)var15.next();
                        if (debugLogger.isDebugEnabled()) {
                           debug(var2 + " adding entry: '" + var13 + "." + var16);
                        }
                     }

                     return var14;
                  }
               }
            }
         }
      }
   }

   public static int comparePaths(String var0, String var1) {
      if (var0 != null && var1 != null) {
         StringTokenizer var4 = new StringTokenizer(var0, ".");
         StringTokenizer var5 = new StringTokenizer(var1, ".");
         int var6 = var4.countTokens();
         int var7 = var5.countTokens();
         StringTokenizer var2;
         StringTokenizer var3;
         if (var4.countTokens() < var5.countTokens()) {
            var2 = var4;
            var3 = var5;
         } else {
            var2 = var5;
            var3 = var4;
         }

         String var8;
         String var9;
         do {
            if (!var2.hasMoreTokens()) {
               if (var3.hasMoreTokens()) {
                  return 1;
               }

               return 0;
            }

            var8 = var2.nextToken();
            var9 = var3.nextToken();
         } while(var8.equals(var9));

         return -1;
      } else {
         return -1;
      }
   }

   public static int countPathNodes(String var0) {
      if (var0 == null) {
         return 0;
      } else if (var0.length() == 0) {
         return 0;
      } else if (var0.indexOf(".") == -1) {
         return 1;
      } else {
         int var1 = 1;
         int var2 = 0;

         while(var2 != -1) {
            var2 = var0.indexOf(".", var2);
            if (var2 != -1) {
               ++var1;
               ++var2;
            }
         }

         return var1;
      }
   }

   public static JoinNode getFirstNode(JoinNode var0, String var1) throws IllegalExpressionException {
      String var2 = getFirstFieldFromId(var1);
      return var0.getChild(var2);
   }

   public static JoinNode getNode(JoinNode var0, String var1) throws IllegalExpressionException {
      if (endsInField(var0, var1) || endsInRemoteInterface(var0, var1)) {
         int var2 = var1.lastIndexOf(".");
         if (var2 == -1) {
            return var0;
         }

         var1 = var1.substring(0, var2);
      }

      Object var8 = null;
      String var3 = "";
      JoinNode var4 = var0;

      for(StringTokenizer var5 = new StringTokenizer(var1, "."); var5.hasMoreTokens(); var4 = var4.getChild(var3)) {
         var3 = (String)var5.nextElement();
         if (!var4.hasChild(var3)) {
            RDBMSBean var6 = var4.getRDBMSBean();
            String var7 = var6.getCmpColumnForField(var3);
            if (var7 == null) {
               var7 = var6.getCmpColumnForVariable(var3);
            }

            if (var7 != null) {
               throw new IllegalExpressionException(7, "called getNode on an pathExpression that ends with a Field: " + var1);
            }

            throw new IllegalExpressionException(7, "called getNode on an unparsed pathExpression: " + var1);
         }
      }

      return var4;
   }

   public static List createJoinListEntry(String var0, String var1) {
      ArrayList var2 = new ArrayList();
      var2.add(var0);
      var2.add(var1);
      return var2;
   }

   public static void markDoLeftOuterJoins(JoinNode var0, String var1, String var2) throws IllegalExpressionException {
      if (countPathNodes(var1) > countPathNodes(var2)) {
         throw new IllegalExpressionException(7, "Internal Error !  in doLeftOuterJoins.  The number of path elements in Start Path: '" + var1 + "', is greater than the number of path elements in the End Path '" + var2 + "'.");
      } else {
         JoinNode var3 = getFirstNode(var0, var1);
         if (var3 == null) {
            throw new IllegalExpressionException(7, "Internal Error !  in doLeftOuterJoins.  Start Path: '" + var1 + "', does not start with the Alias of an Abstract Schema Name defined in the Query.");
         } else {
            String var4 = getFirstFieldFromId(var1);
            JoinNode var5 = getFirstNode(var0, var2);
            if (var5 == null) {
               throw new IllegalExpressionException(7, "Internal Error !  in doLeftOuterJoins.  End Path: '" + var2 + "', does not start with the Alias of an Abstract Schema Name defined in the Query.");
            } else {
               String var6 = getFirstFieldFromId(var2);
               if (!var6.equals(var4)) {
                  throw new IllegalExpressionException(7, "Internal Error !  in doLeftOuterJoins.  The Start Path: '" + var1 + "', begins with: '" + var4 + "'.  The End Path: '" + var2 + "', begins with: '" + var6 + "'.   The Start Path and End Path must begin with the same Root PathElement, " + "but they do not.");
               } else {
                  int var7 = 0;
                  int var8 = countPathNodes(var1);
                  StringTokenizer var9 = new StringTokenizer(var2, ".");
                  JoinNode var10 = var0;

                  while(var9.hasMoreTokens()) {
                     String var11 = var9.nextToken();
                     ++var7;
                     var10 = var10.getChild(var11);
                     if (var10 == null) {
                        throw new IllegalExpressionException(7, "Internal Error !  in doLeftOuterJoins.   Start Path: '" + var1 + ", End Path: '" + var2 + ", at element number: '" + var7 + "', we could not get a JoinNode for path element: '" + var11 + "'");
                     }

                     if (var7 > var8) {
                        if (debugLogger.isDebugEnabled()) {
                           debug("in path: '" + var2 + "', element " + var7 + ": '" + var7 + "', setting WHERE Clause Left Outer Join to 'True'.");
                        }

                        var10.setDoLeftOuterJoin(true);
                     }
                  }

                  checkLeftOuterJoinCountSupported(var0);
               }
            }
         }
      }
   }

   public static boolean checkLeftOuterJoinCountSupported(JoinNode var0) throws IllegalExpressionException {
      int var1 = var0.getAllDoLeftOuterJoinCount();
      if (var1 <= 0) {
         return true;
      } else {
         Map var2 = var0.getChildren();
         Iterator var3 = var2.keySet().iterator();
         if (var3.hasNext()) {
            String var4 = (String)var3.next();
            JoinNode var5 = (JoinNode)var2.get(var4);
            int var6 = var5.getRDBMSBean().getDatabaseType();
            boolean var7 = var1 > 1 ? RDBMSUtils.dbSupportForMultiLeftOuterJoin(var6) : RDBMSUtils.dbSupportForSingleLeftOuterJoin(var6);
            if (!var7) {
               Loggable var8 = EJBLogger.logCannotDoNOuterJoinForDBLoggable(var1, DDConstants.getDBNameForType(var6));
               throw new IllegalExpressionException(7, var8.getMessage());
            }
         }

         return true;
      }
   }

   public static List getTableAliasList(JoinNode var0) throws RDBMSException {
      ArrayList var1 = new ArrayList();
      return var0.getTableAlias(var1);
   }

   public static List getTableAliasListFromChildren(JoinNode var0) throws RDBMSException {
      ArrayList var1 = new ArrayList();
      Map var2 = var0.getChildren();
      Iterator var3 = var2.values().iterator();

      while(var3.hasNext()) {
         JoinNode var4 = (JoinNode)var3.next();
         var4.getTableAlias(var1);
      }

      return var1;
   }

   public static RDBMSBean getTerminalBean(JoinNode var0, String var1) throws IllegalExpressionException {
      JoinNode var2 = getNode(var0, var1);
      return var2.getRDBMSBean();
   }

   public static String getFirstFieldFromId(String var0) {
      if (var0 == null) {
         return "";
      } else {
         String var1 = var0;
         int var2 = var0.indexOf(".");
         if (var2 != -1) {
            var1 = var0.substring(0, var2);
         }

         return var1;
      }
   }

   public static String getLastFieldFromId(String var0) {
      if (var0 == null) {
         return "";
      } else {
         String var1 = var0;
         int var2 = var0.lastIndexOf(".");
         if (var2 != -1) {
            var1 = var0.substring(var2 + 1);
         }

         return var1;
      }
   }

   public static String chopLastFieldFromId(String var0) {
      if (var0 == null) {
         return "";
      } else {
         int var1 = var0.lastIndexOf(".");
         return var1 != -1 ? var0.substring(0, var1) : "";
      }
   }

   public static String getFirstNFieldsFromId(String var0, int var1) {
      if (var0 == null) {
         return "";
      } else {
         int var2 = 0;

         int var3;
         for(var3 = -1; var2 < var1; ++var2) {
            var3 = var0.indexOf(".", var3 + 1);
            if (var3 == -1) {
               return var0;
            }
         }

         if (var3 > var0.length()) {
            return var0;
         } else {
            return var0.substring(0, var3);
         }
      }
   }

   public static JoinNode makeJoinRoot(RDBMSBean var0, QueryContext var1) {
      JoinNode var2 = new JoinNode((JoinNode)null, "", var0, "", "", -1, false, false, "", var1, new ArrayList());
      return var2;
   }

   private JoinNode newJoinNode(JoinNode var1, String var2, RDBMSBean var3, String var4, String var5, int var6, boolean var7, boolean var8, String var9, List var10) {
      if (debugLogger.isDebugEnabled()) {
         debug(" new JoinNode: using QueryContext ");
      }

      return new JoinNode(var1, var2, var3, var4, var5, var6, var7, var8, var9, this.queryContext, var10);
   }

   private static void debug(String var0) {
      debugLogger.debug("[JoinNode] " + var0);
   }

   private boolean isUseInnerJoin() {
      if (this.queryContext.getRDBMSBean() == null) {
         return false;
      } else {
         return !this.queryContext.getRDBMSBean().isUseInnerJoin() ? this.thisBean.isUseInnerJoin() : true;
      }
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
