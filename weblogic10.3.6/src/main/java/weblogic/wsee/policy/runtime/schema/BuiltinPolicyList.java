package weblogic.wsee.policy.runtime.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class BuiltinPolicyList {
   private BuiltinPolicy[] builtinPolicies;
   private HashSet<Integer>[] categoryTbl;
   private HashSet<String> cannedPolicyTable;
   private static final String DEFAULT_RELIABILITY_XML = "DefaultReliability.xml";
   private static final String LONG_RUNNING_RELIABILITY_XML = "LongRunningReliability.xml";

   private BuiltinPolicyList() {
      this.builtinPolicies = null;
      this.categoryTbl = null;
      this.cannedPolicyTable = null;
      this.categoryTbl = new HashSet[BuiltinPolicyHelper.getCategorySize()];
      this.cannedPolicyTable = new HashSet();

      for(int var1 = 0; var1 < this.categoryTbl.length; ++var1) {
         this.categoryTbl[var1] = new HashSet();
      }

   }

   protected BuiltinPolicyList(BuiltinPolicyType[] var1) {
      this();
      if (null == var1) {
         throw new IllegalArgumentException("Null BuiltinPolicies");
      } else {
         this.builtinPolicies = new BuiltinPolicy[var1.length];

         for(int var2 = 0; var2 < this.builtinPolicies.length; ++var2) {
            this.builtinPolicies[var2] = new BuiltinPolicy(var1[var2]);
            this.builtinPolicies[var2].setId(var2);
            this.addToCategoryTable(this.builtinPolicies[var2]);
            this.cannedPolicyTable.add(this.builtinPolicies[var2].getPolicyName());
         }

      }
   }

   private void addBuiltinPolicy(BuiltinPolicy var1) {
      if (null == this.builtinPolicies) {
         this.builtinPolicies = new BuiltinPolicy[1];
      } else {
         BuiltinPolicy[] var2 = new BuiltinPolicy[this.builtinPolicies.length + 1];

         for(int var3 = 0; var3 < this.builtinPolicies.length; ++var3) {
            var2[var3] = this.builtinPolicies[var3];
         }

         this.builtinPolicies = var2;
      }

      this.builtinPolicies[this.builtinPolicies.length - 1] = var1;
      this.addToCategoryTable(var1);
   }

   private void addToCategoryTable(BuiltinPolicy var1) {
      int[] var2 = var1.getCatIds();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.categoryTbl[var2[var3] - 1].add(new Integer(var1.getId()));
      }

   }

   public int size() {
      return this.builtinPolicies.length;
   }

   public boolean containsPolicy(String var1) {
      return this.cannedPolicyTable.contains(var1);
   }

   public String[] getAllPolices() {
      String[] var1 = new String[this.cannedPolicyTable.size()];
      ArrayList var2 = new ArrayList(this.cannedPolicyTable);
      Collections.sort(var2);
      Iterator var3 = var2.iterator();

      for(int var4 = 0; var3.hasNext(); var1[var4++] = (String)var3.next()) {
      }

      return var1;
   }

   public String[] getPoliciesByCategory(int var1) {
      String[] var2 = new String[this.categoryTbl[var1 - 1].size()];
      Iterator var3 = this.categoryTbl[var1 - 1].iterator();

      int var5;
      for(int var4 = 0; var3.hasNext(); var2[var4++] = this.builtinPolicies[var5].getPolicyName()) {
         var5 = (Integer)var3.next();
      }

      return var2;
   }

   public String[] getPoliciesExcludeCategory(int var1) {
      String[] var2 = this.getPoliciesByCategory(var1);
      if (null != var2 && var2.length != 0) {
         HashSet var3 = (HashSet)this.cannedPolicyTable.clone();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3.remove(var2[var4]);
         }

         String[] var8 = new String[var3.size()];
         ArrayList var5 = new ArrayList(var3);
         Collections.sort(var5);
         Iterator var6 = var5.iterator();

         for(int var7 = 0; var6.hasNext(); var8[var7++] = (String)var6.next()) {
         }

         return var8;
      } else {
         return this.getAllPolices();
      }
   }

   public String[] getPoliciesByCategory(String var1) {
      return this.getPoliciesByCategory(BuiltinPolicyHelper.getCategoryId(var1));
   }

   public String[] getPoliciesByCategory(CategoryEnum.Enum var1) {
      return this.getPoliciesByCategory(BuiltinPolicyHelper.getCategoryId(var1));
   }

   public String[] get92CannedPolicyNames() {
      return this.getPoliciesByCategory(1);
   }

   public String[] getAllNon92CannedPolicyNames() {
      HashSet var1 = (HashSet)this.cannedPolicyTable.clone();
      String[] var2 = this.get92CannedPolicyNames();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!var2[var3].equals("DefaultReliability.xml") && !var2[var3].equals("LongRunningReliability.xml")) {
            var1.remove(var2[var3]);
         }
      }

      String[] var7 = new String[var1.size()];
      ArrayList var4 = new ArrayList(var1);
      Collections.sort(var4);
      Iterator var5 = var4.iterator();

      for(int var6 = 0; var5.hasNext(); var7[var6++] = (String)var5.next()) {
      }

      return var7;
   }

   public String[] getMcCannedPolicyNames() {
      return this.getPoliciesByCategory(7);
   }

   public String[] getWssp12CannedPolicyNames() {
      return this.getPoliciesByCategory(2);
   }

   public String[] getWssp12_2007_CannedPolicyNames() {
      return this.getPoliciesByCategory(3);
   }

   public String[] getSamlCannedPolicyNames() {
      return this.getPoliciesByCategory(5);
   }

   public String[] getProtectionCannedPolicyNames() {
      return this.getPoliciesByCategory(15);
   }

   public String[] getWsscCannedPolicyNames() {
      return this.getPoliciesByCategory(8);
   }

   public String[] getWssc13CannedPolicyNames() {
      return this.getPoliciesByCategory(9);
   }

   public String[] getWssc14CannedPolicyNames() {
      return this.getPoliciesByCategory(10);
   }

   public String[] getWsrmCannedPolicyNames() {
      return this.getPoliciesByCategory(4);
   }

   public String[] getWsp15CannedPolicyNames() {
      return this.getPoliciesByCategory(11);
   }

   public String[] getWss10CannedPolicyNames() {
      return this.getPoliciesByCategory(13);
   }

   public String[] getWss11CannedPolicyNames() {
      return this.getPoliciesByCategory(14);
   }

   public String[] getMtomCannedPolicyNames() {
      return this.getPoliciesByCategory(6);
   }

   public String[] getTransportCannedPolicyNames() {
      return this.getPoliciesByCategory(12);
   }

   public String[] getInternalCannedPolicyNames() {
      return this.getPoliciesByCategory(16);
   }

   public String[] getNonInternalCannedPolicyNames() {
      return this.getPoliciesExcludeCategory(16);
   }
}
