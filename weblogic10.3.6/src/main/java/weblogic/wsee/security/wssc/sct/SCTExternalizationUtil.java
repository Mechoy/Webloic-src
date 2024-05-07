package weblogic.wsee.security.wssc.sct;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.Key;
import java.util.Calendar;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import weblogic.wsee.server.EncryptionUtil;

public class SCTExternalizationUtil {
   private static final String AES_KEY_FACTORY_ID = "AES";

   public static void writeKey(Key var0, ObjectOutput var1) throws IOException {
      byte[] var2 = var0.getEncoded();
      byte[] var3 = EncryptionUtil.encrypt(var2);
      var1.writeShort(var3.length);
      var1.write(var3);
   }

   public static Key readKey(ObjectInput var0) throws IOException {
      short var1 = var0.readShort();
      byte[] var2 = new byte[var1];
      var0.read(var2);
      byte[] var3 = EncryptionUtil.decrypt(var2);
      return new SecretKeySpec(var3, "AES");
   }

   public static void writeSubject(Subject var0, ObjectOutput var1) throws IOException {
      if (var0 == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeObject(var0);
      }

   }

   public static Subject readSubject(ObjectInput var0) throws IOException, ClassNotFoundException {
      Subject var1 = null;
      if (var0.readBoolean()) {
         var1 = (Subject)var0.readObject();
      }

      return var1;
   }

   public static void writeCalendar(Calendar var0, ObjectOutput var1) throws IOException {
      if (var0 == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeObject(var0);
      }

   }

   public static Calendar readCalendar(ObjectInput var0) throws IOException, ClassNotFoundException {
      Calendar var1 = null;
      if (var0.readBoolean()) {
         var1 = (Calendar)var0.readObject();
      }

      return var1;
   }

   public static void writeConditionalString(String var0, ObjectOutput var1) throws IOException {
      if (var0 == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeObject(var0);
      }

   }

   public static void writeConditionalQName(QName var0, ObjectOutput var1) throws IOException {
      if (var0 == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeObject(var0);
      }

   }

   public static String readConditionalString(ObjectInput var0) throws IOException, ClassNotFoundException {
      String var1 = null;
      if (var0.readBoolean()) {
         var1 = (String)var0.readObject();
      }

      return var1;
   }

   public static QName readConditionalQName(ObjectInput var0) throws IOException, ClassNotFoundException {
      QName var1 = null;
      if (var0.readBoolean()) {
         var1 = (QName)var0.readObject();
      }

      return var1;
   }

   public static void writeConditionalSecurityTokenReferenceInfo(SCCredential.SecurityTokenReferenceInfo var0, ObjectOutput var1) throws IOException {
      if (var0 == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeObject(var0);
      }

   }

   public static SCCredential.SecurityTokenReferenceInfo readConditionalSecurityTokenReferenceInfo(ObjectInput var0) throws IOException, ClassNotFoundException {
      SCCredential.SecurityTokenReferenceInfo var1 = null;
      if (var0.readBoolean()) {
         var1 = (SCCredential.SecurityTokenReferenceInfo)var0.readObject();
      }

      return var1;
   }
}
