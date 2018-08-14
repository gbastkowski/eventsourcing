package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime

sealed trait InvoiceEvent

object InvoiceEvent {
  case class Created(invoiceId: Int) extends InvoiceEvent
  case class RecipientChanged(recipient: Boolean) extends InvoiceEvent
  case class ItemAdded(item: InvoiceItem) extends InvoiceEvent
  case class ItemRemoved(itemId: Int) extends InvoiceEvent
  case class Sent(date: LocalDateTime) extends InvoiceEvent
  case class Reminded(date: LocalDateTime) extends InvoiceEvent
  case class PaymentReceived(date: LocalDateTime) extends InvoiceEvent
}
