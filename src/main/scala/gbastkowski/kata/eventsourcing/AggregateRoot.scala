package gbastkowski.kata.eventsourcing

import scala.collection.mutable

trait AggregateRoot[AR <: AggregateRoot[AR, Event], Event] extends EventSourced[AR, Event]{
  def uncommittedEvents: List[Event]

  def markCommitted: AR
}
