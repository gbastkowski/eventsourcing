package gbastkowski.kata.eventsourcing

trait EventSourced[ES <: EventSourced[ES, Event], Event] {
  def applyEvent: Event ⇒ ES

  def unhandled(event: Event): ES =
    throw new RuntimeException(s"event $event does not apply to $this")
}
