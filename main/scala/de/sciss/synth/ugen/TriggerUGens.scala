/*
 * TriggerUGens.scala
 * (ScalaCollider-UGens)
 *
 * This is a synthetically generated file.
 * Created: Fri Mar 04 00:00:34 GMT 2011
 * ScalaCollider-UGen version: 0.11
 */

package de.sciss.synth
package ugen
import collection.immutable.{IndexedSeq => IIdxSeq}
import aux.UGenHelper._
/**
 * A UGen which outputs a value of 1 for a given duration when triggered.
 * 
 * When a trigger occurs at the input, a value of 1 is output for the specified duration,
 * otherwise zero is output. When a new trigger occurs while this ugens outputs 1, the
 * hold-time is reset to the duration.
 * 
 * @see [[de.sciss.synth.ugen.Trig]]
 */
object Trig1 {
   
   /**
    * @param in              the trigger. This can be any signal. A trigger happens when the signal changes
    *                        from non-positive to positive.
    * @param dur             the duration for which the ugens holds the value of 1 when triggered
    */
   def ar(in: AnyGE, dur: AnyGE = 0.1f) = apply[audio](audio, in, dur)
   /**
    * @param in              the trigger. This can be any signal. A trigger happens when the signal changes
    *                        from non-positive to positive.
    * @param dur             the duration for which the ugens holds the value of 1 when triggered
    */
   def kr(in: AnyGE, dur: AnyGE = 0.1f) = apply[control](control, in, dur)
}
/**
 * A UGen which outputs a value of 1 for a given duration when triggered.
 * 
 * When a trigger occurs at the input, a value of 1 is output for the specified duration,
 * otherwise zero is output. When a new trigger occurs while this ugens outputs 1, the
 * hold-time is reset to the duration.
 * 
 * @param in              the trigger. This can be any signal. A trigger happens when the signal changes
 *                        from non-positive to positive.
 * @param dur             the duration for which the ugens holds the value of 1 when triggered
 * 
 * @see [[de.sciss.synth.ugen.Trig]]
 */
final case class Trig1[R <: Rate](rate: R, in: AnyGE, dur: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _in = in.expand
      val _dur = dur.expand
      val _sz_in = _in.size
      val _sz_dur = _dur.size
      val _exp_ = maxInt(_sz_in, _sz_dur)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Trig1", rate, IIdxSeq(_in(i.%(_sz_in)), _dur(i.%(_sz_dur)))))
   }
}
object Trig {
   
   /**
    * @param in              the trigger. This can be any signal. A trigger happens when the signal changes
    *                        from non-positive to positive.
    * @param dur             the duration for which the ugens holds the value of the input signal when triggered
    */
   def ar(in: AnyGE, dur: AnyGE = 0.1f) = apply[audio](audio, in, dur)
   /**
    * @param in              the trigger. This can be any signal. A trigger happens when the signal changes
    *                        from non-positive to positive.
    * @param dur             the duration for which the ugens holds the value of the input signal when triggered
    */
   def kr(in: AnyGE, dur: AnyGE = 0.1f) = apply[control](control, in, dur)
}
/**
 * @param in              the trigger. This can be any signal. A trigger happens when the signal changes
 *                        from non-positive to positive.
 * @param dur             the duration for which the ugens holds the value of the input signal when triggered
 */
final case class Trig[R <: Rate](rate: R, in: AnyGE, dur: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _in = in.expand
      val _dur = dur.expand
      val _sz_in = _in.size
      val _sz_dur = _dur.size
      val _exp_ = maxInt(_sz_in, _sz_dur)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Trig", rate, IIdxSeq(_in(i.%(_sz_in)), _dur(i.%(_sz_dur)))))
   }
}
/**
 * A UGen that sends a value from the server to all notified clients upon receiving triggers.
 * The message sent is `OSCMessage( "/tr", <(Int) nodeID>, <(Int) trigID>, <(Float) value> )`.
 * 
 * For sending an array of values, or using an arbitrary reply command, see `SendReply`.
 * 
 * '''Warning''': The argument order is different from its sclang counterpart.
 * 
 * @see [[de.sciss.synth.ugen.SendReply]]
 */
object SendTrig {
   
   /**
    * @param trig            the trigger signal causing the value to be read and sent. A trigger occurs
    *                        when passing from non-positive to positive.
    * @param id              an arbitrary integer that will be sent along with the `"/tr"` message.
    *                        This is useful to distinguish between several SendTrig instances per SynthDef.
    * @param value           a changing signal or constant that will be polled at the time of trigger,
    *                        and its value passed with the trigger message
    */
   def ar(trig: GE[audio], id: AnyGE = 0.0f, value: AnyGE = 0.0f) = apply[audio](audio, trig, id, value)
   /**
    * @param trig            the trigger signal causing the value to be read and sent. A trigger occurs
    *                        when passing from non-positive to positive.
    * @param id              an arbitrary integer that will be sent along with the `"/tr"` message.
    *                        This is useful to distinguish between several SendTrig instances per SynthDef.
    * @param value           a changing signal or constant that will be polled at the time of trigger,
    *                        and its value passed with the trigger message
    */
   def kr(trig: GE[control], id: AnyGE = 0.0f, value: AnyGE = 0.0f) = apply[control](control, trig, id, value)
}
/**
 * A UGen that sends a value from the server to all notified clients upon receiving triggers.
 * The message sent is `OSCMessage( "/tr", <(Int) nodeID>, <(Int) trigID>, <(Float) value> )`.
 * 
 * For sending an array of values, or using an arbitrary reply command, see `SendReply`.
 * 
 * '''Warning''': The argument order is different from its sclang counterpart.
 * 
 * @param trig            the trigger signal causing the value to be read and sent. A trigger occurs
 *                        when passing from non-positive to positive.
 * @param id              an arbitrary integer that will be sent along with the `"/tr"` message.
 *                        This is useful to distinguish between several SendTrig instances per SynthDef.
 * @param value           a changing signal or constant that will be polled at the time of trigger,
 *                        and its value passed with the trigger message
 * 
 * @see [[de.sciss.synth.ugen.SendReply]]
 */
