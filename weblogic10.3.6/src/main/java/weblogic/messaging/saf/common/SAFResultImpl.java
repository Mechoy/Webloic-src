package weblogic.messaging.saf.common;

import java.util.List;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFResult;

public class SAFResultImpl implements SAFResult {
   private int resultCode;
   private List sequenceNumbers;
   private SAFConversationInfo info;
   private String description;
   private SAFException safException;

   public SAFResultImpl(SAFConversationInfo var1, List var2, int var3, String var4) {
      this.info = var1;
      this.sequenceNumbers = var2;
      this.resultCode = var3;
      this.description = var4;
   }

   public boolean isDuplicate() {
      return this.resultCode == 1;
   }

   public boolean isSuccessful() {
      return this.resultCode == 0;
   }

   public int getResultCode() {
      return this.resultCode;
   }

   public void setResultCode(int var1) {
      this.resultCode = var1;
   }

   public void setSAFException(SAFException var1) {
      this.safException = var1;
   }

   public SAFException getSAFException() {
      return this.safException;
   }

   public String getDescription() {
      return this.description;
   }

   public List getSequenceNumbers() {
      return this.sequenceNumbers;
   }

   public void setSequenceNumbers(List var1) {
      this.sequenceNumbers = var1;
   }

   public SAFConversationInfo getConversationInfo() {
      return this.info;
   }

   public void setConversationInfo(SAFConversationInfo var1) {
      this.info = var1;
   }

   private boolean isValidResultCode(int var1) {
      return var1 == 0 || var1 == 1 || var1 == 2 || var1 == 3 || var1 == 4 || var1 == 5 || var1 == 6 || var1 == 7 || var1 == 8 || var1 == 9 || var1 == 10 || var1 == 11 || var1 == 19 || var1 == 12;
   }

   public SAFResultImpl() {
   }
}
