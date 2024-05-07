package weblogic.jms.deployer;

public interface DeployerConstants {
   String[][] DEFAULT_FACTORY_NAMES = new String[][]{{"DefaultConnectionFactory", "weblogic.jms.ConnectionFactory"}, {"DefaultXAConnectionFactory", "weblogic.jms.XAConnectionFactory"}, {"DefaultXAConnectionFactory0", "weblogic.jms.XAConnectionFactory0"}, {"DefaultXAConnectionFactory1", "weblogic.jms.XAConnectionFactory1"}, {"DefaultXAConnectionFactory2", "weblogic.jms.XAConnectionFactory2"}, {"MessageDrivenBeanConnectionFactory", "weblogic.jms.MessageDrivenBeanConnectionFactory"}, {"QueueConnectionFactory", "javax.jms.QueueConnectionFactory"}, {"TopicConnectionFactory", "javax.jms.TopicConnectionFactory"}};
}
