package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import scala.collection.mutable

case class Invoice(
  id: Option[Int] = None,
  recipient: Option[String] = None,
  items: List[InvoiceItem] = Nil,
  sent: Option[LocalDateTime] = None,
  reminded: Option[LocalDateTime] = None,
  paymentReceived: Option[LocalDateTime] = None)
{
  def totalAmount: Int = items.map(_.amount).sum

  def changeRecipient(recipient: Option[String]): Invoice = {
    require(sent.isEmpty)
    Invoice(id, recipient, items, sent, reminded, paymentReceived)
  }

  def addItem(item: InvoiceItem): Invoice = {
    require(sent.isEmpty)
    Invoice(id, recipient, item :: items, sent, reminded, paymentReceived)
  }

  def removeItem(item: InvoiceItem): Invoice = {
    require(sent.isEmpty)
    Invoice(id, recipient, items.filterNot(_ == item), sent, reminded, paymentReceived)
  }

  def send(): Invoice = Invoice(id, recipient, items, Some(LocalDateTime.now()), reminded, paymentReceived)

  def remind(): Invoice = {
    Invoice(id, recipient, items, sent, Some(LocalDateTime.now()), paymentReceived)
  }

  def paymentReceived(when: LocalDateTime): Invoice = {
    require(sent.isDefined)
    Invoice(id, recipient, items, sent, reminded, Some(when))
  }
}
