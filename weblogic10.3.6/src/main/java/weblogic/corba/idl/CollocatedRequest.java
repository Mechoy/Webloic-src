package weblogic.corba.idl;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.cert.X509Certificate;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import weblogic.iiop.EndPoint;
import weblogic.iiop.IIOPOutputStream;
import weblogic.iiop.InboundRequest;
import weblogic.iiop.OutboundResponse;
import weblogic.iiop.spi.Message;
import weblogic.protocol.ServerChannel;
import weblogic.rmi.extensions.server.InvokableServerReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.rmi.internal.ServerReference;
import weblogic.rmi.spi.InvokeHandler;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.MsgOutput;
import weblogic.security.service.ContextHandler;
import weblogic.security.subject.AbstractSubject;

public class CollocatedRequest implements InboundRequest, OutboundResponse, ResponseHandler, Message {
   private InvokableServerReference dispatcher;
   private String method;
   private Object txContext;
   private Object activationID;
   private IIOPOutputStream output;
   private InputStream input;
   private boolean responseExpected;
   private boolean exception = false;

   public CollocatedRequest(ServerReference var1, String var2, boolean var3, Object var4) {
      var1 = var1.getDelegate();
      if (var1 instanceof InvokeHandler && var1 instanceof InvokableServerReference) {
         this.dispatcher = (InvokableServerReference)var1;
         this.method = var2;
         this.responseExpected = var3;
         this.activationID = var4;
      } else {
         throw new IllegalArgumentException("ServerReference must be invokable: " + var1);
      }
   }

   public InputStream getInputStream() {
      if (this.input == null) {
         if (this.output == null) {
            throw new IllegalStateException("No OutputStream");
         }

         this.input = this.output.create_input_stream();
         this.output = null;
      }

      return this.input;
   }

   public OutputStream getOutputStream() {
      if (this.output == null) {
         this.output = new IIOPOutputStream(false, (EndPoint)null, this);
      }

      return this.output;
   }

   public ResponseHandler createResponseHandler(InboundRequest var1) {
      if (var1 != this) {
         throw new IllegalArgumentException("request not valid: " + var1);
      } else {
         return this;
      }
   }

   public OutputStream createExceptionReply() {
      this.closeInput();
      this.exception = true;
      return this.getOutputStream();
   }

   public OutputStream createReply() {
      this.closeInput();
      return this.getOutputStream();
   }

   private void closeInput() {
      if (this.input != null) {
         try {
            this.input.close();
         } catch (IOException var2) {
         }

         this.input = null;
      }

   }

   public String getMethod() {
      return this.method;
   }

   public boolean responseExpected() {
      return this.responseExpected;
   }

   public boolean isCollocated() {
      return true;
   }

   public void setTxContext(Object var1) throws RemoteException {
      this.txContext = var1;
   }

   public Object getTxContext() {
      return this.txContext;
   }

   public void invoke() throws Exception {
      Thread var1 = null;
      ClassLoader var2 = null;
      ClassLoader var3 = this.dispatcher.getApplicationClassLoader();
      if (var3 != null) {
         var1 = Thread.currentThread();
         var2 = var1.getContextClassLoader();
         var1.setContextClassLoader(var3);
      }

      try {
         ((InvokeHandler)this.dispatcher).invoke((RuntimeMethodDescriptor)null, this, this.responseExpected ? this : null);
         if (this.exception) {
            InputStream var4 = this.getInputStream();
            var4.mark(0);
            String var5 = var4.read_string();
            var4.reset();
            throw new ApplicationException(var5, var4);
         }
      } finally {
         if (var2 != null) {
            var1.setContextClassLoader(var2);
         }

      }

   }

   public void close() throws IOException {
      this.closeInput();
      if (this.output != null) {
         this.output.close();
      }

   }

   public Object getActivationID() throws IOException {
      return this.activationID;
   }

   public int getMinorVersion() {
      return 2;
   }

   public byte getMaxStreamFormatVersion() {
      return 2;
   }

   public void transferThreadLocalContext(weblogic.rmi.spi.InboundRequest var1) {
   }

   public void retrieveThreadLocalContext() throws IOException {
   }

   public void setContext(int var1, Object var2) throws IOException {
   }

   public Object getContext(int var1) throws IOException {
      return null;
   }

   public X509Certificate[] getCertificateChain() {
      return null;
   }

   public ContextHandler getContextHandler() {
      return null;
   }

   public Object getReplicaInfo() throws IOException {
      return null;
   }

   public void setReplicaInfo(Object var1) throws IOException {
   }

   public void sendThrowable(Throwable var1) {
      throw new UnsupportedOperationException("sendThrowable()");
   }

   public RuntimeMethodDescriptor getRuntimeMethodDescriptor(RuntimeDescriptor var1) {
      throw new UnsupportedOperationException("getRuntimeMethodDescriptor()");
   }

   public weblogic.rmi.spi.OutboundResponse getOutboundResponse() {
      throw new UnsupportedOperationException("getOutboundResponse()");
   }

   public weblogic.rmi.spi.EndPoint getEndPoint() {
      throw new UnsupportedOperationException("getEndPoint()");
   }

   public ServerChannel getServerChannel() {
      throw new UnsupportedOperationException("getServerChannel()");
   }

   public MsgInput getMsgInput() {
      throw new UnsupportedOperationException("getMsgInput()");
   }

   public MsgOutput getMsgOutput() {
      throw new UnsupportedOperationException("getMsgOutput()");
   }

   public AbstractSubject getSubject() {
      throw new UnsupportedOperationException("getSubject()");
   }

   public void send() throws RemoteException {
      throw new UnsupportedOperationException("send()");
   }
}
