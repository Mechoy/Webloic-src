package weblogic.wsee.policy.deployment;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import weblogic.wsee.policy.framework.ExternalizationUtils;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyConstants;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.framework.PolicyStatement;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.util.Verbose;
import weblogic.xml.babel.stream.ExclusiveCanonicalWriter;
import weblogic.xml.babel.stream.XMLOutputStreamBase;
import weblogic.xml.crypto.wss.Base64Encoding;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLInputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class PolicyRef implements Externalizable {
   protected String refName;
   protected PolicyStatement reffedPolicy;
   protected NormalizedExpression normalizedPolicy;
   private static final boolean verbose = Verbose.isVerbose(PolicyRef.class);
   private static final boolean loadPolicyFromClassPath = Boolean.getBoolean("weblogic.wsee.policy.LoadFromClassPathEnabled");
   public static final boolean debug = false;
   protected URI uri;
   byte[] digest;
   QName digestAlg;

   PolicyRef(String var1, URI var2, byte[] var3, QName var4) {
      assert var2 != null;

      if (var1 == null || var1.length() == 0) {
         var1 = var2.getFragment();
      }

      this.refName = var1;
      this.uri = var2;
      if (var3 == null) {
         assert var4 == null;
      } else {
         assert PolicyConstants.SHA1.equals(var4);
      }

      this.digestAlg = var4;
      this.digest = var3;
   }

   PolicyRef(String var1, URI var2) {
      this(var1, var2, (byte[])null, (QName)null);
   }

   PolicyRef(String var1, URI var2, PolicyStatement var3) {
      this.refName = var1;
      this.uri = var2;
      this.reffedPolicy = var3;
   }

   public PolicyRef() {
   }

   public String getRefName() {
      return this.refName;
   }

   public NormalizedExpression getNormalizedPolicy(PolicyServer var1, boolean var2) throws PolicyException {
      if (this.normalizedPolicy == null) {
         this.normalizedPolicy = this.getPolicy(var1, var2).normalize();
      }

      return this.normalizedPolicy;
   }

   public NormalizedExpression getNormalizedPolicy() throws PolicyException {
      return this.getNormalizedPolicy((PolicyServer)null, false);
   }

   protected static boolean nullableEqual(Object var0, Object var1) {
      return var0 == null && var1 == null || var0 != null && var1 != null && var0.equals(var1);
   }

   public PolicyStatement getPolicy(PolicyServer var1, boolean var2) throws PolicyException {
      if (this.reffedPolicy == null) {
         assert this.uri != null;

         String var3 = this.uri.getScheme();
         if (var3 != null && var3.equals("policy")) {
            if (var1 == null) {
               throw new PolicyException("Can not retrieve policy from the static wsdl, you must initialize the service from a dynamic wsdl");
            }

            this.reffedPolicy = var1.getPolicy(this.uri.getSchemeSpecificPart(), var2);
         } else {
            if (this.reffedPolicy == null && var1 != null) {
               try {
                  this.reffedPolicy = var1.getPolicy(this.uri.toString(), var2);
               } catch (PolicyException var7) {
               }
            }

            if (this.reffedPolicy == null && var1 != null) {
               this.reffedPolicy = var1.getPolicyFromCache(this.uri.getFragment());
               if (this.reffedPolicy == null) {
                  this.reffedPolicy = var1.getPolicyFromCache(this.uri.toString());
               }
            }

            if (this.reffedPolicy == null) {
               InputStream var4 = null;

               try {
                  var4 = getInputStreamFromURI(this.uri);
               } catch (IOException var6) {
                  throw new PolicyException("Problem reading policy from URI " + this.uri, var6);
               }

               if (var4 == null) {
                  throw new PolicyException("Unable to to retrieve policy from URI '" + this.uri + "'");
               }

               this.reffedPolicy = parsePolicy(var4, this.digest);
            }
         }

         if (this.reffedPolicy == null) {
            throw new PolicyException("Can not find policy: " + this.uri);
         }
      }

      return this.reffedPolicy;
   }

   public PolicyStatement getPolicy() throws PolicyException {
      return this.getPolicy((PolicyServer)null, false);
   }

   public URI getURI() {
      return this.uri;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else {
         PolicyRef var2 = null;

         try {
            var2 = (PolicyRef)var1;
         } catch (ClassCastException var4) {
            return false;
         }

         return nullableEqual(this.refName, var2.refName) && nullableEqual(this.uri, var2.uri);
      }
   }

   public int hashCode() {
      if (this.refName != null) {
         int var1 = this.refName.hashCode();
         if (this.uri != null) {
            var1 ^= this.uri.hashCode();
         }

         return var1;
      } else {
         return this.uri.hashCode();
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[Policy Ref name = " + this.refName + ", uri = " + this.uri + "]");
      var1.append('\n');
      return var1.toString();
   }

   private static PolicyStatement parsePolicy(InputStream var0, byte[] var1) throws PolicyException {
      PolicyLoader var2 = new PolicyLoader(ProviderRegistry.getTheRegistry());
      DocumentBuilder var3 = PolicyLoader.getParser();
      Document var4 = null;

      try {
         var4 = var3.parse(var0);
      } catch (Exception var6) {
         throw new PolicyException("Problem parsing policy XML", var6);
      }

      if (var1 != null) {
         verifyDigest(var4, var1);
      }

      return var2.load((Node)var4.getDocumentElement());
   }

   private static void verifyDigest(Document var0, byte[] var1) throws PolicyException {
      DigestOutputStream var2 = null;

      try {
         var2 = new DigestOutputStream((OutputStream)null, MessageDigest.getInstance("SHA-1"));
      } catch (NoSuchAlgorithmException var8) {
         throw new AssertionError(var8);
      }

      ExclusiveCanonicalWriter var3 = new ExclusiveCanonicalWriter(new OutputStreamWriter(var2));

      try {
         XMLInputStreamFactory var4 = XMLInputStreamFactory.newInstance();
         XMLInputStream var5 = var4.newInputStream(var0);
         var5.skip(2);
         var5.next();
         XMLOutputStreamBase var6 = new XMLOutputStreamBase(var3);
         var6.add(var5.getSubStream());
      } catch (XMLStreamException var7) {
         throw new PolicyException("Problem verifying document", var7);
      }

      byte[] var9 = var2.getMessageDigest().digest();
      if (!MessageDigest.isEqual(var1, var9)) {
         if (verbose) {
            Verbose.log((Object)("declared digest =" + (new Base64Encoding()).encode(var1)));
            Verbose.log((Object)("Computed digest = " + (new Base64Encoding()).encode(var9)));
         }

         throw new PolicyException("Message digest not verified");
      }
   }

   private static InputStream getInputStreamFromURI(URI var0) throws IOException {
      Object var1 = null;
      String var2 = var0.getScheme();
      if (var2 == null) {
         File var3 = new File(var0.toString());
         if (var3.exists()) {
            var1 = new FileInputStream(var3);
         } else if (loadPolicyFromClassPath) {
            if (verbose) {
               Verbose.log((Object)("Looking up resource '" + var0.toString() + "' via ClassLoader " + getClassLoader().getClass()));
            }

            var1 = getClassLoader().getResourceAsStream(var0.toString());
         }
      } else {
         var1 = var0.toURL().openConnection().getInputStream();
      }

      return (InputStream)var1;
   }

   private static ClassLoader getClassLoader() {
      ClassLoader var0 = Thread.currentThread().getContextClassLoader();
      if (var0 == null) {
         var0 = PolicyRef.class.getClassLoader();
      }

      return var0;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.refName = var1.readUTF();
      this.reffedPolicy = ExternalizationUtils.readPolicyStatement(var1);
      this.normalizedPolicy = ExternalizationUtils.readNormalizedExpression(var1);
      this.uri = (URI)var1.readObject();
      this.digestAlg = ExternalizationUtils.readQName(var1);
      this.digest = ExternalizationUtils.readByteArray(var1);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF(this.refName);
      ExternalizationUtils.writePolicyStatement(this.reffedPolicy, var1);
      ExternalizationUtils.writeNormalizedExpression(this.normalizedPolicy, var1);
      var1.writeObject(this.uri);
      ExternalizationUtils.writeQName(this.digestAlg, var1);
      if (this.digest == null) {
         var1.writeInt(0);
      } else {
         var1.writeInt(this.digest.length);
         var1.write(this.digest);
      }

   }
}