final case class SendTrig[R <: Rate](rate: R, trig: AnyGE, id: AnyGE, value: AnyGE) extends SingleOutUGenSource[R] with HasSideEffect {
   protected def expandUGens = {
      val _trig = trig.expand
      val _value = value.expand
      val _id = id.expand
      val _sz_trig = _trig.size
      val _sz_value = _value.size
      val _sz_id = _id.size
      val _exp_ = maxInt(_sz_trig, _sz_value, _sz_id)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("SendTrig", rate, IIdxSeq(_trig(i.%(_sz_trig)), _value(i.%(_sz_value)), _id(i.%(_sz_id)))))
   }
}
/**
 * A UGen which sends an sequence of values from the server to all notified clients upon receiving triggers.
 * The message sent is `OSCMessage( <(String) msgName>, <(Int) nodeID>, <(Int) replyID>, <(Float) values>* )`.
 * 
 * For sending a single value, `SendTrig` provides an alternative.
 * 
 * '''Warning''': The argument order is different from its sclang counterpart.
 * 
 * @see [[de.sciss.synth.ugen.SendTrig]]
 */
object SendReply {
   
   /**
    * @param trig            a non-positive to positive transition triggers a message
    * @param values          a graph element comprising the signal channels to be polled
    * @param msgName         a string specifying the OSCMessage's name. by convention, this should
    *                        start with a forward slash and contain only 7-bit ascii characters.
    * @param id              an integer identifier which is contained in the reply message. While you can
    *                        distinguish different `SendReply` instances from the same Synth by choosing different
    *                        OSCMessage names, depending on the application you may use the same message name but
    *                        different ids (similar to `SendTrig`).
    */
   def kr(trig: GE[control], values: Multi[AnyGE], msgName: String = "/reply", id: AnyGE = 0.0f) = apply[control](control, trig, values, msgName, id)
   /**
    * @param trig            a non-positive to positive transition triggers a message
    * @param values          a graph element comprising the signal channels to be polled
    * @param msgName         a string specifying the OSCMessage's name. by convention, this should
    *                        start with a forward slash and contain only 7-bit ascii characters.
    * @param id              an integer identifier which is contained in the reply message. While you can
    *                        distinguish different `SendReply` instances from the same Synth by choosing different
    *                        OSCMessage names, depending on the application you may use the same message name but
    *                        different ids (similar to `SendTrig`).
    */
   def ar(trig: GE[audio], values: Multi[AnyGE], msgName: String = "/reply", id: AnyGE = 0.0f) = apply[audio](audio, trig, values, msgName, id)
}
/**
 * A UGen which sends an sequence of values from the server to all notified clients upon receiving triggers.
 * The message sent is `OSCMessage( <(String) msgName>, <(Int) nodeID>, <(Int) replyID>, <(Float) values>* )`.
 * 
 * For sending a single value, `SendTrig` provides an alternative.
 * 
 * '''Warning''': The argument order is different from its sclang counterpart.
 * 
 * @param trig            a non-positive to positive transition triggers a message
 * @param values          a graph element comprising the signal channels to be polled
 * @param msgName         a string specifying the OSCMessage's name. by convention, this should
 *                        start with a forward slash and contain only 7-bit ascii characters.
 * @param id              an integer identifier which is contained in the reply message. While you can
 *                        distinguish different `SendReply` instances from the same Synth by choosing different
 *                        OSCMessage names, depending on the application you may use the same message name but
 *                        different ids (similar to `SendTrig`).
 * 
 * @see [[de.sciss.synth.ugen.SendTrig]]
 */
final case class SendReply[R <: Rate](rate: R, trig: AnyGE, values: Multi[AnyGE], msgName: String, id: AnyGE) extends SingleOutUGenSource[R] with HasSideEffect {
   protected def expandUGens = {
      val _trig = trig.expand
      val _id = id.expand
      val _msgName = stringArg(msgName)
      val _values = values.mexpand
      val _sz_trig = _trig.size
      val _sz_id = _id.size
      val _sz_values = _values.size
      val _exp_ = maxInt(_sz_trig, _sz_id, _sz_values)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("SendReply", rate, IIdxSeq(_trig(i.%(_sz_trig)), _id(i.%(_sz_id))).++(_msgName.++(_values(i.%(_sz_values)).expand))))
   }
}
/**
 * A UGen for printing the current output value of its input to the console.
 * 
 * @see [[de.sciss.synth.ugen.SendTrig]]
 */
object Poll {
   
   /**
    * @param trig            a non-positive to positive transition telling Poll to return a value
    * @param in              the signal you want to poll
    * @param label           a string or symbol to be printed with the polled value
    * @param trigID          if greater then 0, a `"/tr"` OSC message is sent back to the client
    *                        (similar to `SendTrig`)
    */
   def ar(trig: GE[audio], in: AnyGE, label: String = "poll", trigID: AnyGE = -1.0f) = apply[audio](audio, trig, in, label, trigID)
   /**
    * @param trig            a non-positive to positive transition telling Poll to return a value
    * @param in              the signal you want to poll
    * @param label           a string or symbol to be printed with the polled value
    * @param trigID          if greater then 0, a `"/tr"` OSC message is sent back to the client
    *                        (similar to `SendTrig`)
    */
   def kr(trig: GE[control], in: AnyGE, label: String = "poll", trigID: AnyGE = -1.0f) = apply[control](control, trig, in, label, trigID)
}
/**
 * A UGen for printing the current output value of its input to the console.
 * 
 * @param trig            a non-positive to positive transition telling Poll to return a value
 * @param in              the signal you want to poll
 * @param label           a string or symbol to be printed with the polled value
 * @param trigID          if greater then 0, a `"/tr"` OSC message is sent back to the client
 *                        (similar to `SendTrig`)
 * 
 * @see [[de.sciss.synth.ugen.SendTrig]]
 */
