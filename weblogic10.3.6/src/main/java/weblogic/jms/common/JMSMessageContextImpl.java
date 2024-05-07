package weblogic.jms.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.jms.Message;

public class JMSMessageContextImpl implements JMSMessageContext {
   private Message msg;
   protected Map propertyMap = new HashMap();
   private boolean result;
   private boolean done;
   private JMSMessageId msgId;
   private Destination destination = null;
   private JMSFailover failover = null;
   private String user = null;

   public JMSMessageContextImpl(Message var1) {
      this.msg = var1;
   }

   public Message getMessage() {
      return this.msg;
   }

   public void setMessage(Message var1) {
      this.msg = var1;
   }

   public String toString() {
      return this.propertyMap.toString() + this.msg.toString();
   }

   public void setProperty(String var1, Object var2) {
      this.propertyMap.put(var1, var2);
   }

   public Object getProperty(String var1) {
      return this.propertyMap.get(var1);
   }

   public void removeProperty(String var1) {
      this.propertyMap.remove(var1);
   }

   public boolean containsProperty(String var1) {
      return this.propertyMap.containsKey(var1);
   }

   public Iterator getPropertyNames() {
      return this.propertyMap.keySet().iterator();
   }

   public void setResult(boolean var1) {
      this.done = true;
      this.result = var1;
   }

   public boolean isDone() {
      return this.done;
   }

   public boolean isContinue() {
      return this.result;
   }

   public void setReturnedMessageId(JMSMessageId var1) {
      this.msgId = var1;
   }

   public Destination getDestination() {
      return this.destination;
   }

   public void setDestination(Destination var1) {
      this.destination = var1;
   }

   public JMSFailover getFailover() {
      return this.failover;
   }

   public void setFailover(JMSFailover var1) {
      this.failover = var1;
   }

   public String getUser() {
      return this.user;
   }

   public void setUser(String var1) {
      this.user = var1;
   }
}
