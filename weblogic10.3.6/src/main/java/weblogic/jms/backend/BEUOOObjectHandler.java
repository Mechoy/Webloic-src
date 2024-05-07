package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import weblogic.messaging.path.helper.KeyString;
import weblogic.store.helper.StoreObjectHandler;

public final class BEUOOObjectHandler extends StoreObjectHandler {
   private static Object KEY_STRING_CLASS = (new KeyString()).getClass();
   private static Object MEMBER_CLASS = (new BEUOOMember()).getClass();
   public static final short KEYSTRING = 1;
   public static final short UOOMEMBER = 2;
   private static final Short SHORT_KEY = new Short((short)1);
   private static final Short SHORT_MEMBER = new Short((short)2);

   public final Class getClassForId(short var1) {
      if (var1 == SHORT_KEY) {
         return KeyString.class;
      } else {
         return var1 == SHORT_MEMBER ? BEUOOMember.class : null;
      }
   }

   public final Short getIdForClass(Object var1) {
      Class var2 = var1.getClass();
      if (var2 == KEY_STRING_CLASS) {
         return SHORT_KEY;
      } else {
         return var2 == MEMBER_CLASS ? SHORT_MEMBER : null;
      }
   }

   public final void checkIfClassRecognized(short var1) throws IOException {
      if (var1 < 1 || var1 > 2) {
         throw new IOException("Unrecognized class code " + var1);
      }
   }

   protected boolean haveExternal(short var1) {
      switch (var1) {
         case 1:
         case 2:
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
            return new BEUOOMember();
         default:
            assert false;

            throw new IOException("Unrecognized class code " + var1);
      }
   }
}
