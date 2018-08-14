package gbastkowski.kata.eventsourcing

trait AggregateFactory[AR <: AggregateRoot[AR, Event], Event] extends EventSourced[AR, Event] {
  def loadFromHistory[T <: AR](history: Iterable[Event]): T = {
    history.tail.
      foldLeft(applyEvent(history.head)) { _ applyEvent _ }.
      asInstanceOf[AR].
      markCommitted.asInstanceOf[T]
  }
}
