package weblogic.ejb20.cmp.rdbms.finders;

import java.io.Serializable;

public class EJBQLToken implements Serializable {
   String tokenText;
   Boolean hadException;

   public EJBQLToken() {
      this("");
   }

   public EJBQLToken(String var1) {
      this(var1, false);
   }

   public EJBQLToken(String var1, boolean var2) {
      this.tokenText = null;
      this.tokenText = var1;
      this.hadException = new Boolean(var2);
   }

   public boolean getHadException() {
      return this.hadException;
   }

   public void setHadException(boolean var1) {
      this.hadException = new Boolean(var1);
   }

   public String getTokenText() {
      return this.tokenText;
   }

   public void setTokenText(String var1) {
      this.tokenText = var1;
   }

   public void prependTokenText(String var1) {
      if (this.tokenText == null) {
         this.tokenText = var1;
      } else {
         this.tokenText = var1 + this.tokenText;
      }
   }

   public void appendTokenText(String var1) {
      if (this.tokenText == null) {
         this.tokenText = var1;
      } else {
         this.tokenText = this.tokenText + var1;
      }
   }
}
