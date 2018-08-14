package gbastkowski.kata.eventsourcing

sealed trait InvoiceEvent {
  val invoiceId: Int
}

case class InvoiceCreated(invoiceId: Int) extends InvoiceEvent
case class InvoiceRecipientChanged(invoiceId: Int) extends InvoiceEvent
