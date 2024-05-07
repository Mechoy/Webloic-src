package weblogic.wsee.policy.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.deployment.WsdlPolicySubject;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.schema.PolicySchemaValidationException;
import weblogic.wsee.policy.schema.PolicySchemaValidator;
import weblogic.wsee.wsdl.WsdlPort;

public class PolicyServer {
   private static final boolean debug = false;
   private Map cachedPolicies;
   private boolean isCaching;
   private ArrayList policyFinders;
   private PolicySchemaValidator schemaValidator;
   private ArrayList policyCustomizers;
   private Map<QName, NormalizedExpression> endpointPolicies;

   public PolicyServer() {
      this.cachedPolicies = new HashMap();
      this.isCaching = true;
      this.policyFinders = new ArrayList();
      this.policyCustomizers = new ArrayList();
      this.endpointPolicies = new ConcurrentHashMap();
      this.policyFinders.add(new LibraryPolicyFinder());
      this.policyFinders.add(BuiltinPolicyFinder.getInstance());
   }

   public PolicyServer(Map var1) {
      this(true, var1);
   }

   public PolicyServer(boolean var1, Map var2) {
      this();
      this.isCaching = var1;
      if (var2 != null) {
         this.cachedPolicies.putAll(var2);
      }

   }

   public boolean isCaching() {
      return this.isCaching;
   }

   public Map getCachedPolicies() {
      return this.cachedPolicies;
   }

   public void addPolicies(Map var1) {
      this.cachedPolicies.putAll(var1);
   }

   public PolicyStatement getPolicy(String var1) throws PolicyException {
      return this.getPolicy(var1, true);
   }

   public PolicyStatement getPolicy(String var1, boolean var2) throws PolicyException {
      if (var1 != null && var1.length() != 0) {
         PolicyStatement var3 = null;
         if (this.isCaching) {
            synchronized(this.cachedPolicies) {
               var3 = this.getPolicyFromCache(var1);
            }

            if (var3 != null) {
               return var3;
            }
         } else {
            var3 = this.getPolicyFromCache(var1);
         }

         if (var3 == null) {
            var3 = this.loadPolicy(var1);
         }

         if (var3 == null) {
            throw new PolicyException("Could not find policy named \"" + var1 + "\"");
         } else {
            if (this.isCaching) {
               synchronized(this.cachedPolicies) {
                  if (this.getPolicyFromCache(var1) == null) {
                     this.cachedPolicies.put(var1, var3);
                  }
               }
            }

            return var3;
         }
      } else {
         throw new AssertionError();
      }
   }

   public PolicyStatement getPolicyFromCache(String var1) {
      if (var1 == null) {
         return null;
      } else {
         PolicyStatement var2 = (PolicyStatement)this.cachedPolicies.get(var1);
         if (var2 == null && var1.endsWith(".xml")) {
            String var3 = var1.substring(0, var1.lastIndexOf(46));
            var2 = (PolicyStatement)this.cachedPolicies.get(var3);
         }

         return var2;
      }
   }

   public PolicyStatement lookupPolicy(String var1) {
      try {
         if (var1.startsWith("policy:")) {
            var1 = var1.substring("policy:".length());
         }

         String var2 = var1.replace('\\', '/');
         int var3 = var2.lastIndexOf(47);
         if (var3 >= 0) {
            var2 = var2.substring(var3 + 1);
         }

         PolicyStatement var4 = this.getPolicyFromCache(var2);
         return var4 != null ? var4 : this.loadPolicy(var1);
      } catch (PolicyException var5) {
         return null;
      }
   }

   private PolicyStatement loadPolicy(String var1) throws PolicyException {
      PolicyStatement var2 = null;

      for(int var3 = 0; var3 < this.policyFinders.size(); ++var3) {
         PolicyFinder var4 = (PolicyFinder)this.policyFinders.get(var3);
         var2 = var4.findPolicy(var1, (String)null);
         if (var2 != null) {
            break;
         }
      }

      if (var2 == null) {
         throw new PolicyException("Unable to find policy: \"" + var1 + "\", please make sure to use dynamic wsdl when initializing the service stub");
      } else {
         return this.processAssertions(var1, var2);
      }
   }

   public void addPolicyFinder(PolicyFinder var1) {
      this.policyFinders.add(var1);
   }

   public void addPolicyCustomizer(PolicyCustomizer var1) {
      this.policyCustomizers.add(var1);
   }

   public PolicyStatement processAssertions(String var1, PolicyStatement var2) throws PolicyException {
      for(int var3 = 0; var3 < this.policyCustomizers.size(); ++var3) {
         ((PolicyCustomizer)this.policyCustomizers.get(var3)).process(var1, var2);
      }

      return var2;
   }

   public PolicySchemaValidator getPolicySchemaValidator() throws PolicySchemaValidationException {
      if (this.schemaValidator == null) {
         this.schemaValidator = new PolicySchemaValidator();
      }

      return this.schemaValidator;
   }

   public NormalizedExpression getEndpointPolicy(WsdlPort var1) throws PolicyException {
      QName var2 = var1.getName();
      NormalizedExpression var3 = (NormalizedExpression)this.endpointPolicies.get(var2);
      if (var3 == null) {
         var3 = WsdlPolicySubject.getEndpointPolicySubject(this, var1, this.getCachedPolicies());
      }

      return var3;
   }

   public List getPolicyList() {
      Set var1 = this.cachedPolicies.keySet();
      if (null != var1 && !var1.isEmpty()) {
         Iterator var2 = var1.iterator();
         ArrayList var3 = new ArrayList();

         while(var2.hasNext()) {
            var3.add(var2.next());
         }

         return var3;
      } else {
         return null;
      }
   }
}
