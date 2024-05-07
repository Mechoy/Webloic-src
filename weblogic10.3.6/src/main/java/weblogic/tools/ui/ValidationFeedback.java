package weblogic.tools.ui;

import java.awt.Component;

public class ValidationFeedback implements IValidationFeedback {
   private String errmsg;
   private Component comp;

   public ValidationFeedback(String var1, Component var2) {
      this.errmsg = var1;
      this.comp = var2;
   }

   public String getMessage() {
      return this.errmsg;
   }

   public void setMessage(String var1) {
      this.errmsg = var1;
   }

   public Component getFocusComponent() {
      return this.comp;
   }

   public void setFocusComponent(Component var1) {
      this.comp = var1;
   }
}
