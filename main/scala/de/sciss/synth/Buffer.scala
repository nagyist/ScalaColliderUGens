/*
 *  Buffer.scala
 *  (ScalaCollider)
 *
 *  Copyright (c) 2008-2010 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either
 *  version 2, june 1991 of the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public
 *  License (gpl.txt) along with this software; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 *
 *
 *  Changelog:
 */

package de.sciss.synth

import de.sciss.scalaosc.{ OSCBundle, OSCMessage, OSCPacket }
import de.sciss.synth.{ Completion => Comp, play => scplay }
import de.sciss.synth.io.{ AudioFileType, SampleFormat }
import ugen.{ BufRateScale, FreeSelfWhenDone, PlayBuf }
import osc._
import Model._

/**
 * 	@version	0.18, 17-May-10
 */
object Buffer {
//   sealed abstract class Completion {
//      private[Buffer] val message: Option[ Buffer => OSCMessage ]
//      private[Buffer] val action:  Option[ Buffer => Unit ]
//   }
   type Completion = Comp[ Buffer ]
   val NoCompletion = Comp[ Buffer ]( None, None )

   def alloc( server: Server = Server.default, numFrames: Int, numChannels: Int = 1,
              completion: Completion = NoCompletion ) : Buffer = {
      val b = apply( server )
      b.alloc( numFrames, numChannels, completion )
      b
   }

   def read( server: Server = Server.default, path: String, startFrame: Int = 0, numFrames: Int = -1,
             completion: Completion = NoCompletion ) : Buffer = {
      val b = apply( server )
      b.allocRead( path, startFrame, numFrames, completion )
      b
   }

   def cue( server: Server = Server.default, path: String, startFrame: Int = 0, numChannels: Int = 1,
            bufFrames: Int = 32768, completion: Completion = NoCompletion ) : Buffer = {
      val b = apply( server )
      b.alloc( bufFrames, numChannels, b.cueMsg( path, startFrame, completion ))
      b
   }

   def readChannel( server: Server = Server.default, path: String, startFrame: Int = 0, numFrames: Int = -1,
                    channels: Seq[ Int ], completion: Completion = NoCompletion ) : Buffer = {
      val b = apply( server )
      b.allocReadChannel( path, startFrame, numFrames, channels, completion )
      b
   }

   def apply( server: Server = Server.default ) : Buffer = apply( server, server.buffers.alloc( 1 ))

   private def isPowerOfTwo( i: Int ) = (i & (i-1)) == 0
}

