package weblogic.wsee.policy.framework;

import java.io.ByteArrayInputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import weblogic.wsee.util.Verbose;

public class PolicyAlternative implements Externalizable {
   private static final long serialVersionUID = -5879815143243327105L;
   public static final PolicyAlternative EMPTY_ALTERNATIVE = new PolicyAlternative();
   private static final boolean verbose = Verbose.isVerbose(PolicyAlternative.class);
   private static final boolean debug = false;
   private Set assertions = new LinkedHashSet();
   private Set cacheClasses = new HashSet();
   private HashMap cacheClassAssertions = new HashMap();

   public PolicyAlternative() {
   }

   PolicyAlternative(PolicyAssertion[] var1) {
      this.assertions.addAll(Arrays.asList((Object[])var1));
   }

   void addAssertions(Set var1) {
      assert var1 != null;

      this.assertions.addAll(var1);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.removeCachedKeys((PolicyAssertion)var2.next());
      }

   }

   void addAssertion(PolicyAssertion var1) {
      assert var1 != null;

      this.assertions.add(var1);
      this.removeCachedKeys(var1);
   }

   void removeAssertion(PolicyAssertion var1) {
      assert var1 != null;

      this.assertions.remove(var1);
      this.removeCachedKeys(var1);
   }

   void removeCachedKeys(PolicyAssertion var1) {
      Iterator var2 = this.cacheClassAssertions.keySet().iterator();
      Class var3 = null;

      while(var2.hasNext()) {
         var3 = (Class)var2.next();
         if (var3.isInstance(var1)) {
            this.cacheClassAssertions.remove(var3);
         }
      }

   }

   void clearAssertions() {
      this.assertions.clear();
      this.cacheClassAssertions.clear();
   }

   public Set getAssertions() {
      return this.assertions;
   }

   public boolean isTrue(PolicyAssertion var1) {
      assert var1 != null;

      return this.assertions.contains(var1);
   }

   public Set getAssertions(Class var1) {
      assert var1 != null;

      if (this.cacheClassAssertions.containsKey(var1)) {
         return (Set)this.cacheClassAssertions.get(var1);
      } else {
         LinkedHashSet var2 = new LinkedHashSet();
         Iterator var3 = this.assertions.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (var1.isInstance(var4)) {
               var2.add(var4);
            }
         }

         this.cacheClassAssertions.put(var1, var2);
         return var2;
      }
   }

   public boolean isEmpty() {
      return this.assertions.isEmpty();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append('{');
      Iterator var2 = this.assertions.iterator();
      if (var2.hasNext()) {
         var1.append(var2.next().toString());
      }

      while(var2.hasNext()) {
         var1.append(',');
         var1.append(var2.next().toString());
      }

      var1.append('}');
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof PolicyAlternative)) {
         return false;
      } else {
         PolicyAlternative var2 = (PolicyAlternative)var1;
         return this.assertions.equals(var2.assertions);
      }
   }

   public int hashCode() {
      return this.assertions.hashCode();
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.assertions = ExternalizationUtils.readAssertions(var1);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      ExternalizationUtils.writeAssertions(this.assertions, var1);
   }

   public PolicyAlternative clone() {
      try {
         byte[] var1 = ExternalizationUtils.toByteArray(this);
         ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
         ObjectInputStream var3 = new ObjectInputStream(var2);
         PolicyAlternative var4 = new PolicyAlternative();
         var4.readExternal(var3);
         return var4;
      } catch (IOException var5) {
         var5.printStackTrace();
         Verbose.log("Clone policy IO Error", var5);
      } catch (ClassNotFoundException var6) {
         var6.printStackTrace();
         Verbose.log("Clone policy ClassNotFoundException Error", var6);
      }

      return null;
   }
}
