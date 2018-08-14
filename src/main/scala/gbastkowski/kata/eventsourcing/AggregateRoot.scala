package gbastkowski.kata.eventsourcing

import scala.collection.mutable

trait AggregateRoot[T] {
  private[this] val uncommited = mutable.Queue[T]()

  def uncommittedEvents: Iterable[T] = uncommited

  def markCommitted(): Unit = uncommited.clear()

  def loadFromHistory(history: Iterable[T]): Unit = history foreach applyEvent

  protected val applyEvent: T ⇒ Unit

  protected def record(e: T): Unit = {
    applyEvent(e)
    uncommited += e
  }
}
