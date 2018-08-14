package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import scala.collection.mutable

object Invoice extends AggregateFactory[Invoice, InvoiceEvent] {
  import InvoiceEvent.Created
  def create(invoiceId: Int) = applyEvent(Created(invoiceId))

  def applyEvent = {
    case e: Created ⇒ Invoice(e :: Nil, e.invoiceId)
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
  import InvoiceEvent._

  def totalAmount: Int = items.values.map(_.amount).sum

  def changeRecipient(r: Boolean): Invoice = {
    require(!sent)
    applyEvent(RecipientChanged(r))
  }

  def addItem(item: InvoiceItem): Invoice = {
    require(!sent)
    applyEvent(ItemAdded(item))
  }

  def addItems(items: InvoiceItem*): Invoice = items.foldLeft(this)(_ addItem _)

  def removeItem(item: Int): Invoice = {
    require(!sent)
    applyEvent(ItemRemoved(item))
  }

  def send(): Invoice = {
    require(!sent, "invoice already sent")
    // require(recipient, "cannot send invoice to unknown recipient")

    applyEvent(Sent(LocalDateTime.now))
  }

  def remind(): Invoice = {
    require(sent)
    applyEvent(Reminded(LocalDateTime.now))
  }

  def receivePayment(when: LocalDateTime): Invoice = {
    require(paid)
    applyEvent(PaymentReceived(LocalDateTime.now))
  }

  def markCommitted(): Invoice = copy(uncommittedEvents = Nil)

  def applyEvent = {
    case e: Sent ⇒ copy(e :: uncommittedEvents, sent = true)
    case e: Reminded ⇒ copy(e :: uncommittedEvents, reminded = true)
    case e: ItemAdded ⇒ copy(e :: uncommittedEvents, items = items + e.item.id → e.item)
    case e: ItemRemoved ⇒ copy(e :: uncommittedEvents, items = items - e.itemId)
    case e: RecipientChanged ⇒ copy(e :: uncommittedEvents, recipient = e.recipient)
    case event ⇒ unhandled(event)
  }

}