case class Buffer( server: Server, id: Int ) extends Model {
   b =>

   def this( server: Server = Server.default ) = this( server, server.buffers.alloc( 1 ))

   import Buffer._

   private var released       = false
   private var numFramesVar   = -1
   private var numChannelsVar = -1
   private var sampleRateVar  = 0f

   override def toString = "Buffer(" + server + "," + id +
      (if( numFramesVar >= 0 ) ") : <" + numFramesVar + "," + numChannelsVar + "," + sampleRateVar + ">" else ")")

   def numFrames   = numFramesVar
   def numChannels = numChannelsVar
   def sampleRate  = sampleRateVar

   def register {
       server.bufMgr.register( this )
   }

   protected[synth] def updated( change: BufferManager.BufferInfo ) {
      val info       = change.info
      numFramesVar   = info.numFrames
      numChannelsVar = info.numChannels
      sampleRateVar  = info.sampleRate
      dispatch( change )
   }

   def queryMsg = OSCBufferQueryMessage( id )

   def free { server ! freeMsg }

	def free( completion: Option[ OSCMessage ]) {
		server ! freeMsg( completion, true )
	}

   def freeMsg: OSCBufferFreeMessage = freeMsg( None, true )

   /**
    *    @param   release  whether the buffer id should be immediately returned to the id-allocator or not.
    *                      if you build a system that monitors when bundles are really sent to the server,
    *                      and you need to deal with transaction abortion, you might want to pass in
    *                      <code>false</code> here, and manually release the id, using the <code>release</code>
    *                      method
    */
	def freeMsg( completion: Option[ OSCMessage ], release: Boolean = true ) = {
      if( release ) this.release
      OSCBufferFreeMessage( id, completion )
	}

   /**
    *    Releases the buffer id to the id-allocator pool, without sending any
    *    OSCMessage. Use with great care.
    */
   def release {
      if( released ) error( this.toString + " : has already been freed" )
      server.buffers.free( id )
      released = true
   }

   def close { server ! closeMsg }

   def close( completion: Option[ OSCMessage ]) {
      server ! closeMsg( completion )
   }

	def closeMsg: OSCBufferCloseMessage = closeMsg( None )

	def closeMsg( completion: Option[ OSCMessage ]) =
      OSCBufferCloseMessage( id, completion )

//	def alloc { server ! allocMsg }

	def alloc( numFrames: Int, numChannels: Int = 1, completion: Completion = NoCompletion ) {
		server ! allocMsg( numFrames, numChannels, makePacket( completion ))
	}

//	def allocMsg: OSCBufferAllocMessage = allocMsg( None )

	def allocMsg( numFrames: Int, numChannels: Int = 1, completion: Option[ OSCPacket ] = None ) = {
      numFramesVar   = numFrames
      numChannelsVar = numChannels
      sampleRateVar  = server.sampleRate.toFloat
      OSCBufferAllocMessage( id, numFrames, numChannels, completion )
   }

   def allocRead( path: String, startFrame: Int = 0, numFrames: Int = -1,
                  completion: Completion = NoCompletion ) {
//      path = argpath;
      server ! allocReadMsg( path, startFrame, numFrames, makePacket( completion, true ))
   }

   def allocReadMsg( path: String, startFrame: Int = 0, numFrames: Int = -1,
                     completion: Option[ OSCPacket ] = None ) = {
//      this.cache;
//      path = argpath;
      OSCBufferAllocReadMessage( id, path, startFrame, numFrames, completion )
   }

   def allocReadChannel( path: String, startFrame: Int = 0, numFrames: Int = -1, channels: Seq[ Int ],
                         completion: Completion = NoCompletion ) {
//      path = argpath;
      server ! allocReadChannelMsg( path, startFrame, numFrames, channels, makePacket( completion, true ))
   }

   def allocReadChannelMsg( path: String, startFrame: Int = 0, numFrames: Int = -1, channels: Seq[ Int ],
                            completion: Option[ OSCPacket ] = None ) = {
//      this.cache;
//      path = argpath;
      OSCBufferAllocReadChannelMessage( id, path, startFrame, numFrames, channels.toList, completion )
   }

   def cueMsg( path: String, startFrame: Int = 0, completion: Completion = NoCompletion ) =
      OSCBufferReadMessage( id, path, startFrame, numFrames, 0, true, makePacket( completion ))

   def read( path: String, fileStartFrame: Int = 0, numFrames: Int = -1, bufStartFrame: Int = 0,
             leaveOpen: Boolean = false, completion: Completion = NoCompletion ) {
      server ! readMsg( path, fileStartFrame, numFrames, bufStartFrame, leaveOpen, makePacket( completion ))
   }

   def readMsg( path: String, fileStartFrame: Int = 0, numFrames: Int = -1, bufStartFrame: Int = 0,
                leaveOpen: Boolean = false, completion: Option[ OSCPacket ] = None ) =
      OSCBufferReadMessage( id, path, fileStartFrame, numFrames, bufStartFrame, leaveOpen, completion )

   def readChannel( path: String, fileStartFrame: Int = 0, numFrames: Int = -1, bufStartFrame: Int = 0,
             leaveOpen: Boolean = false, channels: Seq[ Int ],
             completion: Completion = NoCompletion ) {
      server ! readChannelMsg( path, fileStartFrame, numFrames, bufStartFrame, leaveOpen,
         channels, makePacket( completion ))
   }

   def readChannelMsg( path: String, fileStartFrame: Int = 0, numFrames: Int = -1, bufStartFrame: Int = 0,
                leaveOpen: Boolean = false, channels: Seq[ Int ],
                completion: Option[ OSCPacket ] = None ) =
      OSCBufferReadChannelMessage( id, path, fileStartFrame, numFrames, bufStartFrame, leaveOpen, channels.toList,
         completion )

   def zero { server ! zeroMsg }

   def zero( completion: Option[ OSCPacket ]) {
      server ! zeroMsg( completion )
   }

	def zeroMsg: OSCBufferZeroMessage = zeroMsg( None )

	def zeroMsg( completion: Option[ OSCPacket ]) =
      OSCBufferZeroMessage( id, completion )

   def write( path: String, fileType: AudioFileType = AudioFileType.AIFF,
              sampleFormat: SampleFormat = SampleFormat.Float, numFrames: Int = -1, startFrame: Int = 0,
              leaveOpen: Boolean = false, completion: Completion = NoCompletion) {
//         path = path ?? { thisProcess.platform.recordingsDir +/+ "SC_" ++ Date.localtime.stamp ++ "." ++ headerFormat };
         server ! writeMsg( path, fileType, sampleFormat, numFrames, startFrame, leaveOpen, makePacket( completion ))
      }

   def writeMsg( path: String, fileType: AudioFileType = AudioFileType.AIFF,
                 sampleFormat: SampleFormat = SampleFormat.Float, numFrames: Int = -1, startFrame: Int = 0,
                 leaveOpen: Boolean = false, completion: Option[ OSCPacket] = None ) = {
//      require( isPowerOfTwo( this.numFrames ))
      OSCBufferWriteMessage( id, path, fileType, sampleFormat, numFrames, startFrame, leaveOpen, completion )
   }

   // ---- utility methods ----
   def play : Synth = play()
   def play( loop: Boolean = false, amp: Float = 1f, out: Int = 0 ) : Synth =
      scplay( server, out ) { // working around nasty compiler bug
         val ply = PlayBuf.ar( numChannels, id, BufRateScale.kr( id ), loop = if( loop ) 1 else 0 )
         if( !loop ) FreeSelfWhenDone.kr( ply )
         ply * "amp".kr( amp )
      }

   private def makePacket( completion: Completion, forceQuery: Boolean = false ) : Option[ OSCPacket ] = {
      val a = completion.action
      if( forceQuery || a.isDefined ) {
         register
         a.foreach( action => {
            lazy val l: Listener = addListener {
               case BufferManager.BufferInfo( _, _ ) => {
                  removeListener( l )
                  action( b )
               }
            }
         })
      }
      (completion.message, a) match {
         case (None, None)                => if( forceQuery ) Some( queryMsg ) else None
         case (Some( msg ), None)         => Some( if( forceQuery ) OSCBundle( msg.apply( b ), queryMsg ) else msg.apply( b ))
         case (None, Some( act ))         => Some( queryMsg )
         case (Some( msg ), Some( act ))  => Some( OSCBundle( msg.apply( b ), queryMsg ))
      }
   }
}