final case class Poll[R <: Rate](rate: R, trig: AnyGE, in: AnyGE, label: String, trigID: AnyGE) extends SingleOutUGenSource[R] with HasSideEffect {
   protected def expandUGens = {
      val _trig = trig.expand
      val _in = in.expand
      val _trigID = trigID.expand
      val _label = stringArg(label)
      val _sz_trig = _trig.size
      val _sz_in = _in.size
      val _sz_trigID = _trigID.size
      val _exp_ = maxInt(_sz_trig, _sz_in, _sz_trigID)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Poll", rate, IIdxSeq(_trig(i.%(_sz_trig)), _in(i.%(_sz_in)), _trigID(i.%(_sz_trigID))).++(_label)))
   }
}
/**
 * A UGen that toggles like a flip-flop between zero and one upon receiving a trigger.
 * The flip-flop is initially outputing zero, so changes to one when the first trigger
 * arrives.
 */
object ToggleFF {
   
   /**
    * @param trig            a signal to trigger the flip-flop. a trigger occurs when the signal
    *                        changes from non-positive to positive.
    */
   def kr(trig: AnyGE) = apply[control](control, trig)
   /**
    * @param trig            a signal to trigger the flip-flop. a trigger occurs when the signal
    *                        changes from non-positive to positive.
    */
   def ar(trig: AnyGE) = apply[audio](audio, trig)
}
/**
 * A UGen that toggles like a flip-flop between zero and one upon receiving a trigger.
 * The flip-flop is initially outputing zero, so changes to one when the first trigger
 * arrives.
 * 
 * @param trig            a signal to trigger the flip-flop. a trigger occurs when the signal
 *                        changes from non-positive to positive.
 */
final case class ToggleFF[R <: Rate](rate: R, trig: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _trig = trig.expand
      IIdxSeq.tabulate(_trig.size)(i => new SingleOutUGen("ToggleFF", rate, IIdxSeq(_trig(i))))
   }
}
object SetResetFF {
   def kr(trig: AnyGE, reset: AnyGE) = apply[control](control, trig, reset)
   def ar(trig: AnyGE, reset: AnyGE) = apply[audio](audio, trig, reset)
}
final case class SetResetFF[R <: Rate](rate: R, trig: AnyGE, reset: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _trig = trig.expand
      val _reset = reset.expand
      val _sz_trig = _trig.size
      val _sz_reset = _reset.size
      val _exp_ = maxInt(_sz_trig, _sz_reset)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("SetResetFF", rate, IIdxSeq(_trig(i.%(_sz_trig)), _reset(i.%(_sz_reset)))))
   }
}
/**
 * A sample-and-hold UGen. When triggered, a new value is taken from the input and
 * hold until the next trigger occurs.
 * 
 * @see [[de.sciss.synth.ugen.Gate]]
 * @see [[de.sciss.synth.ugen.Demand]]
 */
object Latch {
   
   /**
    * @param in              the input signal
    * @param trig            the trigger. The can be any signal. A trigger happens when the signal changes from
    *                        non-positive to positive.
    */
   def kr(in: AnyGE, trig: AnyGE) = apply[control](control, in, trig)
   /**
    * @param in              the input signal
    * @param trig            the trigger. The can be any signal. A trigger happens when the signal changes from
    *                        non-positive to positive.
    */
   def ar(in: AnyGE, trig: AnyGE) = apply[audio](audio, in, trig)
}
/**
 * A sample-and-hold UGen. When triggered, a new value is taken from the input and
 * hold until the next trigger occurs.
 * 
 * @param in              the input signal
 * @param trig            the trigger. The can be any signal. A trigger happens when the signal changes from
 *                        non-positive to positive.
 * 
 * @see [[de.sciss.synth.ugen.Gate]]
 * @see [[de.sciss.synth.ugen.Demand]]
 */
final case class Latch[R <: Rate](rate: R, in: AnyGE, trig: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _in = in.expand
      val _trig = trig.expand
      val _sz_in = _in.size
      val _sz_trig = _trig.size
      val _exp_ = maxInt(_sz_in, _sz_trig)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Latch", rate, IIdxSeq(_in(i.%(_sz_in)), _trig(i.%(_sz_trig)))))
   }
}
/**
 * A gate or hold UGen.
 * It allows the input signal value to pass when the `gate` argument is positive,
 * otherwise it holds last value.
 * 
 * @see [[de.sciss.synth.ugen.Latch]]
 */
object Gate {
   
   /**
    * @param in              the input signal to gate
    * @param gate            the signal specifying whether to pass the input signal (when greater than zero) or
    *                        whether to close the gate and hold the last value (when less than or equal to zero)
    */
   def kr(in: AnyGE, gate: AnyGE) = apply[control](control, in, gate)
   /**
    * @param in              the input signal to gate
    * @param gate            the signal specifying whether to pass the input signal (when greater than zero) or
    *                        whether to close the gate and hold the last value (when less than or equal to zero)
    */
   def ar(in: AnyGE, gate: AnyGE) = apply[audio](audio, in, gate)
}
/**
 * A gate or hold UGen.
 * It allows the input signal value to pass when the `gate` argument is positive,
 * otherwise it holds last value.
 * 
 * @param in              the input signal to gate
 * @param gate            the signal specifying whether to pass the input signal (when greater than zero) or
 *                        whether to close the gate and hold the last value (when less than or equal to zero)
 * 
 * @see [[de.sciss.synth.ugen.Latch]]
 */
