package weblogic.wsee.policy.deployment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;
import weblogic.utils.StringUtils;
import weblogic.wsee.deploy.WsdlAddressInfo;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.util.PolicyHelper;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlWriter;

public class PolicyURIs {
   private static final boolean verbose = Verbose.isVerbose(PolicyURIs.class);
   private static final boolean debug = false;
   private static boolean attachPolicyToWsdl = true;
   private Map knownURIs = new HashMap();

   public PolicyURIs(URI var1) {
      this.addURI(var1);
   }

   public PolicyURIs(URI[] var1) {
      URI[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         URI var5 = var2[var4];
         this.addURI(var5);
      }

   }

   public PolicyURIs(String var1) throws URISyntaxException {
      assert var1 != null && var1.length() > 0;

      String[] var2 = StringUtils.splitCompletely(var1, ", \t\n");

      assert var2.length > 0;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         URI var4 = new URI(var2[var3]);
         this.addURI(var4);
      }

   }

   public PolicyURIs(String[] var1) throws URISyntaxException {
      assert var1 != null && var1.length > 0;

      for(int var2 = 0; var2 < var1.length; ++var2) {
         URI var3 = new URI(var1[var2]);
         this.addURI(var3);
      }

   }

   public URI[] getURIs() {
      return (URI[])((URI[])this.knownURIs.values().toArray(new URI[0]));
   }

   public void addURI(URI var1) {
      this.knownURIs.put(var1, var1);
   }

   public void removeURI(URI var1) {
      this.knownURIs.remove(var1);
   }

   public String getKey() {
      return "PolicyURIs";
   }

   public void write(Element var1, WsdlWriter var2) {
      if (this.knownURIs != null && this.knownURIs.size() != 0) {
         WsdlAddressInfo var3 = var2.getWsdlAddressInfo();
         String var4;
         if (PolicyHelper.hasWsp15NamespaceUri(var1)) {
            var4 = "http://www.w3.org/ns/ws-policy";
         } else {
            var4 = "http://schemas.xmlsoap.org/ws/2004/09/policy";
         }

         var2.createPrefix(var4);
         var2.setAttribute(var1, "PolicyURIs", var4, attachPolicyToWsdl ? this.replaceURIsWithId(var2) : this.replaceURIsWithHostAndPort(var3));
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      URI[] var2 = this.getURIs();
      var1.append(var2[0]);

      for(int var3 = 1; var3 < var2.length; ++var3) {
         var1.append(" ");
         var1.append(var2[var3].toString());
      }

      return var1.toString();
   }

   private String replaceURIsWithHostAndPort(WsdlAddressInfo var1) {
      URI[] var2 = this.getURIs();
      if (var1 == null) {
         return this.toString();
      } else {
         String var3 = var1.getPolicyURL();
         String var4 = replaceURIHostAndPort(var2[0], var3);
         if (verbose) {
            Verbose.log((Object)("replace policy uri:  " + var2[0].toString() + " by  " + var4));
         }

         for(int var5 = 1; var5 < var2.length; ++var5) {
            var4 = var4 + " ";
            var4 = var4 + replaceURIHostAndPort(var2[var5], var3);
         }

         return var4;
      }
   }

   private static String replaceURIHostAndPort(URI var0, String var1) {
      return var0.getScheme() != null && var0.getScheme().equals("policy") ? var1 + var0.getSchemeSpecificPart() : var0.toString();
   }

   private String replaceURIsWithId(WsdlWriter var1) {
      URI[] var2 = this.getURIs();
      if (var2 == null) {
         return null;
      } else {
         String var3 = replaceURIWithId(var2[0], var1);
         if (verbose) {
            Verbose.log((Object)("replace policy uri:  " + var2[0].toString() + " by  " + var3));
         }

         for(int var4 = 1; var4 < var2.length; ++var4) {
            var3 = var3 + " ";
            var3 = var3 + replaceURIWithId(var2[var4], var1);
         }

         return var3;
      }
   }

   protected static String replaceURIWithId(URI var0, WsdlWriter var1) {
      Map var2 = var1.getAttachedPolices();
      String var3 = var0.getScheme();
      String var4 = var0.getSchemeSpecificPart();
      if (var3 != null && var3.equals("policy")) {
         PolicyStatement var5 = (PolicyStatement)var2.get(var4);
         if (var5 != null) {
            return "#" + var5.getId();
         }

         WsdlAddressInfo var6 = var1.getWsdlAddressInfo();
         if (var6 != null) {
            String var7 = var6.getPolicyURL();
            return replaceURIHostAndPort(var0, var7);
         }
      }

      return var0.toString();
   }
}
