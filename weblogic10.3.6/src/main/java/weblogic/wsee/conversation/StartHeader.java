package weblogic.wsee.conversation;

import javax.xml.namespace.QName;
import weblogic.wsee.message.MsgHeaderType;

public class StartHeader extends ConversationHeader {
   private static final long serialVersionUID = 8743527470266529350L;
   public static final QName NAME;
   public static final MsgHeaderType TYPE;

   public StartHeader(String var1) {
      super(var1);
   }

   public StartHeader(String var1, String var2) {
      super(var1, var2);
   }

   public StartHeader() {
   }

   public QName getName() {
      return ConversationConstants.CONV_HEADER_START;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   static {
      NAME = ConversationConstants.CONV_HEADER_START;
      TYPE = new MsgHeaderType();
   }
}
