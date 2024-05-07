package weblogic.wsee.conversation;

import java.io.Serializable;

public class ConversationPhase implements Serializable {
   private static final long serialVersionUID = -2737098509951125428L;
   public static final ConversationPhase START = new ConversationPhase("start");
   public static final ConversationPhase CONTINUE = new ConversationPhase("continue");
   public static final ConversationPhase FINISH = new ConversationPhase("finish");
   public static final ConversationPhase NONE = new ConversationPhase("none");
   private final String name;

   public static ConversationPhase valueOf(String var0) {
      if (var0.equals(START.toString())) {
         return START;
      } else if (var0.equals(CONTINUE.toString())) {
         return CONTINUE;
      } else if (var0.equals(FINISH.toString())) {
         return FINISH;
      } else {
         return var0.equals(NONE.toString()) ? NONE : null;
      }
   }

   private ConversationPhase(String var1) {
      this.name = var1;
   }

   public String toString() {
      return this.name;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ConversationPhase)) {
         return false;
      } else {
         ConversationPhase var2 = (ConversationPhase)var1;
         if (this.name != null) {
            if (!this.name.equals(var2.name)) {
               return false;
            }
         } else if (var2.name != null) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      return this.name != null ? this.name.hashCode() : 0;
   }
}
