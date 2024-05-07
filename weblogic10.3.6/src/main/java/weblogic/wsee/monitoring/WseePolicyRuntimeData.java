package weblogic.wsee.monitoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.management.ManagementException;

public class WseePolicyRuntimeData extends WseeBaseRuntimeData {
   private ArrayList policies = new ArrayList();
   private HashMap knownPoliciesMap = new HashMap();

   public WseePolicyRuntimeData(String var1, String[] var2, WseeBaseRuntimeData var3) throws ManagementException {
      super(var1, var3);
      String[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         this.addPolicy(var7);
      }

   }

   public String[] getAvailablePolicies() {
      return (String[])((String[])this.policies.toArray(new String[this.policies.size()]));
   }

   public void addPolicy(String var1) {
      String var2 = getPolicyName(var1);
      if (!this.knownPoliciesMap.containsKey(var2)) {
         this.knownPoliciesMap.put(var2, var2);
         this.policies.add(var2);
      }

   }

   public void addPolicies(Map var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         this.addPolicy((String)var3);
      }

   }

   public void removePolicy(String var1) {
      this.knownPoliciesMap.remove(var1);
      this.policies.remove(var1);
   }

   private static String getPolicyName(String var0) {
      return "policy:" + var0 + (var0.endsWith(".xml") ? "" : ".xml");
   }
}
