package weblogic.messaging.saf.internal;

import java.io.Externalizable;
import java.util.HashMap;
import javax.jms.JMSException;
import weblogic.jms.extensions.WLMessage;
import weblogic.messaging.Message;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.saf.SAFRequest;
import weblogic.utils.expressions.Expression;
import weblogic.utils.expressions.ExpressionEvaluationException;
import weblogic.utils.expressions.Variable;
import weblogic.utils.expressions.VariableBinder;
import weblogic.utils.expressions.Expression.Type;

final class SAFVariableBinder implements VariableBinder {
   public static final SAFVariableBinder THE_ONE = new SAFVariableBinder();
   private static final HashMap VARIABLES = new HashMap();

   public Variable getVariable(String var1) {
      Variable var2 = (Variable)VARIABLES.get(var1);
      return (Variable)(var2 != null ? var2 : new JMSPropertiesVariable(var1));
   }

   private static SAFRequest saf(Object var0) {
      if (var0 instanceof SAFRequest) {
         return (SAFRequest)var0;
      } else {
         return var0 instanceof MessageElement ? (SAFRequest)((MessageElement)var0).getMessage() : null;
      }
   }

   private static WLMessage msg(Object var0) {
      if (var0 instanceof SAFRequest) {
         Externalizable var1 = ((SAFRequest)var0).getPayload();
         if (var1 instanceof WLMessage) {
            return (WLMessage)var1;
         }
      }

      return var0 instanceof MessageElement ? (WLMessage)((MessageElement)var0).getMessage() : null;
   }

   private static Message kmsg(Object var0) {
      if (var0 instanceof SAFRequest) {
         Externalizable var1 = ((SAFRequest)var0).getPayload();
         if (var1 instanceof Message) {
            return (Message)var1;
         }
      }

      return var0 instanceof MessageElement ? ((MessageElement)var0).getMessage() : null;
   }

   private static ExpressionEvaluationException wrapException(JMSException var0) throws ExpressionEvaluationException {
      ExpressionEvaluationException var1 = new ExpressionEvaluationException("Failed to bind variable");
      var1.initCause(var0);
      throw var1;
   }

   static {
      VARIABLES.put("JMSDeliveryMode", new JMSDeliveryModeVariable());
      VARIABLES.put("JMSMessageID", new JMSMessageIDVariable());
      VARIABLES.put("JMSTimestamp", new JMSTimestampVariable());
      VARIABLES.put("JMSCorrelationID", new JMSCorrelationIDVariable());
      VARIABLES.put("JMSType", new JMSTypeVariable());
      VARIABLES.put("JMSPriority", new JMSPriorityVariable());
      VARIABLES.put("JMSExpiration", new JMSExpirationVariable());
      VARIABLES.put("JMSRedelivered", new JMSRedeliveredVariable());
      VARIABLES.put("JMSDeliveryTime", new JMSDeliveryTimeVariable());
      VARIABLES.put("JMSRedeliveryLimit", new JMSRedeliveryLimitVariable());
      VARIABLES.put("JMS_BEA_Size", new JMS_BEA_SizeVariable());
      VARIABLES.put("JMS_BEA_UnitOfOrder", new JMS_BEA_UnitOfOrderVariable());
      VARIABLES.put("SAFConversationName", new SAFConversationNameVariable());
      VARIABLES.put("SAFSequenceNumber", new SAFSequenceNumberVariable());
      VARIABLES.put("SAFExpiration", new SAFExpirationVariable());
      VARIABLES.put("SAFDeliveryMode", new SAFDeliveryModeVariable());
      VARIABLES.put("SAFTimestamp", new SAFTimestampVariable());
      VARIABLES.put("SAFMessageID", new SAFMessageIDVariable());
   }

   private static class SAFMessageIDVariable implements Variable {
      private SAFMessageIDVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         return SAFVariableBinder.saf(var1).getMessageId();
      }

      public Expression.Type getType() {
         return Type.STRING;
      }

