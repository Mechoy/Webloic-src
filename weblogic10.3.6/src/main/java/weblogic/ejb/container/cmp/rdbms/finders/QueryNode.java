package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.EJBTextTextFormatter;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;
import weblogic.logging.Loggable;
import weblogic.utils.Debug;

public class QueryNode {
   private static final DebugLogger debugLogger;
   static EJBTextTextFormatter fmt;
   private EjbqlFinder finder;
   private QueryContext queryContext;
   private int queryId;
   private QueryNode parent;
   private List children;
   private int queryType;
   private Map rangeVariableMap;
   private Set collectionMemberSet;
   private JoinNode joinTree;
   private List selectList;
   private List selectListForCachingElement;
   private List tableAliasExclusionList;
   private int ORcount;
   private Stack orJoinDataStack;
   private ORJoinData mainOrJoinData;
   private List orJoinDataListList;

   public QueryNode(EjbqlFinder var1, QueryContext var2, QueryNode var3, JoinNode var4, int var5) {
      this.finder = null;
      this.queryContext = null;
      this.parent = null;
      this.children = null;
      this.rangeVariableMap = null;
      this.collectionMemberSet = null;
      this.joinTree = null;
      this.selectList = null;
      this.selectListForCachingElement = null;
      this.tableAliasExclusionList = null;
      this.ORcount = 0;
      this.finder = var1;
      this.queryContext = var2;
      this.parent = var3;
      this.children = new ArrayList();
      this.rangeVariableMap = new HashMap();
      this.collectionMemberSet = new HashSet();
      this.joinTree = var4;
      this.selectList = new ArrayList();
      this.selectListForCachingElement = new ArrayList();
      this.tableAliasExclusionList = new ArrayList();
      this.queryId = var5;
      this.orJoinDataStack = new Stack();
      this.mainOrJoinData = new ORJoinData();
      this.orJoinDataStack.push(this.mainOrJoinData);
      if (var3 != null) {
         var3.addChild(this);
      }

   }

   public QueryNode(EjbqlFinder var1, QueryContext var2, QueryNode var3, JoinNode var4) {
      this(var1, var2, var3, var4, 0);
   }

   public boolean isMainQuery() {
      return this.parent == null;
   }

   public QueryNode getParent() {
      Debug.assertion(this.parent != null);
      return this.parent;
   }

   public void addChild(QueryNode var1) {
      this.children.add(var1);
   }

   public Iterator getChildrenIterator() {
      return this.children.iterator();
   }

   public void setQueryType(int var1) {
      this.queryType = var1;
   }

   public int getQueryType() {
      return this.queryType;
   }

   public void addRangeVariable(String var1, String var2) throws IllegalExpressionException {
      if (this.rangeVariableMap == null) {
         this.rangeVariableMap = new HashMap();
      }

      if (this.rangeVariableMap.containsKey(var1)) {
         Loggable var3 = EJBLogger.logduplicateAsDefinitionLoggable(var1);
         throw new IllegalExpressionException(7, var3.getMessage());
      } else {
         this.rangeVariableMap.put(var1, var2);
      }
   }

   public int rangeVariableMapSize() {
      if (this.rangeVariableMap == null) {
         this.rangeVariableMap = new HashMap();
      }

      return this.rangeVariableMap.size();
   }

   public String getRangeVariableMap(String var1) {
      return this.rangeVariableMap == null ? null : (String)this.rangeVariableMap.get(var1);
   }

   public List getRangeVariableMapIdList() {
      ArrayList var1 = new ArrayList();
      if (this.rangeVariableMap == null) {
         return var1;
      } else {
         Iterator var2 = this.rangeVariableMap.keySet().iterator();

         while(var2.hasNext()) {
            var1.add(var2.next());
         }

         return var1;
      }
   }

