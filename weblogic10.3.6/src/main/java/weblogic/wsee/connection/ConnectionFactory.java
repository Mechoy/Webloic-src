package weblogic.wsee.connection;

import java.util.HashMap;
import weblogic.wsee.connection.local.LocalConnection;
import weblogic.wsee.connection.soap.SoapClientConnection;
import weblogic.wsee.connection.soap.SoapServerConnection;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.connection.transport.http.HTTPClientTransport;
import weblogic.wsee.connection.transport.https.HTTPSClientTransport;
import weblogic.wsee.connection.transport.jms.JmsTransport;
import weblogic.wsee.connection.transport.local.LocalTransport;
import weblogic.wsee.util.ToStringWriter;

public class ConnectionFactory {
   private static ConnectionFactory instance = new ConnectionFactory();
   private HashMap serverConnectionTypes = new HashMap();
   private HashMap clientConnectionTypes = new HashMap();
   private HashMap clientTransportTypes = new HashMap();

   private ConnectionFactory() {
      this.clientConnectionTypes.put("SOAP11", SoapClientConnection.class);
      this.clientConnectionTypes.put("SOAP12", SoapClientConnection.class);
      this.clientConnectionTypes.put("local", LocalConnection.class);
      this.serverConnectionTypes.put("SOAP11", SoapServerConnection.class);
      this.serverConnectionTypes.put("SOAP12", SoapServerConnection.class);
      this.clientTransportTypes.put("http", HTTPClientTransport.class);
      this.clientTransportTypes.put("https", HTTPSClientTransport.class);
      this.clientTransportTypes.put("local", LocalTransport.class);
      this.clientTransportTypes.put("jms", JmsTransport.class);
   }

   public static ConnectionFactory instance() {
      return instance;
   }

   private Transport createClientTransport(String var1) throws ConnectionException {
      Class var2 = (Class)this.clientTransportTypes.get(var1);
      if (var2 == null) {
         throw new ConnectionException("Failed to find transport=" + var1 + ". This factory only knows how to handle: \n" + this.toString());
      } else {
         return (Transport)this.newInstance(var2);
      }
   }

   public Connection createClientConnection(String var1, String var2) throws ConnectionException {
      Transport var3 = this.createClientTransport(var1);
      Class var4 = null;
      if ("local".equals(var1)) {
         var4 = LocalConnection.class;
      }

      if (var4 == null) {
         var4 = (Class)this.clientConnectionTypes.get(var2);
      }

      if (var4 == null) {
         throw new ConnectionException("Failed to find connection for transport='" + var3 + "' binding='" + var2 + "'. " + "This factory only knows how to handle: \n" + this.toString());
      } else {
         Connection var5 = (Connection)this.newInstance(var4);
         var5.setTransport(var3);
         return var5;
      }
   }

   public Connection createServerConnection(Transport var1, String var2) throws ConnectionException {
      Class var3 = (Class)this.serverConnectionTypes.get(var2);
      if (var3 == null) {
         throw new ConnectionException("Failed to find connection for transport='" + var1 + "' binding='" + var2 + "'. " + "This factory only knows how to handle: \n" + this.toString());
      } else {
         Connection var4 = (Connection)this.newInstance(var3);
         var4.setTransport(var1);
         return var4;
      }
   }

   private Object newInstance(Class var1) throws ConnectionException {
      try {
         return var1.newInstance();
      } catch (InstantiationException var3) {
         throw new ConnectionException("Failed to create an instance of '" + var1.getName() + "' due to: " + var3);
      } catch (IllegalAccessException var4) {
         throw new ConnectionException("Failed to create an instance of '" + var1.getName() + "' due to: " + var4);
      }
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeMap("clientConnectionTypes", this.clientConnectionTypes);
      var1.writeMap("clientTransportTypes", this.clientTransportTypes);
      var1.writeMap("serverConnectionTypes", this.serverConnectionTypes);
      var1.end();
   }
}
