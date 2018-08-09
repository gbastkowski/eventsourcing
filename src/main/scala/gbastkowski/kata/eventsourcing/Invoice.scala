package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import scala.collection.mutable

case class Invoice(id: Option[Int] = None) {
  val items: mutable.ListBuffer[InvoiceItem] = mutable.ListBuffer()
  private[this] var totalAmount: Int = 0
  var recipient: Option[String] = None
  var sent: Option[LocalDateTime] = None
  var reminded: Option[LocalDateTime] = None
  var paymentReceived: Option[LocalDateTime] = None

  def changeRecipient(recipient: Option[String]): Unit = {
    this.recipient = recipient
  }

  def addItem(description: String, amount: Int): Unit = {
    require(sent.isEmpty)

    items += new InvoiceItem(description, amount)
    totalAmount += amount
  }

  def removeItem(item: InvoiceItem): Unit = {
    require(sent.isEmpty)

    items -= item
    totalAmount -= item.amount
  }

  def send(): Unit = {
    sent = Some(LocalDateTime.now())
  }

  def remind(): Unit = {
    reminded = Some(LocalDateTime.now())
  }

  def paymentReceived(when: LocalDateTime): Unit = {
    paymentReceived = Some(when)
  }
}
