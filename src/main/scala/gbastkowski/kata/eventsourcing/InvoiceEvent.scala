package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime

sealed trait InvoiceEvent

case class InvoiceCreated(invoiceId: Int) extends InvoiceEvent
case class InvoiceRecipientChanged(recipient: Option[String]) extends InvoiceEvent
case class InvoiceItemAdded(item: InvoiceItem) extends InvoiceEvent
case class InvoiceItemRemoved(itemId: Int) extends InvoiceEvent
case class InvoiceSent(date: LocalDateTime) extends InvoiceEvent
case class InvoiceReminded(date: LocalDateTime) extends InvoiceEvent
case class InvoicePaymentReceived(date: LocalDateTime) extends InvoiceEvent
