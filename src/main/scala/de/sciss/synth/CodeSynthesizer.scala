/*
 *  CodeSynthesizer.scala
 *  (ScalaCollider-UGens)
 *
 *  Copyright (c) 2008-2011 Hanns Holger Rutz. All rights reserved.
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

import scala.tools.nsc.symtab.Flags
import tools.refactoring.Refactoring
import tools.refactoring.util.CompilerProvider
import xml.Node
import collection.breakOut
import collection.immutable.{ IndexedSeq => IIdxSeq }
import net.virtualvoid.string.MyNodePrinter
import tools.refactoring.transformation.TreeFactory
import tools.refactoring.common.{CompilerAccess, Tracing, Change}
import tools.nsc.io.AbstractFile
import java.io.{FileOutputStream, OutputStreamWriter, FileWriter, File}

class CodeSynthesizer extends Refactoring
with Tracing with CompilerProvider with MyNodePrinter with CompilerAccess with TreeFactory {
   override val defaultIndentationStep = "   "

   import global._

   def compilationUnitOfFile( f: AbstractFile ) = global.unitOfFile.get( f )

//   def myMkClass(
//      mods: Modifiers = NoMods,
//      name: String,
//      tparams: List[TypeDef] = Nil,
//      args: List[(Modifiers, String, Tree)],
//      body: List[Tree] = Nil,
//      parents: List[Tree] = Nil,
//      superArgs: List[Tree] = Nil) = {
//
//      val constructorArguments0 = args.map {
//         case (mods, name, tpe) =>
//            ValDef(mods | Flags.PARAMACCESSOR, name, tpe, EmptyTree)
//      }
//      val constructorArguments = if( args.nonEmpty ) constructorArguments0 else {
//         ValDef(NoMods | Flags.PARAMACCESSOR, "\t", EmptyTree, EmptyTree) :: constructorArguments0
//      }
//
//      val superArgsCall = superArgs match {
//         case Nil => EmptyTree
//         case args =>
//            mkDefDef(name = nme.CONSTRUCTOR.toString, body = Apply(EmptyTree, args) :: Nil)
//      }
//
//      ClassDef(
//         mods,
//         mkTypeName(name),
//         tparams,
//         Template(
//            parents,
//            emptyValDef,
//            superArgsCall :: constructorArguments ::: body
//         )
//      )
//   }

   def myMkCaseClass(
      mods: Modifiers = NoMods,
      name: String,
      tparams: List[TypeDef] = Nil,
      args: List[(Modifiers, String, Tree)],
      body: List[Tree] = Nil,
      parents: List[Tree] = Nil,
      superArgs: List[Tree] = Nil) = {

//    if (args.size < 1) throw new IllegalArgumentException("Case-class must have at least one argument.")

      mkClass(mods withPosition (Flags.CASE, NoPosition), name, tparams, args, body, parents, superArgs)
   }

   def perform( xml: Node, dir: File ) {

//      val testAst = treeFrom( "class A(); class B" )
//      println( printer( testAst ))
//      println( createText( testAst ))
//
//      ↓(matchingChildren(transform {
//         case t: Template => t.body.foreach {
//            case d: DefDef =>
////               if( d.name == "eins" || d.name == "zwei" ) {
//                  println( "For " + d.name + " --> " + d.vparamss )
////               }
////               println( "Jo, defdef( name = " + d.name+ ", name " + d.hasSymbol + " ) = " + d )
////               println( printer( d ))
//            case _ =>
//         }
//         t
//         case x => x
//      })) apply testAst

//      val traitGE = ClassDef(
//         NoMods withPosition (Flags.TRAIT, NoPosition), // Modifiers( Flags.CASEACCESSOR ),
//         "GE",
//         Nil,
//         Template(
//            EmptyTree :: Nil,
//            emptyValDef,
//            Nil
//         )
//      )

//      val traitGE = TypeDef( Modifiers( Flags.TRAIT ), "GE", Nil, EmptyTree )
      val traitSideEffect  = TypeDef( Modifiers( Flags.TRAIT ), "HasSideEffect", Nil, EmptyTree )
      val traitDoneFlag    = TypeDef( Modifiers( Flags.TRAIT ), "HasDoneFlag",   Nil, EmptyTree )
      val traitRandom      = TypeDef( Modifiers( Flags.TRAIT ), "UsesRandSeed",  Nil, EmptyTree )
      val traitIndiv       = TypeDef( Modifiers( Flags.TRAIT ), "IsIndividual",  Nil, EmptyTree )
      val traitWritesBuffer= TypeDef( Modifiers( Flags.TRAIT ), "WritesBuffer",  Nil, EmptyTree )
      val traitWritesBus   = TypeDef( Modifiers( Flags.TRAIT ), "WritesBus",     Nil, EmptyTree )
      val identIIdxSeq     = Ident( "IIdxSeq" )
//      val argIndiv         = SyntheticUGenArgInfo( "_indiv", ArgInfo( TypeInfo( ("Int", Nil) :: Nil ), None, None ))

      // bug in scala-refactoring : case class constructor arg list cannot be empty
//      val dummyCaseClassArgs = (NoMods, "", EmptyTree) :: Nil

      (xml \ "file") foreach { node =>
         val name       = (node \ "@name").text
         val fileName   = name + ".scala"
         val ast        = treeFrom( "package de.sciss.synth.ugen\n" )
         val ugens: List[ Tree ] = (node \ "ugen").flatMap( node => {
            val name          = (node \ "@name").text
            val readsBus      = getBoolAttr( node, "readsbus" )
            val writesBus     = getBoolAttr( node, "writesbus" )
//if( name == "Out" ) println( " OUT : " + writesBus )
            val readsBuffer   = getBoolAttr( node, "readsbuf" )
            val writesBuffer  = getBoolAttr( node, "writesbuf" )
            val sideEffect    = getBoolAttr( node, "sideeffect" ) // || writesBus || writesBuffer
            val indSideEffect = writesBuffer || writesBus
            val doneFlag      = getBoolAttr( node, "doneflag" )
            val random        = getBoolAttr( node, "random" )
            val indiv         = getBoolAttr( node, "indiv" ) // || random
            val indIndiv      = writesBuffer || writesBus || random
            val rates: List[ RateInfo ] = (node \ "rate").map( n => {
               val name       = (n \ "@name").text
               val methodName = name match {
                  case "audio"   => "ar"
                  case "control" => "kr"
                  case "scalar"  => "ir"
                  case "demand"  => "dr"
               }
               val implied    = getBoolAttr( n, "implied" )
               RateInfo( name, methodName, implied, n )
            })( breakOut )
            val impliedRate   = rates.find( _.implied )
            if( impliedRate.isDefined ) require( rates.size == 1, "Can only have one implied rate (" + name + ")" )

            val docNode       = (node \ "doc")
            val docSees       = (docNode \ "see").map( _.text )
            val docWarnPos    = docNode.headOption.map( getBoolAttr( _, "warnpos" )).getOrElse( false )
            val docText       = (docNode \ "text").text

            val argsTup : List[ (UGenArgInfo, Int) ] = (node \ "arg").zipWithIndex.map( tup => {
               val (n, idx0)  = tup
               val idx        = getIntAttr( n, "pos", idx0 )
               val name       = (n \ "@name").text
               val multi      = getBoolAttr( n, "multi" )
               val doneFlagArg= getBoolAttr( n, "doneflag" )
               val rateAttr   = (n \ "@rate").text
               if( multi ) require( !doneFlagArg, "Multi arguments cannot have done flag (" + name + ")" )

               def createInfo( rateNode: Option[ Node ], rateInfo: Option[ RateInfo ]) : ArgInfo = {
                  def getEitherAttr( name: String ) : Option[ Node ] = {
                     val attrName = "@" + name
                     (n \ attrName).headOption.orElse( rateNode.flatMap( n => (n \ attrName).headOption ))
                  }
                  val typInfo    = getEitherAttr( "type" ).map( n => TypeInfo( (n.text -> Nil) :: Nil ))
   //                  .getOrElse( TypeInfo( (if( multi ) "MultiGE" else "GE") -> (TypeInfo( ("AnyUGenIn" -> Nil) ) :: Nil) ))
                     .getOrElse({
                        val typ0 = if( (rateAttr == "") || ((rateAttr == "ugen") && rateInfo.isEmpty) ) {
                           if( doneFlagArg ) {
                              TypeInfo( ("GE" -> (TypeInfo( ("R" -> Nil) :: Nil ) :: TypeInfo(
                                 List( ("UGenIn" -> (TypeInfo( ("R" -> Nil) :: Nil ) :: Nil)),
                                       ("HasDoneFlag" -> Nil))
                              ) :: Nil)) :: Nil, Some( "R" -> "<: Rate" ))
                           } else {
                              TypeInfo( ("AnyGE" -> Nil) :: Nil )
                           }
                        } else {
                           val rateName = if( rateAttr == "ugen" ) rateInfo.get.name else rateAttr
                           val t0 = "UGenIn" -> (TypeInfo( (rateName -> Nil) :: Nil ) :: Nil)
                           val t1: List[ (String, List[ TypeInfo ])] = if( doneFlagArg ) {
                              List( t0, "HasDoneFlag" -> Nil )
                           } else {
                              t0 :: Nil
                           }
                           TypeInfo( ("GE" -> (TypeInfo( (rateName -> Nil) :: Nil ) :: TypeInfo( t1 ) :: Nil)) :: Nil )
                        }
                        if( multi ) {
                           TypeInfo( ("Expands" -> (typ0 :: Nil)) :: Nil )
                        } else {
                           typ0
                        }
                     })
                  val default    = getEitherAttr( "default" ).map( _.text )
                  val doc        = getEitherAttr( "doc" ).map( _.text )
                  ArgInfo( typInfo, default, doc )
               }

               val argDefault = createInfo( None, None )
               val argMap: Map[ RateInfo, ArgInfo ] = rates.map( rateInfo => {
                  val rateNode   = (rateInfo.xml \ "arg").find( n => (n \ "@name").text == name )
                  rateInfo -> createInfo( rateNode, Some( rateInfo ))
               })( breakOut )
               UGenArgInfo( name, argDefault, argMap, multi ) -> idx
//               val typ        =
//               val vParam     = ValDef( Modifiers( Flags.PARAM ), name, TypeTree( typ /* selectedValue.tpt.tpe */ ), EmptyTree ) :: Nil
//               vParam
            })( breakOut )
