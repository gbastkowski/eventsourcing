package gbastkowski.kata.eventsourcing

trait AggregateFactory[AR <: AggregateRoot[AR, Event], Event] extends EventSourced[AR, Event] {
  def loadFromHistory(history: Iterable[Event]): AR = {
    history.tail.
      foldLeft(applyEvent(history.head)) { _ applyEvent _ }.
      markCommitted()
  }
}