final case class Gate[R <: Rate](rate: R, in: AnyGE, gate: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _in = in.expand
      val _gate = gate.expand
      val _sz_in = _in.size
      val _sz_gate = _gate.size
      val _exp_ = maxInt(_sz_in, _sz_gate)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Gate", rate, IIdxSeq(_in(i.%(_sz_in)), _gate(i.%(_sz_gate)))))
   }
}
/**
 * A Schmidt trigger UGen. Initially it outputs zero. When the input signal rises above `hi`,
 * its output switches to 1.0, which is hold until the signal falls below `lo`, switching the
 * output again to 0.0. The produces a kind of hysteresis behavior, preventing heavy
 * oscillations in a noisy system which might occur with a single-threshold trigger.
 */
object Schmidt {
   
   /**
    * @param lo              The low threshold.
    * @param hi              The high threshold.
    */
   def kr(in: AnyGE, lo: AnyGE = 0.0f, hi: AnyGE = 1.0f) = apply[control](control, in, lo, hi)
   /**
    * @param lo              The low threshold.
    * @param hi              The high threshold.
    */
   def ar(in: AnyGE, lo: AnyGE = 0.0f, hi: AnyGE = 1.0f) = apply[audio](audio, in, lo, hi)
}
/**
 * A Schmidt trigger UGen. Initially it outputs zero. When the input signal rises above `hi`,
 * its output switches to 1.0, which is hold until the signal falls below `lo`, switching the
 * output again to 0.0. The produces a kind of hysteresis behavior, preventing heavy
 * oscillations in a noisy system which might occur with a single-threshold trigger.
 * 
 * @param lo              The low threshold.
 * @param hi              The high threshold.
 */
final case class Schmidt[R <: Rate](rate: R, in: AnyGE, lo: AnyGE, hi: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _in = in.expand
      val _lo = lo.expand
      val _hi = hi.expand
      val _sz_in = _in.size
      val _sz_lo = _lo.size
      val _sz_hi = _hi.size
      val _exp_ = maxInt(_sz_in, _sz_lo, _sz_hi)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Schmidt", rate, IIdxSeq(_in(i.%(_sz_in)), _lo(i.%(_sz_lo)), _hi(i.%(_sz_hi)))))
   }
}
object PulseDivider {
   def kr(trig: AnyGE, div: AnyGE = 2.0f, start: AnyGE = 0.0f) = apply[control](control, trig, div, start)
   def ar(trig: AnyGE, div: AnyGE = 2.0f, start: AnyGE = 0.0f) = apply[audio](audio, trig, div, start)
}
final case class PulseDivider[R <: Rate](rate: R, trig: AnyGE, div: AnyGE, start: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _trig = trig.expand
      val _div = div.expand
      val _start = start.expand
      val _sz_trig = _trig.size
      val _sz_div = _div.size
      val _sz_start = _start.size
      val _exp_ = maxInt(_sz_trig, _sz_div, _sz_start)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("PulseDivider", rate, IIdxSeq(_trig(i.%(_sz_trig)), _div(i.%(_sz_div)), _start(i.%(_sz_start)))))
   }
}
object PulseCount {
   def kr(trig: AnyGE, reset: AnyGE = 0.0f) = apply[control](control, trig, reset)
   def ar(trig: AnyGE, reset: AnyGE = 0.0f) = apply[audio](audio, trig, reset)
}
final case class PulseCount[R <: Rate](rate: R, trig: AnyGE, reset: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _trig = trig.expand
      val _reset = reset.expand
      val _sz_trig = _trig.size
      val _sz_reset = _reset.size
      val _exp_ = maxInt(_sz_trig, _sz_reset)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("PulseCount", rate, IIdxSeq(_trig(i.%(_sz_trig)), _reset(i.%(_sz_reset)))))
   }
}
object Stepper {
   def kr(trig: AnyGE, reset: AnyGE = 0.0f, lo: AnyGE = 0.0f, hi: AnyGE = 7.0f, step: AnyGE = 1.0f, resetVal: AnyGE = 0.0f) = apply[control](control, trig, reset, lo, hi, step, resetVal)
   def ar(trig: AnyGE, reset: AnyGE = 0.0f, lo: AnyGE = 0.0f, hi: AnyGE = 7.0f, step: AnyGE = 1.0f, resetVal: AnyGE = 0.0f) = apply[audio](audio, trig, reset, lo, hi, step, resetVal)
}
final case class Stepper[R <: Rate](rate: R, trig: AnyGE, reset: AnyGE, lo: AnyGE, hi: AnyGE, step: AnyGE, resetVal: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _trig = trig.expand
      val _reset = reset.expand
      val _lo = lo.expand
      val _hi = hi.expand
      val _step = step.expand
      val _resetVal = resetVal.expand
      val _sz_trig = _trig.size
      val _sz_reset = _reset.size
      val _sz_lo = _lo.size
      val _sz_hi = _hi.size
      val _sz_step = _step.size
      val _sz_resetVal = _resetVal.size
      val _exp_ = maxInt(_sz_trig, _sz_reset, _sz_lo, _sz_hi, _sz_step, _sz_resetVal)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Stepper", rate, IIdxSeq(_trig(i.%(_sz_trig)), _reset(i.%(_sz_reset)), _lo(i.%(_sz_lo)), _hi(i.%(_sz_hi)), _step(i.%(_sz_step)), _resetVal(i.%(_sz_resetVal)))))
   }
}
/**
 * A delay UGen for trigger signals. Other than a normal buffer delay,
 * any new trigger arriving in the time between the previous trigger
 * and the passing of the delay time is ignored.
 */
object TDelay {
   
