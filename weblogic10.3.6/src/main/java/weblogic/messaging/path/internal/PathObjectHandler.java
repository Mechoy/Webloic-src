package weblogic.messaging.path.internal;

import java.io.Externalizable;
import java.io.IOException;
import weblogic.messaging.path.Key;
import weblogic.messaging.path.helper.KeySerializable;
import weblogic.messaging.path.helper.KeyString;
import weblogic.messaging.path.helper.MemberString;
import weblogic.store.ObjectHandler;
import weblogic.store.helper.StoreObjectHandler;

public final class PathObjectHandler extends StoreObjectHandler {
   public static final short KEYSTRING = 1;
   public static final short MEMBERSTRING = 2;
   public static final short KEYSERIALIZABLE = 3;
   private static Object keyStringClass = (new KeyString()).getClass();
   private static Object memberStringClass = (new MemberString()).getClass();
   private static Object keySerializableClass = (new KeySerializable()).getClass();
   private static final Short shortKey = new Short((short)1);
   private static final Short shortMember = new Short((short)2);
   private static final Short shortSerializable = new Short((short)3);
   private static final ObjectHandler[] registeredHandlers;

   public static void setObjectHandler(byte var0, ObjectHandler var1) {
      assert registeredHandlers[var0] == null || registeredHandlers[var0] == var1;

      registeredHandlers[var0] = var1;
   }

   public static ObjectHandler getObjectHandler(byte var0) {
      return registeredHandlers[var0];
   }

   public final Class getClassForId(short var1) {
      return null;
   }

   public final Short getIdForClass(Object var1) {
      Class var2 = var1.getClass();
      if (var2 == keyStringClass) {
         return shortKey;
      } else if (var2 == memberStringClass) {
         return shortMember;
      } else {
         return var2 == keySerializableClass ? shortSerializable : null;
      }
   }

   public final void checkIfClassRecognized(short var1) throws IOException {
      throw new IOException("Unrecognized class code " + var1);
   }

   protected boolean haveExternal(short var1) {
      switch (var1) {
         case 1:
         case 2:
         case 3:
            return true;
         default:
            return false;
      }
   }

   protected Externalizable newExternal(short var1) throws IOException {
      switch (var1) {
         case 1:
            return new KeyString();
         case 2:
            return new MemberString();
         case 3:
            return new KeySerializable();
         default:
            assert false;

            throw new IOException("Unrecognized class code " + var1);
      }
   }

   static {
      registeredHandlers = new ObjectHandler[Key.RESERVED_SUBSYSTEMS.length];
      PathObjectHandler var0 = new PathObjectHandler();

      for(byte var1 = 0; var1 < registeredHandlers.length; ++var1) {
         if (var1 == 1) {
            registeredHandlers[var1] = null;
         } else {
            registeredHandlers[var1] = var0;
         }
      }

   }
}
