package weblogic.wsee.policy.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.wsee.policy.deployment.PolicyDeployUtils;
import weblogic.wsee.policy.deployment.PolicyLoader;
import weblogic.wsee.policy.deployment.ProviderRegistry;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.schema.PolicySchemaValidationException;
import weblogic.wsee.policy.schema.PolicySchemaValidator;
import weblogic.wsee.util.dom.DOMParser;
import weblogic.xml.crypto.utils.DOMUtils;

public abstract class PolicyFinder {
   private static final String EXT = ".xml";

   public abstract PolicyStatement findPolicy(String var1, String var2) throws PolicyException;

   public static PolicyStatement readPolicyFromStream(String var0, InputStream var1) throws PolicyException {
      return readPolicyFromStream(var0, var1, false);
   }

   public static PolicyStatement readPolicyFromStream(String var0, InputStream var1, boolean var2) throws PolicyException {
      return readPolicyFromStream((PolicyServer)null, var0, var1, var2);
   }

   public static PolicyStatement readPolicyFromStream(PolicyServer var0, String var1, InputStream var2, boolean var3) throws PolicyException {
      if (var2 == null) {
         throw new PolicyException("Policy stream can not be null");
      } else {
         PolicyLoader var4 = new PolicyLoader(ProviderRegistry.getTheRegistry());

         PolicyStatement var7;
         try {
            Document var5 = DOMParser.getDocument(new InputSource(var2));
            if (var3) {
               PolicySchemaValidator var6;
               if (var0 == null) {
                  var6 = new PolicySchemaValidator();
               } else {
                  var6 = var0.getPolicySchemaValidator();
               }

               var6.validate(var5);
            }

            PolicyStatement var22 = var4.load(var5);
            if (var0 != null) {
               var22 = var0.processAssertions(var1, var22);
            }

            if (var3) {
               var4.validate(var1, var22);
            }

            setPolicyId(var1, var22);
            var7 = var22;
         } catch (IOException var18) {
            throw new PolicyException("Failed to parse \"" + var1 + "\": " + var18.getMessage());
         } catch (AssertionError var19) {
            throw new PolicyException("Failed to parse \"" + var1 + "\": " + var19.getMessage());
         } catch (PolicySchemaValidationException var20) {
            throw new PolicyException("Policy not valid: ", var20);
         } finally {
            try {
               if (var2 != null) {
                  var2.close();
               }
            } catch (IOException var17) {
               var17.printStackTrace();
            }

         }

         return var7;
      }
   }

   public static PolicyStatement readPolicyStatementFromNode(Node var0) throws PolicyException {
      PolicyLoader var1 = new PolicyLoader(ProviderRegistry.getTheRegistry());
      return var1.load(var0);
   }

   public static Map loadPolicies(URL var0, PolicyServer var1) throws PolicyException {
      Object var2 = new HashMap();
      if (var0 == null) {
         return (Map)var2;
      } else {
         String var3 = var0.getProtocol();
         if (var3.equals("file")) {
            var2 = loadPoliciesFromDir(var0, var1);
         } else if (var3.equals("zip")) {
            var2 = loadPolicesFromZip(var0, var1);
         }

         return (Map)var2;
      }
   }

   public static Map loadPolicies(VirtualJarFile var0, PolicyServer var1) throws PolicyException {
      HashMap var2 = new HashMap();
      Iterator var3 = var0.entries();

      while(var3.hasNext()) {
         ZipEntry var4 = (ZipEntry)var3.next();
         if (!var4.isDirectory()) {
            String var5 = var4.toString();
            if (var5.indexOf("policies") > -1) {
               String var6 = rootName(var4.getName());
               PolicyStatement var7 = null;

               try {
                  var7 = readPolicyFromStream(var1, var6, var0.getInputStream(var4), true);
               } catch (IOException var9) {
                  throw new PolicyException(var9);
               }

               if (var7 != null) {
                  var2.put(var6, var7);
               }
            }
         }
      }

      var1.addPolicies(var2);
      return var2;
   }

   private static Map loadPolicesFromZip(URL var0, PolicyServer var1) throws PolicyException {
      Object var2 = new HashMap();
      String var3 = var0.getFile().toString();
      int var4 = var3.indexOf(".jar!");
      if (var4 > 0) {
         try {
            JarFile var5 = new JarFile(var3.substring(0, var4 + 4));
            VirtualJarFile var6 = VirtualJarFactory.createVirtualJar(var5);
            var2 = loadPolicies(var6, var1);
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

      return (Map)var2;
   }

   private static Map loadPoliciesFromDir(URL var0, PolicyServer var1) throws PolicyException {
      HashMap var2 = new HashMap();
      File var3 = new File(var0.getFile());
      if (var3.exists() && var3.isDirectory()) {
         FilenameFilter var4 = new FilenameFilter() {
            public boolean accept(File var1, String var2) {
               return var2.endsWith(".xml");
            }
         };
         File[] var5 = var3.listFiles(var4);

         for(int var6 = 0; var6 < var5.length; ++var6) {
            FileInputStream var7 = null;

            try {
               var7 = new FileInputStream(var5[var6]);
            } catch (FileNotFoundException var10) {
               throw new AssertionError(var10);
            }

            String var8 = rootName(var5[var6].getName());
            PolicyStatement var9 = readPolicyFromStream(var1, var8, var7, true);
            if (var9 != null) {
               var2.put(var8, var9);
            }
         }

         var1.addPolicies(var2);
         return var2;
      } else {
         throw new AssertionError(var3.getAbsolutePath());
      }
   }

   private static PolicyStatement setPolicyId(String var0, PolicyStatement var1) {
      String var2 = var1.getId();
      if (var2 == null || var2.length() == 0) {
         var1.setId(PolicyDeployUtils.getXPointerId(var0));
      }

      return var1;
   }

   public static PolicyStatement generatePolicyId(PolicyStatement var0) {
      String var1 = var0.getId();
      if (var1 == null || var1.length() == 0) {
         var0.setId(DOMUtils.generateId());
      }

      return var0;
   }

   protected static String checkFileExtension(String var0) {
      return !var0.endsWith(".xml") ? var0 + ".xml" : var0;
   }

   private static String rootName(String var0) {
      int var1 = var0.lastIndexOf(47);
      if (var1 < 0) {
         var1 = 0;
      } else {
         ++var1;
      }

      return var0.substring(var1, var0.lastIndexOf("."));
   }
}