//            val argsPoss   = args0.map( _._2 )
            val numArgsIn  = argsTup.size
//            require( numArgsIn == argsPoss.toSet.size, "Wrong argument positions (" + name + ")" )
            val argsOut    = argsTup.map( _._1 ).filter( _.isGE )
            val argsIn     = List.tabulate( numArgsIn )(
               idx => argsTup.find( _._2 == idx ).getOrElse( Predef.error( "Wrong argument positions (" + name + ")" ))._1 )
//            val argsInS    = if( indiv ) {
//               argsIn ::: (argIndiv :: Nil)
//            } else argsIn
         val argsInS = argsIn  // no exceptions at the moment

            val outputs       = (node \ "outputs").headOption match {
               case Some( n ) => (n \ "@num").text match {
                  case "0" => ZeroOutputs
                  case t   => MultiOutput( t )
               }
               case None      => SingleOutput
            }

//            val trnsAst    = ↓( matchingChildren( trns )) apply ast
//            val changes    = refactor( trnsAst.toList )
//            val outputText = Change.applyChanges( changes, inputText )

//            val methodBody = Block( Select( Ident( "freq" ), "toString" ) :: Nil, EmptyTree )
//            val methodBody = Select( Ident( "freq" ), "toString" ))
            val objectMethodDefs = rates.flatMap( rateInfo => {

               val objectMethodArgs = argsIn map { uArgInfo =>
                  ValDef(
                     Modifiers( Flags.PARAM ),
                     uArgInfo.name,
                     Ident( uArgInfo.arg( rateInfo ).typ.toString ),
                     uArgInfo.arg( rateInfo ).default.map( s => if( uArgInfo.isGE ) {
                        try {
//                        Literal( Constant( s.toFloat ))
                           Ident( s.toFloat.toString + "f" )  // XXX workaround for scala-refactoring bug of missing f
                        } catch {
                           case e: NumberFormatException => Ident( s )
                        }
                     } else Ident( s )).getOrElse( EmptyTree )
                  )
               }

               val args0 = argsInS.map( i => Ident( i.name ))
               val methodBody = if( impliedRate.isDefined ) {
                  Apply( Ident( "apply" ), args0 )
               } else {
                  // the type application is still needed for the compiler not complain about some
                  // audio.type versus audio bullshit...
                  Apply( TypeApply( Ident( "apply" ), Ident( rateInfo.typ ) :: Nil ), Ident( rateInfo.name ) :: args0 )
//                  Apply( Ident( "apply" ), Ident( rateInfo.name ) :: args0 )
               }
               val def0 = DefDef(
                  NoMods withPosition (Flags.METHOD, NoPosition),
                  rateInfo.methodName,
                  Nil,        // tparams
                  objectMethodArgs :: Nil,    // vparamss
                  TypeTree( NoType ), // tpt -- empty for testing
                  methodBody // rhs
               ) :: Nil
               val allDefaults = argsIn.nonEmpty && argsIn.forall( _.arg( rateInfo ).default.isDefined )
               if( allDefaults ) {
                  val methodBody = Apply( Ident( rateInfo.methodName ), Ident( " " ) :: Nil )  // XXX how to get ar() with the parentheses?
                  DefDef(
                     NoMods withPosition (Flags.METHOD, NoPosition),
                     rateInfo.methodName,
                     Nil,        // tparams
                     Nil,        // vparams
                     TypeDef( NoMods, name, if( impliedRate.isDefined ) Nil else {
                        TypeDef( NoMods, rateInfo.typ, Nil, EmptyTree ) :: Nil
                     }, EmptyTree ),
                     methodBody // rhs
                  ) :: def0
               } else def0
            })

            val objectDef = ModuleDef(
               NoMods,
               name,
               Template(
                  EmptyTree :: Nil, // parents
                  emptyValDef,      // self
                  objectMethodDefs  // body
               )
            )

            val caseClassConstrArgs  = {
               val args0 = argsInS map { uArgInfo => (NoMods, uArgInfo.name + ": " + uArgInfo.argDefault.typ, EmptyTree) }
               if( impliedRate.isDefined ) args0 else {
                  (NoMods, "rate: R", EmptyTree) :: args0
               }
            }

            // hmmmm... is this the cleanest way to define R <: Rate?
