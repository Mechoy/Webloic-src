package weblogic.messaging.saf.store;

import java.io.Externalizable;
import java.io.IOException;
import java.util.HashMap;
import weblogic.messaging.saf.common.SAFConversationInfoImpl;
import weblogic.messaging.saf.internal.ReceivingAgentImpl;
import weblogic.messaging.saf.internal.SendingAgentImpl;
import weblogic.store.helper.StoreObjectHandler;

public final class SAFObjectHandler extends StoreObjectHandler {
   private static final Class[] CLASS_CLASS = new Class[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, SendingAgentImpl.class, ReceivingAgentImpl.class, SAFConversationInfoImpl.class, String.class};
   public static final short SAFHISTORYRECORD = 12;
   public static final short SAFCONVERSATIONID = 13;
   public static final short SAFID = 14;
   public static final short SENDINGAGENT = 15;
   public static final short RECEIVINGAGENT = 16;
   public static final short SAFCONVERSATION = 17;
   private static HashMap classToId = new HashMap();

   public final Class getClassForId(short var1) {
      return CLASS_CLASS[var1];
   }

   public final Short getIdForClass(Object var1) {
      return (Short)classToId.get(var1.getClass());
   }

   public final void checkIfClassRecognized(short var1) throws IOException {
      if (var1 < 0 || var1 >= CLASS_CLASS.length || CLASS_CLASS[var1] == null) {
         throw new IOException("Unrecognized class code " + var1);
      }
   }

   protected boolean haveExternal(short var1) {
      return 12 <= var1;
   }

   protected Externalizable newExternal(short var1) throws IOException {
      if (var1 < 16) {
         switch (var1) {
            case 15:
               return new SendingAgentImpl();
         }
      }

      switch (var1) {
         case 16:
            return new ReceivingAgentImpl();
         case 17:
            return new SAFConversationInfoImpl();
         default:
            assert false;

            throw new IOException("Unrecognized class code " + var1);
      }
   }

   static {
      for(short var0 = 0; var0 < CLASS_CLASS.length; ++var0) {
         classToId.put(CLASS_CLASS[var0], new Short(var0));
      }

   }
}