   /**
    * @param trig            The input trigger. A trigger is recognized when the signal passes from
    *                        non-positive to positive. Note that, no matter what the amplitude of
    *                        the input trigger is, the UGen will output a delayed trigger of
    *                        amplitude 1.0.
    * @param dur             The delay time in seconds.
    */
   def kr(trig: AnyGE, dur: AnyGE = 0.1f) = apply[control](control, trig, dur)
   /**
    * @param trig            The input trigger. A trigger is recognized when the signal passes from
    *                        non-positive to positive. Note that, no matter what the amplitude of
    *                        the input trigger is, the UGen will output a delayed trigger of
    *                        amplitude 1.0.
    * @param dur             The delay time in seconds.
    */
   def ar(trig: AnyGE, dur: AnyGE = 0.1f) = apply[audio](audio, trig, dur)
}
/**
 * A delay UGen for trigger signals. Other than a normal buffer delay,
 * any new trigger arriving in the time between the previous trigger
 * and the passing of the delay time is ignored.
 * 
 * @param trig            The input trigger. A trigger is recognized when the signal passes from
 *                        non-positive to positive. Note that, no matter what the amplitude of
 *                        the input trigger is, the UGen will output a delayed trigger of
 *                        amplitude 1.0.
 * @param dur             The delay time in seconds.
 */
final case class TDelay[R <: Rate](rate: R, trig: AnyGE, dur: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _trig = trig.expand
      val _dur = dur.expand
      val _sz_trig = _trig.size
      val _sz_dur = _dur.size
      val _exp_ = maxInt(_sz_trig, _sz_dur)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("TDelay", rate, IIdxSeq(_trig(i.%(_sz_trig)), _dur(i.%(_sz_dur)))))
   }
}
object ZeroCrossing {
   def kr(in: AnyGE) = apply[control](control, in)
   def ar(in: AnyGE) = apply[audio](audio, in)
}
final case class ZeroCrossing[R <: Rate](rate: R, in: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _in = in.expand
      IIdxSeq.tabulate(_in.size)(i => new SingleOutUGen("ZeroCrossing", rate, IIdxSeq(_in(i))))
   }
}
/**
 * A UGen that returns time since last triggered.
 * The time returned is in seconds and is measured from the last received trigger.
 * Note that currently it seems the initial memory is at -1 sample, so for
 * `Impulse.ar(1)` the result (at 44.1 kHz) is 2.26757e-05, followed strangely
 * by 1.00002, and then (as expected) 1.0.
 */
object Timer {
   
   /**
    * @param trig            the trigger to update the output signal.
    *                        A trigger occurs when trig signal crosses from non-positive to positive.
    */
   def kr(trig: AnyGE) = apply[control](control, trig)
   /**
    * @param trig            the trigger to update the output signal.
    *                        A trigger occurs when trig signal crosses from non-positive to positive.
    */
   def ar(trig: AnyGE) = apply[audio](audio, trig)
}
/**
 * A UGen that returns time since last triggered.
 * The time returned is in seconds and is measured from the last received trigger.
 * Note that currently it seems the initial memory is at -1 sample, so for
 * `Impulse.ar(1)` the result (at 44.1 kHz) is 2.26757e-05, followed strangely
 * by 1.00002, and then (as expected) 1.0.
 * 
 * @param trig            the trigger to update the output signal.
 *                        A trigger occurs when trig signal crosses from non-positive to positive.
 */
final case class Timer[R <: Rate](rate: R, trig: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _trig = trig.expand
      IIdxSeq.tabulate(_trig.size)(i => new SingleOutUGen("Timer", rate, IIdxSeq(_trig(i))))
   }
}
/**
 * A UGen which starts a linear raise from zero each time it is
 * triggered.
 * 
 * @see [[de.sciss.synth.ugen.Ramp]]
 * @see [[de.sciss.synth.ugen.Phasor]]
 * @see [[de.sciss.synth.ugen.Line]]
 */
object Sweep {
   
   /**
    * @param trig            the trigger that restarts the ramp, when passing from
    *                        non-positive to positive
    * @param speed           the amount of increment of the output signal per second.
    *                        In SCLang this argument is named `rate`, while ScalaCollider uses
    *                        `speed` to avoid conflict with the UGen's calculation rate.
    */
   def kr(trig: AnyGE, speed: AnyGE) = apply[control](control, trig, speed)
   /**
    * @param trig            the trigger that restarts the ramp, when passing from
    *                        non-positive to positive
    * @param speed           the amount of increment of the output signal per second.
    *                        In SCLang this argument is named `rate`, while ScalaCollider uses
    *                        `speed` to avoid conflict with the UGen's calculation rate.
    */
   def ar(trig: AnyGE, speed: AnyGE) = apply[audio](audio, trig, speed)
}
/**
 * A UGen which starts a linear raise from zero each time it is
 * triggered.
 * 
 * @param trig            the trigger that restarts the ramp, when passing from
 *                        non-positive to positive
 * @param speed           the amount of increment of the output signal per second.
 *                        In SCLang this argument is named `rate`, while ScalaCollider uses
 *                        `speed` to avoid conflict with the UGen's calculation rate.
 * 
 * @see [[de.sciss.synth.ugen.Ramp]]
 * @see [[de.sciss.synth.ugen.Phasor]]
 * @see [[de.sciss.synth.ugen.Line]]
 */
final case class Sweep[R <: Rate](rate: R, trig: AnyGE, speed: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _trig = trig.expand
      val _speed = speed.expand
      val _sz_trig = _trig.size
      val _sz_speed = _speed.size
      val _exp_ = maxInt(_sz_trig, _sz_speed)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Sweep", rate, IIdxSeq(_trig(i.%(_sz_trig)), _speed(i.%(_sz_speed)))))
   }
}
object Phasor {
   