//            if( impliedRate.isDefined ) Nil else (TypeDef( NoMods, "R", Nil, TypeBoundsTree( EmptyTree, TypeDef( NoMods, "Rate", Nil, EmptyTree ))) :: Nil), // tparams
            val caseClassTypeParam = if( impliedRate.isDefined ) Nil else (TypeDef( NoMods, "R <: Rate", Nil, EmptyTree ) :: Nil)

//            val expArgs    = argsOut.filter( _.isGE )
            val hasArgsOut = argsOut.size > 0
            val ugenName   = if( hasArgsOut ) name + "UGen" else name

            val caseCommonParents : List[ TypeDef ] = {
               val p1 = if( doneFlag ) (traitDoneFlag :: Nil) else Nil
               val p2 = if( random ) (traitRandom :: p1) else p1
               val p3 = if( writesBus ) (traitWritesBus :: p2) else p2
               val p4 = if( writesBuffer ) (traitWritesBuffer :: p3) else p3
               val p5 = if( indiv && !indIndiv ) (traitIndiv :: p4) else p4
//               val p6 = if( sideEffect && !indSideEffect ) ...
               impliedRate.map( r => TypeDef( NoMods, r.traitTyp, Nil, EmptyTree ) :: p5 ).getOrElse( p5 )
            }

            val classes0   = if( hasArgsOut ) {

            val caseClassExpandDef = {
//               val bufE    = buf.expand
//               val multiE  = multi.expand
//               val numExp  = math.max( bufE.size, multiE.size )
//               IIdxSeq.tabulate( numExp )( i => DiskOutUGen( bufE( i % numExp ), multiE( i % numExp ).expand ))
               val moreThanOneArgOut = argsOut.size > 1
               val methodBody = Block(
//                  (expArgs.map( a => ValDef( NoMods, "_" + a.arg.name, TypeTree( NoType ), Select( Ident( a.arg.name ), "expand" )))
                  // XXX dirty
                  (argsOut.map( a => ValDef( NoMods, "_" + a.name + ": IIdxSeq[" + a.deriveGE + "]", TypeTree( NoType ), Select( Ident( a.name ), "expand" ))) ++
                   (if( moreThanOneArgOut ) {
                      argsOut.map( a => ValDef( NoMods, "_sz_" + a.name, TypeTree( NoType ), Select( Ident( "_" + a.name ), "size" )))
                   } else {
                      Nil
                   }) ++ {
//                     if( !moreThanOneArgOut ) println( name + " " + expArgs + " / " + args )
                     val numId = if( moreThanOneArgOut ) Ident( "_exp_" ) else Select( Ident( "_" + argsOut.head.name ), "size" )
                     val app0 = Apply( Apply( Select( identIIdxSeq, "tabulate" ), numId :: Nil ),
                        Function( ValDef( Modifiers( Flags.PARAM ), "i", TypeTree( NoType ), EmptyTree ) :: Nil,
                           Apply( Ident( ugenName ), {
                              val args0 = argsInS.map( a => {
                                 if( a.isGE ) {
                                    val apply = Apply( Ident( "_" + a.name ), {
                                       if( moreThanOneArgOut ) {
                                          Apply( Select( Ident( "i" ), "%" ), Ident( "_sz_" + a.name ) :: Nil )
                                       } else {
                                          Ident( "i" )
                                       }
                                    }  :: Nil )
//                                    if( a.multi ) Select( apply, "expand" ) else apply
                                    apply
                                 } else Ident( a.name )
                              })
                              if( impliedRate.isDefined ) args0 else (Ident( "rate" ) :: args0)
                           })
                        ) :: Nil
                     ) :: Nil
                     if( moreThanOneArgOut ) {
                        ValDef( NoMods, "_exp_", TypeTree( NoType ), Apply( Ident( "maxInt" ), argsOut.map( a => Ident( "_sz_" + a.name )))) :: app0
                     } else app0
                  }): _*
               )
//                  Apply( Ident( "apply" ), Ident( rateInfo.name ) :: args.map( i => Ident( i.arg.name )))
               DefDef(
                  NoMods withPosition (Flags.METHOD, NoPosition),
                  "expand",
                  Nil, // tparams
                  Nil,        // vparamss
                  TypeTree( NoType ), // tpt -- empty for testing
                  methodBody // rhs
               )
            }

            val caseClassMethods =
//            if( indiv ) {
//               caseClassExpandDef :: methodOverrideEquals :: methodOverrideHashCode :: Nil
//            } else {
               caseClassExpandDef :: Nil
//            }

            val caseClassDef = myMkCaseClass(
               NoMods,
               name,

               caseClassTypeParam, // tparams
               caseClassConstrArgs,
               caseClassMethods,
               {
                  val p4 = if( sideEffect && !indSideEffect ) (traitSideEffect :: caseCommonParents) else caseCommonParents
//                  val t0 = TypeDef( NoMods, if( outputs == SingleOutput ) "GE" else "Expands", {
//                     val p0 = TypeDef( NoMods, ugenName,
//                        if( impliedRate.isDefined ) Nil else (TypeDef( NoMods, "R", Nil, EmptyTree ) :: Nil),
//                        EmptyTree ) :: Nil
//                     if( outputs == SingleOutput ) {
//                        TypeDef( NoMods, impliedRate.map( _.typ ).getOrElse( "R" ): String, Nil, EmptyTree ) :: p0
//                     } else {
//                        p0
//                     }
//                  }, EmptyTree )
//                  impliedRate.map( r => t0 :: TypeDef( NoMods, r.traitTyp, Nil, EmptyTree ) :: Nil ).getOrElse( t0 :: Nil )
                  TypeDef( NoMods, if( outputs == SingleOutput ) "GE" else "Expands", {
                     val p0 = TypeDef( NoMods, ugenName,
                        if( impliedRate.isDefined ) Nil else (TypeDef( NoMods, "R", Nil, EmptyTree ) :: Nil),
                        EmptyTree ) :: Nil
                     if( outputs == SingleOutput ) {
                        TypeDef( NoMods, impliedRate.map( _.typ ).getOrElse( "R" ): String, Nil, EmptyTree ) :: p0
                     } else {
                        p0
                     }
                  }, EmptyTree ) :: p4
               }
            )

//            caseClassDef.setSymbol( new tools.nsc.symtab.Symbols.Symbol( NoSymbol, NoPosition, new Name( 0, 0 )))

//            global.docComment( caseClassDef.symbol, "Jo chuck", NoPosition )
//            global.docComments += caseClassDef.symbol -> DocComment( "/** Kuuka */", NoPosition )

//            println( "JUHU " + caseClassConstr.symbol.isConstructor )
//            println( printer( caseClassConstr ))

//            case class SinOscUGen[ R <: Rate ]( freq: AnyUGenIn, phase: AnyUGenIn )
//            extends SingleOutUGen[ R ]( List( freq, phase ))
               objectDef :: caseClassDef :: Nil
            } else {
               objectDef :: Nil
            }

            val ugenCaseClassConstrArgs = {
               val args0 = argsIn map { uArgInfo =>
                  (NoMods, uArgInfo.name + ": " + uArgInfo.deriveGE, EmptyTree)
               }
               if( impliedRate.isDefined ) args0 else {
                  (NoMods, "rate: R", EmptyTree) :: args0
               }
            }

            val ugenCaseClassParents: List[ TypeDef ] = {
               // note: ZeroOutUGen already extends HasSideEffect
               val p4 = if( sideEffect && !indSideEffect && (outputs != ZeroOutputs) ) (traitSideEffect :: caseCommonParents) else caseCommonParents
               TypeDef( NoMods, outputs.typ, if( outputs != SingleOutput ) Nil else {
                  TypeDef( NoMods, impliedRate.map( _.typ ).getOrElse( "R" ): String, Nil, EmptyTree ) :: Nil
               }, EmptyTree ) :: p4
            }
