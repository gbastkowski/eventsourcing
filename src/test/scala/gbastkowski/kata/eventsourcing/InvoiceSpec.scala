package gbastkowski.kata.eventsourcing

import org.scalatest._

class InvoiceSpec extends FreeSpec with Matchers {
  "An invoice" - {
    "can be created" in {
      new Invoice()
    }
    "can add an item" in {
      val invoice = new Invoice()
      invoice.addItem("Coke", 1)
      invoice.items should have size 1
    }
    "can change it's recipient" in {
      val invoice = new Invoice()
      invoice.changeRecipient("Recipient")
  }
}
