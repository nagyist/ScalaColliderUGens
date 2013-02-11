/*
 *  Spec.scala
 *  (ScalaColliderUGens)
 *
 *  Copyright (c) 2008-2013 Hanns Holger Rutz. All rights reserved.
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
 */

package de.sciss
package synth

import collection.immutable
import immutable.{IndexedSeq => IIdxSeq}
import collection.breakOut
import impl.{UGenSpecParser => ParserImpl}

object UGenSpec {
  lazy val standardUGens: Map[String, UGenSpec] = {
    val is = ugen.Control.getClass.getResourceAsStream("standard-ugens.xml")
    try {
      val source = xml.Source.fromInputStream(is)
      parseAll(source)
    } finally {
      is.close()
    }
  }

  def parseAll(source: xml.InputSource): Map[String, UGenSpec] = ParserImpl.parseAll(source)

  def parse(node: xml.Node): UGenSpec = ParserImpl.parse(node)

  // ---- UGen attributes ----

  object Attribute {
    sealed trait ImpliesSideEffect extends Attribute
    sealed trait ImpliesIndividual extends Attribute

    case object ReadsBus      extends Attribute.ImpliesIndividual   // cf Specified.txt
    case object ReadsBuffer   extends Attribute.ImpliesIndividual   // cf Specified.txt
    case object ReadsFFT      extends Attribute.ImpliesIndividual   // cf Specified.txt
    case object UsesRandSeed  extends Attribute.ImpliesIndividual
    case object IsIndividual  extends Attribute.ImpliesIndividual

    case object WritesBus     extends Attribute.ImpliesSideEffect with Attribute.ImpliesIndividual
    case object WritesBuffer  extends Attribute.ImpliesSideEffect with Attribute.ImpliesIndividual
    case object WritesFFT     extends Attribute.ImpliesSideEffect with Attribute.ImpliesIndividual
    case object HasSideEffect extends Attribute.ImpliesSideEffect

    case object HasDoneFlag   extends Attribute
  }
  sealed trait Attribute

  // ---- UGen input arguments ----

  final case class Argument(name: String, tpe: ArgumentType,
                            defaults: Map[MaybeRate, ArgumentValue],
                            rates: Map[MaybeRate, RateConstraint]) {
    override def toString = {
      val base = s"${name}: ${tpe}"
      val s1 = defaults.get(UndefinedRate) match {
        case Some(v) => s"${base} = ${v}"
        case _ => base
      }
      val m = defaults - UndefinedRate
      val s2 = if (m.isEmpty) s1 else {
        s"${s1} ${m.mkString("[", ", ", "]")}"
      }
      if (rates.isEmpty) s2 else {
        s"${s2} -> ${rates.mkString("[", ", ", "]")}"
      }
    }
  }

  object ArgumentType {
    case object Int extends ArgumentType
    sealed trait GELike extends ArgumentType { def shape: SignalShape }
    case object GEWithDoneFlag extends GELike { def shape: SignalShape = SignalShape.Generic }
//    final case class GE(shape: SignalShape) extends GELike
  }
  sealed trait ArgumentType

  object RateConstraint {
    case object SameAsUGen extends RateConstraint {
      override def toString = "same-rate-as-ugen"
    }
    final case class Fixed(rate: Rate) extends RateConstraint {
      override def toString = "fixed-rate=" + rate
    }
  }
  sealed trait RateConstraint

  object SignalShape {
    case object Generic     extends SignalShape // aka Float
    case object Int         extends SignalShape
    case object String      extends SignalShape
    case object Bus         extends SignalShape
    case object Buffer      extends SignalShape
    case object FFT         extends SignalShape
    case object Trigger     extends SignalShape // with values `low` and `high`
    case object Switch      extends SignalShape // with values `false` and `true`
    case object Gate        extends SignalShape // with values `closed` and `open`
    case object Mul         extends SignalShape
    case object DoneAction  extends SignalShape
    case object DoneFlag    extends SignalShape
  }
  sealed trait SignalShape extends ArgumentType.GELike { def shape = this }

  object ArgumentValue {
    final case class Int(value: scala.Int) extends ArgumentValue {
      def toGE = /* if (value == scala.Int.MaxValue) Constant(scala.Float.PositiveInfinity) else */ Constant(value)
      override def toString = value.toString
    }
    final case class Float(value: scala.Float) extends ArgumentValue {
      def toGE = Constant(value)
      override def toString = {
        val s = value.toString
        if (s.contains('.')) s else s + ".0"
      }
    }
    final case class Boolean(value: scala.Boolean) extends ArgumentValue {
      def toGE = Constant(if (value) 1f else 0f)
      override def toString = value.toString
    }
    final case class String(value: java.lang.String) extends ArgumentValue {
      def toGE = ???
      override def toString = s""""${value}""""
    }
    case object Inf extends ArgumentValue {
      def toGE = Constant(scala.Float.PositiveInfinity)
      override def toString = productPrefix.toLowerCase
    }
    final case class DoneAction(peer: synth.DoneAction) extends ArgumentValue {
      def toGE = Constant(peer.id)
      override def toString = peer.toString
    }
    case object Nyquist extends ArgumentValue {
      def toGE = ???
      override def toString = productPrefix.toLowerCase
    }
  }
  sealed trait ArgumentValue {
    def toGE: GE
  }

  final case class Input(arg: String, variadic: Boolean)

  // ---- Supported rates ----

  object Rates {
    final case class Implied(rate: Rate, method: Option[String]) extends Rates {
      override def toString = {
        val base = "implied: " + rate
        method match {
          case Some(m) => s"${base} (method = ${m})"
          case _ => base
        }
      }
    }
    final case class Set(rates: immutable.Set[Rate]) extends Rates {
      override def toString = rates.mkString("[", ", ", "]")
    }
  }
  sealed trait Rates

  // ---- Outputs ----

//  object Outputs {
//    final case class Argument(name: String) extends Outputs
//    // type = fft
//
//  }
//  sealed trait Outputs

  final case class Output(name: Option[String], shape: SignalShape, variadic: Option[String])
}
final case class UGenSpec(name: String, attr: Set[UGenSpec.Attribute], rates: UGenSpec.Rates,
                          args:    IIdxSeq[UGenSpec.Argument],
                          inputs:  IIdxSeq[UGenSpec.Input   ],
                          outputs: IIdxSeq[UGenSpec.Output  ]) {
  lazy val argMap:   Map[String, UGenSpec.Argument] = args.map  (a => a.name -> a)(breakOut)
  lazy val inputMap: Map[String, UGenSpec.Input   ] = inputs.map(i => i.arg  -> i)(breakOut)

  override def toString = s"${productPrefix}(${name}, attr = ${attr.mkString("[", ", ", "]")}, rates = ${rates}, " +
                          s"args = ${args.mkString("[", ", ", "]")}, inputs = ${inputs.mkString("[", ", ", "]")}, " +
                          s"outputs = ${outputs.mkString("[", ", ", "]")})"
}