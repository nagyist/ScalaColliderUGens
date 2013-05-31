package de.sciss.synth
package ugen

final case class ChannelProxy(elem: GE, index: Int) extends GE.Lazy {
  def rate = elem.rate

  override def toString = s"$elem.\\($index)"

  def makeUGens: UGenInLike = {
    val _elem = elem.expand
    _elem.unwrap(index)
  }
}