//            val ugenCaseClassParents: List[ TypeDef ] = impliedRate.map( r =>
//               ugenCaseClassParents0 :: TypeDef( NoMods, r.traitTyp, Nil, EmptyTree ) :: Nil
//            ).getOrElse( ugenCaseClassParents0 :: Nil )

            val ugenCaseClassDef = myMkCaseClass(
               NoMods,
               ugenName,
               caseClassTypeParam, // tparams
               /* if( ugenCaseClassConstrArgs.nonEmpty ) */ ugenCaseClassConstrArgs /* else dummyCaseClassArgs */,
               Nil,
               ugenCaseClassParents,
               superArgs = {
                  val geArgs = argsOut // args.filter( _.isGE )  // XXX TODO: order
                  val args0 = geArgs.lastOption match {
                     case Some( a ) if( a.multi ) =>
//                        val rvsArgs = geArgs.dropRight( 1 ).reverse
//                        rvsArgs.foldLeft[ Tree ]( Select( Ident( a.arg.name ), "expand" ))( (a, b) => Apply( Select( a, "+:" ), Ident( b.arg.name ) :: Nil )) :: Nil
                        val args1   = geArgs.dropRight( 1 )
                        val sel     = Select( Ident( a.name ), "expand" )
                        (if( args1.nonEmpty ) {
                           Apply( Select( Apply( TypeApply( identIIdxSeq, Ident( "AnyUGenIn" ) :: Nil ), geArgs.dropRight( 1 ).map( a => Ident( a.name ))),
                              "++" ), sel :: Nil )
                        } else {
                           sel
                        }) :: Nil
                     case _ => (if( hasArgsOut ) {
                        Apply( identIIdxSeq, geArgs.map( a => Ident( a.name )))
                     } else {
                        Select( identIIdxSeq, "empty" )
                     }) :: Nil
                  }
                  outputs match {
                     case m: MultiOutput =>
                        Apply( Apply( Select( identIIdxSeq, "fill" ), Ident( m.num ) :: Nil ),
                           Ident( impliedRate.map( _.typ ).getOrElse( "rate" )) :: Nil ) :: args0
                     case _ => args0
                  }
               }
            )

            classes0 ++ List( ugenCaseClassDef ) // how to prepend a blank line??

