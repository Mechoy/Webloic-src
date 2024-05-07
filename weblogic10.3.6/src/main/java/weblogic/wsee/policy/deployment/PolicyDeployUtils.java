package weblogic.wsee.policy.deployment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceProvider;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.OperationPolicyBean;
import weblogic.j2ee.descriptor.wl.OwsmSecurityPolicyBean;
import weblogic.j2ee.descriptor.wl.PortPolicyBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.j2ee.descriptor.wl.WsPolicyBean;
import weblogic.j2ee.descriptor.wl.validators.WseePolicyBeanValidator;
import weblogic.jws.Policies;
import weblogic.jws.Policy;
import weblogic.jws.Policy.Direction;
import weblogic.utils.jars.RandomAccessJarFile;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicies;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicy;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.tools.jws.decl.PolicyDecl;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlExtensible;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;

public final class PolicyDeployUtils {
   private static boolean verbose = Verbose.isVerbose(PolicyDeployUtils.class);
   public static final String BOTH = "both";
   public static final String INBOUND = "inbound";
   public static final String OUTBOUND = "outbound";
   public static final String ENABLED = "enabled";
   public static final String DISABLED = "disabled";
   public static final String DELETED = "deleted";

   public static String getXPointerId(String var0) {
      StringBuffer var1 = new StringBuffer(var0.length());

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         if (var3 == '=' || var3 == '+' || var3 == '/') {
            var3 = '_';
         }

         var1.append(var3);
      }

