package weblogic.wsee.policy.deployment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import weblogic.wsee.addressing.policy.internal.UsingAddressingPolicyAssertionFactory;
import weblogic.wsee.mc.internal.McPolicyAssertionFactory;
import weblogic.wsee.mtom.internal.MtomPolicyAssertionFactory;
import weblogic.wsee.policy.factory.DefaultPolicyAssertionFactory;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.provider.ClientConfigurationHandler;
import weblogic.wsee.policy.provider.PolicyProvider;
import weblogic.wsee.policy.provider.PolicyValidationHandler;
import weblogic.wsee.policy.provider.ServiceConfigurationHandler;
import weblogic.wsee.reliability.policy.ReliabilityPolicyAssertionsFactory;
import weblogic.wsee.reliability.policy.WsrmClientConfigHandler;
import weblogic.wsee.reliability.policy.WsrmServiceConfigHandler;
import weblogic.wsee.security.policy.assertions.SecurityPolicyAssertionFactory;
import weblogic.wsee.security.policy.assertions.SecurityPolicyDeploymentValidationHandler;
import weblogic.wsee.security.policy.assertions.WssServiceConfigHandler;
import weblogic.wsee.security.policy11.assertions.SecurityPolicy11AssertionFactory;
import weblogic.wsee.security.policy12.assertions.SecurityPolicy122007AssertionFactory;
import weblogic.wsee.security.policy12.assertions.SecurityPolicy12AssertionFactory;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wstx.wsat.policy.ATPolicyAssertionFactory;

public class ProviderRegistry {
   private static final boolean verbose = Verbose.isVerbose(ProviderRegistry.class);
   private static ProviderRegistry theRegistry = new ProviderRegistry();
   private Set providers = new LinkedHashSet();
   private PolicyProvider defaultProvider;

   private ProviderRegistry() {
      PolicyProvider var1 = new PolicyProvider(new ReliabilityPolicyAssertionsFactory(), new WsrmClientConfigHandler(), new WsrmServiceConfigHandler(), (PolicyValidationHandler)null);
      this.deployProvider(var1);
      PolicyProvider var2 = new PolicyProvider(SecurityPolicy12AssertionFactory.getInstance(), (ClientConfigurationHandler)null, (ServiceConfigurationHandler)null, (PolicyValidationHandler)null);
      this.deployProvider(var2);
      PolicyProvider var3 = new PolicyProvider(new SecurityPolicyAssertionFactory(), (ClientConfigurationHandler)null, new WssServiceConfigHandler(), new SecurityPolicyDeploymentValidationHandler());
      this.deployProvider(var3);
      PolicyProvider var4 = new PolicyProvider(new SecurityPolicy11AssertionFactory(), (ClientConfigurationHandler)null, (ServiceConfigurationHandler)null, (PolicyValidationHandler)null);
      this.deployProvider(var4);
      PolicyProvider var5 = new PolicyProvider(new SecurityPolicy122007AssertionFactory(), (ClientConfigurationHandler)null, (ServiceConfigurationHandler)null, (PolicyValidationHandler)null);
      this.deployProvider(var5);
      PolicyProvider var6 = new PolicyProvider(new MtomPolicyAssertionFactory(), (ClientConfigurationHandler)null, (ServiceConfigurationHandler)null, (PolicyValidationHandler)null);
      this.deployProvider(var6);
      PolicyProvider var7 = new PolicyProvider(new McPolicyAssertionFactory(), (ClientConfigurationHandler)null, (ServiceConfigurationHandler)null, (PolicyValidationHandler)null);
      this.deployProvider(var7);
      PolicyProvider var8 = new PolicyProvider(new UsingAddressingPolicyAssertionFactory(), (ClientConfigurationHandler)null, (ServiceConfigurationHandler)null, (PolicyValidationHandler)null);
      this.deployProvider(var8);
      PolicyProvider var9 = new PolicyProvider(new ATPolicyAssertionFactory(), (ClientConfigurationHandler)null, (ServiceConfigurationHandler)null, (PolicyValidationHandler)null);
      this.deployProvider(var9);
      this.defaultProvider = new PolicyProvider(new DefaultPolicyAssertionFactory(), (ClientConfigurationHandler)null, (ServiceConfigurationHandler)null, (PolicyValidationHandler)null);
   }

   public void deployProviders(PolicyProvider[] var1) {
      assert this.providers != null;

      this.providers.addAll(Arrays.asList((Object[])var1));
   }

   public void undeployProviders(PolicyProvider[] var1) {
      assert this.providers != null;

      this.providers.removeAll(Arrays.asList((Object[])var1));
   }

   public void deployProvider(PolicyProvider var1) {
      if (verbose) {
         Verbose.logArgs("provider", var1);
      }

      assert var1 != null;

      this.providers.add(var1);
   }

   public void undeployProvider(PolicyProvider var1) {
      assert var1 != null;

      this.providers.remove(var1);
   }

   public boolean isEmpty() {
      return this.providers.size() == 0;
   }

   public Iterator iterateProviders() {
      Iterator var1 = this.providers.iterator();
      ArrayList var2 = new ArrayList();

      while(var1.hasNext()) {
         var2.add(var1.next());
      }

      var2.add(this.defaultProvider);
      return var2.iterator();
   }

   public static ProviderRegistry getTheRegistry() throws PolicyException {
      return theRegistry;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Policy Registry:\n");
      Iterator var2 = this.providers.iterator();

      while(var2.hasNext()) {
         PolicyProvider var3 = (PolicyProvider)var2.next();
         var1.append(var3.toString());
      }

      return var1.toString();
   }
}
