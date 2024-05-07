package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import weblogic.security.utils.SSLIOContext;

final class JaSSLEngineRunner {
   private static final int APP_IN_BUFFER_MARGIN_BYTES = 50;
   private static final int NET_BUFFER_MARGIN_BYTES = 50;
   private static final SSLEngineStateTransition TRANSITION_NEED_TASK = new Transition_NeedTask();
   private static final SSLEngineStateTransition TRANSITION_NEED_UNWRAP = new Transition_NeedUnwrap();
   private static final SSLEngineStateTransition TRANSITION_NEED_WRAP = new Transition_NeedWrap();
   private static final SSLEngineStateTransition TRANSITION_HANDSHAKE_FINISHED = new Transition_HandshakeFinished();
   private static final SSLEngineStateTransition TRANSITION_BUFFER_OVERFLOW_WRAP = new Transition_BufferOverflow_Wrap();
   private static final SSLEngineStateTransition TRANSITION_BUFFER_OVERFLOW_UNWRAP = new Transition_BufferOverflow_Unwrap();
   private static final SSLEngineStateTransition TRANSITION_BUFFER_UNDERFLOW_UNWRAP = new Transition_BufferUnderflow_Unwrap();
   private static final Map<SSLEngineState, SSLEngineStateTransition> sslEngineTransitions;

   private JaSSLEngineRunner() {
   }

   private static boolean isTransitioning(Method var0, Context var1, TransitionResult var2) {
      if (null != var0 && null != var1 && null != var2) {
         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "isTransitioning: method={0}, transitionResult={1}.", var0, var2);
         }

         RunnerResult var3 = var2.getRunnerResult();
         switch (var3) {
            case INCOMPLETE_NETWORK_READ:
            case INCOMPLETE_NETWORK_WRITE:
            case NEED_APPLICATION_READ:
               if (JaLogger.isLoggable(Level.FINEST)) {
                  JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "Need data transfer: method={0}, transitionResult={1}.", var0, var2);
               }

