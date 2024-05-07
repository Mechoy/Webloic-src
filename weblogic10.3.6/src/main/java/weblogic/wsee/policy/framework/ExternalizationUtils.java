package weblogic.wsee.policy.framework;

import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlObject.Factory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.wsee.policy.deployment.ProviderRegistry;

public class ExternalizationUtils {
   private static final byte EXTVERSION_90 = 0;
   public static final byte EXTVERSION = 0;
   private static HashMap nameToClassMap = new HashMap();

   public static byte getExtVersion(ObjectInput var0) throws IOException {
      return var0.readByte();
   }

   public static void writeExtVersion(ObjectOutput var0) throws IOException {
      var0.writeByte(0);
   }

   public static void checkVersion(ObjectInput var0, byte var1) throws IOException {
      if (var1 != 0) {
         throw new IOException("Policy version mis-matched");
      }
   }

   public static void registerExternalizable(QName var0, String var1) {
      nameToClassMap.put(var0, var1);
   }

   public static void writeQName(QName var0, ObjectOutput var1) throws IOException {
      if (var0 == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeUTF(var0.getNamespaceURI());
         var1.writeUTF(var0.getLocalPart());
         var1.writeUTF(var0.getPrefix());
      }

   }

   public static QName readQName(ObjectInput var0) throws IOException {
      return var0.readBoolean() ? new QName(var0.readUTF(), var0.readUTF(), var0.readUTF()) : null;
   }

   public static String getClassNameFromMap(QName var0) {
      return (String)nameToClassMap.get(var0);
   }

   public static void writeAssertions(Set var0, ObjectOutput var1) throws IOException {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         PolicyAssertion var4 = (PolicyAssertion)var3.next();
         if (var4 != null) {
            String var5 = getClassNameFromMap(var4.getName());
            if (var5 != null) {
               var2.add(var4);
            }
         }
      }

      int var8 = var2.size();
      var1.writeInt(var8);

      for(int var7 = 0; var7 < var8; ++var7) {
         PolicyAssertion var6 = (PolicyAssertion)var2.get(var7);
         writeQName(var6.getName(), var1);
         var6.writeExternal(var1);
      }

   }

   public static Set readAssertions(ObjectInput var0) throws IOException, ClassNotFoundException {
      int var1 = var0.readInt();
      LinkedHashSet var2 = new LinkedHashSet();

      for(int var3 = 0; var3 < var1; ++var3) {
         QName var4 = readQName(var0);
         String var5 = getClassNameFromMap(var4);
         if (var5 == null) {
            throw new IOException("Need to register " + var4 + " for externalization");
         }

         PolicyAssertion var6 = null;

         try {
            var6 = (PolicyAssertion)Class.forName(var5).newInstance();
         } catch (InstantiationException var8) {
            throw new IOException(var8.getMessage());
         } catch (IllegalAccessException var9) {
            throw new IOException(var9.getMessage());
         }

         var6.readExternal(var0);
         var2.add(var6);
      }

      return var2;
   }

   public static byte[] toByteArray(PolicyAlternative var0) throws IOException {
      if (null == var0) {
         return null;
      } else {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         ObjectOutputStream var2 = new ObjectOutputStream(var1);
         var0.writeExternal(var2);
         var2.flush();
         var2.close();
         var1.close();
         return var1.toByteArray();
      }
   }

   public static void writeAlternative(PolicyAlternative var0, ObjectOutput var1) throws IOException {
      assert var0 != null;

      writeAssertions(var0.getAssertions(), var1);
   }

   public static void writeAlternatives(Set var0, ObjectOutput var1) throws IOException {
      assert var0 != null;

      int var2 = var0.size();
      var1.writeInt(var2);
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         PolicyAlternative var4 = (PolicyAlternative)var3.next();
         writeAssertions(var4.getAssertions(), var1);
      }

   }

   public static Set readAlternatives(ObjectInput var0) throws IOException, ClassNotFoundException {
      LinkedHashSet var1 = new LinkedHashSet();
      int var2 = var0.readInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         PolicyAlternative var4 = new PolicyAlternative();
         var4.addAssertions(readAssertions(var0));
         var1.add(var4);
      }

      return var1;
   }

   public static byte[] readByteArray(ObjectInput var0) throws IOException {
      int var1 = var0.readInt();
      if (var1 <= 0) {
         return null;
      } else {
         byte[] var2 = new byte[var1];
         boolean var3 = false;

         int var5;
         for(int var4 = 0; var4 < var1; var4 += var5) {
            var5 = var0.read(var2, var4, var1 - var4);
         }

         return var2;
      }
   }

   public static void writeXmlObject(XmlObject var0, ObjectOutput var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      var0.save(var2);
      var2.close();
      byte[] var3 = var2.toByteArray();
      var1.writeInt(var3.length);
      var1.write(var3);
   }

   public static XmlObject readXmlObject(ObjectInput var0) throws IOException {
      byte[] var1 = readByteArray(var0);
      if (var1 == null) {
         return null;
      } else {
         try {
            return Factory.parse(new ByteArrayInputStream(var1));
         } catch (XmlException var3) {
            throw new IOException(var3.getMessage());
         }
      }
   }

   public static void writePolicyStatement(PolicyStatement var0, ObjectOutput var1) throws IOException {
      if (var0 == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var0.writeExternal(var1);
      }

   }

   public static PolicyStatement readPolicyStatement(ObjectInput var0) throws IOException, ClassNotFoundException {
      if (var0.readBoolean()) {
         PolicyStatement var1 = new PolicyStatement();
         var1.readExternal(var0);
         return var1;
      } else {
         return null;
      }
   }

   public static void writeNormalizedExpression(NormalizedExpression var0, ObjectOutput var1) throws IOException {
      if (var0 != null && !var0.isUninitialized()) {
         var1.writeBoolean(true);
         var0.writeExternal(var1);
      } else {
         var1.writeBoolean(false);
      }

   }

   public static NormalizedExpression readNormalizedExpression(ObjectInput var0) throws IOException, ClassNotFoundException {
      if (var0.readBoolean()) {
         NormalizedExpression var1 = new NormalizedExpression();
         var1.readExternal(var0);
         return var1;
      } else {
         return null;
      }
   }

   static {
      try {
         ProviderRegistry.getTheRegistry();
      } catch (PolicyException var1) {
         throw new AssertionError(var1);
      }
   }
}