      return var1.toString();
   }

   public static PolicyReferenceWsdlExtension addPolicyReferenceToWsdlExtensible(WsdlExtensible var0, URI var1) {
      if (var0 == null) {
         return new PolicyReferenceWsdlExtension();
      } else {
         PolicyReferenceWsdlExtension var2 = (PolicyReferenceWsdlExtension)var0.getExtension("PolicyReference");
         if (var2 == null) {
            var2 = new PolicyReferenceWsdlExtension();
            var0.putExtension(var2);
         }

         if (!var2.getURIs().contains(var1)) {
            var2.addURI(var1);
         }

         return var2;
      }
   }

   private static PolicyReferenceWsdlExtension removePolicyFromWsdlExtensible(WsdlExtensible var0, URI var1) {
      PolicyReferenceWsdlExtension var2 = getPolicyExtension(var0);
      if (var2 != null && var2.getURIs() != null && var2.getURIs().contains(var1)) {
         var2.removeURI(var1);
      }

      return var2;
   }

   private static PolicyReferenceWsdlExtension removePolicy(WsdlPort var0, URI var1) {
      PolicyReferenceWsdlExtension var2 = removePolicyFromWsdlExtensible(var0, var1);
      PolicyURIs var3 = var0.getPortType().getPolicyUris();
      var3.removeURI(var1);
      return var2;
   }

   private static void removePolicy(WsdlPort var0, URI var1, String var2) {
      Iterator var3 = var0.getBinding().getOperations().values().iterator();

      while(var3.hasNext()) {
         WsdlBindingOperation var4 = (WsdlBindingOperation)var3.next();
         if (var2.equals("inbound")) {
            removePolicyFromWsdlExtensible(var4.getInput(), var1);
         } else {
            removePolicyFromWsdlExtensible(var4.getOutput(), var1);
         }
      }

   }

   private static void removePolicy(WsdlBindingOperation var0, URI var1, String var2) {
      if (var2.equals("inbound")) {
         removePolicyFromWsdlExtensible(var0.getInput(), var1);
      } else {
         removePolicyFromWsdlExtensible(var0.getOutput(), var1);
      }

   }

   private static List<URI> getPolicies(WsdlPort var0, String var1) {
      if ("both".equals(var1)) {
         return getPolicies(var0);
      } else {
         List var2 = null;
         Iterator var3 = var0.getBinding().getOperations().values().iterator();
         if (var3.hasNext()) {
            var2 = getPolicies((WsdlBindingOperation)var3.next(), var1);
         }

         if (var2 != null) {
            while(true) {
               List var4;
               do {
                  if (!var3.hasNext()) {
                     return var2;
                  }

                  var4 = getPolicies((WsdlBindingOperation)var3.next(), var1);
               } while(var4 == null);

               Iterator var5 = var2.iterator();

               while(var5.hasNext()) {
                  URI var6 = (URI)var5.next();
                  if (!var4.contains(var6)) {
                     var2.remove(var6);
                  }
               }
            }
         } else {
            return var2;
         }
      }
   }

   private static List<URI> getPolicies(WsdlBindingOperation var0, String var1) {
      if ("inbound".equals(var1)) {
         return getPoliciesFromWsdlExtensible(var0.getInput());
      } else {
         return "outbound".equals(var1) ? getPoliciesFromWsdlExtensible(var0.getOutput()) : null;
      }
   }

   private static List<URI> getPoliciesFromWsdlExtensible(WsdlExtensible var0) {
      PolicyReferenceWsdlExtension var1 = var0 == null ? null : getPolicyExtension(var0);
      return var1 != null ? var1.getURIs() : null;
   }

   private static List<URI> getPolicies(WsdlPort var0) {
      ArrayList var1 = new ArrayList();
      List var2 = getPoliciesFromWsdlExtensible(var0);
      if (var2 != null) {
         var1.addAll(var2);
      }

      PolicyURIs var3 = var0.getPortType().getPolicyUris();
      if (var3 != null && var3.getURIs() != null) {
         URI[] var4 = var3.getURIs();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            URI var7 = var4[var6];
            var1.add(var7);
         }
      }

      return var1;
   }

   private static PolicyReferenceWsdlExtension getPolicyExtension(WsdlExtensible var0) {
      return (PolicyReferenceWsdlExtension)var0.getExtension("PolicyReference");
   }

   public static PolicyURIs addPolicyURIToPortType(WsdlPortType var0, URI var1) {
      PolicyURIs var2 = var0.getPolicyUris();
      if (var2 == null) {
         if (verbose) {
            Verbose.log((Object)("Add PolicyURIs on port: " + var0.getName()));
         }

         var2 = new PolicyURIs(var1);
      } else {
         var2.addURI(var1);
      }

      var0.setPolicyUris(var2);
      return var2;
   }

   public static PolicyURIs addPoliyURIToPort(WsdlPort var0, URI var1, String var2) {
      PolicyURIs var3 = null;
      if (var1 == null) {
         try {
            var1 = new URI("policy:" + var2);
         } catch (URISyntaxException var5) {
            var5.printStackTrace();
         }
      }

      var3 = var0.getPolicyUris();
      if (var3 == null) {
         if (verbose) {
            Verbose.log((Object)("Add PolicyURIs on port: " + var0.getName()));
         }

         var3 = new PolicyURIs(var1);
      } else {
         var3.addURI(var1);
      }

      var0.setPolicyUris(var3);
      return var3;
   }

   public static PolicyURIs addPoliyURIToOperation(WsdlBindingOperation var0, URI var1, String var2) {
      PolicyURIs var3 = null;
      if (var1 == null) {
         try {
            var1 = new URI("policy:" + var2);
         } catch (URISyntaxException var5) {
            var5.printStackTrace();
         }
      }

      var3 = var0.getPolicyUris();
      if (var3 == null) {
         if (verbose) {
            Verbose.log((Object)("Add PolicyURIs on operation: " + var0.getName()));
         }

         var3 = new PolicyURIs(var1);
      } else {
         var3.addURI(var1);
      }

      var0.setPolicyUris(var3);
      return var3;
   }

   public static PolicyURIs addPoliyURIToMessage(WsdlBindingMessage var0, URI var1, String var2) {
      PolicyURIs var3 = null;
      if (var1 == null) {
         try {
            var1 = new URI("policy:" + var2);
         } catch (URISyntaxException var5) {
            var5.printStackTrace();
         }
      }

      var3 = var0.getPolicyUris();
      if (var3 == null) {
         if (verbose) {
            Verbose.log((Object)("Add PolicyURIs on message: " + var0.getName()));
         }

         var3 = new PolicyURIs(var1);
      } else {
         var3.addURI(var1);
      }

      var0.setPolicyUris(var3);
      return var3;
   }

   public static ArrayList<String> getPoliciesFromAnnotatedElement(AnnotatedElement var0) {
      return getPoliciesFromAnnotatedElement(var0, (Policy.Direction)null);
   }

   public static ArrayList<String> getPoliciesFromAnnotatedElement(AnnotatedElement var0, Policy.Direction var1) {
      ArrayList var2 = new ArrayList();
      Policies var3 = (Policies)var0.getAnnotation(Policies.class);
      if (var3 != null) {
         Policy[] var4 = var3.value();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Policy var7 = var4[var6];
            if (var1 == null || var1.equals(var7.direction())) {
               var2.add(var7.uri());
            }
         }
      } else {
         Policy var8 = (Policy)var0.getAnnotation(Policy.class);
         if (var8 != null && (var1 == null || var1.equals(var8.direction()))) {
            var2.add(var8.uri());
         }
      }

      return var2;
   }

   public static ArrayList<String> getOwsmSecurityPoliciesFromAnnotatedElement(AnnotatedElement var0) {
      ArrayList var1 = new ArrayList();
      SecurityPolicies var2 = (SecurityPolicies)var0.getAnnotation(SecurityPolicies.class);
      if (var2 != null) {
         SecurityPolicy[] var3 = var2.value();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            SecurityPolicy var6 = var3[var5];
            var1.add(var6.uri());
         }
      } else {
         SecurityPolicy var7 = (SecurityPolicy)var0.getAnnotation(SecurityPolicy.class);
         if (var7 != null) {
            var1.add(var7.uri());
         }
      }

      return var1;
   }

   public static String[] getPoliciesFromPort(WebservicePolicyRefBean var0, String var1, Class var2) {
      ArrayList var3 = new ArrayList();
      if (var2 != null) {
         var3.addAll(getPoliciesFromAnnotatedElement(var2));
      }

      PortPolicyBean[] var4 = var0.getPortPolicy();

      for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
         if (var4[var5].getPortName().equals(var1)) {
            WsPolicyBean[] var6 = var4[var5].getWsPolicy();
            updatePolicies((List)var3, (WsPolicyBean[])var6);
         }
      }

      return (String[])((String[])var3.toArray(new String[0]));
   }

   private static void updatePolicies(List<String> var0, WsPolicyBean[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         WsPolicyBean var3 = var1[var2];
         if (var3.getStatus().equals("enabled") && !var0.contains(var3.getUri())) {
            var0.add(var3.getUri());
         }

         if (var3.getStatus().equals("disabled") || var3.getStatus().equals("deleted")) {
            var0.remove(var3.getUri());
         }
      }

   }

   private static void updatePolicies(List<String> var0, OwsmSecurityPolicyBean[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         OwsmSecurityPolicyBean var3 = var1[var2];
         if (var3.getStatus().equals("enabled") && !var0.contains(var3.getUri())) {
            var0.add(var3.getUri());
         }

         if (var3.getStatus().equals("disabled") || var3.getStatus().equals("deleted")) {
            var0.remove(var3.getUri());
         }
      }

   }

   private static void updatePolicies(String var0, List<String> var1, WsPolicyBean[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var0 == null) {
            if (var2[var3].getDirection() != null) {
               continue;
            }
         } else if (!var0.equals(var2[var3].getDirection())) {
            continue;
         }

         if (var2[var3].getStatus().equals("enabled") && !var1.contains(var2[var3].getUri())) {
            var1.add(var2[var3].getUri());
         }
      }

      if (var0 == null) {
         var0 = "both";
      }

      removePolicies(var1, var2, var0);
   }

   private static void removePolicies(List<String> var0, WsPolicyBean[] var1, String var2) {
      ArrayList var3 = new ArrayList();
      var3.addAll(var0);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();

         for(int var6 = 0; var6 < var1.length; ++var6) {
            WsPolicyBean var7 = var1[var6];
            if (urisEquivalent(var7.getUri(), var5)) {
               if (var2.equals("both")) {
                  if (!var7.getStatus().equals("enabled")) {
                     var0.remove(var5);
                  }
               } else if (!var7.getStatus().equals("enabled")) {
                  if (var2.equals(var7.getDirection())) {
                     var0.remove(var5);
                  }
               } else if (!var2.equals(var7.getDirection())) {
                  var0.remove(var5);
               }
            }
         }
      }

   }

   private static boolean urisEquivalent(String var0, String var1) {
      if (var0.equals(var1)) {
         return true;
      } else {
         if (var0.endsWith(".xml")) {
            String var2 = var0.substring(0, var0.length() - 4);
            if (var2.equals(var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public static String[] getPoliciesFromPort(WebservicePolicyRefBean var0, String var1) {
      return getPoliciesFromPort(var0, var1, (Class)null);
   }

   public static String[] getOwsmSecurityPoliciesFromPort(WebservicePolicyRefBean var0, String var1, Class var2) {
      ArrayList var3 = new ArrayList();
      if (var2 != null) {
         var3.addAll(getOwsmSecurityPoliciesFromAnnotatedElement(var2));
      }

      PortPolicyBean[] var4 = var0.getPortPolicy();

      for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
         if (var4[var5].getPortName().equals(var1)) {
            OwsmSecurityPolicyBean[] var6 = var4[var5].getOwsmSecurityPolicy();
            updatePolicies((List)var3, (OwsmSecurityPolicyBean[])var6);
         }
      }

      return (String[])((String[])var3.toArray(new String[0]));
   }

   public static String[] getPoliciesFromOperation(WebservicePolicyRefBean var0, String var1, String var2, Class var3, String var4) {
      ArrayList var5 = new ArrayList();
      if (var3 != null && var3.getAnnotation(WebServiceProvider.class) == null) {
         Method var6 = findWebMethodFromService(var3, var1);
         var5.addAll(getPoliciesFromAnnotatedElement(var6, Direction.valueOf(var2)));
      }

      OperationPolicyBean[] var11 = var0.getOperationPolicy();
      if (var11 != null && var11.length > 0) {
         for(int var7 = 0; var7 < var11.length; ++var7) {
            String var8 = var11[var7].getServiceLink();
            String var9 = var11[var7].getOperationName();
            if ((StringUtil.isEmpty(var8) || !StringUtil.isEmpty(var8) && var8.equals(var4)) && var9.equals(var1)) {
               WsPolicyBean[] var10 = var11[var7].getWsPolicy();
               updatePolicies(var2, var5, var10);
            }
         }
      }

      return (String[])((String[])var5.toArray(new String[0]));
   }

   public static String[] getPoliciesFromOperation(WebservicePolicyRefBean var0, String var1, String var2) {
      return getPoliciesFromOperation(var0, var1, var2, (Class)null, (String)null);
   }

   public static void attachPolicy(WebservicePolicyRefBean var0, WsdlDefinitions var1, WsdlPort var2, String var3) {
      boolean var4 = false;
      String var5 = var2.getName().getLocalPart();
      PortPolicyBean[] var6 = var0.getPortPolicy();

      for(int var7 = 0; var6 != null && var7 < var6.length; ++var7) {
         if (var6[var7].getPortName().equals(var5)) {
            WsPolicyBean[] var8 = var6[var7].getWsPolicy();
            var4 = updatePolicies(var2, var8);
         }
      }

      OperationPolicyBean[] var14 = var0.getOperationPolicy();
      int var15 = 0;

      label62:
      while(var14 != null && var15 < var14.length) {
         String var9 = var14[var15].getOperationName();
         String var10 = var14[var15].getServiceLink();
         Iterator var11 = var2.getBinding().getOperations().values().iterator();

         while(true) {
            WsdlBindingOperation var12;
            do {
               do {
                  if (!var11.hasNext()) {
                     ++var15;
                     continue label62;
                  }

                  var12 = (WsdlBindingOperation)var11.next();
               } while(!var9.equals(var12.getName().getLocalPart()));
            } while(!StringUtil.isEmpty(var10) && !var10.equals(var3));

            WsPolicyBean[] var13 = var14[var15].getWsPolicy();
            var4 = updatePolicies(var12, var13) || var4;
         }
      }

      if (var4) {
         UsingPolicy var16 = UsingPolicy.narrow(var1);
         if (var16 == null) {
            var1.putExtension(new UsingPolicy(true));
         } else if (!var16.isSet()) {
            var16.enableUsingPolicy();
         }
      }

   }

   private static boolean updatePolicies(WsdlBindingOperation var0, WsPolicyBean[] var1) {
      boolean var2 = false;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         WsPolicyBean var10000 = var1[var3];
         if (var1[var3].getStatus().equals("enabled")) {
            addPolicies(var1[var3].getDirection(), var0, var1[var3].getUri());
            var2 = true;
         }
      }

      removePolicies(var0, var1, "both");
      removePolicies(var0, var1, "inbound");
      removePolicies(var0, var1, "outbound");
      return var2;
   }

   private static boolean updatePolicies(WsdlPort var0, WsPolicyBean[] var1) {
      boolean var2 = false;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (var1[var3].getStatus().equals("enabled")) {
            addPolicies(var1[var3].getDirection(), var0, var1[var3].getUri());
            var2 = true;
         }
      }

      removePolicies(var0, var1, "both");
      removePolicies(var0, var1, "inbound");
      removePolicies(var0, var1, "outbound");
      return var2;
   }

   private static void removePolicies(WsdlPort var0, WsPolicyBean[] var1, String var2) {
      List var3 = getPolicies(var0, var2);
      if (var3 != null) {
         ArrayList var4 = new ArrayList(var3);
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            URI var6 = (URI)var5.next();

            for(int var7 = 0; var7 < var1.length; ++var7) {
               WsPolicyBean var8 = var1[var7];
               if (urisEquivalent(var8.getUri(), var6.toString())) {
                  if (var2.equals("both")) {
                     if (!var8.getStatus().equals("enabled")) {
                        removePolicy(var0, var6);
                     }
                  } else if (!var8.getStatus().equals("enabled")) {
                     if (var2.equals(var8.getDirection())) {
                        removePolicy(var0, var6, var2);
                     }
                  } else if (!var2.equals(var8.getDirection())) {
                     removePolicy(var0, var6, var2);
                  }
               }
            }
         }
      }

   }

   private static void removePolicies(WsdlBindingOperation var0, WsPolicyBean[] var1, String var2) {
      List var3 = null;
      if (var2 != null && !var2.equals("both")) {
         var3 = getPolicies(var0, var2);
      } else {
         var3 = getPoliciesFromWsdlExtensible(var0);
      }

      if (var3 != null) {
         ArrayList var4 = new ArrayList(var3);
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            URI var6 = (URI)var5.next();

            for(int var7 = 0; var7 < var1.length; ++var7) {
               WsPolicyBean var8 = var1[var7];
               if (urisEquivalent(var8.getUri(), var6.toString())) {
                  if (var2.equals("both")) {
                     if (!var8.getStatus().equals("enabled")) {
                        removePolicyFromWsdlExtensible(var0, var6);
                     }
                  } else if (!var8.getStatus().equals("enabled")) {
                     if (var2.equals(var8.getDirection())) {
                        removePolicy(var0, var6, var2);
                     }
                  } else if (!var2.equals(var8.getDirection())) {
                     removePolicy(var0, var6, var2);
                  }
               }
            }
         }
      }

   }

   private static void addPolicies(String var0, WsdlPort var1, String var2) {
      if (var0 != null && !var0.equals("both")) {
         Iterator var3 = var1.getBinding().getOperations().values().iterator();

         while(var3.hasNext()) {
            WsdlBindingOperation var4 = (WsdlBindingOperation)var3.next();
            if (var0.equals("inbound")) {
               addPolicyReferenceToWsdlExtensible(var4.getInput(), getURI(var2));
            } else {
               if (!var0.equals("outbound")) {
                  throw new AssertionError("unrecognized value of direction: " + var0);
               }

               addPolicyReferenceToWsdlExtensible(var4.getOutput(), getURI(var2));
            }
         }
      } else {
         addPolicyReferenceToWsdlExtensible(var1, getURI(var2));
      }

   }

   private static void addPolicies(String var0, WsdlBindingOperation var1, String var2) {
      if (var0 != null && !var0.equals("both")) {
         if (var0.equals("inbound")) {
            addPolicyReferenceToWsdlExtensible(var1.getInput(), getURI(var2));
         } else {
            if (!var0.equals("outbound")) {
               throw new AssertionError("unrecognized value of direction: " + var0);
            }

            addPolicyReferenceToWsdlExtensible(var1.getOutput(), getURI(var2));
         }
      } else {
         addPolicyReferenceToWsdlExtensible(var1, getURI(var2));
      }

   }

   public static void writePolicyBeanToStream(EditableDescriptorManager var0, WebservicePolicyRefBean var1, OutputStream var2) throws IOException {
      var0.writeDescriptorAsXML(((DescriptorBean)var1).getDescriptor(), var2);
      var2.flush();
      var2.close();
   }

   public static void writePolicyBeanToPath(EditableDescriptorManager var0, WebservicePolicyRefBean var1, String var2) throws IOException {
      if (var2.endsWith(".war")) {
         writePolicyBeanToWar(var0, var1, var2);
      } else if (var2.endsWith("jar")) {
         writePolicyBeanToEjb(var0, var1, var2);
      } else {
         File var3 = new File(var2);
         writePolicyBeanToFile(var0, var1, var3);
      }

   }

   public static void writePolicyBeanToFile(EditableDescriptorManager var0, WebservicePolicyRefBean var1, File var2) throws IOException {
      if (!var2.exists()) {
         File var3 = var2.getParentFile();
         if (var3 != null && !var3.exists()) {
            var3.mkdirs();
         }
      }

      BufferedOutputStream var4 = new BufferedOutputStream(new FileOutputStream(var2));
      writePolicyBeanToStream(var0, var1, var4);
   }

   public static void writePolicyBeanToWar(EditableDescriptorManager var0, WebservicePolicyRefBean var1, String var2) throws IOException {
      String var3 = ".";
      RandomAccessJarFile var4 = new RandomAccessJarFile(new File(var3), new File(var2));
      OutputStream var5 = var4.writeEntry("WEB-INF/weblogic-webservices-policy.xml", true);
      writePolicyBeanToStream(var0, var1, var5);
      var4.close();
   }

   public static void writePolicyBeanToEjb(EditableDescriptorManager var0, WebservicePolicyRefBean var1, String var2) throws IOException {
      String var3 = ".";
      RandomAccessJarFile var4 = new RandomAccessJarFile(new File(var3), new File(var2));
      OutputStream var5 = var4.writeEntry("META-INF/weblogic-webservices-policy.xml", true);
      writePolicyBeanToStream(var0, var1, var5);
      var4.close();
   }

   public static boolean addPolicyToPort(WebservicePolicyRefBean var0, String var1, String var2, String var3) {
      return addPolicyToPort(var0, var1, var2, var3, "enabled");
   }

   public static boolean addPolicyToPort(WebservicePolicyRefBean var0, Class var1, String var2, String var3, String var4) {
      return destroyOrUpdate(var0, var1, var2, var3, var4) ? true : addPolicyToPort(var0, var2, var3, var4, "enabled");
   }

   private static boolean destroyOrUpdate(WebservicePolicyRefBean var0, Class var1, String var2, String var3, String var4) {
      PortPolicyBean[] var5 = var0.getPortPolicy();
      if (var5 != null && var5.length != 0) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6].getPortName().equals(var2) && destroyOrUpdate(var5[var6], var1, var3, var4)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static boolean destroyOrUpdate(PortPolicyBean var0, Class var1, String var2, String var3) {
      WsPolicyBean[] var4 = var0.getWsPolicy();
      if (var4 != null && var4.length != 0) {
         WsPolicyBean[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            WsPolicyBean var8 = var5[var7];
            if (var8.getUri().equals(var2) && var8.getStatus().equals("deleted")) {
               ArrayList var9 = getPoliciesFromAnnotatedElement(var1, Direction.valueOf(var3));
               if (var9.contains(var2)) {
                  var0.destroyWsPolicy(var8);
               } else {
                  var8.setDirection(var3);
                  var8.setStatus("enabled");
               }

               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean addPolicyToPort(WebservicePolicyRefBean var0, String var1, String var2, String var3, String var4) {
      WseePolicyBeanValidator.checkDuplicatedPolicyForPort(var0, var1, var2);
      PortPolicyBean[] var5 = var0.getPortPolicy();
      if (var5 != null && var5.length != 0) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (var5[var6].getPortName().equals(var1)) {
               createWsPolicyBean(var5[var6], var2, var3, var4);
               return true;
            }
         }

         createPortPolicyBean(var0, var1, var2, var3, var4);
      } else {
         createPortPolicyBean(var0, var1, var2, var3, var4);
      }

      return true;
   }

   public static boolean addPolicyToPort(WebservicePolicyRefBean var0, String var1, String var2) {
      return addPolicyToPort(var0, var1, var2, "both", "enabled");
   }

   public static boolean addOwsmSecurityPolicyToPort(WebservicePolicyRefBean var0, String var1, String var2) {
      return destroyOrUpdate(var0, var1, var2) ? true : addOwsmSecurityPolicyToPort(var0, var1, var2, "enabled");
   }

   private static boolean destroyOrUpdate(WebservicePolicyRefBean var0, String var1, String var2) {
      PortPolicyBean[] var3 = var0.getPortPolicy();
      if (var3 != null && var3.length != 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].getPortName().equals(var1)) {
               OwsmSecurityPolicyBean[] var5 = var3[var4].getOwsmSecurityPolicy();
               if (var5 == null || var5.length == 0) {
                  return false;
               }

               OwsmSecurityPolicyBean[] var6 = var5;
               int var7 = var5.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  OwsmSecurityPolicyBean var9 = var6[var8];
                  if (var9.getUri().equals(var2) && var9.getStatus().equals("deleted")) {
                     var3[var4].destroyOwsmSecurityPolicy(var9);
                     return true;
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean addOwsmSecurityPolicyToPort(WebservicePolicyRefBean var0, String var1, String var2, String var3) {
      WseePolicyBeanValidator.checkDuplicatedOwsmSecurityPolicyForPort(var0, var1, var2);
      PortPolicyBean[] var4 = var0.getPortPolicy();
      if (var4 != null && var4.length != 0) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getPortName().equals(var1)) {
               WsPolicyBean[] var6 = var4[var5].getWsPolicy();
               if (var6 != null && var6.length != 0) {
                  WsPolicyBean[] var7 = var6;
                  int var8 = var6.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     WsPolicyBean var10 = var7[var9];
                     if (var10.getStatus().equals("enabled")) {
                        throw new IllegalArgumentException("OWSM Policy " + var2 + " cannot be attached to port " + var1 + " due to Ws Policy alreay exist!");
                     }
                  }
               }

               createOwsmSecurityPolicyBean(var4[var5], var2, var3);
               return true;
            }
         }

         createOwsmPortPolicyBean(var0, var1, var2, var3);
      } else {
         createOwsmPortPolicyBean(var0, var1, var2, var3);
      }

      return true;
   }

   public static boolean addPolicyToOperation(WebservicePolicyRefBean var0, Class var1, String var2, String var3, String var4, String var5) {
      return destroyOrUpdate(var0, var1, var2, var3, var4, var5) ? true : addPolicyToOperation(var0, var2, var3, var4, var5);
   }

   private static boolean destroyOrUpdate(WebservicePolicyRefBean var0, Class var1, String var2, String var3, String var4, String var5) {
      OperationPolicyBean[] var6 = var0.getOperationPolicy();
      if (var6 != null && var6.length != 0) {
         for(int var7 = 0; var7 < var6.length; ++var7) {
            if (var6[var7].getOperationName().equals(var2) && (var6[var7].getServiceLink() == null && var5 == null || var6[var7].getServiceLink().equals(var5)) && destroyOrUpdate(var6[var7], var1, var3, var4)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static boolean destroyOrUpdate(OperationPolicyBean var0, Class var1, String var2, String var3) {
      WsPolicyBean[] var4 = var0.getWsPolicy();
      if (var4 != null && var4.length != 0) {
         WsPolicyBean[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            WsPolicyBean var8 = var5[var7];
            if (var8.getUri().equals(var2) && var8.getStatus().equals("deleted")) {
               Method var9 = findWebMethodFromService(var1, var0.getOperationName());
               ArrayList var10 = getPoliciesFromAnnotatedElement(var9, Direction.valueOf(var3));
               if (var10.contains(var2)) {
                  var0.destroyWsPolicy(var8);
               } else {
                  var8.setDirection(var3);
                  var8.setStatus("enabled");
               }

               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean addPolicyToOperation(WebservicePolicyRefBean var0, String var1, String var2, String var3, String var4) {
      WseePolicyBeanValidator.checkDuplidatePolicyFroOperation(var0, var1, var2, var3, var4);
      OperationPolicyBean[] var5 = var0.getOperationPolicy();
      if (var5 != null && var5.length != 0) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            String var7 = var5[var6].getOperationName();
            String var8 = var5[var6].getServiceLink();
            if ((!StringUtil.isEmpty(var8) && var8.equals(var4) || StringUtil.isEmpty(var8)) && var7.equals(var1)) {
               if (StringUtil.isEmpty(var8)) {
                  var5[var6].setServiceLink(var4);
               }

               createWsPolicyBean(var5[var6], var2, var3);
               return true;
            }
         }

         createOperationPolicyBean(var0, var1, var2, var3, var4);
      } else {
         createOperationPolicyBean(var0, var1, var2, var3, var4);
      }

      return true;
   }

   public static void removePolicyFromPort(WebservicePolicyRefBean var0, String var1, String var2) {
      boolean var3 = false;
      PortPolicyBean[] var4 = var0.getPortPolicy();

      for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
         if (var4[var5].getPortName().equals(var1)) {
            WsPolicyBean[] var6 = var4[var5].getWsPolicy();

            for(int var7 = 0; var6 != null && var7 < var6.length; ++var7) {
               if (var6[var7].getUri().equals(var2)) {
                  var4[var5].destroyWsPolicy(var6[var7]);
                  var3 = true;
               }
            }
         }
      }

      if (!var3) {
         boolean var8 = true;
         if (var4 == null) {
            createPortPolicyBean(var0, var1, var2, "both", "deleted");
         } else {
            for(int var9 = 0; var9 < var4.length; ++var9) {
               if (var4[var9].getPortName().equals(var1)) {
                  createWsPolicyBean(var4[var9], var2, "both", "deleted");
                  return;
               }
            }

            createPortPolicyBean(var0, var1, var2, "both", "deleted");
         }
      }

   }

   public static void removeOwsmSecurityPolicyFromPort(WebservicePolicyRefBean var0, String var1, String var2) {
      boolean var3 = false;
      PortPolicyBean[] var4 = var0.getPortPolicy();

      int var5;
      for(var5 = 0; var4 != null && var5 < var4.length; ++var5) {
         if (var4[var5].getPortName().equals(var1)) {
            OwsmSecurityPolicyBean[] var6 = var4[var5].getOwsmSecurityPolicy();

            for(int var7 = 0; var6 != null && var7 < var6.length; ++var7) {
               if (var6[var7].getUri().equals(var2)) {
                  var4[var5].destroyOwsmSecurityPolicy(var6[var7]);
                  var3 = true;
               }
            }
         }
      }

      if (!var3) {
         if (var4 == null) {
            createOwsmPortPolicyBean(var0, var1, var2, "deleted");
         } else {
            for(var5 = 0; var5 < var4.length; ++var5) {
               if (var4[var5].getPortName().equals(var1)) {
                  createOwsmSecurityPolicyBean(var4[var5], var2, "deleted");
                  return;
               }
            }

            createOwsmPortPolicyBean(var0, var1, var2, "deleted");
         }
      }

   }

   public static void removePolicyFromOperation(WebservicePolicyRefBean var0, String var1, String var2) {
      removePolicyFromOperation(var0, var1, var2, "both", (String)null);
   }

   public static void removePolicyFromOperation(WebservicePolicyRefBean var0, String var1, String var2, String var3, String var4) {
      boolean var5 = false;
      OperationPolicyBean[] var6 = var0.getOperationPolicy();

      String var9;
      for(int var7 = 0; var6 != null && var7 < var6.length; ++var7) {
         String var8 = var6[var7].getOperationName();
         var9 = var6[var7].getServiceLink();
         if (StringUtil.isEmpty(var9) || !StringUtil.isEmpty(var9) && var9.equals(var4) && var8.equals(var1)) {
            WsPolicyBean[] var10 = var6[var7].getWsPolicy();

            for(int var11 = 0; var10 != null && var11 < var10.length; ++var11) {
               if (var10[var11].getUri().equals(var2) && (var3.equals("both") || var3.equals(var10[var11].getDirection()))) {
                  var6[var7].destroyWsPolicy(var10[var11]);
                  var10 = var6[var7].getWsPolicy();
                  var5 = true;
               }
            }
         }
      }

      if (!var5) {
         boolean var12 = true;
         if (var6 != null && var6.length != 0) {
            for(int var13 = 0; var13 < var6.length; ++var13) {
               var9 = var6[var13].getOperationName();
               String var14 = var6[var13].getServiceLink();
               if ((!StringUtil.isEmpty(var14) && var14.equals(var4) || StringUtil.isEmpty(var14)) && var9.equals(var1)) {
                  if (StringUtil.isEmpty(var14)) {
                     var6[var13].setServiceLink(var4);
                  }

                  createWsPolicyBean(var6[var13], var2, var3, var12);
                  return;
               }
            }

            createOperationPolicyBean(var0, var1, var2, var3, var4, var12);
         } else {
            createOperationPolicyBean(var0, var1, var2, var3, var4, var12);
         }
      }

   }

   public static void embedPoliciesInWsdl(WsdlDefinitions var0, PolicyServer var1) throws PolicyException {
      UsingPolicy var2 = UsingPolicy.narrow(var0);
      if (var2 != null && var2.isSet()) {
         PolicyWsdlExtension var3 = getPolicyWsdlExtension(var0);
         Iterator var4 = var0.getPortTypes().values().iterator();

         Iterator var6;
         while(var4.hasNext()) {
            WsdlPortType var5 = (WsdlPortType)var4.next();
            var5.setPolicyUris(replacePolicyURIs(var5.getPolicyUris(), var1, var3));
            var6 = var5.getOperations().values().iterator();

            while(var6.hasNext()) {
               WsdlOperation var7 = (WsdlOperation)var6.next();
               var7.setPolicyUris(replacePolicyURIs(var7.getPolicyUris(), var1, var3));
               replacePolicyReference(var7, var1, var3);
               WsdlMessage var8 = var7.getInput();
               if (var8 != null) {
                  var7.setInputPolicyUris(replacePolicyURIs(var7.getInputPolicyUris(), var1, var3));
               }

               WsdlMessage var9 = var7.getOutput();
               if (var9 != null) {
                  var7.setOutputPolicyUris(replacePolicyURIs(var7.getOutputPolicyUris(), var1, var3));
               }
            }
         }

         var4 = var0.getBindings().values().iterator();

         while(var4.hasNext()) {
            WsdlBinding var12 = (WsdlBinding)var4.next();
            var12.setPolicyUris(replacePolicyURIs(var12.getPolicyUris(), var1, var3));
            replacePolicyReference(var12, var1, var3);
         }

         var4 = var0.getMessages().values().iterator();

         while(var4.hasNext()) {
            WsdlMessage var13 = (WsdlMessage)var4.next();
            var13.setPolicyUris(replacePolicyURIs(var13.getPolicyUris(), var1, var3));
            replacePolicyReference(var13, var1, var3);
         }

         var4 = var0.getServices().values().iterator();

         while(var4.hasNext()) {
            WsdlService var14 = (WsdlService)var4.next();
            var6 = var14.getPorts().values().iterator();

            while(var6.hasNext()) {
               WsdlPort var15 = (WsdlPort)var6.next();
               var15.setPolicyUris(replacePolicyURIs(var15.getPolicyUris(), var1, var3));
               replacePolicyReference(var15, var1, var3);
               Iterator var16 = var15.getBinding().getOperations().values().iterator();

               while(var16.hasNext()) {
                  WsdlBindingOperation var17 = (WsdlBindingOperation)var16.next();
                  var17.setPolicyUris(replacePolicyURIs(var17.getPolicyUris(), var1, var3));
                  replacePolicyReference(var17, var1, var3);
                  WsdlBindingMessage var10 = var17.getInput();
                  if (var10 != null) {
                     var10.setPolicyUris(replacePolicyURIs(var10.getPolicyUris(), var1, var3));
                     replacePolicyReference(var10, var1, var3);
                  }

                  WsdlBindingMessage var11 = var17.getOutput();
                  if (var11 != null) {
                     var11.setPolicyUris(replacePolicyURIs(var11.getPolicyUris(), var1, var3));
                     replacePolicyReference(var17.getOutput(), var1, var3);
                  }
               }
            }
         }

      }
   }

   public static boolean isCannedMtomPolicy(PolicyDecl var0) {
      if (var0 == null) {
         return false;
      } else {
         if (var0.isBuiltInPolicy()) {
            String var1 = var0.getPolicyURI().toString();
            if (var1 != null) {
               if (var1.indexOf("policy:Mtom") >= 0) {
                  return true;
               }

               return false;
            }
         }

         return false;
      }
   }

   private static PolicyWsdlExtension getPolicyWsdlExtension(WsdlDefinitions var0) {
      PolicyWsdlExtension var1 = (PolicyWsdlExtension)var0.getExtension("Policy");
      if (var1 == null) {
         var1 = new PolicyWsdlExtension();
         var0.putExtension(var1);
      }

      return var1;
   }

   private static void replacePolicyReference(WsdlExtensible var0, PolicyServer var1, PolicyWsdlExtension var2) throws PolicyException {
      if (var0 != null) {
         PolicyReferenceWsdlExtension var3 = (PolicyReferenceWsdlExtension)var0.getExtension("PolicyReference");
         if (var3 != null) {
            ArrayList var4 = var3.getURIs();
            ArrayList var5 = new ArrayList();

            for(int var6 = 0; var6 < var4.size(); ++var6) {
               URI var7 = (URI)var4.get(var6);
               replaceURI(var7, var1, var5, var2);
            }

            var3.setURIs(var5);
         }
      }
   }

   private static PolicyURIs replacePolicyURIs(PolicyURIs var0, PolicyServer var1, PolicyWsdlExtension var2) throws PolicyException {
      if (var0 == null) {
         return var0;
      } else {
         ArrayList var3 = new ArrayList();
         URI[] var4 = var0.getURIs();
         URI[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            URI var8 = var5[var7];
            replaceURI(var8, var1, var3, var2);
         }

         return new PolicyURIs((URI[])((URI[])var3.toArray(new URI[var3.size()])));
      }
   }

   private static void replaceURI(URI var0, PolicyServer var1, ArrayList var2, PolicyWsdlExtension var3) throws PolicyException {
      if ("policy".equals(var0.getScheme())) {
         PolicyStatement var4 = var1.getPolicy(var0.getRawSchemeSpecificPart());

         try {
            URI var5 = new URI("#" + var4.getId());
            var3.addPolicy(var4);
            var2.add(var5);
         } catch (URISyntaxException var6) {
            var6.printStackTrace();
         }
      } else {
         var2.add(var0);
      }

   }

   private static WsPolicyBean createWsPolicyBean(PortPolicyBean var0, String var1, String var2) {
      return createWsPolicyBean(var0, var1, var2, "enabled");
   }

   private static WsPolicyBean createWsPolicyBean(PortPolicyBean var0, String var1, String var2, String var3) {
      WsPolicyBean[] var4 = var0.getWsPolicy();
      WsPolicyBean[] var5;
      if (var4 != null) {
         var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            WsPolicyBean var8 = var5[var7];
            if (var8.getUri().equals(var1)) {
               var8.setStatus(var3);
               if (var3.equals("enabled")) {
                  if (var2 != null) {
                     var8.setDirection(var2);
                  } else {
                     var8.setDirection("both");
                  }
               }

               return var8;
            }
         }
      }

      var5 = null;

      try {
         WsPolicyBean var10 = var0.createWsPolicy();
         var10.setUri(var1);
         if (var2 != null) {
            var10.setDirection(var2);
         }

         var10.setStatus(var3);
         return var10;
      } catch (IllegalArgumentException var9) {
         if (var5 != null) {
            var0.destroyWsPolicy(var5);
         }

         throw var9;
      }
   }

   private static OwsmSecurityPolicyBean createOwsmSecurityPolicyBean(PortPolicyBean var0, String var1) {
      return createOwsmSecurityPolicyBean(var0, var1, "enabled");
   }

   private static OwsmSecurityPolicyBean createOwsmSecurityPolicyBean(PortPolicyBean var0, String var1, String var2) {
      OwsmSecurityPolicyBean[] var3 = var0.getOwsmSecurityPolicy();
      OwsmSecurityPolicyBean[] var4;
      if (var3 != null) {
         var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            OwsmSecurityPolicyBean var7 = var4[var6];
            if (var7.getUri().equals(var1)) {
               var7.setStatus(var2);
               return var7;
            }
         }
      }

      var4 = null;

      try {
         OwsmSecurityPolicyBean var9 = var0.createOwsmSecurityPolicy();
         var9.setUri(var1);
         var9.setStatus(var2);
         return var9;
      } catch (IllegalArgumentException var8) {
         if (var4 != null) {
            var0.destroyOwsmSecurityPolicy(var4);
         }

         throw var8;
      }
   }

   private static WsPolicyBean createWsPolicyBean(OperationPolicyBean var0, String var1, String var2) {
      return createWsPolicyBean(var0, var1, var2, false);
   }

   private static WsPolicyBean createWsPolicyBean(OperationPolicyBean var0, String var1, String var2, boolean var3) {
      WsPolicyBean[] var4 = var0.getWsPolicy();
      WsPolicyBean[] var5;
      if (var4 != null) {
         var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            WsPolicyBean var8 = var5[var7];
            if (var8.getUri().equals(var1)) {
               if (!var3) {
                  var8.setStatus("enabled");
                  var8.setDirection(var2);
               } else if (var2.equals(var8.getDirection())) {
                  var8.setStatus("deleted");
               }

               return var8;
            }
         }
      }

      var5 = null;

      try {
         WsPolicyBean var10 = var0.createWsPolicy();
         var10.setUri(var1);
         if (var2 != null) {
            var10.setDirection(var2);
         }

         if (var3) {
            var10.setStatus("deleted");
         }

         return var10;
      } catch (IllegalArgumentException var9) {
         if (var5 != null) {
            var0.destroyWsPolicy(var5);
         }

         throw var9;
      }
   }

   private static PortPolicyBean createPortPolicyBean(WebservicePolicyRefBean var0, String var1, String var2, String var3, String var4) {
      PortPolicyBean var5 = var0.createPortPolicy();
      var5.setPortName(var1);
      createWsPolicyBean(var5, var2, var3, var4);
      return var5;
   }

   private static PortPolicyBean createOwsmPortPolicyBean(WebservicePolicyRefBean var0, String var1, String var2, String var3) {
      PortPolicyBean var4 = var0.createPortPolicy();
      var4.setPortName(var1);
      createOwsmSecurityPolicyBean(var4, var2, var3);
      return var4;
   }

   private static OperationPolicyBean createOperationPolicyBean(WebservicePolicyRefBean var0, String var1, String var2, String var3, String var4) {
      return createOperationPolicyBean(var0, var1, var2, var3, var4, false);
   }

   private static OperationPolicyBean createOperationPolicyBean(WebservicePolicyRefBean var0, String var1, String var2, String var3, String var4, boolean var5) {
      OperationPolicyBean var6 = var0.createOperationPolicy();
      var6.setOperationName(var1);
      var6.setServiceLink(var4);
      createWsPolicyBean(var6, var2, var3, var5);
      return var6;
   }

   private static final URI getURI(String var0) {
      if (var0 == null) {
         return null;
      } else {
         try {
            return new URI(var0);
         } catch (URISyntaxException var2) {
            var2.printStackTrace();
            return null;
         }
      }
   }

   public static final Method findWebMethodFromService(Class var0, String var1) {
      Method[] var2 = var0.getMethods();
      Method[] var3 = getSEIMethods(var0);
      Method[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Method var7 = var4[var6];
         if (var7.getName().equals(var1)) {
            return var7;
         }

         WebMethod var8 = (WebMethod)var7.getAnnotation(WebMethod.class);
         if (var8 != null && var8.operationName().equals(var1)) {
            return var7;
         }

         if (matchSEIMethod(var7, var1, var3)) {
            return var7;
         }
      }

      throw new IllegalArgumentException("Can't find method " + var1 + " for service " + var0.getName());
   }

   static final Method[] getSEIMethods(Class var0) {
      Method[] var1 = null;

      try {
         WebService var2 = (WebService)var0.getAnnotation(WebService.class);
         if (var2 != null && var2.endpointInterface() != null && var2.endpointInterface().length() > 0) {
            Class var3 = var0.getClassLoader().loadClass(var2.endpointInterface());
            if (var3 == null) {
               var3 = ClassUtil.loadClass(var2.endpointInterface());
            }

            if (var3 != null) {
               var1 = var3.getMethods();
            }
         }
      } catch (Exception var4) {
         if (verbose) {
            Verbose.log((Object)("Info: Get Methods failed from SEI: " + var4.toString()));
            var4.printStackTrace();
         }
      }

      return var1;
   }

   private static final boolean matchSEIMethod(Method var0, String var1, Method[] var2) {
      if (var2 == null) {
         return false;
      } else {
         Method[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Method var6 = var3[var5];
            if (ClassUtil.equalMethods(var6, var0)) {
               WebMethod var7 = (WebMethod)var6.getAnnotation(WebMethod.class);
               if (var7 != null && var7.operationName().equals(var1)) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
