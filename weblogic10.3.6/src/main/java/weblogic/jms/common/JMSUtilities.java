package weblogic.jms.common;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UTFDataFormatException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.jms.Message;
import weblogic.common.internal.PeerInfo;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.messaging.MessagingLogger;
import weblogic.utils.StringUtils;
import weblogic.utils.io.DataIO;

public final class JMSUtilities {
   public static final String RESERVED_ROLLBACK_ONLY = "ReservedRollbackOnly";
   private static final short TYPENULL = 99;
   private static final short TYPESTRING = 102;
   private static final short TYPEBOOLEAN = 103;
   private static final short TYPEINTEGER = 104;
   private static final short TYPELONG = 105;
   private static final short TYPEBYTE = 106;
   private static final short TYPESHORT = 107;
   private static final short TYPEFLOAT = 108;
   private static final short TYPEDOUBLE = 109;
   private static final short TYPECHARACTER = 110;
   private static final short TYPEBYTEARRAY = 111;
   private static final short TYPEBIGSTRING = 112;

   private JMSUtilities() {
   }

   public static javax.jms.JMSException jmsException(String var0, Exception var1) {
      return jmsExceptionThrowable(var0, var1);
   }

   public static javax.jms.JMSException jmsExceptionThrowable(String var0, Throwable var1) {
      if (JMSDebug.JMSCommon.isDebugEnabled()) {
         JMSDebug.JMSCommon.debug(var0, var1);
      }

      if (var1 instanceof javax.jms.JMSException) {
         return (javax.jms.JMSException)var1;
      } else {
         JMSException var2 = new JMSException(var0, var1);
         return var2;
      }
   }

   public static javax.jms.JMSException throwJMSOrRuntimeException(Throwable var0) throws javax.jms.JMSException {
      if (var0 instanceof javax.jms.JMSException) {
         throw (javax.jms.JMSException)var0;
      } else if (var0 instanceof RuntimeException) {
         throw (RuntimeException)var0;
      } else if (var0 instanceof Error) {
         throw (Error)var0;
      } else {
         throw new JMSException(var0.getMessage(), var0);
      }
   }

   public static StreamCorruptedException versionIOException(int var0, int var1, int var2) {
      return new StreamCorruptedException(MessagingLogger.logUnsupportedClassVersionLoggable(var0, var1, var2).getMessage());
   }

   public static boolean getTracing(Message var0) {
      return true;
   }

   public static void setTracing(Message var0, boolean var1) {
      try {
         var0.setBooleanProperty("tracing", var1);
      } catch (javax.jms.JMSException var3) {
      }

   }

   private static boolean getDropNulls(PeerInfo var0) {
      if (var0 == null) {
         return true;
      } else {
         return var0.compareTo(PeerInfo.VERSION_612) <= 0;
      }
   }

