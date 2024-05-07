package weblogic.deployment.descriptors;

import java.util.ListResourceBundle;

public final class DDValidationBundle extends ListResourceBundle implements DDValidationErrorCodes {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   static final Object[][] contents = new Object[][]{{"ENV_VALUE_NOT_OF_TYPE", "{0} cannot be converted to type {1}"}, {"INVALID_EJB_REF_TYPE", "Invalid EJB Reference type"}, {"INVALID_ENV_TYPE", "{0} is not a valid environment type"}, {"INVALID_RES_AUTH", "The resource authorization was not \"Container\" or \"Application\""}, {"INVALID_RES_JNDI_NAME", "The JNDI name for the referenced resource factory is not a valid composite name"}, {"INVALID_RES_TYPE", "The referenced resource factory type was not one of \"javax.sql.DataSource,\"\"javax.jms.QueueConnectionFactory,\"\"javax.jms.TopicConnectionFactory\" or \"java.net.URL\""}, {"NO_EJB_REF_HOME_SET", "A home interface name was not set for this EJBReference"}, {"NO_EJB_REF_EJB_NAME_SET", "No ejb-name set for the referenced EJB"}, {"NO_EJB_REF_JNDI_NAME_SET", "No JNDI name set for the referenced EJB in EJB Reference {0}"}, {"NO_EJB_REF_NAME_SET", "A name was not set for this EJBReference"}, {"NO_EJB_REF_REMOTE_SET", "A remote interface name was not set for this EJBReference"}, {"NO_EJB_REF_TYPE_SET", "A type was not set for this EJBReference"}, {"NO_ENV_ENTRY_NAME_SET", "A name was not set for this EnvironmentEntry"}, {"NO_ENV_ENTRY_VALUE_SET", "A value was not set for this EnvironmentEntry"}, {"NO_RES_AUTH_SET", "A resource authorization was not set for this ResourceReference"}, {"NO_RES_JNDI_NAME", "No JNDI name set for the referenced resource factory for reference {0} of type {1}"}, {"NO_RES_REF_NAME_SET", "A name was not set for this ResourceReference"}, {"NO_RES_REF_TYPE_SET", "A resource type was not set for this ResourceReference"}};

   public Object[][] getContents() {
      return contents;
   }
}
