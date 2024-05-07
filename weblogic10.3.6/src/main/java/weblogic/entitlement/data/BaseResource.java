package weblogic.entitlement.data;

import weblogic.entitlement.expression.EAuxiliary;
import weblogic.entitlement.expression.EExpression;

public abstract class BaseResource {
   private EExpression mExpr;
   private EAuxiliary mAux;
   private boolean mDeployData = false;
   private String mCollectionName = null;

   public BaseResource(EExpression var1) {
      this.mExpr = var1;
   }

   public BaseResource(EExpression var1, boolean var2) {
      this.mExpr = var1;
      this.mDeployData = var2;
   }

   public BaseResource(EExpression var1, boolean var2, String var3) {
      this.mExpr = var1;
      this.mDeployData = var2;
      this.mCollectionName = var3;
   }

   public BaseResource(EExpression var1, EAuxiliary var2) {
      this.mExpr = var1;
      this.mAux = var2;
   }

   public BaseResource(EExpression var1, EAuxiliary var2, boolean var3) {
      this.mExpr = var1;
      this.mAux = var2;
      this.mDeployData = var3;
   }

   public BaseResource(EExpression var1, EAuxiliary var2, boolean var3, String var4) {
      this.mExpr = var1;
      this.mAux = var2;
      this.mDeployData = var3;
      this.mCollectionName = var4;
   }

   public abstract Object getPrimaryKey();

   public EExpression getExpression() {
      return this.mExpr;
   }

   public void setExpression(EExpression var1) {
      this.mExpr = var1;
   }

   public String getEntitlement() {
      return this.mExpr == null ? null : this.mExpr.externalize();
   }

   public EAuxiliary getAuxiliary() {
      return this.mAux;
   }

   public void setAuxiliary(EAuxiliary var1) {
      this.mAux = var1;
   }

   public boolean isDeployData() {
      return this.mDeployData;
   }

   public String getCollectionName() {
      return this.mCollectionName;
   }
}