   /**
    * @param trig            Warning: SC 3.4 has a bug where an initial trig value of 1 will
    *                        be ignored (you need to feed it zero first)
    */
   def kr(trig: AnyGE, speed: AnyGE = 1.0f, lo: AnyGE = 0.0f, hi: AnyGE = 1.0f, resetVal: AnyGE = 0.0f) = apply[control](control, trig, speed, lo, hi, resetVal)
   /**
    * @param trig            Warning: SC 3.4 has a bug where an initial trig value of 1 will
    *                        be ignored (you need to feed it zero first)
    */
   def ar(trig: AnyGE, speed: AnyGE = 1.0f, lo: AnyGE = 0.0f, hi: AnyGE = 1.0f, resetVal: AnyGE = 0.0f) = apply[audio](audio, trig, speed, lo, hi, resetVal)
}
/**
 * @param trig            Warning: SC 3.4 has a bug where an initial trig value of 1 will
 *                        be ignored (you need to feed it zero first)
 */
final case class Phasor[R <: Rate](rate: R, trig: AnyGE, speed: AnyGE, lo: AnyGE, hi: AnyGE, resetVal: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _trig = trig.expand
      val _speed = speed.expand
      val _lo = lo.expand
      val _hi = hi.expand
      val _resetVal = resetVal.expand
      val _sz_trig = _trig.size
      val _sz_speed = _speed.size
      val _sz_lo = _lo.size
      val _sz_hi = _hi.size
      val _sz_resetVal = _resetVal.size
      val _exp_ = maxInt(_sz_trig, _sz_speed, _sz_lo, _sz_hi, _sz_resetVal)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Phasor", rate, IIdxSeq(_trig(i.%(_sz_trig)), _speed(i.%(_sz_speed)), _lo(i.%(_sz_lo)), _hi(i.%(_sz_hi)), _resetVal(i.%(_sz_resetVal)))))
   }
}
object Peak {
   def kr(in: AnyGE, trig: AnyGE) = apply[control](control, in, trig)
   def ar(in: AnyGE, trig: AnyGE) = apply[audio](audio, in, trig)
}
final case class Peak[R <: Rate](rate: R, in: AnyGE, trig: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _in = in.expand
      val _trig = trig.expand
      val _sz_in = _in.size
      val _sz_trig = _trig.size
      val _exp_ = maxInt(_sz_in, _sz_trig)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Peak", rate, IIdxSeq(_in(i.%(_sz_in)), _trig(i.%(_sz_trig)))))
   }
}
object RunningMin {
   def kr(in: AnyGE, trig: AnyGE) = apply[control](control, in, trig)
   def ar(in: AnyGE, trig: AnyGE) = apply[audio](audio, in, trig)
}
final case class RunningMin[R <: Rate](rate: R, in: AnyGE, trig: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _in = in.expand
      val _trig = trig.expand
      val _sz_in = _in.size
      val _sz_trig = _trig.size
      val _exp_ = maxInt(_sz_in, _sz_trig)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("RunningMin", rate, IIdxSeq(_in(i.%(_sz_in)), _trig(i.%(_sz_trig)))))
   }
}
object RunningMax {
   def kr(in: AnyGE, trig: AnyGE) = apply[control](control, in, trig)
   def ar(in: AnyGE, trig: AnyGE) = apply[audio](audio, in, trig)
}
final case class RunningMax[R <: Rate](rate: R, in: AnyGE, trig: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _in = in.expand
      val _trig = trig.expand
      val _sz_in = _in.size
      val _sz_trig = _trig.size
      val _exp_ = maxInt(_sz_in, _sz_trig)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("RunningMax", rate, IIdxSeq(_in(i.%(_sz_in)), _trig(i.%(_sz_trig)))))
   }
}
object PeakFollower {
   def kr(in: AnyGE, decay: AnyGE = 0.999f) = apply[control](control, in, decay)
   def ar(in: AnyGE, decay: AnyGE = 0.999f) = apply[audio](audio, in, decay)
}
final case class PeakFollower[R <: Rate](rate: R, in: AnyGE, decay: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _in = in.expand
      val _decay = decay.expand
      val _sz_in = _in.size
      val _sz_decay = _decay.size
      val _exp_ = maxInt(_sz_in, _sz_decay)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("PeakFollower", rate, IIdxSeq(_in(i.%(_sz_in)), _decay(i.%(_sz_decay)))))
   }
}
object MostChange {
   def kr(a: AnyGE, b: AnyGE) = apply[control](control, a, b)
   def ar(a: AnyGE, b: AnyGE) = apply[audio](audio, a, b)
}
final case class MostChange[R <: Rate](rate: R, a: AnyGE, b: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _a = a.expand
      val _b = b.expand
      val _sz_a = _a.size
      val _sz_b = _b.size
      val _exp_ = maxInt(_sz_a, _sz_b)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("MostChange", rate, IIdxSeq(_a(i.%(_sz_a)), _b(i.%(_sz_b)))))
   }
}
object LeastChange {
   def kr(a: AnyGE, b: AnyGE) = apply[control](control, a, b)
   def ar(a: AnyGE, b: AnyGE) = apply[audio](audio, a, b)
}
final case class LeastChange[R <: Rate](rate: R, a: AnyGE, b: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _a = a.expand
      val _b = b.expand
      val _sz_a = _a.size
      val _sz_b = _b.size
      val _exp_ = maxInt(_sz_a, _sz_b)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("LeastChange", rate, IIdxSeq(_a(i.%(_sz_a)), _b(i.%(_sz_b)))))
   }
}
object LastValue {
   def kr(in: AnyGE, thresh: AnyGE = 0.01f) = apply[control](control, in, thresh)
   def ar(in: AnyGE, thresh: AnyGE = 0.01f) = apply[audio](audio, in, thresh)
}
final case class LastValue[R <: Rate](rate: R, in: AnyGE, thresh: AnyGE) extends SingleOutUGenSource[R] {
   protected def expandUGens = {
      val _in = in.expand
      val _thresh = thresh.expand
      val _sz_in = _in.size
      val _sz_thresh = _thresh.size
      val _exp_ = maxInt(_sz_in, _sz_thresh)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("LastValue", rate, IIdxSeq(_in(i.%(_sz_in)), _thresh(i.%(_sz_thresh)))))
   }
}
/**
 * A UGen which monitors another UGen to see when it is finished.
 * Some UGens, such as `PlayBuf`, `RecordBuf`, `Line`, `XLine`, `EnvGen`, `Linen`, `BufRd`, `BufWr`, `DbufRd`,
 * and the Buffer delay UGens set a 'done' flag when they are finished playing. This UGen echoes that flag
 * as an explicit output signal when it is set to track a particular UGen. When the tracked UGen changes
 * to done, the output signal changes from zero to one.
 * 
 * @see [[de.sciss.synth.ugen.PlayBuf]]
 * @see [[de.sciss.synth.ugen.Line]]
 * @see [[de.sciss.synth.ugen.EnvGen]]
 */