//            println( outputText )
         })( breakOut )

         // XXX add UGen class

         val packageDef = PackageDef( Select( Select( Ident( "de" ), "sciss" ), "synth" ),
            PackageDef( Ident( "ugen" ),
               Import( Select( Ident( "collection" ), "immutable" ), ImportSelector( "IndexedSeq", -1, identIIdxSeq.name, -1 ) :: Nil ) ::
               Import( Ident( "UGenHelper" ), ImportSelector( nme.WILDCARD, -1, nme.WILDCARD, -1 ) :: Nil ) ::
               ugens ) :: Nil )
//         println( createText( packageDef ))
//         println()
//         println( createText( ugens.head ))
         println( "Writing " + fileName )
         val osw = new OutputStreamWriter( new FileOutputStream( new File( dir, fileName )), "UTF-8" )
         osw.write( """/*
 * """ + fileName + """
 * (ScalaCollider-UGens)
 *
 * This is a synthetically generated file.
 * Created: """ + (new java.util.Date()).toString + """
 * ScalaCollider-UGen version: """ + UGens.versionString + """
 */

""" )
         osw.write( createText( packageDef ))
         osw.close()
      }
      println( "Done.")
   }

   private def getBoolAttr( n: Node, name: String, default: Boolean = false ) =
      (n \ ("@" + name)).headOption.map( _.text.toBoolean ).getOrElse( default )

   private def getIntAttr( n: Node, name: String, default: Int ) =
      (n \ ("@" + name)).headOption.map( _.text.toInt ).getOrElse( default )

   private case class RateInfo( name: String, methodName: String, implied: Boolean, xml: Node ) {
//      def typ = name + ".type"
      def typ = name // + ".type"
      def traitTyp = name.capitalize + "Rated"
   }
