package de.sciss.synth
package ugen

import collection.immutable.{IndexedSeq => IIdxSeq}
import aux.Optional

//object WrapOut {
//
//}

//final case class WrapOut[R <: Rate]( rate: R, bus: GE, in: GE ) extends ZeroOutUGenSource[R] with WritesBus {
//   protected def expandUGens = {
//      val _bus = bus.expand
//      val _in = in.mexpand
//      val _sz_bus = _bus.size
//      val _sz_in = _in.size
//      val _exp_ = maxInt(_sz_bus, _sz_in)
//      IIdxSeq.tabulate(_exp_)(i => new ZeroOutUGen("Out", rate, IIdxSeq(_bus(i.%(_sz_bus))).++(_in(i.%(_sz_in)).expand)))
//   }
//}

object WrapOut {
   private def makeFadeEnv( fadeTime: Float ) : GE = {
      val dt			= "fadeTime".kr( fadeTime )
      val gate       = "gate".kr( 1 )
      val startVal	= (dt <= 0)
      // this is slightly more costly than what sclang does
      // (using non-linear shape plus an extra unary op),
      // but it fadeout is much smoother this way...
      EnvGen.kr( Env( startVal, List( Env.Seg( 1, 1, curveShape( -4 )), Env.Seg( 1, 0, sinShape )), 1 ),
         gate, timeScale = dt, doneAction = freeSelf ).squared
   }

   private def replaceZeroesWithSilence( a: IIdxSeq[ UGenIn ]) : IIdxSeq[ UGenIn ] = {
      error( "TODO")
   }
}
final case class WrapOut( in: GE, fadeTime: Optional[ Float ] = 0.02f ) extends UGenSource.ZeroOut with WritesBus {
   import WrapOut._
   protected def makeUGens: Unit = {
      val oute = "out".kr.expand
      val res2 = fadeTime.option match {
         case Some( fdt ) => IIdxSeq( oute, makeFadeEnv( fdt ).expand )
         case None        => IIdxSeq( oute )
      }
      unwrap( res2 ++ in.expand.outputs )
   }
   protected def makeUGen( _args: IIdxSeq[ UGenIn ]): Unit = {
      val (out, envo, in) = if( fadeTime.option.isDefined ) {
         (_args(0), None, _args.drop(1))
      } else {
         (_args(0), Some(_args(1)), _args.drop(2))
      }
      val rate = Rate.highest( in.map( _.rate ): _* )
      if( (rate == audio) || (rate == control) ) {
         val in3 = envo match {
            case Some( env ) =>
               val in2 = if( rate == audio ) replaceZeroesWithSilence( in ) else in
               in2.map( ch => BinaryOp.Times.make1( ch, env ))
               //* res1
            case None => in
         }
         new UGen.ZeroOut( "Out", rate, out +: in3 )
      }
   }
}

//object WrapType {
//   implicit def aGE( g: AnyGE ) : WrapType
//   implicit def aMulti( g: AnyMulti ) : WrapType
//   implicit def aLazy( g: Lazy ) : WrapType
//
//   private class
//}
//sealed trait WrapType
//
//object Hirzel {
//   def wrapOut[ O <: Option[ Float ], G ]( g: G, fadeTime : O = Some( 0.02f ))( implicit cons: WrapType[ G, O ]) {
//
//   }
//}

//private[synth] trait CanWrapOutLowImplicits {
//
//}

//object CanWrapOut {
////   implicit def ge( g: AnyGE ) : CanWrapOut = new GEImpl( g )
//   implicit def lazyElem( lz: => Lazy ) : CanWrapOut = new LazyImpl( lz )
//   implicit def unit( lz: => Unit ) : CanWrapOut = new LazyImpl( lz )
//
//   implicit def ar( g: GE[ audio ]) : CanWrapOut = new GEImpl[ audio ]( fdo =>
//      Out.ar( "out".kr, fdo match {
//         case Some( t ) => g * SynthGraph.makeFadeEnv( t )
//         case None => g
//      })
//   )
//
//   implicit def kr( g: GE[ control ]) : CanWrapOut = new GEImpl[ control ]( fdo =>
//      Out.kr( "out".kr, fdo match {
//         case Some( t ) => g * SynthGraph.makeFadeEnv( t )
//         case None => g
//      })
//   )
//
////   implicit def kr( g: Multi[ GE[ control ]]) : CanWrapOut = new GEImpl( g )
////   implicit def ir( g: Multi[ GE[ scalar  ]]) : CanWrapOut = new GEImpl( g )
//
//   private class GEImpl[ R <: Rate ]( g: Option[ Float ] => Unit ) extends CanWrapOut {
//      def wrapOut( fadeTime: Option[ Float ]) = SynthGraph { g( fadeTime )}
//   }
//
//   private class LazyImpl( lz: => Unit ) extends CanWrapOut {
//      def wrapOut( fadeTime: Option[ Float ]) = SynthGraph { lz }
//   }
//}
//
//sealed trait CanWrapOut { def wrapOut( fadeTime: Option[ Float ] = Some( 0.02f )) : SynthGraph }
