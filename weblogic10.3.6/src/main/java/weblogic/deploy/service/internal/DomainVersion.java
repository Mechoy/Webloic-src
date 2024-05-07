package weblogic.deploy.service.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.Version;

public final class DomainVersion implements Externalizable {
   private static final long serialVersionUID = 8660184188777148469L;
   private static final String ADDITIONS = "ADDITIONS";
   private static final String DELETIONS = "DELETIONS";
   private static final String CHANGES = "CHANGES";
   private Map deploymentsVersionMap;

   public DomainVersion() {
      this.deploymentsVersionMap = null;
      this.deploymentsVersionMap = Collections.synchronizedMap(new HashMap());
   }

   public DomainVersion(Map var1) {
      this();
      this.deploymentsVersionMap.putAll(var1);
   }

   public final Map getDeploymentsVersionMap() {
      return this.deploymentsVersionMap;
   }

   public final void addOrUpdateDeploymentVersion(String var1, Version var2) {
      Version var3 = (Version)this.deploymentsVersionMap.put(var1, var2);
      if (this.isDebugEnabled()) {
         String var4 = null;
         if (var3 != null) {
            var4 = super.toString() + ".addOrUpdateDeploymentVersion():" + " Changed version on - '" + var1 + "' from: '" + var3 + "' to: '" + var2 + "'";
         } else {
            var4 = super.toString() + ".addOrUpdateDeploymentVersion():" + " Added version for - '" + var1 + "' : '" + var2 + "'";
         }

         this.debug(var4);
      }

   }

   public final void removeDeploymentVersion(String var1) {
      this.deploymentsVersionMap.remove(var1);
   }

   public final Version getDeploymentVersion(String var1) {
      return (Version)this.deploymentsVersionMap.get(var1);
   }

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof DomainVersion) {
         DomainVersion var2 = (DomainVersion)var1;
         return this.deploymentsVersionMap.equals(var2.getDeploymentsVersionMap());
      } else {
         return false;
      }
   }

   public final int hashCode() {
      return this.deploymentsVersionMap.hashCode();
   }

   public final DomainVersion getCopy() {
      return new DomainVersion(this.deploymentsVersionMap);
   }

   public final Map getDifferences(DomainVersion var1) {
      HashMap var2 = new HashMap();
      if (var1 == null) {
         var2.put("ADDITIONS", this.deploymentsVersionMap);
      } else {
         HashMap var3 = new HashMap();
         HashMap var4 = new HashMap();
         HashMap var5 = new HashMap();
         Map var6 = var1.getDeploymentsVersionMap();
         Iterator var7;
         String var8;
         if (var6.size() > this.deploymentsVersionMap.size()) {
            var7 = var6.keySet().iterator();

            while(var7.hasNext()) {
               var8 = (String)var7.next();
               if (this.deploymentsVersionMap.containsKey(var8)) {
                  if (!var6.get(var8).equals(this.deploymentsVersionMap.get(var8))) {
                     var5.put(var8, this.deploymentsVersionMap.get(var8));
                  }
               } else {
                  var4.put(var8, var6.get(var8));
               }
            }
         } else {
            var7 = this.deploymentsVersionMap.keySet().iterator();

            while(var7.hasNext()) {
               var8 = (String)var7.next();
               if (var6.containsKey(var8)) {
                  if (!this.deploymentsVersionMap.get(var8).equals(var6.get(var8))) {
                     var5.put(var8, this.deploymentsVersionMap.get(var8));
                  }
               } else {
                  var3.put(var8, this.deploymentsVersionMap.get(var8));
               }
            }
         }

         var2.put("ADDITIONS", var3);
         var2.put("DELETIONS", var4);
         var2.put("CHANGES", var5);
      }

      return var2;
   }

   public DomainVersion getFilteredVersion(Set var1) {
      DomainVersion var2 = this;
      if (var1 != null && !var1.equals(this.deploymentsVersionMap.keySet())) {
         var2 = this.getCopy();
         var2.getDeploymentsVersionMap().keySet().retainAll(var1);
      }

      return var2;
   }

   public final String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString()).append(": [");
      Iterator var2 = this.deploymentsVersionMap.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.append(" { Deployment: ");
         var1.append("'");
         var1.append(var3);
         var1.append("'");
         var1.append(" : v = '");
         var1.append(this.deploymentsVersionMap.get(var3));
         var1.append("' }, ");
      }

      var1.append("]");
      return var1.toString();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.deploymentsVersionMap);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.deploymentsVersionMap.putAll((Map)var1.readObject());
   }

   private boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   private void debug(String var1) {
      Debug.serviceDebug(var1);
   }
}