object Done {
   
   /**
    * @param src             the UGen to track
    */
   def kr(src: AnyGE with HasDoneFlag) = apply(src)
}
/**
 * A UGen which monitors another UGen to see when it is finished.
 * Some UGens, such as `PlayBuf`, `RecordBuf`, `Line`, `XLine`, `EnvGen`, `Linen`, `BufRd`, `BufWr`, `DbufRd`,
 * and the Buffer delay UGens set a 'done' flag when they are finished playing. This UGen echoes that flag
 * as an explicit output signal when it is set to track a particular UGen. When the tracked UGen changes
 * to done, the output signal changes from zero to one.
 * 
 * @param src             the UGen to track
 * 
 * @see [[de.sciss.synth.ugen.PlayBuf]]
 * @see [[de.sciss.synth.ugen.Line]]
 * @see [[de.sciss.synth.ugen.EnvGen]]
 */
final case class Done(src: AnyGE with HasDoneFlag) extends SingleOutUGenSource[control] with HasSideEffect {
   protected def expandUGens = {
      val _src = src.expand
      IIdxSeq.tabulate(_src.size)(i => new SingleOutUGen("Done", control, IIdxSeq(_src(i))))
   }
}
/**
 * A UGen which pauses and resumes another node.
 * Note that the UGen initially assumes the node is running, that is,
 * if `gate` is initially 1, this will '''not''' resume a paused node.
 * Instead, the gate must go to zero and back to one to resume the node.
 * Additionally, this UGen will only cause action if the gate value
 * changes, that is, if the node is paused or resumed otherwise, this
 * UGen will not interfere with that action, unless the gate value is
 * adjusted.
 * 
 * @see [[de.sciss.synth.ugen.Free]]
 * @see [[de.sciss.synth.ugen.PauseSelf]]
 */
object Pause {
   
   /**
    * @param gate            when 0, node is paused, when 1, node is resumed
    * @param node            the id of the node to be paused or resumed
    */
   def kr(gate: AnyGE, node: AnyGE) = apply(gate, node)
}
/**
 * A UGen which pauses and resumes another node.
 * Note that the UGen initially assumes the node is running, that is,
 * if `gate` is initially 1, this will '''not''' resume a paused node.
 * Instead, the gate must go to zero and back to one to resume the node.
 * Additionally, this UGen will only cause action if the gate value
 * changes, that is, if the node is paused or resumed otherwise, this
 * UGen will not interfere with that action, unless the gate value is
 * adjusted.
 * 
 * @param gate            when 0, node is paused, when 1, node is resumed
 * @param node            the id of the node to be paused or resumed
 * 
 * @see [[de.sciss.synth.ugen.Free]]
 * @see [[de.sciss.synth.ugen.PauseSelf]]
 */
final case class Pause(gate: AnyGE, node: AnyGE) extends SingleOutUGenSource[control] with HasSideEffect {
   protected def expandUGens = {
      val _gate = gate.expand
      val _node = node.expand
      val _sz_gate = _gate.size
      val _sz_node = _node.size
      val _exp_ = maxInt(_sz_gate, _sz_node)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Pause", control, IIdxSeq(_gate(i.%(_sz_gate)), _node(i.%(_sz_node)))))
   }
}
/**
 * A UGen that, when triggered, frees enclosing synth.
 * It frees the enclosing synth when the input signal crosses from non-positive to positive.
 * 
 * This UGen outputs its input signal for convenience.
 * 
 * @see [[de.sciss.synth.ugen.Free]]
 * @see [[de.sciss.synth.ugen.PauseSelf]]
 */
object FreeSelf {
   
   /**
    * @param trig            the input signal which will trigger the action.
    */
   def kr(trig: AnyGE) = apply(trig)
}
/**
 * A UGen that, when triggered, frees enclosing synth.
 * It frees the enclosing synth when the input signal crosses from non-positive to positive.
 * 
 * This UGen outputs its input signal for convenience.
 * 
 * @param trig            the input signal which will trigger the action.
 * 
 * @see [[de.sciss.synth.ugen.Free]]
 * @see [[de.sciss.synth.ugen.PauseSelf]]
 */
final case class FreeSelf(trig: AnyGE) extends SingleOutUGenSource[control] with HasSideEffect {
   protected def expandUGens = {
      val _trig = trig.expand
      IIdxSeq.tabulate(_trig.size)(i => new SingleOutUGen("FreeSelf", control, IIdxSeq(_trig(i))))
   }
}
/**
 * A UGen that, when triggered, pauses enclosing synth.
 * It pauses the enclosing synth when the input signal crosses from non-positive to positive.
 * 
 * This UGen outputs its input signal for convenience.
 * 
 * @see [[de.sciss.synth.ugen.Pause]]
 * @see [[de.sciss.synth.ugen.FreeSelf]]
 */
object PauseSelf {
   
   /**
    * @param trig            the input signal which will trigger the action.
    */
   def kr(trig: AnyGE) = apply(trig)
}
/**
 * A UGen that, when triggered, pauses enclosing synth.
 * It pauses the enclosing synth when the input signal crosses from non-positive to positive.
 * 
 * This UGen outputs its input signal for convenience.
 * 
 * @param trig            the input signal which will trigger the action.
 * 
 * @see [[de.sciss.synth.ugen.Pause]]
 * @see [[de.sciss.synth.ugen.FreeSelf]]
 */
