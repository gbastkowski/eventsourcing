package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import scala.collection.mutable

class Invoice extends AggregateRoot[InvoiceEvent] {

  var id: Int = _
  var recipient: Option[String] = None
  var items: Map[Int, InvoiceItem] = Map()
  var sent: Option[LocalDateTime] = None
  var reminded: Option[LocalDateTime] = None
  var paid: Option[LocalDateTime] = None

  def totalAmount: Int = items.values.map(_.amount).sum

  def changeRecipient(r: Option[String]): Unit = {
    require(sent.isEmpty)
    record(InvoiceRecipientChanged(r))
  }

  def addItem(item: InvoiceItem): Unit = {
    require(sent.isEmpty)
    record(InvoiceItemAdded(item))
  }

  def removeItem(item: Int): Unit = {
    require(sent.isEmpty)
    record(InvoiceItemRemoved(item))
  }

  def send(): Unit = {
    record(InvoiceSent(LocalDateTime.now))
  }

  def remind(): Unit = {
    require(sent.isDefined)
    record(InvoiceReminded(LocalDateTime.now))
  }

  def paymentReceived(when: LocalDateTime): Unit = {
    require(paid.isDefined)
    record(InvoicePaymentReceived(LocalDateTime.now))
  }

  protected val applyEvent: InvoiceEvent ⇒ Unit = {
    case InvoiceCreated(invoiceId) ⇒ Unit
    case InvoiceItemAdded(item) ⇒ items += item.id → item
    case InvoiceItemRemoved(itemId) ⇒ items -= itemId
    case InvoiceRecipientChanged(r) ⇒ recipient = r
    case InvoiceSent(d) ⇒ sent = Some(d)
    case InvoiceReminded(d) ⇒ reminded = Some(d)
    case InvoicePaymentReceived(d) ⇒ paid = Some(d)
  }
}
