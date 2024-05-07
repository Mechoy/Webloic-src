package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.InvalidCompletionStatusException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Logger;
import weblogic.auddi.util.Util;

public class CompletionStatus extends UDDIListObject implements Serializable {
   public static final String STATUS_COMPLETE = "status:complete";
   public static final String STATUS_TO_KEY_INCOMPLETE = "status:toKey_incomplete";
   public static final String STATUS_FROM_KEY_INCOMPLETE = "status:fromKey_incomplete";
   private String m_text = null;

   public CompletionStatus(String var1) throws InvalidCompletionStatusException {
      if (!var1.equals("status:complete") && !var1.equals("status:toKey_incomplete") && !var1.equals("status:fromKey_incomplete")) {
         throw new InvalidCompletionStatusException(UDDIMessages.get("error.invalidCompletionStatus.status", var1));
      } else {
         this.m_text = var1;
      }
   }

   public CompletionStatus(CompletionStatus var1) {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         this.m_text = var1.m_text;
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof CompletionStatus)) {
         return false;
      } else {
         CompletionStatus var2 = (CompletionStatus)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_text, (Object)var2.m_text);
         if (!var3) {
            Logger.debug("CompletionStatus not equal:::" + this.m_text + "!=" + var2.m_text);
         }

         return var3;
      }
   }

   public String getCompletionStatus() {
      return this.m_text;
   }

   public String toString() {
      return this.m_text == null ? "" : this.m_text;
   }

   public String toXML() {
      if (this.m_text == null) {
         return "";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append("<").append("completionStatus");
         var1.append(">").append(this.fixStringForXML(this.m_text)).append("</").append("completionStatus").append(">");
         return var1.toString();
      }
   }
}