final case class PauseSelf(trig: AnyGE) extends SingleOutUGenSource[control] with HasSideEffect {
   protected def expandUGens = {
      val _trig = trig.expand
      IIdxSeq.tabulate(_trig.size)(i => new SingleOutUGen("PauseSelf", control, IIdxSeq(_trig(i))))
   }
}
/**
 * A UGen that, when triggered, frees a given node.
 * 
 * This UGen outputs its trig input signal for convenience.
 * 
 * @see [[de.sciss.synth.ugen.Pause]]
 * @see [[de.sciss.synth.ugen.FreeSelf]]
 */
object Free {
   
   /**
    * @param trig            the trigger to cause the action
    * @param node            the id of the target node to free upon receiving the trigger
    */
   def kr(trig: AnyGE, node: AnyGE) = apply(trig, node)
}
/**
 * A UGen that, when triggered, frees a given node.
 * 
 * This UGen outputs its trig input signal for convenience.
 * 
 * @param trig            the trigger to cause the action
 * @param node            the id of the target node to free upon receiving the trigger
 * 
 * @see [[de.sciss.synth.ugen.Pause]]
 * @see [[de.sciss.synth.ugen.FreeSelf]]
 */
final case class Free(trig: AnyGE, node: AnyGE) extends SingleOutUGenSource[control] with HasSideEffect {
   protected def expandUGens = {
      val _trig = trig.expand
      val _node = node.expand
      val _sz_trig = _trig.size
      val _sz_node = _node.size
      val _exp_ = maxInt(_sz_trig, _sz_node)
      IIdxSeq.tabulate(_exp_)(i => new SingleOutUGen("Free", control, IIdxSeq(_trig(i.%(_sz_trig)), _node(i.%(_sz_node)))))
   }
}
/**
 * A UGen that, when its input UGen is finished, frees enclosing synth.
 * This is essentially a shortcut for `FreeSelf.kr( Done.kr( src ))`, so instead
 * of providing a trigger signal it reads directly the done flag of an
 * appropriate ugen (such as `Line` or `PlayBuf`).
 * 
 * This UGen outputs its input signal for convenience.
 * 
 * @see [[de.sciss.synth.ugen.Free]]
 * @see [[de.sciss.synth.ugen.FreeSelf]]
 * @see [[de.sciss.synth.ugen.PauseSelfWhenDone]]
 * @see [[de.sciss.synth.ugen.Done]]
 */
object FreeSelfWhenDone {
   
   /**
    * @param src             the input UGen which when finished will trigger the action.
    */
   def kr(src: AnyGE with HasDoneFlag) = apply(src)
}
/**
 * A UGen that, when its input UGen is finished, frees enclosing synth.
 * This is essentially a shortcut for `FreeSelf.kr( Done.kr( src ))`, so instead
 * of providing a trigger signal it reads directly the done flag of an
 * appropriate ugen (such as `Line` or `PlayBuf`).
 * 
 * This UGen outputs its input signal for convenience.
 * 
 * @param src             the input UGen which when finished will trigger the action.
 * 
 * @see [[de.sciss.synth.ugen.Free]]
 * @see [[de.sciss.synth.ugen.FreeSelf]]
 * @see [[de.sciss.synth.ugen.PauseSelfWhenDone]]
 * @see [[de.sciss.synth.ugen.Done]]
 */
final case class FreeSelfWhenDone(src: AnyGE with HasDoneFlag) extends SingleOutUGenSource[control] with HasSideEffect {
   protected def expandUGens = {
      val _src = src.expand
      IIdxSeq.tabulate(_src.size)(i => new SingleOutUGen("FreeSelfWhenDone", control, IIdxSeq(_src(i))))
   }
}
/**
 * A UGen that, when its input UGen is finished, pauses enclosing synth.
 * This is essentially a shortcut for `PauseSelf.kr( Done.kr( src ))`, so instead
 * of providing a trigger signal it reads directly the done flag of an
 * appropriate ugen (such as `Line` or `PlayBuf`).
 * 
 * This UGen outputs its input signal for convenience.
 * 
 * @see [[de.sciss.synth.ugen.Pause]]
 * @see [[de.sciss.synth.ugen.PauseSelf]]
 * @see [[de.sciss.synth.ugen.FreeSelfWhenDone]]
 * @see [[de.sciss.synth.ugen.Done]]
 */
object PauseSelfWhenDone {
   
   /**
    * @param src             the input UGen which when finished will trigger the action.
    */
   def kr(src: AnyGE with HasDoneFlag) = apply(src)
}
/**
 * A UGen that, when its input UGen is finished, pauses enclosing synth.
 * This is essentially a shortcut for `PauseSelf.kr( Done.kr( src ))`, so instead
 * of providing a trigger signal it reads directly the done flag of an
 * appropriate ugen (such as `Line` or `PlayBuf`).
 * 
 * This UGen outputs its input signal for convenience.
 * 
 * @param src             the input UGen which when finished will trigger the action.
 * 
 * @see [[de.sciss.synth.ugen.Pause]]
 * @see [[de.sciss.synth.ugen.PauseSelf]]
 * @see [[de.sciss.synth.ugen.FreeSelfWhenDone]]
 * @see [[de.sciss.synth.ugen.Done]]
 */
final case class PauseSelfWhenDone(src: AnyGE with HasDoneFlag) extends SingleOutUGenSource[control] with HasSideEffect {
   protected def expandUGens = {
      val _src = src.expand
      IIdxSeq.tabulate(_src.size)(i => new SingleOutUGen("PauseSelfWhenDone", control, IIdxSeq(_src(i))))
   }
}