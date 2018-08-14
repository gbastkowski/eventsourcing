package gbastkowski.kata.eventsourcing

import org.apache.logging.log4j.scala.Logging

trait EventSourced[ES <: EventSourced[ES, Event], Event] extends Logging {
  def applyEvent: Event ⇒ ES

  def unhandled(event: Event) = logger.error(s"event $event does not apply to $this")
}
