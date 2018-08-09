package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import scala.collection.mutable

class Invoice {
  var id: Option[Int] = None
  val items: mutable.ListBuffer[InvoiceItem] = mutable.ListBuffer()
  var recipient: Option[String] = None
  var sent: Option[LocalDateTime] = None
  var reminded: Option[LocalDateTime] = None

  def changeRecipient(recipient: Option[String]): Unit = {
    this.recipient = recipient
  }

  def addItem(description: String, amount: Int): Unit = {
    items += new InvoiceItem(description, amount)
  }

  def removeItem(item: InvoiceItem): Unit = {
    items -= item
  }

  def send(): Unit = {
    sent = Some(LocalDateTime.now())
  }

  def remind(): Unit = {
    reminded = Some(LocalDateTime.now())
  }
}