               return false;
            case OK:
               SSLEngineResult var4 = var2.getSslEngineResult();
               SSLEngineResult.Status var5 = var4.getStatus();
               SSLEngineResult.HandshakeStatus var6 = var4.getHandshakeStatus();
               SSLEngine var7 = var1.getSslEngine();
               var1.ensureHandshakeLockIfNeeded(var6);
               switch (var5) {
                  case BUFFER_UNDERFLOW:
                  case BUFFER_OVERFLOW:
                     return true;
                  case OK:
                  case CLOSED:
                     switch (var6) {
                        case NEED_UNWRAP:
                           if (var7.isInboundDone()) {
                              if (JaLogger.isLoggable(Level.FINER)) {
                                 JaLogger.log(Level.FINER, JaLogger.Component.SSLENGINE, "Need unwrap but inbound closed: method={0}, transitionResult={1}.", var0, var2);
                              }

                              return false;
                           }

                           return true;
                        case NEED_WRAP:
                           if (var7.isOutboundDone()) {
                              if (JaLogger.isLoggable(Level.FINER)) {
                                 JaLogger.log(Level.FINER, JaLogger.Component.SSLENGINE, "Need wrap but outbound closed: method={0}, transitionResult={1}.", var0, var2);
                              }

                              return false;
                           }

                           return true;
                        case NEED_TASK:
                        case FINISHED:
                           return true;
                        case NOT_HANDSHAKING:
                           if (Status.CLOSED == var5) {
                              if (JaLogger.isLoggable(Level.FINER)) {
                                 JaLogger.log(Level.FINER, JaLogger.Component.SSLENGINE, "Not handshaking but status was CLOSED: method={0}, transitionResult={1}.", var0, var2);
                              }

                              return false;
                           } else {
                              if (Status.OK == var5 && HandshakeStatus.NOT_HANDSHAKING == var6) {
                                 return false;
                              }

                              throw new IllegalStateException("Unexpected status: " + var4);
                           }
                        default:
                           throw new IllegalStateException("Unexpected handshake status: " + var6);
                     }
                  default:
                     throw new IllegalStateException("Unexpected status: " + var5);
               }
            default:
               throw new IllegalStateException("Unexpected RunnerResult: " + var3);
         }
      } else {
         throw new IllegalArgumentException("Expected non-null arguments.");
      }
   }

   private static RunnerResult doTransitions(Context var0, Method var1) throws IOException {
      if (null == var0) {
         throw new IllegalArgumentException("Expected non-null Context.");
      } else if (null == var1) {
         throw new IllegalArgumentException("Expected non-null Method.");
      } else {
         SSLEngine var3 = var0.getSslEngine();
         SSLEngineStateTransition var2;
         switch (var1) {
            case UNWRAP:
               if (var3.isInboundDone()) {
                  return JaSSLEngineRunner.RunnerResult.CLOSED;
               }

               var2 = TRANSITION_NEED_UNWRAP;
               break;
            case WRAP:
               if (var3.isOutboundDone()) {
                  return JaSSLEngineRunner.RunnerResult.CLOSED;
               }

               var2 = TRANSITION_NEED_WRAP;
               break;
            default:
               throw new IllegalArgumentException("Unexpected Method=" + var1 + ".");
         }

         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "Starting doTransitions: method={0}, starting transition={1}.", var1, var2.getClass().getName());
         }

         SSLEngineResult var5 = null;

         TransitionResult var4;
         try {
            while(isTransitioning(var1, var0, var4 = var2.getNextState(var1, var0, var5))) {
               var5 = var4.getSslEngineResult();
               SSLEngineState var6 = new SSLEngineState(var1, var5);
               var2 = (SSLEngineStateTransition)sslEngineTransitions.get(var6);
               if (null == var2) {
                  throw new IllegalStateException("Unexpected null transition for state: " + var6);
               }

               if (JaLogger.isLoggable(Level.FINEST)) {
                  JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "Next transition: method={0}, last result={1}, next transition={2}.", var1, var5, var2.getClass().getName());
               }
            }
         } finally {
            var0.ensureHandshakeUnlock();
         }

         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "Ending doTransitions: method={0}, result={1}.", var1, var4);
         }

         var5 = var4.getSslEngineResult();
         return null != var5 && var5.getStatus() == Status.CLOSED ? JaSSLEngineRunner.RunnerResult.CLOSED : var4.getRunnerResult();
      }
   }

   private static void log_wrapException(Exception var0) {
      if (JaLogger.isLoggable(Level.FINE)) {
         JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, var0, "Unable to complete JaSSLEngineRunner.wrap.");
      }

   }

   private static void log_unwrapException(Exception var0) {
      if (JaLogger.isLoggable(Level.FINE)) {
         JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, var0, "Unable to complete JaSSLEngineRunner.unwrap.");
      }

   }

   private static void log_closeInboundException(Exception var0, boolean var1) {
      if (JaLogger.isLoggable(Level.FINE)) {
         JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, var0, "Unable to complete JaSSLEngineRunner.closeInbound, expectCloseNotify={0}.", var1);
      }

   }

   private static void log_closeOutboundException(Exception var0) {
      if (JaLogger.isLoggable(Level.FINE)) {
         JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, var0, "Unable to complete JaSSLEngineRunner.closeOutbound.");
      }

   }

   static RunnerResult unwrap(Context var0) throws IOException {
      try {
         if (null == var0) {
            throw new IllegalArgumentException("Expected non-null Context.");
         } else {
            SSLEngine var1 = var0.getSslEngine();
            RunnerResult var2;
            if (null != var1 && !var1.isInboundDone()) {
               var0.getSync().lock(JaSSLEngineSynchronizer.LockState.INBOUND);

               try {
                  var2 = doTransitions(var0, JaSSLEngineRunner.Method.UNWRAP);
               } finally {
                  var0.getSync().unlock();
               }
            } else {
               var2 = JaSSLEngineRunner.RunnerResult.CLOSED;
            }

            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "JaSSLEngineRunner.unwrap result={0}.", var2);
            }

            return var2;
         }
      } catch (InterruptedIOException var10) {
         throw var10;
      } catch (SSLException var11) {
         log_unwrapException(var11);
         throw var11;
      } catch (IOException var12) {
         log_unwrapException(var12);
         throw var12;
      } catch (RuntimeException var13) {
         log_unwrapException(var13);
         throw var13;
      }
   }

   static RunnerResult wrap(Context var0) throws IOException {
      try {
         if (null == var0) {
            throw new IllegalArgumentException("Expected non-null Context.");
         } else {
            SSLEngine var1 = var0.getSslEngine();
            RunnerResult var2;
            if (null != var1 && !var1.isOutboundDone()) {
               var0.getSync().lock(JaSSLEngineSynchronizer.LockState.OUTBOUND);

               try {
                  var2 = doTransitions(var0, JaSSLEngineRunner.Method.WRAP);
               } finally {
                  var0.getSync().unlock();
               }
            } else {
               var2 = JaSSLEngineRunner.RunnerResult.CLOSED;
            }

            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "JaSSLEngineRunner.wrap result={0}.", var2);
            }

            return var2;
         }
      } catch (InterruptedIOException var14) {
         throw var14;
      } catch (SSLException var15) {
         log_wrapException(var15);

         try {
            closeOutbound(var0);
         } catch (Exception var11) {
         }

         throw var15;
      } catch (IOException var16) {
         log_wrapException(var16);

         try {
            closeOutbound(var0);
         } catch (Exception var12) {
         }

         throw var16;
      } catch (RuntimeException var17) {
         log_wrapException(var17);
         throw var17;
      }
   }

   static RunnerResult closeOutbound(Context var0) throws IOException {
      try {
         if (null == var0) {
            throw new IllegalArgumentException("Expected non-null Context.");
         } else {
            SSLEngine var1 = var0.getSslEngine();
            RunnerResult var2;
            if (null != var1 && !var1.isOutboundDone()) {
               var0.getSync().lock(JaSSLEngineSynchronizer.LockState.OUTBOUND);

               try {
                  var1.closeOutbound();
                  var2 = doTransitions(var0, JaSSLEngineRunner.Method.WRAP);
               } finally {
                  var0.getSync().unlock();
               }
            } else {
               var2 = JaSSLEngineRunner.RunnerResult.CLOSED;
            }

            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "JaSSLEngineRunner.closeOutbound result={0}.", var2);
            }

            return var2;
         }
      } catch (InterruptedIOException var9) {
         throw var9;
      } catch (IOException var10) {
         log_closeOutboundException(var10);
         throw var10;
      } catch (RuntimeException var11) {
         log_closeOutboundException(var11);
         throw var11;
      }
   }

   static boolean isOutboundDone(Context var0) {
      try {
         if (null == var0) {
            throw new IllegalArgumentException("Expected non-null Context.");
         } else {
            SSLEngine var1 = var0.getSslEngine();
            boolean var2 = null == var1 || var1.isOutboundDone();
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "JaSSLEngineRunner.isOutboundDone result={0}, engine={1}", var2, var1);
            }

            return var2;
         }
      } catch (RuntimeException var3) {
         if (JaLogger.isLoggable(Level.FINE)) {
            JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, var3, "Unable to complete JaSSLEngineRunner.isOutboundDone.");
         }

         throw var3;
      }
   }

   static RunnerResult closeInbound(Context var0, boolean var1) throws IOException {
      try {
         if (null == var0) {
            throw new IllegalArgumentException("Expected non-null Context.");
         } else {
            SSLEngine var2 = var0.getSslEngine();
            RunnerResult var3;
            if (null != var2 && !var2.isInboundDone()) {
               var0.getSync().lock(JaSSLEngineSynchronizer.LockState.INBOUND);

               try {
                  var3 = doTransitions(var0, JaSSLEngineRunner.Method.UNWRAP);
                  if (var1 && JaSSLEngineRunner.RunnerResult.CLOSED != var3) {
                     var2.closeInbound();
                  }
               } finally {
                  var0.getSync().unlock();
               }
            } else {
               var3 = JaSSLEngineRunner.RunnerResult.CLOSED;
            }

            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "JaSSLEngineRunner.closeInbound result={0}, expectCloseNotify={1}.", var3, var1);
            }

            return var3;
         }
      } catch (InterruptedIOException var10) {
         throw var10;
      } catch (IOException var11) {
         log_closeInboundException(var11, var1);
         throw var11;
      } catch (RuntimeException var12) {
         log_closeInboundException(var12, var1);
         throw var12;
      }
   }

   static boolean isInboundDone(Context var0) {
      try {
         if (null == var0) {
            throw new IllegalArgumentException("Expected non-null Context.");
         } else {
            SSLEngine var1 = var0.getSslEngine();
            boolean var2 = null == var1 || var1.isInboundDone();
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "JaSSLEngineRunner.isInboundDone result={0}, engine={1}", var2, var1);
            }

            return var2;
         }
      } catch (RuntimeException var3) {
         if (JaLogger.isLoggable(Level.FINE)) {
            JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, var3, "Unable to complete JaSSLEngineRunner.isInboundDone.");
         }

         throw var3;
      }
   }

   static void close(Context var0, boolean var1) throws IOException {
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "Attempting to execute JaSSLEngineRunner.close, expectCloseNotify={0}.", var1);
      }

      if (null == var0) {
         if (JaLogger.isLoggable(Level.FINER)) {
            JaLogger.log(Level.FINER, JaLogger.Component.SSLENGINE, "JaSSLEngineRunner.Context is null, no SSL context to close.");
         }

      } else {
         RunnerResult var2 = closeOutbound(var0);
         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "During JaSSLEngineRunner.close, closeOutbound result={0}", var2);
         }

         var2 = closeInbound(var0, var1);
         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "During JaSSLEngineRunner.close, closeInbound result={0}", var2);
         }

      }
   }

   static {
      HashMap var0 = new HashMap(64);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.OK, HandshakeStatus.NOT_HANDSHAKING), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.OK, HandshakeStatus.NEED_TASK), TRANSITION_NEED_TASK);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.OK, HandshakeStatus.NEED_UNWRAP), TRANSITION_NEED_UNWRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.OK, HandshakeStatus.NEED_WRAP), TRANSITION_NEED_WRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.OK, HandshakeStatus.FINISHED), TRANSITION_HANDSHAKE_FINISHED);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.BUFFER_OVERFLOW, HandshakeStatus.NOT_HANDSHAKING), TRANSITION_BUFFER_OVERFLOW_WRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.BUFFER_OVERFLOW, HandshakeStatus.NEED_TASK), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.BUFFER_OVERFLOW, HandshakeStatus.NEED_UNWRAP), TRANSITION_BUFFER_OVERFLOW_UNWRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.BUFFER_OVERFLOW, HandshakeStatus.NEED_WRAP), TRANSITION_BUFFER_OVERFLOW_WRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.BUFFER_OVERFLOW, HandshakeStatus.FINISHED), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.BUFFER_UNDERFLOW, HandshakeStatus.NOT_HANDSHAKING), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.BUFFER_UNDERFLOW, HandshakeStatus.NEED_TASK), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.BUFFER_UNDERFLOW, HandshakeStatus.NEED_UNWRAP), TRANSITION_BUFFER_UNDERFLOW_UNWRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.BUFFER_UNDERFLOW, HandshakeStatus.NEED_WRAP), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.BUFFER_UNDERFLOW, HandshakeStatus.FINISHED), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.CLOSED, HandshakeStatus.NOT_HANDSHAKING), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.CLOSED, HandshakeStatus.NEED_TASK), TRANSITION_NEED_TASK);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.CLOSED, HandshakeStatus.NEED_UNWRAP), TRANSITION_NEED_UNWRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.CLOSED, HandshakeStatus.NEED_WRAP), TRANSITION_NEED_WRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.WRAP, Status.CLOSED, HandshakeStatus.FINISHED), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.OK, HandshakeStatus.NOT_HANDSHAKING), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.OK, HandshakeStatus.NEED_TASK), TRANSITION_NEED_TASK);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.OK, HandshakeStatus.NEED_UNWRAP), TRANSITION_NEED_UNWRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.OK, HandshakeStatus.NEED_WRAP), TRANSITION_NEED_WRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.OK, HandshakeStatus.FINISHED), TRANSITION_HANDSHAKE_FINISHED);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.BUFFER_OVERFLOW, HandshakeStatus.NOT_HANDSHAKING), TRANSITION_BUFFER_OVERFLOW_UNWRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.BUFFER_OVERFLOW, HandshakeStatus.NEED_TASK), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.BUFFER_OVERFLOW, HandshakeStatus.NEED_UNWRAP), TRANSITION_BUFFER_OVERFLOW_UNWRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.BUFFER_OVERFLOW, HandshakeStatus.NEED_WRAP), TRANSITION_BUFFER_OVERFLOW_WRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.BUFFER_OVERFLOW, HandshakeStatus.FINISHED), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.BUFFER_UNDERFLOW, HandshakeStatus.NOT_HANDSHAKING), TRANSITION_BUFFER_UNDERFLOW_UNWRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.BUFFER_UNDERFLOW, HandshakeStatus.NEED_TASK), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.BUFFER_UNDERFLOW, HandshakeStatus.NEED_UNWRAP), TRANSITION_BUFFER_UNDERFLOW_UNWRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.BUFFER_UNDERFLOW, HandshakeStatus.NEED_WRAP), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.BUFFER_UNDERFLOW, HandshakeStatus.FINISHED), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.CLOSED, HandshakeStatus.NOT_HANDSHAKING), (Object)null);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.CLOSED, HandshakeStatus.NEED_TASK), TRANSITION_NEED_TASK);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.CLOSED, HandshakeStatus.NEED_UNWRAP), TRANSITION_NEED_UNWRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.CLOSED, HandshakeStatus.NEED_WRAP), TRANSITION_NEED_WRAP);
      var0.put(new SSLEngineState(JaSSLEngineRunner.Method.UNWRAP, Status.CLOSED, HandshakeStatus.FINISHED), (Object)null);
      sslEngineTransitions = Collections.unmodifiableMap(var0);
   }

   private static class Transition_BufferUnderflow_Unwrap implements SSLEngineStateTransition {
      private Transition_BufferUnderflow_Unwrap() {
      }

      public TransitionResult getNextState(Method var1, Context var2, SSLEngineResult var3) throws IOException {
         SSLSession var4 = var2.getSslEngine().getSession();
         int var5 = var4.getPacketBufferSize() + 50;
         if (var5 > var2.getBufferNetIn().capacity()) {
            ByteBuffer var6 = ByteBuffer.allocate(var5);
            var2.getBufferNetIn().flip();
            var6.put(var2.getBufferNetIn());
            var2.setBufferNetIn(var6);
         }

         return 0 == var2.fillBufferNetIn() ? new TransitionResult(JaSSLEngineRunner.RunnerResult.INCOMPLETE_NETWORK_READ, var3) : JaSSLEngineRunner.TRANSITION_NEED_UNWRAP.getNextState(var1, var2, var3);
      }

      // $FF: synthetic method
      Transition_BufferUnderflow_Unwrap(Object var1) {
         this();
      }
   }

   private static class Transition_BufferOverflow_Unwrap implements SSLEngineStateTransition {
      private Transition_BufferOverflow_Unwrap() {
      }

      public TransitionResult getNextState(Method var1, Context var2, SSLEngineResult var3) throws IOException {
         SSLSession var4 = var2.getSslEngine().getSession();
         int var5 = var4.getApplicationBufferSize() + 50;
         if (var5 > var2.getBufferAppIn().capacity()) {
            ByteBuffer var6 = ByteBuffer.allocate(var5);
            var2.getBufferAppIn().flip();
            var6.put(var2.getBufferAppIn());
            var2.setBufferAppIn(var6);
            TransitionResult var7 = JaSSLEngineRunner.TRANSITION_NEED_UNWRAP.getNextState(var1, var2, var3);
            return var7;
         } else {
            return new TransitionResult(JaSSLEngineRunner.RunnerResult.NEED_APPLICATION_READ, var3);
         }
      }

      // $FF: synthetic method
      Transition_BufferOverflow_Unwrap(Object var1) {
         this();
      }
   }

   private static class Transition_BufferOverflow_Wrap implements SSLEngineStateTransition {
      private Transition_BufferOverflow_Wrap() {
      }

      public TransitionResult getNextState(Method var1, Context var2, SSLEngineResult var3) throws IOException {
         SSLSession var4 = var2.getSslEngine().getSession();
         int var5 = var4.getPacketBufferSize() + 50;
         TransitionResult var7;
         if (var5 > var2.getBufferNetOut().capacity()) {
            ByteBuffer var6 = ByteBuffer.allocate(var5);
            var2.getBufferNetOut().flip();
            var6.put(var2.getBufferNetOut());
            var2.setBufferNetOut(var6);
         } else if (!var2.flushBufferNetOut()) {
            var7 = new TransitionResult(JaSSLEngineRunner.RunnerResult.INCOMPLETE_NETWORK_WRITE, var3);
            return var7;
         }

         var7 = JaSSLEngineRunner.TRANSITION_NEED_WRAP.getNextState(var1, var2, var3);
         return var7;
      }

      // $FF: synthetic method
      Transition_BufferOverflow_Wrap(Object var1) {
         this();
      }
   }

   private static class Transition_NeedTask implements SSLEngineStateTransition {
      private Transition_NeedTask() {
      }

      public TransitionResult getNextState(Method var1, Context var2, SSLEngineResult var3) throws IOException {
         Runnable var4;
         for(; (var4 = var2.getSslEngine().getDelegatedTask()) != null; var4.run()) {
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "Transition_NeedTask: Running delegated task: method={0}, last result={1}, task={2}.", var1, var3, var4.getClass().getName());
            }
         }

         SSLEngineResult.HandshakeStatus var5 = var2.getSslEngine().getHandshakeStatus();
         return new TransitionResult(JaSSLEngineRunner.RunnerResult.OK, new SSLEngineResult(Status.OK, var5, 0, 0));
      }

      // $FF: synthetic method
      Transition_NeedTask(Object var1) {
         this();
      }
   }

   private static class Transition_HandshakeFinished implements SSLEngineStateTransition {
      private Transition_HandshakeFinished() {
      }

      public TransitionResult getNextState(Method var1, Context var2, SSLEngineResult var3) throws IOException {
         var2.getSync().unlock();
         TransitionResult var4;
         switch (var1) {
            case UNWRAP:
               var4 = JaSSLEngineRunner.TRANSITION_NEED_UNWRAP.getNextState(var1, var2, var3);
               break;
            case WRAP:
               var4 = JaSSLEngineRunner.TRANSITION_NEED_WRAP.getNextState(var1, var2, var3);
               break;
            default:
               throw new IllegalStateException("Unsupported Method " + var1);
         }

         return var4;
      }

      // $FF: synthetic method
      Transition_HandshakeFinished(Object var1) {
         this();
      }
   }

   private static class Transition_NeedWrap implements SSLEngineStateTransition {
      private Transition_NeedWrap() {
      }

      public TransitionResult getNextState(Method var1, Context var2, SSLEngineResult var3) throws IOException {
         TransitionResult var4;
         try {
            var2.getBufferAppOut().flip();
            var4 = new TransitionResult(JaSSLEngineRunner.RunnerResult.OK, var2.getSslEngine().wrap(var2.getBufferAppOut(), var2.getBufferNetOut()));
         } finally {
            var2.getBufferAppOut().compact();
         }

         SSLEngineResult var5 = var4.getSslEngineResult();
         if (var5.bytesProduced() > 0 && !var2.flushBufferNetOut()) {
            var4 = new TransitionResult(JaSSLEngineRunner.RunnerResult.INCOMPLETE_NETWORK_WRITE, var4.getSslEngineResult());
            return var4;
         } else {
            return var4;
         }
      }

      // $FF: synthetic method
      Transition_NeedWrap(Object var1) {
         this();
      }
   }

   private static class Transition_NeedUnwrap implements SSLEngineStateTransition {
      private Transition_NeedUnwrap() {
      }

      public TransitionResult getNextState(Method var1, Context var2, SSLEngineResult var3) throws IOException {
         int var4;
         try {
            var2.getBufferNetIn().flip();
            var4 = var2.getBufferNetIn().remaining();
         } finally {
            var2.getBufferNetIn().compact();
         }

         if (var4 <= 0 && 0 == var2.fillBufferNetIn()) {
            return new TransitionResult(JaSSLEngineRunner.RunnerResult.INCOMPLETE_NETWORK_READ, var3);
         } else {
            TransitionResult var5;
            try {
               var2.getBufferNetIn().flip();
               var5 = new TransitionResult(JaSSLEngineRunner.RunnerResult.OK, var2.getSslEngine().unwrap(var2.getBufferNetIn(), var2.getBufferAppIn()));
            } finally {
               var2.getBufferNetIn().compact();
            }

            return var5;
         }
      }

      // $FF: synthetic method
      Transition_NeedUnwrap(Object var1) {
         this();
      }
   }

   private interface SSLEngineStateTransition {
      TransitionResult getNextState(Method var1, Context var2, SSLEngineResult var3) throws IOException;
   }

   static class SSLEngineState {
      private final Method method;
      private final SSLEngineResult.Status status;
      private final SSLEngineResult.HandshakeStatus handshakeStatus;

      private SSLEngineState(Method var1, SSLEngineResult var2) {
         this(var1, var2.getStatus(), var2.getHandshakeStatus());
      }

      private SSLEngineState(Method var1, SSLEngineResult.Status var2, SSLEngineResult.HandshakeStatus var3) {
         if (null != var1 && null != var2 && null != var3) {
            this.method = var1;
            this.status = var2;
            this.handshakeStatus = var3;
         } else {
            throw new IllegalArgumentException("Expected non-null arguments.");
         }
      }

      Method getMethod() {
         return this.method;
      }

      SSLEngineResult.Status getStatus() {
         return this.status;
      }

      SSLEngineResult.HandshakeStatus getHandshakeStatus() {
         return this.handshakeStatus;
      }

      public int hashCode() {
         return this.handshakeStatus.hashCode();
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof SSLEngineState)) {
            return false;
         } else {
            SSLEngineState var2 = (SSLEngineState)var1;
            return var2.method == this.method && var2.status == this.status && var2.handshakeStatus == this.handshakeStatus;
         }
      }

      public String toString() {
         return MessageFormat.format("SSLEngine State: Method={0}, Status={1}, HandshakeStatus={2}", this.method, this.status, this.handshakeStatus);
      }

      // $FF: synthetic method
      SSLEngineState(Method var1, SSLEngineResult var2, Object var3) {
         this(var1, var2);
      }

      // $FF: synthetic method
      SSLEngineState(Method var1, SSLEngineResult.Status var2, SSLEngineResult.HandshakeStatus var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   static final class TransitionResult {
      private final RunnerResult runnerResult;
      private final SSLEngineResult sslEngineResult;

      private TransitionResult(RunnerResult var1, SSLEngineResult var2) {
         if (null == var1) {
            throw new IllegalArgumentException("Expected non-null RunnerResult.");
         } else {
            this.runnerResult = var1;
            this.sslEngineResult = var2;
         }
      }

      RunnerResult getRunnerResult() {
         return this.runnerResult;
      }

      SSLEngineResult getSslEngineResult() {
         return this.sslEngineResult;
      }

      public String toString() {
         return MessageFormat.format("TransitionResult: RunnerResult={0}, SSLEngineResult={1}", this.runnerResult, this.sslEngineResult);
      }

      // $FF: synthetic method
      TransitionResult(RunnerResult var1, SSLEngineResult var2, Object var3) {
         this(var1, var2);
      }
   }

   static final class Context {
      private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);
      private final JaSSLEngineSynchronizer sync = new JaSSLEngineSynchronizer();
      private SSLEngine sslEngine;
      private ByteBuffer bufferNetIn;
      private ByteBuffer bufferAppIn;
      private ByteBuffer bufferAppOut;
      private ByteBuffer bufferNetOut;
      private ReadableByteChannel networkReadableByteChannel;
      private WritableByteChannel networkWritableByteChannel;
      private SSLIOContext sslIoContext;

      Context() {
         this.bufferNetIn = EMPTY_BUFFER;
         this.bufferAppIn = EMPTY_BUFFER;
         this.bufferAppOut = EMPTY_BUFFER;
         this.bufferNetOut = EMPTY_BUFFER;
      }

      SSLEngine getSslEngine() {
         return this.sslEngine;
      }

      private void setSslEngine(SSLEngine var1) {
         this.sslEngine = var1;
      }

      private ByteBuffer getBufferNetIn() {
         return this.bufferNetIn;
      }

      private void setBufferNetIn(ByteBuffer var1) {
         this.bufferNetIn = var1;
      }

      ByteBuffer getBufferAppIn() {
         return this.bufferAppIn;
      }

      void setBufferAppIn(ByteBuffer var1) {
         this.bufferAppIn = var1;
      }

      ByteBuffer getBufferAppOut() {
         return this.bufferAppOut;
      }

      void setBufferAppOut(ByteBuffer var1) {
         if (null == var1) {
            var1 = EMPTY_BUFFER;
         }

         this.bufferAppOut = var1;
      }

      private ByteBuffer getBufferNetOut() {
         return this.bufferNetOut;
      }

      private void setBufferNetOut(ByteBuffer var1) {
         this.bufferNetOut = var1;
      }

      private ReadableByteChannel getNetworkReadableByteChannel() {
         return this.networkReadableByteChannel;
      }

      private void setNetworkReadableByteChannel(ReadableByteChannel var1) {
         this.networkReadableByteChannel = var1;
      }

      private WritableByteChannel getNetworkWritableByteChannel() {
         return this.networkWritableByteChannel;
      }

      private void setNetworkWritableByteChannel(WritableByteChannel var1) {
         this.networkWritableByteChannel = var1;
      }

      JaSSLEngineSynchronizer getSync() {
         return this.sync;
      }

      SSLIOContext getSslIoContext() {
         return this.sslIoContext;
      }

      private void setSslIoContext(SSLIOContext var1) {
         this.sslIoContext = var1;
      }

      boolean notCompleteSSLRecord() {
         return this.sslIoContext != null && this.sslIoContext.isMuxerActivated() && !this.sslIoContext.hasSSLRecord();
      }

      InputStream getMuxerInputStream() {
         return this.sslIoContext == null ? null : this.sslIoContext.getMuxerIS();
      }

      boolean isMuxerActivated() {
         return this.sslIoContext != null && this.sslIoContext.isMuxerActivated();
      }

      void init(SSLEngine var1, ReadableByteChannel var2, WritableByteChannel var3, SSLIOContext var4) throws IOException {
         if (null == var1) {
            throw new IllegalArgumentException("Expected non-null SSLEngine.");
         } else if (null == var2) {
            throw new IllegalArgumentException("Expected non-null networkReadableByteChannel.");
         } else if (null == var3) {
            throw new IllegalArgumentException("Expected non-null networkWritableByteChannel.");
         } else {
            this.setSslEngine(var1);
            SSLSession var5 = var1.getSession();
            int var6 = var5.getApplicationBufferSize() + 50;
            int var7 = var5.getPacketBufferSize() + 50;
            this.setBufferAppIn(ByteBuffer.allocate(var6));
            this.setBufferAppOut(EMPTY_BUFFER);
            this.setBufferNetIn(ByteBuffer.allocate(var7));
            this.setBufferNetOut(ByteBuffer.allocate(var7));
            this.setNetworkReadableByteChannel(var2);
            this.setNetworkWritableByteChannel(var3);
            this.setSslIoContext(var4);
         }
      }

      boolean flushBufferNetOut() throws IOException {
         ByteBuffer var1 = this.getBufferNetOut();

         boolean var5;
         try {
            var1.flip();
            if (var1.remaining() <= 0) {
               boolean var9 = true;
               return var9;
            }

            WritableByteChannel var2 = this.getNetworkWritableByteChannel();
            int var3 = var2.write(var1);
            int var4 = var1.remaining();
            if (var4 <= 0) {
               if (JaLogger.isLoggable(Level.FINEST)) {
                  JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "Completely flushed outbound network buffer. Flushed bytes={0}", var3);
               }

               var5 = true;
               return var5;
            }

            if (JaLogger.isLoggable(Level.FINER)) {
               JaLogger.log(Level.FINER, JaLogger.Component.SSLENGINE, "Unable to completely flush outbound network buffer. Remaining bytes={0}, Flushed bytes={1}", var4, var3);
            }

            var5 = false;
         } finally {
            var1.compact();
         }

         return var5;
      }

      int fillBufferNetIn() throws IOException {
         ByteBuffer var1 = this.getBufferNetIn();
         if (var1.remaining() <= 0) {
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "No data read, bufferNetIn is full.");
            }

            return 0;
         } else {
            int var2;
            label408: {
               try {
                  var1.flip();
                  if (var1.remaining() <= 0) {
                     break label408;
                  }

                  if (JaLogger.isLoggable(Level.FINER)) {
                     JaLogger.log(Level.FINER, JaLogger.Component.SSLENGINE, "Attempted to fill bufferNetIn before reading entire previous SSL record.");
                  }

                  var2 = 0;
               } finally {
                  var1.compact();
               }

               return var2;
            }

            if (this.notCompleteSSLRecord()) {
               throw new InterruptedIOException();
            } else {
               int var4;
               do {
                  if (0 == (var2 = this.getBytesPending())) {
                     int var24;
                     try {
                        var1.flip();
                        var24 = var1.remaining();
                     } finally {
                        var1.compact();
                     }

                     return var24;
                  }

                  if (var2 > var1.remaining()) {
                     String var23 = MessageFormat.format("Unexpectedly large SSL Record size, bytesPending={0}, bufferNetIn.remaining={1}.", var2, var1.remaining());
                     throw new IllegalStateException(var23);
                  }

                  InputStream var3 = this.getMuxerInputStream();
                  if (var3 == null) {
                     int var25 = var1.limit();

                     try {
                        var1.limit(var1.position() + var2);
                        ReadableByteChannel var27 = this.getNetworkReadableByteChannel();
                        var4 = var27.read(var1);
                     } finally {
                        var1.limit(var25);
                     }
                  } else {
                     byte[] var5 = new byte[var2];
                     int var6 = 0;
                     int var7 = var5.length;

                     while((var4 = var3.read(var5, var6, var7)) < var7) {
                        if (0 == var4) {
                           String var28 = MessageFormat.format("Unable to read complete SSL record from muxer input stream, no bytes available, remaining={0} bytes.", var7);
                           if (JaLogger.isLoggable(Level.FINE)) {
                              JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, var28);
                           }

                           throw new IllegalStateException(var28);
                        }

                        if (-1 == var4) {
                           SSLEngine var8 = this.getSslEngine();
                           if (null != var8) {
                              var8.closeInbound();
                           }

                           throw new ClosedChannelException();
                        }

                        var7 -= var4;
                        var6 += var4;
                        Thread.currentThread();
                        Thread.yield();
                     }

                     var1.put(var5);
                  }

                  if (-1 == var4) {
                     SSLEngine var26 = this.getSslEngine();
                     if (null != var26) {
                        var26.closeInbound();
                     }

                     throw new ClosedChannelException();
                  }
               } while(0 != var4);

               if (this.isMuxerActivated()) {
                  throw new IllegalStateException("Muxer is activated, but available bytes are not readable.");
               } else {
                  return 0;
               }
            }
         }
      }

      private int getBytesPending() throws IOException {
         ByteBuffer var1 = this.getBufferNetIn();
         int var2 = var1.position();
         boolean var3 = false;
         if (var2 == 0) {
            return 1;
         } else {
            int var6;
            String var7;
            if ((var1.get(0) & 128) == 128) {
               if (var2 < 2) {
                  return 2 - var2;
               }

               int var4 = readUInt8(var1.get(0));
               int var5 = readUInt8(var1.get(1));
               var6 = (var4 & 127) << 8 | var5 + 2;
            } else {
               if (var2 < 5) {
                  return 5 - var2;
               }

               var6 = readUInt16(var1.get(3), var1.get(4)) + 5;
               if (var6 < 0) {
                  var7 = MessageFormat.format("Illegal negative SSL record length field, value={0}.", var6);
                  throw new IOException(var7);
               }
            }

            if (var6 < var2) {
               var7 = MessageFormat.format("Buffer filled beyond SSL record length. SSLRecord length={0}, byte previously read={1}.", var6, var2);
               throw new IllegalStateException(var7);
            } else {
               return var6 - var2;
            }
         }
      }

      private static int readUInt8(byte var0) {
         return var0 & 255;
      }

      private static int readUInt16(byte var0, byte var1) {
         int var2 = readUInt8(var0);
         int var3 = readUInt8(var1);
         return (var2 << 8) + var3;
      }

      private void ensureHandshakeLockIfNeeded(SSLEngineResult.HandshakeStatus var1) {
         if (null == var1) {
            throw new IllegalArgumentException("Non-null HandshakeStatus expected.");
         } else if (this.getSync().getLockState() != JaSSLEngineSynchronizer.LockState.HANDSHAKE) {
            if (var1 == HandshakeStatus.NEED_WRAP && this.getSync().getLockState() == JaSSLEngineSynchronizer.LockState.INBOUND) {
               this.getSync().lock(JaSSLEngineSynchronizer.LockState.HANDSHAKE);
            } else if (var1 == HandshakeStatus.NEED_UNWRAP && this.getSync().getLockState() == JaSSLEngineSynchronizer.LockState.OUTBOUND) {
               this.getSync().lock(JaSSLEngineSynchronizer.LockState.HANDSHAKE);
            }

         }
      }

      private void ensureHandshakeUnlock() {
         while(this.getSync().getLockState() == JaSSLEngineSynchronizer.LockState.HANDSHAKE) {
            this.getSync().unlock();
         }

      }
   }

   static enum RunnerResult {
      OK,
      INCOMPLETE_NETWORK_READ,
      INCOMPLETE_NETWORK_WRITE,
      NEED_APPLICATION_READ,
      CLOSED;
   }

   static enum Method {
      WRAP,
      UNWRAP;
   }
}