   private static int getEntryCount(Map var0, boolean var1) {
      if (var1) {
         int var2 = 0;
         Iterator var3 = var0.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            if (var4.getValue() != null) {
               ++var2;
            }
         }

         return var2;
      } else {
         return var0.size();
      }
   }

   static final int writeBasicMap(DataOutput var0, Map var1, PeerInfo var2) throws IOException {
      boolean var3 = getDropNulls(var2);
      var0.writeInt(getEntryCount(var1, var3));
      int var4 = 0;
      Iterator var5 = var1.entrySet().iterator();

      while(true) {
         Map.Entry var6;
         do {
            if (!var5.hasNext()) {
               return var4;
            }

            var6 = (Map.Entry)var5.next();
         } while(var3 && var6.getValue() == null);

         var4 += writeBasicType(var0, var6.getKey());
         var4 += writeBasicType(var0, var6.getValue());
      }
   }

   static final int writeBigStringBasicMap(ObjectOutput var0, Map var1, PeerInfo var2, boolean var3) throws IOException {
      boolean var4 = getDropNulls(var2);
      boolean var5 = false;
      if (var2 != null) {
         var5 = var3 && var2.compareTo(PeerInfo.VERSION_701) >= 0;
      }

      var0.writeInt(getEntryCount(var1, var4));
      int var6 = 0;
      Iterator var7 = var1.entrySet().iterator();

      while(true) {
         Map.Entry var8;
         do {
            if (!var7.hasNext()) {
               return var6;
            }

            var8 = (Map.Entry)var7.next();
         } while(var4 && var8.getValue() == null);

         if (var5) {
            var6 += writeBigStringBasicType(var0, var8.getKey());
            var6 += writeBigStringBasicType(var0, var8.getValue());
         } else {
            var6 += writeBasicType(var0, var8.getKey());
            var6 += writeBasicType(var0, var8.getValue());
         }
      }
   }

   static final HashMap readBasicMap(DataInput var0) throws IOException {
      HashMap var1 = new HashMap();
      int var2 = var0.readInt();

      while(var2-- > 0) {
         Object var3 = readBasicType(var0);
         Object var4 = readBasicType(var0);
         var1.put(var3, var4);
      }

      return var1;
   }

   static final HashMap readBigStringBasicMap(ObjectInput var0) throws IOException {
      HashMap var1 = new HashMap();
      int var2 = var0.readInt();

      while(var2-- > 0) {
         Object var3 = readBigStringBasicType(var0);
         Object var4 = readBigStringBasicType(var0);
         var1.put(var3, var4);
      }

      return var1;
   }

   private static int writeBigStringBasicType(ObjectOutput var0, Object var1) throws IOException {
      if (var1 != null && var1 instanceof String) {
         String var2 = (String)var1;
         int var3 = StringUtils.getUTFLength(var2);
         if (var3 <= 32767) {
            var0.writeShort(102);
            writeUTF(var0, var2, var3);
         } else {
            var0.writeShort(112);
            var0.writeObject(var2);
         }

         return 4 + (var2.length() << 2);
      } else {
         return writeBasicType(var0, var1);
      }
   }

   private static int writeBasicType(DataOutput var0, Object var1) throws IOException {
      if (var1 == null) {
         var0.writeShort(99);
         return 2;
      } else if (var1 instanceof String) {
         String var2 = (String)var1;
         int var3 = StringUtils.getUTFLength(var2);
         if (var3 <= 32767) {
            var0.writeShort(102);
            writeUTF(var0, var2, var3);
            return 4 + (var2.length() << 2);
         } else {
            ByteArrayOutputStream var4 = new ByteArrayOutputStream((var2.length() << 2) + 50);
            ObjectOutputStream var5 = new ObjectOutputStream(var4);
            var5.writeObject(var2);
            var5.close();
            return writeBasicType(var0, var4.toByteArray());
         }
      } else if (var1 instanceof Integer) {
         var0.writeShort(104);
         var0.writeInt((Integer)var1);
         return 6;
      } else if (var1 instanceof Long) {
         var0.writeShort(105);
         var0.writeLong((Long)var1);
         return 10;
      } else if (var1 instanceof byte[]) {
         var0.writeShort(111);
         var0.writeInt(((byte[])((byte[])var1)).length);
         var0.write((byte[])((byte[])var1));
         return 6 + ((byte[])((byte[])var1)).length;
      } else if (var1 instanceof Boolean) {
         var0.writeShort(103);
         var0.writeBoolean((Boolean)var1);
         return 3;
      } else if (var1 instanceof Byte) {
         var0.writeShort(106);
         var0.writeByte((Byte)var1);
         return 3;
      } else if (var1 instanceof Short) {
         var0.writeShort(107);
         var0.writeShort((Short)var1);
         return 4;
      } else if (var1 instanceof Float) {
         var0.writeShort(108);
         var0.writeFloat((Float)var1);
         return 6;
      } else if (var1 instanceof Double) {
         var0.writeShort(109);
         var0.writeDouble((Double)var1);
         return 10;
      } else if (var1 instanceof Character) {
         var0.writeShort(110);
         var0.writeChar((Character)var1);
         return 4;
      } else {
         throw new StreamCorruptedException(JMSClientExceptionLogger.logSimpleObjectLoggable(var1.getClass().getName()).getMessage());
      }
   }

   private static Object readBigStringBasicType(ObjectInput var0) throws IOException {
      short var1 = var0.readShort();
      if (var1 == 112) {
         try {
            return (String)var0.readObject();
         } catch (ClassNotFoundException var3) {
            throw new IOException(var3.toString());
         }
      } else {
         return readBasicType(var0, var1);
      }
   }

   private static Object readBasicType(DataInput var0) throws IOException {
      short var1 = var0.readShort();
      return readBasicType(var0, var1);
   }

   private static Object readBasicType(DataInput var0, short var1) throws IOException {
      switch (var1) {
         case 99:
            return null;
         case 100:
         case 101:
         default:
            throw new StreamCorruptedException(JMSClientExceptionLogger.logUnrecognizedClassCodeLoggable(var1).getMessage());
         case 102:
            return DataInputStream.readUTF(var0);
         case 103:
            return var0.readBoolean();
         case 104:
            return new Integer(var0.readInt());
         case 105:
            return new Long(var0.readLong());
         case 106:
            return new Byte(var0.readByte());
         case 107:
            return new Short(var0.readShort());
         case 108:
            return new Float(var0.readFloat());
         case 109:
            return new Double(var0.readDouble());
         case 110:
            return new Character(var0.readChar());
         case 111:
            byte[] var2 = new byte[var0.readInt()];
            var0.readFully(var2);
            return var2;
      }
   }

   private static void writeUTF(DataOutput var0, String var1, int var2) throws IOException {
      if (var2 > 32767) {
         throw new UTFDataFormatException();
      } else {
         var0.writeShort(var2);
         int var3 = var1.length();

         for(int var4 = 0; var4 < var3; ++var4) {
            DataIO.writeUTFChar(var0, var1.charAt(var4));
         }

      }
   }
}
