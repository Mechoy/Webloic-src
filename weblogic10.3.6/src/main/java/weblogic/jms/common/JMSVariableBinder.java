package weblogic.jms.common;

import java.util.HashMap;
import weblogic.jms.extensions.WLMessage;
import weblogic.messaging.Message;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.runtime.MessageInfo;
import weblogic.utils.expressions.Expression;
import weblogic.utils.expressions.ExpressionEvaluationException;
import weblogic.utils.expressions.Variable;
import weblogic.utils.expressions.VariableBinder;
import weblogic.utils.expressions.Expression.Type;

public final class JMSVariableBinder implements VariableBinder {
   public static final JMSVariableBinder THE_ONE = new JMSVariableBinder();
   private static final HashMap VARIABLES = new HashMap();

   public Variable getVariable(String var1) {
      Variable var2 = (Variable)VARIABLES.get(var1);
      return (Variable)(var2 != null ? var2 : new JMSPropertiesVariable(var1));
   }

   protected static WLMessage msg(Object var0) {
      return (WLMessage)element(var0).getMessage();
   }

   protected static Message kmsg(Object var0) {
      return element(var0).getMessage();
   }

   protected static MessageElement element(Object var0) {
      return (MessageElement)var0;
   }

   protected static ExpressionEvaluationException wrapException(javax.jms.JMSException var0) throws ExpressionEvaluationException {
      ExpressionEvaluationException var1 = new ExpressionEvaluationException("Failed to bind variable");
      var1.initCause(var0);
      throw var1;
   }

   static {
      VARIABLES.put("JMSDeliveryMode", new JMSDeliveryModeVariable());
      VARIABLES.put("JMSMessageID", new JMSMessageIDVariable());
      VARIABLES.put("JMSMessageId", new JMSMessageIDVariable());
      VARIABLES.put("JMSTimestamp", new JMSTimestampVariable());
      VARIABLES.put("JMSCorrelationID", new JMSCorrelationIDVariable());
      VARIABLES.put("JMSType", new JMSTypeVariable());
      VARIABLES.put("JMSPriority", new JMSPriorityVariable());
      VARIABLES.put("JMSExpiration", new JMSExpirationVariable());
      VARIABLES.put("JMSRedelivered", new JMSRedeliveredVariable());
      VARIABLES.put("JMSDeliveryTime", new JMSDeliveryTimeVariable());
      VARIABLES.put("JMS_BEA_DeliveryTime", new JMSDeliveryTimeVariable());
      VARIABLES.put("JMSRedeliveryLimit", new JMSRedeliveryLimitVariable());
      VARIABLES.put("JMS_BEA_RedeliveryLimit", new JMSRedeliveryLimitVariable());
      VARIABLES.put("JMS_BEA_Size", new JMS_BEA_SizeVariable());
      VARIABLES.put("JMS_BEA_UnitOfOrder", new JMS_BEA_UnitOfOrderVariable());
      VARIABLES.put("JMSXUserID", new JMSXUserIDVariable());
      VARIABLES.put("JMSXDeliveryCount", new JMSXDeliveryCountVariable());
      VARIABLES.put("JMS_BEA_State", new JMSBEAStateVariable());
      VARIABLES.put("JMS_WL_DDForwarded", new JMS_WL_DDForwardedVariable());
   }

   private static class JMS_WL_DDForwardedVariable implements Variable {
      private JMS_WL_DDForwardedVariable() {
      }

      public Object get(Object var1) {
         return new Boolean(((WLMessage)JMSVariableBinder.element(var1).getMessage()).getDDForwarded());
      }

      public Expression.Type getType() {
         return Type.BOOLEAN;
      }

      // $FF: synthetic method
      JMS_WL_DDForwardedVariable(Object var1) {
         this();
      }
   }

   private static class JMSBEAStateVariable implements Variable {
      private JMSBEAStateVariable() {
      }

      public Object get(Object var1) {
         return MessageInfo.getStateString(JMSVariableBinder.element(var1).getState());
      }

      public Expression.Type getType() {
         return Type.STRING;
      }

      // $FF: synthetic method
      JMSBEAStateVariable(Object var1) {
         this();
      }
   }

   private static class JMSXDeliveryCountVariable implements Variable {
      private JMSXDeliveryCountVariable() {
      }

      public Object get(Object var1) {
         return new Integer(JMSVariableBinder.element(var1).getDeliveryCount());
      }

      public Expression.Type getType() {
         return Type.NUMERIC;
      }

      // $FF: synthetic method
      JMSXDeliveryCountVariable(Object var1) {
         this();
      }
   }

   private static class JMSXUserIDVariable implements Variable {
      private JMSXUserIDVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return JMSVariableBinder.msg(var1).getStringProperty("JMSXUserID");
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
         }
      }

      public Expression.Type getType() {
         return Type.STRING;
      }

      // $FF: synthetic method
      JMSXUserIDVariable(Object var1) {
         this();
      }
   }

   private static class JMS_BEA_UnitOfOrderVariable implements Variable {
      private JMS_BEA_UnitOfOrderVariable() {
      }

      public Object get(Object var1) throws ExpressionEvaluationException {
         try {
            return JMSVariableBinder.msg(var1).getStringProperty("JMS_BEA_UnitOfOrder");
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
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
         return new Long(JMSVariableBinder.kmsg(var1).size());
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
            return new Integer(JMSVariableBinder.msg(var1).getJMSRedeliveryLimit());
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
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
            return new Long(JMSVariableBinder.msg(var1).getJMSDeliveryTime());
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
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

      public Object get(Object var1) {
         return JMSVariableBinder.element(var1).getDeliveryCount() > 1;
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
            return new Long(JMSVariableBinder.msg(var1).getJMSExpiration());
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
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
            return new Integer(JMSVariableBinder.msg(var1).getJMSPriority());
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
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
            return JMSVariableBinder.msg(var1).getJMSType();
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
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
            return JMSVariableBinder.msg(var1).getJMSCorrelationID();
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
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
            return new Long(JMSVariableBinder.msg(var1).getJMSTimestamp());
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
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
            return JMSVariableBinder.msg(var1).getJMSMessageID();
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
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
            return JMSVariableBinder.msg(var1).getJMSDeliveryMode() == 2 ? "PERSISTENT" : "NON_PERSISTENT";
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
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
            return JMSVariableBinder.msg(var1).getObjectProperty(this.key);
         } catch (javax.jms.JMSException var3) {
            throw JMSVariableBinder.wrapException(var3);
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
