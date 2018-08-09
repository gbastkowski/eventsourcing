package gbastkowski.kata.eventsourcing

import scala.collection.mutable

class Invoice {
  var id: Option[Int] = None
  val recipients: mutable.ListBuffer[String] = mutable.ListBuffer()

  def addRecipient(recipient: String): Unit = {
    recipients += recipient
  }

}
