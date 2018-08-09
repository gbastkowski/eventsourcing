package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import scala.collection.mutable

case class Invoice(
  id: Option[Int] = None) {
  val items: mutable.ListBuffer[InvoiceItem] = mutable.ListBuffer()
  private[this] var totalAmount: Int = 0
  var recipient: Option[String] = None
  var sent: Option[LocalDateTime] = None
  var reminded: Option[LocalDateTime] = None
  var paymentReceived: Option[LocalDateTime] = None

  def changeRecipient(recipient: Option[String]): Invoice = {
    this.recipient = recipient
    this
  }

  def addItem(description: String, amount: Int): Invoice = {
    require(sent.isEmpty)

    items += new InvoiceItem(description, amount)
    totalAmount += amount
    this
  }

  def removeItem(item: InvoiceItem): Invoice = {
    require(sent.isEmpty)

    items -= item
    totalAmount -= item.amount
    this
  }

  def send(): Invoice = {
    sent = Some(LocalDateTime.now())
    this
  }

  def remind(): Invoice = {
    reminded = Some(LocalDateTime.now())
    this
  }

  def paymentReceived(when: LocalDateTime): Invoice = {
    paymentReceived = Some(when)
    this
  }
}