//   private object TypeInfo {
//      def toString( typ: Seq[ TypeInfo ]) : String = {
//         typ.map( t => t.name + (t.params match {
//            case Nil  => ""
//            case coll => coll.mkString( "[ ", ", ", " ]" )
//         })).mkString( " with ")
//      }
//   }
//   private case class TypeInfo( name: String, params: List[ TypeInfo ] = Nil )

//   private case class MyFunction(vparams: List[ValOrDefDef], body: Tree) extends TermTree with SymTree
//   private case class ArgDef(mods: Modifiers, name: Name, tpt: Tree, rhs: Tree) extends ValOrDefDef

   // XXX remove in favour of TypeDef
   private case class TypeInfo( tuples: List[ (String, List[ TypeInfo ])], exist: Option[ (String, String) ] = None ) {
      override def toString = {
         val s0 = tuples.map( t => t._1 + (t._2 match {
            case Nil  => ""
            case coll => coll.mkString( "[", ", ", "]" )
         })).mkString( " with ")
         exist.map( tup => s0 + " forSome { type " + tup._1 + " " + tup._2 + " }" ).getOrElse( s0 )
      }
   }
   private case class ArgInfo( typ: TypeInfo, default: Option[ String ], doc: Option[ String ])
   private trait UGenArgInfoLike {
      def name : String
      def argDefault : ArgInfo
      def multi : Boolean

      def arg( rate: RateInfo ) : ArgInfo

      def isGE = argDefault.typ.tuples.headOption match {
         case Some( ("GE", _) )        => true
         case Some( ("AnyGE", _) )     => true
         case Some( ("Expands", List( TypeInfo( List( (sub, _), _* ), _ ), _* ))) => sub match {
            case "GE"      => true
            case "AnyGE"   => true
            case _         => false
         }
//         case Some( ("MultiGE", _) )   => true
         case _                        => false
      }

      def deriveGE = argDefault.typ.tuples.headOption match {
         case Some( ("GE", List( r, sub, _* )))    => argDefault.typ.exist.map( tup => {
//            println( "Jo. derive with existential '" + sub + "'" )
            val res = sub match {
//               case TypeInfo( List( ("UGenIn", List( TypeInfo( List( (tup._1, Nil) ), None )))), None ) => "AnyUGenIn"
               case TypeInfo( List( ("UGenIn", List( TypeInfo( List( (tup._1, Nil) ), None ))), rest @ _* ), None ) =>
                  TypeInfo( ("AnyUGenIn" -> Nil) :: rest.toList, None )
               case _ => Predef.error( "Cannot derive type " + argDefault.typ.toString )
            }
//            println( "res = " + res )
            res.toString
         }).getOrElse( sub.toString )
         case Some( ("Expands", List( sub, _* )))  => sub.toString
         case Some( ("AnyGE", _) )                 => "AnyUGenIn"
         case _                                    => argDefault.typ.toString
      }
   }

   private case class UGenArgInfo( name: String, argDefault: ArgInfo, argMap: Map[ RateInfo, ArgInfo ], multi: Boolean )
   extends UGenArgInfoLike {
      def arg( rate: RateInfo ) : ArgInfo = argMap.getOrElse( rate, Predef.error( "Accessing illegal rate " + rate.name + " (" + name + ")" ))
   }

   private case class SyntheticUGenArgInfo( name: String, argDefault: ArgInfo )
   extends UGenArgInfoLike {
      def multi = false
      def arg( rate: RateInfo ) : ArgInfo = argDefault
   }

   private abstract sealed class Outputs {
      def typ: String
   }
   private case object ZeroOutputs extends Outputs {
      val typ = "ZeroOutUGen"
   }
   private case object SingleOutput extends Outputs {
      val typ = "SingleOutUGen"
   }
   private case class MultiOutput( num: String ) extends Outputs {
      val typ = "MultiOutUGen"
   }
}