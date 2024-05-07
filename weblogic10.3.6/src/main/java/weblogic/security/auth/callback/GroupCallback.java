package weblogic.security.auth.callback;

import java.util.ArrayList;
import java.util.Collection;
import javax.security.auth.callback.Callback;

public class GroupCallback implements Callback {
   private Collection value = null;
   private String prompt;

   public GroupCallback(String var1) {
      this.prompt = var1;
   }

   public String getPrompt() {
      return this.prompt;
   }

   public Collection getValue() {
      return this.value;
   }

   public void setValue(Collection var1) {
      this.value = new ArrayList(var1);
   }
}
