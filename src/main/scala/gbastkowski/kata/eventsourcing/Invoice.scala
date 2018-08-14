package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import scala.collection.mutable

class Invoice extends AggregateRoot[InvoiceEvent] {

  var id: Int = _
  var recipient: Option[String] = None
  var items: Map[Int, InvoiceItem] = Map()
  var sent: Boolean = false
  var reminded: Boolean = false
  var paid: Boolean = false

  def totalAmount: Int = items.values.map(_.amount).sum

  def changeRecipient(r: Option[String]): Unit = {
    require(!sent)
    record(InvoiceRecipientChanged(r))
  }

  def addItem(item: InvoiceItem): Unit = {
    require(!sent)
    record(InvoiceItemAdded(item))
  }

  def removeItem(item: Int): Unit = {
    require(!sent)
    record(InvoiceItemRemoved(item))
  }

  def send(): Unit = {
    record(InvoiceSent(LocalDateTime.now))
  }

  def remind(): Unit = {
    require(sent)
    record(InvoiceReminded(LocalDateTime.now))
  }

  def receivePayment(when: LocalDateTime): Unit = {
    require(paid)
    record(InvoicePaymentReceived(LocalDateTime.now))
  }

  protected val applyEvent: InvoiceEvent ⇒ Unit = {
    case InvoiceCreated(invoiceId) ⇒ Unit
    case InvoiceItemAdded(item) ⇒ items += item.id → item
    case InvoiceItemRemoved(itemId) ⇒ items -= itemId
    case InvoiceRecipientChanged(r) ⇒ recipient = r
    case InvoiceSent(d) ⇒ sent = true
    case InvoiceReminded(d) ⇒ reminded = true
    case InvoicePaymentReceived(d) ⇒ paid = true
  }
}
