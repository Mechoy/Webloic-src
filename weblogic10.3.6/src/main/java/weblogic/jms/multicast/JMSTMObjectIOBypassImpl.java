package weblogic.jms.multicast;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import weblogic.jms.common.BufferDataInputStream;
import weblogic.jms.common.BufferDataOutputStream;
import weblogic.jms.common.BytesMessageImpl;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.HdrMessageImpl;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.MapMessageImpl;
import weblogic.jms.common.ObjectIOBypass;
import weblogic.jms.common.ObjectMessageImpl;
import weblogic.jms.common.StreamMessageImpl;
import weblogic.jms.common.TextMessageImpl;
import weblogic.jms.common.XMLMessageImpl;

public final class JMSTMObjectIOBypassImpl implements ObjectIOBypass {
   private static final Class[] CLASS_ARRAY = new Class[]{null, null, null, null, null, BytesMessageImpl.class, HdrMessageImpl.class, MapMessageImpl.class, ObjectMessageImpl.class, StreamMessageImpl.class, TextMessageImpl.class, XMLMessageImpl.class, DestinationImpl.class, JMSID.class, JMSMessageId.class, JMSServerId.class, null, null, String.class, null};
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
   private static HashMap classToId;
   private static final short CODE_NULL = -1;
   private static final short CODE_NOTFOUND = -3;

   JMSTMObjectIOBypassImpl() {
      synchronized(CLASS_ARRAY) {
         if (classToId == null) {
            classToId = new HashMap();

            for(short var2 = 0; var2 < CLASS_ARRAY.length; ++var2) {
               if (CLASS_ARRAY[var2] != null) {
                  classToId.put(CLASS_ARRAY[var2], new Short(var2));
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

   public final void writeObject(ObjectOutput var1, Object var2) throws IOException {
      if (var2 == null) {
         var1.writeShort(-1);
      } else {
         Short var3 = (Short)classToId.get(var2.getClass());
         if (var3 == null) {
            throw new IOException("Can't serialize class, type=" + var2.getClass().getName());
         } else {
            short var4;
            var1.writeShort(var4 = var3);
            if (var4 == 18) {
               BufferDataOutputStream.writeUTF32(var1, (String)var2);
            } else {
               ((Externalizable)var2).writeExternal(var1);
            }

         }
      }
   }

   public final Object readObject(ObjectInput var1) throws ClassNotFoundException, IOException {
      short var2 = var1.readShort();
      if (var2 == -1) {
         return null;
      } else if (var2 == 18) {
         return BufferDataInputStream.readUTF32(var1);
      } else if (var2 >= 0 && var2 < CLASS_ARRAY.length && CLASS_ARRAY[var2] != null) {
         Class var3 = CLASS_ARRAY[var2];

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
}
