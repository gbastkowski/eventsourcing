package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import scala.collection.mutable

object Invoice extends AggregateFactory[Invoice, InvoiceEvent] {
  def create(invoiceId: Int) = applyEvent(InvoiceCreated(invoiceId))

  def applyEvent = {
    case e: InvoiceCreated ⇒ Invoice(e :: Nil, e.invoiceId)
    case event ⇒ unhandled(event)
  }
}

case class Invoice(
  uncommittedEvents: List[InvoiceEvent],
  id: Int,
  recipient: Boolean = false,
  items: Map[Int, InvoiceItem] = Map(),
  sent: Boolean = false,
  reminded: Boolean = false,
  paid: Boolean = false
) extends AggregateRoot[Invoice, InvoiceEvent] {

  def totalAmount: Int = items.values.map(_.amount).sum

  def changeRecipient(r: Boolean): Invoice = {
    require(!sent)
    applyEvent(InvoiceRecipientChanged(r))
  }

  def addItem(item: InvoiceItem): Invoice = {
    require(!sent)
    applyEvent(InvoiceItemAdded(item))
  }

  def addItems(items: InvoiceItem*): Invoice = items.foldLeft(this)(_ addItem _)

  def removeItem(item: Int): Invoice = {
    require(!sent)
    applyEvent(InvoiceItemRemoved(item))
  }

  def send(): Invoice = {
    require(!sent, "invoice already sent")
    // require(recipient, "cannot send invoice to unknown recipient")

    applyEvent(InvoiceSent(LocalDateTime.now))
  }

  def remind(): Invoice = {
    require(sent)
    applyEvent(InvoiceReminded(LocalDateTime.now))
  }

  def receivePayment(when: LocalDateTime): Invoice = {
    require(paid)
    applyEvent(InvoicePaymentReceived(LocalDateTime.now))
  }

  def markCommitted(): Invoice = copy(uncommittedEvents = Nil)

  def applyEvent = {
    case e: InvoiceSent ⇒ copy(e :: uncommittedEvents, sent = true)
    case e: InvoiceReminded ⇒ copy(e :: uncommittedEvents, reminded = true)
    case e: InvoiceItemAdded ⇒ copy(e :: uncommittedEvents, items = items + e.item.id → e.item)
    case e: InvoiceItemRemoved ⇒ copy(e :: uncommittedEvents, items = items - e.itemId)
    case e: InvoiceRecipientChanged ⇒ copy(e :: uncommittedEvents, recipient = e.recipient)
    case event ⇒ unhandled(event)
  }

}
