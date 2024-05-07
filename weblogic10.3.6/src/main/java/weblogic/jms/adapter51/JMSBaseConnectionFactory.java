package weblogic.jms.adapter51;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Properties;
import javax.naming.Reference;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;
import weblogic.jms.bridge.AdapterConnectionFactory;
import weblogic.jms.bridge.AdapterMetaData;
import weblogic.jms.bridge.ConnectionSpec;
import weblogic.jms.bridge.SourceConnection;
import weblogic.jms.bridge.TargetConnection;

public class JMSBaseConnectionFactory implements AdapterConnectionFactory {
   static final long serialVersionUID = 2366460655498131081L;
   public static final String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
   private JMSManagedConnectionFactory mcf;
   private ConnectionManager cm;
   private Reference reference;
   private AdapterMetaData metaData;
   private transient PrintWriter logWriter;
   private boolean initialized;
   private Hashtable props = new Hashtable();

   public JMSBaseConnectionFactory(ManagedConnectionFactory var1, ConnectionManager var2) throws ResourceException {
      this.mcf = (JMSManagedConnectionFactory)var1;
      if (var2 == null) {
         this.cm = new JMSConnectionManager();
      } else {
         this.cm = var2;
      }

      this.metaData = new AdapterMetaDataImpl(var1);
      if (var1.getLogWriter() != null) {
         this.logWriter = var1.getLogWriter();
      }

   }

   public SourceConnection getSourceConnection() throws ResourceException {
      JMSConnectionRequestInfo var1 = new JMSConnectionRequestInfo((String)null, (String)null, 1);
      return (SourceConnection)this.cm.allocateConnection(this.mcf, var1);
   }

   public SourceConnection getSourceConnection(ConnectionSpec var1) throws ResourceException {
      JMSConnectionSpec var2 = (JMSConnectionSpec)var1;
      JMSConnectionRequestInfo var3 = new JMSConnectionRequestInfo(var2.getUser(), var2.getPassword(), 1, var2.getUrl(), var2.getInitialContextFactory(), var2.getSelector(), var2.getFactoryJndi(), var2.getDestJndi(), var2.getDestType(), var2.getName(), var2.getDurability(), var2.getClasspath());
      return (SourceConnection)this.cm.allocateConnection(this.mcf, var3);
   }

   public TargetConnection getTargetConnection() throws ResourceException {
      JMSConnectionRequestInfo var1 = new JMSConnectionRequestInfo((String)null, (String)null, 2);
      return (TargetConnection)this.cm.allocateConnection(this.mcf, var1);
   }

   public TargetConnection getTargetConnection(ConnectionSpec var1) throws ResourceException {
      JMSConnectionSpec var2 = (JMSConnectionSpec)var1;
      JMSConnectionRequestInfo var3 = new JMSConnectionRequestInfo(var2.getUser(), var2.getPassword(), 2, var2.getUrl(), var2.getInitialContextFactory(), (String)null, var2.getFactoryJndi(), var2.getDestJndi(), var2.getDestType(), var2.getClasspath());
      return (TargetConnection)this.cm.allocateConnection(this.mcf, var3);
   }

   public ConnectionSpec createConnectionSpec(Properties var1) throws ResourceException {
      return new JMSConnectionSpec(var1);
   }

   public PrintWriter getLogWriter() throws ResourceException {
      return this.logWriter;
   }

   public AdapterMetaData getMetaData() throws ResourceException {
      return this.metaData;
   }

   public long getTimeout() throws ResourceException {
      throw new NotSupportedException("Not implemented");
   }

   public void setLogWriter(PrintWriter var1) throws ResourceException {
      this.logWriter = var1;
   }

   public void setTimeout(long var1) throws ResourceException {
      throw new NotSupportedException("Not implemented");
   }

   public void setReference(Reference var1) {
      this.reference = var1;
   }

   public Reference getReference() {
      return this.reference;
   }

   public String getTransactionSupport() throws ResourceException {
      return this.mcf.getAdapterType();
   }
}
