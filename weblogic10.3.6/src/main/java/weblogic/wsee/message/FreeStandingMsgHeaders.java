package weblogic.wsee.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.NodeList;
import weblogic.wsee.util.ToStringWriter;

public class FreeStandingMsgHeaders implements MsgHeaders, Serializable {
   private final Map headers = new HashMap();

   public MsgHeader getHeader(MsgHeaderType var1) throws MsgHeaderException {
      return (MsgHeader)this.headers.get(var1);
   }

   public void addHeader(MsgHeader var1) throws MsgHeaderException {
      if (this.headers.containsKey(var1.getType())) {
         throw new MsgHeaderException("FreeStandingMsgHeaders already contains header of type " + var1.getName() + ". Cannot add another one.");
      } else {
         this.headers.put(var1.getType(), var1);
      }
   }

   public void addHeaders(NodeList var1) throws MsgHeaderException {
   }

   public boolean isEmpty() {
      return this.headers.isEmpty();
   }

   public void merge(FreeStandingMsgHeaders var1) {
      Iterator var2 = var1.listHeaders();

      while(var2.hasNext()) {
         MsgHeader var3 = (MsgHeader)var2.next();
         if (var3.isMultiple()) {
            this.addHeader(var3);
         } else if (this.getHeader(var3.getType()) == null) {
            this.addHeader(var3);
         }
      }

   }

   public Iterator listHeaders() {
      return this.headers.values().iterator();
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.writeMap("headers", this.headers);
   }
}