      // $FF: synthetic method
      SAFMessageIDVariable(Object var1) {
         this();
      }
   }

   private static class SAFTimestampVariable implements Variable {
      private SAFTimestampVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         return new Long(SAFVariableBinder.saf(var1).getTimestamp());
      }

      public Expression.Type getType() {
         return Type.NUMERIC;
      }

      // $FF: synthetic method
      SAFTimestampVariable(Object var1) {
         this();
      }
   }

   private static class SAFDeliveryModeVariable implements Variable {
      private SAFDeliveryModeVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         return SAFVariableBinder.saf(var1).getDeliveryMode() == 2 ? "PERSISTENT" : "NON_PERSISTENT";
      }

      public Expression.Type getType() {
         return Type.STRING;
      }

      // $FF: synthetic method
      SAFDeliveryModeVariable(Object var1) {
         this();
      }
   }

   private static class SAFExpirationVariable implements Variable {
      private SAFExpirationVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         return SAFVariableBinder.saf(var1).getTimeToLive() == -1L ? new Long(-1L) : new Long(SAFVariableBinder.saf(var1).getTimestamp() + SAFVariableBinder.saf(var1).getTimeToLive());
      }

      public Expression.Type getType() {
         return Type.NUMERIC;
      }

      // $FF: synthetic method
      SAFExpirationVariable(Object var1) {
         this();
      }
   }

   private static class SAFSequenceNumberVariable implements Variable {
      private SAFSequenceNumberVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         return new Long(SAFVariableBinder.saf(var1).getSequenceNumber());
      }

      public Expression.Type getType() {
         return Type.NUMERIC;
      }

      // $FF: synthetic method
      SAFSequenceNumberVariable(Object var1) {
         this();
      }
   }

   private static class SAFConversationNameVariable implements Variable {
      private SAFConversationNameVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         return SAFVariableBinder.saf(var1).getConversationName();
      }

      public Expression.Type getType() {
         return Type.STRING;
      }

      // $FF: synthetic method
      SAFConversationNameVariable(Object var1) {
         this();
      }
   }

   private static class JMS_BEA_UnitOfOrderVariable implements Variable {
      private JMS_BEA_UnitOfOrderVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return SAFVariableBinder.msg(var1).getStringProperty("JMS_BEA_UnitOfOrder");
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.STRING;
      }

      // $FF: synthetic method
      JMS_BEA_UnitOfOrderVariable(Object var1) {
         this();
      }
   }

   private static class JMS_BEA_SizeVariable implements Variable {
      private JMS_BEA_SizeVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         return new Long(SAFVariableBinder.kmsg(var1).size());
      }

      public Expression.Type getType() {
         return Type.NUMERIC;
      }

      // $FF: synthetic method
      JMS_BEA_SizeVariable(Object var1) {
         this();
      }
   }

   private static class JMSRedeliveryLimitVariable implements Variable {
      private JMSRedeliveryLimitVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return new Integer(SAFVariableBinder.msg(var1).getJMSRedeliveryLimit());
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.NUMERIC;
      }

      // $FF: synthetic method
      JMSRedeliveryLimitVariable(Object var1) {
         this();
      }
   }

   private static class JMSDeliveryTimeVariable implements Variable {
      private JMSDeliveryTimeVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return new Long(SAFVariableBinder.msg(var1).getJMSDeliveryTime());
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.NUMERIC;
      }

      // $FF: synthetic method
      JMSDeliveryTimeVariable(Object var1) {
         this();
      }
   }

   private static class JMSRedeliveredVariable implements Variable {
      private JMSRedeliveredVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return SAFVariableBinder.msg(var1).getJMSRedelivered();
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.BOOLEAN;
      }

      // $FF: synthetic method
      JMSRedeliveredVariable(Object var1) {
         this();
      }
   }

   private static class JMSExpirationVariable implements Variable {
      private JMSExpirationVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return new Long(SAFVariableBinder.msg(var1).getJMSExpiration());
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.NUMERIC;
      }

      // $FF: synthetic method
      JMSExpirationVariable(Object var1) {
         this();
      }
   }

   private static class JMSPriorityVariable implements Variable {
      private JMSPriorityVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return new Integer(SAFVariableBinder.msg(var1).getJMSPriority());
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.NUMERIC;
      }

      // $FF: synthetic method
      JMSPriorityVariable(Object var1) {
         this();
      }
   }

   private static class JMSTypeVariable implements Variable {
      private JMSTypeVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return SAFVariableBinder.msg(var1).getJMSType();
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.STRING;
      }

      // $FF: synthetic method
      JMSTypeVariable(Object var1) {
         this();
      }
   }

   private static class JMSCorrelationIDVariable implements Variable {
      private JMSCorrelationIDVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return SAFVariableBinder.msg(var1).getJMSCorrelationID();
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.STRING;
      }

      // $FF: synthetic method
      JMSCorrelationIDVariable(Object var1) {
         this();
      }
   }

   private static class JMSTimestampVariable implements Variable {
      private JMSTimestampVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return new Long(SAFVariableBinder.msg(var1).getJMSTimestamp());
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.NUMERIC;
      }

      // $FF: synthetic method
      JMSTimestampVariable(Object var1) {
         this();
      }
   }

   private static class JMSMessageIDVariable implements Variable {
      private JMSMessageIDVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return SAFVariableBinder.msg(var1).getJMSMessageID();
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.STRING;
      }

      // $FF: synthetic method
      JMSMessageIDVariable(Object var1) {
         this();
      }
   }

   private static class JMSDeliveryModeVariable implements Variable {
      private JMSDeliveryModeVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return SAFVariableBinder.msg(var1).getJMSDeliveryMode() == 2 ? "PERSISTENT" : "NON_PERSISTENT";
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.STRING;
      }

      // $FF: synthetic method
      JMSDeliveryModeVariable(Object var1) {
         this();
      }
   }

   private static class JMSPropertiesVariable implements Variable {
      private final String key;

      private JMSPropertiesVariable(String var1) {
         this.key = var1;
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return SAFVariableBinder.msg(var1).getObjectProperty(this.key);
         } catch (JMSException var3) {
            throw SAFVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.ANY;
      }

      // $FF: synthetic method
      JMSPropertiesVariable(String var1, Object var2) {
         this(var1);
      }
   }
}
