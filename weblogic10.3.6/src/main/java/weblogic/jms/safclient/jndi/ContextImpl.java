package weblogic.jms.safclient.jndi;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import org.w3c.dom.Document;
import weblogic.jms.extensions.ClientSAF;
import weblogic.jms.safclient.ClientSAFDelegate;
import weblogic.jms.safclient.admin.ConfigurationUtils;
import weblogic.jms.safclient.agent.DestinationImpl;

public class ContextImpl implements Context {
   private ClientSAF provider = null;
   private HashMap contextMap = new HashMap();
   private HashMap jmsMap = new HashMap();

   public ContextImpl(ClientSAF var1, Document var2, ClientSAFDelegate var3) throws JMSException {
      this.provider = var1;
      ConfigurationUtils.doJNDIConnectionFactories(var2, var3, this.contextMap);
      ConfigurationUtils.doJNDIDestinations(var2, this.jmsMap, this.contextMap);
   }

   public DestinationImpl getDestination(String var1, String var2) {
      synchronized(this.jmsMap) {
         HashMap var4 = (HashMap)this.jmsMap.get(var1);
         return var4 == null ? null : (DestinationImpl)var4.get(var2);
      }
   }

   public DestinationImpl getDestination(String var1) {
      synchronized(this.jmsMap) {
         Iterator var3 = this.jmsMap.keySet().iterator();

         DestinationImpl var5;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            HashMap var4 = (HashMap)this.jmsMap.get(var3.next());
            var5 = (DestinationImpl)var4.get(var1);
         } while(var5 == null);

         return var5;
      }
   }

   public Map getDestinationMap() {
      return this.jmsMap;
   }

   public int howManyDestinationsWithThisName(String var1) {
      int var2 = 0;
      synchronized(this.jmsMap) {
         Iterator var4 = this.jmsMap.keySet().iterator();

         while(var4.hasNext()) {
            HashMap var5 = (HashMap)this.jmsMap.get(var4.next());
            if (var5.containsKey(var1)) {
               ++var2;
            }
         }

         return var2;
      }
   }

   public Object lookup(Name var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public Object lookup(String var1) throws NamingException {
      synchronized(this.contextMap) {
         if (!this.contextMap.containsKey(var1)) {
            throw new NamingException("No element with key \"" + var1 + "\" was bound into the JNDI context");
         } else {
            return this.contextMap.get(var1);
         }
      }
   }

   public void bind(Name var1, Object var2) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public void bind(String var1, Object var2) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public void rebind(Name var1, Object var2) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public void rebind(String var1, Object var2) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public void unbind(Name var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public void unbind(String var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public void rename(Name var1, Name var2) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public void rename(String var1, String var2) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public NamingEnumeration list(Name var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public NamingEnumeration list(String var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public NamingEnumeration listBindings(Name var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public NamingEnumeration listBindings(String var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public void destroySubcontext(Name var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public void destroySubcontext(String var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public Context createSubcontext(Name var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public Context createSubcontext(String var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public Object lookupLink(Name var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public Object lookupLink(String var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public NameParser getNameParser(Name var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public NameParser getNameParser(String var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public Name composeName(Name var1, Name var2) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public String composeName(String var1, String var2) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public Object addToEnvironment(String var1, Object var2) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public Object removeFromEnvironment(String var1) throws NamingException {
      throw new NamingException("Not implemented");
   }

   public Hashtable getEnvironment() throws NamingException {
      throw new NamingException("Not implemented");
   }

   public void close() throws NamingException {
      if (this.provider != null) {
         this.provider.close();
      }

   }

   public String getNameInNamespace() throws NamingException {
      throw new NamingException("Not implemented");
   }

   public void shutdown(JMSException var1) {
      synchronized(this.contextMap) {
         Iterator var3 = this.contextMap.keySet().iterator();

         while(var3.hasNext()) {
            Object var4 = this.contextMap.get(var3.next());
            if (var4 instanceof Shutdownable) {
               Shutdownable var5 = (Shutdownable)var4;
               var5.shutdown(var1);
            }
         }

         this.contextMap.clear();
      }

      synchronized(this.jmsMap) {
         this.jmsMap.clear();
      }
   }
}
