package weblogic.messaging.path;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.utils.encoders.BASE64Encoder;

public class MemberInfo {
   static final long serialVersionUID = -6246813905189434496L;
   private static final String ITEM_VERSION_NUMBER = "VersionNumber";
   private static final String ITEM_SERIALIZED_MEMBER = "SerializedMember";
   private static String OPEN_TYPE_NAME = "MemberInfo";
   private static String OPEN_DESCRIPTION = "This object represents information about a path service member.A Member is an entry in an Assembly. For example, Queues are Members of a JMS Distributed Queue Assembly.  A 'path' is a particular Assembly member given a key.  A single Member will be identified by many different 'path' keys";
   private static String[] itemNames = new String[]{"VersionNumber", "SerializedMember"};
   private static String[] itemDescriptions = new String[]{"The version number.", "The serialized representation of a path service Member"};
   private static OpenType[] itemTypes;
   private static final int VERSION = 1;
   private Member member;

   public MemberInfo(CompositeData var1) throws OpenDataException {
      this.readCompositeData(var1);
   }

   public MemberInfo(Member var1) {
      this.member = var1;
   }

   public Member getMember() {
      return this.member;
   }

   public CompositeData toCompositeData() throws OpenDataException {
      CompositeDataSupport var1 = new CompositeDataSupport(this.getCompositeType(), this.getCompositeDataMap());
      return var1;
   }

   protected void readCompositeData(CompositeData var1) throws OpenDataException {
      OpenDataException var3;
      try {
         String var2 = (String)var1.get("SerializedMember");
         BASE64Decoder var9 = new BASE64Decoder();
         byte[] var4 = var9.decodeBuffer(var2);
         ByteArrayInputStream var5 = new ByteArrayInputStream(var4);
         ObjectInputStream var6 = new ObjectInputStream(var5);
         this.member = (Member)var6.readObject();
      } catch (IOException var7) {
         var3 = new OpenDataException("Unable to deserialize member.");
         var3.initCause(var7);
         throw var3;
      } catch (ClassNotFoundException var8) {
         var3 = new OpenDataException("Unable to deserialize member.");
         var3.initCause(var8);
         throw var3;
      }
   }

   private Map getCompositeDataMap() throws OpenDataException {
      HashMap var1 = new HashMap();

      try {
         var1.put("VersionNumber", new Integer(1));
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         ObjectOutputStream var12 = new ObjectOutputStream(var2);
         var12.writeObject(this.member);
         BASE64Encoder var4 = new BASE64Encoder();
         String var5 = var4.encodeBuffer(var2.toByteArray());
         var1.put("SerializedMember", var5);
         return var1;
      } catch (IOException var10) {
         OpenDataException var3 = new OpenDataException("Unable to serialize member.");
         var3.initCause(var10);
         throw var3;
      } finally {
         ;
      }
   }

   private CompositeType getCompositeType() throws OpenDataException {
      CompositeType var1 = new CompositeType(OPEN_TYPE_NAME, OPEN_DESCRIPTION, itemNames, itemDescriptions, itemTypes);
      return var1;
   }

   static {
      itemTypes = new OpenType[]{SimpleType.INTEGER, SimpleType.STRING};
   }
}
