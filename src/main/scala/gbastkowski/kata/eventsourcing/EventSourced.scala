package gbastkowski.kata.eventsourcing

trait EventSourced[Source <: EventSourced[Source, Event], Event] {
  def applyEvent: Event ⇒ Source

  def unhandled(event: Event): Source = throw new RuntimeException(s"event $event does not apply to $this")
}
