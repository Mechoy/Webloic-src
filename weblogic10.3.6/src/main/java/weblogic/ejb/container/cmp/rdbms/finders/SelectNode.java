package weblogic.ejb.container.cmp.rdbms.finders;

import weblogic.ejb.container.cmp.rdbms.RDBMSBean;

public class SelectNode {
   private Expression selectItemExpr = null;
   private Expr selectItemBaseExpr = null;
   private int selectType;
   private String selectTypeName = "";
   private String selectTarget = null;
   private boolean isAggregate = false;
   private boolean isAggregateDistinct = false;
   private RDBMSBean selectBean = null;
   private Class selectClass = null;
   private String dbmsTarget = "";
   private String cachingElementGroupName = null;
   private RDBMSBean prevBean = null;
   private boolean correlatedSubQuery = false;

   public void setCorrelatedSubQuery() {
      this.correlatedSubQuery = true;
   }

   public boolean isCorrelatedSubQuery() {
      return this.correlatedSubQuery;
   }

   public void setSelectItemExpr(Expression var1) {
      this.selectItemExpr = var1;
   }

   public void setSelectItemBaseExpr(Expr var1) {
      this.selectItemBaseExpr = var1;
   }

   public void setSelectType(int var1) {
      this.selectType = var1;
   }

   public void setSelectTypeName(String var1) {
      this.selectTypeName = var1;
   }

   public void setSelectTarget(String var1) {
      this.selectTarget = var1;
   }

   public void setIsAggregate(boolean var1) {
      this.isAggregate = var1;
   }

   public void setIsAggregateDistinct(boolean var1) {
      this.isAggregateDistinct = var1;
   }

   public void setSelectBean(RDBMSBean var1) {
      this.selectBean = var1;
   }

   public void setSelectClass(Class var1) {
      this.selectClass = var1;
   }

   public void setDbmsTarget(String var1) {
      this.dbmsTarget = var1;
   }

   public Expression getSelectItemExpr() {
      return this.selectItemExpr;
   }

   public Expr getSelectItemBaseExpr() {
      return this.selectItemBaseExpr;
   }

   public int getSelectType() {
      return this.selectType;
   }

   public String getSelectTypeName() {
      return this.selectTypeName;
   }

   public String getSelectTarget() {
      return this.selectTarget;
   }

   public boolean getIsAggregate() {
      return this.isAggregate;
   }

   public boolean getIsAggregateDistinct() {
      return this.isAggregateDistinct;
   }

   public RDBMSBean getSelectBean() {
      return this.selectBean;
   }

   public Class setSelectClass() {
      return this.selectClass;
   }

   public String getDbmsTarget() {
      return this.dbmsTarget;
   }

   public String getCachingElementGroupName() {
      return this.cachingElementGroupName;
   }

   public void setCachingElementGroupName(String var1) {
      this.cachingElementGroupName = var1;
   }

   public void setPrevBean(RDBMSBean var1) {
      this.prevBean = var1;
   }

   public RDBMSBean getPrevBean() {
      return this.prevBean;
   }
}
