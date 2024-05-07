package weblogic.upgrade.jms;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.HashMap;
import weblogic.jms.common.BytesMessageImpl;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.HdrMessageImpl;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.MapMessageImpl;
import weblogic.jms.common.ObjectMessageImpl;
import weblogic.jms.common.StreamMessageImpl;
import weblogic.jms.common.TextMessageImpl;
import weblogic.jms.common.XMLMessageImpl;

public class UpgradeIOBypass {
   private static final int BDOS_VERSION = 1234;
   private static final Class[] CLASS_CLASS = new Class[]{null, UpgradeConsumer.class, null, UpgradeXAXid.class, UpgradeXATranEntryReceive.class, BytesMessageImpl.class, HdrMessageImpl.class, MapMessageImpl.class, ObjectMessageImpl.class, StreamMessageImpl.class, TextMessageImpl.class, XMLMessageImpl.class, DestinationImpl.class, JMSID.class, JMSMessageId.class, JMSServerId.class, null, UpgradeDurableTopicMessageInfo.class, String.class, UpgradeDistConsumer.class, UpgradeMoveMessagePendingDeleteRecord.class, UpgradeDestinationDeleteRecord.class};
   public static final short JMSSUBSCRIBER = 1;
   public static final short JMSBEXAXID = 3;
   public static final short JMSBEXATRANENTRYRECEIVE = 4;
   public static final short JMSBYTESMESSAGE = 5;
   public static final short JMSHDRMESSAGE = 6;
   public static final short JMSMAPMESSAGE = 7;
   public static final short JMSOBJECTMESSAGE = 8;
   public static final short JMSSTREAMMESSAGE = 9;
   public static final short JMSTEXTMESSAGE = 10;
   public static final short JMSXMLMESSAGE = 11;
   public static final short DURABLETOPICMESSAGEINFO = 17;
   private static final short STRING = 18;
   public static final short JMSDISTSUBSCRIBER = 19;
   public static final short MOVEMESSAGINGPENDING = 20;
   public static final short DESTINATIONDELETERECORD = 21;
   private static HashMap classToId;
   private static final short CODE_NULL = -1;
   private static final short CODE_NOTFOUND = -3;

   public UpgradeIOBypass() {
      synchronized(CLASS_CLASS) {
         if (classToId == null) {
            classToId = new HashMap();

            for(short var2 = 0; var2 < CLASS_CLASS.length; ++var2) {
               if (CLASS_CLASS[var2] != null) {
                  classToId.put(CLASS_CLASS[var2], new Short(var2));
               }
            }

         }
      }
   }

   public final short getCode(Object var1) {
      if (var1 == null) {
         return -1;
      } else {
         Short var2 = (Short)classToId.get(var1.getClass());
         return var2 == null ? -3 : var2;
      }
   }

   public final Object readObject(ObjectInput var1) throws ClassNotFoundException, IOException {
      short var2 = var1.readShort();
      if (var2 == -1) {
         return null;
      } else if (var2 == 18) {
         return readUTF32(var1);
      } else if (var2 >= 0 && var2 < CLASS_CLASS.length && CLASS_CLASS[var2] != null) {
         Class var3 = CLASS_CLASS[var2];

         Externalizable var4;
         try {
            var4 = (Externalizable)((Externalizable)var3.newInstance());
         } catch (InstantiationException var6) {
            throw new ClassNotFoundException(var6.toString() + ", " + var3.getName());
         } catch (IllegalAccessException var7) {
            throw new ClassNotFoundException(var7.toString() + ", " + var3.getName());
         } catch (SecurityException var8) {
            throw new ClassNotFoundException(var8.toString() + ", " + var3.getName());
         }

         var4.readExternal(var1);
         return var4;
      } else {
         throw new IOException("Unrecognized class code " + var2);
      }
   }

   private static final String readUTF32(ObjectInput var0) throws IOException {
      int var1 = var0.readInt();
      char[] var2 = new char[var1];
      int var3 = 0;
      int var4 = 0;

      while(var3 < var1) {
         int var5 = var0.readUnsignedByte();
         int var6;
         switch (var5 >> 4) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
               ++var3;
               var2[var4++] = (char)var5;
               break;
            case 8:
            case 9:
            case 10:
            case 11:
            default:
               throw new IOException("Error decoding String.");
            case 12:
            case 13:
               var3 += 2;
               if (var3 > var1) {
                  throw new IOException("Error decoding String.");
               }

               var6 = var0.readUnsignedByte();
               if ((var6 & 192) != 128) {
                  throw new IOException("Error decoding String.");
               }

               var2[var4++] = (char)((var5 & 31) << 6 | var6 & 63);
               break;
            case 14:
               var3 += 3;
               if (var3 > var1) {
                  throw new IOException("Error decoding String.");
               }

               var6 = var0.readUnsignedByte();
               int var7 = var0.readUnsignedByte();
               if ((var6 & 192) != 128 || (var7 & 192) != 128) {
                  throw new IOException("Error decoding String.");
               }

               var2[var4++] = (char)((var5 & 15) << 12 | (var6 & 63) << 6 | (var7 & 63) << 0);
         }
      }

      return new String(var2, 0, var4);
   }
}
