package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import scala.collection.mutable

import InvoiceEvent._

object Invoice extends AggregateFactory[Invoice, InvoiceEvent] {
  def create(invoiceId: Int) = DraftInvoice(Created(invoiceId) :: Nil, invoiceId)

  def applyEvent = {
    case e: Created ⇒ DraftInvoice(e :: Nil, e.invoiceId)
    case event ⇒ unhandled(event)
  }
}

sealed trait Invoice extends AggregateRoot[Invoice, InvoiceEvent]

case class DraftInvoice(
  uncommittedEvents: List[InvoiceEvent], 
  id: Int, 
  recipient: Boolean = false, 
  items: Map[Int, InvoiceItem] = Map()
) extends Invoice {
  def totalAmount: Int = items.values.map(_.amount).sum

  def changeRecipient(r: Boolean): DraftInvoice = applyRecipientChanged(RecipientChanged(r))
  private[this] def applyRecipientChanged(event: RecipientChanged) =
    copy(event :: uncommittedEvents, recipient = event.recipient)

  def addItem(item: InvoiceItem): DraftInvoice = applyItemAdded(ItemAdded(item))
  private[this] def applyItemAdded(event: ItemAdded) =
    copy(event :: uncommittedEvents, items = items + event.item.id → event.item)

  def removeItem(item: Int): DraftInvoice = applyItemRemoved(ItemRemoved(item))
  private[this] def applyItemRemoved(event: ItemRemoved) =
    copy(event :: uncommittedEvents, items = items - event.itemId)

  def send: SentInvoice = {
    require(readyToSend, "invoice not ready to send")
    applySent(Sent(LocalDateTime.now))
  }
  private[this] def applySent(event: Sent) = SentInvoice(event :: uncommittedEvents, id)
  private[this] def readyToSend = recipient == true && items.nonEmpty

  def markCommitted(): DraftInvoice = copy(uncommittedEvents = Nil)

  def applyEvent = {
    case e: Sent ⇒ applySent(e)
    case e: ItemAdded ⇒ applyItemAdded(e)
    case e: ItemRemoved ⇒ applyItemRemoved(e)
    case e: RecipientChanged ⇒ applyRecipientChanged(e)
    case event ⇒ unhandled(event)
  }
}

case class SentInvoice(
  uncommittedEvents: List[InvoiceEvent],
  id: Int,
  reminded: Boolean = false,
  paid: Boolean = false
) extends Invoice
{
  def remind(): Invoice = {
    applyEvent(Reminded(LocalDateTime.now))
  }

  def receivePayment(when: LocalDateTime): Invoice = {
    applyEvent(PaymentReceived(LocalDateTime.now))
  }

  def markCommitted(): SentInvoice = copy(uncommittedEvents = Nil)

  def applyEvent = {
    case e: Reminded ⇒ copy(e :: uncommittedEvents, reminded = true)
    case event ⇒ unhandled(event)
  }
}