   public List getIDsFromRangeVariableMapForSchema(String var1) {
      ArrayList var2 = new ArrayList();
      if (this.rangeVariableMap == null) {
         return var2;
      } else {
         Iterator var3 = this.rangeVariableMap.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            String var5 = (String)this.rangeVariableMap.get(var4);
            if (var5.equals(var1)) {
               var2.add(var4);
            }
         }

         return var2;
      }
   }

   public void addCollectionMember(String var1) throws IllegalExpressionException {
      if (this.collectionMemberSet.contains(var1)) {
         Loggable var2 = EJBLogger.logduplicateCollectionMemberDefinitionLoggable(var1);
         throw new IllegalExpressionException(7, var2.getMessage());
      } else {
         this.collectionMemberSet.add(var1);
      }
   }

   public boolean containsCollectionMember(String var1) {
      if (var1 == null) {
         return false;
      } else {
         return JoinNode.countPathNodes(var1) != 1 ? false : this.collectionMemberSet.contains(var1);
      }
   }

   public boolean isCollectionMemberInScope(String var1) {
      QueryNode var2 = null;

      try {
         var2 = this.getQueryNodeForCollectionMember(var1);
      } catch (IllegalExpressionException var4) {
         return false;
      }

      return var2 != null;
   }

   public QueryNode getQueryNodeForCollectionMember(String var1) throws IllegalExpressionException {
      if (this.containsCollectionMember(var1)) {
         return this;
      } else if (this.parent == null) {
         throw new IllegalExpressionException(5, "Error, attempt to reference a Collection Member Identifier, '" + var1 + "' that is outside of the scope of it's query or subquery.");
      } else {
         return this.parent.getQueryNodeForCollectionMember(var1);
      }
   }

   public boolean isRangeVariableInScope(String var1) {
      if (var1 == null) {
         return false;
      } else if (JoinNode.countPathNodes(var1) != 1) {
         return false;
      } else {
         QueryNode var2 = null;

         try {
            var2 = this.getQueryNodeForId(var1);
         } catch (IllegalExpressionException var4) {
            return false;
         }

         return var2 != null;
      }
   }

   public boolean thisQueryNodeOwnsId(String var1) {
      String var2 = JoinNode.getFirstFieldFromId(var1);
      return this.getRangeVariableMap(var2) != null;
   }

   public QueryNode getQueryNodeForId(String var1) throws IllegalExpressionException {
      if (debugLogger.isDebugEnabled()) {
         debug("\n About to search for joinTree that owns: " + var1);
      }

      String var2 = this.queryContext.replaceIdAliases(var1);
      String var3 = JoinNode.getFirstFieldFromId(var2);
      QueryNode var4 = null;

      try {
         var4 = this.getQueryNodeForRangeVariableID(var3);
         return var4;
      } catch (IllegalExpressionException var8) {
         if (var8.getErrorCode() == 5) {
            Loggable var6 = EJBLogger.logpathExpressionNotInContextOfQueryTreeLoggable(var2, var3);
            IllegalExpressionException var7 = new IllegalExpressionException(5, var6.getMessage());
            throw var7;
         } else {
            throw var8;
         }
      }
   }

   public QueryNode getQueryNodeForRangeVariableID(String var1) throws IllegalExpressionException {
      if (this.getRangeVariableMap(var1) != null) {
         return this;
      } else if (this.parent == null) {
         throw new IllegalExpressionException(5, "Error, attempt to reference a path expression, '" + var1 + "' that is outside of the scope of it's query or subquery.");
      } else {
         return this.parent.getQueryNodeForRangeVariableID(var1);
      }
   }

   public JoinNode getJoinTreeForId(String var1) throws IllegalExpressionException {
      QueryNode var2 = this.getQueryNodeForId(var1);
      return var2 == null ? null : var2.getJoinTree();
   }

   public JoinNode getJoinNodeForFirstId(String var1) throws IllegalExpressionException {
      JoinNode var2 = this.getJoinTreeForId(var1);
      return JoinNode.getFirstNode(var2, var1);
   }

   public JoinNode getJoinNodeForLastId(String var1) throws IllegalExpressionException {
      this.prepareIdentifierForSQLGen(var1);
      JoinNode var2 = this.getJoinTreeForId(var1);
      return JoinNode.getNode(var2, var1);
   }

   public JoinNode getJoinTree() {
      return this.joinTree;
   }

   public void prepareIdentifierForSQLGen(String var1) throws IllegalExpressionException {
      String var2 = this.queryContext.replaceIdAliases(var1);
      String var3 = JoinNode.getFirstFieldFromId(var1);
      QueryNode var4 = this.getQueryNodeForId(var2);
      if (var4 == null) {
         Loggable var5 = EJBLogger.logpathExpressionNotInContextOfQueryTreeLoggable(var1, var3);
         new IllegalExpressionException(5, var5.getMessage());
      }

      this.parseJoin(var2);
      var4.replaceORPathMaybe(var2);
   }

   private void parseJoin(String var1) throws IllegalExpressionException {
      JoinNode var2 = this.getJoinTreeForId(var1);
      if (var2 == null) {
         String var3 = JoinNode.getFirstFieldFromId(var1);
         Loggable var4 = EJBLogger.logidNotDefinedInAsDeclarationLoggable(var1, var3);
         throw new IllegalExpressionException(5, var4.getMessage());
      } else {
         var2.parseJoin(var1);
      }
   }

   public void addSelectList(SelectNode var1) {
      this.selectList.add(var1);
   }

   public List getSelectList() {
      return this.selectList;
   }

   public SelectNode selectListRootMatch(String var1) {
      if (var1 == null) {
         return null;
      } else if (var1.length() <= 0) {
         return null;
      } else {
         String var2 = this.finder.replaceCorrVars(var1);
         String var3 = JoinNode.getFirstFieldFromId(var2);
         List var4 = this.getSelectList();
         Iterator var5 = var4.iterator();

         SelectNode var6;
         String var9;
         do {
            if (!var5.hasNext()) {
               return null;
            }

            var6 = (SelectNode)var5.next();
            String var7 = var6.getSelectTarget();
            String var8 = this.finder.replaceCorrVars(var7);
            var9 = JoinNode.getFirstFieldFromId(var8);
         } while(!var9.equals(var3));

         return var6;
      }
   }

   public void addSelectListForCachingElement(SelectNode var1) {
      this.selectListForCachingElement.add(var1);
   }

   public List getSelectListForCachingElement() {
      return this.selectListForCachingElement;
   }

   public boolean containsInSelectListForCachingElement(SelectNode var1) {
      return this.selectListForCachingElement.contains(var1);
   }

   public boolean containsInSelectListForCachingElement(RDBMSBean var1, RDBMSBean var2) {
      Iterator var3 = this.selectListForCachingElement.iterator();

      RDBMSBean var5;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         SelectNode var4 = (SelectNode)var3.next();
         var5 = var4.getSelectBean();
      } while(var1 != var5 && var2 != var5);

      return true;
   }

   public void addTableAliasExclusionList(String var1) {
      if (this.tableAliasExclusionList == null) {
         this.tableAliasExclusionList = new ArrayList();
      }

      this.tableAliasExclusionList.add(var1);
   }

   public List getTableAliasExclusionList() {
      return this.tableAliasExclusionList;
   }

   public Set getTableNameSetMinusExcluded() throws IllegalExpressionException {
      return this.getTableInfoSetMinusExcluded(0);
   }

   private Set getTableInfoSetMinusExcluded(int var1) throws IllegalExpressionException {
      List var2;
      try {
         var2 = JoinNode.getTableAliasList(this.getJoinTree());
      } catch (Exception var11) {
         throw new IllegalExpressionException(7, var11.getMessage());
      }

      List var3 = this.getTableAliasExclusionList();
      Map var4 = this.queryContext.getGlobalTableAliasMap();
      HashSet var5 = new HashSet();
      Iterator var6 = var2.iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         boolean var8 = false;
         if (var3 != null) {
            Iterator var9 = var3.iterator();

            while(var9.hasNext()) {
               String var10 = (String)var9.next();
               if (var10.compareTo(var7) == 0) {
                  var8 = true;
                  break;
               }
            }
         }

         if (!var8) {
            if (var1 == 1) {
               var5.add(var7);
            } else {
               String var12 = (String)var4.get(var7);
               var5.add(var12);
            }
         }
      }

      return var5;
   }

   String getFROMDeclaration(int var1) throws IllegalExpressionException {
      return this.joinTree.getFROMDeclaration(this.getTableAliasExclusionList(), var1);
   }

   public void setQueryId(int var1) {
      this.queryId = var1;
   }

   public int getQueryId() {
      return this.queryId;
   }

   public QueryNode getQueryNodeForQueryId(int var1) {
      if (this.queryId == var1) {
         return this;
      } else {
         QueryNode var2 = null;
         Iterator var3 = this.children.iterator();

         do {
            if (!var3.hasNext()) {
               return null;
            }

            QueryNode var4 = (QueryNode)var3.next();
            var2 = var4.getQueryNodeForQueryId(var1);
         } while(var2 == null);

         return var2;
      }
   }

   public void pushOR(Expr var1) {
      this.pushOR(this.newORJoinData(var1));
      ++this.ORcount;
   }

   public void pushOR(ORJoinData var1) {
      this.orJoinDataStack.push(var1);
   }

   public ORJoinData popOR() {
      return (ORJoinData)this.orJoinDataStack.pop();
   }

   public ORJoinData currentOR() {
      return (ORJoinData)this.orJoinDataStack.peek();
   }

   public void addORJoinDataListList(List var1) {
      if (this.orJoinDataListList == null) {
         this.orJoinDataListList = new ArrayList();
      }

      if (!this.orJoinDataListList.contains(var1)) {
         this.orJoinDataListList.add(var1);
      }

   }

   public List getORJoinDataListList() {
      return this.orJoinDataListList;
   }

   public void replaceORPathMaybe(String var1) throws IllegalExpressionException {
      if (debugLogger.isDebugEnabled()) {
         debug(" Entered replaceORPathMaybe for id: '" + var1 + "'");
      }

      if (!this.thisQueryNodeOwnsId(var1)) {
         if (debugLogger.isDebugEnabled()) {
            debug(" queryNode does not own id.  exiting.");
         }

      } else {
         this.checkORPath(var1);
         this.addSQLTableGenSymbolMap(var1);
      }
   }

   private void checkORPath(String var1) throws IllegalExpressionException {
      ORJoinData var2 = this.currentOR();
      Vector var3 = var2.getOrVector();
      int var4 = var3.size();
      if (debugLogger.isDebugEnabled()) {
         debug("Entered checkORPath. With input: '" + var1 + "',  current OR Vector has: " + var4 + " entries");
      }

      var1 = JoinNode.getPathWithoutTrailingCmpField(this.joinTree, var1);

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = (String)var3.elementAt(var5);
         if (debugLogger.isDebugEnabled()) {
            debug(" checking input '" + var1 + "' against OR path: '" + var6 + "'");
         }

         int var7 = JoinNode.comparePaths(var1, var6);
         switch (var7) {
            case -1:
               if (debugLogger.isDebugEnabled()) {
                  debug(" JoinNode.PATHS_DISTINCT  try again.");
               }
               break;
            case 0:
               if (debugLogger.isDebugEnabled()) {
                  debug(" JoinNode.PATHS_EQUAL  we're done.");
               }

               return;
            case 1:
               if (debugLogger.isDebugEnabled()) {
                  debug(" JoinNode.PATHS_SUBSET  replace shorter path if needed.");
               }

               if (var1.length() > var6.length()) {
                  var3.setElementAt(var1, var5);
                  if (debugLogger.isDebugEnabled()) {
                     debug(" JoinNode.PATHS_SUBSET  replaced shorter path in OR Vector with '" + var1 + "'");
                  }
               }

               return;
            default:
               throw new IllegalExpressionException(7, " UNKNOWN JoinNode.comparePaths return code: " + var7);
         }
      }

      if (debugLogger.isDebugEnabled()) {
         debug(" Adding path '" + var1 + "' to current OR Vector.");
      }

      var3.add(var1);
   }

   private void addSQLTableGenSymbolMap(String var1) throws IllegalExpressionException {
      if (debugLogger.isDebugEnabled()) {
         debug(" addSQLTableGenSymbols on '" + var1 + "'");
      }

      ORJoinData var2 = this.currentOR();
      var1 = JoinNode.getPathWithoutTrailingCmpField(this.joinTree, var1);
      int var3 = JoinNode.countPathNodes(var1);

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = JoinNode.getFirstNFieldsFromId(var1, var4 + 1);
         if (var2.getOrSQLTableGenSymbolMap().containsKey(var1)) {
            if (debugLogger.isDebugEnabled()) {
               debug(" addSQLTableGenSymbolMap: skipping, already processed '" + var1 + "'");
            }
         } else {
            if (debugLogger.isDebugEnabled()) {
               debug(" addSQLTableGenSymbolMap: checking pathExpression '" + var5 + "'");
            }

            int var6 = this.getRelationshipTypeForPathExpressionWithNoSQLGen(var5);
            switch (var6) {
               case -1:
               case 0:
               default:
                  break;
               case 1:
               case 2:
               case 3:
               case 4:
               case 5:
               case 6:
               case 7:
               case 8:
                  JoinNode var7 = JoinNode.getNode(this.joinTree, var5);
                  String var8 = var7.getTableName();
                  if (debugLogger.isDebugEnabled()) {
                     debug(" addSQLTableGenSymbolMap:  adding key value pair: '" + var5 + "', tableName: '" + var8 + "'");
                  }

                  var2.addOrSQLTableGenInfo(var5, var8);
            }
         }
      }

   }

   public void checkAllORCrossProducts() {
      HashMap var1 = new HashMap();
      this.checkAllORCrossProducts(var1);
      if (var1.size() > 0) {
         StringBuffer var2 = new StringBuffer();
         Iterator var3 = var1.keySet().iterator();
         boolean var4 = false;

         String var5;
         while(var3.hasNext()) {
            var5 = (String)var3.next();
            Iterator var6 = ((Set)var1.get(var5)).iterator();
            if (var6.hasNext()) {
               if (var4) {
                  var2.append(", ");
               } else {
                  var4 = true;
               }

               var2.append(var5);
            }
         }

         var5 = var2.toString();
         Loggable var7 = EJBLogger.logOrMayYieldEmptyCrossProductLoggable(var5);
         this.queryContext.addWarning(new IllegalExpressionException(6, var7.getMessage()));
      }

   }

   public void checkAllORCrossProducts(Map var1) {
      this.checkORCrossProducts(var1);
      Iterator var2 = this.getChildrenIterator();

      while(var2.hasNext()) {
         QueryNode var3 = (QueryNode)var2.next();
         var3.checkAllORCrossProducts(var1);
      }

   }

   public void checkORCrossProducts(Map var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("checkORCrossProducts()");
      }

      List var2 = this.getORJoinDataListList();
      if (var2 == null) {
         if (debugLogger.isDebugEnabled()) {
            debug("checkORCrossProducts() NO OR CLAUSES exit.  \n\n");
         }

      } else {
         int var3 = var2.size();
         if (debugLogger.isDebugEnabled()) {
            debug(" checkORCrossProducts()  ORListList size " + var3);
         }

         if (var3 > 0) {
            Iterator var4 = var2.iterator();

            while(true) {
               List var5;
               do {
                  if (!var4.hasNext()) {
                     return;
                  }

                  var5 = (List)var4.next();
                  var3 = var5.size();
               } while(var5.size() == 0);

               if (debugLogger.isDebugEnabled()) {
                  debug("\n check next orJoinDataList");
               }

               for(int var6 = 0; var6 < var3 - 1; ++var6) {
                  ORJoinData var7 = (ORJoinData)var5.get(var6);

                  for(int var8 = var6 + 1; var8 < var3; ++var8) {
                     ORJoinData var9 = (ORJoinData)var5.get(var8);
                     this.checkORCrossProductPair(var7, var9, var1);
                  }
               }
            }
         }
      }
   }

   private void checkORCrossProductPair(ORJoinData var1, ORJoinData var2, Map var3) {
      Expr var4 = var1.getOrTerm();
      Expr var5 = var2.getOrTerm();
      if (debugLogger.isDebugEnabled()) {
         debug("\n\n\nchecking OR pair: clause 1: '" + var4.printEJBQLTree() + "', \n                  clause 2: '" + var5.printEJBQLTree() + "'\n");
      }

      Set var6 = (Set)((HashSet)var1.getOrSQLTableGenTableSet()).clone();
      Set var7 = (Set)((HashSet)var2.getOrSQLTableGenTableSet()).clone();
      if (var6.size() != 0 && var7.size() != 0) {
         HashSet var8 = new HashSet();
         Iterator var9 = var6.iterator();

         while(var9.hasNext()) {
            Object var10 = var9.next();
            if (debugLogger.isDebugEnabled()) {
               debug(" set1 table: '" + var10 + "'");
            }

            if (var7.contains(var10)) {
               var9.remove();
               var7.remove(var10);
               if (debugLogger.isDebugEnabled()) {
                  debug("      table: '" + var10 + "' is in Set2, removing");
               }
            } else {
               if (debugLogger.isDebugEnabled()) {
                  debug(" table '" + var10 + "' in set1 but not set2. ");
               }

               var8.add(var10);
            }
         }

         if (var6.size() == 0 && var7.size() == 0) {
            if (debugLogger.isDebugEnabled()) {
               debug(" both sets have identical table lists. ");
            }

         } else {
            HashSet var17 = new HashSet();
            if (var7.size() > 0) {
               var17 = new HashSet();
               Iterator var11 = var7.iterator();

               while(var11.hasNext()) {
                  Object var12 = var11.next();
                  if (debugLogger.isDebugEnabled()) {
                     debug(" set2 table: '" + var12 + "'");
                  }

                  if (var6.contains(var12)) {
                     var11.remove();
                     var6.remove(var12);
                     if (debugLogger.isDebugEnabled()) {
                        debug("      table: '" + var12 + "' is in Set1, removing");
                     }
                  } else {
                     if (debugLogger.isDebugEnabled()) {
                        debug(" table '" + var12 + "' in set2 but not set1. ");
                     }

                     var17.add(var12);
                  }
               }
            }

            StringBuffer var18 = new StringBuffer();
            String var19;
            if (var8.size() > 0) {
               var9 = var8.iterator();

               while(var9.hasNext()) {
                  var19 = (String)var9.next();
                  var18.append(var19);
                  if (var9.hasNext()) {
                     var18.append(" ");
                  }
               }
            }

            var19 = var18.toString();
            String var13 = "";
            var18.setLength(0);
            Iterator var14;
            if (var17 != null) {
               var14 = var17.iterator();

               while(var14.hasNext()) {
                  String var15 = (String)var14.next();
                  var18.append(var15);
                  if (var14.hasNext()) {
                     var18.append(" ");
                  }
               }

               var13 = var18.toString();
            }

            if (debugLogger.isDebugEnabled()) {
               debug("  tables in OR clause 1 but not in OR clause 2 '" + var19 + "'");
               debug("  tables in OR clause 2 but not in OR clause 1 '" + var13 + "'");
            }

            var14 = null;

            Set var20;
            try {
               var20 = this.getTableNameSetMinusExcluded();
            } catch (Exception var16) {
               return;
            }

            var18.setLength(0);
            this.listORCrossProductPairDiff(var8, var1.getOrSQLTableGenSymbolMap(), var20, var3, var4);
            this.listORCrossProductPairDiff(var17, var2.getOrSQLTableGenSymbolMap(), var20, var3, var5);
         }
      }
   }

   private void listORCrossProductPairDiff(Set var1, Map var2, Set var3, Map var4, Expr var5) {
      if (var1.size() > 0) {
         Iterator var6 = var1.iterator();

         while(true) {
            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               if (var3.contains(var7)) {
                  Iterator var8 = var2.keySet().iterator();

                  while(var8.hasNext()) {
                     String var9 = (String)var8.next();
                     String var10 = (String)var2.get(var9);
                     if (var7.equals(var10)) {
                        Object var11 = (Set)var4.get(var7);
                        if (var11 == null) {
                           var11 = new HashSet();
                           var4.put(var7, var11);
                        }

                        ((Set)var11).add(var9);
                     }
                  }
               } else if (debugLogger.isDebugEnabled()) {
                  debug(" table '" + var7 + "' not in query's FROM clause, ignoring.");
               }
            }

            return;
         }
      }
   }

   private static void get_line(String var0) {
      System.out.print(var0);
      System.out.flush();

      try {
         while(true) {
            if (System.in.read() != 10) {
               continue;
            }
         }
      } catch (Exception var3) {
      }

   }

   public String getMainJoinBuffer() throws IllegalExpressionException {
      return this.getMainORJoinBuffer();
   }

   public String getMainORJoinBuffer() throws IllegalExpressionException {
      return this.getORJoinBuffer(this.mainOrJoinData);
   }

   public String getCurrentORJoinBuffer() throws IllegalExpressionException {
      ORJoinData var1 = (ORJoinData)this.orJoinDataStack.peek();
      return this.getORJoinBuffer(var1);
   }

   private String getORJoinBuffer(ORJoinData var1) throws IllegalExpressionException {
      Vector var2 = var1.getOrVector();
      StringBuffer var3 = new StringBuffer();
      boolean var4 = false;
      if (var2.size() > 0) {
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            StringBuffer var7 = new StringBuffer();
            JoinNode.getJoinSQLForPath(this.joinTree, var6, this.tableAliasExclusionList, var7);
            String var8 = var7.toString();
            if (var8.length() > 0) {
               if (var4) {
                  var3.append(" AND ");
               }

               var3.append(var8);
               var4 = true;
            }
         }
      }

      Map var9 = var1.getOrCmpFieldJoinMap();
      Iterator var10 = var9.keySet().iterator();

      while(var10.hasNext()) {
         String var11 = (String)var9.get(var10.next());
         if (var11.length() > 0) {
            if (var4) {
               var3.append(" AND ");
            }

            var3.append(var11);
            var4 = true;
         }
      }

      return var3.toString();
   }

   public void addCmpFieldJoinSQL(String var1, String var2, String var3) {
      String var4 = var1 + "." + var2;
      ORJoinData var5 = this.currentOR();
      Map var6 = var5.getOrCmpFieldJoinMap();
      var6.put(var4, var3);
   }

   void checkOracleORJoin(String var1) throws IllegalExpressionException {
      int var2 = this.getDbType();
      if (var2 == 1) {
         if (this.ORcount <= 0) {
            return;
         }

         int var3 = this.joinTree.getAllDoLeftOuterJoinCount();
         if (var3 > 0) {
            Loggable var4 = EJBLogger.logOracleCannotDoOuterJoinAndORLoggable(var1);
            throw new IllegalExpressionException(7, var4.getMessage());
         }
      }

   }

   int getRelationshipTypeForPathExpressionWithNoSQLGen(String var1) throws IllegalExpressionException {
      JoinNode var2 = this.getJoinTreeForId(var1);
      return JoinNode.getRelationshipTypeForPathExpressionWithNoSQLGen(this.queryContext, var2, var1);
   }

   public RDBMSBean getLastRDBMSBeanForPathExpressionWithNoSQLGen(String var1) throws IllegalExpressionException {
      JoinNode var2 = this.getJoinTreeForId(var1);
      return JoinNode.getLastRDBMSBeanForPathExpressionWithNoSQLGen(this.queryContext, var2, var1);
   }

   public RDBMSBean getLastRDBMSBeanForPathExpression(String var1) throws IllegalExpressionException {
      JoinNode var2 = this.getJoinTreeForId(var1);
      return JoinNode.getTerminalBean(var2, var1);
   }

   private int getDbType() {
      return this.queryContext.getRDBMSBean().getDatabaseType();
   }

   private ORJoinData newORJoinData() {
      return new ORJoinData();
   }

   private ORJoinData newORJoinData(Expr var1) {
      return new ORJoinData(var1);
   }

   public static QueryNode newQueryNode(EjbqlFinder var0, QueryContext var1, QueryNode var2, int var3) {
      JoinNode var4 = JoinNode.makeJoinRoot(var0.getRDBMSBean(), var1);
      return new QueryNode(var0, var1, var2, var4, var3);
   }

   private static void debug(String var0) {
      debugLogger.debug("[QueryNode] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
      fmt = new EJBTextTextFormatter();
   }
}
