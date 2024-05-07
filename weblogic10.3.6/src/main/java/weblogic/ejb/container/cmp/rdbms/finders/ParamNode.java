package weblogic.ejb.container.cmp.rdbms.finders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.cmp.rdbms.RDBMSBean;

public final class ParamNode {
   private static final DebugLogger debugLogger;
   private RDBMSBean rbean;
   private String paramName;
   private int variableNumber;
   private Class classType;
   private String id;
   private String remoteHomeName;
   private boolean isBeanParam;
   private boolean isSelectInEntity;
   private Class primaryKeyClass;
   private boolean hasCompoundKey;
   private List paramSubList;
   private boolean isOracleNLSDataType;

   public ParamNode(RDBMSBean var1, String var2, int var3, Class var4, String var5, String var6, boolean var7, boolean var8, Class var9, boolean var10, boolean var11) {
      this.rbean = var1;
      this.paramName = var2;
      this.variableNumber = var3;
      this.classType = var4;
      this.id = var5;
      this.remoteHomeName = var6;
      this.isBeanParam = var7;
      this.isSelectInEntity = var8;
      this.primaryKeyClass = var9;
      this.hasCompoundKey = var10;
      this.isOracleNLSDataType = var11;
      if (debugLogger.isDebugEnabled()) {
         debug(" Created new Param Node for: " + var5 + ", variableNumber: " + var3);
         debug(this.toString());
      }

   }

   public boolean isBeanParam() {
      return this.isBeanParam;
   }

   public boolean hasCompoundKey() {
      return this.hasCompoundKey;
   }

   public String getRemoteHomeName() {
      return this.remoteHomeName;
   }

   public String getId() {
      return this.id;
   }

   public Class getParamClass() {
      return this.classType;
   }

   public String getParamName() {
      return this.paramName;
   }

   public void addParamSubList(ParamNode var1) {
      if (this.paramSubList == null) {
         this.paramSubList = new ArrayList();
      }

      this.paramSubList.add(var1);
   }

   public List getParamSubList() {
      if (this.paramSubList == null) {
         this.paramSubList = new ArrayList();
      }

      return this.paramSubList;
   }

   public Class getPrimaryKeyClass() {
      return this.primaryKeyClass;
   }

   public void setPrimaryKeyClass(Class var1) {
      this.primaryKeyClass = var1;
   }

   public RDBMSBean getRDBMSBean() {
      return this.rbean;
   }

   public boolean isSelectInEntity() {
      return this.isSelectInEntity;
   }

   public int getVariableNumber() {
      return this.variableNumber;
   }

   public boolean isOracleNLSDataType() {
      return this.isOracleNLSDataType;
   }

   public void setOracleNLSDataType(boolean var1) {
      this.isOracleNLSDataType = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("\n\n------------------------");
      var1.append("\n  ParamNode     name: " + this.paramName);
      var1.append("\n               class: " + this.classType.getName());
      var1.append("\n      variableNumber: " + this.variableNumber);
      var1.append("\n                  id: " + this.id);
      var1.append("\n      remoteHomeName: " + this.remoteHomeName);
      var1.append("\n         isBeanParam: " + (this.isBeanParam ? "true" : "false"));
      var1.append("\n    isSelectInEntity: " + (this.isSelectInEntity ? "true" : "false"));
      var1.append("\n     primaryKeyClass: " + this.primaryKeyClass);
      var1.append("\n      hasCompoundKey: " + (this.hasCompoundKey ? "true" : "false"));
      var1.append("\n");
      if (this.paramSubList != null) {
         Iterator var2 = this.paramSubList.iterator();
         if (var2.hasNext()) {
            var1.append("  ----------- subList ------");

            while(var2.hasNext()) {
               ParamNode var3 = (ParamNode)var2.next();
               var1.append(var3.toString());
            }
         }
      }

      return var1.toString();
   }

   public static void addInVariableOrder(List var0, ParamNode var1) {
      Iterator var2 = var0.iterator();
      int var3 = var0.size();
      boolean var4 = false;

      for(int var5 = 0; var5 < var3; ++var5) {
         ParamNode var6 = (ParamNode)var0.get(var5);
         if (var1.getVariableNumber() < var6.getVariableNumber()) {
            var0.add(var5, var1);
            var4 = true;
         }

         if (var4) {
            break;
         }
      }

      if (!var4) {
         var0.add(var1);
      }

   }

   public static List listInVariableOrder(List var0) {
      Iterator var1 = var0.iterator();
      ArrayList var2 = new ArrayList();
      boolean var3 = false;

      while(var1.hasNext()) {
         ParamNode var4 = (ParamNode)var1.next();
         int var5 = var2.size();
         boolean var6 = false;
         boolean var7 = false;

         for(int var8 = 0; var8 < var5; ++var8) {
            ParamNode var9 = (ParamNode)var2.get(var8);
            if (var4.getVariableNumber() < var9.getVariableNumber()) {
               var2.add(var8, var4);
               var7 = true;
            }

            if (var7) {
               break;
            }
         }

         if (!var7) {
            var2.add(var4);
         }
      }

      return var2;
   }

   private static void debug(String var0) {
      debugLogger.debug("[ParamNode] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cmpDeploymentLogger;
   }
}
