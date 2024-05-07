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

public class PSEntryInfo {
   private static final String ITEM_VERSION_NUMBER = "VersionNumber";
   private static final String ITEM_CURSOR_HANDLE = "CursorHandle";
   private static final String ITEM_SERIALIZED_KEY = "SerializedKey";
   private static String OPEN_TYPE_NAME = "PSEntryInfo";
   private static String OPEN_DESCRIPTION = "This object represents information about a path service entry.  It is used by the Path Service cursor to return batches of path service entries to an administration client";
   private static String[] itemNames = new String[]{"VersionNumber", "CursorHandle", "SerializedKey"};
   private static String[] itemDescriptions = new String[]{"The version number.", "The handle to the entry in the cursor.", "The serialized representation of a path service Key"};
   private static OpenType[] itemTypes;
   private static final int VERSION = 1;
   private Key key;
   private int cursorHandle;

   public PSEntryInfo(CompositeData var1) throws OpenDataException {
      this.readCompositeData(var1);
   }

   public PSEntryInfo(Key var1, int var2) {
      this.key = var1;
      this.cursorHandle = var2;
   }

   public Key getKey() {
      return this.key;
   }

   public long getCursorHandle() {
      return (long)this.cursorHandle;
   }

   public CompositeData toCompositeData() throws OpenDataException {
      CompositeDataSupport var1 = new CompositeDataSupport(this.getCompositeType(), this.getCompositeDataMap());
      return var1;
   }

   protected void readCompositeData(CompositeData var1) throws OpenDataException {
      Long var2 = (Long)var1.get("CursorHandle");
      if (var2 != null) {
         this.cursorHandle = var2.intValue();
      }

      OpenDataException var4;
      try {
         String var3 = (String)var1.get("SerializedKey");
         BASE64Decoder var10 = new BASE64Decoder();
         byte[] var5 = var10.decodeBuffer(var3);
         ByteArrayInputStream var6 = new ByteArrayInputStream(var5);
         ObjectInputStream var7 = new ObjectInputStream(var6);
         this.key = (Key)var7.readObject();
      } catch (IOException var8) {
         var4 = new OpenDataException("Unable to deserialize path service entry.");
         var4.initCause(var8);
         throw var4;
      } catch (ClassNotFoundException var9) {
         var4 = new OpenDataException("Unable to deserialize path service entry.");
         var4.initCause(var9);
         throw var4;
      }
   }

   private Map getCompositeDataMap() throws OpenDataException {
      HashMap var1 = new HashMap();
      var1.put("VersionNumber", new Integer(1));
      var1.put("CursorHandle", new Long(this.getCursorHandle()));

      try {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         ObjectOutputStream var7 = new ObjectOutputStream(var2);
         var7.writeObject(this.getKey());
         BASE64Encoder var4 = new BASE64Encoder();
         String var5 = var4.encodeBuffer(var2.toByteArray());
         var1.put("SerializedKey", var5);
         return var1;
      } catch (IOException var6) {
         OpenDataException var3 = new OpenDataException("Unable to serialize path service entry.");
         var3.initCause(var6);
         throw var3;
      }
   }

   private CompositeType getCompositeType() throws OpenDataException {
      CompositeType var1 = new CompositeType(OPEN_TYPE_NAME, OPEN_DESCRIPTION, itemNames, itemDescriptions, itemTypes);
      return var1;
   }

   static {
      itemTypes = new OpenType[]{SimpleType.INTEGER, SimpleType.LONG, SimpleType.STRING};
   }
}
