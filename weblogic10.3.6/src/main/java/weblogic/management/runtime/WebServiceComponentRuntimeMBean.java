package weblogic.management.runtime;

public interface WebServiceComponentRuntimeMBean extends RuntimeMBean {
   WebServiceRuntimeMBean[] getWebServiceRuntimes();
}
